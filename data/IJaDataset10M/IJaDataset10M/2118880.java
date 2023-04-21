package de.uni_siegen.hiek11.test;

import static org.junit.Assert.assertEquals;
import java.math.BigInteger;
import java.security.SecureRandom;
import org.junit.Test;
import de.uni_siegen.hiek11.RSA42;
import de.uni_siegen.hiek11.RSA_JCT;
import de.uni_siegen.hiek11.RSA_JuGu02;

public class Test_RSA42 {

    @Test
    public void test_JuGu02_JCT() {
        int k = 1024;
        SecureRandom aSecureRandom = new SecureRandom();
        BigInteger e = BigInteger.probablePrime(k, aSecureRandom);
        int t = Integer.MAX_VALUE / 2;
        RSA42 aAlice = new RSA_JuGu02(k, e, t, aSecureRandom);
        aAlice.findPrimeP();
        aAlice.findPrimeQ();
        aAlice.calcD();
        aAlice.calcN();
        RSA42 aBob = new RSA_JCT(k, aSecureRandom);
        aBob.findPrimeP();
        aBob.findPrimeQ();
        aBob.calcN();
        aBob.chooseE();
        aBob.calcD();
        BigInteger message = aAlice.getPrime();
        BigInteger cipher = aAlice.encrypt(message, aBob.getPublicKey());
        assertEquals(message, aBob.decrypt(cipher));
        message = aBob.getPrime();
        cipher = aBob.encrypt(message, aAlice.getPublicKey());
        assertEquals(message, aAlice.decrypt(cipher));
    }
}
