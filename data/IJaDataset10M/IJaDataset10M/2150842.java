package org.horen.ui.editors.columns;

import java.util.Date;
import org.horen.task.Task;

/**
 * Provides the finish time of a task.
 * 
 * @author Steffen
 */
public class FinishTimeColumnProvider extends TaskColumnProvider {

    public static final String COLUMN_ID = "Column.FinishTime";

    /**
	 * (see super class)
	 */
    @Override
    public String getColumnID() {
        return COLUMN_ID;
    }

    /**
	 * (see class description)
	 */
    @Override
    public Object getValue(Object element) {
        if (element instanceof Task) {
            Date date = ((Task) element).getFinishTs();
            return date;
        }
        return null;
    }

    /**
	 * Always returns a <code>Date</code> as value.
	 */
    @Override
    public Class<?> getValueClass() {
        return Date.class;
    }
}
