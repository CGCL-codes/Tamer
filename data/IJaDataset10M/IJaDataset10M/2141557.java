package de.hu_berlin.sam.mmunit.coverage.report.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;
import net.sf.jasperreports.engine.JRExporter;
import net.sf.jasperreports.engine.JRExporterParameter;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.export.JRCsvExporter;
import net.sf.jasperreports.engine.export.JRHtmlExporter;
import net.sf.jasperreports.engine.export.JRHtmlExporterParameter;
import net.sf.jasperreports.engine.export.JRPdfExporter;
import net.sf.jasperreports.engine.export.JRRtfExporter;
import net.sf.jasperreports.engine.export.JRTextExporter;
import net.sf.jasperreports.engine.export.JRTextExporterParameter;
import net.sf.jasperreports.engine.export.JRXlsExporter;
import net.sf.jasperreports.engine.export.JRXlsExporterParameter;
import net.sf.jasperreports.engine.export.JRXmlExporter;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.impl.EObjectImpl;
import de.hu_berlin.sam.mmunit.coverage.TestGoal;
import de.hu_berlin.sam.mmunit.coverage.report.JasperEngine;
import de.hu_berlin.sam.mmunit.coverage.report.OutputFormat;
import de.hu_berlin.sam.mmunit.coverage.report.ReportDefinition;
import de.hu_berlin.sam.mmunit.coverage.report.ReportPackage;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Jasper Engine</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * </p>
 *
 * @generated
 */
public class JasperEngineImpl extends EObjectImpl implements JasperEngine {

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated NOT
	 */
    @SuppressWarnings("unused")
    private Logger logger = Logger.getLogger("JasperEngineImpl");

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    protected JasperEngineImpl() {
        super();
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    @Override
    protected EClass eStaticClass() {
        return ReportPackage.Literals.JASPER_ENGINE;
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated NOT
	 */
    public void export(ReportDefinition reportDefinition) {
        try {
            for (OutputFormat format : reportDefinition.getFormats()) {
                JRExporter exporter = null;
                String exportFile = reportDefinition.getDestinationFileName();
                switch(format.getValue()) {
                    case OutputFormat.PDF_VALUE:
                        exporter = new JRPdfExporter();
                        exportFile = exportFile + ".pdf";
                        break;
                    case OutputFormat.HTML_VALUE:
                        exporter = new JRHtmlExporter();
                        exportFile = exportFile + ".html";
                        exporter.setParameter(JRHtmlExporterParameter.IS_USING_IMAGES_TO_ALIGN, false);
                        break;
                    case OutputFormat.XML_VALUE:
                        exporter = new JRXmlExporter();
                        exportFile = exportFile + ".xml";
                        break;
                    case OutputFormat.CSV_VALUE:
                        exporter = new JRCsvExporter();
                        exportFile = exportFile + ".csv";
                        break;
                    case OutputFormat.XLS_VALUE:
                        exporter = new JRXlsExporter();
                        exporter.setParameter(JRXlsExporterParameter.IS_ONE_PAGE_PER_SHEET, Boolean.TRUE);
                        exporter.setParameter(JRXlsExporterParameter.IS_WHITE_PAGE_BACKGROUND, Boolean.FALSE);
                        exporter.setParameter(JRXlsExporterParameter.IS_REMOVE_EMPTY_SPACE_BETWEEN_ROWS, Boolean.TRUE);
                        exportFile = exportFile + ".xls";
                        break;
                    case OutputFormat.RTF_VALUE:
                        exporter = new JRRtfExporter();
                        exportFile = exportFile + ".rtf";
                        break;
                    case OutputFormat.TEXT_VALUE:
                        exporter = new JRTextExporter();
                        exporter.setParameter(JRTextExporterParameter.PAGE_WIDTH, 80);
                        exporter.setParameter(JRTextExporterParameter.PAGE_HEIGHT, 60);
                        exportFile = exportFile + ".txt";
                        break;
                }
                Map<String, Object> parameters = new HashMap<String, Object>();
                List<TestGoal> tgList = reportDefinition.getCriterion().getTestgoals();
                double count = 0.00;
                for (TestGoal testGoal : tgList) {
                    if (testGoal.isCovered()) count++;
                }
                double percentage = 0.00;
                if (tgList.size() > 0) {
                    percentage = count / tgList.size();
                }
                parameters.put("percentage", percentage);
                JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(tgList);
                JasperReport jasperReport = JasperCompileManager.compileReport(reportDefinition.getReport());
                JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, dataSource);
                exporter.setParameter(JRExporterParameter.JASPER_PRINT, jasperPrint);
                exporter.setParameter(JRExporterParameter.OUTPUT_FILE_NAME, exportFile);
                exporter.exportReport();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
