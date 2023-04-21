package org.apache.harmony.sql.tests.javax.sql.rowset.serial;

import java.io.Serializable;
import java.util.Arrays;
import javax.sql.rowset.serial.SerialException;
import javax.sql.rowset.serial.SerialJavaObject;
import junit.framework.TestCase;

public class SerialJavaObjectTest extends TestCase {

    public void test_Constructor() throws Exception {
        TransientFieldClass tfc = new TransientFieldClass();
        SerialJavaObject sjo;
        try {
            sjo = new SerialJavaObject(tfc);
            fail("should throw SerialException");
        } catch (SerialException e) {
        }
        try {
            NonSerialiableClass nsc = new NonSerialiableClass();
            sjo = new SerialJavaObject(nsc);
            fail("should throw SerialException");
        } catch (SerialException e) {
        }
        SerializableClass sc = new SerializableClass();
        sjo = new SerialJavaObject(sc);
        assertSame(sc, sjo.getObject());
        Arrays.equals(sjo.getFields(), sc.getClass().getFields());
        try {
            new SerialJavaObject(null);
            fail("should throw NullPointerException");
        } catch (NullPointerException e) {
        }
    }

    static class TransientFieldClass {

        transient int i;

        String s;
    }

    static class NonSerialiableClass {

        int i;

        String s;
    }

    static class StaticFieldClass {

        static int i;

        String s;
    }

    static class SerializableClass implements Serializable {

        private static final long serialVersionUID = 0L;

        int i;

        String s;
    }
}
