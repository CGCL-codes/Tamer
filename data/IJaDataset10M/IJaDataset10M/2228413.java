package org.posterita.core.utils;

import java.math.BigDecimal;

public class FormatBigDecimal {

    public static BigDecimal currency(BigDecimal bd) {
        return bd.setScale(2, BigDecimal.ROUND_HALF_UP);
    }

    public static BigDecimal currency1(BigDecimal bd) {
        return bd.setScale(2, BigDecimal.ROUND_HALF_DOWN);
    }

    public static BigDecimal currency(double d) {
        BigDecimal bd = new BigDecimal(d).setScale(2, BigDecimal.ROUND_HALF_UP);
        return bd;
    }
}
