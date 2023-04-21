package net.sf.crepido.base;

import junit.framework.TestCase;
import static junit.framework.TestCase.*;
import net.sf.crepido.util.*;

public class EitherTest extends TestCase {

    public void testEitherClass() {
        Either<String, Integer> value1 = Either.left("value1");
        Either<String, Integer> value2 = Either.left("value2");
        Either<String, Integer> value3 = Either.right(33);
        Either<String, Integer> value4 = Either.right(44);
        assertEquals(value1.left(), "value1");
        assertEquals(value2.left(), "value2");
        assertTrue(value3.right() == 33);
        assertTrue(value4.right() == 44);
        assertEquals(Either.lefts(Seq.ofItems(value1, value2, value3, value4)).implode(","), "value1,value2");
        assertEquals(Either.rights(Seq.ofItems(value1, value2, value3, value4)).implode(","), "33,44");
    }
}
