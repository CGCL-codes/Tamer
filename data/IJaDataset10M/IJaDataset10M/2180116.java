package david.free.lottery;

import java.util.*;
import david.util.Common;

public class DrawDateComparator implements Comparator {

    public int compare(Object o1, Object o2) {
        Number n1 = (Number) o1;
        Number n2 = (Number) o2;
        Date d1 = Common.getInstance().getDateObject(n1.getDrawingDateString());
        Date d2 = Common.getInstance().getDateObject(n2.getDrawingDateString());
        return (int) (d2.getTime() - d1.getTime());
    }
}
