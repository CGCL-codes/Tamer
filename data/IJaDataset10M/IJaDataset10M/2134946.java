package be.fedict.trust.client;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.net.ProxySelector;
import java.security.InvalidKeyException;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.PublicKey;
import java.security.SecureRandom;
import java.security.SignatureException;
import java.security.cert.CRLException;
import java.security.cert.CertificateEncodingException;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509CRL;
import java.security.cert.X509Certificate;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;
import javax.xml.ws.Binding;
import javax.xml.ws.BindingProvider;
import javax.xml.ws.handler.Handler;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.bouncycastle.ocsp.OCSPResp;
import org.bouncycastle.tsp.TimeStampToken;
import org.bouncycastle.x509.X509V2AttributeCertificate;
import sun.security.timestamp.TimestampToken;
import be.fedict.trust.client.exception.RevocationDataCorruptException;
import be.fedict.trust.client.exception.RevocationDataNotFoundException;
import be.fedict.trust.client.exception.TrustDomainNotFoundException;
import be.fedict.trust.client.exception.ValidationFailedException;
import be.fedict.trust.client.jaxb.xades132.CRLValuesType;
import be.fedict.trust.client.jaxb.xades132.CertifiedRolesListType;
import be.fedict.trust.client.jaxb.xades132.EncapsulatedPKIDataType;
import be.fedict.trust.client.jaxb.xades132.OCSPValuesType;
import be.fedict.trust.client.jaxb.xades132.RevocationValuesType;
import be.fedict.trust.client.jaxb.xkms.KeyBindingType;
import be.fedict.trust.client.jaxb.xkms.MessageExtensionAbstractType;
import be.fedict.trust.client.jaxb.xkms.QueryKeyBindingType;
import be.fedict.trust.client.jaxb.xkms.StatusType;
import be.fedict.trust.client.jaxb.xkms.TimeInstantType;
import be.fedict.trust.client.jaxb.xkms.UseKeyWithType;
import be.fedict.trust.client.jaxb.xkms.ValidateRequestType;
import be.fedict.trust.client.jaxb.xkms.ValidateResultType;
import be.fedict.trust.client.jaxb.xmldsig.KeyInfoType;
import be.fedict.trust.client.jaxb.xmldsig.X509DataType;
import be.fedict.trust.client.jaxws.xkms.XKMSPortType;
import be.fedict.trust.client.jaxws.xkms.XKMSService;
import be.fedict.trust.xkms.extensions.AttributeCertificateMessageExtensionType;
import be.fedict.trust.xkms.extensions.RevocationDataMessageExtensionType;
import be.fedict.trust.xkms.extensions.TSAMessageExtensionType;
import be.fedict.trust.xkms2.LoggingSoapHandler;
import be.fedict.trust.xkms2.ResultMajorCode;
import be.fedict.trust.xkms2.ResultMinorCode;
import be.fedict.trust.xkms2.XKMSConstants;
import be.fedict.trust.xkms2.XKMSServiceFactory;
import com.sun.xml.ws.developer.JAXWSProperties;

/**
 * Client component for the eID Trust Service XKMS2 web service.
 * 
 * @author Frank Cornelis
 */
public class XKMS2Client {

    private static final Log LOG = LogFactory.getLog(XKMS2Client.class);

    private final XKMSPortType port;

    private RevocationValuesType revocationValues;

    private WSSecurityClientHandler wsSecurityClientHandler;

    protected List<String> invalidReasonURIs;

    private String location;

    private static XKMS2ProxySelector proxySelector;

    static {
        ProxySelector defaultProxySelector = ProxySelector.getDefault();
        XKMS2Client.proxySelector = new XKMS2ProxySelector(defaultProxySelector);
        ProxySelector.setDefault(XKMS2Client.proxySelector);
    }

    /**
	 * Main constructor
	 * 
	 * @param location
	 *            location ( complete path ) of the XKMS2 web service
	 */
    public XKMS2Client(String location) {
        this.location = location;
        this.invalidReasonURIs = new LinkedList<String>();
        XKMSService xkmsService = XKMSServiceFactory.getInstance();
        this.port = xkmsService.getXKMSPort();
        registeredWSSecurityHandler(this.port);
        setEndpointAddress(location);
    }

    /**
	 * Enables/disables logging of all SOAP requests/responses.
	 * 
	 * @param logging
	 *            enable logging on or not
	 */
    public void setLogging(boolean logging) {
        if (logging) {
            registerLoggerHandler(this.port);
        } else {
            removeLoggerHandler(this.port);
        }
    }

    /**
	 * Proxy configuration setting ( both http as https ).
	 * 
	 * @param proxyHost
	 *            proxy host
	 * @param proxyPort
	 *            proxy port
	 */
    public void setProxy(String proxyHost, int proxyPort) {
        XKMS2Client.proxySelector.setProxy(this.location, proxyHost, proxyPort);
    }

    /**
	 * Set the optional server {@link X509Certificate}. If specified and the
	 * trust service has message signing configured, the incoming
	 * {@link X509Certificate} will be checked against the specified
	 * {@link X509Certificate}.
	 * 
	 * @param serverCertificate
	 *            the server X509 certificate.
	 */
    public void setServerCertificate(X509Certificate serverCertificate) {
        this.wsSecurityClientHandler.setServerCertificate(serverCertificate);
    }

    /**
	 * Set the maximum offset of the WS-Security timestamp ( in seconds ). If
	 * not specified this will be defaulted to 5 minutes.
	 * 
	 * @param maxWSSecurityTimestampOffset
	 *            maximum WS Security Timestamp offset
	 */
    public void setMaxWSSecurityTimestampOffset(long maxWSSecurityTimestampOffset) {
        this.wsSecurityClientHandler.setMaxWSSecurityTimestampOffset(maxWSSecurityTimestampOffset * 1000);
    }

    /**
	 * If set, unilateral TLS authentication will occurs, verifying the server
	 * {@link X509Certificate} specified {@link PublicKey}.
	 * 
	 * @param publicKey
	 *            public key to validate server TLS certificate against.
	 */
    public void setServicePublicKey(final PublicKey publicKey) {
        TrustManager[] trustManager = { new X509TrustManager() {

            public X509Certificate[] getAcceptedIssuers() {
                return null;
            }

            public void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException {
                X509Certificate serverCertificate = chain[0];
                LOG.debug("server X509 subject: " + serverCertificate.getSubjectX500Principal().toString());
                LOG.debug("authentication type: " + authType);
                if (null == publicKey) {
                    return;
                }
                try {
                    serverCertificate.verify(publicKey);
                    LOG.debug("valid server certificate");
                } catch (InvalidKeyException e) {
                    throw new CertificateException("Invalid Key");
                } catch (NoSuchAlgorithmException e) {
                    throw new CertificateException("No such algorithm");
                } catch (NoSuchProviderException e) {
                    throw new CertificateException("No such provider");
                } catch (SignatureException e) {
                    throw new CertificateException("Wrong signature");
                }
            }

            public void checkClientTrusted(X509Certificate[] chain, String authType) throws CertificateException {
                throw new CertificateException("this trust manager cannot be used as server-side trust manager");
            }
        } };
        try {
            SSLContext sslContext = SSLContext.getInstance("TLS");
            SecureRandom secureRandom = new SecureRandom();
            sslContext.init(null, trustManager, secureRandom);
            LOG.debug("SSL context provider: " + sslContext.getProvider().getName());
            Map<String, Object> requestContext = ((BindingProvider) this.port).getRequestContext();
            requestContext.put(JAXWSProperties.SSL_SOCKET_FACTORY, sslContext.getSocketFactory());
        } catch (KeyManagementException e) {
            String msg = "key management error: " + e.getMessage();
            LOG.error(msg, e);
            throw new RuntimeException(msg, e);
        } catch (NoSuchAlgorithmException e) {
            String msg = "TLS algo not present: " + e.getMessage();
            LOG.error(msg, e);
            throw new RuntimeException(msg, e);
        }
    }

    private void setEndpointAddress(String location) {
        LOG.debug("ws location: " + location);
        if (null == location) {
            throw new IllegalArgumentException("XKMS2 location URL cannot be null");
        }
        BindingProvider bindingProvider = (BindingProvider) this.port;
        bindingProvider.getRequestContext().put(BindingProvider.ENDPOINT_ADDRESS_PROPERTY, location);
    }

    /**
	 * Registers the logging SOAP handler on the given JAX-WS port component.
	 */
    protected void registerLoggerHandler(Object port) {
        BindingProvider bindingProvider = (BindingProvider) port;
        Binding binding = bindingProvider.getBinding();
        @SuppressWarnings("unchecked") List<Handler> handlerChain = binding.getHandlerChain();
        handlerChain.add(new LoggingSoapHandler());
        binding.setHandlerChain(handlerChain);
    }

    /**
	 * Unregister possible logging SOAP handlers on the given JAX-WS port
	 * component.
	 */
    protected void removeLoggerHandler(Object port) {
        BindingProvider bindingProvider = (BindingProvider) port;
        Binding binding = bindingProvider.getBinding();
        @SuppressWarnings("unchecked") List<Handler> handlerChain = binding.getHandlerChain();
        Iterator<Handler> iter = handlerChain.iterator();
        while (iter.hasNext()) {
            Handler handler = iter.next();
            if (handler instanceof LoggingSoapHandler) {
                iter.remove();
            }
        }
    }

    protected void registeredWSSecurityHandler(Object port) {
        BindingProvider bindingProvider = (BindingProvider) port;
        Binding binding = bindingProvider.getBinding();
        @SuppressWarnings("unchecked") List<Handler> handlerChain = binding.getHandlerChain();
        this.wsSecurityClientHandler = new WSSecurityClientHandler();
        handlerChain.add(this.wsSecurityClientHandler);
        binding.setHandlerChain(handlerChain);
    }

    /**
	 * Validate the specified certificate chain against the default trust domain
	 * configured at the trust service we are connecting to.
	 */
    public void validate(List<X509Certificate> certificateChain) throws CertificateEncodingException, TrustDomainNotFoundException, RevocationDataNotFoundException, ValidationFailedException {
        validate(null, certificateChain);
    }

    /**
	 * Validate the specified certificate chain against the default trust domain
	 * configured at the trust service we are connecting to.
	 * <p/>
	 * The used revocation data can be retrieved using
	 * {@link #getRevocationValues()}.
	 * 
	 * @param returnRevocationData
	 *            whether or not the used revocation data should be returned.
	 */
    public void validate(List<X509Certificate> certificateChain, boolean returnRevocationData) throws CertificateEncodingException, TrustDomainNotFoundException, RevocationDataNotFoundException, ValidationFailedException {
        validate(null, certificateChain, returnRevocationData);
    }

    /**
	 * Validate the specified certificate chain against the specified trust
	 * domain.
	 */
    public void validate(String trustDomain, List<X509Certificate> certificateChain) throws CertificateEncodingException, TrustDomainNotFoundException, RevocationDataNotFoundException, ValidationFailedException {
        validate(trustDomain, certificateChain, false);
    }

    /**
	 * Validate the specified certificate chain against the specified trust
	 * domain.
	 * <p/>
	 * The used revocation data can be retrieved using
	 * {@link #getRevocationValues()}.
	 * 
	 * @param returnRevocationData
	 *            whether or not the used revocation data should be returned.
	 */
    public void validate(String trustDomain, List<X509Certificate> certificateChain, boolean returnRevocationData) throws CertificateEncodingException, TrustDomainNotFoundException, RevocationDataNotFoundException, ValidationFailedException {
        validate(trustDomain, certificateChain, returnRevocationData, null, null, null, null, null, null);
    }

    /**
	 * Validate the specified certificate chain against the specified trust
	 * domain using historical validation using the specified revocation data.
	 */
    public void validate(String trustDomain, List<X509Certificate> certificateChain, Date validationDate, List<OCSPResp> ocspResponses, List<X509CRL> crls) throws CertificateEncodingException, TrustDomainNotFoundException, RevocationDataNotFoundException, ValidationFailedException {
        if ((null == ocspResponses || ocspResponses.isEmpty()) && (null == crls || crls.isEmpty())) {
            LOG.error("No revocation data for historical validation.");
            throw new RevocationDataNotFoundException();
        }
        try {
            List<byte[]> encodedOcspResponses = new LinkedList<byte[]>();
            List<byte[]> encodedCrls = new LinkedList<byte[]>();
            for (OCSPResp ocspResponse : ocspResponses) {
                encodedOcspResponses.add(ocspResponse.getEncoded());
            }
            for (X509CRL crl : crls) {
                encodedCrls.add(crl.getEncoded());
            }
            validate(trustDomain, certificateChain, false, validationDate, encodedOcspResponses, encodedCrls, null, null, null);
        } catch (IOException e) {
            LOG.error("Failed to get encoded OCSPResponse: " + e.getMessage(), e);
            throw new RuntimeException(e);
        } catch (CRLException e) {
            LOG.error("Failed to get encoded CRL: " + e.getMessage(), e);
            throw new RuntimeException(e);
        }
    }

    /**
	 * Validate the specified certificate chain against the specified trust
	 * domain using historical validation using the specified revocation data.
	 */
    public void validateEncoded(String trustDomain, List<X509Certificate> certificateChain, Date validationDate, List<byte[]> ocspResponses, List<byte[]> crls) throws CertificateEncodingException, TrustDomainNotFoundException, RevocationDataNotFoundException, ValidationFailedException, RevocationDataCorruptException {
        if ((null == ocspResponses || ocspResponses.isEmpty()) && (null == crls || crls.isEmpty())) {
            LOG.error("No revocation data for historical validation.");
            throw new RevocationDataNotFoundException();
        }
        try {
            for (byte[] encodedOcspResponse : ocspResponses) {
                new OCSPResp(encodedOcspResponse);
            }
            CertificateFactory certificateFactory = CertificateFactory.getInstance("X.509");
            for (byte[] encodedCrl : crls) {
                ByteArrayInputStream bais = new ByteArrayInputStream(encodedCrl);
                certificateFactory.generateCRL(bais);
            }
        } catch (IOException e) {
            throw new RevocationDataCorruptException("Invalid OCSP response", e);
        } catch (CRLException e) {
            throw new RevocationDataCorruptException("Invalid CRL", e);
        } catch (CertificateException e) {
            throw new RevocationDataCorruptException(e);
        }
        validate(trustDomain, certificateChain, false, validationDate, ocspResponses, crls, null, null, null);
    }

    /**
	 * Validate the specified certificate chain against the specified trust
	 * domain using historical validation using the specified revocation data.
	 */
    public void validate(String trustDomain, List<X509Certificate> certificateChain, Date validationDate, RevocationValuesType revocationValues) throws CertificateEncodingException, TrustDomainNotFoundException, RevocationDataNotFoundException, ValidationFailedException {
        if (null == revocationValues) {
            LOG.error("No revocation data for historical validation.");
            throw new RevocationDataNotFoundException();
        }
        validate(trustDomain, certificateChain, false, validationDate, null, null, revocationValues, null, null);
    }

    /**
	 * Validate the specified {@link TimestampToken} for the specified TSA trust
	 * domain
	 */
    public void validate(String trustDomain, TimeStampToken timeStampToken) throws TrustDomainNotFoundException, CertificateEncodingException, RevocationDataNotFoundException, ValidationFailedException {
        LOG.debug("validate timestamp token");
        validate(trustDomain, new LinkedList<X509Certificate>(), false, null, null, null, revocationValues, timeStampToken, null);
    }

    /**
	 * Validate the specified
	 * {@link be.fedict.trust.client.jaxb.xades132.EncapsulatedPKIDataType}
	 * holding the {@link X509V2AttributeCertificate}.
	 * 
	 * @param certificateChain
	 *            the certificate chain for the attribute certificate
	 */
    public void validate(String trustDomain, List<X509Certificate> certificateChain, CertifiedRolesListType attributeCertificates) throws CertificateEncodingException, TrustDomainNotFoundException, RevocationDataNotFoundException, ValidationFailedException {
        LOG.debug("validate attribute certificate");
        validate(trustDomain, certificateChain, false, null, null, null, revocationValues, null, attributeCertificates);
    }

    protected void validate(String trustDomain, List<X509Certificate> certificateChain, boolean returnRevocationData, Date validationDate, List<byte[]> ocspResponses, List<byte[]> crls, RevocationValuesType revocationValues, TimeStampToken timeStampToken, CertifiedRolesListType attributeCertificates) throws CertificateEncodingException, TrustDomainNotFoundException, RevocationDataNotFoundException, ValidationFailedException {
        LOG.debug("validate");
        be.fedict.trust.client.jaxb.xkms.ObjectFactory objectFactory = new be.fedict.trust.client.jaxb.xkms.ObjectFactory();
        be.fedict.trust.client.jaxb.xmldsig.ObjectFactory xmldsigObjectFactory = new be.fedict.trust.client.jaxb.xmldsig.ObjectFactory();
        ValidateRequestType validateRequest = objectFactory.createValidateRequestType();
        QueryKeyBindingType queryKeyBinding = objectFactory.createQueryKeyBindingType();
        KeyInfoType keyInfo = xmldsigObjectFactory.createKeyInfoType();
        queryKeyBinding.setKeyInfo(keyInfo);
        X509DataType x509Data = xmldsigObjectFactory.createX509DataType();
        for (X509Certificate certificate : certificateChain) {
            byte[] encodedCertificate = certificate.getEncoded();
            x509Data.getX509IssuerSerialOrX509SKIOrX509SubjectName().add(xmldsigObjectFactory.createX509DataTypeX509Certificate(encodedCertificate));
        }
        keyInfo.getContent().add(xmldsigObjectFactory.createX509Data(x509Data));
        validateRequest.setQueryKeyBinding(queryKeyBinding);
        if (null != trustDomain) {
            UseKeyWithType useKeyWith = objectFactory.createUseKeyWithType();
            useKeyWith.setApplication(XKMSConstants.TRUST_DOMAIN_APPLICATION_URI);
            useKeyWith.setIdentifier(trustDomain);
            queryKeyBinding.getUseKeyWith().add(useKeyWith);
        }
        if (null != timeStampToken) {
            addTimeStampToken(validateRequest, timeStampToken);
        }
        if (null != attributeCertificates) {
            addAttributeCertificates(validateRequest, attributeCertificates);
        }
        if (returnRevocationData) {
            validateRequest.getRespondWith().add(XKMSConstants.RETURN_REVOCATION_DATA_URI);
        }
        if (null != validationDate) {
            TimeInstantType timeInstant = objectFactory.createTimeInstantType();
            timeInstant.setTime(getXmlGregorianCalendar(validationDate));
            queryKeyBinding.setTimeInstant(timeInstant);
            addRevocationData(validateRequest, ocspResponses, crls, revocationValues);
        }
        ValidateResultType validateResult = this.port.validate(validateRequest);
        if (null == validateResult) {
            throw new RuntimeException("missing ValidateResult element");
        }
        checkResponse(validateResult);
        if (returnRevocationData) {
            for (MessageExtensionAbstractType messageExtension : validateResult.getMessageExtension()) {
                if (messageExtension instanceof RevocationDataMessageExtensionType) {
                    this.revocationValues = ((RevocationDataMessageExtensionType) messageExtension).getRevocationValues();
                }
            }
            if (null == this.revocationValues) {
                LOG.error("no revocation data found");
                throw new RevocationDataNotFoundException();
            }
        }
        this.invalidReasonURIs.clear();
        List<KeyBindingType> keyBindings = validateResult.getKeyBinding();
        for (KeyBindingType keyBinding : keyBindings) {
            StatusType status = keyBinding.getStatus();
            String statusValue = status.getStatusValue();
            LOG.debug("status: " + statusValue);
            if (XKMSConstants.KEY_BINDING_STATUS_VALID_URI.equals(statusValue)) {
                return;
            }
            for (String invalidReason : status.getInvalidReason()) {
                this.invalidReasonURIs.add(invalidReason);
            }
            throw new ValidationFailedException(invalidReasonURIs);
        }
    }

    /**
	 * Add revocation data either from list of {@link OCSPResp} objects and
	 * {@link X509CRL} objects or from specified {@link RevocationValuesType}.
	 */
    private void addRevocationData(ValidateRequestType validateRequest, List<byte[]> ocspResponses, List<byte[]> crls, RevocationValuesType revocationData) {
        be.fedict.trust.xkms.extensions.ObjectFactory extensionsObjectFactory = new be.fedict.trust.xkms.extensions.ObjectFactory();
        RevocationDataMessageExtensionType revocationDataMessageExtension = extensionsObjectFactory.createRevocationDataMessageExtensionType();
        if (null != revocationData) {
            revocationDataMessageExtension.setRevocationValues(revocationData);
        } else {
            be.fedict.trust.client.jaxb.xades132.ObjectFactory xadesObjectFactory = new be.fedict.trust.client.jaxb.xades132.ObjectFactory();
            RevocationValuesType revocationValues = xadesObjectFactory.createRevocationValuesType();
            OCSPValuesType ocspValues = xadesObjectFactory.createOCSPValuesType();
            for (byte[] ocspResponse : ocspResponses) {
                EncapsulatedPKIDataType ocspValue = xadesObjectFactory.createEncapsulatedPKIDataType();
                ocspValue.setValue(ocspResponse);
                ocspValues.getEncapsulatedOCSPValue().add(ocspValue);
            }
            revocationValues.setOCSPValues(ocspValues);
            CRLValuesType crlValues = xadesObjectFactory.createCRLValuesType();
            for (byte[] crl : crls) {
                EncapsulatedPKIDataType crlValue = xadesObjectFactory.createEncapsulatedPKIDataType();
                crlValue.setValue(crl);
                crlValues.getEncapsulatedCRLValue().add(crlValue);
            }
            revocationValues.setCRLValues(crlValues);
            revocationDataMessageExtension.setRevocationValues(revocationValues);
        }
        validateRequest.getMessageExtension().add(revocationDataMessageExtension);
    }

    /**
	 * Add the specified {@link TimeStampToken} to the
	 * {@link ValidateRequestType}.
	 */
    private void addTimeStampToken(ValidateRequestType validateRequest, TimeStampToken timeStampToken) {
        be.fedict.trust.xkms.extensions.ObjectFactory extensionsObjectFactory = new be.fedict.trust.xkms.extensions.ObjectFactory();
        be.fedict.trust.client.jaxb.xades132.ObjectFactory xadesObjectFactory = new be.fedict.trust.client.jaxb.xades132.ObjectFactory();
        TSAMessageExtensionType tsaMessageExtension = extensionsObjectFactory.createTSAMessageExtensionType();
        EncapsulatedPKIDataType timeStampTokenValue = xadesObjectFactory.createEncapsulatedPKIDataType();
        try {
            timeStampTokenValue.setValue(timeStampToken.getEncoded());
        } catch (IOException e) {
            LOG.error("Failed to get encoded timestamp token", e);
            throw new RuntimeException(e);
        }
        tsaMessageExtension.setEncapsulatedTimeStamp(timeStampTokenValue);
        validateRequest.getMessageExtension().add(tsaMessageExtension);
    }

    /**
	 * Add the specified {@link EncapsulatedPKIDataType} holding the attribute
	 * certificate to the {@link ValidateRequestType}.
	 */
    private void addAttributeCertificates(ValidateRequestType validateRequest, CertifiedRolesListType attributeCertificates) {
        be.fedict.trust.xkms.extensions.ObjectFactory extensionsObjectFactory = new be.fedict.trust.xkms.extensions.ObjectFactory();
        AttributeCertificateMessageExtensionType attributeCertificateMessageExtension = extensionsObjectFactory.createAttributeCertificateMessageExtensionType();
        attributeCertificateMessageExtension.setCertifiedRoles(attributeCertificates);
        validateRequest.getMessageExtension().add(attributeCertificateMessageExtension);
    }

    /**
	 * Checks the ResultMajor and ResultMinor code.
	 */
    private void checkResponse(ValidateResultType validateResult) throws TrustDomainNotFoundException {
        if (!validateResult.getResultMajor().equals(ResultMajorCode.SUCCESS.getErrorCode())) {
            if (validateResult.getResultMinor().equals(ResultMinorCode.TRUST_DOMAIN_NOT_FOUND.getErrorCode())) {
                throw new TrustDomainNotFoundException();
            }
        }
    }

    /**
	 * Return the optionally filled {@link RevocationValuesType}. Returns
	 * <code>null</code> if this was not specified.
	 */
    public RevocationValuesType getRevocationValues() {
        return this.revocationValues;
    }

    /**
	 * Returns the XKMS v2.0 reason URIs for the failed validation.
	 * 
	 * @see <a href="http://www.w3.org/TR/xkms2/#XKMS_2_0_Section_5_1">XKMS
	 *      2.0</a>
	 */
    public List<String> getInvalidReasons() {
        return this.invalidReasonURIs;
    }

    private XMLGregorianCalendar getXmlGregorianCalendar(Date date) {
        try {
            DatatypeFactory datatypeFactory = DatatypeFactory.newInstance();
            GregorianCalendar gregorianCalendar = new GregorianCalendar();
            gregorianCalendar.setTime(date);
            XMLGregorianCalendar currentXmlGregorianCalendar = datatypeFactory.newXMLGregorianCalendar(gregorianCalendar);
            return currentXmlGregorianCalendar;
        } catch (DatatypeConfigurationException e) {
            throw new RuntimeException("datatype config error");
        }
    }
}
