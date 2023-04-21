package applications;

import hcrypto.cipher.*;
import hcrypto.provider.*;
import hcrypto.engines.*;

/**
 *  <p>This command-line program can be used to test the
 *   installation of the HcryptoJ API. The following example command
 *   lines show how to compile and run the program.  It assumes that 
 *   the hcrypto.jar file is located 2 directory levels above the directory 
 *   containing this program, which is the default hierarchy of HcryptoJ.
 *
 *   <p>In the following example, a Caesar cipher with a shift of 4 and an
 *   alphabet of 'a' to 'z' is being used to encrypt and then decrypt
 *   the string "thisisatest".
 * <pre>
 *  To compile: javac -classpath ../../hcrypto.jar:. TestCipher.java
 *  To run:     java -classpath ../../hcrypto.jar:. TestCipher Caesar/Provider 4/az thisisatest
 * </pre>
 */
public class TestCipher {

    public static void main(String args[]) throws Exception {
        if (args.length < 3) {
            System.out.println("Usage: java TestCipher algorithm keyspec plaintext [-d]");
            System.out.println("Usage: java TestCipher Caesar 5/az+AZ/az+AZ thisisatest");
            System.out.println("Usage: java TestCipher Vigenere keyword/az+AZ thisisatestofVigenereCipher");
            System.out.println("Usage: java TestCipher Transposition 3012/printable thisisatest");
            return;
        }
        Provider.addProvider(new DefaultProvider("Default"));
        Provider.addProvider(new RamProvider("Ram"));
        java.util.StringTokenizer st = new java.util.StringTokenizer(Provider.getCipherNames(), Provider.DELIMITER);
        System.out.println("Available algorithms (Provider): ");
        while (st.hasMoreTokens()) System.out.println(st.nextToken());
        String cipherName, providerName;
        if (args[0].indexOf('/') != -1) {
            cipherName = args[0].substring(0, args[0].indexOf('/'));
            providerName = args[0].substring(args[0].indexOf('/') + 1);
        } else {
            cipherName = args[0];
            providerName = "Default";
        }
        String keyspec = args[1];
        Cipher cipher = Cipher.getInstance(cipherName, providerName);
        HistoricalKey key = HistoricalKey.getInstance(cipher.getAlgorithm(), cipher.getProvider());
        key.init(keyspec);
        System.out.println("\nThe keyword is: " + key.getKeyword());
        cipher.init(key);
        System.out.println("Algorithm = " + cipher.getAlgorithm());
        System.out.println("Provider = " + cipher.getProvider());
        String secret = args[2];
        String c1encrypt;
        System.out.println(secret);
        if (args.length == 4 && args[3].equals("-d")) c1encrypt = cipher.decrypt(secret); else c1encrypt = cipher.encrypt(secret);
        System.out.println(c1encrypt);
        cipher.init(key);
        if (args.length == 4 && args[3].equals("-d")) System.out.println(cipher.encrypt(c1encrypt)); else System.out.println(cipher.decrypt(c1encrypt));
        System.out.println();
    }
}
