package up5.mi.visio.accountingservice;

import java.util.Random;

public class RandomString {

    private int taille;

    private static final int chiffre = 0;

    private static final int lettre_min = 1;

    private static final int lettre_maj = 2;

    public RandomString(int taille) {
        this.taille = taille;
    }

    private char nextChar() {
        int ret = 0;
        Random r = new Random();
        int nextType = r.nextInt(3);
        switch(nextType) {
            case chiffre:
                ret = r.nextInt(10);
                ret += 48;
                break;
            case lettre_min:
                ret = r.nextInt(26);
                ret += 97;
                break;
            case lettre_maj:
                ret = r.nextInt(26);
                ret += 65;
                break;
        }
        return (char) ret;
    }

    public String generateRandomString() {
        char[] tab_c = new char[taille];
        for (int i = 0; i < taille; i++) {
            tab_c[i] = this.nextChar();
            try {
                Thread.sleep(500);
            } catch (Exception e) {
            }
        }
        return new String(tab_c);
    }
}
