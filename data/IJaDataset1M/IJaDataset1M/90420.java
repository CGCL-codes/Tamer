package com.mindprod;

import com.google.gwt.user.server.Base64Utils;

/**
 * The xor password scrambler uses the xor
 * operator to mask passwords in your Java source code.
 * Use this when you want to embed a password in
 * your Java code but don't want a casual
 * observer of your code or your class
 * file to see it.
 * <p>
 * Understand that embedding passwords in a
 * program is always insecure no
 * matter how clever the scrambling method,
 * because the program contains
 * all the code needed to descramble it.
 * My technique provides only
 * protection against casual hackers.
 * For true security you must not embed
 * passwords in a program at all.
 * The user should type them in each time, or
 * use a ThumbDrive to store them.
 * @see http://mindprod.com/bgloss/thumbdrive.html
 * @author Roedy Green  copyright Canadian Mind Products 2005
 * may be used freely for any purpose but military.
 * version 1.0 2005-07-05
 **/
public class XorPWScrambler {

    /**
    * This is NOT the password. This is a scrambler phrase for the password.
    * The key used both to scramble and unscramble the password.
    * Just some pass phrase ideally
    * that blends into your program that looks like nothing special e.g.
    ( "error 576 : Token stack initialiser error."
    */
    static final char[] scrambler = "error 576 : Token stack initialiser error.".toCharArray();

    /**
    * You run this code once ahead of time to find out the string to embed
    * to represent the password. You later embed some of the code it generates
    * in your own program.
    *
    * @param put your real case-sensitive password
    * on the command line IN QUOTES!
    */
    public static void main(String[] args) {
        if (args.length != 1) {
            throw new IllegalArgumentException("You need to put your real password on the command line in quotes: e.g \"ali baba\"");
        }
        char[] scrambledPassword = scramble(args[0]);
        byte[] scrambledPasswordData = new String(scrambledPassword).getBytes();
        String scrambledPasswordBase64 = Base64Utils.toBase64(scrambledPasswordData);
        System.out.println("String scrambledBase64Password=\"" + scrambledPasswordBase64 + "\";");
        byte[] reconstructedData = Base64Utils.fromBase64(scrambledPasswordBase64);
        String reconstructedScrambledPassword = new String(reconstructedData);
        String reconstitutedPassword = unscramble(reconstructedScrambledPassword.toCharArray());
        System.out.println("reconstituted: " + reconstitutedPassword);
    }

    /**
    * display the magic numbers representing your scrambled password.
    * @param realPassword your actual password or passphrase you want to
    * scramble so you can more safely embed it in a program.
    * @return scrambled password as a String. Not much use since it
    * contains gibberish characters. See console for the
    * java code for a char[] representation
    */
    public static char[] scramble(String realPassword) {
        char[] thePasswordYouEmbed = xor(realPassword.toCharArray(), scrambler);
        System.out.println();
        System.out.println("char[] scrambledPassword = new char[] {");
        for (int i = 0; i < thePasswordYouEmbed.length; i++) {
            System.out.println((int) thePasswordYouEmbed[i] + ",");
        }
        System.out.println("};");
        System.out.println("String reconstitutedPassword = unscramble( scrambledPassword );");
        System.out.println();
        return thePasswordYouEmbed;
    }

    /**
    * reconstitute a scrambled password
    * @param scrambledPassword an array of chars mostly unprintable.
    */
    public static String unscramble(char[] scrambledPassword) {
        return new String(xor(scrambledPassword, scrambler));
    }

    /**
    * @return xor of arrays a and b
    */
    public static char[] xor(char[] a, char[] b) {
        int length = Math.min(a.length, b.length);
        char[] result = new char[length];
        for (int i = 0; i < length; i++) {
            result[i] = (char) (a[i] ^ b[i]);
        }
        return result;
    }
}
