package com.sitescape.team.lucene;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.lang.Character.UnicodeBlock;

public class LanguageTaster {

    public static String DEFAULT = "DEFAULT";

    public static String CJK = "CJK";

    public static String ARABIC = "ARABIC";

    public static String HEBREW = "HEBREW";

    public static String taste(char[] cbuf) {
        try {
            int buffCount = 0;
            int arabicCount = 0;
            int hebrewCount = 0;
            int cjkCount = 0;
            int defCount = 0;
            int bt = cbuf.length;
            while (buffCount < bt) {
                char c = cbuf[buffCount];
                UnicodeBlock cu = UnicodeBlock.of(c);
                if (cu == UnicodeBlock.ARABIC || cu == UnicodeBlock.ARABIC_PRESENTATION_FORMS_A || cu == UnicodeBlock.ARABIC_PRESENTATION_FORMS_B) {
                    arabicCount++;
                } else if (cu == UnicodeBlock.HEBREW) {
                    hebrewCount++;
                } else if (cu == UnicodeBlock.CJK_COMPATIBILITY || cu == UnicodeBlock.CJK_COMPATIBILITY_FORMS || cu == UnicodeBlock.CJK_COMPATIBILITY_IDEOGRAPHS || cu == UnicodeBlock.CJK_COMPATIBILITY_IDEOGRAPHS_SUPPLEMENT || cu == UnicodeBlock.CJK_RADICALS_SUPPLEMENT || cu == UnicodeBlock.CJK_SYMBOLS_AND_PUNCTUATION || cu == UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS || cu == UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS_EXTENSION_A || cu == UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS_EXTENSION_B || cu == UnicodeBlock.HANGUL_COMPATIBILITY_JAMO || cu == UnicodeBlock.HANGUL_JAMO || cu == UnicodeBlock.HANGUL_SYLLABLES || cu == UnicodeBlock.HIRAGANA || cu == UnicodeBlock.KATAKANA || cu == UnicodeBlock.KATAKANA_PHONETIC_EXTENSIONS || cu == UnicodeBlock.ENCLOSED_CJK_LETTERS_AND_MONTHS || cu == UnicodeBlock.YIJING_HEXAGRAM_SYMBOLS) {
                    cjkCount++;
                } else {
                    defCount++;
                }
                buffCount++;
            }
            if (cjkCount > 0) {
                return CJK;
            } else if (hebrewCount > 0) {
                return HEBREW;
            } else if (arabicCount > 0) {
                return ARABIC;
            } else {
                return DEFAULT;
            }
        } catch (Exception e) {
            System.out.println(e.toString());
        }
        return "DEFAULT";
    }

    public static void main(String[] args) {
        InputStream fis = null;
        char cbuf[] = new char[1024];
        try {
            fis = new FileInputStream("/languages/japan.txt");
            Reader rd = new InputStreamReader(fis, "UTF-8");
            BufferedReader buff = new BufferedReader(rd);
            int bt = buff.read(cbuf, 0, 1024);
            if (bt == -1) {
                System.out.println("Uh Oh");
            }
        } catch (Exception e) {
            System.out.println(e.toString());
        } finally {
            try {
                fis.close();
            } catch (Exception e) {
            }
        }
        System.out.println(taste(cbuf));
    }
}
