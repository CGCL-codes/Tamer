package org.aigebi.rbac.util;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Ligong Xu
 * @version $Id: CommonUtils.java 1 2007-09-22 18:10:03Z ligongx $
 */
public class ListUtil {

    public ListUtil() {
    }

    /**Return list with elements found in firstList but not found in second list. */
    public static List<Long> notInSecondList(List<Long> firstList, List<Long> secondList) {
        if (firstList == null || firstList.isEmpty()) return null;
        if (secondList == null || secondList.isEmpty()) return firstList;
        List<Long> list = new ArrayList<Long>();
        for (Long id : firstList) {
            if (!secondList.contains(id)) list.add(id);
        }
        return list;
    }

    /**Return first list elements found in second list. */
    public static List<Long> inSecondList(List<Long> firstList, List<Long> secondList) {
        if (firstList == null || firstList.isEmpty()) return null;
        if (secondList == null || secondList.isEmpty()) return null;
        List<Long> list = new ArrayList<Long>();
        for (Long id : firstList) {
            if (secondList.contains(id)) list.add(id);
        }
        return list;
    }

    public static <T> List<T> createList(T... args) {
        List<T> l = new ArrayList<T>();
        for (T arg : args) {
            l.add(arg);
        }
        return l;
    }
}
