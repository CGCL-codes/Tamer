package taxonfinder.util;

public class XmlEndec {

    public static final String ENC_AMPS = "&amp;";

    public static final String ENC_QUOT = "&quot;";

    public static final String ENC_LT = "&lt;";

    public static final String ENC_GT = "&gt;";

    public static final char DEC_AMPS = '&';

    public static final char DEC_QUOT = '\"';

    public static final char DEC_LT = '<';

    public static final char DEC_GT = '>';

    public static String encodeXML(String myString) {
        StringBuffer buffer = new StringBuffer();
        char chars[] = myString.toCharArray();
        for (int i = 0; i < chars.length; i++) {
            if (chars[i] == DEC_AMPS) {
                buffer.append("&amp;");
            } else if (chars[i] == DEC_GT) {
                buffer.append("&gt;");
            } else if (chars[i] == DEC_LT) {
                buffer.append("&lt;");
            } else if (chars[i] == DEC_QUOT) {
                buffer.append("&quot;");
            } else {
                buffer.append(chars[i]);
            }
        }
        return buffer.toString();
    }

    public static String decodeXML(String myString) {
        String temp = "";
        if (myString.contains(ENC_AMPS)) {
            temp = myString.replace(ENC_AMPS, new Character(DEC_AMPS).toString());
        }
        if (temp.contains(ENC_GT)) {
            myString = temp.replace(ENC_GT, new Character(DEC_GT).toString());
        }
        if (myString.contains(ENC_LT)) {
            temp = myString.replace(ENC_LT, new Character(DEC_LT).toString());
        }
        if (temp.contains(ENC_QUOT)) {
            myString = temp.replace(ENC_QUOT, new Character(DEC_QUOT).toString());
        }
        return myString;
    }

    public static void main(String args[]) {
        encodeXML("Is this cool or what? \" > < & What say you my precious my love?");
    }
}
