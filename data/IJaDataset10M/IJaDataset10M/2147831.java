package net.sourceforge.maxim.server.db;

import java.security.*;

public class Hash {

    /**
    * Performs a hash on a given string, using a specified algorithm,
    * and returns the hash of that string using that specified
    * algorithm.
    * <p>
    * Example: hash("test","MD5")
    * <p>
    * Can also use SHA, SHA-1.
    * @param data The string to be hashed
    * @param hashType The type of hash to be used
    * @return A string containing the hash
    */
    public String hash(String data, String hashType) {
        byte[] byteRepresentation = data.getBytes();
        try {
            MessageDigest algorithm = MessageDigest.getInstance(hashType);
            algorithm.reset();
            algorithm.update(byteRepresentation);
            byte messageDigest[] = algorithm.digest();
            StringBuffer hexString = new StringBuffer();
            int i = 0;
            while (i < messageDigest.length) {
                hexString.append(Integer.toHexString(0xFF & messageDigest[i]));
                i++;
            }
            return hexString.toString();
        } catch (NoSuchAlgorithmException nsae) {
            return null;
        }
    }
}
