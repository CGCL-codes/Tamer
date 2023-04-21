package playground.mrieser.core.mobsim.impl;

import org.junit.Assert;
import org.junit.Test;
import org.matsim.api.core.v01.Id;
import org.matsim.testcases.MatsimTestUtils;
import playground.mrieser.core.mobsim.api.DriverAgent;
import playground.mrieser.core.mobsim.impl.DefaultSimVehicle;
import playground.mrieser.core.mobsim.network.api.MobSimLink;

/**
 * @author mrieser
 */
public class DefaultSimVehicleTest {

    @Test
    public void testSetGetDriver() {
        DefaultSimVehicle vehicle = new DefaultSimVehicle(null);
        FakeDriverAgent driver = new FakeDriverAgent();
        Assert.assertNull(vehicle.getDriver());
        vehicle.setDriver(driver);
        Assert.assertEquals(driver, vehicle.getDriver());
        vehicle.setDriver(null);
        Assert.assertNull(vehicle.getDriver());
    }

    @Test
    public void testGetSizeInEquivalents() {
        DefaultSimVehicle vehicle = new DefaultSimVehicle(null);
        Assert.assertEquals(1.0, vehicle.getSizeInEquivalents(), MatsimTestUtils.EPSILON);
        vehicle = new DefaultSimVehicle(null, 1.2);
        Assert.assertEquals(1.2, vehicle.getSizeInEquivalents(), MatsimTestUtils.EPSILON);
        vehicle = new DefaultSimVehicle(null, 6.0);
        Assert.assertEquals(6.0, vehicle.getSizeInEquivalents(), MatsimTestUtils.EPSILON);
    }

    static class FakeDriverAgent implements DriverAgent {

        @Override
        public Id getNextLinkId() {
            return null;
        }

        @Override
        public void notifyMoveToNextLink() {
        }

        @Override
        public double getNextActionOnCurrentLink() {
            return -1.0;
        }

        @Override
        public void handleNextAction(final MobSimLink link) {
        }
    }
}
