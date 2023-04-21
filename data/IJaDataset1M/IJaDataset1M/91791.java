package org.chargecar.prize.policies;

import org.chargecar.prize.battery.BatteryModel;
import org.chargecar.prize.util.PointFeatures;
import org.chargecar.prize.util.PowerFlowException;
import org.chargecar.prize.util.PowerFlows;
import org.chargecar.prize.util.TripFeatures;

/**
 * DO NOT EDIT
 * 
 * An example policy that uses just the current speed of the vehicle to
 * determine how much space to leave in the capacitor. If it is under that
 * space, it trickles power into the capacitor if possible. Illustrates the
 * capacitor current limitations, the difference between +/- current, and using
 * the models to keep track of state.
 * 
 * @author Alex Styler
 */
public class SpeedPolicy implements Policy {

    private BatteryModel modelCap;

    private BatteryModel modelBatt;

    private String name = "Speed Trickle Policy";

    private String shortName = "speed";

    @Override
    public String getShortName() {
        return this.shortName;
    }

    public void beginTrip(TripFeatures tripFeatures, BatteryModel batteryClone, BatteryModel capacitorClone) {
        modelCap = capacitorClone;
        modelBatt = batteryClone;
    }

    public PowerFlows calculatePowerFlows(PointFeatures pf) {
        double wattsDemanded = pf.getPowerDemand();
        int periodMS = pf.getPeriodMS();
        double speed = pf.getSpeed();
        double targetWattHours = modelCap.getMaxWattHours() - 0 * speed;
        double minCapPower = modelCap.getMinPowerDrawable(periodMS);
        double maxCapPower = modelCap.getMaxPowerDrawable(periodMS);
        double defaultTrickleRate = 7000.0;
        double batteryToCapWatts = 0;
        double capToMotorWatts = wattsDemanded > maxCapPower ? maxCapPower : wattsDemanded;
        capToMotorWatts = capToMotorWatts < minCapPower ? minCapPower : capToMotorWatts;
        double batteryToMotorWatts = wattsDemanded - capToMotorWatts;
        if (modelCap.getWattHours() < targetWattHours) {
            batteryToCapWatts = defaultTrickleRate - batteryToMotorWatts;
        }
        batteryToCapWatts = batteryToCapWatts < 0 ? 0 : batteryToCapWatts;
        if (capToMotorWatts - batteryToCapWatts > maxCapPower) {
            batteryToCapWatts = capToMotorWatts - maxCapPower;
        } else if (capToMotorWatts - batteryToCapWatts < minCapPower) {
            batteryToCapWatts = capToMotorWatts - minCapPower;
        }
        try {
            modelCap.drawPower(capToMotorWatts - batteryToCapWatts, pf);
            modelBatt.drawPower(batteryToMotorWatts + batteryToCapWatts, pf);
        } catch (PowerFlowException e) {
        }
        return new PowerFlows(batteryToMotorWatts, capToMotorWatts, batteryToCapWatts);
    }

    public void endTrip() {
        modelCap = null;
        modelBatt = null;
    }

    public void loadState() {
    }

    public String getName() {
        return name;
    }
}
