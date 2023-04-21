package org.itsocial.framework.security;

import org.apache.shiro.crypto.hash.Sha256Hash;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

public class PasswordEncryption {

    private String secretSalt;

    public PasswordEncryption(String value) {
        secretSalt = value;
    }

    public String getEncryptedPassword(String password) {
        Sha256Hash sha256Hash = new Sha256Hash(password, secretSalt);
        return sha256Hash.toHex();
    }
}
