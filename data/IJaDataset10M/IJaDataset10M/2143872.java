package net.sf.zorobot.conj;

public class MasuConjugator implements Conjugator {

    public String[] getName() {
        return new String[] { "polite", "polite-form", "masu", "masu-form", HiraStr.ma + HiraStr.su };
    }

    public ConjugatedString conjugate(ConjugatedString stringToConjugate) {
        String c = Util.toIForm(stringToConjugate);
        if (c != null) return new ConjugatedString(c + getSuffix(stringToConjugate), getType()); else return null;
    }

    public String getSuffix(ConjugatedString stringToConjugate) {
        return "ます";
    }

    public String getType() {
        return "masu";
    }
}
