package org.mobicents.protocols.ss7.map.service.sms;

import java.util.Arrays;
import org.mobicents.protocols.asn.AsnInputStream;
import org.mobicents.protocols.asn.AsnOutputStream;
import org.mobicents.protocols.asn.Tag;
import org.mobicents.protocols.ss7.map.api.primitives.MAPExtensionContainer;
import org.mobicents.protocols.ss7.map.api.service.sms.SmsSignalInfo;
import org.mobicents.protocols.ss7.map.primitives.MAPExtensionContainerTest;
import static org.testng.Assert.*;
import org.testng.*;
import org.testng.annotations.*;

/**
 * 
 * @author sergey vetyutnev
 *
 */
public class MtForwardShortMessageResponseIndicationTest {

    private byte[] getEncodedData() {
        return new byte[] { 48, 48, 4, 5, 11, 22, 33, 44, 55, 48, 39, -96, 32, 48, 10, 6, 3, 42, 3, 4, 11, 12, 13, 14, 15, 48, 5, 6, 3, 42, 3, 6, 48, 11, 6, 3, 42, 3, 5, 21, 22, 23, 24, 25, 26, -95, 3, 31, 32, 33 };
    }

    @Test(groups = { "functional.decode", "service.sms" })
    public void testDecode() throws Exception {
        byte[] rawData = getEncodedData();
        AsnInputStream asn = new AsnInputStream(rawData);
        int tag = asn.readTag();
        MtForwardShortMessageResponseIndicationImpl ind = new MtForwardShortMessageResponseIndicationImpl();
        ind.decodeAll(asn);
        assertEquals(tag, Tag.SEQUENCE);
        assertEquals(asn.getTagClass(), Tag.CLASS_UNIVERSAL);
        assertTrue(Arrays.equals(new byte[] { 11, 22, 33, 44, 55 }, ind.getSM_RP_UI().getData()));
        assertTrue(MAPExtensionContainerTest.CheckTestExtensionContainer(ind.getExtensionContainer()));
    }

    @Test(groups = { "functional.encode", "service.sms" })
    public void testEncode() throws Exception {
        SmsSignalInfo ui = new SmsSignalInfoImpl(new byte[] { 11, 22, 33, 44, 55 }, null);
        MAPExtensionContainer extensionContainer = MAPExtensionContainerTest.GetTestExtensionContainer();
        MtForwardShortMessageResponseIndicationImpl ind = new MtForwardShortMessageResponseIndicationImpl(ui, extensionContainer);
        AsnOutputStream asnOS = new AsnOutputStream();
        ind.encodeAll(asnOS);
        byte[] encodedData = asnOS.toByteArray();
        byte[] rawData = getEncodedData();
        assertTrue(Arrays.equals(rawData, encodedData));
    }
}
