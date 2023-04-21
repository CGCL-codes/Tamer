package org.biff.crypto.security;

import java.util.Hashtable;
import java.util.Enumeration;
import org.bouncycastle.asn1.DERObjectIdentifier;
import org.bouncycastle.asn1.pkcs.PKCSObjectIdentifiers;
import org.bouncycastle.asn1.oiw.OIWObjectIdentifiers;
import org.bouncycastle.asn1.nist.NISTObjectIdentifiers;
import org.bouncycastle.asn1.teletrust.TeleTrusTObjectIdentifiers;
import org.bouncycastle.asn1.cryptopro.CryptoProObjectIdentifiers;
import org.bouncycastle.crypto.Digest;
import org.bouncycastle.crypto.digests.GOST3411Digest;
import org.bouncycastle.crypto.digests.MD2Digest;
import org.bouncycastle.crypto.digests.MD4Digest;
import org.bouncycastle.crypto.digests.MD5Digest;
import org.bouncycastle.crypto.digests.RIPEMD128Digest;
import org.bouncycastle.crypto.digests.RIPEMD160Digest;
import org.bouncycastle.crypto.digests.RIPEMD256Digest;
import org.bouncycastle.crypto.digests.RIPEMD320Digest;
import org.bouncycastle.crypto.digests.SHA1Digest;
import org.bouncycastle.crypto.digests.SHA224Digest;
import org.bouncycastle.crypto.digests.SHA256Digest;
import org.bouncycastle.crypto.digests.SHA384Digest;
import org.bouncycastle.crypto.digests.SHA512Digest;
import org.bouncycastle.crypto.digests.TigerDigest;
import org.bouncycastle.crypto.digests.WhirlpoolDigest;

/**
 * 
 */
public final class DigestUtilities {

    private static Hashtable algorithms = new Hashtable();

    private static Hashtable oids = new Hashtable();

    static {
        algorithms.put(PKCSObjectIdentifiers.md2.getId(), "MD2");
        algorithms.put(PKCSObjectIdentifiers.md4.getId(), "MD4");
        algorithms.put(PKCSObjectIdentifiers.md5.getId(), "MD5");
        algorithms.put("SHA1", "SHA-1");
        algorithms.put(OIWObjectIdentifiers.idSHA1.getId(), "SHA-1");
        algorithms.put("SHA224", "SHA-224");
        algorithms.put(NISTObjectIdentifiers.id_sha224.getId(), "SHA-224");
        algorithms.put("SHA256", "SHA-256");
        algorithms.put(NISTObjectIdentifiers.id_sha256.getId(), "SHA-256");
        algorithms.put("SHA384", "SHA-384");
        algorithms.put(NISTObjectIdentifiers.id_sha384.getId(), "SHA-384");
        algorithms.put("SHA512", "SHA-512");
        algorithms.put(NISTObjectIdentifiers.id_sha512.getId(), "SHA-512");
        algorithms.put("RIPEMD-128", "RIPEMD128");
        algorithms.put(TeleTrusTObjectIdentifiers.ripemd128.getId(), "RIPEMD128");
        algorithms.put("RIPEMD-160", "RIPEMD160");
        algorithms.put(TeleTrusTObjectIdentifiers.ripemd160.getId(), "RIPEMD160");
        algorithms.put("RIPEMD-256", "RIPEMD256");
        algorithms.put(TeleTrusTObjectIdentifiers.ripemd256.getId(), "RIPEMD256");
        algorithms.put("RIPEMD-320", "RIPEMD320");
        algorithms.put(CryptoProObjectIdentifiers.gostR3411.getId(), "GOST3411");
        oids.put("MD2", PKCSObjectIdentifiers.md2);
        oids.put("MD4", PKCSObjectIdentifiers.md4);
        oids.put("MD5", PKCSObjectIdentifiers.md5);
        oids.put("SHA-1", OIWObjectIdentifiers.idSHA1);
        oids.put("SHA-224", NISTObjectIdentifiers.id_sha224);
        oids.put("SHA-256", NISTObjectIdentifiers.id_sha256);
        oids.put("SHA-384", NISTObjectIdentifiers.id_sha384);
        oids.put("SHA-512", NISTObjectIdentifiers.id_sha512);
        oids.put("RIPEMD128", TeleTrusTObjectIdentifiers.ripemd128);
        oids.put("RIPEMD160", TeleTrusTObjectIdentifiers.ripemd160);
        oids.put("RIPEMD256", TeleTrusTObjectIdentifiers.ripemd256);
        oids.put("GOST3411", CryptoProObjectIdentifiers.gostR3411);
    }

    public static DERObjectIdentifier getObjectIdentifier(String mechanism) {
        mechanism = (String) algorithms.get(mechanism);
        if (mechanism != null) {
            return (DERObjectIdentifier) oids.get(mechanism);
        }
        return null;
    }

    public static Enumeration getAlgorithms() {
        return oids.keys();
    }

    public static Digest getDigest(DERObjectIdentifier id) throws SecurityUtilityException {
        return getDigest(id.getId());
    }

    public static Digest getDigest(String algorithm) throws SecurityUtilityException {
        String upper = algorithm.toUpperCase();
        String mechanism = (String) algorithms.get(upper);
        if (mechanism == null) {
            mechanism = upper;
        }
        if (mechanism.equals("GOST3411")) return new GOST3411Digest(); else if (mechanism.equals("MD2")) return new MD2Digest(); else if (mechanism.equals("MD4")) return new MD4Digest(); else if (mechanism.equals("MD5")) return new MD5Digest(); else if (mechanism.equals("RIPEMD128")) return new RIPEMD128Digest(); else if (mechanism.equals("RIPEMD160")) return new RIPEMD160Digest(); else if (mechanism.equals("RIPEMD256")) return new RIPEMD256Digest(); else if (mechanism.equals("RIPEMD320")) return new RIPEMD320Digest(); else if (mechanism.equals("SHA-1")) return new SHA1Digest(); else if (mechanism.equals("SHA-224")) return new SHA224Digest(); else if (mechanism.equals("SHA-256")) return new SHA256Digest(); else if (mechanism.equals("SHA-384")) return new SHA384Digest(); else if (mechanism.equals("SHA-512")) return new SHA512Digest(); else if (mechanism.equals("TIGER")) return new TigerDigest(); else if (mechanism.equals("WHIRLPOOL")) return new WhirlpoolDigest(); else throw new SecurityUtilityException("Digest " + mechanism + " not recognised.");
    }

    public static String getAlgorithmnName(DERObjectIdentifier oid) {
        return (String) algorithms.get(oid.getId());
    }

    public static byte[] doFinal(Digest digest) {
        byte[] b = new byte[digest.getDigestSize()];
        digest.doFinal(b, 0);
        return b;
    }
}
