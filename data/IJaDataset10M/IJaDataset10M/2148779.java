package net.sf.brightside.dentalwizard.metamodel.beans;

import static org.easymock.EasyMock.createStrictMock;
import static org.testng.AssertJUnit.assertEquals;
import static org.testng.AssertJUnit.assertFalse;
import static org.testng.AssertJUnit.assertNotNull;
import static org.testng.AssertJUnit.assertNull;
import static org.testng.AssertJUnit.assertTrue;
import java.util.Date;
import java.util.LinkedList;
import net.sf.brightside.dentalwizard.core.beans.BaseBeanTest;
import net.sf.brightside.dentalwizard.metamodel.Appointment;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

public class SchedueleListBeanTest extends BaseBeanTest {

    private SchedueleListBean schedueleListBeanUnderTest;

    @BeforeMethod
    public void setUp() throws Exception {
        super.setUp();
        schedueleListBeanUnderTest = createUnderTest();
        schedueleListBeanUnderTest.setAppointments(new LinkedList<Appointment>());
    }

    protected SchedueleListBean createUnderTest() {
        return new SchedueleListBean();
    }

    @Test
    public void testMonth() {
        Date month = new Date();
        assertNull(schedueleListBeanUnderTest.getMonth());
        schedueleListBeanUnderTest.setMonth(month);
        assertEquals(month, schedueleListBeanUnderTest.getMonth());
    }

    @Test
    public void testNameSetNull() {
        Date month = new Date();
        schedueleListBeanUnderTest.setMonth(month);
        assertEquals(month, schedueleListBeanUnderTest.getMonth());
        schedueleListBeanUnderTest.setMonth(null);
        assertNull(schedueleListBeanUnderTest.getMonth());
    }

    @Test
    public void testAppointmentsNotNull() {
        assertNotNull(schedueleListBeanUnderTest.getAppointments());
    }

    @Test
    public void testAppointmentsAssociation() {
        Appointment appointment = createStrictMock(Appointment.class);
        assertFalse(schedueleListBeanUnderTest.getAppointments().contains(appointment));
        schedueleListBeanUnderTest.getAppointments().add(appointment);
        assertTrue(schedueleListBeanUnderTest.getAppointments().contains(appointment));
    }
}
