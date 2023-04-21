package de.ifu.arbeitsgruppenassistent.client.planning.productiontable;

import java.util.Date;
import de.ifu.arbeitsgruppenassistent.client.planning.utils.calendar.CalendarView;

public interface Builder {

    /** Calculate the blocks that should be displayed in the weekview.
    * This method should not be called manually.
    * It is called by the CalendarView during the build process.
    * @see #build
    * @param start
    * @param end
    */
    void prepareBuild(Date start, Date end);

    /** The maximal ending-hour of all blocks that should be displayed.
     Call prepareBuild first to calculate the blocks.*/
    int getMax();

    /** The minimal starting-hour of all blocks that should be displayed.
     Call prepareBuild first to calculate the blocks.*/
    int getMin();

    /** Build the calculated blocks into the jobview. This method should not be called manually.
     * It is called by the CalendarView during the build process.
     * @see #prepareBuild */
    void build(CalendarView cv);

    void setEnabled(boolean enable);

    boolean isEnabled();
}
