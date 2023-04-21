package com.apelon.matchpack.lexicon.category;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

/**
 * This class calucaltes the variants of Noun category.
 * For detailed alogrithm, see page 18 in "The SPECIALIST Lexicon" in UMLS2001.
 *
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2002</p>
 * <p>Company: </p>
 * @author unascribed
 * @version 1.0
 */
public class NounCat extends SyntacticCategory {

    public NounCat() {
    }

    public String[] getAbbreviationFromLine(String base, String abbreviations) {
        int index = abbreviations.indexOf("|");
        String value = null;
        if (index < 0) {
            value = abbreviations;
        } else {
            value = abbreviations.substring(0, index);
        }
        return new String[] { value };
    }

    public String[] getAcronymFromLine(String base, String acronyms) {
        int index = acronyms.indexOf("|");
        String value = null;
        if (index < 0) {
            value = acronyms;
        } else {
            value = acronyms.substring(0, index);
        }
        return new String[] { value };
    }

    public String[] getVariantFromLine(String base, String variant) {
        String value = null;
        List strings = new ArrayList();
        if (variant.equals("reg")) {
            value = plural(base);
            strings.add(value);
        } else if (variant.startsWith("glreg")) {
            value = grecoLatinPlural(base);
            strings.add(value);
        } else if (variant.startsWith("metareg")) {
            String[] values = metaLinguistic(base);
            for (int i = 0; i < values.length; i++) {
                strings.add(values[i]);
            }
        } else if (variant.startsWith("irreg")) {
            int length = "irreg".length();
            StringTokenizer st = new StringTokenizer(variant.substring(length + 1), "|");
            while (st.hasMoreTokens()) {
                value = st.nextToken();
                strings.add(value);
            }
        } else if (variant.startsWith("sing")) {
            strings.add(base);
        } else if (variant.startsWith("plur")) {
            strings.add(base);
        } else if (variant.startsWith("inv")) {
            strings.add(base);
        } else if (variant.startsWith("group(")) {
            strings.add(base);
        } else if (variant.startsWith("uncount")) {
            strings.add(base);
        } else if (variant.startsWith("groupuncount")) {
            strings.add(base);
        }
        return (String[]) strings.toArray(new String[0]);
    }

    public static String plural(String base) {
        int length = base.length();
        if (base.endsWith("y") && !AdjCat.isVowel(base.charAt(length - 2))) {
            return base.substring(0, length - 1) + "ies";
        }
        if (base.endsWith("s") || base.endsWith("z") || base.endsWith("x") || base.endsWith("ch") || base.endsWith("sh")) {
            return base + "es";
        }
        if (base.charAt(length - 1) != 'y') {
            return base + "s";
        }
        return base + "s";
    }

    public static String grecoLatinPlural(String base) {
        int length = base.length();
        if (base.endsWith("us")) {
            return base.substring(0, length - 2) + "i";
        }
        if (base.endsWith("ma")) {
            return base + "ta";
        }
        if (base.endsWith("a")) {
            return base + "e";
        }
        if (base.endsWith("um") || base.endsWith("on")) {
            return base.substring(0, length - 2) + "a";
        }
        if (base.endsWith("sis")) {
            return base.substring(0, length - 2) + "es";
        }
        if (base.endsWith("is")) {
            return base.substring(0, length - 1) + "des";
        }
        if (base.endsWith("men")) {
            return base.substring(0, length - 2) + "ina";
        }
        if (base.endsWith("ex")) {
            return base.substring(0, length - 2) + "ices";
        }
        if (base.endsWith("x")) {
            return base.substring(0, length - 1) + "ces";
        }
        System.out.println("Noun: grecoLatinPlural: cannot cover " + base);
        return base + "s";
    }

    public static String[] metaLinguistic(String base) {
        return new String[] { base + "s", base + "'s" };
    }

    public static void main(String[] args) throws Exception {
        BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
        while (true) {
            System.out.println("Enter phrase to be normalized: hit enter to exit");
            String input = in.readLine();
            System.out.println(NounCat.plural(input));
        }
    }
}
