package de.fhg.igd.util;

import java.io.IOException;
import java.io.InputStream;
import java.security.GeneralSecurityException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Arrays;
import java.util.Iterator;
import java.util.Map;
import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.PBEParameterSpec;
import codec.Base64;
import codec.CorruptedCodeException;
import codec.util.JCA;

/**
 * This class adds methods to the {@link Manifest Manifest}
 * class that allow to protect a manifest against tampering
 * based on a password.
 *
 * @author Volker Roth
 * @version "$Id: LockableManifest.java 1913 2007-08-08 02:41:53Z jpeters $"
 */
public class LockableManifest extends Manifest {

    /**
      * This method verifies the Manifest against the given
      * password. Only the Manifest itself is verified by this
      * method; the digests of the individual entries are not
      * verified. Use method
      * {@link ManifestVerifier#verify(Manifest) verify} of
      * class {@link Manifest Manifest} for this.
      *
      * @exception ManifestException if an error occurs during
      *   decoding of MAC information such as no headers found,
      *   algorithms not available and similar error conditions.
      * @exception GeneralSecurityException if the MAC could be
      *   verified but turned out to be false. This happens if
      *   the Manifest has been tampered with or a wrong password
      *   was given.
      * @exception CorruptedCodeException if the MAC header
      *   information is corrupted.
      */
    public void verify(char[] passwd) throws CorruptedCodeException, GeneralSecurityException, ManifestException, IOException {
        int n;
        int rounds;
        byte[] b;
        byte[] salt;
        String oid;
        Cipher cipher;
        String[] v;
        SecretKey key;
        SecretKeyFactory factory;
        v = new String[5];
        v[0] = attributes_.get("MAC-CipherAlg");
        v[1] = attributes_.get("MAC-DigestAlg");
        v[2] = attributes_.get("Content-MAC");
        v[3] = attributes_.get("MAC-Params");
        if (v[0] == null || v[1] == null || v[2] == null || v[3] == null) {
            throw new ManifestException("MAC information is incomplete!");
        }
        if (v[0].startsWith("OID.")) {
            v[0] = JCA.getName(v[0].substring(4));
        }
        if (v[1].startsWith("OID.")) {
            v[4] = JCA.getName(v[1].substring(4));
        } else {
            v[4] = v[1];
        }
        if (v[0] == null || v[4] == null) {
            throw new ManifestException("Cannot determine algorithms!");
        }
        n = v[3].indexOf(",");
        if (n < 0) {
            throw new ManifestException("Cannot recover parameters!");
        }
        salt = Base64.decode(v[3].substring(0, n));
        rounds = Integer.parseInt(v[3].substring(n + 1));
        factory = SecretKeyFactory.getInstance(v[0]);
        key = factory.generateSecret(new PBEKeySpec(passwd));
        cipher = Cipher.getInstance(v[0]);
        cipher.init(Cipher.DECRYPT_MODE, key, new PBEParameterSpec(salt, 64));
        b = cipher.doFinal(Base64.decode(v[2]));
        if (!Arrays.equals(b, digest(v[4], v[1]))) {
            throw new GeneralSecurityException("Bad MAC or wrong password!");
        }
        return;
    }

    /**
      * This method locks the Manifest by computing a digest
      * that is computed based on a password and stored in the
      * Manifest header attributes.<p>
      *
      * This method depends on the (non-)interoperability of the
      * installed Providers. For practical purposes, add the ABA
      * Provider before the JCSI Provider, or this method will
      * fail due to the peculiarities of those providers.
      *
      * @exception GeneralSecurityException if for some
      *   reason (algorithm unavailable, algorithm does not
      *   accept PBEKeySpec or PBEParameterSpec) the MAC cannot
      *   be computed.
      */
    public void lock(char[] passwd, String pbeAlg, String mdAlg) throws GeneralSecurityException {
        byte[] b;
        String oid;
        byte[] salt;
        Cipher cipher;
        SecretKey key;
        MessageDigest md;
        SecretKeyFactory factory;
        try {
            factory = SecretKeyFactory.getInstance(pbeAlg);
            key = factory.generateSecret(new PBEKeySpec(passwd));
            salt = generateSalt();
            cipher = Cipher.getInstance(pbeAlg);
            cipher.init(Cipher.ENCRYPT_MODE, key, new PBEParameterSpec(salt, 64));
            oid = JCA.getOID(mdAlg);
            if (oid == null) oid = mdAlg;
            attributes_.put("MAC-DigestAlg", oid);
            b = cipher.doFinal(digest(mdAlg, oid));
            attributes_.put("Content-MAC", Base64.encode(b));
            oid = JCA.getOID(pbeAlg);
            if (oid == null) oid = pbeAlg;
            attributes_.put("MAC-CipherAlg", oid);
            attributes_.put("MAC-Params", Base64.encode(salt) + ",64");
        } catch (Exception e) {
            throw new GeneralSecurityException("Cannot recoved MAC-Key");
        }
    }

    /**
      * This method computes a digest on the Manifest entries
      * with the given digest algorithm. Since the entries are
      * kept in a TreeMap, which is a SortedMap also, we digest
      * the entries according to the canonical order of the
      * names of the Manifest sections. After digesting each
      * section (by obtaining an input stream of it), we also
      * digest the lowest order bytes of the given digest
      * algorithm name passed in <code>alt_name</code> in order
      * to prevent digest algorithm substitution attacks on the
      * digest algorithm.
      *
      * @param digest_alg The digest algorithm name that is used
      *   to request the digest implementation.
      * @param alt_name The digest algorithm name that is included in
      *   the digest.
      */
    protected byte[] digest(String digest_alg, String alt_name) throws NoSuchAlgorithmException, IOException {
        int n;
        byte[] b;
        byte[] m;
        Iterator i;
        Map.Entry entry;
        Attributes attributes;
        InputStream in;
        MessageDigest md;
        m = new byte[alt_name.length()];
        for (n = 0; n < m.length; n++) {
            m[n] = (byte) alt_name.charAt(n);
        }
        b = new byte[0];
        md = MessageDigest.getInstance(digest_alg);
        for (i = entries_.entrySet().iterator(); i.hasNext(); ) {
            entry = (Map.Entry) i.next();
            attributes = (Attributes) entry.getValue();
            in = getInputStream((String) entry.getKey());
            n = in.available();
            if (n > b.length) {
                b = new byte[n];
            }
            n = in.read(b);
            md.update(b, 0, n);
            md.update(m);
        }
        return md.digest();
    }

    /**
      * This method computes 4 bytes of salt using the
      * &quot;SHA1PRNG&quot; pseudo-random number generator.
      * The PRNG is not seeded, hence we rely on the initial
      * seeding capability of the prefered local implementation.
      *
      * @return Four bytes of salt value.
      */
    protected byte[] generateSalt() throws NoSuchAlgorithmException {
        byte[] b;
        SecureRandom rnd;
        rnd = SecureRandom.getInstance("SHA1PRNG");
        b = new byte[8];
        rnd.nextBytes(b);
        return b;
    }
}
