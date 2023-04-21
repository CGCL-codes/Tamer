package org.firebirdsql.jdbc.field;

import java.math.BigDecimal;
import java.sql.Date;
import java.sql.SQLException;
import org.firebirdsql.gds.XSQLVAR;
import org.firebirdsql.gds.ISCConstants;
import junit.framework.Test;
import junit.framework.TestSuite;

/**
 * Describe class <code>TestFBDateField</code> here.
 *
 * @author <a href="mailto:rrokytskyy@users.sourceforge.net">Roman Rokytskyy</a>
 * @version 1.0
 */
public class TestFBDateField extends BaseTestFBField {

    public TestFBDateField(String testName) {
        super(testName);
    }

    public static Test suite() {
        return new TestSuite(TestFBDateField.class);
    }

    protected void setUp() throws SQLException {
        final XSQLVAR[] xsqlvars = new XSQLVAR[1];
        xsqlvars[0] = createXSQLVAR();
        xsqlvars[0].sqltype = ISCConstants.SQL_TYPE_DATE;
        field = FBField.createField(xsqlvars[0], createDataProvider(xsqlvars), null, false);
    }

    protected void tearDown() {
    }

    public void testShort() throws SQLException {
        try {
            super.testShort();
            assertTrue("This method should fail.", false);
        } catch (SQLException ex) {
        }
    }

    public void testDouble() throws SQLException {
        try {
            super.testDouble();
            assertTrue("This method should fail.", false);
        } catch (SQLException ex) {
        }
    }

    public void testLong() throws SQLException {
        try {
            super.testLong();
            assertTrue("This method should fail.", false);
        } catch (SQLException ex) {
        }
    }

    public void testUnicodeStream() throws SQLException {
        try {
            super.testUnicodeStream();
            assertTrue("This method should fail.", false);
        } catch (SQLException ex) {
        }
    }

    public void testByte() throws SQLException {
        try {
            super.testByte();
            assertTrue("This method should fail.", false);
        } catch (SQLException ex) {
        }
    }

    public void testBoolean() throws SQLException {
        try {
            super.testBoolean();
            assertTrue("This method should fail.", false);
        } catch (SQLException ex) {
        }
    }

    public void testBinaryStream() throws SQLException {
        try {
            super.testBinaryStream();
            assertTrue("This method should fail.", false);
        } catch (SQLException ex) {
        }
    }

    public void testFloat() throws SQLException {
        try {
            super.testFloat();
            assertTrue("This method should fail.", false);
        } catch (SQLException ex) {
        }
    }

    public void testBytes() throws SQLException {
        try {
            super.testBytes();
            assertTrue("This method should fail.", false);
        } catch (SQLException ex) {
        }
    }

    public void testAsciiStream() throws SQLException {
        try {
            super.testAsciiStream();
            assertTrue("This method should fail.", false);
        } catch (SQLException ex) {
        }
    }

    public void testInteger() throws SQLException {
        try {
            super.testInteger();
            assertTrue("This method should fail.", false);
        } catch (SQLException ex) {
        }
    }

    public void testBigDecimal() throws SQLException {
        try {
            field.setBigDecimal(new BigDecimal(TEST_DOUBLE));
            assertTrue("This method should fail.", false);
        } catch (SQLException ex) {
        }
    }

    public void testTime() throws SQLException {
        try {
            super.testTime();
            assertTrue("This method should fail.", false);
        } catch (SQLException ex) {
        }
    }

    public void testTimestamp() throws SQLException {
        String dateStr = new Date(TEST_TIMESTAMP.getTime()).toString();
        field.setTimestamp(TEST_TIMESTAMP);
        field.copyOI();
        assertTrue("Timestamp value test failure.", field.getDate().toString().equals(dateStr));
    }

    public void testString() throws SQLException {
        field.setString(TEST_DATE.toString());
        field.copyOI();
        assertTrue("String value test failure", field.getString().equals(TEST_DATE.toString()));
    }

    public void testObject() throws SQLException {
        field.setObject(TEST_DATE);
        field.copyOI();
        assertTrue("Object value test failure", field.getString().equals(TEST_DATE.toString()));
    }
}
