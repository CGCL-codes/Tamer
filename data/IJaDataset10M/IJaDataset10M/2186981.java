package doghost.reports;

import java.awt.Dialog.ModalExclusionType;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.Query;
import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.query.JRJpaQueryExecuterFactory;
import net.sf.jasperreports.view.JasperViewer;

/**
 *
 * @author duo
 */
public class ReportRunner {

    protected String reportFileName;

    protected String reportTitle;

    protected String reportLogoFileName;

    protected String persistenceUnitName;

    protected Query reportQuery;

    protected HashMap parametersMap;

    /**
     * Get the value of persistenceUnitName
     *
     * @return the value of persistenceUnitName
     */
    public String getPersistenceUnitName() {
        return persistenceUnitName;
    }

    /**
     * Set the value of persistenceUnitName
     *
     * @param persistenceUnitName new value of persistenceUnitName
     */
    public void setPersistenceUnitName(String persistenceUnitName) {
        this.persistenceUnitName = persistenceUnitName;
    }

    /**
     * Get the value of reportFileName
     *
     * @return the value of reportFileName
     */
    public String getReportFileName() {
        return reportFileName;
    }

    /**
     * Set the value of reportFileName
     *
     * @param reportFileName new value of reportFileName
     */
    public void setReportFileName(String reportFileName) {
        this.reportFileName = reportFileName;
    }

    /**
     * Get the value of reportTitle
     *
     * @return the value of reportTitle
     */
    public String getReportTitle() {
        return reportTitle;
    }

    /**
     * Set the value of reportTitle
     *
     * @param reportTitle new value of reportTitle
     */
    public void setReportTitle(String reportTitle) {
        this.reportTitle = reportTitle;
    }

    /**
     * Get the value of reportLogoFileName
     *
     * @return the value of reportLogoFileName
     */
    public String getReportLogoFileName() {
        return reportLogoFileName;
    }

    /**
     * Set the value of reportLogoFileName
     *
     * @param reportLogoFileName new value of reportLogoFileName
     */
    public void setReportLogoFileName(String reportLogoFileName) {
        this.reportLogoFileName = reportLogoFileName;
    }

    /**
     * Get the value of parametersMap
     *
     * @return the value of parametersMap
     */
    public HashMap getParametersMap() {
        return parametersMap;
    }

    /**
     * Set the value of parametersMap
     *
     * @param parametersMap new value of parametersMap
     */
    public void setParametersMap(HashMap parametersMap) {
        this.parametersMap = parametersMap;
    }

    /**
     * Get the value of reportQuery
     *
     * @return the value of reportQuery
     */
    public Query getReportQuery() {
        return reportQuery;
    }

    /**
     * Set the value of reportQuery
     *
     * @param reportQuery new value of reportQuery
     */
    public void setReportQuery(Query reportQuery) {
        this.reportQuery = reportQuery;
    }

    public ReportRunner(String persistenceUnitName) throws java.text.ParseException {
        this.setPersistenceUnitName(persistenceUnitName);
    }

    public void runReport() throws java.text.ParseException {
        try {
            EntityManagerFactory emf = Persistence.createEntityManagerFactory(this.getPersistenceUnitName(), new HashMap());
            if (!emf.isOpen()) {
                Logger.getLogger(this.getClass().getName()).log(Level.WARNING, "Erro alocando unidade de persistencia!");
                return;
            }
            EntityManager em = emf.createEntityManager();
            if (this.getReportFileName().isEmpty()) {
                Logger.getLogger(this.getClass().getName()).log(Level.WARNING, "O nome do arquivo de relatório está vazio!");
                return;
            }
            Map parameters = this.getParametersMap();
            parameters.put("TITULO", this.getReportTitle());
            JasperPrint jasperPrint = null;
            InputStream reportStream = this.getClass().getResourceAsStream(this.getReportFileName());
            if (this.getReportQuery() == null) {
                parameters.put(JRJpaQueryExecuterFactory.PARAMETER_JPA_ENTITY_MANAGER, em);
                if (this.getReportFileName().endsWith(".jrxml")) {
                    JasperReport jasperReport = JasperCompileManager.compileReport(reportStream);
                    jasperPrint = JasperFillManager.fillReport(jasperReport, parameters);
                } else {
                    jasperPrint = JasperFillManager.fillReport(reportStream, parameters);
                }
            } else {
                JRDataSource dataSource = this.createReportDataSource(em, this.getReportQuery());
                if (this.getReportFileName().endsWith(".jrxml")) {
                    JasperReport jasperReport = JasperCompileManager.compileReport(reportStream);
                    jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, dataSource);
                } else {
                    jasperPrint = JasperFillManager.fillReport(reportStream, parameters, dataSource);
                }
            }
            JasperViewer jrViewer = new JasperViewer(jasperPrint, false);
            jrViewer.setModalExclusionType(ModalExclusionType.APPLICATION_EXCLUDE);
            jrViewer.setTitle("Visualização de Relatório - Doghost");
            jrViewer.setVisible(true);
            jrViewer.setDefaultCloseOperation(JasperViewer.DISPOSE_ON_CLOSE);
        } catch (JRException ex) {
            ex.printStackTrace();
        }
    }

    private JRDataSource createReportDataSource(EntityManager em, Query query) {
        JRBeanCollectionDataSource dataSource;
        dataSource = new JRBeanCollectionDataSource(query.getResultList());
        return dataSource;
    }
}
