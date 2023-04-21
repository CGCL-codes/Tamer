package tests.com.scholardesk.utilities;

import java.lang.reflect.Method;
import com.scholardesk.utilities.DbUtil;
import junit.framework.TestCase;

/**
 * Tests the {@link com.scholardesk.utilities.DbUtil} class.
 * 
 * @author Christopher Dunavant
 * @version $Revision: 1 $
 */
public class DbUtilTest extends TestCase {

    public DbUtilTest(String name) {
        super(name);
    }

    /**
	 * Tests that an sql string is properly quoted
	 */
    public void testQuote() {
        String in_string = "'string'";
        String out_string = DbUtil.quote("string");
        assertEquals(in_string, out_string);
        in_string = "'string\\%'";
        out_string = DbUtil.quote("string%");
        assertEquals(in_string, out_string);
        in_string = "'string\\''";
        out_string = DbUtil.quote("string'");
        assertEquals(in_string, out_string);
        in_string = "'string\\\"'";
        out_string = DbUtil.quote("string\"");
        assertEquals(in_string, out_string);
        in_string = "'string\\\\'";
        out_string = DbUtil.quote("string\\\\");
        assertEquals(in_string, out_string);
        in_string = "'\\\\" + "\\\"" + "\\'" + "\\%'";
        out_string = DbUtil.quote("\\\\\"'%");
        assertEquals(in_string, out_string);
    }

    public void testGetMethodName() throws Exception {
        String out_string = "lastName";
        Method method = this.getClass().getMethod("getLastName", new Class[0]);
        String in_string = DbUtil.getMethodName(method, "get");
        assertEquals(in_string, out_string);
        method = this.getClass().getMethod("setLastName", new Class[0]);
        in_string = DbUtil.getMethodName(method, "set");
        assertEquals(in_string, out_string);
    }

    public void testIsGetter() throws Exception {
        Method method = this.getClass().getMethod("getLastName", new Class[0]);
        assertTrue(DbUtil.isGetter(method));
        Method method2 = this.getClass().getMethod("setLastName", new Class[0]);
        assertFalse(DbUtil.isGetter(method2));
    }

    public void testIsSetter() throws Exception {
        Method method = this.getClass().getMethod("setLastName", new Class[0]);
        assertTrue(DbUtil.isSetter(method));
        Method method2 = this.getClass().getMethod("getLastName", new Class[0]);
        assertFalse(DbUtil.isSetter(method2));
    }

    public void testClassToDbTable() {
        String orig_string = "com.scholardesk.Person";
        String in_string = DbUtil.classToDbTable(orig_string);
        String out_string = "person";
        assertEquals(in_string, out_string);
    }

    public void getLastName() {
    }

    public void setLastName() {
    }
}
