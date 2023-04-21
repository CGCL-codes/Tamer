package tyrex.util;

import java.util.Enumeration;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.FileInputStream;
import java.io.File;
import junit.framework.*;
import junit.extensions.*;

/**
 *
 * @author <a href="mailto:mills@intalio.com">David Mills</a>
 * @version $Revision: 1.4 $
 */
public class HashIntTableTest extends TestCase {

    private PrintWriter _logger = null;

    public HashIntTableTest(String name) {
        super(name);
    }

    public void setUp() {
        _logger = new PrintWriter(System.out);
    }

    public void tearDown() {
        _logger.flush();
    }

    /**
     * <p>Create an instance.  Populate it, change some of the values
     * associated with various keys.  Remove instances.  Ensure it
     * behaves as expected.</p>
     *
     * @result Ensure that after creation that the table is empty.
     * Add 3 values.  Ensure that the table size is 3 and that the
     * values are retrievable using get().  Ensure that attempts to
     * retieve a non-existent value returns the default value of 0.
     *
     * <p>Increment on of the values and ensure that it's new value is
     * retrievable.  Remove a value and ensure that the table size is
     * suitably reduced.  Call keys() and ensure that the remaining
     * keys are returned in the enumeration.  Create a new
     * HashIntTable but this time with a different default value.
     * Call get() with a non-existent value again and this time ensure
     * that the new default value is returned.</p>
     */
    public void testBasicFunctionality() throws Exception {
        HashIntTable table = new HashIntTable();
        assertEquals("Size", 0, table.size());
        Integer i1 = new Integer(1);
        Integer i2 = new Integer(2);
        Integer i3 = new Integer(3);
        Integer i4 = new Integer(4);
        table.put(i1, 1);
        table.put(i2, 2);
        table.put(i3, 3);
        assertEquals("Size", 3, table.size());
        assertEquals("get()", 1, table.get(i1));
        assertEquals("get()", 2, table.get(i2));
        assertEquals("get()", 3, table.get(i3));
        assertEquals("get()", 0, table.get(i4));
        table.increment(i2, 5);
        assertEquals("get()", 7, table.get(i2));
        table.remove(i1);
        assertEquals("Size", 2, table.size());
        Enumeration keys = table.keys();
        assert (keys.hasMoreElements());
        assertEquals("Key", i2, keys.nextElement());
        assert (keys.hasMoreElements());
        assertEquals("Key", i3, keys.nextElement());
        assert (!keys.hasMoreElements());
        table = new HashIntTable(10, 999);
        table.put(i1, 1);
        table.put(i2, 2);
        table.put(i3, 3);
        assertEquals("Size", 3, table.size());
        assertEquals("get()", 1, table.get(i1));
        assertEquals("get()", 2, table.get(i2));
        assertEquals("get()", 3, table.get(i3));
        assertEquals("get()", 999, table.get(i4));
    }

    /** Adds a message in the log (except if the log is null)*/
    private void logMessage(String message) {
        if (_logger != null) {
            _logger.println(message);
        }
    }

    public static void main(String args[]) {
        tyrex.Unit.runTests(args, new TestSuite(HashIntTableTest.class));
    }
}
