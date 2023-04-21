package org.codecover.report;

import java.io.File;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import junit.framework.TestCase;
import org.codecover.model.utils.*;
import org.codecover.model.extensions.*;
import org.codecover.metrics.*;
import org.codecover.report.exceptions.FileCreationException;
import org.codecover.report.exceptions.ReportException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

/**
 * Tests the class <code>org.codecover.report.Report</code>.
 *
 * @author Robert Hanussek
 * @version 1.0 ($Id: ReportTest.java 1 2007-12-12 17:37:26Z t-scheller $)
 */
public class ReportTest extends TestCase {

    private File directory;

    private Document template;

    private static final String FILEPATH_INDEXFILE = "indexfile";

    private static final String DUMMYREPORTGEN_PATH = "org.codecover.report.dummy.DummyReportGenerator";

    public static final String DUMMYREPORTGEN_LOAD_SUCCESS = "Successfull";

    protected void setUp() throws Exception {
        this.directory = new File(System.getProperty("java.io.tmpdir"), "testSetFileSystemPath" + (new Long(System.nanoTime())).toString());
        if (this.directory.exists()) {
            throw new Exception("Temporary output directory already exists" + " (" + this.directory.getAbsolutePath() + ").");
        }
        if (!this.directory.mkdir()) {
            throw new Exception("Temporary output directory could not be" + " created. (" + this.directory.getAbsolutePath() + ").");
        }
        this.template = createDummyTemplate();
    }

    protected void tearDown() throws Exception {
        this.directory.delete();
    }

    /**
     * Tests the method <code>setFileSystemPath</code> by checking if it sets
     * the correct directory name.
     */
    public void testSetFileSystemPath() throws FileCreationException {
        PluginManager pluginManager = PluginManager.create();
        File indexfile = new File(this.directory, FILEPATH_INDEXFILE);
        Report report = new Report(pluginManager, MetricProvider.getAvailabeMetrics(pluginManager, Logger.NULL), Logger.NULL);
        report.setFileSystemPath(indexfile.getAbsolutePath());
        assertEquals(FILEPATH_INDEXFILE + Report.OUTPUT_DIR_SUFFIX, report.getDirectoryName());
        indexfile.delete();
        (new File(indexfile.getParent(), report.getDirectoryName())).delete();
    }

    /**
     * Tests the method <code>generateReport</code> of class Report by checking
     * if it loads a dummy report generator (<code>DummyReportGenerator</code>)
     * successfully.
     */
    public void testGenerateReport() throws ReportException {
        PluginManager pluginManager = PluginManager.create();
        Report report = new Report(pluginManager, MetricProvider.getAvailabeMetrics(pluginManager, Logger.NULL), Logger.NULL);
        report.setTemplate(this.template);
    }

    /**
     * Creates a dummy template to test method <code>generateReport</code>.
     *
     * @see #testGenerateReport()
     */
    private Document createDummyTemplate() throws ParserConfigurationException {
        DocumentBuilderFactory docBFac = DocumentBuilderFactory.newInstance();
        DocumentBuilder docBuild = docBFac.newDocumentBuilder();
        Document template = docBuild.newDocument();
        Element repElemChild;
        Element reportElement = template.createElementNS(Template.REPORT_NAMESPACE, Template.REPORT_ELEMENT);
        reportElement.setAttribute(Template.REPORT_VERSION_ATTR, Template.READER_VERSION);
        template.appendChild(reportElement);
        repElemChild = template.createElementNS(Template.REPORT_NAMESPACE, Template.GENERATOR_ELEMENT);
        repElemChild.setTextContent(ReportTest.DUMMYREPORTGEN_PATH);
        reportElement.appendChild(repElemChild);
        repElemChild = template.createElementNS(Template.REPORT_NAMESPACE, Template.NAME_ELEMENT);
        repElemChild.setAttributeNS(Template.XML_NAMESPACE, Template.NAME_LANG_ATTR, "en");
        repElemChild.setTextContent("Dummy report generator");
        reportElement.appendChild(repElemChild);
        repElemChild = template.createElementNS(Template.REPORT_NAMESPACE, Template.DESC_ELEMENT);
        repElemChild.setAttributeNS(Template.XML_NAMESPACE, Template.DESC_LANG_ATTR, "en");
        repElemChild.setTextContent("Dummy report generator for testing.");
        reportElement.appendChild(repElemChild);
        repElemChild = template.createElement(Template.TEMPLATE_ELEMENT);
        repElemChild.setAttribute(Template.TEMPLATE_VERSION_ATTR, "1");
        reportElement.appendChild(repElemChild);
        return template;
    }
}
