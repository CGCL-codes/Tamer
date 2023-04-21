package org.mobicents.protocols.ss7.map.service.lsm;

import static org.testng.Assert.*;
import org.testng.*;
import org.testng.annotations.*;
import java.util.Arrays;
import org.mobicents.protocols.asn.AsnInputStream;
import org.mobicents.protocols.asn.AsnOutputStream;
import org.mobicents.protocols.ss7.map.MAPParameterFactoryImpl;
import org.mobicents.protocols.ss7.map.api.MAPParameterFactory;
import org.mobicents.protocols.ss7.map.api.primitives.AddressNature;
import org.mobicents.protocols.ss7.map.api.primitives.IMSI;
import org.mobicents.protocols.ss7.map.api.primitives.ISDNAddressString;
import org.mobicents.protocols.ss7.map.api.primitives.NumberingPlan;
import org.mobicents.protocols.ss7.map.api.primitives.SubscriberIdentity;
import org.mobicents.protocols.ss7.map.primitives.SubscriberIdentityImpl;
import org.mobicents.protocols.ss7.tcap.asn.TcapFactory;
import org.mobicents.protocols.ss7.tcap.asn.comp.Parameter;

/**
 * @author amit bhayani
 * 
 */
public class SendRoutingInfoForLCSRequestIndicationTest {

    MAPParameterFactory MAPParameterFactory = new MAPParameterFactoryImpl();

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

    @Test(groups = { "functional.decode", "service.lsm" })
    public void testDecodeProvideSubscriberLocationRequestIndication() throws Exception {
        byte[] data = new byte[] { 0x30, 0x13, (byte) 0x80, 0x05, (byte) 0x91, 0x55, 0x16, 0x09, 0x70, (byte) 0xa1, 0x0a, (byte) 0x80, 0x08, 0x27, (byte) 0x94, (byte) 0x99, 0x09, 0x00, 0x00, 0x00, (byte) 0xf7 };
        AsnInputStream asn = new AsnInputStream(data);
        int tag = asn.readTag();
        SendRoutingInfoForLCSRequestIndicationImpl rtgInfnoForLCSreqInd = new SendRoutingInfoForLCSRequestIndicationImpl();
        rtgInfnoForLCSreqInd.decodeAll(asn);
        ISDNAddressString mlcNum = rtgInfnoForLCSreqInd.getMLCNumber();
        assertNotNull(mlcNum);
        assertEquals(mlcNum.getAddressNature(), AddressNature.international_number);
        assertEquals(mlcNum.getNumberingPlan(), NumberingPlan.ISDN);
        assertEquals(mlcNum.getAddress(), "55619007");
        SubscriberIdentity subsIdent = rtgInfnoForLCSreqInd.getTargetMS();
        assertNotNull(subsIdent);
        IMSI imsi = subsIdent.getIMSI();
        ISDNAddressString msisdn = subsIdent.getMSISDN();
        assertNotNull(imsi);
        assertNull(msisdn);
        assertEquals(imsi.getData(), "724999900000007");
    }

    @Test(groups = { "functional.encode", "service.lsm" })
    public void testEncode() throws Exception {
        byte[] data = new byte[] { 0x30, 0x13, (byte) 0x80, 0x05, (byte) 0x91, 0x55, 0x16, 0x09, 0x70, (byte) 0xa1, 0x0a, (byte) 0x80, 0x08, 0x27, (byte) 0x94, (byte) 0x99, 0x09, 0x00, 0x00, 0x00, (byte) 0xf7 };
        IMSI imsi = this.MAPParameterFactory.createIMSI("724999900000007");
        SubscriberIdentity subsIdent = new SubscriberIdentityImpl(imsi);
        ISDNAddressString mlcNumber = this.MAPParameterFactory.createISDNAddressString(AddressNature.international_number, NumberingPlan.ISDN, "55619007");
        SendRoutingInfoForLCSRequestIndicationImpl rtgInfnoForLCSreqInd = new SendRoutingInfoForLCSRequestIndicationImpl(mlcNumber, subsIdent);
        AsnOutputStream asnOS = new AsnOutputStream();
        rtgInfnoForLCSreqInd.encodeAll(asnOS);
        byte[] encodedData = asnOS.toByteArray();
        assertTrue(Arrays.equals(data, encodedData));
    }
}
