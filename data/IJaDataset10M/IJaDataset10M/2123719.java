package net.sf.laja.example.car.state.vehiclesize;

public class VehicleSizeStateTemplate {

    int lengthInCentimeters;

    public boolean isValid() {
        return lengthInCentimeters >= 0;
    }
}
