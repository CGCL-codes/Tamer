package org.apache.rampart.handler.config;

import org.apache.rampart.handler.WSSHandlerConstants;
import org.apache.ws.security.handler.WSHandlerConstants;
import junit.framework.TestCase;

public class InflowConfigurationTest extends TestCase {

    public InflowConfigurationTest() {
        super();
    }

    public InflowConfigurationTest(String name) {
        super(name);
    }

    public void testGetProperty() {
        String actionItems = "Timestamp Signature Encrypt";
        String sigPropFile = "sig.properties";
        String decPropFile = "enc.properties";
        String pwcb = "org.apache.axis2.security.PWCallback";
        InflowConfiguration ifc = new InflowConfiguration();
        ifc.setActionItems(actionItems);
        ifc.setSignaturePropFile(sigPropFile);
        ifc.setDecryptionPropFile(decPropFile);
        ifc.setPasswordCallbackClass(pwcb);
        assertTrue("Action items missing", -1 < ifc.getProperty().getParameterElement().toString().indexOf("<" + WSSHandlerConstants.ACTION_ITEMS + ">" + actionItems + "</" + WSSHandlerConstants.ACTION_ITEMS + ">"));
        assertTrue("passwordCallbackClass missing", -1 < ifc.getProperty().getParameterElement().toString().indexOf("<" + WSHandlerConstants.PW_CALLBACK_CLASS + ">" + pwcb + "</" + WSHandlerConstants.PW_CALLBACK_CLASS + ">"));
        assertTrue("sigPropFile missing", -1 < ifc.getProperty().getParameterElement().toString().indexOf("<" + WSHandlerConstants.SIG_PROP_FILE + ">" + sigPropFile + "</" + WSHandlerConstants.SIG_PROP_FILE + ">"));
        assertTrue("decPropFile missing", -1 < ifc.getProperty().getParameterElement().toString().indexOf("<" + WSHandlerConstants.DEC_PROP_FILE + ">" + decPropFile + "</" + WSHandlerConstants.DEC_PROP_FILE + ">"));
    }
}
