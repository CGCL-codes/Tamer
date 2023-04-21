package net.sf.jsignpdf.crl;

import static net.sf.jsignpdf.Constants.RES;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.security.cert.CRL;
import java.security.cert.CRLException;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.util.HashSet;
import java.util.Set;
import net.sf.jsignpdf.BasicSignerOptions;
import net.sf.jsignpdf.Constants;
import org.apache.commons.io.input.CountingInputStream;
import org.apache.log4j.Logger;
import org.bouncycastle.asn1.DERString;
import org.bouncycastle.asn1.x509.CRLDistPoint;
import org.bouncycastle.asn1.x509.DistributionPoint;
import org.bouncycastle.asn1.x509.DistributionPointName;
import org.bouncycastle.asn1.x509.GeneralName;
import org.bouncycastle.asn1.x509.GeneralNames;
import org.bouncycastle.asn1.x509.X509Extension;
import org.bouncycastle.x509.extension.X509ExtensionUtil;

/**
 * Helper bean for holding CRL related data.
 * 
 * @author Josef Cacek
 * 
 */
public class CRLInfo {

    private static final Logger LOGGER = Logger.getLogger(CRLInfo.class);

    private CRL[] crls;

    private long byteCount = 0L;

    private BasicSignerOptions options;

    private Certificate[] certChain;

    /**
   * Constructor
   * 
   * @param anOptions
   * @param aChain
   */
    public CRLInfo(final BasicSignerOptions anOptions, final Certificate[] aChain) {
        if (anOptions == null || aChain == null) {
            throw new NullPointerException();
        }
        options = anOptions;
        certChain = aChain;
    }

    /**
   * Returns CRLs for the certificate chain.
   * 
   * @return
   */
    public CRL[] getCrls() {
        initCrls();
        return crls;
    }

    /**
   * Returns byte count, which should
   * 
   * @return
   */
    public long getByteCount() {
        initCrls();
        return byteCount;
    }

    /**
   * Initialize CRLs (load URLs from certificates and download the CRLs).
   */
    private void initCrls() {
        if (!options.isCrlEnabledX() || crls != null) {
            return;
        }
        LOGGER.info(RES.get("console.readingCRLs"));
        final Set<String> urls = new HashSet<String>();
        for (Certificate cert : certChain) {
            if (cert instanceof X509Certificate) {
                urls.addAll(getCrlUrls((X509Certificate) cert));
            }
        }
        final Set<CRL> crlSet = new HashSet<CRL>();
        for (final String urlStr : urls) {
            try {
                LOGGER.info(RES.get("console.crlinfo.loadCrl", urlStr));
                final URL tmpUrl = new URL(urlStr);
                final CountingInputStream inStream = new CountingInputStream(tmpUrl.openConnection(options.createProxy()).getInputStream());
                final CertificateFactory cf = CertificateFactory.getInstance(Constants.CERT_TYPE_X509);
                final CRL crl = cf.generateCRL(inStream);
                final long tmpBytesRead = inStream.getByteCount();
                LOGGER.info(RES.get("console.crlinfo.crlSize", String.valueOf(tmpBytesRead)));
                if (!crlSet.contains(crl)) {
                    byteCount += tmpBytesRead;
                    crlSet.add(crl);
                } else {
                    LOGGER.info(RES.get("console.crlinfo.alreadyLoaded"));
                }
                inStream.close();
            } catch (MalformedURLException e) {
                LOGGER.warn("", e);
            } catch (IOException e) {
                LOGGER.warn("", e);
            } catch (CertificateException e) {
                LOGGER.warn("", e);
            } catch (CRLException e) {
                LOGGER.warn("", e);
            }
        }
        crls = crlSet.toArray(new CRL[crlSet.size()]);
    }

    /**
   * Returns (initialized, but maybe empty) set of URLs of CRLs for given
   * certificate.
   * 
   * @param aCert
   *          X509 certificate.
   * @return
   */
    private Set<String> getCrlUrls(final X509Certificate aCert) {
        final Set<String> tmpResult = new HashSet<String>();
        LOGGER.info(RES.get("console.crlinfo.retrieveCrlUrl", aCert.getSubjectX500Principal().getName()));
        final byte[] crlDPExtension = aCert.getExtensionValue(X509Extension.cRLDistributionPoints.getId());
        if (crlDPExtension != null) {
            CRLDistPoint crlDistPoints = null;
            try {
                crlDistPoints = CRLDistPoint.getInstance(X509ExtensionUtil.fromExtensionValue(crlDPExtension));
            } catch (IOException e) {
                LOGGER.warn("", e);
            }
            if (crlDistPoints != null) {
                final DistributionPoint[] distPoints = crlDistPoints.getDistributionPoints();
                distPoint: for (DistributionPoint dp : distPoints) {
                    final DistributionPointName dpName = dp.getDistributionPoint();
                    final GeneralNames generalNames = (GeneralNames) dpName.getName();
                    if (generalNames != null) {
                        final GeneralName[] generalNameArr = generalNames.getNames();
                        if (generalNameArr != null) {
                            for (final GeneralName generalName : generalNameArr) {
                                if (generalName.getTagNo() == GeneralName.uniformResourceIdentifier) {
                                    final DERString derString = (DERString) generalName.getName();
                                    final String uri = derString.getString();
                                    if (uri != null && uri.startsWith("http")) {
                                        LOGGER.info(RES.get("console.crlinfo.foundCrlUri", uri));
                                        tmpResult.add(uri);
                                        continue distPoint;
                                    }
                                }
                            }
                        }
                        LOGGER.info(RES.get("console.crlinfo.noUrlInDistPoint"));
                    }
                }
            }
        } else {
            LOGGER.info(RES.get("console.crlinfo.distPointNotSupported"));
        }
        return tmpResult;
    }
}
