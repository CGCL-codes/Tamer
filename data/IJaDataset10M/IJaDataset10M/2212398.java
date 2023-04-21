package de.helwich.math.symbolic.matrix;

import java.util.Iterator;
import java.util.NoSuchElementException;
import de.helwich.math.symbolic.node.Scalar;
import de.helwich.math.symbolic.node.ScalarDeserializationException;
import junit.framework.TestCase;

public class MatrixTest extends TestCase {

    public void testIterable() throws MatrixDeserializationException, ScalarDeserializationException {
        Matrix A = new MatrixFactory().create("1 2 3 4 5 6 [2x3]");
        Iterator<Scalar> it = A.iterator();
        for (int i = 1; i <= 6; i++) {
            assertTrue(it.hasNext());
            assertTrue(it.hasNext());
            assertEquals(Integer.toString(i), it.next().toString());
        }
        assertFalse(it.hasNext());
        assertFalse(it.hasNext());
        try {
            it.next();
            fail();
        } catch (NoSuchElementException e) {
        }
    }
}
