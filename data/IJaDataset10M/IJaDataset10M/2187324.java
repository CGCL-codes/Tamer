package nlp.lang.he.morph.erel.mila.morphemes;

import nlp.lang.he.morph.erel.io.TextFormatException;

/**
 * class XlbDibr xelqei dibber (po&l, &ecm, to^r,..., $em-pra@i).
 */
public final class PartOfSpeech {

    public static final byte KLUM = 0;

    public static final byte VERB = 1;

    public static final byte ECM = 2;

    public static final byte TOAR = 3;

    public static final byte MSPR = 4;

    public static final byte MILT_YXS = 5;

    public static final byte MILT_GUF = 6;

    public static final byte MILT_JELA = 7;

    public static final byte MILT_XIBUR = -8;

    public static final byte MILIT = -7;

    public static final byte TOAR_POAL = -6;

    public static final byte AUXILARY_VERB = -5;

    public static final byte JM_PRTI = -4;

    public static final byte RAJON = -8;

    public static final byte AXRON = 7;

    private static char[] chrs = { 'x', 'm', 't', 'z', 'p', '*', '*', '*', '*', 'P', 'E', 'T', 'M', 'y', 'g', 'j' };

    private static String[] strs = { "MILT~XIBWR", "MILIT", "TWAR~PW&L", "PW&L~&ZR", "$M~PR@I", "*", "*", "*", "*", "PW&L", "&CM", "TWAR", "MSPR", "MILT~IXS", "MILT~GWP", "MILT~$ALH" };

    /** mtargem xelq-dibber l-"char" */
    public static final char chr(byte xd) {
        return chrs[xd + 8];
    }

    /** mtargem xelq-dibber l-"String" */
    public static final String str(byte xd) {
        return strs[xd + 8];
    }

    /** mtargem "char" l-xelq-dibber */
    public static final byte xlqdibr(char c) throws TextFormatException {
        return c == 'P' ? VERB : c == 'E' ? ECM : c == 'T' ? TOAR : c == 'M' ? MSPR : c == 'y' ? MILT_YXS : c == 'g' ? MILT_GUF : c == 'j' ? MILT_JELA : c == 'x' ? MILT_XIBUR : c == 'm' ? MILIT : c == 't' ? TOAR_POAL : c == 'z' ? AUXILARY_VERB : c == 'p' ? JM_PRTI : c == '*' ? KLUM : MorphologicalForms.KLUM();
    }

    /** bodeq $qilut bein $nei xelqei-dibber */
    public static final boolean jwim(byte a, byte b) {
        return a == b;
    }

    public static final boolean hu_poal(byte xd) {
        return xd == VERB || xd == AUXILARY_VERB;
    }

    public static final boolean ykol_lhyot_somk(byte xd) {
        return xd == ECM || xd == MSPR || xd == JM_PRTI;
    }
}
