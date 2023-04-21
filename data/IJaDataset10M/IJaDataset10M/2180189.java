package com.apelon.matchpack.lexicon.category;

import com.apelon.matchpack.lexicon.LexiconEntry;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

/**
 * This class calucaltes the variants of Adjective category.
 * For detailed alogrithm, see page 7 in "The SPECIALIST Lexicon" in UMLS2001.
 *
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2002</p>
 * <p>Company: </p>
 * @author unascribed
 * @version 1.0
 */
public class VerbCat extends SyntacticCategory {

    public VerbCat() {
    }

    public String[] getVariantFromLine(String base, String variants) {
        List strings = new ArrayList();
        String value = null;
        if (variants.equals("reg")) {
            value = thirdSingular(base);
            strings.add(value);
            value = pastParticiple(base);
            strings.add(value);
            value = presentParticiple(base);
            strings.add(value);
        } else if (variants.equals("regd")) {
            value = thirdSingular(base);
            strings.add(value);
            value = doublePastParticiple(base);
            strings.add(value);
            value = doublePresentParticiple(base);
            strings.add(value);
        } else if (variants.startsWith("irreg")) {
            int length = "irreg".length();
            StringTokenizer st = new StringTokenizer(variants.substring(length + 1), "|");
            while (st.hasMoreTokens()) {
                value = st.nextToken();
                strings.add(value);
            }
        }
        return (String[]) strings.toArray(new String[0]);
    }

    public static String thirdSingular(String base) {
        int length = base.length();
        if (base.endsWith("s") || base.endsWith("z") || base.endsWith("x") || base.endsWith("ch") || base.endsWith("sh")) {
            return base + "es";
        }
        if (base.endsWith("ie") || base.endsWith("ee") || base.endsWith("oe") || base.endsWith("ye")) {
            return base + "s";
        }
        if (base.charAt(length - 1) == 'y' && !AdjCat.isVowel(base.charAt(length - 2))) {
            return base.substring(0, base.length() - 1) + "ies";
        }
        char secondChar = base.charAt(length - 2);
        if (base.charAt(length - 1) == 'e' && !(secondChar == 'i' || secondChar == 'y' || secondChar == 'e' || secondChar == 'o')) {
            return base + "s";
        }
        return base + "s";
    }

    public static String pastParticiple(String base) {
        int length = base.length();
        if (base.endsWith("s") || base.endsWith("z") || base.endsWith("x") || base.endsWith("ch") || base.endsWith("sh")) {
            return base + "ed";
        }
        if (base.endsWith("ie") || base.endsWith("ee") || base.endsWith("oe") || base.endsWith("ye")) {
            return base + "d";
        }
        if (base.charAt(length - 1) == 'y' && !AdjCat.isVowel(base.charAt(length - 2))) {
            return base.substring(0, base.length() - 1) + "ied";
        }
        char secondChar = base.charAt(length - 2);
        if (base.charAt(length - 1) == 'e' && !(secondChar == 'i' || secondChar == 'y' || secondChar == 'e' || secondChar == 'o')) {
            return base + "d";
        }
        return base + "ed";
    }

    public static String presentParticiple(String base) {
        int length = base.length();
        if (base.endsWith("s") || base.endsWith("z") || base.endsWith("x") || base.endsWith("ch") || base.endsWith("sh")) {
            return base + "ing";
        }
        if (base.endsWith("ie")) {
            return base.substring(0, base.length() - 2) + "ying";
        }
        if (base.endsWith("ee") || base.endsWith("oe") || base.endsWith("ye")) {
            return base + "ing";
        }
        if (base.charAt(length - 1) == 'y' && !AdjCat.isVowel(base.charAt(length - 2))) {
            return base + "ing";
        }
        char secondChar = base.charAt(length - 2);
        if (base.charAt(length - 1) == 'e' && !(secondChar == 'i' || secondChar == 'y' || secondChar == 'e' || secondChar == 'o')) {
            return base.substring(0, base.length() - 1) + "ing";
        }
        return base + "ing";
    }

    public static String doublePastParticiple(String base) {
        int length = base.length();
        return base + base.charAt(length - 1) + "ed";
    }

    public static String doublePresentParticiple(String base) {
        int length = base.length();
        return base + base.charAt(length - 1) + "ing";
    }

    public static void main(String[] args) throws Exception {
        AdjCat.testCategory(args, LexiconEntry.VERB_CAT);
    }
}
