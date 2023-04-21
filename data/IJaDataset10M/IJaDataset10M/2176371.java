package edu.vt.middleware.crypt;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.security.KeyStore;
import java.security.PrivateKey;
import java.security.cert.Certificate;
import java.security.cert.X509Certificate;
import java.util.Enumeration;
import edu.vt.middleware.crypt.digest.MD5;
import edu.vt.middleware.crypt.digest.SHA1;
import edu.vt.middleware.crypt.util.CryptReader;
import edu.vt.middleware.crypt.util.CryptWriter;
import edu.vt.middleware.crypt.util.HexConverter;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.Option;

/**
 * Command line interface for keystore operations.
 *
 * @author  Middleware Services
 * @version  $Revision: 84 $
 */
public class KeyStoreCli extends AbstractCli {

    /** Default key algorithm name. */
    public static final String DEFAULT_KEY_ALGORITHM = "RSA";

    /** Option for listing keystore contents. */
    protected static final String OPT_LIST = "list";

    /** Option for importing keypair into keystore. */
    protected static final String OPT_IMPORT = "import";

    /** Option for exporting keypair from keystore. */
    protected static final String OPT_EXPORT = "export";

    /** Option for specifying keystore file. */
    protected static final String OPT_STORE = "keystore";

    /** Option for specifying keystore type. */
    protected static final String OPT_TYPE = "storetype";

    /** Option for specifying keystore password. */
    protected static final String OPT_PASS = "storepass";

    /** Option for specifying alias for keystore entry to import/export. */
    protected static final String OPT_ALIAS = "alias";

    /** Option for specifying certificate file to import/export. */
    protected static final String OPT_CERT = "cert";

    /** Option for specifying key file to import/export. */
    protected static final String OPT_KEY = "key";

    /** Option for specifying key cipher algorithm. */
    protected static final String OPT_KEYALG = "keyalg";

    /** Name of operation provided by this class. */
    private static final String COMMAND_NAME = "keystore";

    /** For calculating MD5 cert fingerprints. */
    private final MD5 md5 = new MD5();

    /** For calculating SHA-1 cert fingerprints. */
    private final SHA1 sha1 = new SHA1();

    /** For pretty-printing cert signature fingerprint in HEX. */
    private final HexConverter hexConv = new HexConverter(true);

    /**
   * CLI entry point method.
   *
   * @param  args  Command line arguments.
   */
    public static void main(final String[] args) {
        new KeyStoreCli().performAction(args);
    }

    /** {@inheritDoc} */
    protected void initOptions() {
        super.initOptions();
        final Option keystore = new Option(OPT_STORE, true, "keystore file");
        keystore.setArgName("filepath");
        keystore.setOptionalArg(false);
        final Option pass = new Option(OPT_PASS, true, "keystore password");
        pass.setArgName("password");
        pass.setOptionalArg(false);
        final Option type = new Option(OPT_TYPE, true, "keystore type, e.g. BKS (default), JKS");
        type.setArgName("name");
        type.setOptionalArg(false);
        final Option alias = new Option(OPT_ALIAS, true, "alias assigned to imported item or alias of item to export");
        alias.setArgName("name");
        alias.setOptionalArg(false);
        final Option cert = new Option(OPT_CERT, true, "X.509 certificate file; " + "encoding determined by file extension (der|pem)");
        cert.setArgName("filepath");
        cert.setOptionalArg(false);
        final Option key = new Option(OPT_KEY, true, "DER-encoded PKCS#8 or PEM-encoded SSLeay private key; " + "encoding determined by file extension (der|pem)");
        key.setArgName("filepath");
        key.setOptionalArg(false);
        final Option keyalg = new Option(OPT_KEYALG, true, "private key algorithm name; assumes RSA if not specified");
        keyalg.setArgName("algorithm");
        keyalg.setOptionalArg(false);
        options.addOption(keystore);
        options.addOption(pass);
        options.addOption(type);
        options.addOption(alias);
        options.addOption(cert);
        options.addOption(key);
        options.addOption(keyalg);
        options.addOption(new Option(OPT_LIST, "list keystore contents"));
        options.addOption(new Option(OPT_IMPORT, "import cert or cert/key pair"));
        options.addOption(new Option(OPT_EXPORT, "export cert or cert/key pair"));
    }

    /** {@inheritDoc} */
    protected void dispatch(final CommandLine line) throws Exception {
        if (line.hasOption(OPT_LIST)) {
            list(line);
        } else if (line.hasOption(OPT_IMPORT)) {
            doImport(line);
        } else if (line.hasOption(OPT_EXPORT)) {
            doExport(line);
        } else {
            printHelp();
        }
    }

    /**
   * Lists keystore contents on STDOUT. Output is similar to keytool -list -v.
   *
   * @param  line  Parsed command line arguments container.
   *
   * @throws  Exception  On errors.
   */
    protected void list(final CommandLine line) throws Exception {
        validateOptions(line);
        final KeyStore store = readKeyStore(line);
        final Enumeration<String> aliases = store.aliases();
        System.out.println("");
        while (aliases.hasMoreElements()) {
            final String alias = aliases.nextElement();
            System.out.println("Alias name: " + alias);
            System.out.println("Creation date: " + store.getCreationDate(alias));
            if (store.isKeyEntry(alias)) {
                System.out.println("Entry type: keyEntry");
                final Certificate[] chain = store.getCertificateChain(alias);
                System.out.println("Certificate chain length: " + chain.length);
                for (int i = 0; i < chain.length; i++) {
                    System.out.println("===== Certificate [" + i + "] =====");
                    printCertificate(chain[i]);
                }
            } else {
                System.out.println("Entry type: trustedCertEntry");
                System.out.println("Certificate details:");
                printCertificate(store.getCertificate(alias));
            }
            System.out.println("");
            System.out.println("");
        }
    }

    /**
   * Imports a certificate or key pair into the keystore.
   *
   * @param  line  Parsed command line arguments container.
   *
   * @throws  Exception  On errors.
   */
    protected void doImport(final CommandLine line) throws Exception {
        validateOptions(line);
        final KeyStore store = readKeyStore(line);
        final String alias = line.getOptionValue(OPT_ALIAS);
        final File certFile = new File(line.getOptionValue(OPT_CERT));
        if (line.hasOption(OPT_KEY)) {
            final File keyFile = new File(line.getOptionValue(OPT_KEY));
            final char[] passChars = line.getOptionValue(OPT_PASS).toCharArray();
            final PrivateKey key = CryptReader.readPrivateKey(keyFile);
            final Certificate[] chain = CryptReader.readCertificateChain(certFile);
            System.err.println("Read certificate chain of length " + chain.length + ":");
            for (int i = 0; i < chain.length; i++) {
                System.out.println("===== Certificate [" + i + "] =====");
                printCertificate(chain[i]);
            }
            store.setKeyEntry(alias, key, passChars, chain);
            System.err.println("Imported key entry " + alias);
        } else {
            final Certificate cert = CryptReader.readCertificate(certFile);
            System.err.println("Read certificate:");
            printCertificate(cert);
            store.setCertificateEntry(alias, cert);
            System.err.println("Imported trusted cert entry " + alias);
        }
        final OutputStream os = new FileOutputStream(new File(line.getOptionValue(OPT_STORE)));
        try {
            store.store(os, line.getOptionValue(OPT_PASS).toCharArray());
        } finally {
            os.close();
        }
    }

    /**
   * Exports a certificate or key pair from the keystore.
   *
   * @param  line  Parsed command line arguments container.
   *
   * @throws  Exception  On errors.
   */
    protected void doExport(final CommandLine line) throws Exception {
        validateOptions(line);
        final KeyStore store = readKeyStore(line);
        final String alias = line.getOptionValue(OPT_ALIAS);
        boolean wroteData = false;
        if (line.hasOption(OPT_CERT)) {
            final File certFile = new File(line.getOptionValue(OPT_CERT));
            final Certificate[] certs = store.getCertificateChain(alias);
            if (certs != null) {
                if (certFile.getName().endsWith(PEM_SUFFIX)) {
                    CryptWriter.writePemCertificates(certs, certFile);
                } else {
                    CryptWriter.writeEncodedCertificates(certs, certFile);
                }
            } else {
                final Certificate cert = store.getCertificate(alias);
                if (certFile.getName().endsWith(PEM_SUFFIX)) {
                    CryptWriter.writePemCertificate(cert, certFile);
                } else {
                    CryptWriter.writeEncodedCertificate(cert, certFile);
                }
            }
            System.err.println("Wrote certificate to " + certFile);
            wroteData = true;
        }
        if (line.hasOption(OPT_KEY)) {
            final File keyFile = new File(line.getOptionValue(OPT_KEY));
            final PrivateKey key = (PrivateKey) store.getKey(alias, line.getOptionValue(OPT_PASS).toCharArray());
            if (keyFile.getName().endsWith(PEM_SUFFIX)) {
                CryptWriter.writePemKey(key, null, null, keyFile);
            } else {
                CryptWriter.writeEncodedKey(key, keyFile);
            }
            System.err.println("Wrote key to " + keyFile);
            wroteData = true;
        }
        if (!wroteData) {
            System.err.println("No data was written because neither -cert nor -key was specified.");
        }
    }

    /** {@inheritDoc} */
    protected String getCommandName() {
        return COMMAND_NAME;
    }

    /**
   * Reads a keystore from disk using command line arguments.
   *
   * @param  line  Parsed command line arguments container.
   *
   * @return  Initialized key store read from disk.
   *
   * @throws  Exception  On errors.
   */
    protected KeyStore readKeyStore(final CommandLine line) throws Exception {
        KeyStore store = null;
        if (line.hasOption(OPT_TYPE)) {
            store = CryptProvider.getKeyStore(line.getOptionValue(OPT_TYPE));
        } else {
            store = CryptProvider.getKeyStore();
        }
        final File storeFile = new File(line.getOptionValue(OPT_STORE));
        final char[] passChars = line.getOptionValue(OPT_PASS).toCharArray();
        if (storeFile.exists()) {
            final InputStream in = new FileInputStream(storeFile);
            try {
                store.load(in, passChars);
            } finally {
                in.close();
            }
        } else {
            if (line.hasOption(OPT_IMPORT)) {
                store.load(null, passChars);
            } else {
                throw new IllegalArgumentException("Keystore does not exist at " + storeFile + ". " + "An existing keystore is required for this operation.");
            }
        }
        return store;
    }

    /**
   * Prints a string representation of the given certificate to STDOUT. For an
   * X.509 certificate, prints important fields.
   *
   * @param  cert  Certificate to print.
   *
   * @throws  Exception  On print errors.
   */
    protected void printCertificate(final Certificate cert) throws Exception {
        if (cert instanceof X509Certificate) {
            final X509Certificate xCert = (X509Certificate) cert;
            final byte[] encodedCert = xCert.getEncoded();
            System.out.println("Subject: " + xCert.getSubjectDN());
            System.out.println("Issuer: " + xCert.getIssuerDN());
            System.out.println("Serial: " + hexConv.fromBytes(xCert.getSerialNumber().toByteArray()));
            System.out.println("Valid not before: " + xCert.getNotBefore());
            System.out.println("Valid not after: " + xCert.getNotAfter());
            System.out.println("MD5 fingerprint: " + md5.digest(encodedCert, hexConv));
            System.out.println("SHA1 fingerprint: " + sha1.digest(encodedCert, hexConv));
        } else {
            System.out.println(cert);
        }
    }

    /**
   * Validates the existence of required options for an operation.
   *
   * @param  line  Parsed command line arguments container.
   */
    protected void validateOptions(final CommandLine line) {
        if (!line.hasOption(OPT_STORE)) {
            throw new IllegalArgumentException("keystore option is required.");
        }
        if (!line.hasOption(OPT_PASS)) {
            throw new IllegalArgumentException("storepass option is required.");
        }
        if (line.hasOption(OPT_IMPORT) || line.hasOption(OPT_EXPORT)) {
            if (!line.hasOption(OPT_ALIAS)) {
                throw new IllegalArgumentException("alias option is required.");
            }
        }
        if (line.hasOption(OPT_IMPORT)) {
            if (!line.hasOption(OPT_CERT)) {
                throw new IllegalArgumentException("cert option is required.");
            }
        }
    }
}
