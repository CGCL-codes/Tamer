package junit4.smpp.domain.tlv;

import junit.framework.JUnit4TestAdapter;
import static org.junit.Assert.assertEquals;
import org.junit.Test;
import org.bulatnig.smpp.pdu.tlv.MsMsgWaitFacilities;
import org.bulatnig.smpp.pdu.tlv.ParameterTag;
import org.bulatnig.smpp.pdu.tlv.TLVException;
import org.bulatnig.smpp.pdu.tlv.TLVNotFoundException;
import org.bulatnig.smpp.util.SMPPByteBuffer;
import org.bulatnig.smpp.util.WrongParameterException;

public class MsMsgWaitFacilitiesTest {

    public static junit.framework.Test suite() {
        return new JUnit4TestAdapter(MsMsgWaitFacilitiesTest.class);
    }

    @Test
    public void testMMWFConstructor1() throws TLVException, WrongParameterException {
        SMPPByteBuffer bb = new SMPPByteBuffer();
        bb.appendShort(0x0030);
        bb.appendShort(0x0001);
        bb.appendByte((short) 0x01);
        MsMsgWaitFacilities das = new MsMsgWaitFacilities(bb.getBuffer());
        assertEquals(ParameterTag.MS_MSG_WAIT_FACILITIES, das.getTag());
        assertEquals(5, das.getBytes().length);
        assertEquals((short) 1, das.getValue());
        assertEquals("0030000101", new SMPPByteBuffer(das.getBytes()).getHexDump());
    }

    @Test(expected = TLVNotFoundException.class)
    public void testMMWFConstructor2() throws TLVException, WrongParameterException {
        SMPPByteBuffer bb = new SMPPByteBuffer();
        bb.appendShort(0x0000);
        bb.appendShort(0x0001);
        bb.appendByte((byte) 0x55);
        new MsMsgWaitFacilities(bb.getBuffer());
    }

    @Test(expected = TLVException.class)
    public void testMMWFConstructor3() throws TLVException, WrongParameterException {
        SMPPByteBuffer bb = new SMPPByteBuffer();
        bb.appendShort(0x0005);
        bb.appendShort(0x0001);
        bb.appendShort(0x0003);
        new MsMsgWaitFacilities(bb.getBuffer());
    }

    @Test
    public void testMMWFConstructor4() throws TLVException, WrongParameterException {
        MsMsgWaitFacilities das = new MsMsgWaitFacilities((short) 2);
        assertEquals(ParameterTag.MS_MSG_WAIT_FACILITIES, das.getTag());
        assertEquals(5, das.getBytes().length);
        assertEquals(2, das.getValue());
        assertEquals("0030000102", new SMPPByteBuffer(das.getBytes()).getHexDump());
    }

    @Test(expected = TLVException.class)
    public void testSASConstructor5() throws TLVException, WrongParameterException {
        SMPPByteBuffer bb = new SMPPByteBuffer();
        bb.appendShort(0x0030);
        bb.appendShort(0x0002);
        bb.appendShort(0x0012);
        new MsMsgWaitFacilities(bb.getBuffer());
    }

    @Test(expected = ClassCastException.class)
    public void testMMWFConstructor6() throws TLVException, WrongParameterException {
        SMPPByteBuffer bb = new SMPPByteBuffer();
        bb.appendShort(0x0006);
        bb.appendShort(0x0001);
        bb.appendByte((byte) 0x00);
        new MsMsgWaitFacilities(bb.getBuffer());
    }

    @Test(expected = TLVException.class)
    public void testMMWFConstructor7() throws TLVException {
        new MsMsgWaitFacilities((short) 32000).getBytes();
    }
}
