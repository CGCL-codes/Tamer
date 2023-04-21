package br.net.woodstock.rockframework.security.store.impl;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.security.GeneralSecurityException;
import java.security.KeyStore;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.cert.Certificate;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import javax.crypto.SecretKey;
import br.net.woodstock.rockframework.security.Alias;
import br.net.woodstock.rockframework.security.store.CertificateEntry;
import br.net.woodstock.rockframework.security.store.KeyStoreType;
import br.net.woodstock.rockframework.security.store.PasswordAlias;
import br.net.woodstock.rockframework.security.store.PrivateKeyEntry;
import br.net.woodstock.rockframework.security.store.PublicKeyEntry;
import br.net.woodstock.rockframework.security.store.SecretKeyEntry;
import br.net.woodstock.rockframework.security.store.Store;
import br.net.woodstock.rockframework.security.store.StoreEntry;
import br.net.woodstock.rockframework.security.store.StoreEntryType;
import br.net.woodstock.rockframework.security.store.StoreException;
import br.net.woodstock.rockframework.util.Assert;
import br.net.woodstock.rockframework.utils.CollectionUtils;
import br.net.woodstock.rockframework.utils.ConditionUtils;

public class JCAStore implements Store {

    private KeyStore keyStore;

    public JCAStore(final KeyStore keyStore) {
        super();
        this.keyStore = keyStore;
    }

    public JCAStore(final KeyStoreType keyStoreType) {
        super();
        try {
            this.keyStore = KeyStore.getInstance(keyStoreType.getType());
            this.keyStore.load(null, null);
        } catch (Exception e) {
            throw new StoreException(e);
        }
    }

    @Override
    public Alias[] aliases() {
        try {
            Enumeration<String> aliases = this.keyStore.aliases();
            List<Alias> list = new ArrayList<Alias>();
            while (aliases.hasMoreElements()) {
                String alias = aliases.nextElement();
                list.add(new Alias(alias));
            }
            return CollectionUtils.toArray(list, Alias.class);
        } catch (GeneralSecurityException e) {
            throw new StoreException(e);
        }
    }

    @Override
    public StoreEntry get(final Alias alias, final StoreEntryType type) {
        try {
            Assert.notNull(alias, "alias");
            Assert.notNull(type, "type");
            String name = alias.getName();
            String password = null;
            StoreEntry entry = null;
            if (alias instanceof PasswordAlias) {
                PasswordAlias pa = (PasswordAlias) alias;
                password = pa.getPassword();
            }
            switch(type) {
                case CERTIFICATE:
                    Certificate certificate = this.keyStore.getCertificate(alias.getName());
                    if (certificate != null) {
                        entry = new CertificateEntry(alias, certificate);
                    }
                    break;
                case PRIVATE_KEY:
                    PrivateKey privateKey = (PrivateKey) this.keyStore.getKey(name, this.toCharArray(password));
                    if (privateKey != null) {
                        Certificate[] chain = this.keyStore.getCertificateChain(name);
                        entry = new PrivateKeyEntry(alias, privateKey, chain);
                    }
                    break;
                case PUBLIC_KEY:
                    PublicKey publicKey = (PublicKey) this.keyStore.getKey(name, this.toCharArray(password));
                    if (publicKey != null) {
                        entry = new PublicKeyEntry(alias, publicKey);
                    }
                    break;
                case SECRET_KEY:
                    SecretKey secretKey = (SecretKey) this.keyStore.getKey(name, this.toCharArray(password));
                    if (secretKey != null) {
                        entry = new SecretKeyEntry(alias, secretKey);
                    }
                    break;
                default:
                    break;
            }
            return entry;
        } catch (GeneralSecurityException e) {
            throw new StoreException(e);
        }
    }

    @Override
    public boolean add(final StoreEntry entry) {
        try {
            Assert.notNull(entry, "entry");
            Assert.notNull(entry.getAlias(), "entry.alias");
            Assert.notNull(entry.getType(), "entry.type");
            Assert.notNull(entry.getValue(), "entry.value");
            Alias alias = entry.getAlias();
            String name = alias.getName();
            Object value = entry.getValue();
            String password = null;
            if (alias instanceof PasswordAlias) {
                PasswordAlias pa = (PasswordAlias) alias;
                password = pa.getPassword();
            }
            switch(entry.getType()) {
                case CERTIFICATE:
                    this.keyStore.setCertificateEntry(name, (Certificate) value);
                    break;
                case PRIVATE_KEY:
                    this.keyStore.setKeyEntry(name, (PrivateKey) value, this.toCharArray(password), ((PrivateKeyEntry) entry).getChain());
                    break;
                case PUBLIC_KEY:
                    this.keyStore.setKeyEntry(name, (PublicKey) value, this.toCharArray(password), null);
                    break;
                case SECRET_KEY:
                    this.keyStore.setKeyEntry(name, (SecretKey) value, this.toCharArray(password), null);
                    break;
                default:
                    break;
            }
            return true;
        } catch (GeneralSecurityException e) {
            throw new StoreException(e);
        }
    }

    @Override
    public boolean remove(final Alias alias) {
        try {
            Assert.notNull(alias, "alias");
            this.keyStore.deleteEntry(alias.getName());
            return true;
        } catch (GeneralSecurityException e) {
            throw new StoreException(e);
        }
    }

    @Override
    public void read(final InputStream inputStream, final String password) throws IOException {
        try {
            this.keyStore.load(inputStream, this.toCharArray(password));
        } catch (GeneralSecurityException e) {
            throw new StoreException(e);
        }
    }

    @Override
    public void write(final OutputStream outputStream, final String password) throws IOException {
        try {
            this.keyStore.store(outputStream, this.toCharArray(password));
        } catch (GeneralSecurityException e) {
            throw new StoreException(e);
        }
    }

    private char[] toCharArray(final String s) {
        if (ConditionUtils.isNotEmpty(s)) {
            return s.toCharArray();
        }
        return null;
    }

    @Override
    public KeyStore toKeyStore() {
        return this.keyStore;
    }
}
