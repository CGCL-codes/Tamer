package net.sourceforge.jtds.test;

import java.sql.Types;
import junit.framework.TestCase;
import net.sourceforge.jtds.jdbc.TypeInfo;

/**
 * Tests for the <code>TypeInfo</code> class.
 *
 * @author David Eaves
 * @version $Id: TypeInfoTest.java,v 1.1.1.1 2007/01/24 20:51:58 mwillett Exp $
 */
public class TypeInfoTest extends TestCase {

    public TypeInfoTest(String testName) {
        super(testName);
    }

    public void testCharTypes() {
        TypeInfo charType = new TypeInfo("char", Types.CHAR, false);
        TypeInfo nchar = new TypeInfo("nchar", -8, false);
        TypeInfo uniqueid = new TypeInfo("uniqueidentifier", -11, false);
        assertComparesLessThan(charType, nchar);
        assertComparesLessThan(nchar, uniqueid);
    }

    public void testVarcharTypes() {
        TypeInfo varchar = new TypeInfo("varchar", Types.VARCHAR, false);
        TypeInfo nvarchar = new TypeInfo("nvarchar", -9, false);
        TypeInfo sysname = new TypeInfo("sysname", -9, false);
        TypeInfo variant = new TypeInfo("sql_variant", -150, false);
        assertComparesLessThan(varchar, nvarchar);
        assertComparesLessThan(nvarchar, sysname);
        assertComparesLessThan(sysname, variant);
    }

    public void testCompareToDifferentDataType() {
        TypeInfo decimal = new TypeInfo("decimal", Types.DECIMAL, false);
        TypeInfo integer = new TypeInfo("integer", Types.INTEGER, false);
        assertComparesLessThan(decimal, integer);
    }

    public void testCompareToIdentity() {
        TypeInfo bigint = new TypeInfo("bigint", Types.BIGINT, false);
        TypeInfo bigintIdentity = new TypeInfo("bigint identity", Types.BIGINT, true);
        assertComparesLessThan(bigint, bigintIdentity);
    }

    private void assertComparesLessThan(TypeInfo t1, TypeInfo t2) {
        assertTrue(t1 + " < " + t2 + " failed", t1.compareTo(t2) < 0);
    }

    public static void main(String[] args) {
        junit.textui.TestRunner.run(TypeInfoTest.class);
    }
}
