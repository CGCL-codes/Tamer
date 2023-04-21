package core.crypto;

/**
 * This class has the constants used for encrypt/decrypt the files on CryptoInputStream and
 * CryptoOutputStream it.
 * The Cipher used is provided by "Legion of the Bouncy Castle" library
 * @author Glauber Magalh�es Pires
 *
 */
public class CryptoConstants {

    public static final String cipher = "PBEWITHSHA256AND192BITAES-CBC-BC";

    public static final byte[] salt = { (byte) 0xA9, (byte) 0x9B, (byte) 0xC8, (byte) 0x32, (byte) 0x56, (byte) 0x35, (byte) 0xE3, (byte) 0x03 };

    public static final int iterationCount = 19;
}
