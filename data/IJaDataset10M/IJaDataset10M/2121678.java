package com.googlecode.openwnn.legacy.JAJP;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import com.googlecode.openwnn.legacy.WnnDictionary;
import com.googlecode.openwnn.legacy.WnnPOS;
import com.googlecode.openwnn.legacy.WnnWord;

/**
 * The EISU-KANA converter class for Japanese IME.
 *
 * @author Copyright (C) 2009 OMRON SOFTWARE CO., LTD.  All Rights Reserved.
 */
public class KanaConverter {

    /** Conversion rule for half-width numeric */
    private static final HashMap<String, String> mHalfNumericMap = new HashMap<String, String>() {

        {
            put("あ", "1");
            put("い", "11");
            put("う", "111");
            put("え", "1111");
            put("お", "11111");
            put("ぁ", "111111");
            put("ぃ", "1111111");
            put("ぅ", "11111111");
            put("ぇ", "111111111");
            put("ぉ", "1111111111");
            put("か", "2");
            put("き", "22");
            put("く", "222");
            put("け", "2222");
            put("こ", "22222");
            put("さ", "3");
            put("し", "33");
            put("す", "333");
            put("せ", "3333");
            put("そ", "33333");
            put("た", "4");
            put("ち", "44");
            put("つ", "444");
            put("て", "4444");
            put("と", "44444");
            put("っ", "444444");
            put("な", "5");
            put("に", "55");
            put("ぬ", "555");
            put("ね", "5555");
            put("の", "55555");
            put("は", "6");
            put("ひ", "66");
            put("ふ", "666");
            put("へ", "6666");
            put("ほ", "66666");
            put("ま", "7");
            put("み", "77");
            put("む", "777");
            put("め", "7777");
            put("も", "77777");
            put("や", "8");
            put("ゆ", "88");
            put("よ", "888");
            put("ゃ", "8888");
            put("ゅ", "88888");
            put("ょ", "888888");
            put("ら", "9");
            put("り", "99");
            put("る", "999");
            put("れ", "9999");
            put("ろ", "99999");
            put("わ", "0");
            put("を", "00");
            put("ん", "000");
            put("ゎ", "0000");
            put("ー", "00000");
        }
    };

    /** Conversion rule for full-width numeric */
    private static final HashMap<String, String> mFullNumericMap = new HashMap<String, String>() {

        {
            put("あ", "１");
            put("い", "１１");
            put("う", "１１１");
            put("え", "１１１１");
            put("お", "１１１１１");
            put("ぁ", "１１１１１１");
            put("ぃ", "１１１１１１１");
            put("ぅ", "１１１１１１１１");
            put("ぇ", "１１１１１１１１１");
            put("ぉ", "１１１１１１１１１１");
            put("か", "２");
            put("き", "２２");
            put("く", "２２２");
            put("け", "２２２２");
            put("こ", "２２２２２");
            put("さ", "３");
            put("し", "３３");
            put("す", "３３３");
            put("せ", "３３３３");
            put("そ", "３３３３３");
            put("た", "４");
            put("ち", "４４");
            put("つ", "４４４");
            put("て", "４４４４");
            put("と", "４４４４４");
            put("っ", "４４４４４４");
            put("な", "５");
            put("に", "５５");
            put("ぬ", "５５５");
            put("ね", "５５５５");
            put("の", "５５５５５");
            put("は", "６");
            put("ひ", "６６");
            put("ふ", "６６６");
            put("へ", "６６６６");
            put("ほ", "６６６６６");
            put("ま", "７");
            put("み", "７７");
            put("む", "７７７");
            put("め", "７７７７");
            put("も", "７７７７７");
            put("や", "８");
            put("ゆ", "８８");
            put("よ", "８８８");
            put("ゃ", "８８８８");
            put("ゅ", "８８８８８");
            put("ょ", "８８８８８８");
            put("ら", "９");
            put("り", "９９");
            put("る", "９９９");
            put("れ", "９９９９");
            put("ろ", "９９９９９");
            put("わ", "０");
            put("を", "００");
            put("ん", "０００");
            put("ゎ", "００００");
            put("ー", "０００００");
        }
    };

    /** Conversion rule for half-width Katakana */
    private static final HashMap<String, String> mHalfKatakanaMap = new HashMap<String, String>() {

        {
            put("あ", "ｱ");
            put("い", "ｲ");
            put("う", "ｳ");
            put("え", "ｴ");
            put("お", "ｵ");
            put("ぁ", "ｧ");
            put("ぃ", "ｨ");
            put("ぅ", "ｩ");
            put("ぇ", "ｪ");
            put("ぉ", "ｫ");
            put("ヴぁ", "ｳﾞｧ");
            put("ヴぃ", "ｳﾞｨ");
            put("ヴ", "ｳﾞ");
            put("ヴぇ", "ｳﾞｪ");
            put("ヴぉ", "ｳﾞｫ");
            put("か", "ｶ");
            put("き", "ｷ");
            put("く", "ｸ");
            put("け", "ｹ");
            put("こ", "ｺ");
            put("が", "ｶﾞ");
            put("ぎ", "ｷﾞ");
            put("ぐ", "ｸﾞ");
            put("げ", "ｹﾞ");
            put("ご", "ｺﾞ");
            put("さ", "ｻ");
            put("し", "ｼ");
            put("す", "ｽ");
            put("せ", "ｾ");
            put("そ", "ｿ");
            put("ざ", "ｻﾞ");
            put("じ", "ｼﾞ");
            put("ず", "ｽﾞ");
            put("ぜ", "ｾﾞ");
            put("ぞ", "ｿﾞ");
            put("た", "ﾀ");
            put("ち", "ﾁ");
            put("つ", "ﾂ");
            put("て", "ﾃ");
            put("と", "ﾄ");
            put("っ", "ｯ");
            put("だ", "ﾀﾞ");
            put("ぢ", "ﾁﾞ");
            put("づ", "ﾂﾞ");
            put("で", "ﾃﾞ");
            put("ど", "ﾄﾞ");
            put("な", "ﾅ");
            put("に", "ﾆ");
            put("ぬ", "ﾇ");
            put("ね", "ﾈ");
            put("の", "ﾉ");
            put("は", "ﾊ");
            put("ひ", "ﾋ");
            put("ふ", "ﾌ");
            put("へ", "ﾍ");
            put("ほ", "ﾎ");
            put("ば", "ﾊﾞ");
            put("び", "ﾋﾞ");
            put("ぶ", "ﾌﾞ");
            put("べ", "ﾍﾞ");
            put("ぼ", "ﾎﾞ");
            put("ぱ", "ﾊﾟ");
            put("ぴ", "ﾋﾟ");
            put("ぷ", "ﾌﾟ");
            put("ぺ", "ﾍﾟ");
            put("ぽ", "ﾎﾟ");
            put("ま", "ﾏ");
            put("み", "ﾐ");
            put("む", "ﾑ");
            put("め", "ﾒ");
            put("も", "ﾓ");
            put("や", "ﾔ");
            put("ゆ", "ﾕ");
            put("よ", "ﾖ");
            put("ゃ", "ｬ");
            put("ゅ", "ｭ");
            put("ょ", "ｮ");
            put("ら", "ﾗ");
            put("り", "ﾘ");
            put("る", "ﾙ");
            put("れ", "ﾚ");
            put("ろ", "ﾛ");
            put("わ", "ﾜ");
            put("を", "ｦ");
            put("ん", "ﾝ");
            put("ゎ", "ﾜ");
            put("ー", "ｰ");
        }
    };

    /** Conversion rule for full-width Katakana */
    private static final HashMap<String, String> mFullKatakanaMap = new HashMap<String, String>() {

        {
            put("あ", "ア");
            put("い", "イ");
            put("う", "ウ");
            put("え", "エ");
            put("お", "オ");
            put("ぁ", "ァ");
            put("ぃ", "ィ");
            put("ぅ", "ゥ");
            put("ぇ", "ェ");
            put("ぉ", "ォ");
            put("ヴぁ", "ヴァ");
            put("ヴぃ", "ヴィ");
            put("ヴ", "ヴ");
            put("ヴぇ", "ヴェ");
            put("ヴぉ", "ヴォ");
            put("か", "カ");
            put("き", "キ");
            put("く", "ク");
            put("け", "ケ");
            put("こ", "コ");
            put("が", "ガ");
            put("ぎ", "ギ");
            put("ぐ", "グ");
            put("げ", "ゲ");
            put("ご", "ゴ");
            put("さ", "サ");
            put("し", "シ");
            put("す", "ス");
            put("せ", "セ");
            put("そ", "ソ");
            put("ざ", "ザ");
            put("じ", "ジ");
            put("ず", "ズ");
            put("ぜ", "ゼ");
            put("ぞ", "ゾ");
            put("た", "タ");
            put("ち", "チ");
            put("つ", "ツ");
            put("て", "テ");
            put("と", "ト");
            put("っ", "ッ");
            put("だ", "ダ");
            put("ぢ", "ヂ");
            put("づ", "ヅ");
            put("で", "デ");
            put("ど", "ド");
            put("な", "ナ");
            put("に", "ニ");
            put("ぬ", "ヌ");
            put("ね", "ネ");
            put("の", "ノ");
            put("は", "ハ");
            put("ひ", "ヒ");
            put("ふ", "フ");
            put("へ", "ヘ");
            put("ほ", "ホ");
            put("ば", "バ");
            put("び", "ビ");
            put("ぶ", "ブ");
            put("べ", "ベ");
            put("ぼ", "ボ");
            put("ぱ", "パ");
            put("ぴ", "ピ");
            put("ぷ", "プ");
            put("ぺ", "ペ");
            put("ぽ", "ポ");
            put("ま", "マ");
            put("み", "ミ");
            put("む", "ム");
            put("め", "メ");
            put("も", "モ");
            put("や", "ヤ");
            put("ゆ", "ユ");
            put("よ", "ヨ");
            put("ゃ", "ャ");
            put("ゅ", "ュ");
            put("ょ", "ョ");
            put("ら", "ラ");
            put("り", "リ");
            put("る", "ル");
            put("れ", "レ");
            put("ろ", "ロ");
            put("わ", "ワ");
            put("を", "ヲ");
            put("ん", "ン");
            put("ゎ", "ヮ");
            put("ー", "ー");
        }
    };

    /** Conversion rule for half-width alphabet */
    private static final HashMap<String, String> mHalfAlphabetMap = new HashMap<String, String>() {

        {
            put("あ", ".");
            put("い", "@");
            put("う", "-");
            put("え", "_");
            put("お", "/");
            put("ぁ", ":");
            put("ぃ", "~");
            put("か", "A");
            put("き", "B");
            put("く", "C");
            put("さ", "D");
            put("し", "E");
            put("す", "F");
            put("た", "G");
            put("ち", "H");
            put("つ", "I");
            put("な", "J");
            put("に", "K");
            put("ぬ", "L");
            put("は", "M");
            put("ひ", "N");
            put("ふ", "O");
            put("ま", "P");
            put("み", "Q");
            put("む", "R");
            put("め", "S");
            put("や", "T");
            put("ゆ", "U");
            put("よ", "V");
            put("ら", "W");
            put("り", "X");
            put("る", "Y");
            put("れ", "Z");
            put("わ", "-");
        }
    };

    /** Conversion rule for full-width alphabet */
    private static final HashMap<String, String> mFullAlphabetMap = new HashMap<String, String>() {

        {
            put("あ", "．");
            put("い", "＠");
            put("う", "ー");
            put("え", "＿");
            put("お", "／");
            put("ぁ", "：");
            put("ぃ", "〜");
            put("か", "Ａ");
            put("き", "Ｂ");
            put("く", "Ｃ");
            put("さ", "Ｄ");
            put("し", "Ｅ");
            put("す", "Ｆ");
            put("た", "Ｇ");
            put("ち", "Ｈ");
            put("つ", "Ｉ");
            put("な", "Ｊ");
            put("に", "Ｋ");
            put("ぬ", "Ｌ");
            put("は", "Ｍ");
            put("ひ", "Ｎ");
            put("ふ", "Ｏ");
            put("ま", "Ｐ");
            put("み", "Ｑ");
            put("む", "Ｒ");
            put("め", "Ｓ");
            put("や", "Ｔ");
            put("ゆ", "Ｕ");
            put("よ", "Ｖ");
            put("ら", "Ｗ");
            put("り", "Ｘ");
            put("る", "Ｙ");
            put("れ", "Ｚ");
            put("わ", "ー");
        }
    };

    /** Conversion rule for full-width alphabet (QWERTY mode) */
    private static final HashMap<String, String> mFullAlphabetMapQwety = new HashMap<String, String>() {

        {
            put("a", "ａ");
            put("b", "ｂ");
            put("c", "ｃ");
            put("d", "ｄ");
            put("e", "ｅ");
            put("f", "ｆ");
            put("g", "ｇ");
            put("h", "ｈ");
            put("i", "ｉ");
            put("j", "ｊ");
            put("k", "ｋ");
            put("l", "ｌ");
            put("m", "ｍ");
            put("n", "ｎ");
            put("o", "ｏ");
            put("p", "ｐ");
            put("q", "ｑ");
            put("r", "ｒ");
            put("s", "ｓ");
            put("t", "ｔ");
            put("u", "ｕ");
            put("v", "ｖ");
            put("w", "ｗ");
            put("x", "ｘ");
            put("y", "ｙ");
            put("z", "ｚ");
            put("A", "Ａ");
            put("B", "Ｂ");
            put("C", "Ｃ");
            put("D", "Ｄ");
            put("E", "Ｅ");
            put("F", "Ｆ");
            put("G", "Ｇ");
            put("H", "Ｈ");
            put("I", "Ｉ");
            put("J", "Ｊ");
            put("K", "Ｋ");
            put("L", "Ｌ");
            put("M", "Ｍ");
            put("N", "Ｎ");
            put("O", "Ｏ");
            put("P", "Ｐ");
            put("Q", "Ｑ");
            put("R", "Ｒ");
            put("S", "Ｓ");
            put("T", "Ｔ");
            put("U", "Ｕ");
            put("V", "Ｖ");
            put("W", "Ｗ");
            put("X", "Ｘ");
            put("Y", "Ｙ");
            put("Z", "Ｚ");
        }
    };

    /** Decimal format using comma */
    private static final DecimalFormat mFormat = new DecimalFormat("###,###");

    /** List of the generated candidates */
    private List<WnnWord> mAddCandidateList;

    /** Work area for generating string */
    private StringBuffer mStringBuff;

    /** part of speech (default) */
    private WnnPOS mPosDefault;

    /** part of speech (number) */
    private WnnPOS mPosNumber;

    /** part of speech (symbol) */
    private WnnPOS mPosSymbol;

    /**
     * Constructor
     */
    public KanaConverter() {
        mAddCandidateList = new ArrayList<WnnWord>();
        mStringBuff = new StringBuffer();
    }

    /**
     * Set The dictionary.
     * <br>
     * {@link KanaConverter} gets part-of-speech tags from the dictionary.
     * 
     * @param dict  The dictionary
     */
    public void setDictionary(WnnDictionary dict) {
        mPosDefault = dict.getPOS(WnnDictionary.POS_TYPE_MEISI);
        mPosNumber = dict.getPOS(WnnDictionary.POS_TYPE_SUUJI);
        mPosSymbol = dict.getPOS(WnnDictionary.POS_TYPE_KIGOU);
    }

    /**
     * Create the pseudo candidate list
     * <br>
     * @param inputHiragana     The input string (Hiragana)
     * @param inputRomaji       The input string (Romaji)
     * @param keyBoardMode      The mode of keyboard
     * @return                  The candidate list
     */
    public List<WnnWord> createPseudoCandidateList(String inputHiragana, String inputRomaji, int keyBoardMode) {
        List<WnnWord> list = mAddCandidateList;
        list.clear();
        if (inputHiragana.length() == 0) {
            return list;
        }
        list.add(new WnnWord(inputHiragana, inputHiragana));
        if (createCandidateString(inputHiragana, mFullKatakanaMap, mStringBuff)) {
            list.add(new WnnWord(mStringBuff.toString(), inputHiragana, mPosDefault));
        }
        if (createCandidateString(inputHiragana, mHalfKatakanaMap, mStringBuff)) {
            list.add(new WnnWord(mStringBuff.toString(), inputHiragana, mPosDefault));
        }
        if (keyBoardMode == OpenWnnEngineJAJP.KEYBOARD_QWERTY) {
            createPseudoCandidateListForQwerty(inputHiragana, inputRomaji);
        } else {
            if (createCandidateString(inputHiragana, mHalfNumericMap, mStringBuff)) {
                String convHanSuuji = mStringBuff.toString();
                String convNumComma = convertNumber(convHanSuuji);
                list.add(new WnnWord(convHanSuuji, inputHiragana, mPosNumber));
                if (convNumComma != null) {
                    list.add(new WnnWord(convNumComma, inputHiragana, mPosNumber));
                }
            }
            if (createCandidateString(inputHiragana, mFullNumericMap, mStringBuff)) {
                list.add(new WnnWord(mStringBuff.toString(), inputHiragana, mPosNumber));
            }
            if (createCandidateString(inputHiragana, mHalfAlphabetMap, mStringBuff)) {
                String convHanEiji = mStringBuff.toString();
                String convHanEijiLower = convHanEiji.toLowerCase();
                list.add(new WnnWord(convHanEijiLower, inputHiragana, mPosSymbol));
                list.add(new WnnWord(convertCaps(convHanEijiLower), inputHiragana, mPosSymbol));
                list.add(new WnnWord(convHanEiji, inputHiragana, mPosSymbol));
            }
            if (createCandidateString(inputHiragana, mFullAlphabetMap, mStringBuff)) {
                String convZenEiji = mStringBuff.toString();
                String convZenEijiLower = convZenEiji.toLowerCase(Locale.JAPAN);
                list.add(new WnnWord(convZenEijiLower, inputHiragana, mPosSymbol));
                list.add(new WnnWord(convertCaps(convZenEijiLower), inputHiragana, mPosSymbol));
                list.add(new WnnWord(convZenEiji, inputHiragana, mPosSymbol));
            }
        }
        return list;
    }

    /**
     * Create the pseudo candidate list for Qwerty keyboard
     * <br>
     * @param inputHiragana     The input string (Hiragana)
     * @param inputRomaji       The input string (Romaji)
     */
    private void createPseudoCandidateListForQwerty(String inputHiragana, String inputRomaji) {
        List<WnnWord> list = mAddCandidateList;
        String convHanEijiLower = inputRomaji.toLowerCase();
        list.add(new WnnWord(inputRomaji, inputHiragana, mPosDefault));
        list.add(new WnnWord(convHanEijiLower, inputHiragana, mPosSymbol));
        list.add(new WnnWord(convertCaps(convHanEijiLower), inputHiragana, mPosSymbol));
        list.add(new WnnWord(inputRomaji.toUpperCase(), inputHiragana, mPosSymbol));
        if (createCandidateString(inputRomaji, mFullAlphabetMapQwety, mStringBuff)) {
            String convZenEiji = mStringBuff.toString();
            String convZenEijiLower = convZenEiji.toLowerCase(Locale.JAPAN);
            list.add(new WnnWord(convZenEiji, inputHiragana, mPosSymbol));
            list.add(new WnnWord(convZenEijiLower, inputHiragana, mPosSymbol));
            list.add(new WnnWord(convertCaps(convZenEijiLower), inputHiragana, mPosSymbol));
            list.add(new WnnWord(convZenEiji.toUpperCase(Locale.JAPAN), inputHiragana, mPosSymbol));
        }
    }

    /**
     * Create the candidate string
     * <br>
     * @param input     The input string
     * @param map       The hash map
     * @param outBuf    The output string
     * @return          {@code true} if success
     */
    private boolean createCandidateString(String input, HashMap<String, String> map, StringBuffer outBuf) {
        if (outBuf.length() > 0) {
            outBuf.delete(0, outBuf.length());
        }
        for (int index = 0; index < input.length(); index++) {
            String convChar = map.get(input.substring(index, index + 1));
            if (convChar == null) {
                return false;
            }
            outBuf.append(convChar);
        }
        return true;
    }

    /**
     * Convert into both small and capital letter
     * <br>
     * @param moji  The input string
     * @return      The converted string
     */
    private String convertCaps(String moji) {
        String tmp = "";
        if (moji != null && moji.length() > 0) {
            tmp = moji.substring(0, 1).toUpperCase(Locale.JAPAN) + moji.substring(1).toLowerCase(Locale.JAPAN);
        }
        return tmp;
    }

    /**
     * Convert the numeric into formatted string
     * <br>
     * @param numComma  The value
     * @return          {@code true} if success
     */
    private String convertNumber(String numComma) {
        try {
            return mFormat.format(Double.parseDouble(numComma));
        } catch (NumberFormatException e) {
            return null;
        }
    }
}
