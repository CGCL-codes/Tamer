package net.sf.zorobot.conj;

import java.util.Hashtable;

public class SpecialStemDict {

    Hashtable dict = new Hashtable();

    public SpecialStemDict() {
        add(new MasuSS());
        add(new DesuSS());
    }

    private void add(SpecialStem ss) {
        dict.put(ss.getType(), ss);
    }

    public String getA(String type, String word) {
        SpecialStem ss = (SpecialStem) dict.get(type);
        if (ss != null) {
            return prefix(word, ss.getWordLength()) + ss.getA();
        }
        return null;
    }

    public String getI(String type, String word) {
        SpecialStem ss = (SpecialStem) dict.get(type);
        if (ss != null) {
            return prefix(word, ss.getWordLength()) + ss.getI();
        }
        return null;
    }

    public String getU(String type, String word) {
        SpecialStem ss = (SpecialStem) dict.get(type);
        if (ss != null) {
            return prefix(word, ss.getWordLength()) + ss.getU();
        }
        return null;
    }

    public String getE(String type, String word) {
        SpecialStem ss = (SpecialStem) dict.get(type);
        if (ss != null) {
            return prefix(word, ss.getWordLength()) + ss.getE();
        }
        return null;
    }

    public String getO(String type, String word) {
        SpecialStem ss = (SpecialStem) dict.get(type);
        if (ss != null) {
            return prefix(word, ss.getWordLength()) + ss.getO();
        }
        return null;
    }

    private String prefix(String word, int i) {
        return word.substring(0, word.length() - i);
    }
}
