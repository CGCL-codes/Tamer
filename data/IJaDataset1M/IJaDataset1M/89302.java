package org.modcal;

public class NumericString implements CharSequence {

    private final String value;

    public NumericString(CharSequence s) {
        String tmp;
        tmp = s.toString();
        Double.valueOf(tmp);
        this.value = tmp.replaceFirst("0+$", "");
    }

    public static boolean isValid(String s) {
        try {
            Double.valueOf(s);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    public char charAt(int index) {
        return value.charAt(index);
    }

    public int length() {
        return value.length();
    }

    public CharSequence subSequence(int start, int end) {
        return value.subSequence(start, end);
    }

    public String getValue() {
        return value;
    }

    public String toString() {
        return value.toString();
    }
}
