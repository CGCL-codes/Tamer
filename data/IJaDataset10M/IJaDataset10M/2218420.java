package org.protesting.jft.generator.generic;

import java.util.Date;

/**
 * User: ab83625
 * Date: 26.02.2008
 * Time: 11:46:32
 */
public class DateGenerator implements GenericGenerator {

    private static final long DAY = 86400000;

    private static final long SECOND = 1000;

    public Object getSmallerThan(Object leftBorder) {
        long left = ((Date) leftBorder).getTime();
        left -= DAY;
        return new Date(left);
    }

    public Object getGreaterThan(Object rightBorder) {
        long right = ((Date) rightBorder).getTime();
        right += DAY;
        return new Date(++right);
    }

    public Object getValue(Object leftBorder, Object rightBorder) {
        double left = (double) ((Date) leftBorder).getTime();
        double right = (double) ((Date) rightBorder).getTime();
        return new Date(Math.round(Math.random() * (right - left) + left));
    }

    public static Date getValue(Date leftBorder, Date rightBorder) {
        double left = (double) leftBorder.getTime();
        double right = (double) rightBorder.getTime();
        return new Date(Math.round(Math.random() * (right - left) + left));
    }

    public boolean validate(Object leftBorder, Object rightBorder) {
        long left = ((Date) leftBorder).getTime();
        long right = ((Date) rightBorder).getTime();
        if (left > right) {
            throw new IllegalArgumentException("left border [" + left + "] is bigger than right [" + right + "]");
        }
        return Math.abs(left - right) > Math.abs(DAY);
    }
}
