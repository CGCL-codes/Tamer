package fleetAdmin;

public class PassAircraft {

    protected String make = "Boeing";

    protected String model = "747";

    protected String registrationNumber = "NOT REGISTERED";

    protected int engines = 4;

    protected int passengers = 350;

    public PassAircraft() {
    }

    public PassAircraft(String make, String model, String registrationNumber, int engines, int passengers) {
        this.make = make;
        this.model = model;
        this.registrationNumber = registrationNumber;
        this.engines = engines;
        this.passengers = passengers;
    }

    public void setPassengers(int passengers) {
        this.passengers = passengers;
    }

    public int getPassengers() {
        return passengers;
    }

    public String toString() {
        return ("PLANE <MAKE:" + make + "\tMODEL:" + model + "\tREG#:" + registrationNumber + "\t#ENGINES:" + engines + "\t#PASSENGERS:" + passengers);
    }
}
