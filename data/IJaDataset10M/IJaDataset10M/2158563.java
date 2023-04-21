package org.apache.ws.secpolicy11.builders;

import java.util.Iterator;
import java.util.List;
import javax.xml.namespace.QName;
import org.apache.axiom.om.OMAttribute;
import org.apache.axiom.om.OMElement;
import org.apache.neethi.Assertion;
import org.apache.neethi.AssertionBuilderFactory;
import org.apache.neethi.Policy;
import org.apache.neethi.PolicyEngine;
import org.apache.neethi.builders.AssertionBuilder;
import org.apache.ws.secpolicy.SP11Constants;
import org.apache.ws.secpolicy.SPConstants;
import org.apache.ws.secpolicy.model.X509Token;

public class X509TokenBuilder implements AssertionBuilder {

    public static final String USER_CERT_ALIAS_LN = "userCertAlias";

    public static final String ENCRYPTION_USER_LN = "encryptionUser";

    public static final QName RAMPART_CONFIG = new QName("http://ws.apache.org/rampart/policy", "RampartConfig");

    public static final QName USER_CERT_ALIAS = new QName("http://ws.apache.org/rampart/policy", USER_CERT_ALIAS_LN);

    public static final QName ENCRYPTION_USER = new QName("http://ws.apache.org/rampart/policy", ENCRYPTION_USER_LN);

    public Assertion build(OMElement element, AssertionBuilderFactory factory) throws IllegalArgumentException {
        X509Token x509Token = new X509Token(SPConstants.SP_V11);
        OMElement policyElement = element.getFirstElement();
        OMAttribute includeAttr = element.getAttribute(SP11Constants.INCLUDE_TOKEN);
        if (includeAttr != null) {
            int inclusion = SP11Constants.getInclusionFromAttributeValue(includeAttr.getAttributeValue());
            x509Token.setInclusion(inclusion);
        }
        if (policyElement != null) {
            if (policyElement.getFirstChildWithName(SP11Constants.REQUIRE_DERIVED_KEYS) != null) {
                x509Token.setDerivedKeys(true);
            }
            Policy policy = PolicyEngine.getPolicy(element.getFirstElement());
            policy = (Policy) policy.normalize(false);
            for (Iterator iterator = policy.getAlternatives(); iterator.hasNext(); ) {
                processAlternative((List) iterator.next(), x509Token);
                break;
            }
        }
        if (x509Token != null && policyElement != null) {
            OMElement ramp = null;
            ramp = policyElement.getFirstChildWithName(RAMPART_CONFIG);
            if (ramp != null) {
                OMElement child = null;
                if ((child = ramp.getFirstChildWithName(USER_CERT_ALIAS)) != null) {
                    x509Token.setUserCertAlias(child.getText());
                }
                if ((child = ramp.getFirstChildWithName(ENCRYPTION_USER)) != null) {
                    x509Token.setEncryptionUser(child.getText());
                }
            }
        }
        return x509Token;
    }

    private void processAlternative(List assertions, X509Token parent) {
        Assertion assertion;
        QName name;
        for (Iterator iterator = assertions.iterator(); iterator.hasNext(); ) {
            assertion = (Assertion) iterator.next();
            name = assertion.getName();
            if (SP11Constants.REQUIRE_KEY_IDENTIFIRE_REFERENCE.equals(name)) {
                parent.setRequireKeyIdentifierReference(true);
            } else if (SP11Constants.REQUIRE_ISSUER_SERIAL_REFERENCE.equals(name)) {
                parent.setRequireIssuerSerialReference(true);
            } else if (SP11Constants.REQUIRE_EMBEDDED_TOKEN_REFERENCE.equals(name)) {
                parent.setRequireEmbeddedTokenReference(true);
            } else if (SP11Constants.REQUIRE_THUMBPRINT_REFERENCE.equals(name)) {
                parent.setRequireThumbprintReference(true);
            } else if (SP11Constants.WSS_X509_V1_TOKEN_10.equals(name)) {
                parent.setTokenVersionAndType(SPConstants.WSS_X509_V1_TOKEN10);
            } else if (SP11Constants.WSS_X509_V1_TOKEN_11.equals(name)) {
                parent.setTokenVersionAndType(SPConstants.WSS_X509_V1_TOKEN11);
            } else if (SP11Constants.WSS_X509_V3_TOKEN_10.equals(name)) {
                parent.setTokenVersionAndType(SPConstants.WSS_X509_V3_TOKEN10);
            } else if (SP11Constants.WSS_X509_V3_TOKEN_11.equals(name)) {
                parent.setTokenVersionAndType(SPConstants.WSS_X509_V3_TOKEN11);
            } else if (SP11Constants.WSS_X509_PKCS7_TOKEN_10.equals(name)) {
                parent.setTokenVersionAndType(SPConstants.WSS_X509_PKCS7_TOKEN10);
            } else if (SP11Constants.WSS_X509_PKCS7_TOKEN_11.equals(name)) {
                parent.setTokenVersionAndType(SPConstants.WSS_X509_PKCS7_TOKEN11);
            } else if (SP11Constants.WSS_X509_PKI_PATH_V1_TOKEN_10.equals(name)) {
                parent.setTokenVersionAndType(SPConstants.WSS_X509_PKI_PATH_V1_TOKEN10);
            } else if (SP11Constants.WSS_X509_PKI_PATH_V1_TOKEN_11.equals(name)) {
                parent.setTokenVersionAndType(SPConstants.WSS_X509_PKI_PATH_V1_TOKEN11);
            }
        }
    }

    public QName[] getKnownElements() {
        return new QName[] { SP11Constants.X509_TOKEN };
    }
}
