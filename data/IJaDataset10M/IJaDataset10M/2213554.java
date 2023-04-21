package com.plasticcode.stats;

public interface TimeMeasurementRecorder {

    void record(String label, TimeMeasurement measurement);
}
