package net.sf.staccatocommons.collections.stream.impl.internal;

import static net.sf.staccatocommons.lang.Compare.*;
import net.sf.staccatocommons.collections.stream.Streams;
import net.sf.staccatocommons.testing.junit.theories.IterableTheories;

/**
 * @author flbulgarelli
 * 
 */
public class FilterIteratorUnitTest extends IterableTheories {

    protected Iterable<?> createTwoElementsIterable() {
        return Streams.cons(10, 20, 60, 50, 90).filter(greaterThan(55));
    }

    protected Iterable<?> createOneElementIterable() {
        return Streams.cons(10, 20, 60, 50).filter(greaterThan(55));
    }
}
