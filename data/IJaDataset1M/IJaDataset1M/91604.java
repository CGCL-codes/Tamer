package net.sourceforge.transumanza.processor.field;

public class Substringer {

    public static String substring(String str, int pos) {
        if (str == null) return null;
        return str.substring(pos);
    }
}
