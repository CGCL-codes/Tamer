package alvis.newarch;

import java.util.ArrayList;
import java.util.List;

/**
 * A Year.
 * 
 * @author grousset
 */
public class Year {

    /** The year label */
    public String label;

    /** the months */
    public List<Month> months = new ArrayList<Month>();

    public Year(String label) {
        this.label = label;
    }

    /**
   * Get a month by its label.
   * 
   * @param monthLabel the month label.
   * @return the Month or null if not found.
   */
    public Month getMonth(String monthLabel) {
        Month month = null;
        if (monthLabel != null) for (Month aMonth : months) if (aMonth.label.equals(monthLabel)) month = aMonth;
        return month;
    }

    public void addMonth(Month month) {
        months.add(month);
    }
}
