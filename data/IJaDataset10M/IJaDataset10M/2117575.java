package com.widen.prima.util;

import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.util.List;
import java.util.Map;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRAbstractBeanDataSourceProvider;
import net.sf.jasperreports.engine.util.JRLoader;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jface.operation.IRunnableWithProgress;
import com.jasperassistant.designer.viewer.ViewerComposite;
import com.widen.prima.Messages;
import com.widen.prima.datasource.DataSourceFactory;

public class ReportWorker implements IRunnableWithProgress {

    /**
     * �ռ��ʱ�����ļ����
     */
    public static final String RPT_BOA = "book_of_account";

    /**
     * �ʲ�ծ����ļ����
     */
    public static final String RPT_BSH = "balance_sheet";

    /**
     * �������ļ����
     */
    public static final String RPT_IST = "income_statement";

    /**
     * ���ʱ��?��ʾ���У����ļ����
     */
    public static final String RPT_SBL = "subject_balance";

    /**
     * ���ʱ��?���ڲ�ѯ�����ļ����
     */
    public static final String RPT_SBL_QRY = "subject_balance_query";

    private String reportName;

    private Map reportParams;

    private List hqlParams;

    private ViewerComposite viewer;

    public ReportWorker(ViewerComposite viewer, String reportName, Map reportParams, List hqlParams) {
        this.viewer = viewer;
        this.reportName = reportName;
        this.reportParams = reportParams;
        this.hqlParams = hqlParams;
    }

    public void run(IProgressMonitor monitor) throws InvocationTargetException, InterruptedException {
        monitor.beginTask(Messages.getString("ReportWorker.build.report"), IProgressMonitor.UNKNOWN);
        try {
            monitor.subTask(Messages.getString("ReportWorker.load.report.file"));
            InputStream is = this.getClass().getResourceAsStream("/report/" + reportName + ".jasper");
            JasperReport jasperReport = (JasperReport) JRLoader.loadObject(is);
            monitor.subTask(Messages.getString("ReportWorker.fill.report.data"));
            Thread.currentThread().setContextClassLoader(this.getClass().getClassLoader());
            JRAbstractBeanDataSourceProvider dataSource = DataSourceFactory.getDataSource(hqlParams, reportName);
            JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, reportParams, dataSource.create(jasperReport));
            monitor.subTask(Messages.getString("ReportWorker.ready.to.show"));
            try {
                viewer.getReportViewer().setDocument(jasperPrint);
            } catch (Exception e) {
            }
        } catch (JRException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        monitor.done();
        if (monitor.isCanceled()) throw new InterruptedException(Messages.getString("ReportWorker.long.run.cancelled"));
    }
}
