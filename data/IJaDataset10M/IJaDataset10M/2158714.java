package de.vsis.coordination.util;

import java.util.Iterator;
import org.apache.axiom.om.OMElement;
import org.apache.axiom.soap.SOAPHeaderBlock;
import org.apache.axis2.context.MessageContext;

public class RegistrationServiceParser {

    public static String execute(MessageContext msg) {
        Iterator<?> hbs = msg.getEnvelope().getHeader().examineAllHeaderBlocks();
        String regServiceEPR = "";
        while (hbs.hasNext()) {
            SOAPHeaderBlock hb = (SOAPHeaderBlock) hbs.next();
            if (hb.getLocalName().equalsIgnoreCase("CoordinationContext")) {
                Iterator<?> ch = hb.getChildElements();
                while (ch.hasNext()) {
                    OMElement element = (OMElement) ch.next();
                    if (element.getLocalName().equalsIgnoreCase("RegistrationService")) {
                        Iterator<?> ch2 = element.getChildElements();
                        while (ch2.hasNext()) {
                            OMElement element2 = (OMElement) ch2.next();
                            if (element2.getLocalName().equalsIgnoreCase("Address")) {
                                regServiceEPR = element2.getText();
                            }
                        }
                    }
                }
            }
        }
        return regServiceEPR;
    }
}
