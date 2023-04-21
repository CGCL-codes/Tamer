package com.aelitis.azureus.core.crypto;

import com.aelitis.azureus.core.security.*;

public class VuzeCryptoManager {

    private static VuzeCryptoManager singleton;

    public static synchronized VuzeCryptoManager getSingleton() {
        if (singleton == null) {
            singleton = new VuzeCryptoManager();
        }
        return (singleton);
    }

    private CryptoManager crypt_man;

    protected VuzeCryptoManager() {
        crypt_man = CryptoManagerFactory.getSingleton();
    }

    public byte[] getPlatformAZID() {
        return (crypt_man.getSecureID());
    }
}
