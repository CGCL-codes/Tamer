package de.xore.idea.plugins.tasks;

import java.text.DateFormat;
import java.util.Date;

/**
 *
 * Diese Klasse repr�sentiert einen tats�chlichen Task.
 *
 * Created by Johannes Schneider shake@web.de
 * User: Shake
 * Date: 21.05.2002
 * Time: 23:31:13
 * ++++++++++++++++++
 * $Log: Task.java,v $
 * Revision 1.2  2002/06/01 17:59:55  stuartharper
 * Made slight change to getExpirationDate()
 *
 * Revision 1.1  2002/05/23 22:04:17  shake234
 * *** empty log message ***
 *
 * ++++++++++++++++++
 */
public class Task {

    private String name;

    private String description;

    private Date buildDate;

    private Date expirationDate;

    private int priority;

    public Task(String name) {
        this.name = name;
    }

    public Date getBuildDate() {
        return buildDate;
    }

    public void setBuildDate(Date buildDate) {
        this.buildDate = buildDate;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Date getExpirationDate() {
        return expirationDate;
    }

    public void setExpirationDate(Date expirationDate) {
        this.expirationDate = expirationDate;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getPriority() {
        return priority;
    }

    public void setPriority(int priority) {
        this.priority = priority;
    }

    public Object getPriorityFormatted() {
        return new Integer(getPriority());
    }

    public String getExpirationDateFormatted() {
        Date expDate = getExpirationDate();
        String expirationDate;
        if (expDate == null) {
            expirationDate = new String("");
        } else {
            expirationDate = DateFormat.getDateInstance().format(expDate);
        }
        return expirationDate;
    }
}
