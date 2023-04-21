package org.systemsbiology.apps.gui.domain;

import java.io.Serializable;

/**
 * Serializable and Persistent Object representing status of a single stage in the ATAQS pipeline
 * <p>
 * Copyright (C) 2010 by Institute for Systems Biology,
 * Seattle, Washington, USA.  All rights reserved.
 *
 * This source code is distributed under the GNU Lesser
 * General Public License, the text of which is available at:
 *   http://www.gnu.org/copyleft/lesser.html
 *
 * @see ATAQSProject 
 */
public class Status implements Serializable {

    /**
	 * 
	 */
    private static final long serialVersionUID = 3787834150094648253L;

    /**
	 * 
	 */
    public static Status NOTSTARTED = new Status(1, "Not Started");

    /**
	 * 
	 */
    public static Status PENDING = new Status(2, "Pending");

    /**
	 * 
	 */
    public static Status RUNNING = new Status(3, "Running");

    /**
	 * 
	 */
    public static Status DONE = new Status(4, "Done");

    /**
	 * 
	 */
    public static Status FAILED = new Status(5, "Failed");

    /**
	 * 
	 */
    public static Status CANCELED = new Status(6, "Canceled");

    /**
	 * Executor has finished, but the gui does not have the data
	 */
    public static Status GUI_NEEDS_RESULTS = new Status(7, "GUI Needs Results");

    /**
	 * Intermediate state between saving and executing.  
	 */
    public static Status IN_TRANSITION = new Status(8, "In Transition");

    /**
	 * 
	 */
    public static Status UNKNOWN = new Status(9, "Unknown");

    private int id;

    private String statusString = "Not Started";

    /**
	 * Public, default constructor required by GWT serializable objects.
	 * Invoking this constructor will create a new status object that
	 * equals(NOTSTARTED).
	 */
    public Status() {
    }

    private Status(int id, String asString) {
        this.id = id;
        this.statusString = asString;
    }

    /**
	 * @return id
	 */
    public int getId() {
        return id;
    }

    /**
	 * Get identifier
	 * @param id identifier
	 */
    public void setId(int id) {
        this.id = id;
    }

    /**
	 * Get status string
	 * @return status string
	 */
    public String getStatusString() {
        return statusString;
    }

    /**
	 * Set status string
	 * @param statusString status string
	 */
    public void setStatusString(String statusString) {
        this.statusString = statusString;
    }

    /**
	 * Returns the string representation of the status object.
	 */
    public String toString() {
        return statusString;
    }

    /**
	 * Check to see if the status is not started
	 * @return <code>true</code> if in the not started state
	 */
    public boolean notStarted() {
        return this.equals(NOTSTARTED);
    }

    /**
	 * Check to see if the status is pending
	 * @return <code>true</code> if in the pending state
	 */
    public boolean pending() {
        return this.equals(PENDING);
    }

    /**
	 * Check to see if the status is running
	 * @return <code>true</code> if in the running state
	 */
    public boolean running() {
        return this.equals(RUNNING);
    }

    /**
	 * Check to see if the status is done
	 * @return <code>true</code> if in the done state
	 */
    public boolean done() {
        return this.equals(DONE);
    }

    /**
	 * Check to see if the status is ready to retrieve results
	 * @return <code>true</code> if in the needs_results state
	 */
    public boolean gui_needs_results() {
        return this.equals(GUI_NEEDS_RESULTS);
    }

    /**
	 * Check to see if the status is between saved and executing
	 * @return <code>true</code> if in the needs_results state
	 */
    public boolean in_transition() {
        return this.equals(IN_TRANSITION);
    }

    /**
	 * Check to see if the status is failed
	 * @return <code>true</code> if in the failed state
	 */
    public boolean failed() {
        return this.equals(FAILED);
    }

    /**
	 * Check to see if the status is canceled
	 * @return <code>true</code> if in the canceled state
	 */
    public boolean canceled() {
        return this.equals(CANCELED);
    }

    public boolean equals(Object status) {
        if (status instanceof Status) {
            return ((Status) status).statusString.equals(this.statusString);
        } else return false;
    }

    public int hashCode() {
        return statusString.hashCode();
    }

    /**
	 * Returns Status.NOTSTARTED
	 * @return Status
	 */
    public static Status defaultStatus() {
        return NOTSTARTED;
    }

    /**
	 * Get status at index
	 * @param i index 
	 * @return status
	 */
    public static Status getStatus(int i) {
        switch(i) {
            case 1:
                return NOTSTARTED;
            case 2:
                return PENDING;
            case 3:
                return RUNNING;
            case 4:
                return DONE;
            case 5:
                return FAILED;
            case 6:
                return CANCELED;
            case 7:
                return GUI_NEEDS_RESULTS;
            case 8:
                return IN_TRANSITION;
            default:
                return UNKNOWN;
        }
    }
}
