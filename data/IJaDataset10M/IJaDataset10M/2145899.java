package com.googlecode.openwnn.legacy.JAJP;

import java.util.HashMap;
import com.googlecode.openwnn.legacy.*;
import android.content.SharedPreferences;

/**
 * The Romaji to Hiragana converter class for Japanese IME.
 *
 * @author Copyright (C) 2009 OMRON SOFTWARE CO., LTD.  All Rights Reserved.
 */
public class Romkan implements LetterConverter {

    /** HashMap for Romaji-to-Kana conversion (Japanese mode) */
    private static final HashMap<String, String> romkanTable = new HashMap<String, String>() {

        {
            put("la", "ぁ");
            put("xa", "ぁ");
            put("a", "あ");
            put("li", "ぃ");
            put("lyi", "ぃ");
            put("xi", "ぃ");
            put("xyi", "ぃ");
            put("i", "い");
            put("yi", "い");
            put("ye", "いぇ");
            put("lu", "ぅ");
            put("xu", "ぅ");
            put("u", "う");
            put("whu", "う");
            put("wu", "う");
            put("wha", "うぁ");
            put("whi", "うぃ");
            put("wi", "うぃ");
            put("we", "うぇ");
            put("whe", "うぇ");
            put("who", "うぉ");
            put("le", "ぇ");
            put("lye", "ぇ");
            put("xe", "ぇ");
            put("xye", "ぇ");
            put("e", "え");
            put("lo", "ぉ");
            put("xo", "ぉ");
            put("o", "お");
            put("ca", "か");
            put("ka", "か");
            put("ga", "が");
            put("ki", "き");
            put("kyi", "きぃ");
            put("kye", "きぇ");
            put("kya", "きゃ");
            put("kyu", "きゅ");
            put("kyo", "きょ");
            put("gi", "ぎ");
            put("gyi", "ぎぃ");
            put("gye", "ぎぇ");
            put("gya", "ぎゃ");
            put("gyu", "ぎゅ");
            put("gyo", "ぎょ");
            put("cu", "く");
            put("ku", "く");
            put("qu", "く");
            put("kwa", "くぁ");
            put("qa", "くぁ");
            put("qwa", "くぁ");
            put("qi", "くぃ");
            put("qwi", "くぃ");
            put("qyi", "くぃ");
            put("qwu", "くぅ");
            put("qe", "くぇ");
            put("qwe", "くぇ");
            put("qye", "くぇ");
            put("qo", "くぉ");
            put("qwo", "くぉ");
            put("qya", "くゃ");
            put("qyu", "くゅ");
            put("qyo", "くょ");
            put("gu", "ぐ");
            put("gwa", "ぐぁ");
            put("gwi", "ぐぃ");
            put("gwu", "ぐぅ");
            put("gwe", "ぐぇ");
            put("gwo", "ぐぉ");
            put("ke", "け");
            put("ge", "げ");
            put("co", "こ");
            put("ko", "こ");
            put("go", "ご");
            put("sa", "さ");
            put("za", "ざ");
            put("ci", "し");
            put("shi", "し");
            put("si", "し");
            put("syi", "しぃ");
            put("she", "しぇ");
            put("sye", "しぇ");
            put("sha", "しゃ");
            put("sya", "しゃ");
            put("shu", "しゅ");
            put("syu", "しゅ");
            put("sho", "しょ");
            put("syo", "しょ");
            put("ji", "じ");
            put("zi", "じ");
            put("jyi", "じぃ");
            put("zyi", "じぃ");
            put("je", "じぇ");
            put("jye", "じぇ");
            put("zye", "じぇ");
            put("ja", "じゃ");
            put("jya", "じゃ");
            put("zya", "じゃ");
            put("ju", "じゅ");
            put("jyu", "じゅ");
            put("zyu", "じゅ");
            put("jo", "じょ");
            put("jyo", "じょ");
            put("zyo", "じょ");
            put("su", "す");
            put("swa", "すぁ");
            put("swi", "すぃ");
            put("swu", "すぅ");
            put("swe", "すぇ");
            put("swo", "すぉ");
            put("zu", "ず");
            put("ce", "せ");
            put("se", "せ");
            put("ze", "ぜ");
            put("so", "そ");
            put("zo", "ぞ");
            put("ta", "た");
            put("da", "だ");
            put("chi", "ち");
            put("ti", "ち");
            put("cyi", "ちぃ");
            put("tyi", "ちぃ");
            put("che", "ちぇ");
            put("cye", "ちぇ");
            put("tye", "ちぇ");
            put("cha", "ちゃ");
            put("cya", "ちゃ");
            put("tya", "ちゃ");
            put("chu", "ちゅ");
            put("cyu", "ちゅ");
            put("tyu", "ちゅ");
            put("cho", "ちょ");
            put("cyo", "ちょ");
            put("tyo", "ちょ");
            put("di", "ぢ");
            put("dyi", "ぢぃ");
            put("dye", "ぢぇ");
            put("dya", "ぢゃ");
            put("dyu", "ぢゅ");
            put("dyo", "ぢょ");
            put("ltsu", "っ");
            put("ltu", "っ");
            put("xtu", "っ");
            put("", "っ");
            put("tsu", "つ");
            put("tu", "つ");
            put("tsa", "つぁ");
            put("tsi", "つぃ");
            put("tse", "つぇ");
            put("tso", "つぉ");
            put("du", "づ");
            put("te", "て");
            put("thi", "てぃ");
            put("the", "てぇ");
            put("tha", "てゃ");
            put("thu", "てゅ");
            put("tho", "てょ");
            put("de", "で");
            put("dhi", "でぃ");
            put("dhe", "でぇ");
            put("dha", "でゃ");
            put("dhu", "でゅ");
            put("dho", "でょ");
            put("to", "と");
            put("twa", "とぁ");
            put("twi", "とぃ");
            put("twu", "とぅ");
            put("twe", "とぇ");
            put("two", "とぉ");
            put("do", "ど");
            put("dwa", "どぁ");
            put("dwi", "どぃ");
            put("dwu", "どぅ");
            put("dwe", "どぇ");
            put("dwo", "どぉ");
            put("na", "な");
            put("ni", "に");
            put("nyi", "にぃ");
            put("nye", "にぇ");
            put("nya", "にゃ");
            put("nyu", "にゅ");
            put("nyo", "にょ");
            put("nu", "ぬ");
            put("ne", "ね");
            put("no", "の");
            put("ha", "は");
            put("ba", "ば");
            put("pa", "ぱ");
            put("hi", "ひ");
            put("hyi", "ひぃ");
            put("hye", "ひぇ");
            put("hya", "ひゃ");
            put("hyu", "ひゅ");
            put("hyo", "ひょ");
            put("bi", "び");
            put("byi", "びぃ");
            put("bye", "びぇ");
            put("bya", "びゃ");
            put("byu", "びゅ");
            put("byo", "びょ");
            put("pi", "ぴ");
            put("pyi", "ぴぃ");
            put("pye", "ぴぇ");
            put("pya", "ぴゃ");
            put("pyu", "ぴゅ");
            put("pyo", "ぴょ");
            put("fu", "ふ");
            put("hu", "ふ");
            put("fa", "ふぁ");
            put("fwa", "ふぁ");
            put("fi", "ふぃ");
            put("fwi", "ふぃ");
            put("fyi", "ふぃ");
            put("fwu", "ふぅ");
            put("fe", "ふぇ");
            put("fwe", "ふぇ");
            put("fye", "ふぇ");
            put("fo", "ふぉ");
            put("fwo", "ふぉ");
            put("fya", "ふゃ");
            put("fyu", "ふゅ");
            put("fyo", "ふょ");
            put("bu", "ぶ");
            put("pu", "ぷ");
            put("he", "へ");
            put("be", "べ");
            put("pe", "ぺ");
            put("ho", "ほ");
            put("bo", "ぼ");
            put("po", "ぽ");
            put("ma", "ま");
            put("mi", "み");
            put("myi", "みぃ");
            put("mye", "みぇ");
            put("mya", "みゃ");
            put("myu", "みゅ");
            put("myo", "みょ");
            put("mu", "む");
            put("me", "め");
            put("mo", "も");
            put("lya", "ゃ");
            put("xya", "ゃ");
            put("ya", "や");
            put("lyu", "ゅ");
            put("xyu", "ゅ");
            put("yu", "ゆ");
            put("lyo", "ょ");
            put("xyo", "ょ");
            put("yo", "よ");
            put("ra", "ら");
            put("ri", "り");
            put("ryi", "りぃ");
            put("rye", "りぇ");
            put("rya", "りゃ");
            put("ryu", "りゅ");
            put("ryo", "りょ");
            put("ru", "る");
            put("re", "れ");
            put("ro", "ろ");
            put("lwa", "ゎ");
            put("xwa", "ゎ");
            put("wa", "わ");
            put("wo", "を");
            put("nn", "ん");
            put("xn", "ん");
            put("vu", "ヴ");
            put("va", "ヴぁ");
            put("vi", "ヴぃ");
            put("vyi", "ヴぃ");
            put("ve", "ヴぇ");
            put("vye", "ヴぇ");
            put("vo", "ヴぉ");
            put("vya", "ヴゃ");
            put("vyu", "ヴゅ");
            put("vyo", "ヴょ");
            put("bb", "っb");
            put("cc", "っc");
            put("dd", "っd");
            put("ff", "っf");
            put("gg", "っg");
            put("hh", "っh");
            put("jj", "っj");
            put("kk", "っk");
            put("ll", "っl");
            put("mm", "っm");
            put("pp", "っp");
            put("qq", "っq");
            put("rr", "っr");
            put("ss", "っs");
            put("tt", "っt");
            put("vv", "っv");
            put("ww", "っw");
            put("xx", "っx");
            put("yy", "っy");
            put("zz", "っz");
            put("nb", "んb");
            put("nc", "んc");
            put("nd", "んd");
            put("nf", "んf");
            put("ng", "んg");
            put("nh", "んh");
            put("nj", "んj");
            put("nk", "んk");
            put("nm", "んm");
            put("np", "んp");
            put("nq", "んq");
            put("nr", "んr");
            put("ns", "んs");
            put("nt", "んt");
            put("nv", "んv");
            put("nw", "んw");
            put("nx", "んx");
            put("nz", "んz");
            put("nl", "んl");
            put("-", "ー");
            put(".", "。");
            put(",", "、");
            put("?", "？");
            put("/", "・");
            put("@", "＠");
            put("#", "＃");
            put("%", "％");
            put("&", "＆");
            put("*", "＊");
            put("+", "＋");
            put("=", "＝");
            put("(", "（");
            put(")", "）");
            put("~", "～");
            put("\"", "＂");
            put("'", "＇");
            put(":", "：");
            put(";", "；");
            put("!", "！");
            put("^", "＾");
            put("¥", "￥");
            put("$", "＄");
            put("[", "「");
            put("]", "」");
            put("_", "＿");
            put("{", "｛");
            put("}", "｝");
            put("`", "｀");
            put("<", "＜");
            put(">", "＞");
            put("\\", "＼");
            put("|", "｜");
            put("1", "１");
            put("2", "２");
            put("3", "３");
            put("4", "４");
            put("5", "５");
            put("6", "６");
            put("7", "７");
            put("8", "８");
            put("9", "９");
            put("0", "０");
        }
    };

    /** @see LetterConverter#convert */
    public boolean convert(ComposingText text) {
        int cursor = text.getCursor(1);
        if (cursor <= 0) {
            return false;
        }
        StrSegment[] str = new StrSegment[3];
        int start = 2;
        str[2] = text.getStrSegment(1, cursor - 1);
        if (cursor >= 2) {
            str[1] = text.getStrSegment(1, cursor - 2);
            start = 1;
            if (cursor >= 3) {
                str[0] = text.getStrSegment(1, cursor - 3);
                start = 0;
            }
        }
        StringBuffer key = new StringBuffer();
        while (start < 3) {
            for (int i = start; i < 3; i++) {
                key.append(str[i].string);
            }
            boolean upper = Character.isUpperCase(key.charAt(key.length() - 1));
            String match = Romkan.romkanTable.get(key.toString().toLowerCase());
            if (match != null) {
                if (upper) {
                    match = match.toUpperCase();
                }
                StrSegment[] out;
                if (match.length() == 1) {
                    out = new StrSegment[1];
                    out[0] = new StrSegment(match, str[start].from, str[2].to);
                    text.replaceStrSegment(1, out, 3 - start);
                } else {
                    out = new StrSegment[2];
                    out[0] = new StrSegment(match.substring(0, match.length() - 1), str[start].from, str[2].to - 1);
                    out[1] = new StrSegment(match.substring(match.length() - 1), str[2].to, str[2].to);
                    text.replaceStrSegment(1, out, 3 - start);
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