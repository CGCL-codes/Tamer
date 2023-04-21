package drcl.inet.sensorsim;

import drcl.comp.*;
import drcl.data.DoubleObj;

/** This class implements a simple battery whose capacity is assumed to be constant.
*
* @author Ahmed Sobeih
* @version 1.0, 12/19/2003
*/
public class BatterySimple extends drcl.inet.sensorsim.BatteryBase {

    public static final double VOLTAGE = 1.0;

    double lastTimeOut;

    ACATimer batteryOutEventTimer;

    /** Initializes the simple battery model. */
    public void initializeSimple() {
        lastTimeOut = 0.0;
        batteryOutEventTimer = null;
    }

    public BatterySimple() {
        super();
        initializeSimple();
    }

    public BatterySimple(double energy) {
        super(energy);
        initializeSimple();
    }

    /** Calculates the new value of the energy.  */
    public void updateEnergy() {
        double now = getTime();
        int i;
        if (energy_ <= 0.0) return;
        double totalPower = 0.0;
        for (i = 0; i < MAX_COMPONENT; i++) totalPower += (currentRating[i] * VOLTAGE);
        energy_ -= (totalPower) * (now - lastTimeOut);
        if (energy_ <= 0.0) energy_ = 0.0;
        lastTimeOut = now;
    }

    /** Gets the energy. */
    public double energy() {
        updateEnergy();
        return energy_;
    }

    /** Gets the percentage of the energy that has been spent so far. */
    public double energyPercent() {
        updateEnergy();
        return (energy_ * 100.0 / totalEnergy);
    }

    /** Gets the energy that has been spent so far. */
    public double energySpent() {
        updateEnergy();
        return (totalEnergy - energy_);
    }

    /** Returns true if all of the energy has been consumed.  */
    public boolean isDead() {
        return (energy_ > 0.0) ? false : true;
    }

    /** Changes the current drained by a power consumer. */
    public int changeCurrent(double cur, int componentID) {
        int i = 0;
        double now = getTime();
        if ((componentID < 0) || (componentID >= MAX_COMPONENT)) {
            return -1;
        }
        updateEnergy();
        currentRating[componentID] = cur;
        double totalPower = 0.0;
        for (i = 0; i < MAX_COMPONENT; i++) totalPower += (currentRating[i] * VOLTAGE);
        if (totalPower != 0.0) {
            if (!isDead()) {
                double timeToZero = energy_ / totalPower;
                if (batteryOutEventTimer != null) cancelFork(batteryOutEventTimer);
                batteryOutEventTimer = fork(forkPort, "BATTERY_OUT", timeToZero);
            }
        } else if (batteryOutEventTimer != null) cancelFork(batteryOutEventTimer);
        return 1;
    }

    /** Sets the energy of the battery. */
    public int setEnergy(double newEnergyValue) {
        int i = 0;
        double now = getTime();
        double totalPower = 0.0;
        for (i = 0; i < MAX_COMPONENT; i++) totalPower += (currentRating[i] * VOLTAGE);
        newEnergyValue = (newEnergyValue < 0.0) ? 0.0 : newEnergyValue;
        newEnergyValue = (newEnergyValue > 100.0) ? 100.0 : newEnergyValue;
        energy_ = (newEnergyValue / 100.0) * totalEnergy;
        if (isDead()) {
            batteryOutPort.doSending("BATTERY_OUT");
            energy_ = 0.0;
            return 1;
        }
        if (totalPower != 0.0) {
            double timeToZero = energy_ / totalPower;
            if (batteryOutEventTimer != null) cancelFork(batteryOutEventTimer);
            batteryOutEventTimer = fork(forkPort, "BATTERY_OUT", timeToZero);
        } else if (batteryOutEventTimer != null) cancelFork(batteryOutEventTimer);
        return 1;
    }

    protected synchronized void process(Object data_, Port inPort_) {
        if (inPort_ == forkPort) {
            String msg = new String((String) data_);
            if (msg.equals("BATTERY_OUT")) {
                batteryOutPort.doSending("BATTERY_OUT");
                energy_ = 0.0;
            }
        } else if (inPort_ == batteryPort) {
            if (data_ instanceof BatteryContract.Message) {
                BatteryContract.Message struct_ = (BatteryContract.Message) data_;
                int type = struct_.getType();
                if (type == BatteryContract.GET_REMAINING_ENERGY) {
                    batteryPort.doSending(new DoubleObj(energy_));
                } else if (type == BatteryContract.SET_CONSUMER_CURRENT) {
                    int consumer_id = struct_.getConsumerID();
                    double current = struct_.getCurrent();
                    changeCurrent(current, consumer_id);
                }
            }
        }
    }
}
