package com.kongur.network.erp.common.crypto.impl;

import java.io.UnsupportedEncodingException;
import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import org.apache.commons.codec.binary.Base64;
import com.kongur.network.erp.common.crypto.Crypto;

/**
 * 
 * @author fish
 * 
 */
public abstract class AbstractCryptoImpl implements Crypto {

    private static final Encoding DefaultEncoding = Encoding.Base32;

    private static final String DefaultCharset = "UTF-8";

    private static final String Base64StringCharset = "ISO-8859-1";

    private class CryptoCipher {

        private Cipher encryptCipher;

        private Cipher decryptCipher;

        private CryptoCipher() {
            super();
            this.encryptCipher = getEncryptCipher();
            this.decryptCipher = getDecryptCipher();
        }

        public byte[] encrypt(byte[] bs) {
            try {
                return this.encryptCipher.doFinal(bs);
            } catch (IllegalBlockSizeException e) {
                throw new RuntimeException(e);
            } catch (BadPaddingException e) {
                throw new RuntimeException(e);
            }
        }

        public byte[] dectypt(byte[] bs) {
            try {
                return this.decryptCipher.doFinal(bs);
            } catch (IllegalBlockSizeException e) {
                throw new RuntimeException(e);
            } catch (BadPaddingException e) {
                throw new RuntimeException(e);
            }
        }
    }

    private ThreadLocal<CryptoCipher> local = new ThreadLocal<CryptoCipher>();

    protected CryptoCipher getLocalCipher() {
        CryptoCipher cc = local.get();
        if (cc == null) {
            cc = new CryptoCipher();
            local.set(cc);
        }
        return cc;
    }

    protected AbstractCryptoImpl() {
        super();
    }

    protected abstract Cipher getEncryptCipher();

    protected abstract Cipher getDecryptCipher();

    public String dectypt(String s) {
        return dectypt(s, DefaultEncoding, DefaultCharset);
    }

    public String dectypt(String s, Encoding en) {
        return dectypt(s, en, DefaultCharset);
    }

    public String dectypt(String s, String charset) {
        return dectypt(s, DefaultEncoding, charset);
    }

    public String dectypt(String s, Encoding en, String charset) {
        if (s == null) {
            throw new NullPointerException("dectypt string can't be null");
        }
        if (en == null) {
            throw new NullPointerException("dectypt Encoding can't be null");
        }
        if (charset == null) {
            throw new NullPointerException("dectypt charset can't be null");
        }
        try {
            byte[] bs = en == Encoding.Base32 ? com.kongur.network.erp.util.Base32.decode(s) : Base64.decodeBase64(s.getBytes(Base64StringCharset));
            bs = this.dectypt(bs);
            return new String(bs, charset);
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
    }

    public String encrypt(String s) {
        return encrypt(s, DefaultEncoding, DefaultCharset);
    }

    public String encrypt(String s, Encoding en) {
        return encrypt(s, en, DefaultCharset);
    }

    public String encrypt(String s, String charset) {
        return encrypt(s, DefaultEncoding, charset);
    }

    public String encrypt(String s, Encoding en, String charset) {
        if (s == null) {
            throw new NullPointerException("encrypt string can't be null");
        }
        if (en == null) {
            throw new NullPointerException("encrypt Encoding can't be null");
        }
        if (charset == null) {
            throw new NullPointerException("encrypt charset can't be null");
        }
        try {
            byte[] bs = s.getBytes(charset);
            bs = this.encrypt(bs);
            return en == Encoding.Base32 ? com.kongur.network.erp.util.Base32.encode(bs) : new String(Base64.encodeBase64(bs), Base64StringCharset);
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
    }

    public byte[] dectypt(byte[] bytes) {
        if (bytes == null) {
            throw new NullPointerException("dectypt bytes can't be null");
        }
        return this.getLocalCipher().dectypt(bytes);
    }

    public byte[] encrypt(byte[] bytes) {
        if (bytes == null) {
            throw new NullPointerException("encrypt bytes can't be null");
        }
        return this.getLocalCipher().encrypt(bytes);
    }
}
