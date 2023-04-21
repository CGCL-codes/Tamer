package org.dbunit.database.search;

import org.dbunit.database.IDatabaseConnection;

/**  
 * @author Felipe Leme (dbunit@felipeal.net)
 * @version $Revision: 769 $
 * @since Aug 28, 2005
 */
public class ImportAndExportNodesFilterSearchCallbackTest extends ImportNodesFilterSearchCallbackTest {

    public ImportAndExportNodesFilterSearchCallbackTest(String testName) {
        super(testName);
    }

    protected String[][] getExpectedOutput() {
        int size = getInput().length;
        String[][] result = new String[size][];
        String[] allResults = super.getExpectedOutput()[1];
        for (int i = 0; i < result.length; i++) {
            result[i] = allResults;
        }
        return result;
    }

    protected AbstractMetaDataBasedSearchCallback getCallback(IDatabaseConnection connection) {
        return new ImportedAndExportedKeysSearchCallback(connection);
    }
}
