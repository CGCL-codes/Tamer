package chapter2;

import java.util.ArrayList;
import java.util.List;

public class Exercise_2_4_3_OrderedArrray<E extends Comparable<E>> extends Exercise_2_4_3_Base<E> {

    List<E> list;

    public Exercise_2_4_3_OrderedArrray() {
        list = new ArrayList<E>();
    }

    public void insert(E item) {
        int i = 0;
        for (E e : list) {
            i++;
            if (e.compareTo(item) > 0) break;
        }
        list.add(i, item);
    }

    public boolean isEmpty() {
        return list.size() == 0;
    }

    public E removeMaximum() {
        if (isEmpty()) throw new RuntimeException("Stack underflow");
        E max = list.get(list.size() - 1);
        list.remove(max);
        return max;
    }
}
