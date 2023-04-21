package org.hardtokenmgmt.admin.model.statusanalyzers;

import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import org.bouncycastle.util.encoders.Base64;
import org.ejbca.util.CertTools;
import org.hardtokenmgmt.common.vo.CAInfoVO;
import org.hardtokenmgmt.common.Constants;
import org.hardtokenmgmt.core.InterfaceFactory;
import org.hardtokenmgmt.core.log.LocalLog;
import org.hardtokenmgmt.core.util.CertUtils;

/**
 * 
 * Class in charge of analyzing the data of a CA Info VO
 * 
 * @author Philip Vendil 22 feb 2009
 *
 * @version $Id$
 */
public class CAStatusAnalyzer extends BaseExtendedStatusAnalyzer implements Comparable<CAStatusAnalyzer> {

    private CAInfoVO cAInfoVO = null;

    public CAStatusAnalyzer(String hostname, CAInfoVO cAInfoVO) {
        super(hostname);
        this.cAInfoVO = cAInfoVO;
    }

    /**
	 * @return the current CAInfoVO
	 */
    public CAInfoVO getCAInfoVO() {
        return cAInfoVO;
    }

    /**
	 * @see org.hardtokenmgmt.admin.model.BaseStatusAnalyzer#analyze()
	 */
    @Override
    protected void analyze() {
        status = Constants.STATUS_OK;
        statusMessage = "adminstatus.caonline";
        if (getCACertificate() != null) {
            int expireInDays = Integer.parseInt(InterfaceFactory.getGlobalSettings().getProperty(Constants.THRESHOLDSETTING_CAEXPIRE, Constants.DEFAULT_SETTINGS.getProperty(Constants.THRESHOLDSETTING_CAEXPIRE)));
            long expireInMS = expireInDays * 24L * 3600L * 1000L;
            long certExpireThreshold = getCACertificate().getNotAfter().getTime() - expireInMS;
            if (new Date().after(new Date(certExpireThreshold))) {
                status = Constants.STATUS_WARNING;
                statusMessage = "adminstatus.caexpiring";
                alarmPriority = Constants.ALARMPRIO_CAEXPIRING;
            }
        }
        String cAStatus = getCAInfoVO().getCAStatus();
        if (cAStatus.equalsIgnoreCase(Constants.CASTATUS_REVOKED)) {
            status = Constants.STATUS_OK;
            statusMessage = "adminca.revoked";
        } else {
            if (cAStatus.equalsIgnoreCase(Constants.CASTATUS_OFFLINE)) {
                status = Constants.STATUS_OK;
                statusMessage = "adminstatus.caoffline";
            }
            String cATokenStatus = getCAInfoVO().getCATokenStatus();
            if (getThreshold().equalsIgnoreCase("true")) {
                if (cATokenStatus.equals(Constants.CATOKENSTATUS_OFFLINE)) {
                    status = Constants.STATUS_ERROR;
                    statusMessage = "adminstatus.caoffline";
                    alarmPriority = Constants.ALARMPRIO_CAOFFLINE;
                }
            }
        }
        if (getCACertificate() != null) {
            if (getCACertificate().getNotAfter().before(new Date())) {
                status = Constants.STATUS_ERROR;
                statusMessage = "adminstatus.caexpired";
                alarmPriority = Constants.ALARMPRIO_CAEXPIRED;
                return;
            }
        }
    }

    /**
	 * @see org.hardtokenmgmt.admin.model.BaseStatusAnalyzer#getThreshold()
	 */
    @Override
    public String getThreshold() {
        String t = super.getThreshold();
        if (t == null) {
            return "true";
        }
        return t;
    }

    /**
	 * Checks that the string is boolean.
	 * 
	 * @see org.hardtokenmgmt.admin.model.BaseStatusAnalyzer#setThreshold(java.lang.String)
	 */
    @Override
    public void setThreshold(String threshold) {
        if (AnalyzeHelper.isThresholdValueBoolean(this, threshold)) {
            super.setThreshold(threshold);
        }
    }

    public boolean isMonitored() {
        return Boolean.parseBoolean(getThreshold());
    }

    public void resetThreshold() {
        super.setThreshold("true");
    }

    public X509Certificate getCACertificate() {
        readCertificates();
        if (cACerts.size() > 0) {
            return cACerts.get(0);
        }
        return null;
    }

    public List<X509Certificate> getCACertificateChain() {
        readCertificates();
        return cACerts;
    }

    private ArrayList<X509Certificate> cACerts = null;

    private void readCertificates() {
        if (cACerts == null) {
            cACerts = new ArrayList<X509Certificate>();
            for (String certString : cAInfoVO.getCertificatePath()) {
                byte[] certData = Base64.decode(certString.getBytes());
                try {
                    cACerts.add(CertUtils.getCertfromByteArray(certData));
                } catch (CertificateException e) {
                    LocalLog.getLogger().log(Level.SEVERE, "Error decoding CA certificate", e);
                }
            }
        }
    }

    public String getSubject() {
        return CertTools.getSubjectDN(getCACertificate());
    }

    public String getIssuer() {
        return CertTools.getIssuerDN(getCACertificate());
    }

    @Override
    public int compareTo(CAStatusAnalyzer o) {
        return getCAInfoVO().getCAName().toLowerCase().compareTo(o.getCAInfoVO().getCAName().toLowerCase());
    }
}
