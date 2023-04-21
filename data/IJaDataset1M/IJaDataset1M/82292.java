package de.uni_siegen.hiek11;

import java.math.BigInteger;
import java.security.SecureRandom;

/**
 * This class is a java-implementation of: [Essl97] Esslinger, Bernhard et al.:
 * Esslinger, Bernhard (Eds.): Das CrypTool-Skript Kryptographie, Mathematik und
 * mehr. (10th edition), www.cryptool.org, Frankfurt am Main 2010.
 * 
 * This class represents a RSA-object able to encrypt and decrypt messages if
 * setup correctly. See hiek11.test.TEST_RSA_JCT for how to use it.
 * 
 * Note: If message M is larger than n = p*q, this class takes a very simple
 * approach to truncation: Every n-th part of M is truncated recusively until
 * the remainder of M is less than n.
 * 
 * @author hhiekmann
 * 
 */
public class RSA_JCT extends RSA42 {

    private BigInteger phiN = null;

    public RSA_JCT() {
        this.setK(RSA42.theK);
        this.setSecureRandom(new SecureRandom());
    }

    public RSA_JCT(int inK) {
        this.setK(inK);
        this.setSecureRandom(new SecureRandom());
    }

    public RSA_JCT(int inK, SecureRandom inSecureRandom) {
        this.setK(inK);
        this.setSecureRandom(inSecureRandom);
    }

    public RSA_JCT(SecureRandom inSecureRandom) {
        this.setK(RSA42.theK);
        this.setSecureRandom(new SecureRandom());
    }

    public BigInteger calcD() {
        return this.setD(this.getE().modInverse(this.getPhiN()));
    }

    private void calcPhiN() {
        this.setPhiN((this.getP().subtract(BigInteger.ONE).multiply(this.getQ().subtract(BigInteger.ONE))));
    }

    public BigInteger chooseE() {
        this.calcPhiN();
        BigInteger i = this.getN();
        for (i = this.getN().subtract(BigInteger.ONE); (i.compareTo(BigInteger.valueOf(2)) > 0); i = i.subtract(BigInteger.ONE)) {
            if ((this.gcdEqual1(this.getPhiN(), i))) {
                break;
            }
        }
        return this.setE(i);
    }

    public BigInteger findPrimeP() {
        do {
            this.setP(this.getPrime());
        } while (this.getP() == null);
        return this.getP();
    }

    public BigInteger findPrimeQ() {
        do {
            this.setQ(this.getPrime());
        } while (this.getQ() == null);
        return this.getQ();
    }

    private BigInteger getPhiN() {
        return this.phiN;
    }

    private BigInteger setPhiN(BigInteger inPhiN) {
        this.phiN = inPhiN;
        return this.getPhiN();
    }
}
