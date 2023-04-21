package Source;

import java.util.ArrayList;

/**
 *
 * @author Joshua King
 */
public class DeleteFlight {

    private String flightDepartureLoc;

    private String flightArrivalLoc;

    private String flightDepartureDate;

    private String flightArrivalDate;

    private String flightDepartureTime;

    private String flightArrivalTime;

    private String flightNoSeat;

    public void setFlightArrivalDate(String flightArrivalDate) {
        this.flightArrivalDate = flightArrivalDate;
    }

    public void setFlightArrivalLoc(String flightArrivalLoc) {
        this.flightArrivalLoc = flightArrivalLoc;
    }

    public void setFlightArrivalTime(String flightArrivalTime) {
        this.flightArrivalTime = flightArrivalTime;
    }

    public void setFlightDepartureDate(String flightDepartureDate) {
        this.flightDepartureDate = flightDepartureDate;
    }

    public void setFlightDepartureLoc(String flightDepartureLoc) {
        this.flightDepartureLoc = flightDepartureLoc;
    }

    public void setFlightDepartureTime(String flightDepartureTime) {
        this.flightDepartureTime = flightDepartureTime;
    }

    public void setFlightNoSeat(String flightNoSeat) {
        this.flightNoSeat = flightNoSeat;
    }

    public boolean commitData() {
        FlightServerInteraction logIn = new FlightServerInteraction();
        ArrayList<String> request = new ArrayList<String>();
        ArrayList<String> reply = new ArrayList<String>();
        request.add(0, "flightDelete");
        request.add(1, flightDepartureLoc);
        request.add(2, flightArrivalLoc);
        request.add(3, flightDepartureDate);
        request.add(4, flightArrivalDate);
        request.add(5, flightDepartureTime);
        request.add(6, flightArrivalTime);
        request.add(7, flightNoSeat);
        request.add(8, "100.00");
        reply = logIn.run(request);
        return true;
    }
}
