package net.sf.staccatocommons.iterators;

import java.util.Iterator;
import net.sf.staccatocommons.testing.junit.theories.IteratorTheories;

/**
 * @author flbulgarelli
 * 
 */
public class CharSequenceIteratorUnitTest extends IteratorTheories {

    protected Iterator<?> createTwoElementsIterator() {
        return new CharSequenceThriterator("ab");
    }

    protected Iterator<?> createOneElementIterator() {
        return new CharSequenceThriterator("a");
    }
}
