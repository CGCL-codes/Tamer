package net.derquinse.common.base;

import static com.google.common.base.Preconditions.checkNotNull;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Utility class for dealing with message digests.
 * @author Andres Rodriguez
 */
public final class Digests {

    /** Not instantiable. */
    private Digests() {
        throw new AssertionError();
    }

    /** Algorithm: MD5. */
    public static final String MD5 = "MD5";

    /** Algorithm: SHA-1. */
    public static final String SHA1 = "SHA-1";

    /** Algorithm: SHA-256. */
    public static final String SHA256 = "SHA-256";

    /** Algorithm: SHA-512. */
    public static final String SHA512 = "SHA-512";

    /**
	 * Creates a new instance.
	 * @param algorithm Digest algorithm.
	 * @return A Digest object that implements the specified algorithm.
	 * @throws NoSuchAlgorithmException If the algorithm is not available in the caller's environment.
	 */
    public static MessageDigest getInstance(String algorithm) throws NoSuchAlgorithmException {
        checkNotNull(algorithm, "The digest algorithm must be provided");
        return MessageDigest.getInstance(algorithm);
    }

    /**
	 * Returns a MessageDigest object that implements the specified digest algorithm.
	 * @param algorithm the name of the algorithm requested.
	 * @return A Digest object that implements the specified algorithm.
	 * @throws RuntimeException (with {@link NoSuchAlgorithmException} cause) if the specified
	 *           algorithm is not provided.
	 */
    private static MessageDigest getInstanceUnchecked(String algorithm) {
        try {
            return MessageDigest.getInstance(algorithm);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(String.format("No %s algorithm", algorithm), e);
        }
    }

    /**
	 * Returns a Digest object that implements the MD5 algorithm.
	 */
    public static MessageDigest md5() {
        return getInstanceUnchecked(MD5);
    }

    /**
	 * Returns a Digest object that implements the SHA-1 algorithm.
	 */
    public static MessageDigest sha1() {
        return getInstanceUnchecked(SHA1);
    }

    /**
	 * Returns a Digest object that implements the SHA-256 algorithm.
	 */
    public static MessageDigest sha256() {
        return getInstanceUnchecked(SHA256);
    }

    /**
	 * Returns a Digest object that implements the SHA-512 algorithm.
	 */
    public static MessageDigest sha512() {
        return getInstanceUnchecked(SHA512);
    }
}
