package com.google.common.collect.testing;

import com.google.common.collect.testing.features.CollectionFeature;
import com.google.common.collect.testing.features.CollectionSize;
import junit.framework.Test;
import junit.framework.TestCase;
import java.util.Collection;

/**
 * Unit test for {@link MinimalCollection}.
 *
 * @author Kevin Bourrillion
 */
public class MinimalCollectionTest extends TestCase {

    public static Test suite() {
        return CollectionTestSuiteBuilder.using(new TestStringCollectionGenerator() {

            @Override
            public Collection<String> create(String[] elements) {
                for (Object element : elements) {
                    Helpers.checkNotNull(element);
                }
                return MinimalCollection.of(elements);
            }
        }).named("MinimalCollection").withFeatures(CollectionFeature.NONE, CollectionSize.ANY).createTestSuite();
    }
}
