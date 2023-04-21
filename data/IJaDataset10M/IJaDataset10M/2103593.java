package com.google.visualization.datasource.query.parser;

import com.google.common.collect.Lists;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

class GenericsHelper {

    private GenericsHelper() {
    }

    static <T> List<T> makeTypedList(ArrayList<? extends T> list) {
        List<T> result = Lists.newArrayListWithExpectedSize(list.size());
        for (T obj : list) {
            result.add(obj);
        }
        return result;
    }

    static <T> List<T> makeAbstractColumnList(T[] array) {
        List<T> result = Lists.newArrayListWithExpectedSize(array.length);
        result.addAll(Arrays.asList(array));
        return result;
    }
}
