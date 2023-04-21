package br.net.woodstock.rockframework.security.cert.impl;

import java.math.BigInteger;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.PrivateKey;
import java.security.cert.Certificate;
import java.security.cert.X509Certificate;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import java.util.Vector;
import org.bouncycastle.asn1.DERObject;
import org.bouncycastle.asn1.x509.GeneralName;
import org.bouncycastle.asn1.x509.GeneralNames;
import org.bouncycastle.asn1.x509.KeyPurposeId;
import org.bouncycastle.asn1.x509.KeyUsage;
import org.bouncycastle.asn1.x509.X509Extension;
import org.bouncycastle.cert.X509CertificateHolder;
import org.bouncycastle.cert.jcajce.JcaX509v1CertificateBuilder;
import org.bouncycastle.cert.jcajce.JcaX509v3CertificateBuilder;
import org.bouncycastle.operator.ContentSigner;
import org.bouncycastle.operator.jcajce.JcaContentSignerBuilder;
import org.bouncycastle.x509.extension.SubjectKeyIdentifierStructure;
import br.net.woodstock.rockframework.security.cert.CertificateBuilder;
import br.net.woodstock.rockframework.security.cert.CertificateException;
import br.net.woodstock.rockframework.security.cert.CertificateType;
import br.net.woodstock.rockframework.security.cert.ExtendedKeyUsageType;
import br.net.woodstock.rockframework.security.cert.KeyUsageType;
import br.net.woodstock.rockframework.security.cert.PrivateKeyHolder;
import br.net.woodstock.rockframework.security.crypt.KeyPairType;
import br.net.woodstock.rockframework.security.sign.SignatureType;
import br.net.woodstock.rockframework.security.util.BouncyCastleProviderHelper;
import br.net.woodstock.rockframework.security.util.SecurityUtils;
import br.net.woodstock.rockframework.util.DateBuilder;

public class BouncyCastleCertificateBuilder implements CertificateBuilder {

    private static final String DEFAULT_ISSUER = "Woodstock Tecnologia";

    private String subject;

    private KeyPair keyPair;

    private SignatureType signType;

    private String issuerName;

    private Certificate issuerCertificate;

    private BigInteger serialNumber;

    private Date notBefore;

    private Date notAfter;

    private boolean v3;

    private Set<KeyUsageType> keyUsage;

    private Set<ExtendedKeyUsageType> extendedKeyUsage;

    public BouncyCastleCertificateBuilder(final String subject) {
        this(subject, BouncyCastleCertificateBuilder.DEFAULT_ISSUER);
    }

    public BouncyCastleCertificateBuilder(final String subject, final String issuer) {
        super();
        this.subject = subject;
        this.issuerName = issuer;
        this.keyUsage = new HashSet<KeyUsageType>();
        this.extendedKeyUsage = new HashSet<ExtendedKeyUsageType>();
    }

    public BouncyCastleCertificateBuilder withKeyPair(final KeyPair keyPair) {
        this.keyPair = keyPair;
        return this;
    }

    public BouncyCastleCertificateBuilder withSignType(final SignatureType signType) {
        this.signType = signType;
        return this;
    }

    public BouncyCastleCertificateBuilder withIssuer(final String issuer) {
        this.issuerName = issuer;
        return this;
    }

    public BouncyCastleCertificateBuilder withIssuer(final Certificate issuer) {
        this.issuerCertificate = issuer;
        return this;
    }

    public BouncyCastleCertificateBuilder withSerialNumber(final BigInteger serialNumber) {
        this.serialNumber = serialNumber;
        return this;
    }

    public BouncyCastleCertificateBuilder withNotBefore(final Date notBefore) {
        this.notBefore = notBefore;
        return this;
    }

    public BouncyCastleCertificateBuilder withNotAfter(final Date notAfter) {
        this.notAfter = notAfter;
        return this;
    }

    public BouncyCastleCertificateBuilder withKeyUsage(final KeyUsageType... array) {
        for (KeyUsageType keyUsage : array) {
            this.keyUsage.add(keyUsage);
        }
        return this;
    }

    public BouncyCastleCertificateBuilder withExtendedKeyUsage(final ExtendedKeyUsageType... array) {
        for (ExtendedKeyUsageType keyUsage : array) {
            this.extendedKeyUsage.add(keyUsage);
        }
        return this;
    }

    public BouncyCastleCertificateBuilder withV3Extensions(final boolean v3) {
        this.v3 = v3;
        return this;
    }

    @Override
    public PrivateKeyHolder build() {
        try {
            long time = System.currentTimeMillis();
            String subject = this.subject;
            KeyPair keyPair = this.keyPair;
            SignatureType signType = this.signType;
            String issuer = this.issuerName;
            BigInteger serialNumber = this.serialNumber;
            Date notBefore = this.notBefore;
            Date notAfter = this.notAfter;
            X509Certificate certificate = null;
            PrivateKey privateKey = null;
            if (keyPair == null) {
                keyPair = KeyPairGenerator.getInstance(KeyPairType.RSA.getAlgorithm()).generateKeyPair();
            }
            if (signType == null) {
                signType = SignatureType.SHA1_RSA;
            }
            if (issuer == null) {
                issuer = BouncyCastleCertificateBuilder.DEFAULT_ISSUER;
            }
            if (serialNumber == null) {
                serialNumber = BigInteger.valueOf(time);
            }
            if (notBefore == null) {
                DateBuilder dateBuilder = new DateBuilder(time);
                dateBuilder.removeDays(1);
                notBefore = dateBuilder.getDate();
            }
            if (notAfter == null) {
                DateBuilder dateBuilder = new DateBuilder(time);
                dateBuilder.addYears(1);
                notAfter = dateBuilder.getDate();
            }
            if (this.v3) {
                JcaX509v3CertificateBuilder builder = null;
                if (this.issuerCertificate != null) {
                    builder = new JcaX509v3CertificateBuilder((X509Certificate) this.issuerCertificate, serialNumber, notBefore, notAfter, BouncyCastleProviderHelper.toX500Principal(subject), keyPair.getPublic());
                } else {
                    builder = new JcaX509v3CertificateBuilder(BouncyCastleProviderHelper.toX500Name(issuer), serialNumber, notBefore, notAfter, BouncyCastleProviderHelper.toX500Name(subject), keyPair.getPublic());
                }
                JcaContentSignerBuilder contentSignerBuilder = new JcaContentSignerBuilder(signType.getAlgorithm());
                contentSignerBuilder.setProvider(BouncyCastleProviderHelper.PROVIDER_NAME);
                ContentSigner contentSigner = contentSignerBuilder.build(keyPair.getPrivate());
                if (this.keyUsage.size() > 0) {
                    int usage = 0;
                    for (KeyUsageType keyUsage : this.keyUsage) {
                        usage = usage | this.toKeyUsage(keyUsage);
                    }
                    org.bouncycastle.asn1.x509.KeyUsage ku = new org.bouncycastle.asn1.x509.KeyUsage(usage);
                    builder.addExtension(X509Extension.keyUsage, false, ku);
                }
                if (this.extendedKeyUsage.size() > 0) {
                    Vector<DERObject> vector = new Vector<DERObject>();
                    for (ExtendedKeyUsageType keyUsageType : this.extendedKeyUsage) {
                        KeyPurposeId keyPurposeId = this.toExtendedKeyUsage(keyUsageType);
                        if (keyPurposeId != null) {
                            vector.add(keyPurposeId);
                        }
                    }
                    if (vector.size() > 0) {
                        org.bouncycastle.asn1.x509.ExtendedKeyUsage extendedKeyUsage = new org.bouncycastle.asn1.x509.ExtendedKeyUsage(vector);
                        builder.addExtension(X509Extension.extendedKeyUsage, true, extendedKeyUsage);
                    } else {
                        org.bouncycastle.asn1.x509.ExtendedKeyUsage extendedKeyUsage = new org.bouncycastle.asn1.x509.ExtendedKeyUsage(KeyPurposeId.anyExtendedKeyUsage);
                        builder.addExtension(X509Extension.extendedKeyUsage, false, extendedKeyUsage);
                    }
                } else {
                    org.bouncycastle.asn1.x509.ExtendedKeyUsage extendedKeyUsage = new org.bouncycastle.asn1.x509.ExtendedKeyUsage(KeyPurposeId.anyExtendedKeyUsage);
                    builder.addExtension(X509Extension.extendedKeyUsage, false, extendedKeyUsage);
                }
                GeneralNames subjectAltName = new GeneralNames(new GeneralName(GeneralName.rfc822Name, subject));
                builder.addExtension(X509Extension.subjectAlternativeName, false, subjectAltName);
                SubjectKeyIdentifierStructure subjectKeyIdentifierStructure = new SubjectKeyIdentifierStructure(keyPair.getPublic());
                builder.addExtension(X509Extension.subjectKeyIdentifier, false, subjectKeyIdentifierStructure);
                X509CertificateHolder holder = builder.build(contentSigner);
                certificate = (X509Certificate) SecurityUtils.getCertificateFromFile(holder.getEncoded(), CertificateType.X509);
                privateKey = keyPair.getPrivate();
            } else {
                JcaX509v1CertificateBuilder builder = new JcaX509v1CertificateBuilder(BouncyCastleProviderHelper.toX500Name(issuer), serialNumber, notBefore, notAfter, BouncyCastleProviderHelper.toX500Name(subject), keyPair.getPublic());
                JcaContentSignerBuilder contentSignerBuilder = new JcaContentSignerBuilder(signType.getAlgorithm());
                contentSignerBuilder.setProvider(BouncyCastleProviderHelper.PROVIDER_NAME);
                ContentSigner contentSigner = contentSignerBuilder.build(keyPair.getPrivate());
                X509CertificateHolder holder = builder.build(contentSigner);
                certificate = (X509Certificate) SecurityUtils.getCertificateFromFile(holder.getEncoded(), CertificateType.X509);
                privateKey = keyPair.getPrivate();
            }
            PrivateKeyHolder privateKeyHolder = new PrivateKeyHolder(privateKey, new Certificate[] { certificate });
            return privateKeyHolder;
        } catch (Exception e) {
            throw new CertificateException(e);
        }
    }

    private int toKeyUsage(final KeyUsageType keyUsageType) {
        switch(keyUsageType) {
            case CRL_SIGN:
                return KeyUsage.cRLSign;
            case DATA_ENCIPHERMENT:
                return KeyUsage.dataEncipherment;
            case DECIPHER_ONLY:
                return KeyUsage.decipherOnly;
            case DIGITAL_SIGNATURE:
                return KeyUsage.digitalSignature;
            case ENCIPHER_ONLY:
                return KeyUsage.encipherOnly;
            case KEY_AGREEMENT:
                return KeyUsage.keyAgreement;
            case KEY_CERT_SIGN:
                return KeyUsage.keyCertSign;
            case KEY_ENCIPHERMENT:
                return KeyUsage.keyEncipherment;
            case NON_REPUDIATION:
                return KeyUsage.nonRepudiation;
            default:
                return 0;
        }
    }

    private KeyPurposeId toExtendedKeyUsage(final ExtendedKeyUsageType keyUsageType) {
        switch(keyUsageType) {
            case ANY:
                return KeyPurposeId.anyExtendedKeyUsage;
            case CAP_WAP_AC:
                return KeyPurposeId.id_kp_capwapAC;
            case CAP_WAP_WTP:
                return KeyPurposeId.id_kp_capwapWTP;
            case CLIENT_AUTH:
                return KeyPurposeId.id_kp_clientAuth;
            case CODE_SIGN:
                return KeyPurposeId.id_kp_codeSigning;
            case DVCS:
                return KeyPurposeId.id_kp_dvcs;
            case EAP_OVER_LAN:
                return KeyPurposeId.id_kp_eapOverLAN;
            case EAP_OVER_PPP:
                return KeyPurposeId.id_kp_eapOverPPP;
            case EMAIL_PROTECTION:
                return KeyPurposeId.id_kp_emailProtection;
            case IPSEC_END_SYSTEM:
                return KeyPurposeId.id_kp_ipsecEndSystem;
            case IPSEC_IKE:
                return KeyPurposeId.id_kp_ipsecIKE;
            case IPSEC_TUNNEL:
                return KeyPurposeId.id_kp_ipsecTunnel;
            case IPSEC_USER:
                return KeyPurposeId.id_kp_ipsecUser;
            case OCSP_SIGNING:
                return KeyPurposeId.id_kp_OCSPSigning;
            case SBGP_CERT_AA_SERVER_AUTH:
                return KeyPurposeId.id_kp_sbgpCertAAServerAuth;
            case SCVP_CLIENT:
                return KeyPurposeId.id_kp_scvpClient;
            case SCVP_RESPONDER:
                return KeyPurposeId.id_kp_scvp_responder;
            case SCVP_SERVER:
                return KeyPurposeId.id_kp_scvpServer;
            case SERVER_AUTH:
                return KeyPurposeId.id_kp_serverAuth;
            case SMART_CARD_LOGIN:
                return KeyPurposeId.id_kp_smartcardlogon;
            case TIMESTAMPING:
                return KeyPurposeId.id_kp_timeStamping;
            default:
                return null;
        }
    }
}
