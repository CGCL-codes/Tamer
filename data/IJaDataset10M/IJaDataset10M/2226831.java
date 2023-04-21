package com.calipso.reportgenerator.reportcalculator.strategy;

import com.calipso.reportgenerator.reportcalculator.shareddata.SharedFloat;
import com.calipso.reportgenerator.common.exception.InfoException;
import com.calipso.reportgenerator.reportcalculator.BasicSQLConstants;
import java.io.Serializable;

/**
 *
 * User: jbassino
 * Date: 03/11/2004
 * Time: 14:34:32
 *
 */
public class MaxStrategy extends MetricCalculationStrategy implements Serializable {

    public Object operate(Object[] node, int index, Object measure, Object[] aRow) {
        SharedFloat sharedFloat = (SharedFloat) node[index];
        if (measure != null) {
            if (Float.isNaN(sharedFloat.floatValue())) {
                return measure;
            }
            if (sharedFloat.compareTo(measure) >= 0) {
                return sharedFloat;
            } else {
                return measure;
            }
        } else {
            return sharedFloat;
        }
    }

    public String getSQLFunction() throws InfoException {
        return BasicSQLConstants.MAX;
    }
}
