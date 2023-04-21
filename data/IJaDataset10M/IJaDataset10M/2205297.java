package nl.knaw.dans.common.dataperfect;

import java.text.CharacterIterator;
import java.text.StringCharacterIterator;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Represents a number format picture. This class checks that the picture adheres to the syntax for
 * number format pictures and provides an iterator to iterate over the mask characters. The mask
 * characters in the picture are those that actually represent the position of a resulting
 * character.
 *
 * @author Martin Braaksma
 */
class NumberPicture {

    private static final String FIELD_TYPE = "([NGH])";

    private static final String DECIMAL_THOUSANDS_SEPARATORS = "(\\.,|,\\.)?";

    private static final String CURRENCY_SYMBOL = "(\\p{Sc})?";

    private static final String SIGN = "([-+()]?)";

    private static final String LITERAL = "(.*)";

    private static final String NUMBER = "((?:[Z*9]+)(?:[.,][Z*9]+)*(?:[.,][Z*9]+)?)";

    private static final String REGEX = FIELD_TYPE + DECIMAL_THOUSANDS_SEPARATORS + LITERAL + SIGN + CURRENCY_SYMBOL + NUMBER + SIGN + LITERAL;

    private static final Pattern PATTERN = Pattern.compile(REGEX);

    private static final int DECIMAL_THOUSANDS_SEPARATOR_SUBGROUP = 2;

    private final String numberPicture;

    private Matcher matcher = null;

    private Type type = null;

    private String decimalThousandsSeparatorsFormat = null;

    private String mask = null;

    private int dataLength;

    NumberPicture(final String numberPicture) {
        this.numberPicture = numberPicture;
        matcher = PATTERN.matcher(numberPicture);
        checkPicture();
        analyzePicture();
    }

    private void checkPicture() {
        if (!matcher.matches()) {
            throw new IllegalArgumentException("Picture does not match expected pattern: " + numberPicture);
        }
    }

    private void analyzePicture() {
        setType(numberPicture.charAt(0));
        setDecimalThousandsSeparatorsSpecifier();
        setMask();
        setDataLength();
    }

    private void setDataLength() {
        dataLength = numberPicture.length() - 1;
        if (hasDecimalThousandsSeparators()) {
            dataLength -= matcher.group(DECIMAL_THOUSANDS_SEPARATOR_SUBGROUP).length();
        }
    }

    private boolean hasDecimalThousandsSeparators() {
        return matcher.group(DECIMAL_THOUSANDS_SEPARATOR_SUBGROUP) != null;
    }

    private void setType(final char type) {
        switch(type) {
            case 'N':
                this.type = Type.N;
                break;
            case 'G':
                this.type = Type.G;
                break;
            case 'H':
                this.type = Type.H;
        }
    }

    private void setDecimalThousandsSeparatorsSpecifier() {
        if ((type == Type.G || type == Type.H) && (numberPicture.substring(1).startsWith(".,") || numberPicture.substring(1).startsWith(",."))) {
            decimalThousandsSeparatorsFormat = numberPicture.substring(1, 3);
        }
    }

    private void setMask() {
        if (decimalThousandsSeparatorsFormat == null) {
            mask = numberPicture.substring(1);
        } else {
            mask = numberPicture.substring(3);
        }
    }

    CharacterIterator maskIterator() {
        return new StringCharacterIterator(mask);
    }

    int getMaskSize() {
        return mask.length();
    }

    Type getType() {
        return type;
    }

    int digitsInMask() {
        int digitCounter = 0;
        for (int i = 0; i < mask.length(); ++i) {
            if (mask.charAt(i) == '9' || mask.charAt(i) == 'Z' || mask.charAt(i) == '*') {
                ++digitCounter;
            }
        }
        return digitCounter;
    }

    public int getDataLength() {
        return dataLength;
    }
}
