package org.plazmaforge.bsolution.payroll.common.beans;

import org.plazmaforge.framework.core.data.Dictionary;

/**
 * @author Oleh Hapon
 * Date: 24.10.2004
 * Time: 14:49:21
 * $Id: SicklistAverangeType.java,v 1.2 2010/04/28 06:24:19 ohapon Exp $
 */
public class SicklistAverangeType extends Dictionary {

    private int month;

    public int getMonth() {
        return month;
    }

    public void setMonth(int month) {
        this.month = month;
    }
}
