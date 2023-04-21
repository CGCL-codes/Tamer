package enigma.crypto.rsa;

import java.math.BigInteger;

public final class RSAPublicKeyImpl implements RSAPublicKey {

    private static final BigInteger X509_PUBLIC_EXPONENT = BigInteger.valueOf(65537);

    private BigInteger n;

    public RSAPublicKeyImpl(BigInteger n) {
        this.n = n;
    }

    public BigInteger rsaep(BigInteger data) {
        return data.modPow(X509_PUBLIC_EXPONENT, n);
    }

    public BigInteger getModulus() {
        return n;
    }

    public BigInteger getPublicExponent() {
        return X509_PUBLIC_EXPONENT;
    }

    public byte[] getEncoded() {
        return new CompactKeySpec(this).getEncoded();
    }

    public String getAlgorithm() {
        return "RSA";
    }

    public String getFormat() {
        return "Compact";
    }
}
