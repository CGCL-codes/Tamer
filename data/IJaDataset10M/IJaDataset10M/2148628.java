package net.sourceforge.piqle.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/** Just to read a character or a number from the keyboard. 
<br>
<i> This code comes from <i>IUT A</i> <i>
 */
public class KeyBoard {

    public static char readChar() {
        boolean erreur;
        String s;
        do {
            erreur = false;
            s = readString();
            if (s.length() != 1) {
                System.out.println("Hit a key, then Enter");
                erreur = true;
            }
        } while (erreur);
        return s.charAt(0);
    }

    public static int readInt() {
        boolean erreur;
        int n = 0;
        do {
            erreur = false;
            try {
                n = Integer.parseInt(readString());
            } catch (NumberFormatException e) {
                System.out.println("Wrong, try again");
                erreur = true;
            }
        } while (erreur);
        return n;
    }

    public static double readDouble() {
        boolean erreur;
        double n = 0;
        do {
            erreur = false;
            try {
                n = new Double(readString()).doubleValue();
            } catch (NumberFormatException e) {
                System.out.println("Wrong, try again");
                erreur = true;
            }
        } while (erreur);
        return n;
    }

    public static String readString() {
        String resultat = null;
        BufferedReader buf = new BufferedReader(new InputStreamReader(System.in));
        try {
            resultat = buf.readLine();
        } catch (IOException e) {
            System.out.println(e);
            return null;
        }
        return resultat;
    }
}
