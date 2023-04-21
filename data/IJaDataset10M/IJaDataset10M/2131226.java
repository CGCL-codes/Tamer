package org.mobicents.protocols.ss7.cap.service.circuitSwitchedCall;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;
import java.util.Arrays;
import org.mobicents.protocols.asn.AsnInputStream;
import org.mobicents.protocols.asn.AsnOutputStream;
import org.mobicents.protocols.asn.Tag;
import org.mobicents.protocols.ss7.cap.api.primitives.AppendFreeFormatData;
import org.mobicents.protocols.ss7.cap.primitives.SendingSideIDImpl;
import org.mobicents.protocols.ss7.cap.service.circuitSwitchedCall.primitive.FCIBCCCAMELsequence1Impl;
import org.mobicents.protocols.ss7.inap.api.primitives.LegType;
import org.testng.annotations.Test;

/**
 * 
 * @author sergey vetyutnev
 * 
 */
public class FurnishChargingInformationIndicationTest {

    public byte[] getData1() {
        return new byte[] { 4, 16, (byte) 160, 14, (byte) 128, 4, 4, 5, 6, 7, (byte) 161, 3, (byte) 128, 1, 2, (byte) 130, 1, 1 };
    }

    public byte[] getDataFFD() {
        return new byte[] { 4, 5, 6, 7 };
    }

    @Test(groups = { "functional.decode", "circuitSwitchedCall" })
    public void testDecode() throws Exception {
        byte[] data = this.getData1();
        AsnInputStream ais = new AsnInputStream(data);
        FurnishChargingInformationRequestIndicationImpl elem = new FurnishChargingInformationRequestIndicationImpl();
        int tag = ais.readTag();
        assertEquals(tag, Tag.STRING_OCTET);
        elem.decodeAll(ais);
        assertTrue(Arrays.equals(elem.getFCIBCCCAMELsequence1().getFreeFormatData(), this.getDataFFD()));
        assertEquals(elem.getFCIBCCCAMELsequence1().getPartyToCharge().getSendingSideID(), LegType.leg2);
        assertEquals(elem.getFCIBCCCAMELsequence1().getAppendFreeFormatData(), AppendFreeFormatData.append);
    }

    @Test(groups = { "functional.encode", "circuitSwitchedCall" })
    public void testEncode() throws Exception {
        SendingSideIDImpl partyToCharge = new SendingSideIDImpl(LegType.leg2);
        FCIBCCCAMELsequence1Impl fci = new FCIBCCCAMELsequence1Impl(getDataFFD(), partyToCharge, AppendFreeFormatData.append);
        FurnishChargingInformationRequestIndicationImpl elem = new FurnishChargingInformationRequestIndicationImpl(fci);
        AsnOutputStream aos = new AsnOutputStream();
        elem.encodeAll(aos);
        assertTrue(Arrays.equals(aos.toByteArray(), this.getData1()));
    }
}
