package be.fedict.trust.service;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.cert.CRLException;
import java.security.cert.CertStoreException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.Date;
import java.util.List;
import javax.ejb.Local;
import org.bouncycastle.cms.CMSException;
import org.bouncycastle.tsp.TSPException;
import org.bouncycastle.tsp.TimeStampToken;
import org.bouncycastle.x509.X509V2AttributeCertificate;
import be.fedict.trust.service.entity.AuditEntity;
import be.fedict.trust.service.entity.WSSecurityConfigEntity;
import be.fedict.trust.service.exception.TrustDomainNotFoundException;

/**
 * Trust Service interface.
 * 
 * @author Frank Cornelis
 */
@Local
public interface TrustService {

    /**
	 * Checks whether the given certificate chain is valid.
	 * 
	 * @param trustDomain
	 *            optional, can be null. If so default trust domain is taken.
	 * @param certificateChain
	 *            cert. chain to validate
	 * @param returnRevocationData
	 *            if true, used revocation data will be filled in in the
	 *            {@link ValidationResult} and no caching will be used.
	 * @return validation result
	 * @throws TrustDomainNotFoundException
	 *             specified trust domain not found
	 */
    ValidationResult validate(String trustDomain, List<X509Certificate> certificateChain, boolean returnRevocationData) throws TrustDomainNotFoundException;

    /**
	 * Checks whether the given certificate chain was valid at the specified
	 * {@link Date}, using the specified revocation data.
	 * 
	 * @param trustDomain
	 *            optional, can be null. If so default trust domain is taken.
	 * @param certificateChain
	 *            cert. chain to validate
	 * @param validationDate
	 *            validation date
	 * @param ocspResponses
	 *            OCSP response to use
	 * @param crls
	 *            CRLs to use
	 * @return validation result
	 * @throws TrustDomainNotFoundException
	 *             specified trust domain not found
	 * @throws IOException
	 *             IO Exception
	 * @throws NoSuchProviderException
	 *             JCE provider not found.
	 * @throws CRLException
	 *             failure using specified CRLs
	 * @throws CertificateException
	 *             certificate exception
	 */
    ValidationResult validate(String trustDomain, List<X509Certificate> certificateChain, Date validationDate, List<byte[]> ocspResponses, List<byte[]> crls) throws TrustDomainNotFoundException, CertificateException, NoSuchProviderException, CRLException, IOException;

    /**
	 * Validate the specified encoded {@link TimeStampToken} against the
	 * specified trust domain
	 * 
	 * @param trustDomain
	 *            optional, can be null. If so default trust domain is taken.
	 * @param timestampToken
	 *            encoded {@link TimeStampToken}.
	 * @param returnRevocationData
	 *            if true, used revocation data will be filled in in the
	 *            {@link ValidationResult} and no caching will be used.
	 * @return validation result
	 * @throws TrustDomainNotFoundException
	 *             specified trust domain not found
	 * @throws IOException
	 *             IO Exception
	 * @throws NoSuchProviderException
	 *             JCE provider not found.
	 * @throws NoSuchAlgorithmException
	 *             no such algorithm exception
	 * @throws CertStoreException
	 *             certificate store failure
	 * @throws CMSException
	 *             CMS Exception
	 * @throws TSPException
	 *             TSP Exception
	 */
    ValidationResult validateTimestamp(String trustDomain, byte[] timestampToken, boolean returnRevocationData) throws TSPException, IOException, CMSException, NoSuchAlgorithmException, NoSuchProviderException, CertStoreException, TrustDomainNotFoundException;

    /**
	 * Validate the specified encoded {@link X509V2AttributeCertificate}'s.
	 * 
	 * @param trustDomain
	 *            optional, can be null. If so default trust domain is taken.
	 * @param attributeCertificates
	 *            the encoded attribute certificates
	 * @param certificateChain
	 *            to be validate cert. chain
	 * @param returnRevocationData
	 *            if true, used revocation data will be filled in in the
	 *            {@link ValidationResult} and no caching will be used.
	 * @return validation result
	 * @throws TrustDomainNotFoundException
	 *             specified trust domain not found
	 */
    ValidationResult validateAttributeCertificates(String trustDomain, List<byte[]> attributeCertificates, List<X509Certificate> certificateChain, boolean returnRevocationData) throws TrustDomainNotFoundException;

    /**
	 * @return the {@link WSSecurityConfigEntity}.
	 */
    WSSecurityConfigEntity getWsSecurityConfig();

    /**
	 * Log specified message in an {@link AuditEntity}.
	 * 
	 * @param message
	 *            message to audit
	 */
    void logAudit(String message);
}
