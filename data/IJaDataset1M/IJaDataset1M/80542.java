package de.gruessing.gwtsports.shared.mappeddata.tcdata;

import com.google.gwt.user.client.rpc.IsSerializable;

public class PositionDTO implements IsSerializable {

    protected double latitudeDegrees;

    protected double longitudeDegrees;

    public PositionDTO() {
    }

    public double getLatitudeDegrees() {
        return latitudeDegrees;
    }

    public void setLatitudeDegrees(double value) {
        this.latitudeDegrees = value;
    }

    public double getLongitudeDegrees() {
        return longitudeDegrees;
    }

    public void setLongitudeDegrees(double value) {
        this.longitudeDegrees = value;
    }
}
