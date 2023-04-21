package net.sf.vietpad.converter;

public class UnicodeViqrConverter extends Converter {

    final String[] VIQR_char = { "y~", "Y~", "y?", "Y?", "y.", "Y.", "y`", "Y`", "u+.", "U+.", "u+~", "U+~", "u+?", "U+?", "u+`", "U+`", "u+'", "U+'", "u?", "U?", "u.", "U.", "o+.", "O+.", "o+~", "O+~", "o+?", "O+?", "o+`", "O+`", "o+'", "O+'", "o^.", "O^.", "o^~", "O^~", "o^?", "O^?", "o^`", "O^`", "o^'", "O^'", "o?", "O?", "o.", "O.", "i.", "I.", "i?", "I?", "e^.", "E^.", "e^~", "E^~", "e^?", "E^?", "e^`", "E^`", "e^'", "E^'", "e~", "E~", "e?", "E?", "e.", "E.", "a(.", "A(.", "a(~", "A(~", "a(?", "A(?", "a(`", "A(`", "a('", "A('", "a^.", "A^.", "a^~", "A^~", "a^?", "A^?", "a^`", "A^`", "a^'", "A^'", "a?", "A?", "a.", "A.", "u+", "U+", "o+", "O+", "u~", "U~", "i~", "I~", "dd", "a(", "A(", "y'", "u'", "u`", "o~", "o^", "o'", "o`", "i'", "i`", "e^", "e'", "e`", "a~", "a^", "a'", "a`", "Y'", "U'", "U`", "O~", "O^", "O'", "O`", "DD", "I'", "I`", "E^", "E'", "E`", "A~", "A^", "A'", "A`" };

    final String[] Unicode_char = { "ỹ", "Ỹ", "ỷ", "Ỷ", "ỵ", "Ỵ", "ỳ", "Ỳ", "ự", "Ự", "ữ", "Ữ", "ử", "Ử", "ừ", "Ừ", "ứ", "Ứ", "ủ", "Ủ", "ụ", "Ụ", "ợ", "Ợ", "ỡ", "Ỡ", "ở", "Ở", "ờ", "Ờ", "ớ", "Ớ", "ộ", "Ộ", "ỗ", "Ỗ", "ổ", "Ổ", "ồ", "Ồ", "ố", "Ố", "ỏ", "Ỏ", "ọ", "Ọ", "ị", "Ị", "ỉ", "Ỉ", "ệ", "Ệ", "ễ", "Ễ", "ể", "Ể", "ề", "Ề", "ế", "Ế", "ẽ", "Ẽ", "ẻ", "Ẻ", "ẹ", "Ẹ", "ặ", "Ặ", "ẵ", "Ẵ", "ẳ", "Ẳ", "ằ", "Ằ", "ắ", "Ắ", "ậ", "Ậ", "ẫ", "Ẫ", "ẩ", "Ẩ", "ầ", "Ầ", "ấ", "Ấ", "ả", "Ả", "ạ", "Ạ", "ư", "Ư", "ơ", "Ơ", "ũ", "Ũ", "ĩ", "Ĩ", "đ", "ă", "Ă", "ý", "ú", "ù", "õ", "ô", "ó", "ò", "í", "ì", "ê", "é", "è", "ã", "â", "á", "à", "Ý", "Ú", "Ù", "Õ", "Ô", "Ó", "Ò", "Đ", "Í", "Ì", "Ê", "É", "È", "Ã", "Â", "Á", "À" };

    /**
     *  Converts Unicode to VIQR.
     *
     * @param str     Source string
     * @param html    True if HTML code
     * @return        VIQR string
     */
    public String convert(String str, boolean html) {
        if (str.matches(".*\\p{InCombiningDiacriticalMarks}+.*")) {
            str = compositeToPrecomposed(str);
        }
        str = str.replaceAll("(?=[.?'])", "\\\\").replaceAll("(?i)(?<=d)(?=d)", "\\\\");
        str = replaceString(str, Unicode_char, VIQR_char);
        return cleanupVIQR(str);
    }

    /**
     *  Removes unneeded '\' characters.
     */
    String cleanupVIQR(String str) {
        if (str.charAt(0) == '﻿') {
            str = str.substring(1);
        }
        return str.replaceAll("(?i)(?<![aeiouy^(+])\\\\", "").replaceAll("(?<=://|mailto:)([^\\\\]+)\\\\(?=[.?])", "$1");
    }
}
