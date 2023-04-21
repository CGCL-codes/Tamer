package cartagows.wsframework.wssecurity.policy.builders;

import java.util.Iterator;
import java.util.List;
import javax.xml.namespace.QName;
import org.apache.axiom.om.OMElement;
import org.apache.neethi.Assertion;
import org.apache.neethi.AssertionBuilderFactory;
import org.apache.neethi.Policy;
import org.apache.neethi.PolicyEngine;
import org.apache.neethi.builders.AssertionBuilder;
import cartagows.wsframework.wssecurity.policy.SP11Constants;
import cartagows.wsframework.wssecurity.policy.SPConstants;
import cartagows.wsframework.wssecurity.policy.model.Wss10;

public class WSS10Builder implements AssertionBuilder {

    public Assertion build(OMElement element, AssertionBuilderFactory factory) throws IllegalArgumentException {
        Wss10 wss10 = new Wss10(SPConstants.SP_V11);
        Policy policy = PolicyEngine.getPolicy(element.getFirstElement());
        policy = (Policy) policy.normalize(false);
        for (Iterator iterator = policy.getAlternatives(); iterator.hasNext(); ) {
            processAlternative((List) iterator.next(), wss10);
            break;
        }
        return wss10;
    }

    public QName[] getKnownElements() {
        return new QName[] { SP11Constants.WSS10 };
    }

    private void processAlternative(List assertions, Wss10 parent) {
        Assertion assertion;
        QName name;
        for (Iterator iterator = assertions.iterator(); iterator.hasNext(); ) {
            assertion = (Assertion) iterator.next();
            name = assertion.getName();
            if (SP11Constants.MUST_SUPPORT_REF_KEY_IDENTIFIER.equals(name)) {
                parent.setMustSupportRefKeyIdentifier(true);
            } else if (SP11Constants.MUST_SUPPORT_REF_ISSUER_SERIAL.equals(name)) {
                parent.setMustSupportRefIssuerSerial(true);
            } else if (SP11Constants.MUST_SUPPORT_REF_EXTERNAL_URI.equals(name)) {
                parent.setMustSupportRefExternalURI(true);
            } else if (SP11Constants.MUST_SUPPORT_REF_EMBEDDED_TOKEN.equals(name)) {
                parent.setMustSupportRefEmbeddedToken(true);
            }
        }
    }
}
