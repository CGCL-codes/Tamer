package model;

/**
 * 签名keyStore
 * @author 13leaf
 *
 */
public class KeyStore {

    public String keyPath;

    public String keyPass;

    public String storePass;

    public String keyAlias;

    public static KeyStore getTestKeyStore() {
        KeyStore keyStore = new KeyStore();
        keyStore.keyPath = "D:\\Backup\\我的文档\\ifeng_work\\keystore.keystore";
        keyStore.keyPass = "ifeng123";
        keyStore.storePass = "ifeng123";
        keyStore.keyAlias = "ifeng";
        return keyStore;
    }

    @Override
    public String toString() {
        return "KeyStore [path=" + keyPath + ", keyPass=" + keyPass + ", storePass=" + storePass + ", alias=" + keyAlias + "]";
    }
}
