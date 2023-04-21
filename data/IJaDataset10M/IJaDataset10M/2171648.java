package net.sourceforge.squirrel_sql.fw.sql;

import java.lang.reflect.Field;
import java.sql.Types;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import net.sourceforge.squirrel_sql.BaseSQuirreLTestCase;

/**
 * 
 * Uses reflection to test JDBCTypeMapper against java.sql.Types.  This will 
 * help catch (when upgrading to a new release) any new types that aren't 
 * accounted for.
 * 
 * @author manningr
 */
public class JDBCTypeMapperTest extends BaseSQuirreLTestCase {

    protected void setUp() throws Exception {
        super.setUp();
    }

    protected void tearDown() throws Exception {
        super.tearDown();
    }

    public void testGetJdbcTypeList() {
        String[] typeListArr = JDBCTypeMapper.getJdbcTypeList();
        List<String> typeList = Arrays.asList(typeListArr);
        Set<String> s = new HashSet<String>();
        s.addAll(typeList);
        Field[] fields = java.sql.Types.class.getDeclaredFields();
        for (int i = 0; i < fields.length; i++) {
            Field field = fields[i];
            String fieldName = field.getName();
            assertTrue("JDBCTypeMapper.getJdbcTypeList() did not return an item for type: " + fieldName, s.contains(fieldName));
        }
    }

    public void testGetJdbcTypeName() throws Exception {
        Field[] fields = java.sql.Types.class.getDeclaredFields();
        for (int i = 0; i < fields.length; i++) {
            Field field = fields[i];
            String fieldName = field.getName();
            int jdbcType = field.getInt(null);
            String typeName = JDBCTypeMapper.getJdbcTypeName(jdbcType);
            assertEquals(fieldName, typeName);
        }
    }

    public void testGetJdbcType() throws Exception {
        Field[] fields = java.sql.Types.class.getDeclaredFields();
        for (int i = 0; i < fields.length; i++) {
            Field field = fields[i];
            String fieldName = field.getName();
            int expectedType = field.getInt(null);
            int actualType = JDBCTypeMapper.getJdbcType(fieldName);
            assertEquals("Field name=" + fieldName, expectedType, actualType);
        }
        assertEquals("Field name=null", Types.NULL, JDBCTypeMapper.getJdbcType(null));
    }

    public void testIsNumberType() throws Exception {
        Integer[] numberTypes = new Integer[] { Types.BIGINT, Types.DECIMAL, Types.DOUBLE, Types.FLOAT, Types.INTEGER, Types.NUMERIC };
        Set<Integer> s = new HashSet<Integer>();
        s.addAll(Arrays.asList(numberTypes));
        testIsType(s, new NumberTypeCheck());
    }

    public void testIsDateType() throws Exception {
        Integer[] dateTypes = new Integer[] { Types.DATE, Types.TIME, Types.TIMESTAMP };
        Set<Integer> s = new HashSet<Integer>();
        s.addAll(Arrays.asList(dateTypes));
        testIsType(s, new DateTypeCheck());
    }

    public void testIsLongType() throws Exception {
        Integer[] longTypes = new Integer[] { Types.LONGVARBINARY, Types.LONGVARCHAR, Types.BLOB, Types.CLOB };
        Set<Integer> s = new HashSet<Integer>();
        s.addAll(Arrays.asList(longTypes));
        testIsType(s, new LongTypeCheck());
    }

    public void testIsType(Set<Integer> s, TypeCheck checker) throws Exception {
        Field[] fields = java.sql.Types.class.getDeclaredFields();
        for (int i = 0; i < fields.length; i++) {
            Field field = fields[i];
            String fieldName = field.getName();
            Integer jdbcType = field.getInt(null);
            if (s.contains(jdbcType)) {
                assertTrue("Field name=" + fieldName, checker.isType(jdbcType));
            } else {
                assertFalse("Field name=" + fieldName, checker.isType(jdbcType));
            }
        }
    }

    private interface TypeCheck {

        boolean isType(int type);
    }

    private class NumberTypeCheck implements TypeCheck {

        public boolean isType(int type) {
            return JDBCTypeMapper.isNumberType(type);
        }
    }

    private class DateTypeCheck implements TypeCheck {

        public boolean isType(int type) {
            return JDBCTypeMapper.isDateType(type);
        }
    }

    private class LongTypeCheck implements TypeCheck {

        public boolean isType(int type) {
            return JDBCTypeMapper.isLongType(type);
        }
    }
}
