package net.sourceforge.btb;

import static junit.framework.Assert.*;
import java.io.File;
import java.util.ArrayList;
import java.util.HashSet;
import net.sourceforge.btb.Bridge;
import net.sourceforge.btb.SqlDescriptor;
import net.sourceforge.btb.types.Employee;
import net.sourceforge.btb.utils.DatabaseManager;
import org.apache.log4j.BasicConfigurator;
import org.junit.BeforeClass;
import org.junit.Test;

public class BasicDataTableTest {

    private SqlDescriptor<Employee> sqlDescriptor;

    private ArrayList<String> columns;

    private static DatabaseManager dbm;

    private static String dbName = "data/testdb";

    @BeforeClass
    public static void setUpBeforeClass() throws Exception {
        BasicConfigurator.configure();
        File testDB = new File(dbName);
        boolean testDBExists = testDB.exists();
        dbm = new DatabaseManager();
        if (!testDBExists) {
            dbm.createDatabase(new File("TestDB.sql"));
        }
    }

    public BasicDataTableTest() throws Exception {
        Bridge<Employee> bridge = new Bridge<Employee>(Employee.class);
        sqlDescriptor = bridge.getDescriptor();
        columns = new ArrayList<String>();
        columns.add("departmentId");
        columns.add("id");
        columns.add("name");
        columns.add("sin");
        columns.add("wages");
    }

    @Test
    public void testGetTableName() {
        String name = sqlDescriptor.getTableName();
        assertEquals("name should be Employee instead of " + name, "employee", name);
    }

    @Test
    public void testGetPrimaryKey() {
        String expected = "id";
        String primKey = sqlDescriptor.getPrimaryKey();
        assertEquals("primKey should be id instead of " + primKey, expected, primKey);
    }

    @Test
    public void testGetSetValue() throws Exception {
        Employee e1 = new Employee();
        e1.setName("Robert Walsh");
        e1.setSin("123 345 567");
        e1.setWages(14.35);
        Employee e2 = new Employee();
        for (String property : columns) {
            if (sqlDescriptor.isAccessible(property)) {
                Object value = sqlDescriptor.getValue(e1, property);
                sqlDescriptor.setValue(e2, property, value);
            } else {
                System.out.println("INACCESSIBLE: " + property);
            }
        }
        assertEquals("e2 should be equal to e1.", e1, e2);
    }

    @Test
    public void testGetColumns() {
        assertEquals("The columns set and descriptor.getColumns should be equal.", new HashSet<String>(columns), new HashSet<String>(sqlDescriptor.getColumns()));
    }

    @Test
    public void testGetSelectQuery() {
        String expectedQuery = "SELECT ";
        int i = 0;
        for (String property : columns) {
            if (i > 0) {
                expectedQuery += ", ";
            }
            expectedQuery += property;
            i++;
        }
        expectedQuery += " FROM employee";
        String actualQuery = sqlDescriptor.getSelectQuery();
        assertEquals("the actual query should be " + expectedQuery + " instead of " + actualQuery, expectedQuery, actualQuery);
    }

    @Test
    public void testGetInsertQuery() {
        String expectedQuery = "INSERT INTO employee ";
        String cols = "(";
        int i = 0;
        for (String column : columns) {
            if (sqlDescriptor.isReadOnly(column)) {
                continue;
            }
            if (i > 0) {
                cols += ", ";
            }
            cols += column;
            i++;
        }
        cols += ")";
        expectedQuery += cols + " VALUES (?, ?, ?, ?)";
        String actualQuery = sqlDescriptor.getInsertQuery();
        assertEquals("the actual query should be " + expectedQuery + " instead of " + actualQuery, expectedQuery, actualQuery);
    }

    @Test
    public void testGetUpdateQuery() {
        String expectedQuery = "UPDATE employee SET ";
        int i = 0;
        for (String column : columns) {
            if (sqlDescriptor.getPrimaryKey().equals(column)) {
                continue;
            }
            if (i > 0) {
                expectedQuery += ", ";
            }
            expectedQuery += column + " = ?";
            i++;
        }
        expectedQuery += " WHERE id = ?";
        String actualQuery = sqlDescriptor.getUpdateQuery();
        assertEquals("the actual query should be " + expectedQuery + " instead of " + actualQuery, expectedQuery, actualQuery);
    }

    @Test
    public void testGetDeleteQuery() {
        String expectedQuery = "DELETE FROM employee WHERE id = ?";
        String actualQuery = sqlDescriptor.getDeleteQuery();
        assertEquals("the actual query should be " + expectedQuery + " instead of " + actualQuery, expectedQuery, actualQuery);
    }

    @Test
    public void testToString() {
        System.out.println(sqlDescriptor);
    }
}
