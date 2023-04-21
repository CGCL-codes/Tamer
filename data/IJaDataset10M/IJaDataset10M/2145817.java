package net.sourceforge.acacia.voms.client;

import java.io.File;
import java.security.PublicKey;
import java.security.Signature;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.Set;
import net.sourceforge.acacia.security.AttributeCertificateUtils;
import net.sourceforge.acacia.security.ElapsedTime;
import net.sourceforge.acacia.security.GlobusCredentialUtils;
import net.sourceforge.acacia.security.GridSecurityConfiguration;
import net.sourceforge.acacia.voms.VOMSAttributes;
import net.sourceforge.acacia.voms.VirtualOrganisation;
import org.apache.log4j.Logger;
import org.bouncycastle.asn1.DERObjectIdentifier;
import org.bouncycastle.asn1.x509.AttributeCertificate;
import org.bouncycastle.asn1.x509.X509Extension;
import org.bouncycastle.asn1.x509.X509Extensions;
import org.globus.gsi.CertUtil;
import org.globus.gsi.GlobusCredential;

/**
  * Validates a proxy containing VOMS ACs
  * <p>
  * Fullfills the criteria as written in RFC 3281 section 5.
  *
  * @author	Gidon Moont / Imperial College London / GridPP Portal Project
  * @version	1.00 (7 March 2007)
  */
public class VOMSValidate {

    private static String basicConstraintsOID = new String("2.5.29.19");

    private static Logger logger = Logger.getLogger("net.sourceforge.acacia.voms");

    public static boolean sloppy_ca_validation = true;

    public VOMSValidate() {
    }

    public static void validate(GlobusCredentialUtils vomsyProxy) {
        boolean valid = true;
        try {
            logger.info("Validating...");
            String credIdentity = vomsyProxy.getGCIdentity();
            int credIdentitySerial = vomsyProxy.getGCIdentitySerial();
            logger.info("Credential Identity = " + credIdentity);
            logger.info("Credential Serial   = " + credIdentitySerial);
            logger.info("Credential Issuer   = " + vomsyProxy.getGCIssuer());
            logger.info("Credential Subject  = " + vomsyProxy.getGCSubject());
            if (vomsyProxy.verify()) {
                logger.debug("Passed point 1 of validation (all parts of the PKC are in date)");
            } else {
                valid = false;
                logger.error("Failed point 1 of validation (some part of the PKC is out of date)");
            }
            X509Certificate[] chain = vomsyProxy.getGC().getCertificateChain();
            logger.debug("Chain length of the PKC is " + chain.length);
            ArrayList acArrayList = vomsyProxy.extractVOMSACs();
            logger.info("There are " + acArrayList.size() + " VOMS ACs present");
            Iterator iter = acArrayList.iterator();
            int count = 0;
            while (iter.hasNext()) {
                count++;
                logger.info(">> VOMS AC " + count);
                AttributeCertificateUtils vomsAC = new AttributeCertificateUtils((AttributeCertificate) iter.next());
                X509Certificate vomsServerCertificate = VirtualOrganisation.getServerCertificate(vomsAC.getIssuer());
                if (vomsAC.verify(vomsServerCertificate)) {
                    logger.debug("  Passed point 2 [1 of 2] of validation (signature of AC is valid)");
                } else {
                    valid = false;
                    logger.error("  Failed point 2 [1 of 2] of validation (signature of AC is invalid)");
                }
                String c_hash = GridSecurityConfiguration.hashIssuer(vomsServerCertificate);
                boolean verified_voms_cert = false;
                int index = 0;
                while (!verified_voms_cert && (new File(new String(GridSecurityConfiguration.getGridSecrityCACertsLocations() + c_hash + "." + index))).isFile()) {
                    String Certfilename = new String(GridSecurityConfiguration.getGridSecrityCACertsLocations() + c_hash + "." + index);
                    logger.debug("Found potential match with " + Certfilename);
                    X509Certificate CACertificate = CertUtil.loadCertificate(Certfilename);
                    Signature sig = Signature.getInstance(vomsServerCertificate.getSigAlgOID());
                    PublicKey pk = CACertificate.getPublicKey();
                    if (pk != null) {
                        sig.initVerify(pk);
                        sig.update(vomsServerCertificate.getTBSCertificate());
                        if (sig.verify(vomsServerCertificate.getSignature())) {
                            verified_voms_cert = true;
                            logger.debug("  Passed point 2 [2 of 2] of validation (signature of VOMS Server Certificate is valid)");
                            boolean foundCritical = false;
                            Set critSet = CACertificate.getCriticalExtensionOIDs();
                            if (critSet != null && !critSet.isEmpty()) {
                                for (Iterator i = critSet.iterator(); i.hasNext(); ) {
                                    String oid = (String) i.next();
                                    if (oid.equals(basicConstraintsOID)) foundCritical = true;
                                }
                            }
                            if ((CACertificate.getBasicConstraints() > -1) && foundCritical) {
                                logger.debug("  Passed on point 2 [2 of 2 extended] (basicConstraints is critical and has CA=TRUE (pathLength = " + CACertificate.getBasicConstraints() + " in the the CA Credential)");
                            } else {
                                logger.error("  Failed on point 2 [2 of 2 extended] (basicConstraints is either non-critical or does not have CA=TRUE in the the CA Credential)");
                                if (sloppy_ca_validation) {
                                    logger.error("  Since sloppy_ca_validation is true, this will not cause an overall invalidation of this proxy");
                                } else {
                                    valid = false;
                                }
                            }
                        }
                    }
                    index++;
                }
                if (!verified_voms_cert) {
                    valid = false;
                    logger.error("  Failed point 2 [2 of 2] of validation (signature of VOMS Server Certificate is invalid)");
                }
                boolean[] usageSwitches = vomsServerCertificate.getKeyUsage();
                if (usageSwitches[0]) {
                    logger.debug("  Passed on point 3 [1 of 2] (digitalSignature keyUsage in the the VOMS Server credential)");
                } else {
                    valid = false;
                    logger.error("  Failed on point 3 [1 of 2] (no digitalSignature keyUsage in the the VOMS Server credential)");
                }
                boolean foundCritical = false;
                Set critSet = vomsServerCertificate.getCriticalExtensionOIDs();
                if (critSet != null && !critSet.isEmpty()) {
                    for (Iterator i = critSet.iterator(); i.hasNext(); ) {
                        String oid = (String) i.next();
                        if (oid.equals(basicConstraintsOID)) foundCritical = true;
                    }
                }
                if ((vomsServerCertificate.getBasicConstraints() == -1) && foundCritical) {
                    logger.debug("  Passed on point 3 [2 of 2] (basicConstraints is critical and has CA=FALSE in the the VOMS Server credential)");
                } else {
                    valid = false;
                    if (foundCritical) {
                        if (vomsServerCertificate.getBasicConstraints() > -1) {
                            logger.error("  Failed on point 3 [2 of 2] (basicConstraints has CA=TRUE in the the VOMS Server credential)");
                        }
                    } else {
                        if (vomsServerCertificate.getBasicConstraints() > -1) {
                            logger.error("  Failed on point 3 [2 of 2] (basicConstraints is non-critical AND has CA=TRUE in the the VOMS Server credential)");
                        } else {
                            logger.error("  Failed on point 3 [2 of 2] (basicConstraints is non-critical in the the VOMS Server credential)");
                        }
                    }
                }
                if (valid) {
                    logger.debug("  Passed point 4 of validation (AC issuer is trusted)");
                }
                long milliseconds = vomsAC.getTime();
                if (milliseconds > 0) {
                    logger.debug("  Passed point 5 of validation (within time limits)");
                    logger.info("  ... is valid for a further " + ElapsedTime.prettyFormatMilliseconds(milliseconds) + " (h:m:s)");
                } else {
                    logger.error("  Failed point 5 of validation (not within time limits)");
                    if (milliseconds < 0) {
                        logger.error("  (in fact the certificate is not yet valid for another " + ElapsedTime.prettyFormatMilliseconds(-1L * milliseconds) + " (h:m:s) )");
                    }
                    valid = false;
                }
                X509Extensions vomsExtensions = vomsAC.getExtensions();
                for (Enumeration e = vomsExtensions.oids(); e.hasMoreElements(); ) {
                    DERObjectIdentifier oid = (DERObjectIdentifier) e.nextElement();
                    if (vomsExtensions.getExtension(oid).isCritical()) {
                        if (!(oid.equals(X509Extensions.AuditIdentity) || oid.equals(X509Extensions.TargetInformation))) {
                            logger.error("  Failed point 7 of validation (found a critical extension that should not be so) " + oid);
                            valid = false;
                        }
                    }
                }
                if (valid) {
                    logger.debug("  Passed point 7 of validation (no critical extensions that should not be so)");
                }
                String acIdentity = vomsAC.getHolder();
                int acIdentitySerial = vomsAC.getHolderSerial();
                if (acIdentity.equalsIgnoreCase(credIdentity) && (acIdentitySerial == credIdentitySerial)) {
                    logger.debug("  Passed another test (certificate identity matches the AC holder)");
                } else {
                    logger.error("  Failed another test (certificate identity does not match the AC holder)");
                    valid = false;
                }
                logger.info("  ... issuer = " + vomsAC.getIssuer());
                logger.info("  ... holder = " + vomsAC.getHolder());
                logger.info("  ... holder serial = " + vomsAC.getHolderSerial());
                logger.info("  ... serialNumber = " + vomsAC.getSerialNumber());
                VOMSAttributes vomsAttributes = new VOMSAttributes(vomsAC.getAttributes());
                ArrayList FQANs = vomsAttributes.getFQANs();
                Iterator iter2 = FQANs.iterator();
                while (iter2.hasNext()) {
                    logger.info("  ... FQAN = " + iter2.next());
                }
                if (valid) {
                    logger.info("  validated");
                } else {
                    logger.error("  NOT validated");
                }
            }
        } catch (Exception e) {
            logger.fatal("a fatal error occured..." + e);
            System.exit(0);
        }
    }
}
