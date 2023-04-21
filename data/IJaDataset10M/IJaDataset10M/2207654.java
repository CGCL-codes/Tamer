package org.ujoframework.orm;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.Date;
import junit.framework.TestCase;
import org.ujoframework.orm.bo.XOrder;

/**
 *
 * @author Pavel Ponec
 */
public class SerializatonTest extends TestCase {

    public SerializatonTest(String testName) {
        super(testName);
    }

    /**
     * Test of getValue method, of class UniqueKey.
     */
    public void testKeySerialization() throws Exception {
        System.out.println("testKeySerialization");
        ForeignKey expResult = new ForeignKey(123L);
        ForeignKey result = serialize(expResult);
        assertEquals(expResult, result);
    }

    /**
     * Test of getValue method, of class UniqueKey.
     */
    public void testOrmTableSerialization() throws Exception {
        System.out.println("testOrmTableSerialization");
        XOrder expResult = new XOrder();
        expResult.setId(33L);
        expResult.setDate(new Date());
        expResult.setState(XOrder.State.DELETED);
        XOrder result = serialize(expResult);
        assertEquals(expResult.getId(), result.getId());
        assertEquals(expResult.getDate(), result.getDate());
        assertSame(expResult.getState(), result.getState());
        assertSame(expResult.getDescr(), result.getDescr());
    }

    /** Object serialization */
    @SuppressWarnings("unchecked")
    private <T> T serialize(Serializable object) throws Exception {
        ByteArrayOutputStream dataFile = new ByteArrayOutputStream(8000);
        ObjectOutputStream encoder = null;
        ObjectInputStream decoder = null;
        encoder = new ObjectOutputStream(dataFile);
        encoder.writeObject(object);
        encoder.flush();
        encoder.close();
        InputStream is = new ByteArrayInputStream(dataFile.toByteArray());
        decoder = new ObjectInputStream(is);
        Object result = (Serializable) decoder.readObject();
        decoder.close();
        return (T) result;
    }
}
