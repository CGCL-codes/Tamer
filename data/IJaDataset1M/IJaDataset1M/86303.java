package cunei.encoding;

public class Buckwalter {

    private static char UTF8[] = { 'ء', 'آ', 'أ', 'ؤ', 'إ', 'ئ', 'ا', 'ب', 'ة', 'ت', 'ث', 'ج', 'ح', 'خ', 'د', 'ذ', 'ر', 'ز', 'س', 'ش', 'ص', 'ض', 'ط', 'ظ', 'ع', 'غ', 'ـ', 'ف', 'ق', 'ك', 'ل', 'م', 'ن', 'ه', 'و', 'ى', 'ي', 'ً', 'ٌ', 'ٍ', 'َ', 'ُ', 'ِ', 'ّ', 'ْ', 'ٰ', 'ٱ', 'پ', 'چ', 'ڤ', 'گ' };

    private static char BUCKWALTER[] = { '\'', '|', '>', '&', '<', '}', 'A', 'b', 'p', 't', 'v', 'j', 'H', 'x', 'd', '*', 'r', 'z', 's', '$', 'S', 'D', 'T', 'Z', 'E', 'g', '_', 'f', 'q', 'k', 'l', 'm', 'n', 'h', 'w', 'Y', 'y', 'F', 'N', 'K', 'a', 'u', 'i', '~', 'o', '`', '{', 'P', 'J', 'V', 'G' };

    public static String decode(String text) {
        for (int j = 0; j < BUCKWALTER.length; j++) text = text.replace(BUCKWALTER[j], UTF8[j]);
        return text;
    }

    public static String encode(String text) {
        for (int j = 0; j < UTF8.length; j++) text = text.replace(UTF8[j], BUCKWALTER[j]);
        return text;
    }
}
