package be.fedict.trust.service.dao;

import java.security.cert.X509Certificate;
import java.util.List;
import javax.ejb.Local;
import be.fedict.trust.service.entity.CertificateAuthorityEntity;
import be.fedict.trust.service.entity.TrustDomainEntity;
import be.fedict.trust.service.entity.TrustPointEntity;
import be.fedict.trust.service.entity.VirtualTrustDomainEntity;
import be.fedict.trust.service.entity.constraints.CertificateConstraintEntity;
import be.fedict.trust.service.entity.constraints.DNConstraintEntity;
import be.fedict.trust.service.entity.constraints.EndEntityConstraintEntity;
import be.fedict.trust.service.entity.constraints.KeyUsageConstraintEntity;
import be.fedict.trust.service.entity.constraints.KeyUsageType;
import be.fedict.trust.service.entity.constraints.PolicyConstraintEntity;
import be.fedict.trust.service.entity.constraints.QCStatementsConstraintEntity;
import be.fedict.trust.service.entity.constraints.TSAConstraintEntity;
import be.fedict.trust.service.exception.TrustDomainNotFoundException;
import be.fedict.trust.service.exception.VirtualTrustDomainNotFoundException;

/**
 * Trust Domain DAO.
 * 
 * @author wvdhaute
 */
@Local
public interface TrustDomainDAO {

    /**
	 * Returns list of {@link TrustDomainEntity}'s
	 */
    List<TrustDomainEntity> listTrustDomains();

    /**
	 * Returns list of {@link VirtualTrustDomainEntity}\s.
	 */
    List<VirtualTrustDomainEntity> listVirtualTrustDomains();

    /**
	 * Returns list of {@link TrustDomainEntity}'s containing the specified
	 * {@link TrustPointEntity}.
	 */
    List<TrustDomainEntity> listTrustDomains(TrustPointEntity trustPoint);

    /**
	 * Return {@link TrustDomainEntity} from specified name. Returns
	 * <code>null</code> if not found.
	 */
    TrustDomainEntity findTrustDomain(String name);

    /**
	 * Returns {@link TrustDomainEntity} from specified name. Throws
	 * {@link TrustDomainNotFoundException} if not found.
	 */
    TrustDomainEntity getTrustDomain(String name) throws TrustDomainNotFoundException;

    /**
	 * Return {@link VirtualTrustDomainEntity} from specified name. Returns
	 * <code>null</code> if not found.
	 */
    VirtualTrustDomainEntity findVirtualTrustDomain(String name);

    /**
	 * Returns {@link VirtualTrustDomainEntity} from specified name. Throws
	 * {@link TrustDomainNotFoundException} if not found.
	 */
    VirtualTrustDomainEntity getVirtualTrustDomain(String name) throws VirtualTrustDomainNotFoundException;

    /**
	 * Create a new {@link TrustDomainEntity}.
	 */
    TrustDomainEntity addTrustDomain(String name);

    /**
	 * Create a new {@link VirtualTrustDomainEntity}.
	 */
    VirtualTrustDomainEntity addVirtualTrustDomain(String name);

    /**
	 * Remove the specified {@link TrustDomainEntity}.
	 */
    void removeTrustDomain(TrustDomainEntity trustDomain);

    /**
	 * Remove the specified {@link VirtualTrustDomainEntity}.
	 */
    void removeVirtualTrustDomain(VirtualTrustDomainEntity virtualTrustDomain);

    /**
	 * Sets the {@link TrustDomainEntity} as default.
	 */
    void setDefaultTrustDomain(TrustDomainEntity trustDomain);

    /**
	 * Returns the default {@link TrustDomainEntity}.
	 */
    TrustDomainEntity getDefaultTrustDomain();

    /**
	 * Create a new {@link TrustPointEntity}.
	 */
    TrustPointEntity addTrustPoint(String crlRefreshCronSchedule, CertificateAuthorityEntity ca);

    /**
	 * Returns list of all {@link TrustPointEntity}'s.
	 */
    List<TrustPointEntity> listTrustPoints();

    /**
	 * Returns list of {@link TrustPointEntity}'s attached to the specified
	 * {@link TrustDomainEntity}.
	 */
    List<TrustPointEntity> listTrustPoints(TrustDomainEntity trustDomain);

    /**
	 * Returns list of {@link TrustPointEntity}'s attached to the specified
	 * {@link TrustDomainEntity} specified by trust domain name.
	 */
    List<TrustPointEntity> listTrustPoints(String trustDomainName) throws TrustDomainNotFoundException;

    /**
	 * Returns list of {@link CertificateAuthorityEntity}'s for the specified
	 * {@link TrustPointEntity}.
	 */
    List<CertificateAuthorityEntity> listCertificateAuthorities(TrustPointEntity trustPoint);

    List<String> listCANames(TrustPointEntity trustPoint);

    /**
	 * Removes the selected {@link TrustPointEntity}.
	 */
    void removeTrustPoint(TrustPointEntity trustPoint);

    /**
	 * Returns the attached {@link TrustPointEntity}.
	 */
    TrustPointEntity attachTrustPoint(TrustPointEntity trustPoint);

    /**
	 * Add a new {@link PolicyConstraintEntity} to the {@link TrustDomainEntity}
	 * 's list of certificate contraints.
	 */
    PolicyConstraintEntity addCertificatePolicy(TrustDomainEntity trustDomain, String policy);

    /**
	 * Removes the specified {@link CertificateConstraintEntity}.
	 */
    void removeCertificateConstraint(CertificateConstraintEntity certificateConstraint);

    /**
	 * Add a new {@link EndEntityConstraintEntity} to the
	 * {@link TrustDomainEntity}'s list of certificate contraints.
	 */
    EndEntityConstraintEntity addEndEntityConstraint(TrustDomainEntity trustDomain, X509Certificate certificate);

    /**
	 * Add a new {@link KeyUsageConstraintEntity} to the
	 * {@link TrustDomainEntity}'s list of certificate contraints.
	 */
    KeyUsageConstraintEntity addKeyUsageConstraint(TrustDomainEntity trustDomain, KeyUsageType keyUsageType, boolean allowed);

    /**
	 * Add a new {@link QCStatementsConstraintEntity} to the
	 * {@link TrustDomainEntity}'s list of certificate contraints.
	 */
    QCStatementsConstraintEntity addQCStatementsConstraint(TrustDomainEntity trustDomain, boolean qcComplianceFilter);

    /**
	 * Add a new {@link DNConstraintEntity} to the {@link TrustDomainEntity}'s
	 * list of certificate contraints.
	 */
    DNConstraintEntity addDNConstraint(TrustDomainEntity trustDomain, String dn);

    /**
	 * Add a new {@link TSAConstraintEntity} to the {@link TrustDomainEntity}'s
	 * list of certificate constraints.
	 */
    TSAConstraintEntity addTSAConstraint(TrustDomainEntity trustDomain);

    /**
	 * Returns the {@link TrustPointEntity} from the specified name. Returns
	 * <code>null</code> if not found.
	 */
    TrustPointEntity findTrustPoint(String name);

    /**
	 * Returns the attached {@link CertificateConstraintEntity}.
	 */
    CertificateConstraintEntity findCertificateConstraint(CertificateConstraintEntity certificateConstraint);
}
