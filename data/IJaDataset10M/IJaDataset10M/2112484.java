package ecom.ibeer.managedbeans.tools;

public class NumberTools {

    private static double getNum(double average) {
        double integerPart = (int) average;
        double decimalPart = average - integerPart;
        if (decimalPart < 0.25) {
            decimalPart = 0;
        } else if (decimalPart < 0.75) {
            decimalPart = 0.5;
        } else {
            decimalPart = 1;
        }
        return integerPart + decimalPart;
    }

    public static String getStringNum(double number) {
        String num = Double.toString(getNum(number));
        return num.replace(".", "");
    }
}
