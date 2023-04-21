package ru.point.insert;

/**
 * @author: Mikhail Sedov [12.01.2009]
 */
public class Translit {

    private static final String[] charTable = new String[81];

    private static final char START_CHAR = '�';

    static {
        charTable['�' - START_CHAR] = "A";
        charTable['�' - START_CHAR] = "B";
        charTable['�' - START_CHAR] = "V";
        charTable['�' - START_CHAR] = "G";
        charTable['�' - START_CHAR] = "D";
        charTable['�' - START_CHAR] = "E";
        charTable['�' - START_CHAR] = "E";
        charTable['�' - START_CHAR] = "ZH";
        charTable['�' - START_CHAR] = "Z";
        charTable['�' - START_CHAR] = "I";
        charTable['�' - START_CHAR] = "I";
        charTable['�' - START_CHAR] = "K";
        charTable['�' - START_CHAR] = "L";
        charTable['�' - START_CHAR] = "M";
        charTable['�' - START_CHAR] = "N";
        charTable['�' - START_CHAR] = "O";
        charTable['�' - START_CHAR] = "P";
        charTable['�' - START_CHAR] = "R";
        charTable['�' - START_CHAR] = "S";
        charTable['�' - START_CHAR] = "T";
        charTable['�' - START_CHAR] = "U";
        charTable['�' - START_CHAR] = "F";
        charTable['�' - START_CHAR] = "H";
        charTable['�' - START_CHAR] = "C";
        charTable['�' - START_CHAR] = "CH";
        charTable['�' - START_CHAR] = "SH";
        charTable['�' - START_CHAR] = "SH";
        charTable['�' - START_CHAR] = "'";
        charTable['�' - START_CHAR] = "Y";
        charTable['�' - START_CHAR] = "'";
        charTable['�' - START_CHAR] = "E";
        charTable['�' - START_CHAR] = "U";
        charTable['�' - START_CHAR] = "YA";
        for (int i = 0; i < charTable.length; i++) {
            char idx = (char) ((char) i + START_CHAR);
            char lower = new String(new char[] { idx }).toLowerCase().charAt(0);
            if (charTable[i] != null) {
                charTable[lower - START_CHAR] = charTable[i].toLowerCase();
            }
        }
    }

    public static String toTranslit(String text) {
        char charBuffer[] = text.toCharArray();
        StringBuilder sb = new StringBuilder(text.length());
        for (char symbol : charBuffer) {
            int i = symbol - START_CHAR;
            if (i >= 0 && i < charTable.length) {
                String replace = charTable[i];
                sb.append(replace == null ? symbol : replace);
            } else {
                sb.append(symbol);
            }
        }
        return sb.toString();
    }
}
