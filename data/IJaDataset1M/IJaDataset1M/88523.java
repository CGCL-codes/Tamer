package crypto.rsacomp.ssl;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.security.KeyStore;
import java.security.Provider;
import java.security.PublicKey;
import java.security.Security;
import java.util.Properties;
import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLPeerUnverifiedException;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManagerFactory;
import javax.security.cert.X509Certificate;
import com.rsa.jsafe.provider.JsafeJCE;
import com.rsa.jsse.JsseProvider;
import com.rsa.jsse.SuiteBMode;
import crypto.rsacomp.common.Print;

/**
 * Abstract class for all RSA JSSE samples. Any class extending
 * <code>JsseSample</code> needs to implement the abstract method
 * <code>runSample()</code>.
 */
public abstract class Common {

    private static final int HEX_BASE = 16;

    private static final int HIGHEST_PRINTABLE_CHARACTER = 126;

    private static final int LOWEST_PRINTABLE_CHARACTER = 32;

    private static final int LINE_NO_WIDTH = 3;

    private static final int HEX_A = 10;

    private static final int EIGHT_BIT = 8;

    private static final int FOUR_BIT = 4;

    private static final int HEX_F = 0xf;

    private static final int HEX_FF = 0xFF;

    private static final int BYTES_PER_LINE = 16;

    public static final JsseProvider RSAJSSE_PROVIDER = new JsseProvider();

    public static final Provider JSAFE_JCE_PROVIDER = new JsafeJCE();

    static void println() {
        Print.println();
    }

    static void println(String string) {
        Print.println(string);
    }

    /**
	 * Print provider information.
	 *
	 * @param prov The JSSE provider to use.
	 */
    protected void printProviderInfo(JsseProvider prov) {
        Common.println("JSSE Provider Name: " + prov.getName());
        Common.println("JSSE Suite-B mode: " + prov.getSuiteBMode());
        Common.println("JSSE FIPS-140 mode: " + prov.getFIPS140Mode());
    }

    /**
	 * Prints out the list of the currently registered providers.
	 */
    protected void printRegisteredProviders() {
        Provider[] currentProviders = Security.getProviders();
        Common.println("Current providers:  ");
        for (int index = 0; index < currentProviders.length; index++) {
            Common.println(currentProviders[index].getName() + " " + currentProviders[index].getVersion());
            Common.println("  " + currentProviders[index].getInfo());
        }
        Common.println();
    }

    /**
	 * Print the peer information.
	 *
	 * @param session Print peer information for this session.
	 * @throws SSLPeerUnverifiedException On failure.
	 */
    protected void printPeerInformation(SSLSession session) {
        Common.println("Peer information:");
        X509Certificate[] peerChain;
        try {
            peerChain = session.getPeerCertificateChain();
            if (peerChain != null) {
                Common.println("Subject name:");
                String subj = peerChain[0].getSubjectDN().toString();
                Common.println(subj);
                Common.println("Issuer name:");
                String issuer = peerChain[0].getIssuerDN().toString();
                Common.println(issuer);
                Common.println("Public Key (BER-encoded):");
                PublicKey pubKey = peerChain[0].getPublicKey();
                Print.printBuffer(pubKey.getEncoded());
                Common.println();
            }
        } catch (SSLPeerUnverifiedException e) {
            Common.println(" No peer information available.");
        }
        Common.println();
    }

    /**
	 * Print information about the connection.
	 *
	 * @param session Print connection information for this session.
	 */
    protected void printConnectionInfo(SSLSession session) {
        Common.println("Connection info:");
        String cipherSuite = session.getCipherSuite();
        Common.println("Cipher suite: " + cipherSuite);
        Common.println("Suite-B info: " + SuiteBMode.getConnectionStatus(session));
        Common.println();
    }

    /**
	 * Print information about the session.
	 *
	 * @param session Print connection information for this session.
	 */
    protected void printSessionInfo(SSLSession session) {
        printSessionInfo(session, null);
    }

    /**
	 * Print information about the session.
	 *
	 * @param session Print connection information for this session.
	 * @param label The label to use.
	 */
    protected void printSessionInfo(SSLSession session, String label) {
        if (label != null) {
            Common.println(label + " Session id: ");
        } else {
            Common.println("Session id: ");
        }
        Common.println(Print.toHexString(session.getId(), 0, session.getId().length));
        Common.println();
    }

    /**
	 * Creates the TrustManagerFactory and loads it with the certificate
	 * contained in a trusted key store.
	 *
	 * @param jks The key store filename.
	 * @param suiteB TODO
	 * @return The TrustedManagerFactory filled with the trusted certificates
	 * @throws Exception On failure.
	 */
    protected TrustManagerFactory createTrustManagerFactory(String jks, boolean suiteB) throws Exception {
        final String dataDir = "data/";
        char[] trustPassword = "password".toCharArray();
        KeyStore trustedKeyStore = KeyStore.getInstance("JKS");
        String truststore = dataDir + jks;
        InputStream ksStream = ResourceReader.getFileAsStream(truststore);
        trustedKeyStore.load(ksStream, trustPassword);
        ksStream.close();
        TrustManagerFactory tmf = TrustManagerFactory.getInstance(suiteB ? JsseProvider.PKIX_SUITEB : JsseProvider.PKIX, RSAJSSE_PROVIDER);
        tmf.init(trustedKeyStore);
        return tmf;
    }

    /**
	 * Creates the KeyManagerFactory and loads the private keys and
	 * certificates contained in the servers KeyStore.
	 *
	 * @param jks The key store filename.
	 * @return The KeyManager.
	 * @throws Exception If a problem occurs loading the KeyStore.
	 */
    protected KeyManagerFactory createKeyManagerFactory(String jks) throws Exception {
        final String dataDir = "data/";
        char[] keyPassword = "password".toCharArray();
        KeyStore serverKeyStore = KeyStore.getInstance("JKS");
        String keystore = dataDir + jks;
        InputStream ksStream = ResourceReader.getFileAsStream(keystore);
        serverKeyStore.load(ksStream, keyPassword);
        ksStream.close();
        KeyManagerFactory kmf = KeyManagerFactory.getInstance(JsseProvider.X509, RSAJSSE_PROVIDER);
        kmf.init(serverKeyStore, keyPassword);
        return kmf;
    }

    /** Write properties to output stream, null termintated.*/
    public static void sendProperties(Properties props, OutputStream outputStream) throws IOException {
        props.store(outputStream, "Sent PropertyList");
        outputStream.write(0);
        outputStream.flush();
    }

    /** Read properties from input stream, stop on null. */
    public static Properties receiveProperties(final InputStream inputStream, final int maxSize) throws Exception {
        Properties props = new Properties();
        props.load(new InputStream() {

            boolean eof;

            int size = 0;

            @Override
            public int read() throws IOException {
                if (eof) return -1;
                int byt = inputStream.read();
                if (byt == -1) throw new RuntimeException("Trucated input stream, truncation attack?");
                eof = byt == 0;
                if (eof) byt = -1;
                if (size++ > maxSize) throw new RuntimeException("Received more than " + maxSize);
                return byt;
            }
        });
        return props;
    }
}
