package net.sourceforge.numbertrans.languages.arabic;

import java.util.ArrayList;
import net.sourceforge.numbertrans.framework.parser.CharacterMultiMappedCardinalParser;

public class HinduArabicCardinalParser extends CharacterMultiMappedCardinalParser {

    public static final char[] OTTOMAN_TURKISH_DIGITS = { '٠', '١', '٢', '٣', '٤', '٥', '٦', '٧', '٨', '٩' };

    public static final char[] PERSIAN_URDU_DIGITS = { '۰', '۱', '۲', '۳', '۴', '۵', '۶', '۷', '۸', '۹' };

    public static final ArrayList<char[]> DIGIT_SETS = new ArrayList<char[]>(2);

    static {
        DIGIT_SETS.add(OTTOMAN_TURKISH_DIGITS);
        DIGIT_SETS.add(PERSIAN_URDU_DIGITS);
    }

    /**
         * Create a new <code>HinduArabicCardinalParser</code> object.
         */
    public HinduArabicCardinalParser() {
        super(DIGIT_SETS);
    }
}
