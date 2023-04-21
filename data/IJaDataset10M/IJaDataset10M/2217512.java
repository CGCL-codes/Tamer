package de.objectcode.time4u.store.db.access;

import de.objectcode.time4u.store.Day;

public class DirtyDayData {

    private long m_id;

    private long m_dayInfoId;

    private long m_syncTargetId;

    private Day m_day;

    public long getDayInfoId() {
        return m_dayInfoId;
    }

    public void setDayInfoId(long dayInfoId) {
        m_dayInfoId = dayInfoId;
    }

    public long getId() {
        return m_id;
    }

    public void setId(long id) {
        m_id = id;
    }

    public long getSyncTargetId() {
        return m_syncTargetId;
    }

    public void setSyncTargetId(long syncTargetId) {
        m_syncTargetId = syncTargetId;
    }

    public Day getDay() {
        return m_day;
    }

    public void setDay(Day day) {
        m_day = day;
    }
}
