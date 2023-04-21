package de.gruessing.gwtsports.shared.mappeddata.tcdata;

import com.google.gwt.user.client.rpc.IsSerializable;

public class LapDTO implements IsSerializable {

    protected double totalTimeSeconds;

    protected double distanceMeters;

    protected HeartRateInBeatsPerMinuteDTO averageHeartRateBpm;

    protected HeartRateInBeatsPerMinuteDTO maximumHeartRateBpm;

    protected IntensityDTO intensity;

    protected Short cadence;

    protected ExtensionsDTO extensions;

    public LapDTO() {
    }

    public double getTotalTimeSeconds() {
        return totalTimeSeconds;
    }

    public void setTotalTimeSeconds(double totalTimeSeconds) {
        this.totalTimeSeconds = totalTimeSeconds;
    }

    public double getDistanceMeters() {
        return distanceMeters;
    }

    public void setDistanceMeters(double distanceMeters) {
        this.distanceMeters = distanceMeters;
    }

    public HeartRateInBeatsPerMinuteDTO getAverageHeartRateBpm() {
        return averageHeartRateBpm;
    }

    public void setAverageHeartRateBpm(HeartRateInBeatsPerMinuteDTO averageHeartRateBpm) {
        this.averageHeartRateBpm = averageHeartRateBpm;
    }

    public HeartRateInBeatsPerMinuteDTO getMaximumHeartRateBpm() {
        return maximumHeartRateBpm;
    }

    public void setMaximumHeartRateBpm(HeartRateInBeatsPerMinuteDTO maximumHeartRateBpm) {
        this.maximumHeartRateBpm = maximumHeartRateBpm;
    }

    public IntensityDTO getIntensity() {
        return intensity;
    }

    public void setIntensity(IntensityDTO intensity) {
        this.intensity = intensity;
    }

    public Short getCadence() {
        return cadence;
    }

    public void setCadence(Short cadence) {
        this.cadence = cadence;
    }

    public ExtensionsDTO getExtensions() {
        return extensions;
    }

    public void setExtensions(ExtensionsDTO extensions) {
        this.extensions = extensions;
    }
}
