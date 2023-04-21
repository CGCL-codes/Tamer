package net.sf.brightside.gymcalendar.metamodel.beans;

import java.util.ArrayList;
import net.sf.brightside.gymcalendar.metamodel.Measurement;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import static org.testng.AssertJUnit.assertNotNull;
import static org.testng.AssertJUnit.assertEquals;
import static org.testng.AssertJUnit.assertNull;
import static org.testng.AssertJUnit.assertFalse;
import static org.testng.AssertJUnit.assertTrue;
import org.easymock.EasyMock;

public class BodyPartBeanTest {

    private BodyPartBean bodyPartBeanUnderTest;

    @BeforeMethod
    public void setUp() {
        bodyPartBeanUnderTest = new BodyPartBean();
        bodyPartBeanUnderTest.setMeasurements(new ArrayList<Measurement>());
    }

    @Test
    public void testName() {
        String name = "Pera";
        assertNull(bodyPartBeanUnderTest.getName());
        bodyPartBeanUnderTest.setName(name);
        assertEquals(name, bodyPartBeanUnderTest.getName());
    }

    @Test
    public void testNameSetNull() {
        String name = "Pera";
        bodyPartBeanUnderTest.setName(name);
        assertEquals(name, bodyPartBeanUnderTest.getName());
        bodyPartBeanUnderTest.setName(null);
        assertNull(bodyPartBeanUnderTest.getName());
    }

    @Test
    public void testMeasurementsNotNull() {
        assertNotNull(bodyPartBeanUnderTest.getMeasurements());
    }

    @Test
    public void testMeasurementsAsociations() {
        Measurement var = EasyMock.createStrictMock(Measurement.class);
        assertFalse(bodyPartBeanUnderTest.getMeasurements().contains(var));
        bodyPartBeanUnderTest.getMeasurements().add(var);
        assertTrue(bodyPartBeanUnderTest.getMeasurements().contains(var));
    }
}
