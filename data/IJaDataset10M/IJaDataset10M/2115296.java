package dk.nullesoft.AirlogExceptions;

public class FlightDataInvalidException extends Exception {

    public FlightDataInvalidException() {
        super("Flight data invalid");
    }

    public FlightDataInvalidException(String msg) {
        super(msg);
    }
}
