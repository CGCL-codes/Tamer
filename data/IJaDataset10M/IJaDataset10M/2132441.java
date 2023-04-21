package org.mobicents.protocols.ss7.map.primitives;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertNull;
import static org.testng.Assert.assertTrue;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Arrays;
import org.mobicents.protocols.asn.AsnInputStream;
import org.mobicents.protocols.asn.AsnOutputStream;
import org.mobicents.protocols.ss7.map.api.primitives.AlertingCategory;
import org.mobicents.protocols.ss7.map.api.primitives.AlertingLevel;
import org.testng.annotations.AfterClass;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

/**
 * @author amit bhayani
 *
 */
public class AlertingPatternTest {

    @BeforeClass
    public static void setUpClass() throws Exception {
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }

    @BeforeTest
    public void setUp() {
    }

    @AfterTest
    public void tearDown() {
    }

    @Test(groups = { "functional.decode", "primitives" })
    public void testDecode() throws Exception {
        byte[] data = new byte[] { (byte) 0x04, 0x01, 0x07 };
        AsnInputStream asn = new AsnInputStream(data);
        int tag = asn.readTag();
        AlertingPatternImpl addNum = new AlertingPatternImpl();
        addNum.decodeAll(asn);
        assertNull(addNum.getAlertingLevel());
        assertNotNull(addNum.getAlertingCategory());
        assertEquals(addNum.getAlertingCategory(), AlertingCategory.Category4);
    }

    @Test(groups = { "functional.encode", "primitives" })
    public void testEncode() throws Exception {
        byte[] data = new byte[] { (byte) 0x04, 0x01, 0x07 };
        AlertingPatternImpl addNum = new AlertingPatternImpl(AlertingCategory.Category4);
        AsnOutputStream asnOS = new AsnOutputStream();
        addNum.encodeAll(asnOS);
        byte[] encodedData = asnOS.toByteArray();
        assertTrue(Arrays.equals(data, encodedData));
    }

    @Test(groups = { "functional.serialize", "primitives" })
    public void testSerialization() throws Exception {
        AlertingPatternImpl original = new AlertingPatternImpl(AlertingCategory.Category4);
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        ObjectOutputStream oos = new ObjectOutputStream(out);
        oos.writeObject(original);
        oos.close();
        byte[] pickled = out.toByteArray();
        InputStream in = new ByteArrayInputStream(pickled);
        ObjectInputStream ois = new ObjectInputStream(in);
        Object o = ois.readObject();
        AlertingPatternImpl copy = (AlertingPatternImpl) o;
        assertEquals(copy.getAlertingCategory(), original.getAlertingCategory());
        assertEquals(copy, original);
        assertNull(copy.getAlertingLevel());
        original = new AlertingPatternImpl(AlertingLevel.Level1);
        out = new ByteArrayOutputStream();
        oos = new ObjectOutputStream(out);
        oos.writeObject(original);
        oos.close();
        pickled = out.toByteArray();
        in = new ByteArrayInputStream(pickled);
        ois = new ObjectInputStream(in);
        o = ois.readObject();
        copy = (AlertingPatternImpl) o;
        assertEquals(copy.getAlertingLevel(), original.getAlertingLevel());
        assertEquals(copy, original);
        assertNull(copy.getAlertingCategory());
    }
}
