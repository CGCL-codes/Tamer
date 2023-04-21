package org.codegallery.javagal.ssl;

import java.io.FileInputStream;
import java.io.IOException;
import java.security.GeneralSecurityException;
import java.security.KeyStore;
import javax.net.ssl.KeyManager;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.TrustManagerFactory;

public class CustomTrustStoreClient extends CustomKeyStoreClient {

    private final String DEFAULT_TRUSTSTORE = "clientTrust";

    private final String DEFAULT_TRUSTSTORE_PASSWORD = "password";

    private String trustStore = DEFAULT_TRUSTSTORE;

    private String trustStorePassword = DEFAULT_TRUSTSTORE_PASSWORD;

    /**
	   * Overrides main() in SimpleSSLClient to use the
	   * CustomTrustStoreClient.
	   */
    public static void main(String args[]) {
        CustomTrustStoreClient client = new CustomTrustStoreClient();
        client.runClient(args);
        client.close();
    }

    /**
	   * Overrides the version in SimpleSSLClient to handle the -ts and
	   * -tspass arguments.
	   * @param args Array of strings.
	   * @param i array cursor.
	   * @return number of successfully handled arguments, zero if an
	   * error was encountered.
	   */
    protected int handleCommandLineOption(String[] args, int i) {
        int out;
        try {
            String arg = args[i].trim().toUpperCase();
            if (arg.equals("-TS")) {
                trustStore = args[i + 1];
                out = 2;
            } else if (arg.equals("-TSPASS")) {
                trustStorePassword = args[i + 1];
                out = 2;
            } else out = super.handleCommandLineOption(args, i);
        } catch (Exception e) {
            out = 0;
        }
        return out;
    }

    /**
	   * Displays the command-line usage for this client.
	   */
    protected void displayUsage() {
        super.displayUsage();
        System.out.println("\t-ts\ttruststore (default '" + DEFAULT_TRUSTSTORE + "', JKS format)");
        System.out.println("\t-tspass\ttruststore password (default '" + DEFAULT_TRUSTSTORE_PASSWORD + "')");
    }

    /**
	   * Provides a SSLSocketFactory which ignores JSSE's choice of truststore,
	   * and instead uses either the hard-coded filename and password, or those
	   * passed in on the command-line.
	   * This method calls out to getTrustManagers() to do most of the
	   * grunt-work. It actally just needs to set up a SSLContext and obtain
	   * the SSLSocketFactory from there.
	   * @return SSLSocketFactory SSLSocketFactory to use
	   */
    protected SSLSocketFactory getSSLSocketFactory() throws IOException, GeneralSecurityException {
        TrustManager[] tms = getTrustManagers();
        KeyManager[] kms = getKeyManagers();
        SSLContext context = SSLContext.getInstance("SSL");
        context.init(kms, tms, null);
        SSLSocketFactory ssf = context.getSocketFactory();
        return ssf;
    }

    /**
	   * Returns an array of TrustManagers, set up to use the required
	   * trustStore. This is pulled out separately so that later  
	   * examples can call it.
	   * This method does the bulk of the work of setting up the custom
	   * trust managers.
	   * @param trustStore the TrustStore to use. This should be in JKS format.
	   * @param password the password for this TrustStore.
	   * @return an array of TrustManagers set up accordingly.
	   */
    protected TrustManager[] getTrustManagers() throws IOException, GeneralSecurityException {
        String alg = TrustManagerFactory.getDefaultAlgorithm();
        TrustManagerFactory tmFact = TrustManagerFactory.getInstance(alg);
        FileInputStream fis = new FileInputStream(trustStore);
        KeyStore ks = KeyStore.getInstance("jks");
        ks.load(fis, trustStorePassword.toCharArray());
        fis.close();
        tmFact.init(ks);
        TrustManager[] tms = tmFact.getTrustManagers();
        return tms;
    }
}
