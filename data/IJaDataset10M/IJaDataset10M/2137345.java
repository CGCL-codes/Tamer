package org.enerj.enhancer;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import org.enerj.annotations.Persist;
import org.enerj.core.Persistable;
import junit.framework.TestCase;

/**
 * Tests enhancement of Serializables. <p>
 * 
 * @version $Id: SerializableEnhancementTest.java,v 1.1 2006/06/06 22:40:58 dsyrstad Exp $
 * @author <a href="mailto:dsyrstad@ener-j.org">Dan Syrstad </a>
 */
public class SerializableEnhancementTest extends TestCase {

    /**
     * Construct a SerializableEnhancementTest. 
     */
    public SerializableEnhancementTest() {
    }

    /**
     * Construct a SerializableEnhancementTest. 
     *
     * @param aName
     */
    public SerializableEnhancementTest(String aName) {
        super(aName);
    }

    /**
     * Create a copy of an Object via serialization.
     *
     * @param obj the original Object.
     * 
     * @return the copy.
     */
    private Object createViaSerialization(Object obj) {
        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ObjectOutputStream oos = new ObjectOutputStream(baos);
            oos.writeObject(obj);
            ByteArrayInputStream bais = new ByteArrayInputStream(baos.toByteArray());
            ObjectInputStream ois = new ObjectInputStream(bais);
            return (Persistable) ois.readObject();
        } catch (IOException x) {
            throw new RuntimeException(x);
        } catch (ClassNotFoundException x) {
            throw new RuntimeException(x);
        }
    }

    /**
     * Test enhancement of a top-level persistable that doesn't implement the
     * readObject method.
     *
     * @throws Exception
     */
    public void testTLPWithoutReadObject() throws Exception {
        TLPWithoutReadObject.class.getDeclaredMethod("readObject", new Class[] { ObjectInputStream.class });
        TLPWithoutReadObject testObj = (TLPWithoutReadObject) createViaSerialization(new TLPWithoutReadObject());
        assertTrue(((Persistable) testObj).enerj_AllowsNonTransactionalRead());
        testObj.test();
    }

    /**
     * Test enhancement of a top-level persistable that implements the
     * readObject method.
     *
     * @throws Exception
     */
    public void testTLPWithReadObject() throws Exception {
        TLPWithReadObject.class.getDeclaredMethod("readObject", new Class[] { ObjectInputStream.class });
        TLPWithReadObject testObj = (TLPWithReadObject) createViaSerialization(new TLPWithReadObject());
        assertTrue(((Persistable) testObj).enerj_AllowsNonTransactionalRead());
        testObj.test();
    }

    /**
     * Test enhancement of a non-top-level persistable that doesn't implement the
     * readObject method. 
     *
     * @throws Exception
     */
    public void testNTLPWithoutReadObject() throws Exception {
        try {
            NTLPWithoutReadObject.class.getDeclaredMethod("readObject", new Class[] { ObjectInputStream.class });
            fail("Expected Exception");
        } catch (NoSuchMethodException e) {
        }
        NTLPWithoutReadObject testObj = (NTLPWithoutReadObject) createViaSerialization(new NTLPWithoutReadObject());
        assertTrue(((Persistable) testObj).enerj_AllowsNonTransactionalRead());
        testObj.test();
    }

    @Persist
    private static class TLPWithoutReadObject implements Serializable {

        private static final long serialVersionUID = 1L;

        private int mField;

        void test() {
            mField = 1;
        }
    }

    @Persist
    private static class TLPWithReadObject implements Serializable {

        private static final long serialVersionUID = 1L;

        private int mField;

        void test() {
            mField = 1;
        }

        private void readObject(ObjectInputStream ois) throws IOException, ClassNotFoundException {
            int x = ois.hashCode() + 5;
        }
    }

    @Persist
    private static class NTLPWithoutReadObject extends TLPWithoutReadObject implements Serializable {

        private static final long serialVersionUID = 1L;

        private int mField;

        void test() {
            mField = 1;
        }
    }
}
