package org.zkforge.zktodo2;

import java.util.ArrayList;
import java.util.List;

import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.NotifyChange;

public class ZkToDoViewModel  {

	protected ReminderService reminderService; 
	
	public void setReminderService(ReminderService reminderService) {
		this.reminderService = reminderService;
	}

	protected List<Reminder> reminders = new ArrayList<Reminder>();
	
	public List<Reminder> getReminders() {
		List<Reminder> rs = this.reminderService.findAll();
		this.reminders.clear();
		this.reminders.addAll(rs);
		return this.reminders;
	}

	protected Reminder selectedReminder = new Reminder();

	public Reminder getSelectedReminder() {
		return this.selectedReminder;
	}
	
	@NotifyChange
	public void setSelectedReminder(Reminder reminder) {
		this.selectedReminder = reminder;
	}
	
	@Command
	@NotifyChange({"reminders","selectedReminder"})
	public void delete() {
		if( this.selectedReminder.getId() != null ){
			try {
				this.reminderService.delete(selectedReminder);
				this.selectedReminder = new Reminder();
			} catch (EntityNotFoundException e) {
				// no doubt someone else deleted it at the same time. nothing to do. 
			}
		}
	}

	@Command
	@NotifyChange({"reminders","selectedReminder"})
	public void save() {
		if( this.selectedReminder.getId() != null ){
			try {
				this.reminderService.merge(selectedReminder);
			} catch (EntityNotFoundException e) {
				e.printStackTrace(); // hum. some else deleted this. should really send this up to the user and ask them to reload the page. 
			}
		} else {
			this.reminderService.persist(this.selectedReminder);
		}
	}
	
	@Command
	@NotifyChange({"reminders","selectedReminder"})
	public void create() {
		this.selectedReminder = new Reminder();
	}

}
