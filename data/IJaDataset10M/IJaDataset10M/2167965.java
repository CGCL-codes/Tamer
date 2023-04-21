package com.mindbright.jce.crypto.interfaces;

import java.math.BigInteger;
import com.mindbright.jca.security.PublicKey;

public interface DHPublicKey extends DHKey, PublicKey {

    public BigInteger getY();
}
