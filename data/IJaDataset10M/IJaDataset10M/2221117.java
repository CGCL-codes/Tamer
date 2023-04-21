package br.net.woodstock.rockframework.security.crypt.impl;

import java.security.GeneralSecurityException;
import java.security.Key;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.SecureRandom;
import br.net.woodstock.rockframework.security.crypt.CrypterException;
import br.net.woodstock.rockframework.security.crypt.KeyPairSupport;
import br.net.woodstock.rockframework.security.crypt.KeyPairType;
import br.net.woodstock.rockframework.security.crypt.impl.CrypterOperation.Mode;
import br.net.woodstock.rockframework.util.Assert;
import br.net.woodstock.rockframework.utils.ConditionUtils;

public class AsyncCrypter extends AbstractCrypter implements KeyPairSupport {

    private static final int DEFAULT_KEY_SIZE = 1024;

    private KeyPair keyPair;

    private KeyPairType keyPairType;

    public AsyncCrypter(final KeyPair keyPair) {
        super();
        Assert.notNull(keyPair, "keyPair");
        this.keyPair = keyPair;
        for (KeyPairType keyPairType : KeyPairType.values()) {
            Key key = null;
            if (this.keyPair.getPrivate() != null) {
                key = this.keyPair.getPrivate();
            } else if (this.keyPair.getPublic() != null) {
                key = this.keyPair.getPublic();
            }
            if (key != null) {
                if (keyPairType.getAlgorithm().equals(key.getAlgorithm())) {
                    this.keyPairType = keyPairType;
                    break;
                }
            }
        }
    }

    public AsyncCrypter(final KeyPairType type) {
        this(type, null);
    }

    public AsyncCrypter(final KeyPairType type, final String seed) {
        super();
        Assert.notNull(type, "type");
        try {
            KeyPairGenerator generator = KeyPairGenerator.getInstance(type.getAlgorithm());
            if (ConditionUtils.isNotEmpty(seed)) {
                SecureRandom random = new SecureRandom(seed.getBytes());
                generator.initialize(AsyncCrypter.DEFAULT_KEY_SIZE, random);
            }
            this.keyPairType = type;
            this.keyPair = generator.generateKeyPair();
        } catch (GeneralSecurityException e) {
            throw new CrypterException(e);
        }
    }

    @Override
    public byte[] encrypt(final byte[] data) {
        return this.encrypt(data, null);
    }

    @Override
    public byte[] encrypt(final byte[] data, final String seed) {
        PrivateKey privateKey = this.keyPair.getPrivate();
        if (privateKey == null) {
            throw new IllegalStateException("Private key is null");
        }
        try {
            Assert.notNull(data, "data");
            CrypterOperation operation = new CrypterOperation(privateKey, Mode.ENCRYPT, data, seed);
            return operation.execute();
        } catch (Exception e) {
            throw new CrypterException(e);
        }
    }

    @Override
    public byte[] decrypt(final byte[] data) {
        return this.decrypt(data, null);
    }

    @Override
    public byte[] decrypt(final byte[] data, final String seed) {
        PublicKey publicKey = this.keyPair.getPublic();
        if (publicKey == null) {
            throw new IllegalStateException("Public key is null");
        }
        try {
            Assert.notNull(data, "data");
            CrypterOperation operation = new CrypterOperation(publicKey, Mode.DECRYPT, data, seed);
            return operation.execute();
        } catch (Exception e) {
            throw new CrypterException(e);
        }
    }

    public String getAlgorithm() {
        if (this.keyPairType == null) {
            return null;
        }
        return this.keyPairType.getAlgorithm();
    }

    @Override
    public PrivateKey getPrivateKey() {
        return this.keyPair.getPrivate();
    }

    @Override
    public PublicKey getPublicKey() {
        return this.keyPair.getPublic();
    }
}
