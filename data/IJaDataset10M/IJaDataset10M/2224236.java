package com.widen.prima.datasource;

import java.util.List;
import net.sf.jasperreports.engine.data.JRAbstractBeanDataSourceProvider;
import com.widen.prima.util.ReportWorker;

public class DataSourceFactory {

    public static JRAbstractBeanDataSourceProvider getDataSource(List params, String reportName) {
        if (reportName.equals(ReportWorker.RPT_BOA)) {
            return new BookOfAccountDsProvider(params);
        }
        if (reportName.equals(ReportWorker.RPT_SBL) || reportName.equals(ReportWorker.RPT_SBL_QRY)) {
            return new SubjectBalanceDsProvider(params);
        }
        if (reportName.equals(ReportWorker.RPT_BSH)) {
            return new BalanceSheetDsProvider(params);
        }
        if (reportName.equals(ReportWorker.RPT_IST)) {
            return new IncomeStatementDsProvider(params);
        }
        return null;
    }
}
