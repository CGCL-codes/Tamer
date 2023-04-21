package com.trohko.jfsim.aircraft.sys.electrical.impl;

import com.trohko.jfsim.core.data.DataListener;
import com.trohko.jfsim.core.data.ElectricalData;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class ThermalCircuitBreakerTest {

    private ThermalCircuitBreaker thermalCircuitBreaker;

    private DataListener<ElectricalData> dataListener;

    private final Object mutex = new Object();

    private int changeCounter;

    @Before
    public void setUp() throws Exception {
        changeCounter = 0;
        dataListener = new DataListener<ElectricalData>() {

            @Override
            public void dataChanged(Object provider, ElectricalData data) {
                synchronized (mutex) {
                    ++changeCounter;
                }
            }
        };
        thermalCircuitBreaker = new ThermalCircuitBreaker(15, dataListener);
    }

    @Test
    public void testUpdateCurrent() throws Exception {
        thermalCircuitBreaker.updateCurrent(14);
        Thread.sleep(3000);
        Assert.assertEquals(0, changeCounter);
        thermalCircuitBreaker.updateCurrent(22);
        Thread.sleep(3000);
        Assert.assertEquals(1, changeCounter);
        thermalCircuitBreaker.reset();
        Thread.sleep(11000);
        thermalCircuitBreaker.updateCurrent(17);
        Thread.sleep(3000);
        Assert.assertEquals(3, changeCounter);
        thermalCircuitBreaker.reset();
        thermalCircuitBreaker.updateCurrent(30);
        Thread.sleep(3000);
        Assert.assertEquals(5, changeCounter);
        thermalCircuitBreaker.reset();
        thermalCircuitBreaker.updateCurrent(15);
        Thread.sleep(3000);
        Assert.assertEquals(6, changeCounter);
        thermalCircuitBreaker.reset();
    }

    @Test
    public void testTrip() throws Exception {
        changeCounter = 0;
        thermalCircuitBreaker.trip();
        thermalCircuitBreaker.reset();
        thermalCircuitBreaker.trip();
        thermalCircuitBreaker.reset();
        thermalCircuitBreaker.trip();
        thermalCircuitBreaker.reset();
        thermalCircuitBreaker.trip();
        thermalCircuitBreaker.reset();
        Assert.assertEquals(8, changeCounter);
    }
}
