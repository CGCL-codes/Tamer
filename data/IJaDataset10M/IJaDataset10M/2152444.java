package junit4.smpp.domain.tlv;

import junit.framework.JUnit4TestAdapter;
import static org.junit.Assert.assertEquals;
import org.junit.Test;
import org.bulatnig.smpp.pdu.tlv.NetworkErrorCode;
import org.bulatnig.smpp.pdu.tlv.ParameterTag;
import org.bulatnig.smpp.pdu.tlv.TLVException;
import org.bulatnig.smpp.pdu.tlv.TLVNotFoundException;
import org.bulatnig.smpp.util.SMPPByteBuffer;
import org.bulatnig.smpp.util.WrongParameterException;

public class NetworkErrorCodeTest {

    public static junit.framework.Test suite() {
        return new JUnit4TestAdapter(NetworkErrorCodeTest.class);
    }

    @Test
    public void testNECConstructor1() throws TLVException, WrongParameterException {
        SMPPByteBuffer bb = new SMPPByteBuffer();
        bb.appendShort(0x0423);
        bb.appendShort(0x0003);
        bb.appendString("123");
        NetworkErrorCode nec = new NetworkErrorCode(bb.getBuffer());
        assertEquals(ParameterTag.NETWORK_ERROR_CODE, nec.getTag());
        assertEquals(7, nec.getBytes().length);
        assertEquals("123", nec.getValue());
        assertEquals("04230003313233", new SMPPByteBuffer(nec.getBytes()).getHexDump());
    }

    @Test(expected = TLVNotFoundException.class)
    public void testNECConstructor2() throws TLVException, WrongParameterException {
        SMPPByteBuffer bb = new SMPPByteBuffer();
        bb.appendShort(0x0000);
        bb.appendShort(0x0001);
        bb.appendByte((byte) 0x11);
        new NetworkErrorCode(bb.getBuffer());
    }

    @Test(expected = TLVException.class)
    public void testNECConstructor3() throws TLVException, WrongParameterException {
        SMPPByteBuffer bb = new SMPPByteBuffer();
        bb.appendShort(0x0423);
        bb.appendShort(0x0002);
        bb.appendByte((byte) 0x11);
        new NetworkErrorCode(bb.getBuffer());
    }

    @Test
    public void testNECConstructor4() throws TLVException, WrongParameterException {
        NetworkErrorCode nec = new NetworkErrorCode("123");
        assertEquals(ParameterTag.NETWORK_ERROR_CODE, nec.getTag());
        assertEquals(7, nec.getBytes().length);
        assertEquals("123", nec.getValue());
        assertEquals("04230003313233", new SMPPByteBuffer(nec.getBytes()).getHexDump());
    }

    @Test(expected = ClassCastException.class)
    public void testNECConstructor5() throws WrongParameterException, TLVException {
        SMPPByteBuffer bb = new SMPPByteBuffer();
        bb.appendShort(0x0005);
        bb.appendShort(0x0001);
        bb.appendByte((byte) 0x11);
        new NetworkErrorCode(bb.getBuffer());
    }
}
