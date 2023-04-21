package org.mobicents.protocols.ss7.map.service.subscriberInformation;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotNull;
import org.mobicents.protocols.asn.AsnInputStream;
import org.mobicents.protocols.ss7.map.api.service.subscriberInformation.LocationInformation;
import org.mobicents.protocols.ss7.map.api.service.subscriberInformation.SubscriberState;
import org.mobicents.protocols.ss7.map.api.service.subscriberInformation.SubscriberStateChoice;
import org.testng.annotations.Test;

/**
 * @author abhayani
 * 
 */
public class SubscriberInfoTest {

    byte[] data = new byte[] { (byte) 0x30, 0x32, (byte) 0xa0, 0x2c, 0x02, 0x01, 0x01, (byte) 0x80, 0x08, 0x10, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, (byte) 0x81, 0x07, (byte) 0x91, 0x55, 0x43, 0x69, 0x26, (byte) 0x99, 0x01, (byte) 0xa3, 0x09, (byte) 0x80, 0x07, 0x27, (byte) 0xf4, 0x43, 0x79, (byte) 0x9e, 0x29, (byte) 0xa0, (byte) 0x86, 0x07, (byte) 0x91, 0x55, 0x43, 0x69, 0x26, (byte) 0x99, 0x01, (byte) 0x89, 0x00, (byte) 0xa1, 0x02, (byte) 0x80, 0x00 };

    @Test(groups = { "functional.decode", "subscriberInformation" })
    public void testDecode() throws Exception {
        AsnInputStream asn = new AsnInputStream(data);
        int tag = asn.readTag();
        SubscriberInfoImpl subscriberInfo = new SubscriberInfoImpl();
        subscriberInfo.decodeAll(asn);
        LocationInformation locInfo = subscriberInfo.getLocationInformation();
        assertNotNull(locInfo);
        SubscriberState subState = subscriberInfo.getSubscriberState();
        assertEquals(subState.getSubscriberStateChoice(), SubscriberStateChoice.assumedIdle);
    }
}
