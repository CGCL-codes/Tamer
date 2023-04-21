package test;

import static java.lang.System.out;
import java.io.File;
import java.util.Scanner;
import com.tscribble.bitleech.core.download.auth.Authentication;
import com.tscribble.bitleech.core.security.DES;
import com.tscribble.bitleech.core.security.HashUtils;

public class DecryptTest {

    public static void main(String[] args) {
        try {
            dencryptAuthentication();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void dencryptAuthentication() throws Exception {
        Scanner input = new Scanner(System.in);
        Scanner masterfile = new Scanner(new File("master.txt"));
        String filehash = masterfile.nextLine();
        out.print("Enter master password for decryption: ");
        String masterpass = input.nextLine();
        String inputhash = HashUtils.sha(masterpass);
        while (!inputhash.equals(filehash)) {
            out.print("Wrong master password, try again: ");
            masterpass = input.nextLine();
            inputhash = HashUtils.sha(masterpass);
        }
        out.println("\nAuthentication Succesfull:");
        DES des = new DES(masterpass);
        Scanner sitesfile = new Scanner(new File("sites.txt"));
        while (sitesfile.hasNext()) {
            String line = sitesfile.nextLine();
            String host = line.substring(0, line.indexOf('='));
            String user = line.substring(line.indexOf('=') + 1, line.indexOf(','));
            String pass = line.substring(line.indexOf(',') + 1, line.length());
            Authentication auth = null;
            out.println(auth);
        }
        input.close();
        masterfile.close();
        sitesfile.close();
    }
}
