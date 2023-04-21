package de.goesberserk.trillian;

import de.goesberserk.trillian.password.*;

/**
 *
 * @author till.klocke
 */
public class Main {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        String usage = " Usage: java -jar TrillianPasswordRecovery.jar -s [encryptedPassword] or\n" + "java -jar TrillianPasswordRecovery.jar -f [path/to/config/file]";
        if (args.length == 2) {
            if (args[0].equals("-s")) {
                try {
                    Recovery recover = new Recovery();
                    out("Password: " + recover.getDecryptedPassword(args[1]));
                } catch (Exception e) {
                    out(e.toString());
                }
            } else if (args[0].equals("-f")) {
                try {
                    Recovery recover = new Recovery(args[1]);
                    out("Passwort: " + recover.getDecryptedPassword());
                } catch (Exception e) {
                    out(e.toString());
                }
            }
        } else {
            out(usage);
        }
    }

    private static void out(String out) {
        System.out.println(out);
    }
}
