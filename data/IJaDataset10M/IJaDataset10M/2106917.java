package org.flightreservationsclient.main.operations;

import java.rmi.RemoteException;
import org.flightreservationsserverinterface.FlightReservationsInterface;
import org.flightreservationsserverinterface.exceptions.ErrorInServerException;

/**
 * @author Facundo Quiroga
 * Creation date: Feb 9, 2009 5:54:42 PM
 */
public class AddCustomerOperation extends Operation {

    public AddCustomerOperation(FlightReservationsInterface flightReservationsInterface) {
        super();
        this.flightReservationsInterface = flightReservationsInterface;
    }

    protected FlightReservationsInterface flightReservationsInterface;

    @Override
    public void perform() throws ErrorInServerException, RemoteException {
        int maxID = (GetCustomerByIDOperation.maxID + GetCustomerByNameOperation.maxID) / 2;
        flightReservationsInterface.addCustomer(maxID, "customer" + maxID + 1, 1);
    }
}
