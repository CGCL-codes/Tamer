package com.amazon.webservices.awsecommerceservice.x20090201;

public class VehicleYear implements java.io.Serializable {

    private java.math.BigInteger year;

    public java.math.BigInteger getYear() {
        return this.year;
    }

    public void setYear(java.math.BigInteger year) {
        this.year = year;
    }

    private java.lang.String isValid;

    public java.lang.String getIsValid() {
        return this.isValid;
    }

    public void setIsValid(java.lang.String isValid) {
        this.isValid = isValid;
    }

    private com.amazon.webservices.awsecommerceservice.x20090201.VehicleMakes vehicleMakes;

    public com.amazon.webservices.awsecommerceservice.x20090201.VehicleMakes getVehicleMakes() {
        return this.vehicleMakes;
    }

    public void setVehicleMakes(com.amazon.webservices.awsecommerceservice.x20090201.VehicleMakes vehicleMakes) {
        this.vehicleMakes = vehicleMakes;
    }
}
