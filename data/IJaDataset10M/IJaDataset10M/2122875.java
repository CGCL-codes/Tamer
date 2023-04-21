package com.calipso.reportgenerator.reportcalculator.filteroperation;

import com.calipso.reportgenerator.common.datadefinition.ReportDimensionSpec;
import com.calipso.reportgenerator.reportcalculator.shareddata.SharedFloat;
import com.calipso.reportgenerator.reportdefinitions.types.DimensionDefinitionOrderType;
import com.calipso.reportgenerator.common.*;
import com.calipso.reportgenerator.reportcalculator.FilterOperation;
import java.util.Collection;
import java.util.Vector;
import java.util.Iterator;
import java.util.Map;

/**
 *
 * User: jbassino
 * Date: 12-may-2005
 * Time: 16:32:32
 * Calipso Software
 */
public class TopOperation extends FilterOperation {

    private float upToValue;

    private int accum = 0;

    private Collection values;

    public TopOperation(ReportResult result, ReportQuery reportQuery, ReportFilterSpec filter, int value) {
        super();
        ReportDimensionSpec dimension = reportQuery.getReportSpec().getDimensionFromName(filter.getDimensionName());
        Collection values = result.getValuesCollection(dimension.getOrder() == DimensionDefinitionOrderType.A);
        this.values = new Vector();
        Iterator it = values.iterator();
        this.upToValue = value;
        for (int i = 0; i < upToValue && it.hasNext(); i++) {
            Object ob = it.next();
            if (ob instanceof DimensionValueNode) {
                this.values.add(((DimensionValueNode) ob).getValue());
            } else if (ob instanceof Map.Entry) {
                this.values.add(((Map.Entry) ((Map.Entry) ob).getValue()).getKey());
            }
        }
    }

    /**
   * Np entra en este metodo porque se redefine el metodo operate(result) de la clase padre
   * @param value
   * @return
   */
    protected boolean accept(SharedFloat value) {
        return false;
    }

    public Collection operate(ReportResult result) {
        return values;
    }
}
