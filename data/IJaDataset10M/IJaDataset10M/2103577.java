package jp.seraph.sample.magicSquare;

public class ErrorMessage {

    private ErrorMessage() {
    }

    public static String duplicatevalue(int i, int j, Integer aValue) {
        return "(" + i + " ," + j + ")の値(=" + aValue + ")が重複しています";
    }

    public static String overMaxValue(int i, int j, Integer aValue, int aMax) {
        return "(" + i + " ," + j + ")の値(=" + aValue + ")が最大値(=" + aMax + ")を超えています";
    }

    public static String underMinValue(int i, int j, Integer aValue) {
        return "(" + i + " ," + j + ")の値(=" + aValue + ")が最小値(1)を下回っています";
    }

    public static String illegalStringValueException(int i, int j, String aValue) {
        return "(" + i + ", " + j + ")の入力値(=" + aValue + ")が整数値でありません";
    }

    public static String illegalRank(int aSize) {
        return "与えられた文字列が、規定数(" + aSize + "）の要素に分割できません。";
    }

    public static String mustBeSquareMatrix() {
        return "n*nの配列でなければなりません。";
    }
}
