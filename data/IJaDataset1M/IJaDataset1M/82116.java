package org.torweg.pulse.component.shop.payment;

import java.io.StringReader;
import java.io.StringWriter;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import junit.framework.TestCase;
import org.apache.log4j.Logger;
import org.torweg.pulse.TestingEnvironment;
import org.torweg.pulse.invocation.lifecycle.Lifecycle;

/**
 * @author Christian Schatt
 * @version $Revision: 1.1 $
 */
public class TestWirecardCCPaymentResponse extends TestCase {

    /**
	 * The <code>Logger</code>.
	 */
    private static final Logger LOGGER = Logger.getLogger(TestWirecardCCPaymentResponse.class);

    /**
	 * @see junit.framework.TestCase#setUp()
	 * 
	 * @throws Exception
	 *             if an <code>Exception</code> occurs
	 */
    @Override
    protected final void setUp() throws Exception {
        super.setUp();
        new TestingEnvironment();
    }

    /**
	 * @see junit.framework.TestCase#tearDown()
	 * 
	 * @throws Exception
	 *             if an <code>Exception</code> occurs
	 */
    @Override
    protected final void tearDown() throws Exception {
        super.tearDown();
    }

    /**
	 * @throws Exception
	 *             if an <code>Exception</code> occurs
	 */
    public final void testWirecardCCPaymentResponse() throws Exception {
        WirecardCCPaymentResponse response1 = createTestWirecardCCPaymentResponse();
        StringWriter out = new StringWriter();
        JAXBContext ctx = Lifecycle.getJAXBContext();
        Marshaller marshaller = ctx.createMarshaller();
        marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
        marshaller.marshal(response1, out);
        LOGGER.info(System.getProperty("line.separator") + out.toString());
        Unmarshaller unmarshaller = ctx.createUnmarshaller();
        WirecardCCPaymentResponse response2 = (WirecardCCPaymentResponse) unmarshaller.unmarshal(new StringReader(out.toString()));
        compareTestWirecardCCPaymentResponses(response1, response2);
    }

    /**
	 * Returns a <code>WirecardCCPaymentResponse</code> for testing.
	 * 
	 * @return a <code>WirecardCCPaymentResponse</code> for testing
	 */
    public static final WirecardCCPaymentResponse createTestWirecardCCPaymentResponse() {
        WirecardCCPaymentResponse response = new WirecardCCPaymentResponse();
        response.setPaymentRequest(TestWirecardCCPaymentRequest.createTestWirecardCCPaymentRequest());
        response.setPaymentStatus(PaymentStatus.INTERRUPTED);
        response.setErrorMessage("TestErrorMessage");
        response.setTransactionId("TestTransactionId");
        response.setGuWID("TestGuWID");
        return response;
    }

    /**
	 * Roughly compares the two <code>WirecardCCPaymentResponse</code> s. If the
	 * comparison fails, an <code>AssertionFailedError</code> is thrown.
	 * 
	 * @param response1
	 *            the first <code>WirecardCCPaymentResponse</code>
	 * @param response2
	 *            the second <code>WirecardCCPaymentResponse</code>
	 */
    public static final void compareTestWirecardCCPaymentResponses(final WirecardCCPaymentResponse response1, final WirecardCCPaymentResponse response2) {
        TestWirecardCCPaymentRequest.compareTestWirecardCCPaymentRequests(response1.getPaymentRequest(), response2.getPaymentRequest());
        assertEquals(response1.getPaymentStatus(), response2.getPaymentStatus());
        assertEquals(response1.getErrorMessage(), response2.getErrorMessage());
        assertEquals(response1.getTransactionId(), response2.getTransactionId());
        assertEquals(response1.getGuWID(), response2.getGuWID());
    }
}
