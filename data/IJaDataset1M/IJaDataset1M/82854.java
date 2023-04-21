package de.jaret.util.ui.timebars.model;

import java.util.ArrayList;
import java.util.List;

/**
 * A default implementation of the TimeBarModel interface. Extends the AbstractTimeBarModel.
 * 
 * @author Peter Kliem
 * @version $Id: DefaultTimeBarModel.java 886 2009-10-08 22:08:27Z kliem $
 */
public class DefaultTimeBarModel extends AbstractTimeBarModel {

    /** list of the rows. */
    protected List<TimeBarRow> _rows = new ArrayList<TimeBarRow>();

    /**
     * {@inheritDoc}
     */
    public TimeBarRow getRow(int row) {
        return (TimeBarRow) _rows.get(row);
    }

    /**
     * {@inheritDoc}
     */
    public int getRowCount() {
        return _rows.size();
    }

    /**
     * Add a row.
     * 
     * @param row row to add.
     */
    public void addRow(TimeBarRow row) {
        addRow(-1, row);
    }

    /**
     * Add a row.
     * 
     * @param index index the row should be inserted. -1 marks append to the end.
     * @param row row to add.
     */
    public void addRow(int index, TimeBarRow row) {
        if (index == -1) {
            _rows.add(row);
        } else {
            _rows.add(index, row);
        }
        if (_minDate == null) {
            _minDate = row.getMinDate();
            _maxDate = row.getMaxDate();
        } else if (row.getMinDate() != null && row.getMaxDate() != null) {
            if (_minDate.compareTo(row.getMinDate()) > 0) {
                _minDate = row.getMinDate();
            }
            if (_maxDate.compareTo(row.getMaxDate()) < 0) {
                _maxDate = row.getMaxDate();
            }
        }
        row.addTimeBarRowListener(this);
        fireRowAdded(row);
    }

    /**
     * Remove a row from the model.
     * 
     * @param row row to remove
     */
    public void remRow(TimeBarRow row) {
        if (_rows.contains(row)) {
            row.remTimeBarRowListener(this);
            _rows.remove(row);
            updateMinMax();
            fireRowRemoved(row);
        }
    }

    /**
     * Retrieve the model index of a given row.
     * 
     * @param row row to check
     * @return index or -1
     */
    public int getIndexForRow(TimeBarRow row) {
        return _rows.indexOf(row);
    }
}
