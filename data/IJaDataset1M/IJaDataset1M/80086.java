package org.columba.calendar.model.api;

import java.util.Calendar;

public interface ITodoInfo extends IComponentInfo {

    /**
	 * @return Returns the dtStart.
	 */
    public abstract Calendar getDtStart();

    public abstract Calendar getDue();

    /**
	 * @return Returns the summary.
	 */
    public abstract String getSummary();
}
