package enigma.crypto.rsa;

import java.math.BigInteger;
import enigma.crypto.random.RC4PrngCipher;
import enigma.util.Bytes;

public class RSA {

    private static RC4PrngCipher random = RC4PrngCipher.getInstance();

    public static BigInteger rsaep(RSAPublicKey key, BigInteger data) {
        return data.modPow(key.getPublicExponent(), key.getModulus());
    }

    public static BigInteger rsadp(RSAPrivateCrtKey key, BigInteger data) {
        BigInteger p = key.getPrimeP();
        BigInteger q = key.getPrimeQ();
        BigInteger dP = key.getPrimeExponentP();
        BigInteger dQ = key.getPrimeExponentQ();
        BigInteger qInv = key.getCrtCoefficient();
        BigInteger m1 = data.modPow(dP, p);
        BigInteger m2 = data.modPow(dQ, q);
        BigInteger h = m1.subtract(m2).multiply(qInv).mod(p);
        BigInteger m = q.multiply(h).add(m2);
        return m;
    }

    public static byte[] paddingSign(byte[] data, int blockSize) {
        byte[] padding = new byte[blockSize];
        padding[0] = 0;
        padding[1] = 1;
        for (int i = 2; i < blockSize - data.length - 1; i++) {
            padding[i] = (byte) 0xff;
        }
        padding[blockSize - data.length - 1] = 0;
        System.arraycopy(data, 0, padding, blockSize - data.length, data.length);
        return padding;
    }

    public static byte[] paddingEncrypt(byte[] data, int blockSize) {
        byte[] padding = new byte[blockSize];
        padding[0] = 0;
        padding[1] = 2;
        random.output(padding, 2, blockSize - data.length - 3);
        for (int i = 2; i < blockSize - data.length - 1; i++) {
            if (padding[i] == 0) {
                padding[i] = (byte) (Bytes.toInt(random.output(4)) % 255);
            }
        }
        padding[blockSize - data.length - 1] = 0;
        System.arraycopy(data, 0, padding, blockSize - data.length, data.length);
        return padding;
    }

    public static byte[] depaddingDecrypt(byte[] padding) {
        int dataLen = 0;
        for (int i = 2; i < padding.length; i++) {
            if (padding[i] == 0) {
                dataLen = padding.length - i - 1;
                break;
            }
        }
        byte[] data = new byte[dataLen];
        System.arraycopy(padding, padding.length - dataLen, data, 0, dataLen);
        return data;
    }

    public static byte[] depaddingVerify(byte[] padding) {
        if (padding[0] == 0 && padding[1] == 1) {
            int dataLen = 0;
            for (int i = 2; i < padding.length; i++) {
                if (padding[i] == 0) {
                    dataLen = padding.length - i - 1;
                    break;
                } else if (padding[i] != -1) {
                    return Bytes.EMPTY_BYTES;
                }
            }
            byte[] data = new byte[dataLen];
            System.arraycopy(padding, padding.length - dataLen, data, 0, dataLen);
            return data;
        }
        return Bytes.EMPTY_BYTES;
    }

    public static int getBlockSize(RSAKey key) {
        return (key.getModulus().bitLength() + 7) / 8;
    }

    public static byte[] rsaep(RSAPublicKey key, byte[] data) {
        return Bytes.fromBigInteger(rsaep(key, new BigInteger(1, data)), getBlockSize(key));
    }

    public static byte[] rsadp(RSAPrivateCrtKey key, byte[] data) {
        return Bytes.fromBigInteger(rsadp(key, new BigInteger(1, data)), getBlockSize(key));
    }

    public static byte[] encrypt(RSAPublicKey key, byte[] data) {
        return rsaep(key, paddingEncrypt(data, getBlockSize(key)));
    }

    public static byte[] decrypt(RSAPrivateCrtKey key, byte[] data) {
        return depaddingDecrypt(rsadp(key, data));
    }

    public static byte[] sign(RSAPrivateCrtKey key, byte[] data) {
        return rsadp(key, paddingSign(data, getBlockSize(key)));
    }

    public static byte[] verify(RSAPublicKey key, byte[] data) {
        return depaddingVerify(rsaep(key, data));
    }
}
