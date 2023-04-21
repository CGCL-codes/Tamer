package com.starjar.swing.chronology;

import org.joda.time.base.AbstractInterval;

/**
 * A cell in the calenday layout.
 * For example when the layout is a year view, a cell represents a day of the 
 * year.
 *
 * @author peter
 */
public interface Cell {

    /**
     * The time interval this cell occupies.
     *
     * @return an interval Or null if this cell is not linked in time.
     */
    public AbstractInterval getInterval();

    /**
     * The text inside the cell
     *
     * @return
     */
    public String getText();

    /**
     * The tool tip text for this cell.
     *
     * @return
     */
    public String getToolTipText();
}
