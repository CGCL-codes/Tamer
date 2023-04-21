package com.erclab.internal.xpresso.reportelements;

import com.erclab.internal.xpresso.reports.Report;
import com.erclab.internal.xpresso.util.MysqlFormatter;

/**
 * @author Enrique Rodriguez.
 *
 */
public class PrintMysqlDate extends PrintValue {

    public String toHTML(String columnValue, String uniqueKeyValue, Report theReport) {
        return super.toHTML(MysqlFormatter.formatToStandardDate(columnValue), uniqueKeyValue, theReport);
    }
}
