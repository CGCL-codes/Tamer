package com.gsoft.query.service.impl;

import java.io.OutputStream;
import java.util.Map;
import org.dom4j.Document;
import net.sf.hibernate.Session;
import net.sf.hibernate.SessionFactory;
import net.sf.jasperreports.engine.JRAbstractExporter;
import net.sf.jasperreports.engine.JRExporterParameter;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.design.JRDesignQuery;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.export.JExcelApiExporter;
import net.sf.jasperreports.engine.export.JRPdfExporter;
import net.sf.jasperreports.engine.export.JRRtfExporter;
import net.sf.jasperreports.engine.xml.JRXmlLoader;
import com.gsoft.query.common.Constants;
import com.gsoft.query.helper.CommonUtil;
import com.gsoft.query.helper.ReportUtil;
import com.gsoft.query.service.IReportService;
import com.gsoft.query.tao.ITemplateFactory;

/**
 * <p>
 * ReportService.java
 * </p>
 * <p>
 * </p>
 * 
 * @author $Author: lutao $
 * @version $Revision: 1.2 $
 */
public class ReportService implements IReportService {

    private SessionFactory sessionFactory;

    private ITemplateFactory templateFactory;

    private int pageSize;

    /**
	 * ������Pdf
	 */
    public void export2Pdf(String queryId, String queryCondition, OutputStream output, boolean isGBK) throws Exception {
        Document templateDoc = templateFactory.getTemplate(queryId);
        export2Pdf(queryId, ReportUtil.parseParamMap(templateDoc, queryCondition, isGBK), output);
    }

    /**
	 * ������Excel
	 */
    public void export2Excel(String queryId, String queryCondition, OutputStream output, boolean isGBK) throws Exception {
        Document templateDoc = templateFactory.getTemplate(queryId);
        export2Excel(queryId, ReportUtil.parseParamMap(templateDoc, queryCondition, isGBK), output);
    }

    /**
	 * ���RTF����
	 * @param queryId
	 * @param queryCondition
	 * @param output
	 * @throws Exception
	 */
    public void export2Rtf(String queryId, String queryCondition, OutputStream output, boolean isGBK) throws Exception {
        Document templateDoc = templateFactory.getTemplate(queryId);
        export2Rtf(queryId, ReportUtil.parseParamMap(templateDoc, queryCondition, isGBK), output);
    }

    /**
	 * ������Pdf��
	 */
    public void export2Pdf(String queryId, Map queryParams, OutputStream output) throws Exception {
        export(queryId, queryParams, output, new JRPdfExporter());
    }

    /**
	 * ������Excel��
	 */
    public void export2Excel(String queryId, Map queryParams, OutputStream output) throws Exception {
        export(queryId, queryParams, output, new JExcelApiExporter());
    }

    /**
	 * ������Rtf��
	 */
    public void export2Rtf(String queryId, Map queryParams, OutputStream output) throws Exception {
        export(queryId, queryParams, output, new JRRtfExporter());
    }

    /**
	 * ��ȡָ��ҳ���Pdf�������
	 */
    public byte[] getPdfReport(String queryId, String queryCondition, int pageNum) throws Exception {
        Document templateDoc = templateFactory.getTemplate(queryId);
        String sourceFileName = CommonUtil.getResourceFileUrl(Constants.TEMPLATE_DIR + "/" + queryId + ".jasper").getPath();
        JasperDesign jasperDesign = JRXmlLoader.load(sourceFileName);
        String originalQueryText = jasperDesign.getQuery().getText();
        JRDesignQuery query = new JRDesignQuery();
        query.setText(getQueryText(originalQueryText, pageNum, pageSize));
        jasperDesign.setQuery(query);
        jasperDesign.setLanguage("java");
        JasperReport jasperReport = JasperCompileManager.compileReport(jasperDesign);
        byte[] bytes = null;
        Session session = null;
        try {
            session = sessionFactory.openSession();
            JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, ReportUtil.parseParamMap(templateDoc, queryCondition, false), session.connection());
            bytes = JasperExportManager.exportReportToPdf(jasperPrint);
        } finally {
            if (session != null) session.close();
        }
        return bytes;
    }

    /**
	 * ����
	 * @param queryId
	 * @param queryParams
	 * @param output
	 * @param exporter
	 * @throws Exception 
	 */
    private void export(String queryId, Map queryParams, OutputStream output, JRAbstractExporter exporter) throws Exception {
        String sourceFileName = CommonUtil.getResourceFileUrl(Constants.TEMPLATE_DIR + "/" + queryId + ".jasper").getPath();
        Session session = null;
        try {
            session = sessionFactory.openSession();
            JasperPrint report = JasperFillManager.fillReport(sourceFileName, queryParams, session.connection());
            exporter.setParameter(JRExporterParameter.JASPER_PRINT, report);
            exporter.setParameter(JRExporterParameter.OUTPUT_STREAM, output);
            exporter.exportReport();
        } finally {
            if (session != null) session.close();
        }
    }

    /**
	 * <p>
	 * ��ȡ��ǰ��ѯSQL�ű�
	 * </p>
	 * 
	 * @return
	 */
    private String getQueryText(String sql, long pageNum, int pageSize) {
        StringBuffer sqlSb = new StringBuffer();
        sqlSb.append("select * from(select rownum row_num,tt.* from( ").append(sql).append(" ) tt ) tmp where tmp.row_num <= ").append(String.valueOf(pageNum * pageSize)).append(" and tmp.row_num > ").append(String.valueOf((pageNum - 1) * pageSize));
        return sqlSb.toString();
    }

    /**
	 * @param sessionFactory
	 *            The sessionFactory to set.
	 */
    public void setSessionFactory(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    /**
	 * @param pageSize The pageSize to set.
	 */
    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    /**
	 * @return Returns the templateFactory.
	 */
    public ITemplateFactory getTemplateFactory() {
        return templateFactory;
    }

    /**
	 * @param templateFactory The templateFactory to set.
	 */
    public void setTemplateFactory(ITemplateFactory templateFactory) {
        this.templateFactory = templateFactory;
    }
}
