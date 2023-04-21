package com.jujunie.integration.bugzilla.dao;

import java.util.Date;
import com.jujunie.integration.bugzilla.BugVO;
import com.jujunie.integration.calendar.ICSEntry;

/**
 * Thius is an adapter to ICS entry using a BugVO and using bug title and Bug deadline
 * @author julien
 *
 */
public class BugVOToICSAdapter implements ICSEntry {

    /** The BugVO adapted */
    private BugVO bug = null;

    public BugVOToICSAdapter(BugVO toAdapt) {
        assert toAdapt != null : "Cannot addapt a null BugVO to ICS entry!";
        this.bug = toAdapt;
    }

    /**
     * @see com.jujunie.integration.calendar.ICSEntry#getClazz()
     */
    public Clazz getClazz() {
        return Clazz.PUBLIC;
    }

    /**
     * @see com.jujunie.integration.calendar.ICSEntry#getEnd()
     */
    public Date getEnd() {
        return this.bug.getDeadline();
    }

    /**
     * @see com.jujunie.integration.calendar.ICSEntry#getStart()
     */
    public Date getStart() {
        return this.bug.getDeadline();
    }

    /**
     * @see com.jujunie.integration.calendar.ICSEntry#getStatus()
     */
    public Status getStatus() {
        return Status.CONFIRMED;
    }

    /**
     * @see com.jujunie.integration.calendar.ICSEntry#getSummary()
     */
    public String getSummary() {
        StringBuffer res = new StringBuffer();
        res.append('[').append(this.bug.getId()).append("] ").append(this.bug.getTitle());
        if (this.bug.isStatusWhiteboard()) {
            res.append(" [").append(this.bug.getStatusWhiteboard()).append(']');
        }
        return res.toString();
    }

    /**
     * @see com.jujunie.integration.calendar.ICSEntry#isFullDay()
     */
    public boolean isFullDay() {
        return true;
    }

    /**
     * @see com.jujunie.integration.calendar.ICSEntry#getType()
     */
    public Type getType() {
        if (this.bug.isDeadline()) {
            return Type.EVENT;
        } else {
            return Type.TODO;
        }
    }

    public String getId() {
        return String.valueOf(this.bug.getId());
    }
}
