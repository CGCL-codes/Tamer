package beans;

import beans.AirportSchedule;
import beans.Airline;

public class OperateSchedule {

    private AirportSchedule portSche = null;

    private Airline airline = null;

    public OperateSchedule() {
    }

    public OperateSchedule(AirportSchedule portSche, Airline airline) {
        this.portSche = portSche;
        this.airline = airline;
    }

    public AirportSchedule getPortSche() {
        return portSche;
    }

    public Airline getAirline() {
        return airline;
    }
}
