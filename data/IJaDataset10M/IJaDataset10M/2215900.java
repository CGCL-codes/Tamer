package jp.ac.keio.ae.comp.yamaguti.doddle.taskanalyzer;

/**
 * @author takeshi morita
 */
public class Morpheme {

    private String surface;

    private String kana;

    private String basic;

    private String pos;

    public static final String NOUN = "名詞-";

    public static final String NOUN_NUM = "名詞-数";

    public static final String VERB = "動詞-";

    public static final String SYMBOL = "記号-";

    public static final String SYMBOL_ALPHABET = "記号-アルファベット";

    public static final String UNKNOWN_WORD = "未知語";

    public static final String SYMBOL_OPENED_PARENTHESIS = "記号-括弧開";

    public static final String SYMBOL_CLOSED_PARENTHESIS = "記号-括弧閉";

    public Morpheme(String[] elems) {
        this(elems[0], elems[1], elems[2], elems[3]);
    }

    public Morpheme(String s, String k, String b, String p) {
        b = b.replaceAll("（", "(");
        b = b.replaceAll("）", ")");
        surface = s;
        kana = k;
        basic = b;
        pos = p;
    }

    public String getSurface() {
        return surface;
    }

    public String getKana() {
        return kana;
    }

    public String getBasic() {
        return basic;
    }

    public String getPos() {
        return pos;
    }

    public boolean equals(Object obj) {
        Morpheme om = (Morpheme) obj;
        return om.getSurface().equals(surface);
    }

    public String toString() {
        return surface;
    }

    public int hashCode() {
        return 0;
    }
}
