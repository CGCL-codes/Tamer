package org.dict.kernel;

/**
 * Insert the type's description here.
 * Creation date: (11.08.01 10:03:22)
 * @author: Administrator
 */
public interface IComparator {

    /**
 * Insert the method's description here.
 * Creation date: (11.08.01 10:06:35)
 * @return int
 * @param o1 java.lang.Object
 * @param o2 java.lang.Object
 */
    int compare(Object o1, Object o2);

    String getComparableKey(String key);
}
