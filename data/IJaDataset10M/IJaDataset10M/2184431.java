package org.flightreservationsclient.main.operations;

import java.rmi.RemoteException;
import java.util.Random;
import org.flightreservationsserverinterface.FlightReservationsAirlineInterface;
import org.flightreservationsserverinterface.exceptions.ErrorInServerException;
import org.flightreservationsserverinterface.exceptions.NotFoundException;

/**
 * @author Facundo Quiroga
 * Creation date: Feb 8, 2009 5:31:08 PM
 */
public class GetReservationAgencyByName extends Operation {

    protected int maxID = 100;

    protected int increase = 10;

    protected double decreaseFactor = 1.5;

    public GetReservationAgencyByName(FlightReservationsAirlineInterface flightReservationsAirlineInterface) {
        super();
        this.flightReservationsAirlineInterface = flightReservationsAirlineInterface;
    }

    protected FlightReservationsAirlineInterface flightReservationsAirlineInterface;

    @Override
    public void perform() throws ErrorInServerException, RemoteException {
        int randomID = new Random().nextInt(maxID);
        try {
            flightReservationsAirlineInterface.getReservationAgency(randomID);
            maxID = maxID + increase;
        } catch (NotFoundException e) {
            int newMaxID = (int) ((double) maxID / decreaseFactor);
            maxID = newMaxID;
        }
    }
}
