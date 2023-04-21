package com.qtc.badminton.util;

import java.text.DecimalFormat;

/**
 * デジタル機能に関連する関数
 * @author madawei
 * @version 1.0.0
 *　@date 2010-3-2
 */
public class MathUtil {

    public static Object fillNullNum(Object i) throws ClassCastException {
        if (!(i instanceof Long) && !(i instanceof Integer)) {
            throw new ClassCastException();
        }
        if (i == null) {
            return 0;
        }
        return i;
    }

    public static Double divisionNum(Object a, Object b, Integer numberOfDecimal) throws ClassCastException {
        return new Double(divisionNumReturnString(a, b, numberOfDecimal));
    }

    public static String divisionNumReturnString(Object a, Object b, Integer numberOfDecimal) throws ClassCastException {
        if (!(a instanceof Long) && !(a instanceof Integer) && !(b instanceof Long) && !(b instanceof Integer)) {
            throw new ClassCastException();
        }
        Double aDouble = 0D;
        Double bDouble = 0D;
        Double resultDouble = 0D;
        if (a instanceof Long) {
            aDouble = (Long) a / 1.0;
        } else if (a instanceof Integer) {
            aDouble = (Integer) a / 1.0;
        }
        if (b instanceof Long) {
            bDouble = (Long) b / 1.0;
        } else if (b instanceof Integer) {
            bDouble = (Integer) b / 1.0;
        }
        if (bDouble == 0D || aDouble == null || bDouble == null) {
            resultDouble = 0D;
        } else {
            resultDouble = aDouble / bDouble;
        }
        return numberFormat(resultDouble, numberOfDecimal);
    }

    public static String numberToString(Object num) throws ClassCastException {
        return "" + num;
    }

    public static String numberFormat(Double num, Integer numberOfDecimal) throws ClassCastException {
        return new DecimalFormat(getFormatString(numberOfDecimal)).format(num);
    }

    public static String decimalFormat(Double num, Integer numberOfDecimal) throws ClassCastException {
        return new DecimalFormat(",##" + getFormatString(numberOfDecimal)).format(num);
    }

    private static String getFormatString(Integer numberOfDecimal) {
        String formatString = "";
        for (int i = 0; i < numberOfDecimal; i++) {
            formatString = formatString + "0";
        }
        if (numberOfDecimal == null || numberOfDecimal == 0) {
            formatString = "0";
        } else {
            formatString = "0." + formatString;
        }
        return formatString;
    }

    public static String getTimeFromLong(Long longTime) {
        String time = "";
        Long secondNum = longTime / 1000;
        Long minuteNum = secondNum / 60;
        Long hour = minuteNum / 60;
        Long minute = minuteNum - (hour * 60);
        Long second = secondNum - (minute * 60) - (hour * 3600);
        time = getTimeTwoBitString(hour.toString()) + ":" + getTimeTwoBitString(minute.toString()) + ":" + getTimeTwoBitString(second.toString());
        return time;
    }

    public static String getTimeTwoBitString(int number) {
        return String.format("%1$02d", number);
    }

    private static String getTimeTwoBitString(String number) {
        if (number.length() == 1) {
            number = "0" + number;
        }
        return number;
    }

    public static String numberPercentageFormat(Double num, int decimal) throws ClassCastException {
        String percentage = "";
        String n = "0.";
        if (decimal > 0) {
            for (int i = 0; i < decimal; i++) {
                n = n + "0";
            }
            n = n + "%";
            percentage = new DecimalFormat(n).format(num);
        } else {
            percentage = new DecimalFormat("0%").format(num);
        }
        return percentage;
    }

    public static void main(String[] args) {
        double pi = 99792458.1415927;
        System.out.println(numberFormat(pi, 10));
        System.out.println(decimalFormat(pi, 0));
        System.out.println(divisionNumReturnString(1000000000, 3, 3));
        System.out.println(getTimeFromLong(1200000L));
        System.out.println(numberPercentageFormat(0.14567, 2));
        System.out.println(numberPercentageFormat(divisionNum(1, 3, 6), 4));
        System.out.println(divisionNum(1, 3, 3));
    }
}
