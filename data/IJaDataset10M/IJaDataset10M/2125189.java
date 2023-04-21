package jp.co.omronsoft.openwnn.JAJP;

import jp.co.omronsoft.openwnn.LetterConverter;
import jp.co.omronsoft.openwnn.ComposingText;
import jp.co.omronsoft.openwnn.StrSegment;
import java.util.HashMap;
import android.content.SharedPreferences;
import java.util.regex.Pattern;
import java.util.regex.Matcher;

/**
 * The Romaji to full-width Katakana converter class for Japanese IME.
 *
 * @author Copyright (C) 2009 OMRON SOFTWARE CO., LTD.  All Rights Reserved.
 */
public class RomkanFullKatakana implements LetterConverter {

    /** HashMap for Romaji-to-Kana conversion (Japanese mode) */
    private static final HashMap<String, String> mRomkanTable = new HashMap<String, String>() {

        {
            put("la", "ァ");
            put("xa", "ァ");
            put("a", "ア");
            put("li", "ィ");
            put("lyi", "ィ");
            put("xi", "ィ");
            put("xyi", "ィ");
            put("i", "イ");
            put("yi", "イ");
            put("ye", "イェ");
            put("lu", "ゥ");
            put("xu", "ゥ");
            put("u", "ウ");
            put("whu", "ウ");
            put("wu", "ウ");
            put("wha", "ウァ");
            put("whi", "ウィ");
            put("wi", "ウィ");
            put("we", "ウェ");
            put("whe", "ウェ");
            put("who", "ウォ");
            put("le", "ェ");
            put("lye", "ェ");
            put("xe", "ェ");
            put("xye", "ェ");
            put("e", "エ");
            put("lo", "ォ");
            put("xo", "ォ");
            put("o", "オ");
            put("ca", "カ");
            put("ka", "カ");
            put("ga", "ガ");
            put("ki", "キ");
            put("kyi", "キィ");
            put("kye", "キェ");
            put("kya", "キャ");
            put("kyu", "キュ");
            put("kyo", "キョ");
            put("gi", "ギ");
            put("gyi", "ギィ");
            put("gye", "ギェ");
            put("gya", "ギャ");
            put("gyu", "ギュ");
            put("gyo", "ギョ");
            put("cu", "ク");
            put("ku", "ク");
            put("qu", "ク");
            put("kwa", "クァ");
            put("qa", "クァ");
            put("qwa", "クァ");
            put("qi", "クィ");
            put("qwi", "クィ");
            put("qyi", "クィ");
            put("qwu", "クゥ");
            put("qe", "クェ");
            put("qwe", "クェ");
            put("qye", "クェ");
            put("qo", "クォ");
            put("qwo", "クォ");
            put("qya", "クャ");
            put("qyu", "クュ");
            put("qyo", "クョ");
            put("gu", "グ");
            put("gwa", "グァ");
            put("gwi", "グィ");
            put("gwu", "グゥ");
            put("gwe", "グェ");
            put("gwo", "グォ");
            put("ke", "ケ");
            put("ge", "ゲ");
            put("co", "コ");
            put("ko", "コ");
            put("go", "ゴ");
            put("sa", "サ");
            put("za", "ザ");
            put("ci", "シ");
            put("shi", "シ");
            put("si", "シ");
            put("syi", "シィ");
            put("she", "シェ");
            put("sye", "シェ");
            put("sha", "シャ");
            put("sya", "シャ");
            put("shu", "シュ");
            put("syu", "シュ");
            put("sho", "ショ");
            put("syo", "ショ");
            put("ji", "ジ");
            put("zi", "ジ");
            put("jyi", "ジィ");
            put("zyi", "ジィ");
            put("je", "ジェ");
            put("jye", "ジェ");
            put("zye", "ジェ");
            put("ja", "ジャ");
            put("jya", "ジャ");
            put("zya", "ジャ");
            put("ju", "ジュ");
            put("jyu", "ジュ");
            put("zyu", "ジュ");
            put("jo", "ジョ");
            put("jyo", "ジョ");
            put("zyo", "ジョ");
            put("su", "ス");
            put("swa", "スァ");
            put("swi", "スィ");
            put("swu", "スゥ");
            put("swe", "スェ");
            put("swo", "スォ");
            put("zu", "ズ");
            put("ce", "セ");
            put("se", "セ");
            put("ze", "ゼ");
            put("so", "ソ");
            put("zo", "ゾ");
            put("ta", "タ");
            put("da", "ダ");
            put("chi", "チ");
            put("ti", "チ");
            put("cyi", "チィ");
            put("tyi", "チィ");
            put("che", "チェ");
            put("cye", "チェ");
            put("tye", "チェ");
            put("cha", "チャ");
            put("cya", "チャ");
            put("tya", "チャ");
            put("chu", "チュ");
            put("cyu", "チュ");
            put("tyu", "チュ");
            put("cho", "チョ");
            put("cyo", "チョ");
            put("tyo", "チョ");
            put("di", "ヂ");
            put("dyi", "ヂィ");
            put("dye", "ヂェ");
            put("dya", "ヂャ");
            put("dyu", "ヂュ");
            put("dyo", "ヂョ");
            put("ltsu", "ッ");
            put("ltu", "ッ");
            put("xtu", "ッ");
            put("", "ッ");
            put("tsu", "ツ");
            put("tu", "ツ");
            put("tsa", "ツァ");
            put("tsi", "ツィ");
            put("tse", "ツェ");
            put("tso", "ツォ");
            put("du", "ヅ");
            put("te", "テ");
            put("thi", "ティ");
            put("the", "テェ");
            put("tha", "テャ");
            put("thu", "テュ");
            put("tho", "テョ");
            put("de", "デ");
            put("dhi", "ディ");
            put("dhe", "デェ");
            put("dha", "デャ");
            put("dhu", "デュ");
            put("dho", "デョ");
            put("to", "ト");
            put("twa", "トァ");
            put("twi", "トィ");
            put("twu", "トゥ");
            put("twe", "トェ");
            put("two", "トォ");
            put("do", "ド");
            put("dwa", "ドァ");
            put("dwi", "ドィ");
            put("dwu", "ドゥ");
            put("dwe", "ドェ");
            put("dwo", "ドォ");
            put("na", "ナ");
            put("ni", "ニ");
            put("nyi", "ニィ");
            put("nye", "ニェ");
            put("nya", "ニャ");
            put("nyu", "ニュ");
            put("nyo", "ニョ");
            put("nu", "ヌ");
            put("ne", "ネ");
            put("no", "ノ");
            put("ha", "ハ");
            put("ba", "バ");
            put("pa", "パ");
            put("hi", "ヒ");
            put("hyi", "ヒィ");
            put("hye", "ヒェ");
            put("hya", "ヒャ");
            put("hyu", "ヒュ");
            put("hyo", "ヒョ");
            put("bi", "ビ");
            put("byi", "ビィ");
            put("bye", "ビェ");
            put("bya", "ビャ");
            put("byu", "ビュ");
            put("byo", "ビョ");
            put("pi", "ピ");
            put("pyi", "ピィ");
            put("pye", "ピェ");
            put("pya", "ピャ");
            put("pyu", "ピュ");
            put("pyo", "ピョ");
            put("fu", "フ");
            put("hu", "フ");
            put("fa", "ファ");
            put("fwa", "ファ");
            put("fi", "フィ");
            put("fwi", "フィ");
            put("fyi", "フィ");
            put("fwu", "フゥ");
            put("fe", "フェ");
            put("fwe", "フェ");
            put("fye", "フェ");
            put("fo", "フォ");
            put("fwo", "フォ");
            put("fya", "フャ");
            put("fyu", "フュ");
            put("fyo", "フョ");
            put("bu", "ブ");
            put("pu", "プ");
            put("he", "ヘ");
            put("be", "ベ");
            put("pe", "ペ");
            put("ho", "ホ");
            put("bo", "ボ");
            put("po", "ポ");
            put("ma", "マ");
            put("mi", "ミ");
            put("myi", "ミィ");
            put("mye", "ミェ");
            put("mya", "ミャ");
            put("myu", "ミュ");
            put("myo", "ミョ");
            put("mu", "ム");
            put("me", "メ");
            put("mo", "モ");
            put("lya", "ャ");
            put("xya", "ャ");
            put("ya", "ヤ");
            put("lyu", "ュ");
            put("xyu", "ュ");
            put("yu", "ユ");
            put("lyo", "ョ");
            put("xyo", "ョ");
            put("yo", "ヨ");
            put("ra", "ラ");
            put("ri", "リ");
            put("ryi", "リィ");
            put("rye", "リェ");
            put("rya", "リャ");
            put("ryu", "リュ");
            put("ryo", "リョ");
            put("ru", "ル");
            put("re", "レ");
            put("ro", "ロ");
            put("lwa", "ヮ");
            put("xwa", "ヮ");
            put("wa", "ワ");
            put("wo", "ヲ");
            put("nn", "ン");
            put("xn", "ン");
            put("vu", "ヴ");
            put("va", "ヴァ");
            put("vi", "ヴィ");
            put("vyi", "ヴィ");
            put("ve", "ヴェ");
            put("vye", "ヴェ");
            put("vo", "ヴォ");
            put("vya", "ヴャ");
            put("vyu", "ヴュ");
            put("vyo", "ヴョ");
            put("bb", "ッb");
            put("cc", "ッc");
            put("dd", "ッd");
            put("ff", "ッf");
            put("gg", "ッg");
            put("hh", "ッh");
            put("jj", "ッj");
            put("kk", "ッk");
            put("ll", "ッl");
            put("mm", "ッm");
            put("pp", "ッp");
            put("qq", "ッq");
            put("rr", "ッr");
            put("ss", "ッs");
            put("tt", "ッt");
            put("vv", "ッv");
            put("ww", "ッw");
            put("xx", "ッx");
            put("yy", "ッy");
            put("zz", "ッz");
            put("nb", "ンb");
            put("nc", "ンc");
            put("nd", "ンd");
            put("nf", "ンf");
            put("ng", "ンg");
            put("nh", "ンh");
            put("nj", "ンj");
            put("nk", "ンk");
            put("nm", "ンm");
            put("np", "ンp");
            put("nq", "ンq");
            put("nr", "ンr");
            put("ns", "ンs");
            put("nt", "ンt");
            put("nv", "ンv");
            put("nw", "ンw");
            put("nx", "ンx");
            put("nz", "ンz");
            put("nl", "ンl");
            put("-", "ー");
            put(".", "。");
            put(",", "、");
            put("?", "？");
            put("/", "・");
        }
    };

    /** @see LetterConverter#convert */
    public boolean convert(ComposingText text) {
        return convert(text, mRomkanTable);
    }

    /**
     * convert Romaji to Full Katakana
     *
     * @param text		The input/output text
     * @param table		HashMap for Romaji-to-Kana conversion
     * @return			{@code true} if conversion is compleated; {@code false} if not
     */
    public static boolean convert(ComposingText text, HashMap<String, String> table) {
        int cursor = text.getCursor(1);
        if (cursor <= 0) {
            return false;
        }
        StrSegment[] str = new StrSegment[3];
        int start = 2;
        str[2] = text.getStrSegment(ComposingText.LAYER1, cursor - 1);
        if (cursor >= 2) {
            str[1] = text.getStrSegment(ComposingText.LAYER1, cursor - 2);
            start = 1;
            if (cursor >= 3) {
                str[0] = text.getStrSegment(ComposingText.LAYER1, cursor - 3);
                start = 0;
            }
        }
        StringBuffer key = new StringBuffer();
        while (start < 3) {
            for (int i = start; i < 3; i++) {
                key.append(str[i].string);
            }
            boolean upper = Character.isUpperCase(key.charAt(key.length() - 1));
            String match = table.get(key.toString().toLowerCase());
            if (match != null) {
                if (upper) {
                    match = match.toUpperCase();
                }
                StrSegment[] out;
                if (match.length() == 1) {
                    out = new StrSegment[1];
                    out[0] = new StrSegment(match, str[start].from, str[2].to);
                    text.replaceStrSegment(ComposingText.LAYER1, out, 3 - start);
                } else {
                    out = new StrSegment[2];
                    out[0] = new StrSegment(match.substring(0, match.length() - 1), str[start].from, str[2].to - 1);
                    out[1] = new StrSegment(match.substring(match.length() - 1), str[2].to, str[2].to);
                    text.replaceStrSegment(ComposingText.LAYER1, out, 3 - start);
                }
                String regex = ".*[a-zA-Z]$";
                Pattern p = Pattern.compile(regex);
                Matcher m = p.matcher(text.toString(ComposingText.LAYER1));
                if (m.matches()) {
                    text.moveCursor(ComposingText.LAYER1, -1);
                }
                return true;
            }
            start++;
            key.delete(0, key.length());
        }
        return false;
    }

    /** @see LetterConverter#setPreferences */
    public void setPreferences(SharedPreferences pref) {
    }
}
