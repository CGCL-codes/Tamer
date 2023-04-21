package net.chrisrichardson.bankingExample.domain;

import junit.framework.Assert;

public class MoneyUtil {

    public static void assertMoneyEquals(double expected, double actual) {
        Assert.assertEquals(expected, actual, 0.01);
    }
}
