package fr.gfi.foundation.core.template;

import static org.testng.Assert.*;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.view.JasperViewer;
import org.testng.annotations.Test;

/**
 * Test case for JasperReportManager.
 *
 * @author Tiago Fernandez
 * @since 0.1
 */
public class JasperTemplateManagerTest {

    private JasperTemplateManager jasperMgr = new JasperTemplateManager();

    private JasperPrint output;

    @Test
    public void generateOutput() throws Exception {
        TemplateInfo info = new TemplateInfo();
        info.setSourcePath("fr/gfi/foundation/core/template/Celebrities.jasper");
        info.addBean("celebrityList", new Celebrity("Angelina Jolie", "Los Angeles", "United States"));
        info.addBean("celebrityList", new Celebrity("Gisele Bundchen", "Horizontina", "Brazil"));
        info.addBean("celebrityList", new Celebrity("Monica Bellucci", "Citta di Castello", "Italia"));
        output = (JasperPrint) jasperMgr.generateOutput(info);
        assertNotNull(output);
    }

    @Test(dependsOnMethods = { "generateOutput" }, enabled = false)
    public void visualizeReport() throws Exception {
        new JasperViewer(output, false).setVisible(true);
        Thread.sleep(10000);
    }

    @Test(dependsOnMethods = { "generateOutput" })
    public void exportReportToPdf() throws Exception {
        byte[] pdfReport = JasperTemplateManager.exportReportToPdf(output);
        assertNotNull(pdfReport);
        assertTrue(pdfReport.length > 0);
    }
}
