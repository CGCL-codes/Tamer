package org.hamcrestcollections;

import java.util.List;

public class Reduction {

    public static <T> T reduce(Iterable<T> iterable, Reducer<T> reducer) {
        List<T> items = ListUtils.asList(iterable);
        T currentValue = null;
        if (items.size() == 0) {
            throw new InvalidReductionException("Cannot reduce zero items");
        }
        if (items.size() == 1) {
            return items.get(0);
        }
        T first = items.get(0);
        T second = items.get(1);
        currentValue = reducer.apply(first, second);
        for (int i = 2; i < items.size(); i++) {
            currentValue = reducer.apply(currentValue, items.get(i));
        }
        return currentValue;
    }
}
