package net.sf.zorobot.conj;

public class NaruConjugator implements Conjugator {

    public String[] getName() {
        return new String[] { "naru", "naru-form", HiraStr.na + HiraStr.ru };
    }

    public ConjugatedString conjugate(ConjugatedString c) {
        if (c.isType("adj-")) {
            return null;
        } else if (c.isType("adj")) {
            return new ConjugatedString(c.word.substring(0, c.word.length() - 1) + HiraStr.ku + getSuffix(c), getType());
        }
        return null;
    }

    public String getSuffix(ConjugatedString stringToConjugate) {
        return HiraStr.na + HiraStr.ru;
    }

    public String getType() {
        return "v5";
    }
}
