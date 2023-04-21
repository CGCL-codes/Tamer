package com.gcalsync.component;

import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Vector;
import javax.microedition.lcdui.*;
import javax.microedition.pim.Event;
import javax.microedition.pim.PIMException;
import com.gcalsync.option.Options;
import com.gcalsync.store.Store;
import com.gcalsync.cal.gcal.GCalClient;
import com.gcalsync.cal.gcal.GCalEvent;
import com.gcalsync.cal.phonecal.PhoneCalClient;
import com.gcalsync.util.*;
import com.gcalsync.log.*;
import com.gcalsync.cal.*;

/**
 *
 * @author 
 */
public class CommitComponent extends MVCComponent implements Runnable, StatusLogger {

    Form form;

    GCalClient gCalClient;

    PhoneCalClient phoneCalClient;

    GCalEvent[] uploads;

    GCalEvent[] downloads;

    /**
    * Constructor
	*/
    public CommitComponent(GCalClient gcal) {
        gCalClient = gcal;
    }

    /**
    * Gets the <code>Displayable</code> object to be displayed for
    * this component
    *
    * @returns <code>Displayable</code>
	*/
    public Displayable getDisplayable() {
        return this.form;
    }

    /**
	* Initializes the model before creating the view
	*/
    protected void initModel() {
    }

    /**
	* Updates the view after it is created
	*/
    protected void updateView() {
    }

    /**
    * Creates the view
	*/
    protected void createView() {
        this.form = new Form("Commit");
        this.form.addCommand(new Command("OK", Command.EXIT, 2));
    }

    /**
    * Appends a message to the current form
    *
    * @param message to be displayed
	*/
    public void update(String message) {
        form.append(message + "\n");
    }

    /**
    * Replaces the last message on the form with a new message
    *
    * @param message to be displayed
	*/
    public void updateMinor(String message) {
        form.delete(form.size() - 1);
        form.append(message + "\n");
    }

    /**
    * Processes menu commands
    *
    * @param c command to execute
    * @param d <code>Displayable</code> from which the command
    *          originates
	*/
    public void commandAction(Command c, Displayable d) {
        if (c.getCommandType() == Command.EXIT) {
            Components.login.showScreen();
        }
    }

    /**
    * Updates screen and begins processing events to be
    * downloaded/uploaded
	*/
    public void handle() {
        showScreen();
        new Thread(this).start();
    }

    /**
    * Entry point for new thread
	*/
    public void run() {
        try {
            phoneCalClient = new PhoneCalClient();
            processUploadEvents();
            processDownloadEvents();
            Store.getTimestamps().lastSync = System.currentTimeMillis();
            Store.saveTimestamps();
            this.form.append(new Spacer(getDisplayable().getWidth(), 20));
            update("GCal:  " + gCalClient.createdCount + " new, " + gCalClient.updatedCount + " updated, " + gCalClient.removedCount + " removed events");
            update("Phone: " + phoneCalClient.createdCount + " new, " + phoneCalClient.updatedCount + " updated, " + phoneCalClient.removedCount + " removed events");
            phoneCalClient.close();
        } catch (Exception e) {
            ErrorHandler.showError(e.getMessage(), e);
        }
    }

    /**
    * Uploads events to Google Calendar
	*/
    void processUploadEvents() {
        String gCalId;
        try {
            if (this.uploads != null && this.uploads.length > 0) {
                gCalClient.setForm(this.form);
                for (int i = 0; i < this.uploads.length; i++) {
                    gCalId = gCalClient.createEvent(this.uploads[i]);
                }
            }
        } catch (Exception e) {
            update("Failed Google update: " + e);
            System.err.println("Failed Google update: " + e);
        }
    }

    /**
    * Saves events to phone calendar
	*/
    void processDownloadEvents() {
        Options options;
        Merger merger;
        Hashtable phoneEventsByGcalId;
        Enumeration phoneEvents;
        Event phoneEvent;
        String gcalId;
        String startDate;
        String endDate;
        long now;
        long startDateLong;
        long endDateLong;
        try {
            if (this.downloads != null && this.downloads.length > 0) {
                options = Store.getOptions();
                now = System.currentTimeMillis();
                startDate = DateUtil.longToIsoDate(now, 0 - options.pastDays);
                endDate = DateUtil.longToIsoDate(now, options.futureDays);
                startDateLong = DateUtil.isoDateToLong(startDate);
                endDateLong = DateUtil.isoDateToLong(endDate) + DateUtil.DAY;
                phoneEvents = phoneCalClient.getPhoneEvents(startDateLong, endDateLong);
                phoneEventsByGcalId = new Hashtable();
                merger = new Merger(phoneCalClient, gCalClient);
                while (phoneEvents.hasMoreElements()) {
                    phoneEvent = (Event) phoneEvents.nextElement();
                    gcalId = phoneCalClient.getGCalId(phoneEvent);
                    if (gcalId != null) phoneEventsByGcalId.put(gcalId, phoneEvent);
                }
                for (int i = 0; i < this.downloads.length; i++) {
                    phoneEvent = (Event) phoneEventsByGcalId.remove(this.downloads[i].id);
                    merger.mergeEvents(phoneEvent, this.downloads[i], this.form);
                }
            }
        } catch (Exception e) {
            update("Failed phone update: " + e);
            System.err.println("Failed phone update: " + e);
        }
    }

    /**
    * Sets the events to be uploaded/downloaded
	*/
    public void setEvents(GCalEvent[] uploads, GCalEvent[] downloads) {
        if (uploads != null) {
            this.uploads = new GCalEvent[uploads.length];
            System.arraycopy(uploads, 0, this.uploads, 0, uploads.length);
        }
        if (downloads != null) {
            this.downloads = new GCalEvent[downloads.length];
            System.arraycopy(downloads, 0, this.downloads, 0, downloads.length);
        }
    }
}
