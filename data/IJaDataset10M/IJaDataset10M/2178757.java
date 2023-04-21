package org.makados.web.beans;

/**
 * @author makados
 */
public class ImsServiceStatBean {

    private long allCount;

    private long monthCount;

    private long weekCount;

    private long dayCount;

    public long getAllCount() {
        return allCount;
    }

    public void setAllCount(long allCount) {
        this.allCount = allCount;
    }

    public long getDayCount() {
        return dayCount;
    }

    public void setDayCount(long dayCount) {
        this.dayCount = dayCount;
    }

    public long getMonthCount() {
        return monthCount;
    }

    public void setMonthCount(long monthCount) {
        this.monthCount = monthCount;
    }

    public long getWeekCount() {
        return weekCount;
    }

    public void setWeekCount(long weekCount) {
        this.weekCount = weekCount;
    }
}
