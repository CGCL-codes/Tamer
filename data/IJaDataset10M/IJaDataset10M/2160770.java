package net.javacrumbs.springws.test.helper;

import net.javacrumbs.springws.test.validator.AbstractValidatorTest;
import org.junit.Test;
import org.springframework.ws.WebServiceMessage;
import org.springframework.ws.soap.SoapVersion;
import org.springframework.ws.soap.saaj.SaajSoapMessageFactory;

/**
 * SOAP 1.2 tests
 * @author Lukas Krecan
 *
 */
public class MessageValidator12Test extends AbstractValidatorTest {

    @Override
    protected void initializeMessageFactory() {
        try {
            SaajSoapMessageFactory newMessageFactory = new SaajSoapMessageFactory();
            newMessageFactory.setSoapVersion(SoapVersion.SOAP_12);
            newMessageFactory.afterPropertiesSet();
            messageFactory = newMessageFactory;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    public void testAssertSoapMessageFault() throws Exception {
        WebServiceMessage message = createMessage("xml/fault-1_2.xml");
        new MessageValidator(message).assertSoapMessage().assertSoapFault().assertFaultCode("Sender").assertFaultStringOrReason("Sender Timeout").assertFaultActorOrRole("http://www.w3.org/2003/05/soap-envelope/role/next");
    }
}
