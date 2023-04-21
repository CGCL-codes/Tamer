package br.eng.eliseu.j2me.utils.number;

public class NumberUtils {

    public static final boolean isDoubleValid(String numero) {
        try {
            Double d = Double.valueOf(numero);
            return true;
        } catch (NumberFormatException e) {
            System.out.println(e);
            return false;
        }
    }

    public static final String toString(double number, int decimal) {
        String s = Double.toString(number);
        int pos = s.indexOf(".");
        s = s.substring(0, pos) + (decimal <= 0 ? "" : s.substring(pos, (pos + decimal + 1 > s.length() ? s.length() : pos + decimal + 1)));
        return s;
    }
}
