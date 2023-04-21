package org.butu.utils;

import java.util.Set;

public class SetUtils {

    /**
     * @return true, ���� ��� ��������� ����� �������� �����������
     */
    public static <E> boolean hasIntersection(Set<E> set1, Set<E> set2) {
        for (E elem : set2) if (set1.contains(elem)) return true;
        return false;
    }
}
