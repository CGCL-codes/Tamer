package org.openliberty.wsc.test;

import org.joda.time.DateTime;
import org.joda.time.format.ISODateTimeFormat;
import org.openliberty.xmltooling.OpenLibertyHelpers;
import org.openliberty.xmltooling.epr.EndpointReference;
import org.openliberty.xmltooling.epr.Framework;
import org.openliberty.xmltooling.security.Created;
import org.openliberty.xmltooling.security.Security;
import org.openliberty.xmltooling.security.Timestamp;
import org.openliberty.xmltooling.soapbinding.Action;
import org.openliberty.xmltooling.soapbinding.MessageID;
import org.openliberty.xmltooling.soapbinding.To;
import org.opensaml.common.BaseComplexSAMLObjectTestCase;
import org.opensaml.saml2.core.Assertion;
import org.opensaml.ws.soap.soap11.Envelope;
import org.opensaml.ws.soap.soap11.Header;
import org.opensaml.ws.soap.util.SOAPConstants;
import org.opensaml.xml.util.XMLHelper;

/**
 * @author tguion
 *
 */
public class DiscoBootstrapTest extends BaseComplexSAMLObjectTestCase {

    @Override
    public void testMarshall() {
    }

    @Override
    public void testUnmarshall() {
    }

    public void testDiscoBootstrap() {
        EndpointReference epr = getDiscoEndpointReference();
        Envelope envelope = issueDiscoQuery(epr);
    }

    private EndpointReference getDiscoEndpointReference() {
        return null;
    }

    /**
	 * Issue a DISCO Query
     * 
	 * @param epr
	 * @return envelope reponse
	 */
    private Envelope issueDiscoQuery(EndpointReference epr) {
        return null;
    }

    /**
	 * Build SOAP header 
	 * @param envelope
	 * @return
	 */
    protected Header buildHeader(Envelope envelope) {
        Header header = envelope.getHeader();
        Framework fw = (Framework) builderFactory.getBuilder(Framework.DEFAULT_ELEMENT_NAME).buildObject(Framework.DEFAULT_ELEMENT_NAME);
        fw.getUnknownAttributes().put(XMLHelper.constructQName(SOAPConstants.SOAP11_NS, "mustUnderstand", "S"), "1");
        fw.getUnknownAttributes().put(XMLHelper.constructQName(SOAPConstants.SOAP11_NS, "actor", "S"), "http://schemas.xmlsoap.org/soap/actor/next");
        fw.getUnknownAttributes().put(XMLHelper.constructQName("id", fw), "sbHdrFramework");
        fw.setVersion("2.0");
        header.getUnknownXMLObjects().add(fw);
        MessageID messageID = (MessageID) builderFactory.getBuilder(MessageID.DEFAULT_ELEMENT_NAME).buildObject(MessageID.DEFAULT_ELEMENT_NAME);
        messageID.getUnknownAttributes().put(XMLHelper.constructQName(SOAPConstants.SOAP11_NS, "mustUnderstand", "S"), "1");
        messageID.getUnknownAttributes().put(XMLHelper.constructQName(SOAPConstants.SOAP11_NS, "actor", "S"), "http://schemas.xmlsoap.org/soap/actor/next");
        messageID.getUnknownAttributes().put(XMLHelper.constructQName("id", messageID), "msgHdrID");
        messageID.setValue("uuid:asdqwer-238asf-4693970d-000d0c53");
        header.getUnknownXMLObjects().add(messageID);
        To to = (To) builderFactory.getBuilder(To.DEFAULT_ELEMENT_NAME).buildObject(To.DEFAULT_ELEMENT_NAME);
        to.getUnknownAttributes().put(XMLHelper.constructQName(SOAPConstants.SOAP11_NS, "mustUnderstand", "S"), "1");
        to.getUnknownAttributes().put(XMLHelper.constructQName(SOAPConstants.SOAP11_NS, "actor", "S"), "http://schemas.xmlsoap.org/soap/actor/next");
        to.getUnknownAttributes().put(XMLHelper.constructQName("id", messageID), "wsaToID");
        to.setValue("https://i-idp.liberty-iop.org:8481/axis/services/LibertyAS2");
        header.getUnknownXMLObjects().add(to);
        Action action = (Action) builderFactory.getBuilder(Action.DEFAULT_ELEMENT_NAME).buildObject(Action.DEFAULT_ELEMENT_NAME);
        action.getUnknownAttributes().put(XMLHelper.constructQName(SOAPConstants.SOAP11_NS, "mustUnderstand", "S"), "1");
        action.getUnknownAttributes().put(XMLHelper.constructQName(SOAPConstants.SOAP11_NS, "actor", "S"), "http://schemas.xmlsoap.org/soap/actor/next");
        action.getUnknownAttributes().put(XMLHelper.constructQName("id", messageID), "wsaActionID");
        action.setValue("urn:liberty:sa:2006-08:SASLRequest");
        header.getUnknownXMLObjects().add(action);
        Security security = (Security) builderFactory.getBuilder(Security.DEFAULT_ELEMENT_NAME).buildObject(Security.DEFAULT_ELEMENT_NAME);
        security.getUnknownAttributes().put(XMLHelper.constructQName(SOAPConstants.SOAP11_NS, "mustUnderstand", "S"), "1");
        security.getUnknownAttributes().put(XMLHelper.constructQName(SOAPConstants.SOAP11_NS, "actor", "S"), "http://schemas.xmlsoap.org/soap/actor/next");
        Timestamp ts = (Timestamp) builderFactory.getBuilder(Timestamp.DEFAULT_ELEMENT_NAME).buildObject(Timestamp.DEFAULT_ELEMENT_NAME);
        ts.getUnknownAttributes().put(XMLHelper.constructQName(SOAPConstants.SOAP11_NS, "mustUnderstand", "S"), "1");
        ts.getUnknownAttributes().put(XMLHelper.constructQName(SOAPConstants.SOAP11_NS, "actor", "S"), "http://schemas.xmlsoap.org/soap/actor/next");
        ts.getUnknownAttributes().put(XMLHelper.constructQName("id", messageID), "WsuTimestampID");
        Created created = (Created) builderFactory.getBuilder(Created.DEFAULT_ELEMENT_NAME).buildObject(Created.DEFAULT_ELEMENT_NAME);
        String date = OpenLibertyHelpers.stringForDateTime(new DateTime());
        created.setValue(date);
        ts.setCreated(created);
        security.getUnknownXMLObjects().add(ts);
        header.getUnknownXMLObjects().add(security);
        return header;
    }

    /**
	 * @return known advisoryAuthnID
	 */
    private String getAdvisoryAuthnID() {
        return "conor";
    }

    /**
	 * @return known encrypted password
	 */
    private String getPassword() {
        return "AGNvbm9yAHRlc3Rjb25vcg==";
    }
}
