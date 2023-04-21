package org.magicdroid.test.mosaic;

import org.magicdroid.commons.MagicExtension;
import junit.framework.TestCase;

public class TestMagicExtension extends TestCase {

    public interface Test1Point {

        String provideName();

        void putValue(int count);
    }

    public static class Extension1 implements Test1Point {

        @Override
        public String provideName() {
            return this.getClass().getName();
        }

        @Override
        public void putValue(int count) {
        }
    }

    public static MagicExtension<Test1Point> EXTENSION = new MagicExtension<TestMagicExtension.Test1Point>(Test1Point.class);

    public void testAdd() {
        EXTENSION.add(new Extension1());
        assertEquals(Extension1.class.getName(), EXTENSION.proxy().provideName());
    }
}
