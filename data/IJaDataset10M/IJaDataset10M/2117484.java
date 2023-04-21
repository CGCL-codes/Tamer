package ch.ethz.dcg.spamato.filter.earlgrey.common.security;

import java.io.*;
import java.util.Properties;

/**
 * Comment for this Type.
 * 
 * @author simon
 */
public class RSAKeyStore {

    private static final String PRIV_KEY = "key.private";

    private static final String PUB_KEY = "key.public";

    private String privateKey = null;

    private String publicKey = null;

    public RSAKeyStore(String keyStoreFilePath) throws IOException {
        readFrom(keyStoreFilePath);
    }

    public RSAKeyStore(InputStream inStream) throws IOException {
        readFrom(inStream);
    }

    public RSAKeyStore(String privateKey, String publicKey) {
        setPrivateKey(privateKey);
        setPublicKey(publicKey);
    }

    public void writeTo(OutputStream outStream) throws IOException {
        Properties props = new Properties();
        props.setProperty(PRIV_KEY, getPrivateKey());
        props.setProperty(PUB_KEY, getPublicKey());
        props.store(outStream, "The keys for RSA communication.");
    }

    public void writeTo(String keyStoreFilePath) throws IOException {
        writeTo(new FileOutputStream(keyStoreFilePath));
    }

    private void readFrom(InputStream inStream) throws IOException {
        Properties props = new Properties();
        props.load(inStream);
        setPrivateKey(props.getProperty(PRIV_KEY));
        setPublicKey(props.getProperty(PUB_KEY));
    }

    private void readFrom(String keyStoreFilePath) throws IOException {
        readFrom(new FileInputStream(keyStoreFilePath));
    }

    /**
    * @return Returns the privateKey.
    */
    public String getPrivateKey() {
        return this.privateKey;
    }

    /**
    * @param privateKey The privateKey to set.
    */
    public void setPrivateKey(String privateKey) {
        this.privateKey = privateKey;
    }

    /**
    * @return Returns the publicKey.
    */
    public String getPublicKey() {
        return this.publicKey;
    }

    /**
    * @param publicKey The publicKey to set.
    */
    public void setPublicKey(String publicKey) {
        this.publicKey = publicKey;
    }
}
