package codec.pkcs.bin;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.security.AlgorithmParameterGenerator;
import java.security.AlgorithmParameters;
import java.security.GeneralSecurityException;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.cert.X509Certificate;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import codec.pkcs7.EnvelopedData;
import codec.util.JCA;
import codec.x501.BadNameException;
import de.fhg.igd.util.ArgsParser;
import de.fhg.igd.util.ArgsParserException;

/**
 * Demonstrates encryption and decryption based on PKCS#7.
 * Encryption requires the following parameters on top of
 * those defined in {@link AbstractTool AbstractTool}:
 *
 * @author Volker Roth
 * @version "$Id: Crypt.java 1913 2007-08-08 02:41:53Z jpeters $"
 *
 * @see EnvelopedData
 */
public class Crypt extends AbstractTool {

    /**
     * The options descriptor.
     */
    public static final String DESCR = "help:!,alias:s,keypass:s,recipients:s[,p7m:F," + "size:n,alg:s,in:F,out:F,encrypt:!,decrypt:!";

    /**
     * The {@link EnvelopedData EnvelopedData} structure.
     */
    protected EnvelopedData p7m_;

    /**
     * Dirty flag, signals that the EnvelopedData must be
     * saved.
     */
    protected boolean dirty_;

    protected void unlock(EnvelopedData ed) throws GeneralSecurityException, IOException, ArgsParserException {
        X509Certificate cert;
        PrivateKey key;
        String passwd;
        String alias;
        alias = p_.stringValue("alias");
        passwd = p_.stringValue("keypass");
        cert = getCertificate(alias);
        key = getPrivateKey(alias, passwd);
        p7m_.init(cert, key);
    }

    protected EnvelopedData create() throws GeneralSecurityException, ArgsParserException {
        AlgorithmParameterGenerator pgen;
        AlgorithmParameters params;
        EnvelopedData ed;
        KeyGenerator kgen;
        SecretKey key;
        String alg;
        String alt;
        String oid;
        int size;
        int n;
        alg = p_.stringValue("alg");
        size = p_.intValue("size");
        oid = JCA.getOID(alg);
        if (oid == null) {
            throw new NoSuchAlgorithmException("No OID mapping for " + alg + " available!");
        }
        n = alg.indexOf('/');
        alt = (n > 0) ? alg.substring(0, n) : alg;
        try {
            pgen = AlgorithmParameterGenerator.getInstance(alt);
            pgen.init(size);
            params = pgen.generateParameters();
        } catch (NoSuchAlgorithmException e) {
            System.out.println("Warning, no parameter generator for " + alg);
            params = null;
        }
        kgen = KeyGenerator.getInstance(alt);
        kgen.init(size);
        key = kgen.generateKey();
        return new EnvelopedData(key, alg, params);
    }

    protected void addRecipients(EnvelopedData ed) throws GeneralSecurityException, IOException, BadNameException, ArgsParserException {
        X509Certificate cert;
        String[] r;
        int n;
        r = (String[]) p_.values("recipients");
        for (n = 0; n < r.length; n++) {
            cert = getCertificate(r[n]);
            if (!ed.hasRecipient(cert)) {
                dirty_ = true;
                ed.addRecipient(cert);
            }
        }
    }

    public void run(String[] argv) throws Exception {
        FileOutputStream out;
        FileInputStream in;
        File file;
        p_ = new ArgsParser(AbstractTool.DESCR + "," + DESCR);
        p_.parse(argv);
        if (p_.isDefined("help")) {
            p_.help(System.out);
            return;
        }
        file = p_.fileValue("p7m");
        if (file.exists()) {
            try {
                p7m_ = (EnvelopedData) loadContentInfo("p7m");
                dirty_ = false;
            } catch (ClassCastException e) {
                System.err.println("No EnvelopedData in file " + file.getName());
                return;
            }
            if (p_.isDefined("alias")) {
                unlock(p7m_);
            }
            if (p_.isDefined("alg")) {
                System.out.println("Ignoring options: alg, size");
            }
        } else {
            p7m_ = create();
            dirty_ = true;
        }
        if (p_.isDefined("recipients")) {
            addRecipients(p7m_);
        }
        if (p_.isDefined("encrypt")) {
            in = new FileInputStream(p_.fileValue("in"));
            out = new FileOutputStream(p_.fileValue("out"));
            p7m_.encryptBulkData(in, out);
        }
        if (p_.isDefined("decrypt")) {
            in = new FileInputStream(p_.fileValue("in"));
            out = new FileOutputStream(p_.fileValue("out"));
            p7m_.decryptBulkData(in, out);
        }
        if (dirty_) {
            saveContentInfo(p7m_, "p7m");
        }
    }

    public static void main(String[] argv) {
        try {
            ((Crypt) new Crypt()).run(argv);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
