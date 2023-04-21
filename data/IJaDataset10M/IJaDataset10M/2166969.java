package com.google.common.collect.testing;

import java.util.Collection;

/**
 * Base class for collection testers.
 *
 * @param <E> the element type of the collection to be tested.
 *
 * @author Kevin Bourrillion
 */
public abstract class AbstractCollectionTester<E> extends AbstractContainerTester<Collection<E>, E> {

    protected Collection<E> collection;

    @Override
    protected Collection<E> actualContents() {
        return collection;
    }

    @Override
    protected Collection<E> resetContainer(Collection<E> newContents) {
        collection = super.resetContainer(newContents);
        return collection;
    }

    /** @see AbstractContainerTester#resetContainer() */
    protected void resetCollection() {
        resetContainer();
    }

    /**
   * @return an array of the proper size with {@code null} inserted into the
   * middle element.
   */
    protected E[] createArrayWithNullElement() {
        E[] array = createSamplesArray();
        array[getNullLocation()] = null;
        return array;
    }

    protected void initCollectionWithNullElement() {
        E[] array = createArrayWithNullElement();
        resetContainer(getSubjectGenerator().create(array));
    }

    /**
   * Equivalent to {@link #expectMissing(Object[]) expectMissing}{@code (null)}
   * except that the call to {@code contains(null)} is permitted to throw a
   * {@code NullPointerException}.
   *
   * @param message message to use upon assertion failure
   */
    protected void expectNullMissingWhenNullUnsupported(String message) {
        try {
            assertFalse(message, actualContents().contains(null));
        } catch (NullPointerException tolerated) {
        }
    }
}
