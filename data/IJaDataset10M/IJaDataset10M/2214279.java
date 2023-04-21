package com.mainatom.rtx;

import java.util.*;

/**
 * Сравнение Rtx по имени
 */
class ComparatorName implements Comparator<Rtx> {

    public int compare(Rtx o1, Rtx o2) {
        return o1.getName().compareTo(o2.getName());
    }
}
