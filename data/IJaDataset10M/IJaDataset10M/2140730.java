package com.hp.hpl.jena.util.iterator.test;

import java.util.List;
import junit.framework.TestSuite;
import com.hp.hpl.jena.rdf.model.test.ModelTestBase;
import com.hp.hpl.jena.util.iterator.*;

public class TestAndThen extends ModelTestBase {

    public TestAndThen(String name) {
        super(name);
    }

    public static TestSuite suite() {
        return new TestSuite(TestAndThen.class);
    }

    public void testAndThen() {
        ExtendedIterator L = iteratorOfStrings("a b c");
        ExtendedIterator R = iteratorOfStrings("d e f");
        assertInstanceOf(NiceIterator.class, L);
        assertInstanceOf(NiceIterator.class, R);
        assertEquals(listOfStrings("a b c d e f"), iteratorToList(L.andThen(R)));
    }

    public void testAndThenExtension() {
        ExtendedIterator L = iteratorOfStrings("a b c");
        ExtendedIterator R = iteratorOfStrings("d e f");
        ExtendedIterator X = iteratorOfStrings("g h i");
        ExtendedIterator LR = L.andThen(R);
        ExtendedIterator LRX = LR.andThen(X);
        assertSame(LR, LRX);
        List aToI = listOfStrings("a b c d e f g h i");
        assertEquals(aToI, iteratorToList(LRX));
    }
}
