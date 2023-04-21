package ao.dd.shell.impl.transfer.ftps.glub;

import com.glub.secureftp.bean.SSLCertificate;
import com.glub.secureftp.bean.SSLSessionManager;
import org.apache.log4j.Logger;

/**
 * User: aostrovsky
 * Date: 22-Jun-2009
 * Time: 3:25:03 PM
 */
public class SessionMan implements SSLSessionManager {

    private static final Logger LOG = Logger.getLogger(SessionMan.class);

    private SSLCertificate currentCert;

    public boolean continueWithCertificateHostMismatch(SSLCertificate cert, String actualHost, String certHost) {
        LOG.error("Certificate host mismatch.");
        return false;
    }

    public boolean continueWithExpiredCertificate(SSLCertificate cert) {
        LOG.error("Certificate expired.");
        return false;
    }

    public boolean continueWithInvalidCertificate(SSLCertificate cert) {
        LOG.error("Certificate invalid.");
        return false;
    }

    public boolean continueWithoutServerCertificate() {
        LOG.error("Certificate not sent from server.");
        return false;
    }

    public short newCertificateEncountered(SSLCertificate cert) {
        StringBuilder str = new StringBuilder();
        str.append("New certificate found:\n").append("Common Name........: ").append(cert.getCN()).append("\n").append("Start Date.........: ").append(cert.getStartDate()).append("\n").append("End Date...........: ").append(cert.getEndDate()).append("\n").append("Fingerprint........: ").append(cert.getFingerprint()).append("\n").append("Serial Number......: ").append(cert.getSerialNumber()).append("\n").append("Organization.......: ").append(cert.getOrg()).append("\n").append("Organizational Unit: ").append(cert.getOU()).append("\n").append("Locality...........: ").append(cert.getLocality()).append("\n").append("State/Province.....: ").append(cert.getState()).append("\n").append("Country............: ").append(cert.getCountry()).append("\n").append("Email..............: ").append(cert.getEmail()).append("\n").append("Issuer's Common Name........: ").append(cert.getIssuerCN()).append("\n").append("Issuer's Organization.......: ").append(cert.getIssuerOrg()).append("\n").append("Issuer's Organizational Unit: ").append(cert.getIssuerOU()).append("\n").append("Issuer's Locality...........: ").append(cert.getIssuerLocality()).append("\n").append("Issuer's State/Province.....: ").append(cert.getIssuerState()).append("\n").append("Issuer's Country............: ").append(cert.getIssuerCountry()).append("\n").append("Issuer's Email..............: ").append(cert.getIssuerEmail()).append("\n");
        LOG.debug(str.toString());
        return SSLSessionManager.ALLOW_CERTIFICATE;
    }

    public short replaceCertificate(SSLCertificate oldCert, SSLCertificate newCert) {
        LOG.debug("Replace certificate.");
        return SSLSessionManager.ALLOW_CERTIFICATE;
    }

    public void randomSeedIsGenerating() {
        LOG.debug("The random seed is generating... ");
    }

    public void randomSeedGenerated() {
        LOG.debug("The random seed is generated... ");
    }

    public void setCurrentCertificate(SSLCertificate currentCert) {
        this.currentCert = currentCert;
    }

    public SSLCertificate certificate() {
        return currentCert;
    }
}
