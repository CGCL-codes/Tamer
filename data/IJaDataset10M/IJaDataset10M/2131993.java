package net.sourceforge.jxweb;

import com.meterware.httpunit.*;
import net.sourceforge.jxunit.*;

/**
 * This class is a test step for retrieving a cell value from a specified
 * <code>WebTable</code> that has already been placed into the
 * test properties.
 * @author avollmer
 */
public class GetTableValueStep extends HttpStep {

    /** The name to store the table cell value under in the test properties */
    public String name;

    /** The name of the <code>WebTable</code> to look for in the test properties */
    public String tableName;

    /** Flag indicating that the 'tableName' attribute should be dereferenced in the test properties */
    public boolean indirect;

    /** The row of the cell to retrieve. Note that this is 0-based. */
    public int row;

    /** The column of the cell to retrieve. Note that this is 0-based. */
    public int column;

    /**
     * Overrides parent <code>eval()</code> method
     * @param testCase
     * @throws Throwable
     */
    public void eval(JXTestCase testCase) throws Throwable {
        super.eval(testCase);
        WebTable table = (WebTable) properties.get(testCase.getStringValue(tableName, indirect));
        String value = null;
        if (table == null) {
            throw new Exception("No WebTable found named '" + testCase.getStringValue(tableName, indirect) + "'");
        }
        try {
            value = table.getCellAsText(row, column);
        } catch (IndexOutOfBoundsException ibe) {
            System.out.println("WARNING: There is no table cell at row " + row + " column " + column);
        }
        properties.put(name, value);
    }
}
