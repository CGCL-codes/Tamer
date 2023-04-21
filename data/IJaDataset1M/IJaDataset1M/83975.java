package ru.cos.sim.meters.impl;

import ru.cos.sim.meters.framework.ModesInitData;
import ru.cos.sim.meters.impl.data.Speed;

/**
 * 
 * @author zroslaw
 */
public class InstantAverageSpeedMeter extends AbstractMeter<Speed> {

    public InstantAverageSpeedMeter(int id, ModesInitData modesInitData) {
        super(id, MeterType.InstantAverageSpeedMeter, modesInitData, null, new DumbAverageDataCollectorFactory<Speed>());
    }
}
