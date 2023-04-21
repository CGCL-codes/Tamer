package br.net.woodstock.rockframework.security.timestamp.impl;

import java.io.IOException;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import org.bouncycastle.cert.X509CertificateHolder;
import org.bouncycastle.cms.CMSSignedData;
import org.bouncycastle.tsp.TimeStampToken;
import org.bouncycastle.tsp.TimeStampTokenInfo;
import org.bouncycastle.util.Store;
import br.net.woodstock.rockframework.security.cert.CertificateType;
import br.net.woodstock.rockframework.security.timestamp.TimeStamp;
import br.net.woodstock.rockframework.security.util.SecurityUtils;

public abstract class BouncyCastleTimeStampHelper {

    private BouncyCastleTimeStampHelper() {
    }

    @SuppressWarnings("rawtypes")
    public static TimeStamp toTimeStamp(final TimeStampToken timeStampToken) throws IOException, CertificateException {
        if (timeStampToken == null) {
            return null;
        }
        TimeStampTokenInfo timeStampTokenInfo = timeStampToken.getTimeStampInfo();
        TimeStamp timeStamp = new TimeStamp();
        timeStamp.setDate(timeStampTokenInfo.getGenTime());
        timeStamp.setEncoded(timeStampToken.getEncoded());
        timeStamp.setHash(timeStampTokenInfo.getMessageImprintDigest());
        timeStamp.setNonce(timeStampTokenInfo.getNonce());
        timeStamp.setSerialNumber(timeStampTokenInfo.getSerialNumber());
        CMSSignedData signedData = timeStampToken.toCMSSignedData();
        Object signedContent = signedData.getSignedContent().getContent();
        if ((signedContent != null) && (signedContent.getClass().isArray())) {
            timeStamp.setContent((byte[]) signedContent);
        }
        Store certificatesStore = timeStampToken.getCertificates();
        Collection certificatesCollection = certificatesStore.getMatches(null);
        List<Certificate> certificates = new ArrayList<Certificate>();
        for (Object obj : certificatesCollection) {
            if (obj instanceof X509CertificateHolder) {
                X509CertificateHolder holder = (X509CertificateHolder) obj;
                byte[] encoded = holder.getEncoded();
                Certificate certificate = SecurityUtils.getCertificateFromFile(encoded, CertificateType.X509);
                certificates.add(certificate);
            }
        }
        timeStamp.setCertificates(certificates.toArray(new Certificate[certificates.size()]));
        return timeStamp;
    }
}
