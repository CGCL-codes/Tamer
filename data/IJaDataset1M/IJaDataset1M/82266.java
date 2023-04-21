package net.sf.adastra.model.impl;

import net.sf.adastra.model.Thruster;
import net.sf.adastra.model.Vehicle;
import org.jmock.Mock;
import org.jmock.MockObjectTestCase;
import com.jme.math.Vector3f;

public class ThrusterImplTest extends MockObjectTestCase {

    private Thruster t;

    @Override
    public void setUp() throws Exception {
        super.setUp();
        t = new ThrusterImpl();
    }

    public void testThrottleRangeIsLimitedToBetweenZeroToOne() {
        t.setThrottle(-1f);
        assertThat(t.getThrottle(), eq(0f));
        t.setThrottle(1.5f);
        assertThat(t.getThrottle(), eq(1f));
    }

    public void testMaximumThrusterIsLimitedToPositiveValues() {
        t.setMaxThrust(-1f);
        assertThat(t.getMaxThrust(), eq(0f));
    }

    public void testThrusterAddsForce() {
        Mock vehicleMock = mock(Vehicle.class);
        t.setVehicle((Vehicle) vehicleMock.proxy());
        t.setMaxThrust(0.5f);
        t.setThrottle(0.5f);
        t.setDirection(new Vector3f(1, 2, 3));
        Vector3f position = new Vector3f(7, 11, 13);
        t.setPosition(position);
        vehicleMock.expects(once()).method("addForce").with(eq(new Vector3f(0.25f, 0.5f, 0.75f)), eq(position));
        t.beforeStep(1f);
    }
}
