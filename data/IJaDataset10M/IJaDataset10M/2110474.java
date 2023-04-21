package org.signserver.validationservice.server;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.net.ConnectException;
import java.security.InvalidAlgorithmParameterException;
import java.security.cert.CertPath;
import java.security.cert.CertPathValidator;
import java.security.cert.CertPathValidatorException;
import java.security.cert.CertStore;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.cert.CertificateExpiredException;
import java.security.cert.CertificateFactory;
import java.security.cert.CertificateNotYetValidException;
import java.security.cert.CollectionCertStoreParameters;
import java.security.cert.PKIXCertPathValidatorResult;
import java.security.cert.PKIXParameters;
import java.security.cert.TrustAnchor;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;
import org.apache.log4j.Logger;
import org.ejbca.util.CertTools;
import org.signserver.common.CryptoTokenOfflineException;
import org.signserver.common.IllegalRequestException;
import org.signserver.common.SignServerException;
import org.signserver.validationservice.common.ICertificate;
import org.signserver.validationservice.common.Validation;
import org.signserver.validationservice.common.ValidationServiceConstants;
import org.signserver.validationservice.common.X509Certificate;

/**
 * OCSP validator used for validating certificates using OCSP only for revocation checking.
 * 
 * NOTE : properties introduced in J2SE5 such as : ocsp.enable,ocsp.responderURL and etc..  are not used since they
 * automatically fail over to CRL in case OCSP encounters any problem. 
 * 
 * @author rayback2
 * @version $Id: OCSPValidator.java 1825 2011-08-10 10:21:18Z netmackan $
 */
public class OCSPValidator extends BaseValidator {

    /** Logger for this class. */
    private static final Logger LOG = Logger.getLogger(OCSPValidator.class);

    public void testConnection() throws ConnectException, SignServerException {
    }

    public Validation validate(ICertificate cert) throws IllegalRequestException, CryptoTokenOfflineException, SignServerException {
        LOG.debug("Validator's validate called with certificate " + cert.getSubject());
        X509Certificate xcert = (X509Certificate) cert;
        try {
            xcert.checkValidity();
        } catch (CertificateExpiredException e1) {
            return new Validation(cert, null, Validation.Status.EXPIRED, "Certificate has expired. " + e1.toString());
        } catch (CertificateNotYetValidException e1) {
            return new Validation(cert, null, Validation.Status.NOTYETVALID, "Certificate is not yet valid. " + e1.toString());
        }
        List<ICertificate> certChain = getCertificateChain(cert);
        if (certChain == null) {
            if (isTrustAnchor(xcert)) {
                return new Validation(cert, Collections.singletonList(cert), Validation.Status.VALID, "This certificate is defined as Trust Anchor.");
            } else {
                return null;
            }
        }
        if (LOG.isDebugEnabled()) {
            LOG.debug("***********************");
            LOG.debug("printing certchain for " + cert.getSubject());
            for (ICertificate tempcert : certChain) {
                LOG.debug(tempcert.getSubject());
            }
            LOG.debug("***********************");
        }
        CertPath certPath = null;
        CertStore certStore = null;
        List<Object> certs = new ArrayList<Object>();
        CertificateFactory certFactory = null;
        CertPathValidator validator = null;
        PKIXParameters params = null;
        ICertificate rootCert = null;
        List<X509Certificate> certChainWithoutRootCert = new ArrayList<X509Certificate>();
        try {
            certFactory = CertificateFactory.getInstance("X509", "BC");
            certs.addAll(certChain);
            certs.add(cert);
            certStore = CertStore.getInstance("Collection", new CollectionCertStoreParameters(certs), "BC");
            if (LOG.isDebugEnabled()) {
                LOG.debug("***********************");
                LOG.debug("printing certs in certstore");
                Iterator<?> tempIter = certStore.getCertificates(null).iterator();
                while (tempIter.hasNext()) {
                    X509Certificate tempcert = (X509Certificate) tempIter.next();
                    LOG.debug(tempcert.getSubject() + " issuer is " + tempcert.getIssuer());
                }
                LOG.debug("***********************");
            }
            for (ICertificate currentCACert : certChain) {
                X509Certificate x509currentCACert = (X509Certificate) currentCACert;
                if (rootCert == null && x509currentCACert.getSubjectX500Principal().equals(x509currentCACert.getIssuerX500Principal())) {
                    rootCert = currentCACert;
                } else {
                    certChainWithoutRootCert.add(x509currentCACert);
                }
            }
            certChainWithoutRootCert.add((X509Certificate) cert);
            certPath = certFactory.generateCertPath(certChainWithoutRootCert);
            if (LOG.isDebugEnabled()) {
                LOG.debug("***********************");
                LOG.debug("printing certs in certpath");
                for (Certificate tempcert : certPath.getCertificates()) {
                    LOG.debug(((X509Certificate) tempcert).getSubject() + " issuer is " + ((X509Certificate) tempcert).getIssuer());
                }
                LOG.debug("***********************");
            }
            validator = CertPathValidator.getInstance("PKIX", "BC");
            TrustAnchor trustAnc = new TrustAnchor((X509Certificate) rootCert, null);
            params = new PKIXParameters(Collections.singleton(trustAnc));
            params.addCertStore(certStore);
            params.setDate(new Date());
            if (LOG.isDebugEnabled()) {
                LOG.debug("***********************");
                LOG.debug("printing trust anchor " + trustAnc.getTrustedCert().getSubjectDN().getName());
                LOG.debug("***********************");
            }
            params.setRevocationEnabled(false);
            addCertPathCheckers(cert, params, rootCert);
        } catch (Exception e) {
            LOG.error("Exception on preparing parameters for validation", e);
            throw new SignServerException(e.toString());
        }
        PKIXCertPathValidatorResult cpv_result = null;
        try {
            cpv_result = (PKIXCertPathValidatorResult) validator.validate(certPath, params);
            return new Validation(cert, getCertificateChain(cert), Validation.Status.VALID, "This certificate is valid. Trust anchor for certificate is :" + cpv_result.getTrustAnchor().getTrustedCert().getSubjectDN());
        } catch (CertPathValidatorException e) {
            LOG.debug("certificate is not valid.", e);
            return new Validation(cert, getCertificateChain(cert), Validation.Status.DONTVERIFY, "Exception on validation. certificate causing exception : " + ((X509Certificate) e.getCertPath().getCertificates().get(e.getIndex())).getSubjectDN() + " " + e.toString());
        } catch (InvalidAlgorithmParameterException e) {
            LOG.error("Exception on validation", e);
            throw new SignServerException("Exception on validation.", e);
        }
    }

    /**
     * adding custom path checker
     * @param cert
     * @param params
     * @param rootCert
     * @throws SignServerException
     * @throws CertificateException
     * @throws IOException
     */
    protected void addCertPathCheckers(ICertificate cert, PKIXParameters params, ICertificate rootCert) throws SignServerException, CertificateException, IOException {
        params.addCertPathChecker(new OCSPPathChecker((X509Certificate) rootCert, this.props, getIssuerAuthorizedOCSPResponderCertificates(cert)));
    }

    /**
     * Find the issuer of this certificate and get the Authorized OCSP Responder Certificates
     * @throws SignServerException 
     * @throws IOException 
     * @throws CertificateException 
     */
    protected List<X509Certificate> getIssuerAuthorizedOCSPResponderCertificates(ICertificate cert) throws SignServerException, CertificateException, IOException {
        ArrayList<X509Certificate> x509Certs = new ArrayList<X509Certificate>();
        Properties issuerProps = getIssuerProperties(cert);
        if (issuerProps == null) {
            return null;
        }
        String key;
        for (int i = 1; i <= ValidationServiceConstants.NUM_OF_SUPPORTED_AUTHORIZED_OCSP_RESPONDER_CERTS; i++) {
            key = ValidationServiceConstants.AUTHORIZED_OCSP_RESPONDER_CERT_PREFIX + i;
            if (issuerProps.containsKey(key)) {
                Collection<?> certs = CertTools.getCertsFromPEM(new ByteArrayInputStream(issuerProps.getProperty(key).getBytes()));
                Iterator<?> certiter = certs.iterator();
                while (certiter.hasNext()) {
                    x509Certs.add(X509Certificate.getInstance((java.security.cert.X509Certificate) certiter.next()));
                }
            }
        }
        return x509Certs;
    }
}
