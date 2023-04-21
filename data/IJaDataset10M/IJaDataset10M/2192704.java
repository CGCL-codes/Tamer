package net.sourceforge.appgen.model;

import java.util.Comparator;

/**
 * @author Byeongkil Woo
 */
public class PrimaryKeyFieldComparator implements Comparator<Field> {

    public int compare(Field o1, Field o2) {
        if (o1.getPkPosition() <= 0 && o2.getPkPosition() <= 0) {
            return 0;
        }
        if (o1.getPkPosition() <= 0 && o2.getPkPosition() > 0) {
            return 1;
        }
        if (o1.getPkPosition() >= 0 && o2.getPkPosition() <= 0) {
            return -1;
        }
        return o1.getPkPosition() - o2.getPkPosition();
    }
}
