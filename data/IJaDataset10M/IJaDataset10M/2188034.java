package com.unboundid.util.ssl;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.IOException;
import java.io.PrintStream;
import java.security.MessageDigest;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.Date;
import java.util.concurrent.ConcurrentHashMap;
import javax.net.ssl.X509TrustManager;
import javax.security.auth.x500.X500Principal;
import com.unboundid.util.NotMutable;
import com.unboundid.util.ThreadSafety;
import com.unboundid.util.ThreadSafetyLevel;
import static com.unboundid.util.Debug.*;
import static com.unboundid.util.StaticUtils.*;
import static com.unboundid.util.ssl.SSLMessages.*;

/**
 * This class provides an SSL trust manager that will interactively prompt the
 * user to determine whether to trust any certificate that is presented to it.
 * It provides the ability to cache information about certificates that had been
 * previously trusted so that the user is not prompted about the same
 * certificate repeatedly, and it can be configured to store trusted
 * certificates in a file so that the trust information can be persisted.
 */
@NotMutable()
@ThreadSafety(level = ThreadSafetyLevel.COMPLETELY_THREADSAFE)
public final class PromptTrustManager implements X509TrustManager {

    /**
   * The message digest that will be used for MD5 hashes.
   */
    private static final MessageDigest MD5;

    /**
   * The message digest that will be used for SHA-1 hashes.
   */
    private static final MessageDigest SHA1;

    static {
        MessageDigest d = null;
        try {
            d = MessageDigest.getInstance("MD5");
        } catch (final Exception e) {
            debugException(e);
            throw new RuntimeException(e);
        }
        MD5 = d;
        d = null;
        try {
            d = MessageDigest.getInstance("SHA-1");
        } catch (final Exception e) {
            debugException(e);
            throw new RuntimeException(e);
        }
        SHA1 = d;
    }

    private final boolean examineValidityDates;

    private final ConcurrentHashMap<String, Boolean> acceptedCerts;

    private final InputStream in;

    private final PrintStream out;

    private final String acceptedCertsFile;

    /**
   * Creates a new instance of this prompt trust manager.  It will cache trust
   * information in memory but not on disk.
   */
    public PromptTrustManager() {
        this(null, true, null, null);
    }

    /**
   * Creates a new instance of this prompt trust manager.  It may optionally
   * cache trust information on disk.
   *
   * @param  acceptedCertsFile  The path to a file in which the certificates
   *                            that have been previously accepted will be
   *                            cached.  It may be {@code null} if the cache
   *                            should only be maintained in memory.
   */
    public PromptTrustManager(final String acceptedCertsFile) {
        this(acceptedCertsFile, true, null, null);
    }

    /**
   * Creates a new instance of this prompt trust manager.  It may optionally
   * cache trust information on disk, and may also be configured to examine or
   * ignore validity dates.
   *
   * @param  acceptedCertsFile     The path to a file in which the certificates
   *                               that have been previously accepted will be
   *                               cached.  It may be {@code null} if the cache
   *                               should only be maintained in memory.
   * @param  examineValidityDates  Indicates whether to reject certificates if
   *                               the current time is outside the validity
   *                               window for the certificate.
   * @param  in                    The input stream that will be used to read
   *                               input from the user.  If this is {@code null}
   *                               then {@code System.in} will be used.
   * @param  out                   The print stream that will be used to display
   *                               the prompt to the user.  If this is
   *                               {@code null} then System.out will be used.
   */
    public PromptTrustManager(final String acceptedCertsFile, final boolean examineValidityDates, final InputStream in, final PrintStream out) {
        this.acceptedCertsFile = acceptedCertsFile;
        this.examineValidityDates = examineValidityDates;
        if (in == null) {
            this.in = System.in;
        } else {
            this.in = in;
        }
        if (out == null) {
            this.out = System.out;
        } else {
            this.out = out;
        }
        acceptedCerts = new ConcurrentHashMap<String, Boolean>();
        if (acceptedCertsFile != null) {
            BufferedReader r = null;
            try {
                final File f = new File(acceptedCertsFile);
                if (f.exists()) {
                    r = new BufferedReader(new FileReader(f));
                    while (true) {
                        final String line = r.readLine();
                        if (line == null) {
                            break;
                        }
                        acceptedCerts.put(line, false);
                    }
                }
            } catch (Exception e) {
                debugException(e);
            } finally {
                if (r != null) {
                    try {
                        r.close();
                    } catch (Exception e) {
                        debugException(e);
                    }
                }
            }
        }
    }

    /**
   * Writes an updated copy of the trusted certificate cache to disk.
   *
   * @throws  IOException  If a problem occurs.
   */
    private void writeCacheFile() throws IOException {
        final File tempFile = new File(acceptedCertsFile + ".new");
        BufferedWriter w = null;
        try {
            w = new BufferedWriter(new FileWriter(tempFile));
            for (final String certBytes : acceptedCerts.keySet()) {
                w.write(certBytes);
                w.newLine();
            }
        } finally {
            if (w != null) {
                w.close();
            }
        }
        final File cacheFile = new File(acceptedCertsFile);
        if (cacheFile.exists()) {
            final File oldFile = new File(acceptedCertsFile + ".previous");
            if (oldFile.exists()) {
                oldFile.delete();
            }
            cacheFile.renameTo(oldFile);
        }
        tempFile.renameTo(cacheFile);
    }

    /**
   * Performs the necessary validity check for the provided certificate array.
   *
   * @param  chain       The chain of certificates for which to make the
   *                     determination.
   * @param  serverCert  Indicates whether the certificate was presented as a
   *                     server certificate or as a client certificate.
   *
   * @throws  CertificateException  If the provided certificate chain should not
   *                                be trusted.
   */
    private synchronized void checkCertificateChain(final X509Certificate[] chain, final boolean serverCert) throws CertificateException {
        String validityWarning = null;
        final Date currentDate = new Date();
        final X509Certificate c = chain[0];
        if (examineValidityDates) {
            if (currentDate.before(c.getNotBefore())) {
                validityWarning = WARN_PROMPT_NOT_YET_VALID.get();
            } else if (currentDate.after(c.getNotAfter())) {
                validityWarning = WARN_PROMPT_EXPIRED.get();
            }
        }
        if ((!examineValidityDates) || (validityWarning == null)) {
            final String certBytes = toLowerCase(toHex(c.getSignature()));
            final Boolean accepted = acceptedCerts.get(certBytes);
            if (accepted != null) {
                if ((validityWarning == null) || (!examineValidityDates) || Boolean.TRUE.equals(accepted)) {
                    return;
                }
            }
        }
        if (serverCert) {
            out.println(INFO_PROMPT_SERVER_HEADING.get());
        } else {
            out.println(INFO_PROMPT_CLIENT_HEADING.get());
        }
        out.println('\t' + INFO_PROMPT_SUBJECT.get(c.getSubjectX500Principal().getName(X500Principal.CANONICAL)));
        out.println("\t\t" + INFO_PROMPT_MD5_FINGERPRINT.get(getFingerprint(c, MD5)));
        out.println("\t\t" + INFO_PROMPT_SHA1_FINGERPRINT.get(getFingerprint(c, SHA1)));
        for (int i = 1; i < chain.length; i++) {
            out.println('\t' + INFO_PROMPT_ISSUER_SUBJECT.get(i, chain[i].getSubjectX500Principal().getName(X500Principal.CANONICAL)));
            out.println("\t\t" + INFO_PROMPT_MD5_FINGERPRINT.get(getFingerprint(chain[i], MD5)));
            out.println("\t\t" + INFO_PROMPT_SHA1_FINGERPRINT.get(getFingerprint(chain[i], SHA1)));
        }
        out.println(INFO_PROMPT_VALIDITY.get(String.valueOf(c.getNotBefore()), String.valueOf(c.getNotAfter())));
        if (chain.length == 1) {
            out.println();
            out.println(WARN_PROMPT_SELF_SIGNED.get());
        }
        if (validityWarning != null) {
            out.println();
            out.println(validityWarning);
        }
        final BufferedReader reader = new BufferedReader(new InputStreamReader(in));
        while (true) {
            try {
                out.println();
                out.println(INFO_PROMPT_MESSAGE.get());
                out.flush();
                final String line = reader.readLine();
                if (line.equalsIgnoreCase("y") || line.equalsIgnoreCase("yes")) {
                    break;
                } else if (line.equalsIgnoreCase("n") || line.equalsIgnoreCase("no")) {
                    throw new CertificateException(ERR_CERTIFICATE_REJECTED_BY_USER.get());
                }
            } catch (CertificateException ce) {
                throw ce;
            } catch (Exception e) {
                debugException(e);
            }
        }
        final String certBytes = toLowerCase(toHex(c.getSignature()));
        acceptedCerts.put(certBytes, (validityWarning != null));
        if (acceptedCertsFile != null) {
            try {
                writeCacheFile();
            } catch (Exception e) {
                debugException(e);
            }
        }
    }

    /**
   * Computes the fingerprint for the provided certificate using the given
   * digest.
   *
   * @param  c  The certificate for which to obtain the fingerprint.
   * @param  d  The message digest to use when creating the fingerprint.
   *
   * @return  The generated certificate fingerprint.
   *
   * @throws  CertificateException  If a problem is encountered while generating
   *                                the certificate fingerprint.
   */
    private static String getFingerprint(final X509Certificate c, final MessageDigest d) throws CertificateException {
        final byte[] encodedCertBytes = c.getEncoded();
        final byte[] digestBytes;
        synchronized (d) {
            digestBytes = d.digest(encodedCertBytes);
        }
        final StringBuilder buffer = new StringBuilder(3 * encodedCertBytes.length);
        toHex(digestBytes, ":", buffer);
        return buffer.toString();
    }

    /**
   * Indicate whether to prompt about certificates contained in the cache if the
   * current time is outside the validity window for the certificate.
   *
   * @return  {@code true} if the certificate validity time should be examined
   *          for cached certificates and the user should be prompted if they
   *          are expired or not yet valid, or {@code false} if cached
   *          certificates should be accepted even outside of the validity
   *          window.
   */
    public boolean examineValidityDates() {
        return examineValidityDates;
    }

    /**
   * Checks to determine whether the provided client certificate chain should be
   * trusted.
   *
   * @param  chain     The client certificate chain for which to make the
   *                   determination.
   * @param  authType  The authentication type based on the client certificate.
   *
   * @throws  CertificateException  If the provided client certificate chain
   *                                should not be trusted.
   */
    public void checkClientTrusted(final X509Certificate[] chain, final String authType) throws CertificateException {
        checkCertificateChain(chain, false);
    }

    /**
   * Checks to determine whether the provided server certificate chain should be
   * trusted.
   *
   * @param  chain     The server certificate chain for which to make the
   *                   determination.
   * @param  authType  The key exchange algorithm used.
   *
   * @throws  CertificateException  If the provided server certificate chain
   *                                should not be trusted.
   */
    public void checkServerTrusted(final X509Certificate[] chain, final String authType) throws CertificateException {
        checkCertificateChain(chain, true);
    }

    /**
   * Retrieves the accepted issuer certificates for this trust manager.  This
   * will always return an empty array.
   *
   * @return  The accepted issuer certificates for this trust manager.
   */
    public X509Certificate[] getAcceptedIssuers() {
        return new X509Certificate[0];
    }
}
