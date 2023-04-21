package junit4.smpp.domain.tlv;

import junit.framework.JUnit4TestAdapter;
import static org.junit.Assert.assertEquals;
import org.junit.Test;
import org.bulatnig.smpp.pdu.tlv.*;
import org.bulatnig.smpp.pdu.SmscEsmClass;
import org.bulatnig.smpp.util.SMPPByteBuffer;
import org.bulatnig.smpp.util.WrongParameterException;

public class TLVTest {

    public static junit.framework.Test suite() {
        return new JUnit4TestAdapter(TLVTest.class);
    }

    @Test
    public void testTLVConstructor1() throws WrongParameterException, TLVException {
        SMPPByteBuffer bb = new SMPPByteBuffer();
        bb.appendShort(0x0005);
        bb.appendShort(0x0001);
        bb.appendByte((short) 0x04);
        TLV tlv = TLVFactoryImpl.INSTANCE.parseTLV(bb.getBuffer(), new SmscEsmClass(), (short) 0);
        assertEquals(ParameterTag.DEST_ADDR_SUBUNIT, tlv.getTag());
        assertEquals(5, tlv.getBytes().length);
        assertEquals(AddrSubunit.EXTERNAL_UNIT_1, ((DestAddrSubunit) tlv).getValue());
        assertEquals("0005000104", new SMPPByteBuffer(tlv.getBytes()).getHexDump());
    }

    @Test(expected = TLVNotFoundException.class)
    public void testTLVConstructor2() throws WrongParameterException, TLVException {
        SMPPByteBuffer bb = new SMPPByteBuffer();
        bb.appendShort(0x0000);
        bb.appendShort(0x0002);
        bb.appendShort(0x1234);
        TLVFactoryImpl.INSTANCE.parseTLV(bb.getBuffer(), new SmscEsmClass(), (short) 0);
    }

    @Test(expected = TLVException.class)
    public void testTLVConstructor3() throws WrongParameterException, TLVException {
        SMPPByteBuffer bb = new SMPPByteBuffer();
        bb.appendShort(0x0005);
        bb.appendShort(0x0001);
        bb.appendShort(0x1234);
        TLVFactoryImpl.INSTANCE.parseTLV(bb.getBuffer(), new SmscEsmClass(), (short) 0);
    }
}
