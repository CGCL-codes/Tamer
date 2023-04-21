package utils;

import components.TextImageObj;
import java.util.Comparator;

/**
 * Comparer
 * @author pmchanh
 */
public class Comparer implements Comparator {

    /**
     * Compare
     */
    public int compare(Object o1, Object o2) {
        Object[] item1 = (Object[]) o1;
        Object[] item2 = (Object[]) o2;
        TextImageObj name1 = (TextImageObj) item1[0];
        TextImageObj name2 = (TextImageObj) item2[0];
        return name1.getText().compareToIgnoreCase(name2.getText());
    }
}
