package org.kopsox.spreadsheet.data.common;

import java.sql.Time;
import java.util.Date;
import org.junit.Before;
import org.junit.Test;
import org.kopsox.spreadsheet.data.Value;

public class TimeValueTest extends AbstractValueTest {

    private final Time t1 = new Time(System.currentTimeMillis());

    private final Time t2 = new Time(System.currentTimeMillis() + 1);

    @Before
    @Override
    public void setUp() throws Exception {
        primaryValue = new TimeValue(t1);
        secondaryValue = new TimeValue(t2);
        thirdValue = new TimeValue(t1);
    }

    @Test
    public void testGetType() {
        assertEquals(Value.Type.TIME, primaryValue.getType());
    }

    @Test
    public void testAsBoolean() {
        assertNull(primaryValue.asBoolean());
    }

    @Test
    public void testAsDate() {
        assertNotNull(primaryValue.asDate());
        assertEquals(primaryValue.asDate(), new Date(t1.getTime()));
        assertNotSame(primaryValue.asDate(), new Date(t2.getTime()));
    }

    @Test
    public void testAsDateString() {
        assertNotNull(primaryValue.asDate("dd-MM-yyyy"));
        assertEquals(primaryValue.asDate("dd-MM-yyyy"), new Date(t1.getTime()));
        assertNotSame(primaryValue.asDate("dd-MM-yyyy"), new Date(t2.getTime()));
    }

    @Test
    public void testAsDouble() {
        assertNull(primaryValue.asDouble());
    }

    @Test
    public void testAsTime() {
        assertNotNull(primaryValue.asTime());
        assertEquals(primaryValue.asTime(), t1);
        assertNotSame(primaryValue.asTime(), t2);
    }

    @Test
    public void testAsString() {
        assertNotNull(primaryValue.asString());
        assertEquals(primaryValue.asString(), t1.toString());
        assertNotSame(primaryValue.asString(), t2.toString());
    }
}
