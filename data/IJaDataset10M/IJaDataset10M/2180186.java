package org.apache.axis2.jaxws.sample;

import junit.framework.Test;
import junit.framework.TestSuite;
import org.apache.axis2.jaxws.TestLogger;
import org.apache.axis2.jaxws.framework.AbstractTestCase;
import org.apache.axis2.jaxws.sample.addnumbers.AddNumbersPortType;
import org.apache.axis2.jaxws.sample.addnumbers.AddNumbersService;
import javax.xml.ws.BindingProvider;
import javax.xml.ws.handler.MessageContext;
import java.util.Map;

public class AddNumbersTests extends AbstractTestCase {

    String axisEndpoint = "http://localhost:6060/axis2/services/AddNumbersService.AddNumbersPortTypeImplPort";

    public static Test suite() {
        return getTestSetup(new TestSuite(AddressBookTests.class));
    }

    public void testAddNumbers() throws Exception {
        TestLogger.logger.debug("----------------------------------");
        TestLogger.logger.debug("test: " + getName());
        AddNumbersService service = new AddNumbersService();
        AddNumbersPortType proxy = service.getAddNumbersPort();
        BindingProvider p = (BindingProvider) proxy;
        p.getRequestContext().put(BindingProvider.ENDPOINT_ADDRESS_PROPERTY, axisEndpoint);
        int total = proxy.addNumbers(10, 10);
        TestLogger.logger.debug("Total =" + total);
        assertEquals("sum", 20, total);
        assertEquals("http response code", new Integer(200), p.getResponseContext().get(MessageContext.HTTP_RESPONSE_CODE));
        Map headers = (Map) p.getResponseContext().get(MessageContext.HTTP_RESPONSE_HEADERS);
        assertTrue("http response headers", headers != null && !headers.isEmpty());
        total = proxy.addNumbers(10, 10);
        TestLogger.logger.debug("Total =" + total);
        assertEquals("sum", 20, total);
        assertEquals("http response code", new Integer(200), p.getResponseContext().get(MessageContext.HTTP_RESPONSE_CODE));
        headers = (Map) p.getResponseContext().get(MessageContext.HTTP_RESPONSE_HEADERS);
        assertTrue("http response headers", headers != null && !headers.isEmpty());
    }

    public void testOneWay() {
        try {
            TestLogger.logger.debug("----------------------------------");
            TestLogger.logger.debug("test: " + getName());
            AddNumbersService service = new AddNumbersService();
            AddNumbersPortType proxy = service.getAddNumbersPort();
            BindingProvider bp = (BindingProvider) proxy;
            bp.getRequestContext().put(BindingProvider.ENDPOINT_ADDRESS_PROPERTY, axisEndpoint);
            proxy.oneWayInt(11);
            proxy.oneWayInt(11);
            TestLogger.logger.debug("----------------------------------");
        } catch (Exception e) {
            e.printStackTrace();
            fail();
        }
    }
}
