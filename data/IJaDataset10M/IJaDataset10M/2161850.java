package net.virtualinfinity.atrobots.hardware.throttle;

import net.virtualinfinity.atrobots.arena.Speed;
import net.virtualinfinity.atrobots.computer.ShutdownListener;
import net.virtualinfinity.atrobots.hardware.HasOverburner;
import net.virtualinfinity.atrobots.hardware.heatsinks.HeatSinks;
import net.virtualinfinity.atrobots.measures.Duration;
import net.virtualinfinity.atrobots.measures.Temperature;
import net.virtualinfinity.atrobots.ports.PortHandler;

/**
 * @author Daniel Pitts
 */
public class Throttle implements ShutdownListener {

    int desiredPower;

    int power;

    Speed speed;

    private static final int MAX_ACCELERATION = 4;

    private static final double STANDARD_MAX_VELOCITY = 4.0;

    private double powerRatio = STANDARD_MAX_VELOCITY / 100.0;

    private HeatSinks heatSinks;

    private HasOverburner overburner;

    public Throttle(double powerRatio) {
        this.powerRatio = powerRatio * STANDARD_MAX_VELOCITY / 100.0;
    }

    public PortHandler getSpedometer() {
        return new PortHandler() {

            public short read() {
                return (short) getPower();
            }
        };
    }

    public PortHandler getActuator() {
        return new PortHandler() {

            public void write(short value) {
                setDesiredPower(Math.max(-75, Math.min(100, value)));
            }
        };
    }

    public int getDesiredPower() {
        return desiredPower;
    }

    public void setDesiredPower(int desiredPower) {
        this.desiredPower = desiredPower;
    }

    public int getPower() {
        return power;
    }

    public void setPower(int power) {
        this.power = power;
        updateSpeed();
    }

    private void updateSpeed() {
        speed.setDistanceOverTime(power * getPowerRatio(), Duration.ONE_CYCLE);
    }

    private double getPowerRatio() {
        return overburner.isOverburn() ? this.powerRatio * 1.3 : this.powerRatio;
    }

    public void setSpeed(Speed speed) {
        this.speed = speed;
    }

    public void update(Duration duration) {
        while (duration.getCycles() > 0) {
            if (Math.abs(power - desiredPower) < MAX_ACCELERATION) {
                power = desiredPower;
            } else {
                power += desiredPower > power ? MAX_ACCELERATION : -MAX_ACCELERATION;
            }
            updateSpeed();
            if (Math.abs(power) > 25) {
                heatSinks.cool(Temperature.fromLogScale(.125));
            }
            duration = duration.minus(Duration.ONE_CYCLE);
        }
    }

    public void setHeatSinks(HeatSinks heatSinks) {
        this.heatSinks = heatSinks;
    }

    public void setOverburner(HasOverburner overburner) {
        this.overburner = overburner;
    }

    public void shutDown() {
        setDesiredPower(0);
    }
}
