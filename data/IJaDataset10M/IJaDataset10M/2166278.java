package net.sf.staccatocommons.iterators;

import java.util.Iterator;
import net.sf.staccatocommons.iterators.thriter.Thriterators;
import net.sf.staccatocommons.testing.junit.theories.IteratorTheories;

/**
 * @author flbulgarelli
 * 
 */
public class AppendIteratorUnitTest extends IteratorTheories {

    protected Iterator<?> createTwoElementsIterator() {
        return new AppendThriterator<Integer>(Thriterators.from(20), 10);
    }

    protected Iterator<?> createOneElementIterator() {
        return new AppendThriterator(EmptyThriterator.empty(), 10);
    }
}
