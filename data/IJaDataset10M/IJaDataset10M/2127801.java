package uk.ac.warwick.dcs.cokefolk.util;

import uk.ac.warwick.dcs.cokefolk.client.ClientInteraction;
import uk.ac.warwick.dcs.cokefolk.ui.Interaction.Confirmation;
import uk.ac.warwick.dcs.cokefolk.util.text.XMLUtils;
import javax.net.ssl.*;
import java.security.*;
import java.security.cert.*;
import javax.security.auth.x500.X500Principal;
import java.text.DateFormat;
import java.io.*;
import java.util.*;
import java.util.logging.Logger;

public class SimpleX509TrustManager implements X509TrustManager {

    private static final Logger LOG = Logger.getLogger(SimpleX509TrustManager.class.getName());

    protected static final String KEYSTORE_FILE = "certs";

    /**
   * The default X509TrustManager. We'll delegate decisions to it, and fall back to the logic
   * in this class if the default X509TrustManager doesn't trust a site.
   */
    private static X509TrustManager defaultTrustManager = null;

    protected static KeyStore keyStore = null;

    private ClientInteraction io;

    public SimpleX509TrustManager(ClientInteraction io, String defaultTrustAlgorithm, boolean verifyKeyStore) {
        this.io = io;
        if (keyStore == null) {
            char[] keyStorePassword = null;
            try {
                keyStore = KeyStore.getInstance(KeyStore.getDefaultType());
                FileInputStream keyStream;
                try {
                    keyStream = new FileInputStream(KEYSTORE_FILE);
                } catch (FileNotFoundException e) {
                    keyStream = null;
                } catch (SecurityException e) {
                    LOG.info(e.toString());
                    keyStream = null;
                }
                if (verifyKeyStore) {
                    keyStorePassword = io.secureInput("Encrypted Communications", "Enter the password for the encryption keys file");
                }
                try {
                    keyStore.load(keyStream, keyStorePassword);
                } catch (IOException e) {
                    if (keyStream != null) {
                        try {
                            keyStream.close();
                        } catch (IOException e2) {
                            LOG.warning(e2.toString());
                        }
                        keyStream = new FileInputStream(KEYSTORE_FILE);
                        keyStorePassword = io.secureInput("Encrypted Communications", "Enter the password for the encryption keys file");
                        keyStore.load(keyStream, keyStorePassword);
                    } else {
                        throw e;
                    }
                }
                Runtime.getRuntime().addShutdownHook(new ShutdownHook(keyStorePassword));
            } catch (KeyStoreException e) {
                LOG.warning(e.toString());
            } catch (IOException e) {
                LOG.info(e.toString());
            } catch (NoSuchAlgorithmException e) {
                LOG.info(e.toString());
            } catch (CertificateException e) {
                LOG.info(e.toString());
            }
            try {
                TrustManagerFactory tmf = TrustManagerFactory.getInstance(defaultTrustAlgorithm);
                tmf.init(keyStore);
                TrustManager[] trustManagers = tmf.getTrustManagers();
                for (TrustManager trustManager : trustManagers) {
                    if (trustManager instanceof X509TrustManager) {
                        defaultTrustManager = (X509TrustManager) trustManager;
                        break;
                    }
                }
            } catch (NoSuchAlgorithmException e) {
                LOG.info(e.toString());
            } catch (KeyStoreException e) {
                LOG.warning(e.toString());
            }
        }
    }

    private static class ShutdownHook extends Thread {

        private static final Logger LOG2 = Logger.getLogger(ShutdownHook.class.getName());

        private final char[] password;

        public ShutdownHook(char[] password) {
            this.password = password;
        }

        @Override
        public void run() {
            try {
                FileOutputStream keyStream = new FileOutputStream(SimpleX509TrustManager.KEYSTORE_FILE);
                if (password != null) {
                    SimpleX509TrustManager.keyStore.store(keyStream, password);
                } else {
                    SimpleX509TrustManager.keyStore.store(keyStream, "clientkspw".toCharArray());
                }
            } catch (Exception e) {
                LOG2.warning(e.toString());
            }
        }
    }

    public void checkClientTrusted(X509Certificate[] chain, String authType) throws CertificateException {
        if (defaultTrustManager != null && !isUnderEclipse()) {
            try {
                defaultTrustManager.checkClientTrusted(chain, authType);
            } catch (CertificateException e) {
                checkTrusted(chain, e);
            }
        } else {
            checkTrusted(chain, new CertificateException("Untrusted certificate"));
        }
    }

    public void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException {
        if (defaultTrustManager != null && !isUnderEclipse()) {
            try {
                defaultTrustManager.checkServerTrusted(chain, authType);
            } catch (CertificateException e) {
                checkTrusted(chain, e);
            }
        } else {
            checkTrusted(chain, new CertificateException("Untrusted certificate"));
        }
    }

    private void checkTrusted(X509Certificate[] chain, CertificateException e) throws CertificateException {
        X509Certificate cert = chain[0];
        boolean fromKeyStore;
        try {
            if (keyStore != null && keyStore.getCertificateAlias(cert) != null) {
                try {
                    cert.checkValidity();
                    return;
                } catch (CertificateExpiredException e2) {
                    fromKeyStore = true;
                } catch (CertificateNotYetValidException e2) {
                    fromKeyStore = true;
                }
            } else {
                fromKeyStore = false;
            }
        } catch (KeyStoreException e2) {
            LOG.info(e2.toString());
            fromKeyStore = false;
        }
        X500Principal subject = cert.getSubjectX500Principal();
        String subjectName = subject.getName();
        StringBuilder prompt = new StringBuilder("<html>");
        prompt.append("Information that you exchange with this computer cannot be viewed or <br>");
        prompt.append("changed by others.  However, there is a problem with the computer's <br>");
        prompt.append("security certificate.");
        prompt.append("<ul>");
        String inDate;
        try {
            cert.checkValidity();
            inDate = "<li> The security certificate date is valid. </li>";
            if (!fromKeyStore) {
                prompt.append("<li> The security certificate was issued by an organization you <br>");
                prompt.append("have not chosen to trust. View the information below to <br>");
                prompt.append("determine whether you want to trust the certificate. </li>");
            } else {
                prompt.append("<li> The security certificate is trusted. </li>");
            }
        } catch (CertificateExpiredException e2) {
            Date expiryDate = cert.getNotAfter();
            inDate = "<li><font color='red'>The security certificate expired on " + DateFormat.getDateInstance(DateFormat.SHORT).format(expiryDate) + "</font></li>";
        } catch (CertificateNotYetValidException e2) {
            inDate = "<li><font color='red'>The security certificate is not yet valid.</font></li>";
        }
        prompt.append(inDate);
        prompt.append("</ul>");
        prompt.append("<h3>Subject</h3>");
        if (!subjectName.equals("")) {
            prompt.append(principalToString(subject));
        } else {
            prompt.append("<ul><li><font color='red'>The subject is unknown.</font></li></ul>");
        }
        X500Principal issuer = cert.getIssuerX500Principal();
        if (!issuer.getName().equals("")) {
            prompt.append("<h3>Issuer</h3>");
            prompt.append(principalToString(issuer));
        }
        prompt.append("<h3>Details</h3>");
        boolean[] keyUsage = cert.getKeyUsage();
        StringBuilder uses = new StringBuilder();
        if (keyUsage != null) {
            if (keyUsage[3] || keyUsage[7]) uses.append("Encryption, ");
            if (keyUsage[8]) uses.append("Decryption, ");
            if (keyUsage[0]) uses.append("Signing, ");
            if (keyUsage[4]) uses.append("Key Agreement ");
            if (keyUsage[1]) uses.append("Non Repundiation, ");
            if (keyUsage[2]) uses.append("Key Enchiperment, ");
            if (keyUsage[5]) uses.append("Key Certificate Signing, ");
            if (keyUsage[6]) uses.append("Certificate Revocation List Signing, ");
            if (keyUsage.length > 9) uses.append("Other, ");
        }
        List<String> extendedUses = cert.getExtendedKeyUsage();
        if (extendedUses != null) {
            for (String use : extendedUses) {
                uses.append(use);
                uses.append(", ");
            }
        }
        int length = uses.length();
        prompt.append("Certified Uses: ");
        if (length >= 2) {
            uses.delete(length - 2, length - 1);
            prompt.append(uses);
        } else {
            prompt.append("Unspecified");
        }
        prompt.append("<br>");
        Confirmation option = io.acceptCertificate(chain, prompt.toString());
        switch(option) {
            case YES:
                break;
            case ALWAYS:
                {
                    if (!subjectName.equals("") && keyStore != null) {
                        try {
                            keyStore.setCertificateEntry(subjectName, cert);
                        } catch (KeyStoreException e2) {
                            LOG.info(e.toString());
                        }
                    }
                    break;
                }
            default:
                throw e;
        }
    }

    private static String principalToString(X500Principal principal) {
        String str = XMLUtils.escapeHTMLCharacters(principal.getName("CANONICAL"));
        str = str.replaceAll("cn=", "Name: ");
        str = str.replaceAll("o=", "<br>Organization: ");
        str = str.replaceAll("ou=", "<br>Department: ");
        str = str.replaceAll("l=", "<br>Location: ");
        str = str.replaceAll("st=", "<br>State/Province: ");
        str = str.replaceAll("c=", "<br>Country: ");
        return str;
    }

    /** Passes through to the default trust manager. */
    public X509Certificate[] getAcceptedIssuers() {
        if (defaultTrustManager != null) {
            return defaultTrustManager.getAcceptedIssuers();
        } else {
            return new X509Certificate[0];
        }
    }

    private static boolean isUnderEclipse() {
        boolean isUnderEclipse = false;
        try {
            isUnderEclipse = Boolean.parseBoolean(System.getProperty("undereclipse"));
        } catch (SecurityException e) {
            LOG.info(e.toString());
        }
        return isUnderEclipse;
    }
}
