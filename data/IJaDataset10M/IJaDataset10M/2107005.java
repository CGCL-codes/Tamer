package com.google.common.collect.testing.testers;

import com.google.common.collect.testing.MinimalCollection;
import com.google.common.collect.testing.features.CollectionFeature;
import static com.google.common.collect.testing.features.CollectionFeature.SUPPORTS_ADD_ALL;
import com.google.common.collect.testing.features.CollectionSize;
import static com.google.common.collect.testing.features.CollectionSize.ZERO;

/**
 * A generic JUnit test which tests addAll operations on a set. Can't be
 * invoked directly; please see
 * {@link com.google.common.collect.testing.SetTestSuiteBuilder}.
 *
 * @author Kevin Bourrillion
 */
@SuppressWarnings("unchecked")
public class SetAddAllTester<E> extends AbstractSetTester<E> {

    @CollectionFeature.Require(SUPPORTS_ADD_ALL)
    @CollectionSize.Require(absent = ZERO)
    public void testAddAll_supportedSomePresent() {
        assertTrue("add(somePresent) should return true", getSet().addAll(MinimalCollection.of(samples.e3, samples.e0)));
        expectAdded(samples.e3);
    }

    @CollectionFeature.Require(SUPPORTS_ADD_ALL)
    public void testAddAll_withDuplicates() {
        MinimalCollection<E> elementsToAdd = MinimalCollection.of(samples.e3, samples.e4, samples.e3, samples.e4);
        assertTrue("add(hasDuplicates) should return true", getSet().addAll(elementsToAdd));
        expectAdded(samples.e3, samples.e4);
    }

    @CollectionFeature.Require(SUPPORTS_ADD_ALL)
    @CollectionSize.Require(absent = ZERO)
    public void testAddAll_supportedAllPresent() {
        assertFalse("add(allPresent) should return false", getSet().addAll(MinimalCollection.of(samples.e0)));
        expectUnchanged();
    }
}
