package com.estontorise.simplersa;

import java.security.Key;
import com.estontorise.simplersa.interfaces.RSAKey;

public class RSAKeyImpl implements RSAKey {

    private Key key;

    public RSAKeyImpl(Key key) {
        this.key = key;
    }

    public Key getKey() {
        return key;
    }
}
