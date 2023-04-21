package com.incendiaryblue.awt.calendar;

public class GhostCalendarRow extends CalendarRow {

    private CalendarRow linkedRow;

    public GhostCalendarRow(CalendarRow linkedRow) {
        this.linkedRow = linkedRow;
    }

    int getRowType() {
        return ROW_TYPE_GHOST;
    }

    public String getLabel() {
        return "";
    }

    /**
	 *	@return	The row level object associated with this calendar row.
	 */
    public RowLevel getLevel() {
        return this.linkedRow.getLevel();
    }

    public CalendarRow getParent() {
        return this.linkedRow.getParent();
    }
}
