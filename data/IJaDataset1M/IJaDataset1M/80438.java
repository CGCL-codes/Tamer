package frost;

import java.io.*;
import frost.crypt.*;

public class crypttest {

    private static Crypt crp = new FrostCrypt();

    private static String[] keys;

    private static String[] keys2;

    public static void main(String[] arg) {
        BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
        keys = crp.generateKeys();
        keys2 = crp.generateKeys();
        System.out.println("private key sha1 : ");
        System.out.println(crp.digest(keys[0]));
        String signed;
        System.out.println("type another plaintext");
        try {
            signed = new String(crp.encryptSign(in.readLine().getBytes(), keys[0], keys2[1]));
        } catch (IOException e) {
            signed = new String("exception happened");
        }
        System.out.println(signed);
        signed = signed.substring(0, 150) + "tra tra tra" + signed.substring(161, signed.length());
        System.out.println("modified signed:");
        System.out.println(signed);
        System.out.println("trying to decrypt");
        signed = new String(crp.decrypt(signed.getBytes(), keys2[0]));
        if (signed != null) System.out.println(signed);
    }
}
