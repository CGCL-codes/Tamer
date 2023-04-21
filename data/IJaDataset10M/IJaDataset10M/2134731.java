package com.zycus.dotproject.util;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import com.zycus.dotproject.exception.GenericException;

public final class PasswordEncryptor {

    private PasswordEncryptor() {
        throw new IllegalAccessError("this class should not be initialised");
    }

    @Override
    protected Object clone() throws CloneNotSupportedException {
        throw new CloneNotSupportedException("This class should not be cloned");
    }

    public static String getEncryptedPassword(String strPasswordToBeEncrypted) {
        System.out.println(strPasswordToBeEncrypted);
        byte[] defaultBytes = strPasswordToBeEncrypted.getBytes();
        try {
            MessageDigest algorithm = MessageDigest.getInstance("MD5");
            algorithm.reset();
            algorithm.update(defaultBytes);
            byte messageDigest[] = algorithm.digest();
            StringBuffer hexString = new StringBuffer();
            for (int iCounter = 0; iCounter < messageDigest.length; iCounter++) {
                hexString.append(Integer.toHexString(0xFF & messageDigest[iCounter]));
            }
            System.out.println(hexString.toString());
            return hexString.toString();
        } catch (NoSuchAlgorithmException nsae) {
            throw new GenericException(nsae);
        }
    }
}
