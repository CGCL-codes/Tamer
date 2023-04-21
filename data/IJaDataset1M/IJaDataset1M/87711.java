package com.google.common.collect.testing;

import com.google.common.collect.testing.SampleElements.Strings;
import java.util.Collection;
import java.util.List;

/**
 * String creation for testing arbitrary collections.
 *
 * @author Jared Levy
 */
public abstract class TestStringCollectionGenerator implements TestCollectionGenerator<String> {

    public SampleElements<String> samples() {
        return new Strings();
    }

    public Collection<String> create(Object... elements) {
        String[] array = new String[elements.length];
        int i = 0;
        for (Object e : elements) {
            array[i++] = (String) e;
        }
        return create(array);
    }

    protected abstract Collection<String> create(String[] elements);

    public String[] createArray(int length) {
        return new String[length];
    }

    /** Returns the original element list, unchanged. */
    public List<String> order(List<String> insertionOrder) {
        return insertionOrder;
    }
}
