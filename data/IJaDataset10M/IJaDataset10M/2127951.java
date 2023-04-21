package net.sf.zorobot.conj;

public class NConjugator implements Conjugator {

    public String[] getName() {
        return new String[] { "n", "n-form", HiraStr.n };
    }

    public ConjugatedString conjugate(ConjugatedString stringToConjugate) {
        String c = Util.toAForm(stringToConjugate);
        if (c != null) return new ConjugatedString(c + getSuffix(stringToConjugate), getType()); else return null;
    }

    public String getSuffix(ConjugatedString stringToConjugate) {
        return HiraStr.n;
    }

    public String getType() {
        return "n";
    }
}
