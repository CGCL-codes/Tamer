package com.infomancers.collections.util;

import java.util.List;

/**
 * Used for simple transformations based on values located at specific
 * indices of a standard List.
 *
 * @see java.util.List;
 */
public final class ListTransformation<K> implements Transformation<Integer, K> {

    private List<K> list;

    public ListTransformation(List<K> list) {
        this.list = list;
    }

    /**
     * Transforms item of type T to item of type K.
     *
     * @param item The item to transform.
     * @return The transformed item.
     */
    public K transform(Integer item) {
        return list.get(item);
    }
}
