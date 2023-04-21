package org.apache.axis2.saaj;

import junit.framework.TestCase;
import javax.xml.soap.MessageFactory;
import javax.xml.soap.Node;
import javax.xml.soap.SAAJResult;
import javax.xml.soap.SOAPBody;
import javax.xml.soap.SOAPEnvelope;
import javax.xml.soap.SOAPHeader;
import javax.xml.soap.SOAPHeaderElement;
import javax.xml.soap.SOAPMessage;
import javax.xml.soap.SOAPPart;

/**
 * 
 */
public class SAAJResultTest extends TestCase {

    private SOAPMessage msg = null;

    private SOAPPart sp = null;

    private SOAPBody body = null;

    private SOAPEnvelope envelope = null;

    private SOAPHeader header = null;

    private SOAPHeaderElement headerEle = null;

    protected void setUp() throws Exception {
        msg = MessageFactory.newInstance().createMessage();
        sp = msg.getSOAPPart();
        envelope = sp.getEnvelope();
        body = envelope.getBody();
        header = envelope.getHeader();
        headerEle = header.addHeaderElement(envelope.createName("foo", "f", "foo-URI"));
        headerEle.setActor("actor-URI");
    }

    public void testGetResultSOAPMessage() throws Exception {
        SAAJResult sr = new SAAJResult(msg);
        Node node = sr.getResult();
        assertNotNull(node);
    }
}
