package org.mobicents.protocols.ss7.cap.service.circuitSwitchedCall.primitive;

import static org.testng.Assert.*;
import java.util.Arrays;
import org.mobicents.protocols.asn.AsnInputStream;
import org.mobicents.protocols.asn.AsnOutputStream;
import org.mobicents.protocols.asn.Tag;
import org.mobicents.protocols.ss7.cap.isup.CalledPartyNumberCapImpl;
import org.mobicents.protocols.ss7.isup.impl.message.parameter.CalledPartyNumberImpl;
import org.mobicents.protocols.ss7.isup.message.parameter.CalledPartyNumber;
import org.mobicents.protocols.ss7.map.api.primitives.AddressNature;
import org.mobicents.protocols.ss7.map.api.primitives.NumberingPlan;
import org.mobicents.protocols.ss7.map.primitives.ISDNAddressStringImpl;
import org.testng.*;
import org.testng.annotations.*;

/**
 * 
 * @author sergey vetyutnev
 * 
 */
public class InitialDPArgExtensionTest {

    public byte[] getData1() {
        return new byte[] { (byte) 191, 59, 8, (byte) 129, 6, (byte) 145, 34, 112, 87, 0, 112 };
    }

    public byte[] getData2() {
        return new byte[] { (byte) 191, 59, 12, (byte) 128, 4, (byte) 152, 17, 17, 17, (byte) 129, 4, 1, 16, 34, 34 };
    }

    @Test(groups = { "functional.decode", "circuitSwitchedCall.primitive" })
    public void testDecode() throws Exception {
        byte[] data = this.getData1();
        AsnInputStream ais = new AsnInputStream(data);
        InitialDPArgExtensionImpl elem = new InitialDPArgExtensionImpl(false);
        int tag = ais.readTag();
        elem.decodeAll(ais);
        assertEquals(elem.getGmscAddress().getAddressNature(), AddressNature.international_number);
        assertEquals(elem.getGmscAddress().getNumberingPlan(), NumberingPlan.ISDN);
        assertTrue(elem.getGmscAddress().getAddress().equals("2207750007"));
        assertNull(elem.getForwardingDestinationNumber());
        data = this.getData2();
        ais = new AsnInputStream(data);
        elem = new InitialDPArgExtensionImpl(true);
        tag = ais.readTag();
        elem.decodeAll(ais);
        assertEquals(elem.getGmscAddress().getAddressNature(), AddressNature.international_number);
        assertEquals(elem.getGmscAddress().getNumberingPlan(), NumberingPlan.national);
        assertTrue(elem.getGmscAddress().getAddress().equals("111111"));
        CalledPartyNumber cpn = elem.getForwardingDestinationNumber().getCalledPartyNumber();
        assertTrue(cpn.getAddress().equals("2222"));
        assertEquals(cpn.getInternalNetworkNumberIndicator(), 0);
        assertEquals(cpn.getNatureOfAddressIndicator(), 1);
        assertEquals(cpn.getNumberingPlanIndicator(), 1);
    }

    @Test(groups = { "functional.encode", "circuitSwitchedCall.primitive" })
    public void testEncode() throws Exception {
        ISDNAddressStringImpl gmscAddress = new ISDNAddressStringImpl(AddressNature.international_number, NumberingPlan.ISDN, "2207750007");
        InitialDPArgExtensionImpl elem = new InitialDPArgExtensionImpl(gmscAddress, null, null, null, null, null, null, null, null, null, null, false, null, false);
        AsnOutputStream aos = new AsnOutputStream();
        elem.encodeAll(aos, Tag.CLASS_CONTEXT_SPECIFIC, 59);
        assertTrue(Arrays.equals(aos.toByteArray(), this.getData1()));
        gmscAddress = new ISDNAddressStringImpl(AddressNature.international_number, NumberingPlan.national, "111111");
        CalledPartyNumberImpl calledPartyNumber = new CalledPartyNumberImpl(1, "2222", 1, 0);
        CalledPartyNumberCapImpl forwardingDestinationNumber = new CalledPartyNumberCapImpl(calledPartyNumber);
        elem = new InitialDPArgExtensionImpl(gmscAddress, forwardingDestinationNumber, null, null, null, null, null, null, null, null, null, false, null, true);
        aos = new AsnOutputStream();
        elem.encodeAll(aos, Tag.CLASS_CONTEXT_SPECIFIC, 59);
        assertTrue(Arrays.equals(aos.toByteArray(), this.getData2()));
    }
}
