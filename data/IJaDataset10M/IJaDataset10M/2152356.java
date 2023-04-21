package ru.cos.sim.meters.impl;

import ru.cos.sim.meters.framework.Measurer;
import ru.cos.sim.meters.impl.data.Speed;

/**
 * Returns instant data as it was set by method setSpeed()
 * @author zroslaw
 */
public class SimpleSpeedMeasurer implements Measurer<Speed> {

    private Speed speed = new Speed(0);

    @Override
    public Speed getInstantData() {
        return speed;
    }

    @Override
    public void measure(float dt) {
    }

    /**
	 * Set instant speed value
	 * @param speed
	 */
    public void setSpeed(float speed) {
        this.speed.setValue(speed);
    }
}
