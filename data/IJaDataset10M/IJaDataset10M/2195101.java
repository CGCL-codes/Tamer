package com.google.gwt.inject.client.privatemanylevel;

import com.google.gwt.inject.client.privatemanylevel.PrivateManyLevelTest.Drive;
import com.google.gwt.inject.client.privatemanylevel.PrivateManyLevelTest.Engine;
import com.google.gwt.inject.client.privatemanylevel.PrivateManyLevelTest.Transmission;
import com.google.inject.Inject;
import javax.inject.Provider;

public class Car {

    private final Engine engine;

    private final Transmission transmission;

    private final Drive driveline;

    @Inject
    public Car(Engine engine, Provider<Transmission> transmission, Provider<Drive> driveline) {
        this.engine = engine;
        this.transmission = transmission.get();
        this.driveline = driveline.get();
    }

    public Engine getEngine() {
        return engine;
    }

    public Transmission getTransmission() {
        return transmission;
    }

    public Drive getDriveline() {
        return driveline;
    }
}
