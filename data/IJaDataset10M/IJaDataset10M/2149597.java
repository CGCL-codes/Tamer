package gnu.kinsight.timeline;

import gnu.kinsight.Date;

public class Event implements Comparable {

    public static final int BIRTH_BOY = 0;

    public static final int BIRTH_GIRL = 1;

    public static final int MARRIAGE = 2;

    public static final int DEATH = 3;

    public static final int OTHER = 4;

    public static final int IGNORE = 5;

    private Date _date;

    private Date _endDate;

    private String _description;

    private String _detail;

    private int _type;

    public Event() {
        this(null, "", "", OTHER);
    }

    public Event(Date date, String desc, String detail) {
        this(date, desc, detail, OTHER);
    }

    public Event(Date date, String desc, String detail, int type) {
        this(date, null, desc, detail, type);
    }

    public Event(Date date, Date endDate, String desc, String detail, int type) {
        _date = date;
        _endDate = endDate;
        _description = desc;
        _detail = detail;
        _type = type;
    }

    public final int compareTo(final Object object) {
        Date d = ((Event) object).getDate();
        if (d == null) {
            return -1;
        }
        if (d.before(_date)) {
            return 1;
        } else if (d.after(_date)) {
            return -1;
        } else {
            return 0;
        }
    }

    public boolean isRange() {
        return _endDate != null && !_endDate.isEmpty();
    }

    /**
     * Gets the value of type
     *
     * @return the value of type
     */
    public final int getType() {
        return _type;
    }

    /**
     * Sets the value of type
     *
     * @param argType Value to assign to this.type
     */
    public final void setType(final int argType) {
        _type = argType;
    }

    /**
     * Gets the value of date
     *
     * @return the value of date
     */
    public final Date getDate() {
        return _date;
    }

    /**
     * Sets the value of date
     *
     * @param argDate Value to assign to _date
     */
    public final void setDate(final Date argDate) {
        _date = argDate;
    }

    /**
     * Gets the value of endDate
     *
     * @return the value of endDate
     */
    public final Date getEndDate() {
        return this._endDate;
    }

    /**
     * Sets the value of endDate
     *
     * @param argEndDate Value to assign to this.endDate
     */
    public final void setEndDate(final Date argEndDate) {
        this._endDate = argEndDate;
    }

    /**
     * Gets the value of description
     *
     * @return the value of description
     */
    public final String getDescription() {
        return _description;
    }

    /**
     * Sets the value of description
     *
     * @param argDescription Value to assign to _description
     */
    public final void setDescription(final String argDescription) {
        _description = argDescription;
    }

    /**
     * Gets the value of detail
     *
     * @return the value of detail
     */
    public final String getDetail() {
        return _detail;
    }

    /**
     * Sets the value of detail
     *
     * @param argDetail Value to assign to _detail
     */
    public final void setDetail(final String argDetail) {
        _detail = argDetail;
    }
}
