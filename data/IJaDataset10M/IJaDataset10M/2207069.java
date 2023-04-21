package be.lassi.base;

import org.testng.annotations.Test;
import be.lassi.support.ObjectBuilder;
import be.lassi.support.ObjectTest;

/**
 * Tests class <code>LongHolder</code>.
 */
public class LongHolderTestCase {

    @Test
    public void object() {
        ObjectBuilder b = new ObjectBuilder() {

            public Object getObject1() {
                return new LongHolder("name", 1);
            }

            public Object getObject2() {
                return new LongHolder("name", 2);
            }
        };
        ObjectTest.test(b);
    }
}
