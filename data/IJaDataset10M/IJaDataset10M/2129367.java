package org.hrodberaht.i18n.formatter.types;

import java.math.BigDecimal;

/**
 * Simple Java Utils
 *
 * @author Robert Alexandersson
 *         2010-apr-01 17:50:27
 * @version 1.0
 * @since 1.0
 */
public class PercentData extends BigDecimal {

    public PercentData(String val) {
        super(val);
    }

    public PercentData(double val) {
        super(val);
    }

    public PercentData(int val) {
        super(val);
    }

    public PercentData(long val) {
        super(val);
    }
}
