package com.visionarysoftware.grasp.app;

/**
 * Storage object for the company information values
 *
 * @author Patrick Janidlo
 * @version $Revision: 1.4 $ $Date: 2002/02/13 20:37:23 $
 */
public class CompanyValue implements java.io.Serializable {

    private String fullName;

    private long id;

    private String template;

    private boolean hasVacation;

    private boolean hasSick;

    private boolean hasHolidays;

    private boolean hasFloating;

    private boolean hasPersonal;

    /**
     * company storage
     * @param id        database id
     * @param fullName  the full name of the company
     * @param template  which template to use for the company
     */
    public CompanyValue(long id, String fullName, String template) {
        this.id = id;
        this.fullName = fullName;
        this.template = template;
        this.hasVacation = false;
        this.hasSick = false;
        this.hasHolidays = false;
        this.hasFloating = false;
        this.hasPersonal = false;
    }

    public long getID() {
        return id;
    }

    public String getName() {
        return fullName;
    }

    public String getTemplate() {
        return template;
    }

    public boolean getHasVacation() {
        return this.hasVacation;
    }

    public boolean getHasSick() {
        return this.hasSick;
    }

    public boolean getHasHolidays() {
        return this.hasHolidays;
    }

    public boolean getHasFloating() {
        return this.hasFloating;
    }

    public boolean getHasPersonal() {
        return this.hasPersonal;
    }

    public void setHasVacation(boolean b) {
        this.hasVacation = b;
    }

    public void setHasSick(boolean b) {
        this.hasSick = b;
    }

    public void setHasHolidays(boolean b) {
        this.hasHolidays = b;
    }

    public void setHasFloating(boolean b) {
        this.hasFloating = b;
    }

    public void setHasPersonal(boolean b) {
        this.hasPersonal = b;
    }
}
