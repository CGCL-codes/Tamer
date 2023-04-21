package edu.stanford;

import java.io.*;
import javax.xml.parsers.ParserConfigurationException;
import org.xml.sax.SAXException;
import static org.junit.Assert.fail;
import org.solrmarc.testUtils.IndexTest;
import org.solrmarc.testUtils.SolrFieldMappingTest;

/**
 * Site Specific code used for testing the Stanford Blacklight index
 * @author Naomi Dushay
 */
public abstract class AbstractStanfordBlacklightTest extends IndexTest {

    /** testDataParentPath is used for mapping tests - full path is needed */
    String testDataParentPath = null;

    /** SolrFieldMappingTest object to be used in specific tests */
    protected SolrFieldMappingTest solrFldMapTest = null;

    {
        String solrPath = System.getProperty("solr.path");
        if (solrPath == null) {
            solrPath = "home" + File.separator + "solrmarc" + File.separator + "jetty" + File.separator + "solr";
            System.setProperty("solr.path", solrPath);
        }
        String configPropDir = System.getProperty("test.config.dir");
        String configPropFile = System.getProperty("test.config.file");
        if (configPropFile == null) {
            configPropFile = new File(configPropDir, "sw_config.properties").getAbsolutePath();
            System.setProperty("test.config.file", configPropFile);
        }
        testDataParentPath = System.getProperty("test.data.path");
        if (testDataParentPath == null) {
            testDataParentPath = System.getProperty("test.data.parent.path");
            if (testDataParentPath == null) testDataParentPath = "examples" + File.separator + "stanfordBlacklight" + File.separator + "test" + File.separator + "data";
            System.setProperty("test.data.path", testDataParentPath);
        }
        String solrDataDir = System.getProperty("solr.data.dir");
        if (solrDataDir == null) solrDataDir = solrPath + File.separator + "data";
    }

    /**
	 * initialization for mapping tests
	 */
    public void mappingTestInit() {
        docIDfname = "id";
        String testDataParentPath = System.getProperty("test.data.path");
        if (testDataParentPath == null) {
            fail("property test.data.path must be defined for the tests to run");
        }
        String anyTestFile = new File(testDataParentPath, "pubDateTests.mrc").getAbsolutePath();
        System.setProperty("marc.source", "FILE");
        System.setProperty("marc.path", anyTestFile);
        String testConfigFname = System.getProperty("test.config.file");
        solrFldMapTest = new SolrFieldMappingTest(testConfigFname, docIDfname);
    }

    /**
	 * creates an index from the indicated test file, and initializes 
	 *  necessary variables
	 */
    public void createIxInitVars(String testDataFname) {
        docIDfname = "id";
        String solrPath = System.getProperty("solr.path");
        if (solrPath == null) fail("property solr.path must be defined for the tests to run");
        String solrDataDir = System.getProperty("solr.data.dir");
        if (solrPath == null) fail("property solr.path must be defined for the tests to run");
        String testDataParentPath = System.getProperty("test.data.path");
        if (testDataParentPath == null) fail("property test.data.path must be defined for the tests to run");
        String testConfigFname = System.getProperty("test.config.file");
        if (testConfigFname == null) fail("property test.config.file must be defined for the tests to run");
        createIxInitVars(testConfigFname, solrPath, solrDataDir, testDataParentPath, testDataFname);
    }
}
