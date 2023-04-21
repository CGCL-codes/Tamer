package org.horen.ui.editors.comparators;

import java.util.Comparator;
import java.util.Date;
import org.horen.ui.util.DateTimeHelper;

public class DateComparator implements Comparator<Object> {

    @Override
    public int compare(Object o1, Object o2) {
        if (o1 instanceof Date && o2 instanceof Date) {
            return DateTimeHelper.compare((Date) o1, (Date) o2);
        }
        return 0;
    }
}
