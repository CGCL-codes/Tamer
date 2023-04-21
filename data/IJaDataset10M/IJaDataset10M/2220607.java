package org.apache.rahas.impl;

import java.security.Principal;
import java.security.SecureRandom;
import java.security.cert.X509Certificate;
import java.text.DateFormat;
import java.util.Arrays;
import java.util.Date;
import org.apache.axiom.om.OMElement;
import org.apache.axiom.om.OMNode;
import org.apache.axiom.om.impl.dom.jaxp.DocumentBuilderFactoryImpl;
import org.apache.axiom.soap.SOAPEnvelope;
import org.apache.axis2.context.MessageContext;
import org.apache.axis2.description.Parameter;
import org.apache.rahas.RahasConstants;
import org.apache.rahas.RahasData;
import org.apache.rahas.Token;
import org.apache.rahas.TokenIssuer;
import org.apache.rahas.TrustException;
import org.apache.rahas.TrustUtil;
import org.apache.rahas.impl.util.SAMLAttributeCallback;
import org.apache.rahas.impl.util.SAMLCallbackHandler;
import org.apache.rahas.impl.util.SAMLNameIdentifierCallback;
import org.apache.ws.security.WSConstants;
import org.apache.ws.security.WSSecurityException;
import org.apache.ws.security.WSUsernameTokenPrincipal;
import org.apache.ws.security.components.crypto.Crypto;
import org.apache.ws.security.components.crypto.CryptoFactory;
import org.apache.ws.security.message.WSSecEncryptedKey;
import org.apache.ws.security.util.Base64;
import org.apache.ws.security.util.Loader;
import org.apache.ws.security.util.XmlSchemaDateFormat;
import org.apache.xml.security.signature.XMLSignature;
import org.apache.xml.security.utils.EncryptionConstants;
import org.opensaml.SAMLAssertion;
import org.opensaml.SAMLAttribute;
import org.opensaml.SAMLAttributeStatement;
import org.opensaml.SAMLAuthenticationStatement;
import org.opensaml.SAMLException;
import org.opensaml.SAMLNameIdentifier;
import org.opensaml.SAMLStatement;
import org.opensaml.SAMLSubject;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.Text;

/**
 * Issuer to issue SAMl tokens
 */
public class SAMLTokenIssuer implements TokenIssuer {

    private String configParamName;

    private OMElement configElement;

    private String configFile;

    public SOAPEnvelope issue(RahasData data) throws TrustException {
        try {
            MessageContext inMsgCtx = data.getInMessageContext();
            SAMLTokenIssuerConfig config = null;
            if (this.configElement != null) {
                config = new SAMLTokenIssuerConfig(configElement.getFirstChildWithName(SAMLTokenIssuerConfig.SAML_ISSUER_CONFIG));
            }
            if (config == null && this.configFile != null) {
                config = new SAMLTokenIssuerConfig(this.configFile);
            }
            if (config == null && this.configParamName != null) {
                Parameter param = inMsgCtx.getParameter(this.configParamName);
                if (param != null && param.getParameterElement() != null) {
                    config = new SAMLTokenIssuerConfig(param.getParameterElement().getFirstChildWithName(SAMLTokenIssuerConfig.SAML_ISSUER_CONFIG));
                } else {
                    throw new TrustException("expectedParameterMissing", new String[] { this.configParamName });
                }
            }
            if (config == null) {
                throw new TrustException("configurationIsNull");
            }
            DocumentBuilderFactoryImpl.setDOOMRequired(true);
            SOAPEnvelope env = TrustUtil.createSOAPEnvelope(inMsgCtx.getEnvelope().getNamespace().getNamespaceURI());
            Crypto crypto;
            if (config.cryptoElement != null) {
                crypto = CryptoFactory.getInstance(TrustUtil.toProperties(config.cryptoElement), inMsgCtx.getAxisService().getClassLoader());
            } else {
                crypto = CryptoFactory.getInstance(config.cryptoPropertiesFile, inMsgCtx.getAxisService().getClassLoader());
            }
            Date creationTime = new Date();
            Date expirationTime = new Date();
            expirationTime.setTime(creationTime.getTime() + config.ttl);
            Document doc = ((Element) env).getOwnerDocument();
            int keySize = data.getKeysize();
            keySize = (keySize == -1) ? config.keySize : keySize;
            String keyType = data.getKeyType();
            SAMLAssertion assertion;
            if (keyType == null) {
                throw new TrustException(TrustException.INVALID_REQUEST, new String[] { "Requested KeyType is missing" });
            }
            if (keyType.endsWith(RahasConstants.KEY_TYPE_SYMM_KEY) || keyType.endsWith(RahasConstants.KEY_TYPE_PUBLIC_KEY)) {
                assertion = createHoKAssertion(config, doc, crypto, creationTime, expirationTime, data);
            } else if (keyType.endsWith(RahasConstants.KEY_TYPE_BEARER)) {
                assertion = createBearerAssertion(config, doc, crypto, creationTime, expirationTime, data);
            } else {
                throw new TrustException("unsupportedKeyType");
            }
            OMElement rstrElem;
            int wstVersion = data.getVersion();
            if (RahasConstants.VERSION_05_02 == wstVersion) {
                rstrElem = TrustUtil.createRequestSecurityTokenResponseElement(wstVersion, env.getBody());
            } else {
                OMElement rstrcElem = TrustUtil.createRequestSecurityTokenResponseCollectionElement(wstVersion, env.getBody());
                rstrElem = TrustUtil.createRequestSecurityTokenResponseElement(wstVersion, rstrcElem);
            }
            TrustUtil.createTokenTypeElement(wstVersion, rstrElem).setText(RahasConstants.TOK_TYPE_SAML_10);
            if (keyType.endsWith(RahasConstants.KEY_TYPE_SYMM_KEY)) {
                TrustUtil.createKeySizeElement(wstVersion, rstrElem, keySize);
            }
            if (config.addRequestedAttachedRef) {
                TrustUtil.createRequestedAttachedRef(rstrElem, assertion.getId(), wstVersion);
            }
            if (config.addRequestedUnattachedRef) {
                TrustUtil.createRequestedUnattachedRef(rstrElem, assertion.getId(), wstVersion);
            }
            if (data.getAppliesToAddress() != null) {
                TrustUtil.createAppliesToElement(rstrElem, data.getAppliesToAddress(), data.getAddressingNs());
            }
            DateFormat zulu = new XmlSchemaDateFormat();
            TrustUtil.createLifetimeElement(wstVersion, rstrElem, zulu.format(creationTime), zulu.format(expirationTime));
            OMElement reqSecTokenElem = TrustUtil.createRequestedSecurityTokenElement(wstVersion, rstrElem);
            Token assertionToken;
            try {
                Node tempNode = assertion.toDOM();
                reqSecTokenElem.addChild((OMNode) ((Element) rstrElem).getOwnerDocument().importNode(tempNode, true));
                assertionToken = new Token(assertion.getId(), (OMElement) assertion.toDOM(), creationTime, expirationTime);
                assertionToken.setSecret(data.getEphmeralKey());
                TrustUtil.getTokenStore(inMsgCtx).add(assertionToken);
            } catch (SAMLException e) {
                throw new TrustException("samlConverstionError", e);
            }
            if (keyType.endsWith(RahasConstants.KEY_TYPE_SYMM_KEY) && config.keyComputation != SAMLTokenIssuerConfig.KeyComputation.KEY_COMP_USE_REQ_ENT) {
                TokenIssuerUtil.handleRequestedProofToken(data, wstVersion, config, rstrElem, assertionToken, doc);
            }
            return env;
        } finally {
            DocumentBuilderFactoryImpl.setDOOMRequired(false);
        }
    }

    private SAMLAssertion createBearerAssertion(SAMLTokenIssuerConfig config, Document doc, Crypto crypto, Date creationTime, Date expirationTime, RahasData data) throws TrustException {
        try {
            Principal principal = data.getPrincipal();
            if (principal instanceof WSUsernameTokenPrincipal) {
                SAMLNameIdentifier nameId = null;
                if (config.getCallbackHander() != null) {
                    SAMLNameIdentifierCallback cb = new SAMLNameIdentifierCallback(data);
                    cb.setUserId(principal.getName());
                    SAMLCallbackHandler callbackHandler = config.getCallbackHander();
                    callbackHandler.handle(cb);
                    nameId = cb.getNameId();
                } else {
                    nameId = new SAMLNameIdentifier(principal.getName(), null, SAMLNameIdentifier.FORMAT_EMAIL);
                }
                return createAuthAssertion(doc, SAMLSubject.CONF_BEARER, nameId, null, config, crypto, creationTime, expirationTime);
            } else {
                throw new TrustException("samlUnsupportedPrincipal", new String[] { principal.getClass().getName() });
            }
        } catch (SAMLException e) {
            throw new TrustException("samlAssertionCreationError", e);
        }
    }

    private SAMLAssertion createHoKAssertion(SAMLTokenIssuerConfig config, Document doc, Crypto crypto, Date creationTime, Date expirationTime, RahasData data) throws TrustException {
        if (data.getKeyType().endsWith(RahasConstants.KEY_TYPE_SYMM_KEY)) {
            Element encryptedKeyElem;
            X509Certificate serviceCert = null;
            try {
                serviceCert = getServiceCert(config, crypto, data.getAppliesToAddress());
                WSSecEncryptedKey encrKeyBuilder = new WSSecEncryptedKey();
                encrKeyBuilder.setKeyIdentifierType(WSConstants.THUMBPRINT_IDENTIFIER);
                encrKeyBuilder.setUseThisCert(serviceCert);
                int keysize = data.getKeysize();
                keysize = (keysize != -1) ? keysize : config.keySize;
                encrKeyBuilder.setKeySize(keysize);
                encrKeyBuilder.setEphemeralKey(TokenIssuerUtil.getSharedSecret(data, config.keyComputation, keysize));
                encrKeyBuilder.setKeyEncAlgo(EncryptionConstants.ALGO_ID_KEYTRANSPORT_RSA15);
                encrKeyBuilder.prepare(doc, crypto);
                byte[] tempKey = new byte[keysize / 8];
                System.arraycopy(encrKeyBuilder.getEphemeralKey(), 0, tempKey, 0, keysize / 8);
                data.setEphmeralKey(tempKey);
                encryptedKeyElem = encrKeyBuilder.getEncryptedKeyElement();
            } catch (WSSecurityException e) {
                throw new TrustException("errorInBuildingTheEncryptedKeyForPrincipal", new String[] { serviceCert.getSubjectDN().getName() }, e);
            }
            return this.createAttributeAssertion(doc, data, encryptedKeyElem, config, crypto, creationTime, expirationTime);
        } else {
            try {
                String subjectNameId = data.getPrincipal().getName();
                SAMLNameIdentifier nameId = new SAMLNameIdentifier(subjectNameId, null, SAMLNameIdentifier.FORMAT_EMAIL);
                X509Certificate clientCert = data.getClientCert();
                if (clientCert == null) {
                    X509Certificate[] certs = crypto.getCertificates(data.getPrincipal().getName());
                    clientCert = certs[0];
                }
                byte[] clientCertBytes = clientCert.getEncoded();
                String base64Cert = Base64.encode(clientCertBytes);
                Text base64CertText = doc.createTextNode(base64Cert);
                Element x509CertElem = doc.createElementNS(WSConstants.SIG_NS, "X509Certificate");
                x509CertElem.appendChild(base64CertText);
                Element x509DataElem = doc.createElementNS(WSConstants.SIG_NS, "X509Data");
                x509DataElem.appendChild(x509CertElem);
                return this.createAuthAssertion(doc, SAMLSubject.CONF_HOLDER_KEY, nameId, x509DataElem, config, crypto, creationTime, expirationTime);
            } catch (Exception e) {
                throw new TrustException("samlAssertionCreationError", e);
            }
        }
    }

    /**
     * Uses the <code>wst:AppliesTo</code> to figure out the certificate to
     * encrypt the secret in the SAML token
     * 
     * @param config
     * @param crypto
     * @param serviceAddress
     *            The address of the service
     * @return
     * @throws WSSecurityException
     */
    private X509Certificate getServiceCert(SAMLTokenIssuerConfig config, Crypto crypto, String serviceAddress) throws WSSecurityException {
        if (serviceAddress != null && !"".equals(serviceAddress)) {
            String alias = (String) config.trustedServices.get(serviceAddress);
            if (alias != null) {
                return crypto.getCertificates(alias)[0];
            } else {
                alias = (String) config.trustedServices.get("*");
                return crypto.getCertificates(alias)[0];
            }
        } else {
            String alias = (String) config.trustedServices.get("*");
            return crypto.getCertificates(alias)[0];
        }
    }

    /**
     * Create the SAML assertion with the secret held in an
     * <code>xenc:EncryptedKey</code>
     * 
     * @param doc
     * @param keyInfoContent
     * @param config
     * @param crypto
     * @param notBefore
     * @param notAfter
     * @return
     * @throws TrustException
     */
    private SAMLAssertion createAttributeAssertion(Document doc, RahasData data, Element keyInfoContent, SAMLTokenIssuerConfig config, Crypto crypto, Date notBefore, Date notAfter) throws TrustException {
        try {
            String[] confirmationMethods = new String[] { SAMLSubject.CONF_HOLDER_KEY };
            Element keyInfoElem = doc.createElementNS(WSConstants.SIG_NS, "KeyInfo");
            ((OMElement) keyInfoContent).declareNamespace(WSConstants.SIG_NS, WSConstants.SIG_PREFIX);
            ((OMElement) keyInfoContent).declareNamespace(WSConstants.ENC_NS, WSConstants.ENC_PREFIX);
            keyInfoElem.appendChild(keyInfoContent);
            SAMLSubject subject = new SAMLSubject(null, Arrays.asList(confirmationMethods), null, keyInfoElem);
            SAMLAttribute[] attrs = null;
            if (config.getCallbackHander() != null) {
                SAMLAttributeCallback cb = new SAMLAttributeCallback(data);
                SAMLCallbackHandler handler = config.getCallbackHander();
                handler.handle(cb);
                attrs = cb.getAttributes();
            } else if (config.getCallbackHandlerName() != null && config.getCallbackHandlerName().trim().length() > 0) {
                SAMLAttributeCallback cb = new SAMLAttributeCallback(data);
                SAMLCallbackHandler handler = null;
                MessageContext msgContext = data.getInMessageContext();
                ClassLoader classLoader = msgContext.getAxisService().getClassLoader();
                Class cbClass = null;
                try {
                    cbClass = Loader.loadClass(classLoader, config.getCallbackHandlerName());
                } catch (ClassNotFoundException e) {
                    throw new TrustException("cannotLoadPWCBClass", new String[] { config.getCallbackHandlerName() }, e);
                }
                try {
                    handler = (SAMLCallbackHandler) cbClass.newInstance();
                } catch (java.lang.Exception e) {
                    throw new TrustException("cannotCreatePWCBInstance", new String[] { config.getCallbackHandlerName() }, e);
                }
                handler.handle(cb);
                attrs = cb.getAttributes();
            } else {
                SAMLAttribute attribute = new SAMLAttribute("Name", "https://rahas.apache.org/saml/attrns", null, -1, Arrays.asList(new String[] { "Colombo/Rahas" }));
                attrs = new SAMLAttribute[] { attribute };
            }
            SAMLAttributeStatement attrStmt = new SAMLAttributeStatement(subject, Arrays.asList(attrs));
            SAMLStatement[] statements = { attrStmt };
            SAMLAssertion assertion = new SAMLAssertion(config.issuerName, notBefore, notAfter, null, null, Arrays.asList(statements));
            X509Certificate[] issuerCerts = crypto.getCertificates(config.issuerKeyAlias);
            String sigAlgo = XMLSignature.ALGO_ID_SIGNATURE_RSA;
            String pubKeyAlgo = issuerCerts[0].getPublicKey().getAlgorithm();
            if (pubKeyAlgo.equalsIgnoreCase("DSA")) {
                sigAlgo = XMLSignature.ALGO_ID_SIGNATURE_DSA;
            }
            java.security.Key issuerPK = crypto.getPrivateKey(config.issuerKeyAlias, config.issuerKeyPassword);
            assertion.sign(sigAlgo, issuerPK, Arrays.asList(issuerCerts));
            return assertion;
        } catch (Exception e) {
            throw new TrustException("samlAssertionCreationError", e);
        }
    }

    /**
     * @param doc
     * @param confMethod
     * @param subjectNameId
     * @param keyInfoContent
     * @param config
     * @param crypto
     * @param notBefore
     * @param notAfter
     * @return
     * @throws TrustException
     */
    private SAMLAssertion createAuthAssertion(Document doc, String confMethod, SAMLNameIdentifier subjectNameId, Element keyInfoContent, SAMLTokenIssuerConfig config, Crypto crypto, Date notBefore, Date notAfter) throws TrustException {
        try {
            String[] confirmationMethods = new String[] { confMethod };
            Element keyInfoElem = null;
            if (keyInfoContent != null) {
                keyInfoElem = doc.createElementNS(WSConstants.SIG_NS, "KeyInfo");
                ((OMElement) keyInfoContent).declareNamespace(WSConstants.SIG_NS, WSConstants.SIG_PREFIX);
                ((OMElement) keyInfoContent).declareNamespace(WSConstants.ENC_NS, WSConstants.ENC_PREFIX);
                keyInfoElem.appendChild(keyInfoContent);
            }
            SAMLSubject subject = new SAMLSubject(subjectNameId, Arrays.asList(confirmationMethods), null, keyInfoElem);
            SAMLAuthenticationStatement authStmt = new SAMLAuthenticationStatement(subject, SAMLAuthenticationStatement.AuthenticationMethod_Password, notBefore, null, null, null);
            SAMLStatement[] statements = { authStmt };
            SAMLAssertion assertion = new SAMLAssertion(config.issuerName, notBefore, notAfter, null, null, Arrays.asList(statements));
            X509Certificate[] issuerCerts = crypto.getCertificates(config.issuerKeyAlias);
            String sigAlgo = XMLSignature.ALGO_ID_SIGNATURE_RSA;
            String pubKeyAlgo = issuerCerts[0].getPublicKey().getAlgorithm();
            if (pubKeyAlgo.equalsIgnoreCase("DSA")) {
                sigAlgo = XMLSignature.ALGO_ID_SIGNATURE_DSA;
            }
            java.security.Key issuerPK = crypto.getPrivateKey(config.issuerKeyAlias, config.issuerKeyPassword);
            assertion.sign(sigAlgo, issuerPK, Arrays.asList(issuerCerts));
            return assertion;
        } catch (Exception e) {
            throw new TrustException("samlAssertionCreationError", e);
        }
    }

    /**
     * {@inheritDoc}
     */
    public String getResponseAction(RahasData data) throws TrustException {
        return TrustUtil.getActionValue(data.getVersion(), RahasConstants.RSTR_ACTION_ISSUE);
    }

    /**
     * Create an ephemeral key
     * 
     * @return The generated key as a byte array
     * @throws TrustException
     */
    protected byte[] generateEphemeralKey(int keySize) throws TrustException {
        try {
            SecureRandom random = SecureRandom.getInstance("SHA1PRNG");
            byte[] temp = new byte[keySize / 8];
            random.nextBytes(temp);
            return temp;
        } catch (Exception e) {
            throw new TrustException("Error in creating the ephemeral key", e);
        }
    }

    /**
     * {@inheritDoc}
     */
    public void setConfigurationFile(String configFile) {
        this.configFile = configFile;
    }

    /**
     * {@inheritDoc}
     */
    public void setConfigurationElement(OMElement configElement) {
        this.configElement = configElement;
    }

    /**
     * {@inheritDoc}
     */
    public void setConfigurationParamName(String configParamName) {
        this.configParamName = configParamName;
    }
}
