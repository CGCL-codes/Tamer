package com.wuala.loader2.crypto;

import java.nio.BufferOverflowException;
import java.nio.ByteBuffer;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.ShortBufferException;
import javax.crypto.spec.SecretKeySpec;

public class SymmetricCrypto {

    public static final String ALGORITHM_NO_PADDING = "AES/ECB/NoPadding";

    public static final String ALGORITHM = "AES";

    private Cipher cipher;

    private Cipher noPaddingCipher;

    private static SymmetricCrypto instance = new SymmetricCrypto();

    private SymmetricCrypto() {
    }

    public static SymmetricCrypto getInstance() {
        return instance;
    }

    public void ensureAvailable(boolean padding) {
        getCipher(true);
    }

    private Cipher getCipher(boolean padding) {
        try {
            if (padding) {
                if (cipher == null) {
                    cipher = Cipher.getInstance(ALGORITHM);
                }
                return cipher;
            } else {
                if (noPaddingCipher == null) {
                    noPaddingCipher = Cipher.getInstance(ALGORITHM_NO_PADDING);
                }
                return noPaddingCipher;
            }
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        } catch (NoSuchPaddingException e) {
            throw new RuntimeException(e);
        }
    }

    ByteBuffer encrypt(ByteBuffer source, Key key) {
        return runCipher(Cipher.ENCRYPT_MODE, source, key, true);
    }

    ByteBuffer decrypt(ByteBuffer source, Key key) {
        return runCipher(Cipher.DECRYPT_MODE, source, key, true);
    }

    ByteBuffer encryptUnpadded(ByteBuffer source, Key key) {
        return runCipher(Cipher.ENCRYPT_MODE, source, key, false);
    }

    ByteBuffer decryptUnpadded(ByteBuffer source, Key key) {
        return runCipher(Cipher.DECRYPT_MODE, source, key, false);
    }

    void encryptUnpadded(ByteBuffer source, ByteBuffer target, Key key) {
        runCipher(Cipher.ENCRYPT_MODE, source, target, key, false);
    }

    void decryptUnpadded(ByteBuffer source, ByteBuffer target, Key key) {
        runCipher(Cipher.DECRYPT_MODE, source, target, key, false);
    }

    void encrypt(ByteBuffer source, ByteBuffer target, Key key) {
        runCipher(Cipher.ENCRYPT_MODE, source, target, key, true);
    }

    void decrypt(ByteBuffer source, ByteBuffer target, Key key) {
        runCipher(Cipher.DECRYPT_MODE, source, target, key, true);
    }

    private synchronized void runCipher(int mode, ByteBuffer source, ByteBuffer target, Key key, boolean padding) {
        try {
            Cipher cipher = getCipher(padding);
            cipher.init(mode, key);
            cipher.doFinal(source, target);
        } catch (InvalidKeyException e) {
            throw new RuntimeException(e);
        } catch (IllegalBlockSizeException e) {
            throw new RuntimeException(e);
        } catch (BadPaddingException e) {
            throw new RuntimeException(e);
        } catch (ShortBufferException e) {
            throw new BufferOverflowException();
        }
    }

    private synchronized ByteBuffer runCipher(int mode, ByteBuffer source, Key key, boolean padding) {
        try {
            Cipher cipher = getCipher(padding);
            cipher.init(mode, key);
            int outSize = cipher.getOutputSize(source.remaining());
            ByteBuffer target = ByteBuffer.allocate(outSize);
            cipher.doFinal(source, target);
            target.flip();
            return target;
        } catch (InvalidKeyException e) {
            throw new RuntimeException(e);
        } catch (IllegalBlockSizeException e) {
            throw new RuntimeException(e);
        } catch (BadPaddingException e) {
            throw new RuntimeException(e);
        } catch (ShortBufferException e) {
            throw new RuntimeException(e);
        }
    }

    public static long predictEncryptedSize(long actualSize) {
        actualSize += 16;
        actualSize -= actualSize % 16;
        return actualSize;
    }
}
