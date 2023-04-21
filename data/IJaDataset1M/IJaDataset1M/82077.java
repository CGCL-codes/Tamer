package com.aconiac.apg.domain;

import java.security.SecureRandom;
import java.util.*;

public class PasswordGeneratorImpl implements PasswordGenerator {

    public static final String Zero_Through_Nine_SET = new String("0123456789");

    public static final String a_Through_z_SET = new String("abcdefghijklmnopqrstuvwxyz");

    public static final String A_Through_Z_SET = new String("ABCDEFGHIJKLMNOPQRSTUVWXYZ");

    public static final String Exotic_Symbols_SET = new String("*=+%$^{¤<}]\"\\|´~'>`[:;]");

    public static final String Exotic_Characters_SET = new String("øØæÆÿŸìÌòÒûÛà�?êÊúÚöÖâÂéÉóÓäÄåÅëËùÙý�?í�?ï�?üÜôÔîÎèÈáÀ");

    public static final String Normal_Symbols_SET = new String(".,-_()!?@/&#");

    private Random random;

    private String characterSet;

    private ArrayList<Rule> ruleList;

    public PasswordGeneratorImpl() {
        random = new SecureRandom();
        ruleList = new ArrayList<Rule>();
        characterSet += Zero_Through_Nine_SET;
        characterSet += a_Through_z_SET;
        characterSet += A_Through_Z_SET;
    }

    public String generate(int length) {
        String localSet = shuffle(characterSet);
        String res = "";
        boolean stop = false;
        while (!stop) {
            res = "";
            for (int i = 0; i < length; i++) {
                int pos = rnd(0, localSet.length() - 1);
                res += localSet.charAt(pos);
            }
            boolean validated = true;
            for (Iterator<Rule> it = getRuleList().iterator(); it.hasNext(); ) {
                Rule r = (Rule) it.next();
                if (!r.isValid(res)) {
                    validated = false;
                    break;
                }
            }
            stop = validated;
        }
        return res;
    }

    /**
     * Shuffle a string
     */
    private String shuffle(String data) {
        int len = data.length();
        String res = data;
        for (int i = 0; i < len; i++) {
            int pos1 = rnd(0, len - 1), pos2 = rnd(0, len - 1);
            String str1 = res.charAt(pos1) + "", str2 = res.charAt(pos2) + "", res2 = "";
            for (int j = 0; j < len; j++) {
                if (j == pos1) res2 += str2; else if (j == pos2) res2 += str1; else res2 += res.charAt(j) + "";
            }
            res = res2;
        }
        return res;
    }

    /**
     * Return a random number between 'min' and 'high'
     */
    private int rnd(int min, int high) {
        int i = random.nextInt() % (high - min + 1);
        if (i < 0) i = -i;
        return min + i;
    }

    public double checkStrength(int passwordLength, int characterSetSize) {
        return (double) passwordLength * (Math.log((double) characterSetSize) / Math.log(2.0));
    }

    public String strengthDescription(double entropyBits) {
        String desc = "";
        if (entropyBits >= 0 && entropyBits <= 6) desc = "If nobody tries you are safe"; else if (entropyBits > 6 && entropyBits <= 15) desc = "Your young cousin will compromise you in 5 min."; else if (entropyBits > 15 && entropyBits <= 21) desc = "You are protected from most people"; else if (entropyBits > 21 && entropyBits <= 30) desc = "For the amateur script kiddie this will be hard"; else if (entropyBits > 30 && entropyBits <= 40) desc = "For the dedicated script kiddie this will be hard"; else if (entropyBits > 40 && entropyBits <= 45) desc = "For the undedicated cracker this will be hard"; else if (entropyBits > 45 && entropyBits <= 56) desc = "For the dedicated cracker this will be hard"; else if (entropyBits > 56 && entropyBits <= 63) desc = "For most corporations this will be hard"; else if (entropyBits > 63 && entropyBits <= 71) desc = "For most governments this will be hard"; else if (entropyBits > 70 && entropyBits <= 80) desc = "Should protect you against today's world"; else if (entropyBits > 80 && entropyBits <= 88) desc = "Should protect you against tomorrow's world"; else if (entropyBits > 88 && entropyBits <= 105) desc = "Should protect you against any mortal"; else if (entropyBits > 105) desc = "This should be super-duper fantastically secure!";
        return desc;
    }

    public void setSeed(long seed) {
        random.setSeed(seed);
    }

    public void setCharacterSet(String characterSet) {
        this.characterSet = characterSet;
    }

    public String getCharacterSet() {
        return characterSet;
    }

    public void addRule(Rule rule) {
        ruleList.add(rule);
    }

    public void removeRule(Rule rule) {
        ruleList.remove(rule);
    }

    public ArrayList<Rule> getRuleList() {
        return ruleList;
    }
}
