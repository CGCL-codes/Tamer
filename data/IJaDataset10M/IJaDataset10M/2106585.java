package org.dbunit.dataset;

import java.io.FileReader;
import org.dbunit.dataset.xml.XmlDataSet;
import org.dbunit.testutil.TestUtils;

/**
 * @author Manuel Laflamme
 * @version $Revision: 1162 $
 * @since Feb 22, 2002
 */
public class FilteredDataSetTest extends AbstractDataSetTest {

    public FilteredDataSetTest(String s) {
        super(s);
    }

    protected IDataSet createDataSet() throws Exception {
        IDataSet dataSet1 = new XmlDataSet(TestUtils.getFileReader("xml/dataSetTest.xml"));
        IDataSet dataSet2 = new XmlDataSet(TestUtils.getFileReader("xml/filteredDataSetTest.xml"));
        IDataSet dataSet = new CompositeDataSet(dataSet1, dataSet2);
        assertEquals("count before filter", getExpectedNames().length + 1, dataSet.getTableNames().length);
        return new FilteredDataSet(getExpectedNames(), dataSet);
    }

    protected IDataSet createDuplicateDataSet() throws Exception {
        IDataSet dataSet1 = new XmlDataSet(TestUtils.getFileReader("xml/xmlDataSetDuplicateTest.xml"));
        IDataSet dataSet2 = new XmlDataSet(TestUtils.getFileReader("xml/filteredDataSetTest.xml"));
        assertEquals(2, dataSet1.getTableNames().length);
        assertEquals(1, dataSet2.getTableNames().length);
        IDataSet dataSet = new CompositeDataSet(dataSet1, dataSet2, false);
        assertEquals("count before filter", 3, dataSet.getTableNames().length);
        return new FilteredDataSet(getExpectedDuplicateNames(), dataSet);
    }

    protected IDataSet createMultipleCaseDuplicateDataSet() throws Exception {
        String[] names = getExpectedDuplicateNames();
        names[0] = names[0].toLowerCase();
        return new FilteredDataSet(names, createDuplicateDataSet());
    }

    public void testGetFilteredTableNames() throws Exception {
        String[] originalNames = getExpectedNames();
        String expectedName = originalNames[0];
        IDataSet dataSet = createDataSet();
        assertTrue("original count", dataSet.getTableNames().length > 1);
        IDataSet filteredDataSet = new FilteredDataSet(new String[] { expectedName }, dataSet);
        assertEquals("filtered count", 1, filteredDataSet.getTableNames().length);
        assertEquals("filtered names", expectedName, filteredDataSet.getTableNames()[0]);
    }

    public void testGetFilteredTable() throws Exception {
        String[] originalNames = getExpectedNames();
        IDataSet filteredDataSet = new FilteredDataSet(new String[] { originalNames[0] }, createDataSet());
        for (int i = 0; i < originalNames.length; i++) {
            String name = originalNames[i];
            if (i == 0) {
                assertEquals("table " + i, name, filteredDataSet.getTable(name).getTableMetaData().getTableName());
            } else {
                try {
                    filteredDataSet.getTable(name);
                    fail("Should throw a NoSuchTableException");
                } catch (NoSuchTableException e) {
                }
            }
        }
    }

    public void testGetFilteredTableMetaData() throws Exception {
        String[] originalNames = getExpectedNames();
        IDataSet filteredDataSet = new FilteredDataSet(new String[] { originalNames[0] }, createDataSet());
        for (int i = 0; i < originalNames.length; i++) {
            String name = originalNames[i];
            if (i == 0) {
                assertEquals("table " + i, name, filteredDataSet.getTableMetaData(name).getTableName());
            } else {
                try {
                    filteredDataSet.getTableMetaData(name);
                    fail("Should throw a NoSuchTableException");
                } catch (NoSuchTableException e) {
                }
            }
        }
    }
}
