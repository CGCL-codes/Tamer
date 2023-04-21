package net.sourceforge.sql.function;

import java.util.Date;
import net.sourceforge.sql.function.system.HostNameFunction;
import net.sourceforge.sql.function.system.IsDateFunction;
import net.sourceforge.sql.function.system.IsNullFunction;
import net.sourceforge.sql.function.system.IsNumericFunction;
import net.sourceforge.sql.function.system.NewIdFunction;
import net.sourceforge.sql.function.system.NullIfFunction;
import net.sourceforge.sql.function.system.SystemUtil;
import junit.framework.TestCase;

/**
 * 
 * @author ekonstantinov
 * 
 */
public class SystemTestCase extends TestCase {

    public void testIsNull() {
        assertEquals(new Integer(5), IsNullFunction.execute(new Integer(5), null));
        assertEquals("test", IsNullFunction.execute(null, "test"));
        assertEquals("test a", IsNullFunction.execute("test a", "test b"));
        assertEquals("test", IsNullFunction.execute(SystemUtil.NULL_OBJECT, "test"));
    }

    public void testIsDate() {
        assertEquals(new Integer(0), IsDateFunction.execute(null));
        assertEquals(new Integer(0), IsDateFunction.execute(SystemUtil.NULL_OBJECT));
        assertEquals(new Integer(1), IsDateFunction.execute(new Date()));
        assertEquals(new Integer(1), IsDateFunction.execute("20010101"));
    }

    public void testIsNumeric() {
        assertEquals(new Integer(0), IsNumericFunction.execute(null));
        assertEquals(new Integer(0), IsNumericFunction.execute(SystemUtil.NULL_OBJECT));
        assertEquals(new Integer(1), IsNumericFunction.execute(new Integer(1)));
        assertEquals(new Integer(0), IsNumericFunction.execute(new Object()));
        assertEquals(new Integer(1), IsNumericFunction.execute("7777"));
        assertEquals(new Integer(1), IsNumericFunction.execute("7777.7"));
        assertEquals(new Integer(0), IsNumericFunction.execute("abcd"));
    }

    public void testNullIf() {
        assertEquals("test", NullIfFunction.execute("test", "test"));
        assertEquals(5, NullIfFunction.execute(5, 5));
        assertEquals(5.7, NullIfFunction.execute(5.7, 5.7));
        assertEquals(SystemUtil.NULL_OBJECT, NullIfFunction.execute("test", SystemUtil.NULL_OBJECT));
        assertEquals(SystemUtil.NULL_OBJECT, NullIfFunction.execute("test", SystemUtil.NULL_OBJECT));
    }

    public void testNewId() {
        assertNotNull(NewIdFunction.execute());
        assertNotSame(NewIdFunction.execute(), NewIdFunction.execute());
    }

    public void testHostName() {
        assertNotNull(HostNameFunction.execute());
        System.out.println(HostNameFunction.execute());
    }
}
