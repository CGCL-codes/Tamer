package be.fedict.trust.service.bean;

import java.io.InputStream;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import be.fedict.trust.client.TrustServiceDomains;
import be.fedict.trust.service.InitializationService;
import be.fedict.trust.service.SchedulingService;
import be.fedict.trust.service.SnmpConstants;
import be.fedict.trust.service.TrustServiceConstants;
import be.fedict.trust.service.dao.CertificateAuthorityDAO;
import be.fedict.trust.service.dao.ConfigurationDAO;
import be.fedict.trust.service.dao.LocalizationDAO;
import be.fedict.trust.service.dao.TrustDomainDAO;
import be.fedict.trust.service.entity.CertificateAuthorityEntity;
import be.fedict.trust.service.entity.ClockDriftConfigEntity;
import be.fedict.trust.service.entity.KeyStoreType;
import be.fedict.trust.service.entity.TimeProtocol;
import be.fedict.trust.service.entity.TrustDomainEntity;
import be.fedict.trust.service.entity.TrustPointEntity;
import be.fedict.trust.service.entity.constraints.KeyUsageType;
import be.fedict.trust.service.exception.InvalidCronExpressionException;
import be.fedict.trust.service.snmp.SNMPInterceptor;

/**
 * Initialization Service Bean implementation.
 * 
 * @author wvdhaute
 */
@Stateless
public class InitializationServiceBean implements InitializationService {

    private static final Log LOG = LogFactory.getLog(InitializationServiceBean.class);

    @EJB
    private ConfigurationDAO configurationDAO;

    @EJB
    private LocalizationDAO localizationDAO;

    @EJB
    private TrustDomainDAO trustDomainDAO;

    @EJB
    private CertificateAuthorityDAO certificateAuthorityDAO;

    @EJB
    private SchedulingService schedulingService;

    public void initialize() {
        LOG.debug("initialize");
        initTexts();
        initWSSecurityConfig();
        initNetworkConfig();
        initClockDrift();
        initSnmpCounters();
        if (this.trustDomainDAO.listTrustDomains().isEmpty()) {
            List<TrustPointEntity> trustPoints = initBelgianEidTrustPoints();
            initBelgianEidAuthTrustDomain(trustPoints);
            initBelgianEidNonRepudiationDomain(trustPoints);
            initBelgianEidNationalRegistryTrustDomain(trustPoints);
            initBelgianEidTestCardsTrustDomain();
            initBelgianTSATrustDomain();
        }
        initTimers();
    }

    private void initSnmpCounters() {
        SNMPInterceptor.setValue(SnmpConstants.VALIDATE, SnmpConstants.SNMP_SERVICE, 0L);
        SNMPInterceptor.setValue(SnmpConstants.VALIDATE_TSA, SnmpConstants.SNMP_SERVICE, 0L);
        SNMPInterceptor.setValue(SnmpConstants.VALIDATE_ATTRIBUTE_CERT, SnmpConstants.SNMP_SERVICE, 0L);
        SNMPInterceptor.setValue(SnmpConstants.CACHE_REFRESH, SnmpConstants.SNMP_SERVICE, 0L);
        SNMPInterceptor.setValue(SnmpConstants.CACHE_HITS, SnmpConstants.SNMP_SERVICE, 0L);
        SNMPInterceptor.setValue(SnmpConstants.CACHE_MISSES, SnmpConstants.SNMP_SERVICE, 0L);
        SNMPInterceptor.setValue(SnmpConstants.CACHE_HIT_PERCENTAGE, SnmpConstants.SNMP_SERVICE, 0L);
        SNMPInterceptor.setValue(SnmpConstants.CRL_DOWNLOAD_FAILURES, SnmpConstants.SNMP_SERVICE, 0L);
        SNMPInterceptor.setValue(SnmpConstants.OCSP_FAILURES, SnmpConstants.SNMP_SERVICE, 0L);
    }

    private void initTexts() {
        if (null == this.localizationDAO.findLocalization(TrustServiceConstants.INFO_MESSAGE_KEY)) {
            Map<Locale, String> texts = new HashMap<Locale, String>();
            texts.put(Locale.ENGLISH, "");
            texts.put(new Locale("nl"), "");
            texts.put(Locale.FRENCH, "");
            texts.put(Locale.GERMAN, "");
            this.localizationDAO.addLocalization(TrustServiceConstants.INFO_MESSAGE_KEY, texts);
        }
    }

    private void initWSSecurityConfig() {
        if (null == this.configurationDAO.findWSSecurityConfig()) {
            this.configurationDAO.setWSSecurityConfig(false, KeyStoreType.PKCS12, null, null, null, null);
        }
    }

    private void initNetworkConfig() {
        if (null == this.configurationDAO.findNetworkConfigEntity()) {
            this.configurationDAO.setNetworkConfig(null, 0);
            this.configurationDAO.setNetworkConfigEnabled(false);
        }
    }

    private void initClockDrift() {
        ClockDriftConfigEntity clockDriftConfig = this.configurationDAO.findClockDriftConfig();
        if (null == clockDriftConfig) {
            this.configurationDAO.setClockDriftConfig(TimeProtocol.NTP, TrustServiceConstants.CLOCK_DRIFT_NTP_SERVER, TrustServiceConstants.CLOCK_DRIFT_TIMEOUT, TrustServiceConstants.CLOCK_DRIFT_MAX_CLOCK_OFFSET, TrustServiceConstants.DEFAULT_CRON_EXPRESSION);
        }
    }

    private List<TrustPointEntity> initBelgianEidTrustPoints() {
        List<TrustPointEntity> trustPoints = new LinkedList<TrustPointEntity>();
        X509Certificate rootCaCertificate = loadCertificate("be/fedict/trust/belgiumrca.crt");
        CertificateAuthorityEntity rootCa = this.certificateAuthorityDAO.findCertificateAuthority(rootCaCertificate);
        if (null == rootCa) {
            rootCa = this.certificateAuthorityDAO.addCertificateAuthority(rootCaCertificate, "http://crl.eid.belgium.be/belgium.crl");
        }
        if (null == rootCa.getTrustPoint()) {
            TrustPointEntity rootCaTrustPoint = this.trustDomainDAO.addTrustPoint(TrustServiceConstants.DEFAULT_CRON_EXPRESSION, rootCa);
            rootCa.setTrustPoint(rootCaTrustPoint);
        }
        trustPoints.add(rootCa.getTrustPoint());
        X509Certificate rootCa2Certificate = loadCertificate("be/fedict/trust/belgiumrca2.crt");
        CertificateAuthorityEntity rootCa2 = this.certificateAuthorityDAO.findCertificateAuthority(rootCa2Certificate);
        if (null == rootCa2) {
            rootCa2 = this.certificateAuthorityDAO.addCertificateAuthority(rootCa2Certificate, "http://crl.eid.belgium.be/belgium2.crl");
        }
        if (null == rootCa2.getTrustPoint()) {
            TrustPointEntity rootCa2TrustPoint = this.trustDomainDAO.addTrustPoint(TrustServiceConstants.DEFAULT_CRON_EXPRESSION, rootCa2);
            rootCa2.setTrustPoint(rootCa2TrustPoint);
        }
        trustPoints.add(rootCa2.getTrustPoint());
        return trustPoints;
    }

    private void initBelgianEidTestCardsTrustDomain() {
        List<TrustPointEntity> trustPoints = new LinkedList<TrustPointEntity>();
        X509Certificate rootCertificate = loadCertificate("be/fedict/trust/belgiumtestrca.crt");
        CertificateAuthorityEntity rootCa = this.certificateAuthorityDAO.findCertificateAuthority(rootCertificate);
        if (null == rootCa) {
            rootCa = this.certificateAuthorityDAO.addCertificateAuthority(rootCertificate, null);
        }
        if (null == rootCa.getTrustPoint()) {
            TrustPointEntity rootCaTrustPoint = this.trustDomainDAO.addTrustPoint(TrustServiceConstants.DEFAULT_CRON_EXPRESSION, rootCa);
            rootCa.setTrustPoint(rootCaTrustPoint);
        }
        trustPoints.add(rootCa.getTrustPoint());
        TrustDomainEntity trustDomain = this.trustDomainDAO.findTrustDomain(TrustServiceDomains.BELGIAN_EID_TEST_TRUST_DOMAIN);
        if (null == trustDomain) {
            LOG.debug("create Belgian eID TEST trust domain");
            trustDomain = this.trustDomainDAO.addTrustDomain(TrustServiceDomains.BELGIAN_EID_TEST_TRUST_DOMAIN);
        }
        trustDomain.setTrustPoints(trustPoints);
        if (trustDomain.getCertificateConstraints().isEmpty()) {
            this.trustDomainDAO.addCertificatePolicy(trustDomain, "2.16.56.1.40.40.40.1");
        }
    }

    private void initBelgianEidAuthTrustDomain(List<TrustPointEntity> trustPoints) {
        TrustDomainEntity trustDomain = this.trustDomainDAO.findTrustDomain(TrustServiceDomains.BELGIAN_EID_AUTH_TRUST_DOMAIN);
        if (null == trustDomain) {
            LOG.debug("create Belgian eID authentication trust domain");
            trustDomain = this.trustDomainDAO.addTrustDomain(TrustServiceDomains.BELGIAN_EID_AUTH_TRUST_DOMAIN);
            this.trustDomainDAO.setDefaultTrustDomain(trustDomain);
        }
        trustDomain.setTrustPoints(trustPoints);
        if (trustDomain.getCertificateConstraints().isEmpty()) {
            this.trustDomainDAO.addKeyUsageConstraint(trustDomain, KeyUsageType.DIGITAL_SIGNATURE, true);
            this.trustDomainDAO.addKeyUsageConstraint(trustDomain, KeyUsageType.NON_REPUDIATION, false);
            this.trustDomainDAO.addCertificatePolicy(trustDomain, "2.16.56.1.1.1.2.2");
            this.trustDomainDAO.addCertificatePolicy(trustDomain, "2.16.56.1.1.1.7.2");
            this.trustDomainDAO.addCertificatePolicy(trustDomain, "2.16.56.9.1.1.2.2");
            this.trustDomainDAO.addCertificatePolicy(trustDomain, "2.16.56.9.1.1.7.2");
        }
    }

    private void initBelgianEidNonRepudiationDomain(List<TrustPointEntity> trustPoints) {
        TrustDomainEntity trustDomain = this.trustDomainDAO.findTrustDomain(TrustServiceDomains.BELGIAN_EID_NON_REPUDIATION_TRUST_DOMAIN);
        if (null == trustDomain) {
            LOG.debug("create Belgian eID Non Repudiation trust domain");
            trustDomain = this.trustDomainDAO.addTrustDomain(TrustServiceDomains.BELGIAN_EID_NON_REPUDIATION_TRUST_DOMAIN);
        }
        trustDomain.setTrustPoints(trustPoints);
        if (trustDomain.getCertificateConstraints().isEmpty()) {
            this.trustDomainDAO.addKeyUsageConstraint(trustDomain, KeyUsageType.DIGITAL_SIGNATURE, false);
            this.trustDomainDAO.addKeyUsageConstraint(trustDomain, KeyUsageType.NON_REPUDIATION, true);
            this.trustDomainDAO.addCertificatePolicy(trustDomain, "2.16.56.1.1.1.2.1");
            this.trustDomainDAO.addCertificatePolicy(trustDomain, "2.16.56.1.1.1.7.1");
            this.trustDomainDAO.addCertificatePolicy(trustDomain, "2.16.56.9.1.1.2.1");
            this.trustDomainDAO.addCertificatePolicy(trustDomain, "2.16.56.9.1.1.7.1");
            this.trustDomainDAO.addQCStatementsConstraint(trustDomain, true);
        }
    }

    private void initBelgianEidNationalRegistryTrustDomain(List<TrustPointEntity> trustPoints) {
        TrustDomainEntity trustDomain = this.trustDomainDAO.findTrustDomain(TrustServiceDomains.BELGIAN_EID_NATIONAL_REGISTRY_TRUST_DOMAIN);
        if (null == trustDomain) {
            LOG.debug("create Belgian eID national registry trust domain");
            trustDomain = this.trustDomainDAO.addTrustDomain(TrustServiceDomains.BELGIAN_EID_NATIONAL_REGISTRY_TRUST_DOMAIN);
        }
        trustDomain.setTrustPoints(trustPoints);
        if (trustDomain.getCertificateConstraints().isEmpty()) {
            this.trustDomainDAO.addKeyUsageConstraint(trustDomain, KeyUsageType.DIGITAL_SIGNATURE, true);
            this.trustDomainDAO.addKeyUsageConstraint(trustDomain, KeyUsageType.NON_REPUDIATION, true);
            this.trustDomainDAO.addCertificatePolicy(trustDomain, "2.16.56.1.1.1.4");
            this.trustDomainDAO.addCertificatePolicy(trustDomain, "2.16.56.9.1.1.4");
            this.trustDomainDAO.addDNConstraint(trustDomain, "CN=RRN, O=RRN, C=BE");
        }
    }

    /**
	 * Initialize the Belgian TSA trust points.
	 */
    private void initBelgianTSATrustDomain() {
        List<TrustPointEntity> trustPoints = new LinkedList<TrustPointEntity>();
        X509Certificate rootCertificate = loadCertificate("be/fedict/trust/belgiumtsa.crt");
        CertificateAuthorityEntity rootCa = this.certificateAuthorityDAO.findCertificateAuthority(rootCertificate);
        if (null == rootCa) {
            rootCa = this.certificateAuthorityDAO.addCertificateAuthority(rootCertificate, null);
        }
        if (null == rootCa.getTrustPoint()) {
            TrustPointEntity rootCaTrustPoint = this.trustDomainDAO.addTrustPoint(TrustServiceConstants.DEFAULT_CRON_EXPRESSION, rootCa);
            rootCa.setTrustPoint(rootCaTrustPoint);
        }
        trustPoints.add(rootCa.getTrustPoint());
        TrustDomainEntity trustDomain = this.trustDomainDAO.findTrustDomain(TrustServiceDomains.BELGIAN_TSA_TRUST_DOMAIN);
        if (null == trustDomain) {
            LOG.debug("create Belgian TSA Repudiation trust domain");
            trustDomain = this.trustDomainDAO.addTrustDomain(TrustServiceDomains.BELGIAN_TSA_TRUST_DOMAIN);
        }
        trustDomain.setTrustPoints(trustPoints);
        this.trustDomainDAO.addTSAConstraint(trustDomain);
    }

    private static X509Certificate loadCertificate(String resourceName) {
        LOG.debug("loading certificate: " + resourceName);
        Thread currentThread = Thread.currentThread();
        ClassLoader classLoader = currentThread.getContextClassLoader();
        InputStream certificateInputStream = classLoader.getResourceAsStream(resourceName);
        if (null == certificateInputStream) {
            throw new IllegalArgumentException("resource not found: " + resourceName);
        }
        try {
            CertificateFactory certificateFactory = CertificateFactory.getInstance("X.509");
            return (X509Certificate) certificateFactory.generateCertificate(certificateInputStream);
        } catch (CertificateException e) {
            throw new RuntimeException("X509 error: " + e.getMessage(), e);
        }
    }

    /**
	 * Initialize timers for all trust points found and the clock drift config
	 * if enabled.
	 */
    private void initTimers() {
        for (TrustPointEntity trustPoint : this.trustDomainDAO.listTrustPoints()) {
            try {
                this.schedulingService.startTimer(trustPoint);
            } catch (InvalidCronExpressionException e) {
                throw new RuntimeException(String.format("Failed to start timer for trustpoint \"%s\"", trustPoint.getName()), e);
            }
        }
        ClockDriftConfigEntity clockDriftConfig = this.configurationDAO.findClockDriftConfig();
        if (null != clockDriftConfig && clockDriftConfig.isEnabled()) {
            try {
                this.schedulingService.startTimer(clockDriftConfig);
            } catch (InvalidCronExpressionException e) {
                throw new RuntimeException("Failed to start timer for clockdrift config,", e);
            }
        }
    }
}
