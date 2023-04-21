package newtonERP.orm.fields.field.type;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import java.util.Calendar;
import java.util.GregorianCalendar;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * @author Jonatan Cloutier
 */
public class FieldDateTimeTest {

    /** The field bool. */
    private FieldDateTime fieldDateTime;

    /** The gregorian calendar. */
    private GregorianCalendar gregorianCalendar;

    /**
	 * Sets the up.
	 */
    @Before
    public void setUp() {
        fieldDateTime = new FieldDateTime();
        gregorianCalendar = new GregorianCalendar();
        gregorianCalendar.add(Calendar.DAY_OF_YEAR, -10);
        gregorianCalendar.add(Calendar.HOUR, -3);
    }

    /**
	 * Tear down.
	 */
    @After
    public void tearDown() {
    }

    /**
	 * Test method for {@link newtonERP.orm.fields.field.type.FieldBool#setDataString(java.lang.String)}.
	 */
    @Test
    public final void testSetDataString1() {
        fieldDateTime.setDataString("2001-01-02 03:04:05");
        assertEquals("2001-01-02 03:04:05", fieldDateTime.getDataString());
    }

    /**
	 * Test method for {@link newtonERP.orm.fields.field.type.FieldBool#getDataString()}.
	 */
    @Test
    public final void testGetDataStringEmpty() {
        fieldDateTime.setDataString("2001-01-02 03:04:05");
        assertEquals("2001-01-02 03:04:05", fieldDateTime.getDataString());
    }

    /**
	 * Test method for {@link newtonERP.orm.fields.field.type.FieldBool#setDefaultValue()}.
	 */
    @Test
    public final void testSetDefaultValue() {
        fieldDateTime.setDefaultValue();
        assertTrue(new GregorianCalendar().compareTo(fieldDateTime.getData()) < 1000);
    }

    /**
	 * Test method for {@link newtonERP.orm.fields.field.type.FieldBool#FieldBool()}.
	 */
    @Test
    public final void testFieldSDateTime() {
        FieldDateTime testFieldDateTime = new FieldDateTime();
        assertNotNull(testFieldDateTime);
    }

    /**
	 * Test method for {@link newtonERP.orm.fields.field.type.FieldBool#FieldBool(java.lang.Boolean)}.
	 */
    @Test
    public final void testFieldDateTimeDate() {
        FieldDateTime testFieldDateTime = new FieldDateTime(gregorianCalendar);
        assertNotNull(testFieldDateTime);
        assertEquals(gregorianCalendar, testFieldDateTime.getData());
    }

    /**
	 * Test method for {@link newtonERP.orm.fields.field.InnerField#setData(java.lang.Object)}.
	 */
    @Test
    public final void testSetData() {
        fieldDateTime.setData(gregorianCalendar);
        assertEquals(gregorianCalendar, fieldDateTime.getData());
    }

    /**
	 * Test method for {@link newtonERP.orm.fields.field.InnerField#getData()}.
	 */
    @Test
    public final void testGetData() {
        fieldDateTime.setData(gregorianCalendar);
        assertEquals(gregorianCalendar, fieldDateTime.getData());
    }

    /**
	 * Test method for {@link newtonERP.orm.fields.field.InnerField#reset()}.
	 */
    @Test
    public final void testReset() {
        fieldDateTime.setData(gregorianCalendar);
        fieldDateTime.reset();
        assertNull(fieldDateTime.getData());
    }
}
