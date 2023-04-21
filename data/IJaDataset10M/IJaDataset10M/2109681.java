package org.apache.rampart.policy;

import org.apache.axis2.policy.model.MTOMAssertion;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.neethi.Assertion;
import org.apache.rampart.policy.model.RampartConfig;
import org.apache.ws.secpolicy.WSSPolicyException;
import org.apache.ws.secpolicy.model.AsymmetricBinding;
import org.apache.ws.secpolicy.model.Binding;
import org.apache.ws.secpolicy.model.ContentEncryptedElements;
import org.apache.ws.secpolicy.model.EncryptionToken;
import org.apache.ws.secpolicy.model.Header;
import org.apache.ws.secpolicy.model.InitiatorToken;
import org.apache.ws.secpolicy.model.OpenIDToken;
import org.apache.ws.secpolicy.model.ProtectionToken;
import org.apache.ws.secpolicy.model.RecipientToken;
import org.apache.ws.secpolicy.model.RequiredElements;
import org.apache.ws.secpolicy.model.SignatureToken;
import org.apache.ws.secpolicy.model.SignedEncryptedElements;
import org.apache.ws.secpolicy.model.SignedEncryptedParts;
import org.apache.ws.secpolicy.model.SupportingToken;
import org.apache.ws.secpolicy.model.SymmetricAsymmetricBindingBase;
import org.apache.ws.secpolicy.model.SymmetricBinding;
import org.apache.ws.secpolicy.model.Token;
import org.apache.ws.secpolicy.model.TokenWrapper;
import org.apache.ws.secpolicy.model.TransportBinding;
import org.apache.ws.secpolicy.model.TransportToken;
import org.apache.ws.secpolicy.model.Trust10;
import org.apache.ws.secpolicy.model.Wss10;
import org.apache.ws.secpolicy.model.Wss11;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class RampartPolicyBuilder {

    private static Log log = LogFactory.getLog(RampartPolicyBuilder.class);

    /**
     * Compile the parsed security data into one Policy data block.
     * 
     * This methods loops over all top level Policy Engine data elements,
     * extracts the parsed parameters and sets them into a single data block.
     * During this processing the method prepares the parameters in a format
     * that is ready for processing by the WSS4J functions.
     * 
     * <p/>
     * 
     * The WSS4J policy enabled handler takes this data block to control the
     * setup of the security header.
     * 
     * @param topLevelAssertions
     *            The iterator of the top level policy assertions
     * @return The compile Poilcy data block.
     * @throws WSSPolicyException
     */
    public static RampartPolicyData build(List topLevelAssertions) throws WSSPolicyException {
        RampartPolicyData rpd = new RampartPolicyData();
        for (Iterator iter = topLevelAssertions.iterator(); iter.hasNext(); ) {
            Assertion assertion = (Assertion) iter.next();
            if (assertion instanceof Binding) {
                if (assertion instanceof SymmetricBinding) {
                    processSymmetricPolicyBinding((SymmetricBinding) assertion, rpd);
                } else if (assertion instanceof AsymmetricBinding) {
                    processAsymmetricPolicyBinding((AsymmetricBinding) assertion, rpd);
                } else {
                    processTransportBinding((TransportBinding) assertion, rpd);
                }
            } else if (assertion instanceof Wss11) {
                processWSS11((Wss11) assertion, rpd);
            } else if (assertion instanceof Wss10) {
                processWSS10((Wss10) assertion, rpd);
            } else if (assertion instanceof SignedEncryptedElements) {
                processSignedEncryptedElements((SignedEncryptedElements) assertion, rpd);
            } else if (assertion instanceof SignedEncryptedParts) {
                processSignedEncryptedParts((SignedEncryptedParts) assertion, rpd);
            } else if (assertion instanceof RequiredElements) {
                processRequiredElements((RequiredElements) assertion, rpd);
            } else if (assertion instanceof ContentEncryptedElements) {
                processContentEncryptedElements((ContentEncryptedElements) assertion, rpd);
            } else if (assertion instanceof SupportingToken) {
                processSupportingTokens((SupportingToken) assertion, rpd);
            } else if (assertion instanceof Trust10) {
                processTrust10((Trust10) assertion, rpd);
            } else if (assertion instanceof RampartConfig) {
                processRampartConfig((RampartConfig) assertion, rpd);
            } else if (assertion instanceof MTOMAssertion) {
                processMTOMSerialization((MTOMAssertion) assertion, rpd);
            } else {
                log.debug("Unknown top level PED found: " + assertion.getClass().getName());
            }
        }
        return rpd;
    }

    /**
     * @param binding
     * @param rpd
     */
    private static void processTransportBinding(TransportBinding binding, RampartPolicyData rpd) {
        binding(binding, rpd);
        rpd.setTransportBinding(true);
        TransportToken transportToken = binding.getTransportToken();
        if (transportToken != null) {
            rpd.setTransportToken(transportToken.getTransportToken());
        }
    }

    /**
     * Add TRust10 assertion info into rampart policy data
     * @param trust10
     * @param rpd
     */
    private static void processTrust10(Trust10 trust10, RampartPolicyData rpd) {
        rpd.setTrust10(trust10);
    }

    /**
     * Add the rampart configuration information into rampart policy data.
     * @param config
     * @param rpd
     */
    private static void processRampartConfig(RampartConfig config, RampartPolicyData rpd) {
        rpd.setRampartConfig(config);
    }

    /**
     * Evaluate the symmetric policy binding data.
     * 
     * @param binding
     *            The binding data
     * @param rpd
     *            The WSS4J data to initialize
     * @throws WSSPolicyException
     */
    private static void processSymmetricPolicyBinding(SymmetricBinding symmBinding, RampartPolicyData rpd) throws WSSPolicyException {
        rpd.setSymmetricBinding(true);
        binding(symmBinding, rpd);
        symmAsymmBinding(symmBinding, rpd);
        symmetricBinding(symmBinding, rpd);
    }

    private static void processWSS10(Wss10 wss10, RampartPolicyData rpd) {
        rpd.setWss10(wss10);
    }

    /**
     * Evaluate the asymmetric policy binding data.
     * 
     * @param binding
     *            The binding data
     * @param rpd
     *            The WSS4J data to initialize
     * @throws WSSPolicyException
     */
    private static void processAsymmetricPolicyBinding(AsymmetricBinding binding, RampartPolicyData rpd) throws WSSPolicyException {
        rpd.setAsymmetricBinding(true);
        binding(binding, rpd);
        symmAsymmBinding(binding, rpd);
        asymmetricBinding(binding, rpd);
    }

    private static void processWSS11(Wss11 wss11, RampartPolicyData rpd) {
        rpd.setSignatureConfirmation(wss11.isRequireSignatureConfirmation());
        rpd.setWss11(wss11);
    }

    /**
     * Populate elements to sign and/or encrypt with the message tokens.
     * 
     * @param sep
     *            The data describing the elements (XPath)
     * @param rpd
     *            The WSS4J data to initialize
     */
    private static void processSignedEncryptedElements(SignedEncryptedElements see, RampartPolicyData rpd) {
        Iterator it = see.getXPathExpressions().iterator();
        if (see.isSignedElemets()) {
            while (it.hasNext()) {
                rpd.setSignedElements((String) it.next());
            }
        } else {
            while (it.hasNext()) {
                rpd.setEncryptedElements((String) it.next());
            }
        }
        rpd.addDeclaredNamespaces(see.getDeclaredNamespaces());
    }

    /**
     * Populate parts to sign and/or encrypt with the message tokens.
     * 
     * @param sep
     *            The data describing the parts
     * @param rpd
     *            The WSS4J data to initialize
     */
    private static void processSignedEncryptedParts(SignedEncryptedParts sep, RampartPolicyData rpd) {
        Iterator it = sep.getHeaders().iterator();
        if (sep.isSignedParts()) {
            rpd.setSignBody(sep.isBody());
            rpd.setSignAttachments(sep.isAttachments());
            while (it.hasNext()) {
                Header header = (Header) it.next();
                rpd.addSignedPart(header.getNamespace(), header.getName());
            }
        } else {
            rpd.setEncryptBody(sep.isBody());
            rpd.setEncryptAttachments(sep.isAttachments());
            while (it.hasNext()) {
                Header header = (Header) it.next();
                rpd.setEncryptedParts(header.getNamespace(), header.getName(), "Header");
            }
        }
    }

    private static void processContentEncryptedElements(ContentEncryptedElements cee, RampartPolicyData rpd) {
        Iterator it = cee.getXPathExpressions().iterator();
        while (it.hasNext()) {
            rpd.setContentEncryptedElements((String) it.next());
        }
        rpd.addDeclaredNamespaces(cee.getDeclaredNamespaces());
    }

    private static void processRequiredElements(RequiredElements req, RampartPolicyData rpd) {
        Iterator it = req.getXPathExpressions().iterator();
        while (it.hasNext()) {
            rpd.setRequiredElements((String) it.next());
        }
        rpd.addDeclaredNamespaces(req.getDeclaredNamespaces());
    }

    /**
     * Evaluate policy data that is common to all bindings.
     * 
     * @param binding
     *            The common binding data
     * @param rpd
     *            The WSS4J data to initialize
     */
    private static void binding(Binding binding, RampartPolicyData rpd) {
        rpd.setLayout(binding.getLayout().getValue());
        rpd.setIncludeTimestamp(binding.isIncludeTimestamp());
        rpd.setAlgorithmSuite(binding.getAlgorithmSuite());
    }

    /**
     * Evaluate policy data that is common to symmetric and asymmetric bindings.
     * 
     * @param binding
     *            The symmetric/asymmetric binding data
     * @param rpd
     *            The WSS4J data to initialize
     */
    private static void symmAsymmBinding(SymmetricAsymmetricBindingBase binding, RampartPolicyData rpd) {
        rpd.setEntireHeadersAndBodySignatures(binding.isEntireHeadersAndBodySignatures());
        rpd.setProtectionOrder(binding.getProtectionOrder());
        rpd.setSignatureProtection(binding.isSignatureProtection());
        rpd.setTokenProtection(binding.isTokenProtection());
        rpd.setAlgorithmSuite(binding.getAlgorithmSuite());
    }

    /**
     * Evaluate policy data that is specific to symmetric binding.
     * 
     * @param binding
     *            The symmetric binding data
     * @param rpd
     *            The WSS4J data to initialize
     */
    private static void symmetricBinding(SymmetricBinding binding, RampartPolicyData rpd) throws WSSPolicyException {
        Assertion token = binding.getProtectionToken();
        if (token != null) {
            rpd.setProtectionToken(((ProtectionToken) token).getProtectionToken());
        } else {
            Assertion encrToken = binding.getEncryptionToken();
            Assertion sigToken = binding.getSignatureToken();
            if (token == null && sigToken == null) {
                throw new WSSPolicyException("Symmetric binding should have a Protection token or" + " both Signature and Encryption tokens defined");
            }
            rpd.setEncryptionToken(((EncryptionToken) encrToken).getEncryptionToken());
            rpd.setSignatureToken(((SignatureToken) sigToken).getSignatureToken());
        }
    }

    /**
     * Evaluate policy data that is specific to asymmetric binding.
     * 
     * @param binding
     *            The asymmetric binding data
     * @param rpd
     *            The WSS4J data to initialize
     */
    private static void asymmetricBinding(AsymmetricBinding binding, RampartPolicyData rpd) throws WSSPolicyException {
        TokenWrapper tokWrapper = binding.getRecipientToken();
        TokenWrapper tokWrapper1 = binding.getInitiatorToken();
        if (tokWrapper == null || tokWrapper1 == null) {
            throw new WSSPolicyException("Asymmetric binding should have both Initiator and " + "Recipient tokens defined");
        }
        rpd.setRecipientToken(((RecipientToken) tokWrapper).getReceipientToken());
        rpd.setInitiatorToken(((InitiatorToken) tokWrapper1).getInitiatorToken());
    }

    private static void processSupportingTokens(SupportingToken token, RampartPolicyData rpd) throws WSSPolicyException {
        rpd.setSupportingTokens(token);
        ArrayList tokens = token.getTokens();
        for (Iterator iter = tokens.iterator(); iter.hasNext(); ) {
            Token supToken = (Token) iter.next();
            if (supToken instanceof OpenIDToken) {
                rpd.setOit((OpenIDToken) supToken);
            }
        }
    }

    private static void processMTOMSerialization(MTOMAssertion mtomAssertion, RampartPolicyData rpd) {
        rpd.setMTOMAssertion(mtomAssertion);
    }
}
