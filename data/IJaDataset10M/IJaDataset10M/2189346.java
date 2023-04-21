package test.database;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import org.dbunit.dataset.DataSetException;
import org.dbunit.dataset.IDataSet;
import org.dbunit.dataset.ITable;
import org.dbunit.dataset.xml.FlatXmlDataSet;
import org.junit.runner.JUnitCore;
import org.junit.runner.Result;
import org.junit.runner.notification.Failure;
import com.bft.commons.standard.util.ResourceLoader;

public class AllTests {

    private static String CASE_PACKAGE = "test/database/";

    public static void main(String[] args) throws Exception {
        IDataSet expectedDataSet = new FlatXmlDataSet(ResourceLoader.getResourceAsStream(CASE_PACKAGE + "testcases.xml"));
        ITable expectedTable = expectedDataSet.getTable("TESTCASE");
        JUnitCore d = new JUnitCore();
        DataMagicTest.setPrepareData(getCollection(expectedTable));
        Result result = d.run(DataMagicTest.class);
        printTestResult(result);
    }

    @SuppressWarnings("unchecked")
    public static Collection getCollection(ITable table) throws DataSetException {
        int rowCount = table.getRowCount();
        int colCount = table.getTableMetaData().getColumns().length;
        Object[][] object = new Object[rowCount][colCount - 1];
        for (int i = 0; i < rowCount; i++) {
            String path = (String) table.getValue(i, "PATH");
            object[i][0] = table.getValue(i, "SOURCEDS");
            object[i][1] = table.getValue(i, "TARGETDS");
            object[i][2] = path + table.getValue(i, "SOURCEIMPORTDATAXML");
            object[i][3] = path + table.getValue(i, "TARGETIMPORTDATAXML");
            object[i][4] = table.getValue(i, "ACTUALTABLE");
            object[i][5] = table.getValue(i, "EXPECTEDTABLE");
            object[i][6] = path + table.getValue(i, "EXPECTEDDATAXML");
            object[i][7] = path + table.getValue(i, "SYNCHSPECXML");
            object[i][8] = ((String) table.getValue(i, "ASSERTIONIGNORECOLS")).split(",");
        }
        return Arrays.asList(object);
    }

    public static void printTestResult(Result result) {
        System.out.print("Runs: " + result.getRunCount());
        System.out.print("    Failures: " + result.getFailureCount());
        System.out.println("    Ignores: " + result.getIgnoreCount());
        if (result.getFailureCount() > 0) {
            List<Failure> failures = result.getFailures();
            for (int i = 0; i < failures.size(); i++) {
                Failure failure = failures.get(i);
                System.out.println("failure" + i + ": " + failure);
            }
        }
    }
}
