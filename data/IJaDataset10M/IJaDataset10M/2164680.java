package org.mobicents.protocols.ss7.map.service.sms;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Arrays;
import org.mobicents.protocols.asn.AsnInputStream;
import org.mobicents.protocols.asn.AsnOutputStream;
import org.mobicents.protocols.asn.Tag;
import org.testng.annotations.Test;

/**
 * 
 * @author sergey vetyutnev
 *
 */
public class MWStatusTest {

    private byte[] getEncodedData() {
        return new byte[] { 3, 2, 2, 64 };
    }

    @Test(groups = { "functional.decode", "service.sms" })
    public void testDecode() throws Exception {
        byte[] rawData = getEncodedData();
        AsnInputStream asn = new AsnInputStream(rawData);
        int tag = asn.readTag();
        MWStatusImpl mws = new MWStatusImpl();
        mws.decodeAll(asn);
        assertEquals(tag, Tag.STRING_BIT);
        assertEquals(asn.getTagClass(), Tag.CLASS_UNIVERSAL);
        assertEquals((boolean) mws.getMcefSet(), false);
        assertEquals((boolean) mws.getMnrfSet(), true);
        assertEquals((boolean) mws.getMnrgSet(), false);
        assertEquals((boolean) mws.getScAddressNotIncluded(), false);
    }

    @Test(groups = { "functional.encode", "service.sms" })
    public void testEncode() throws Exception {
        MWStatusImpl mws = new MWStatusImpl(false, true, false, false);
        AsnOutputStream asnOS = new AsnOutputStream();
        mws.encodeAll(asnOS);
        byte[] encodedData = asnOS.toByteArray();
        byte[] rawData = getEncodedData();
        assertTrue(Arrays.equals(rawData, encodedData));
    }

    @Test(groups = { "functional.serialize", "service.sms" })
    public void testSerialization() throws Exception {
        MWStatusImpl original = new MWStatusImpl(false, true, false, false);
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        ObjectOutputStream oos = new ObjectOutputStream(out);
        oos.writeObject(original);
        oos.close();
        byte[] pickled = out.toByteArray();
        InputStream in = new ByteArrayInputStream(pickled);
        ObjectInputStream ois = new ObjectInputStream(in);
        Object o = ois.readObject();
        MWStatusImpl copy = (MWStatusImpl) o;
        assertEquals(copy.getMcefSet(), original.getMcefSet());
        assertEquals(copy.getScAddressNotIncluded(), original.getScAddressNotIncluded());
        assertEquals(copy.getMnrfSet(), original.getMnrfSet());
        assertEquals(copy.getMnrgSet(), original.getMnrgSet());
    }
}
