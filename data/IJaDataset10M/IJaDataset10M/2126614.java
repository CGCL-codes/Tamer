package com.nitbcn.lib.utils;

import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;

public class KeyGen {

    public KeyGen() {
    }

    public int getKey() {
        SecretKey key1;
        KeyGenerator k;
        int key = 0;
        try {
            k = KeyGenerator.getInstance("AES");
            key1 = k.generateKey();
            key = key1.hashCode();
            if (key < 0) key = key * (-1);
            return key;
        } catch (Exception e) {
            return 0;
        }
    }
}
