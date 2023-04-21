package org.hackystat.socnet.server.resource.users;

import java.util.Random;

/**
 * Provides a static function for generating new, unique passwords. Package private because only
 * UserManager should be interacting with this class.
 * 
 * @author Philip M. Johnson
 */
class PasswordGenerator {

    /**
   * Eliminate letters/numbers that are easily confused, such as 0, O, l, 1, I, etc. Do NOT include
   * the '@' char, because that is used to distinguish dirkeys from email addresses.
   */
    private static String[] charset = { "A", "B", "C", "D", "E", "F", "G", "H", "J", "K", "L", "M", "N", "P", "Q", "R", "S", "T", "U", "V", "W", "X", "Y", "Z", "a", "b", "c", "d", "e", "f", "g", "h", "i", "j", "k", "m", "n", "p", "q", "r", "s", "t", "u", "v", "w", "x", "y", "z", "2", "3", "4", "5", "6", "7", "8", "9" };

    /** Length of the password. */
    private static final int PASSWORD_LENGTH = 12;

    /**
   * Creates and returns a new randomly generated password.
   * 
   * @return The random string.
   */
    public static String make() {
        Random generator = new Random(System.currentTimeMillis());
        StringBuffer password = null;
        password = new StringBuffer(PasswordGenerator.PASSWORD_LENGTH);
        for (int i = 0; i < PasswordGenerator.PASSWORD_LENGTH; i++) {
            password.append(charset[generator.nextInt(charset.length)]);
        }
        return password.toString();
    }

    /** Ensure that no one can create an instance of this class. */
    private PasswordGenerator() {
    }
}
