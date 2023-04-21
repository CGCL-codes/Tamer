package test.datamagic;

import java.util.Collection;
import org.dbunit.Assertion;
import org.dbunit.database.IDatabaseConnection;
import org.dbunit.dataset.IDataSet;
import org.dbunit.dataset.ITable;
import org.dbunit.dataset.xml.FlatXmlDataSet;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;
import com.bft.commons.standard.util.ResourceLoader;
import com.bft.datamagic.gui.StdDataMagicMain;

@SuppressWarnings("unchecked")
@RunWith(Parameterized.class)
public class TestStdDataMagicMain {

    private static String TESTCASES_SETUP = RatSuite.CASE_PACKAGE + "StdTestCasesSetup.xml";

    private String sourceDS;

    private String targetDS;

    private String sourceImportDataXml;

    private String targetImportDataXml;

    private String actualTable;

    private String expectedTable;

    private String expectedDataXml;

    private String synchSpecXml;

    private String[] assertionIgnoreCols;

    @Parameters
    @SuppressWarnings("unchecked")
    public static Collection prepareData() throws Exception {
        return RatSuite.getCollection(TESTCASES_SETUP, "TESTCASE");
    }

    public TestStdDataMagicMain(String sourceDS, String targetDS, String sourceImportDataXml, String targetImportDataXml, String actualTable, String expectedTable, String expectedDataXml, String synchSpecXml, String[] assertionIgnoreCols) {
        this.sourceDS = sourceDS;
        this.targetDS = targetDS;
        this.sourceImportDataXml = sourceImportDataXml;
        this.targetImportDataXml = targetImportDataXml;
        this.actualTable = actualTable;
        this.expectedTable = expectedTable;
        this.expectedDataXml = expectedDataXml;
        this.synchSpecXml = synchSpecXml;
        this.assertionIgnoreCols = assertionIgnoreCols;
    }

    @Test
    public void stdTest() throws Exception {
        IDatabaseConnection dbSourceConn = DBConnection.getConnection(sourceDS);
        IDatabaseConnection dbTargetConn = DBConnection.getConnection(targetDS);
        if (sourceImportDataXml != null) {
            DBConnection.importRawData(dbSourceConn, sourceImportDataXml);
        }
        if (targetImportDataXml != null) {
            DBConnection.importRawData(dbTargetConn, targetImportDataXml);
        }
        StdDataMagicMain.main(new String[] { synchSpecXml });
        IDataSet databaseDataSet = dbTargetConn.createDataSet();
        ITable actTable = databaseDataSet.getTable(actualTable);
        IDataSet expectedDataSet = new FlatXmlDataSet(ResourceLoader.getResourceAsStream(expectedDataXml));
        ITable expdTable = expectedDataSet.getTable(expectedTable);
        Assertion.assertEqualsIgnoreCols(expdTable, actTable, assertionIgnoreCols);
        dbSourceConn.close();
        dbTargetConn.close();
    }
}
