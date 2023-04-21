package org.apache.axis2.jaxws.description;

import junit.framework.TestCase;
import org.apache.axis2.jaxws.description.xml.handler.HandlerChainsType;
import javax.jws.HandlerChain;
import javax.jws.WebService;
import javax.xml.ws.WebServiceException;

/**
 * 
 */
public class HandlerChainConfigFileTests extends TestCase {

    public void testValidConfigFile() {
        ServiceDescription svcDesc = DescriptionFactory.createServiceDescription(ValidConfigFileImpl.class);
        EndpointDescription[] epDescs = svcDesc.getEndpointDescriptions();
        assertEquals(1, epDescs.length);
        EndpointDescription epDesc = epDescs[0];
        HandlerChainsType hct = epDesc.getHandlerChain();
        assertNotNull(hct);
    }

    public void testMissingRelativeConfigFile() {
        try {
            ServiceDescription svcDesc = DescriptionFactory.createServiceDescription(InvalidConfigFileImpl.class);
            EndpointDescription[] epDescs = svcDesc.getEndpointDescriptions();
            assertEquals(1, epDescs.length);
            EndpointDescription epDesc = epDescs[0];
            HandlerChainsType hct = epDesc.getHandlerChain();
            fail("Should have caught exception for a missing handler config file");
        } catch (WebServiceException e) {
            String message = e.toString();
        } catch (Exception e) {
            fail("Expected a WebServiceException, but caught: " + e);
        }
    }

    public void testMissingAbsoluteConfigFile() {
        try {
            ServiceDescription svcDesc = DescriptionFactory.createServiceDescription(InvalidAbsoluteConfigFileImpl.class);
            EndpointDescription[] epDescs = svcDesc.getEndpointDescriptions();
            assertEquals(1, epDescs.length);
            EndpointDescription epDesc = epDescs[0];
            HandlerChainsType hct = epDesc.getHandlerChain();
            fail("Should have caught exception for a missing handler config file");
        } catch (WebServiceException e) {
            String message = e.toString();
        } catch (Exception e) {
            fail("Expected a WebServiceException, but caught: " + e);
        }
    }
}

@WebService()
@HandlerChain(file = "HandlerConfigFile.xml")
class ValidConfigFileImpl {
}

@WebService()
@HandlerChain(file = "MissingHandlerConfigFile.xml")
class InvalidConfigFileImpl {
}

@WebService()
@HandlerChain(file = "http://localhost/will/not/find/MissingHandlerConfigFile.xml")
class InvalidAbsoluteConfigFileImpl {
}
