package com.iplayawriter.novelizer.model;

import static com.iplayawriter.novelizer.test.NovelizerTestHelper.*;
import java.util.Iterator;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Erik
 */
public class LocationTest extends AbstractModelTest {

    private Location location;

    @Before
    public void setUp() throws Exception {
        location = (Location) (getNovelFactory().createNovelElement(INovelElement.Type.LOCATION).asLocation());
        IValues values = new Values();
        values.addValue(ValueFactory.getInstance().createValue(VALUE_NAME, IValue.Type.STRING, LOCATION_NAME));
        values.addValue(ValueFactory.getInstance().createValue(VALUE_DESCRIPTION, IValue.Type.TEXT, LOCATION_DESCRIPTION));
        location.setValues(values);
    }

    /**
     * Test of getLocationTemplate method, of class Location.
     */
    @Test
    public void testGetLocationTemplate() {
        System.out.println("getLocationTemplate");
        IValues result = Location.getLocationTemplate();
        int numValues = 0;
        IValue nameValue = null;
        IValue summaryValue = null;
        Iterator<IValue> iterator = result.getValueIterator();
        while (iterator.hasNext()) {
            IValue value = iterator.next();
            if ("name".equals(value.getName())) {
                nameValue = value;
            } else if ("description".equals(value.getName())) {
                summaryValue = value;
            }
            numValues++;
        }
        assertEquals(2, numValues);
        testValue(nameValue, IValue.Type.STRING, VALUE_NAME, "");
        testValue(summaryValue, IValue.Type.TEXT, VALUE_DESCRIPTION, "");
        nameValue = result.getValue(VALUE_NAME);
        summaryValue = result.getValue(VALUE_DESCRIPTION);
        testValue(nameValue, IValue.Type.STRING, VALUE_NAME, "");
        testValue(summaryValue, IValue.Type.TEXT, VALUE_DESCRIPTION, "");
    }

    /**
     * Test of getName method, of class Location.
     */
    @Test
    public void testGetName() {
        System.out.println("getName");
        assertEquals(LOCATION_NAME, location.getName());
    }

    /**
     * Test of getSummary method, of class Location.
     */
    @Test
    public void testGetSummary() {
        System.out.println("getSummary");
        assertEquals(LOCATION_DESCRIPTION, location.getDescription());
    }

    /**
     * Test of getValues method, of class Location.
     */
    @Test
    public void testGetValues() {
        System.out.println("getValues");
        IValues result = location.getValues();
        int numValues = 0;
        IValue nameValue = null;
        IValue summaryValue = null;
        Iterator<IValue> iterator = result.getValueIterator();
        while (iterator.hasNext()) {
            IValue value = iterator.next();
            if ("name".equals(value.getName())) {
                nameValue = value;
            } else if ("description".equals(value.getName())) {
                summaryValue = value;
            }
            numValues++;
        }
        assertEquals(2, numValues);
        testValue(nameValue, IValue.Type.STRING, VALUE_NAME, LOCATION_NAME);
        testValue(summaryValue, IValue.Type.TEXT, VALUE_DESCRIPTION, LOCATION_DESCRIPTION);
        nameValue = result.getValue(VALUE_NAME);
        summaryValue = result.getValue(VALUE_DESCRIPTION);
        testValue(nameValue, IValue.Type.STRING, VALUE_NAME, LOCATION_NAME);
        testValue(summaryValue, IValue.Type.TEXT, VALUE_DESCRIPTION, LOCATION_DESCRIPTION);
    }

    /**
     * Test of setValues method, of class Location.
     */
    @Test
    public void testSetValues() {
        System.out.println("setValues");
        IValues values = new Values();
        values.addValue(ValueFactory.getInstance().createValue(VALUE_NAME, IValue.Type.STRING, "Foo"));
        values.addValue(ValueFactory.getInstance().createValue(VALUE_DESCRIPTION, IValue.Type.TEXT, "Bar"));
        location.setValues(values);
        assertEquals("Foo", location.getName());
        assertEquals("Bar", location.getDescription());
    }

    /**
     * Test of getModelType method, of class Location.
     */
    @Test
    public void testGetModelType() {
        assertEquals(INovelElement.Type.LOCATION, location.getModelType());
    }

    /**
     * Test of asLocation method, of class Location.
     */
    @Test
    public void testAsLocation() {
        INovelElement element = location.asLocation();
        assertTrue(element instanceof ILocation);
    }
}
