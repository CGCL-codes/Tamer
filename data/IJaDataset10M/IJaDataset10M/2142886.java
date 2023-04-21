package org.dbe.demos.flightservice.diff;

import java.rmi.RemoteException;
import java.sql.SQLException;
import java.util.Date;
import java.util.Random;
import javax.xml.rpc.holders.BooleanHolder;
import javax.xml.rpc.holders.StringHolder;
import org.dbe.demos.bookingservice.BookingFault;
import org.dbe.servent.Adapter;
import org.dbe.servent.ServiceContext;

public class FlightServiceAdapter implements FlightService, Adapter {

    private static int FAILURE_RATE = 5;

    private Random rnd;

    private static final String SMID = "FLIGHT_DIFF-" + FAILURE_RATE;

    private ServiceContext context;

    public void cancelFlight(String bookingReference, BooleanHolder successful) throws RemoteException {
        context.getLogger().debug(SMID + ": cancelFlight()");
        successful.value = true;
    }

    public void bookFlight(String origin, String target, BookingInformation bookingData, StringHolder bookingReference, StringHolder flightNumber, BooleanHolder successful) throws java.rmi.RemoteException, BookingFault {
        context.getLogger().debug(SMID + ": bookFlight()");
        rnd = new Random(new Date().getTime());
        int randomValue = rnd.nextInt(100);
        if (randomValue < FAILURE_RATE) {
            try {
            } catch (Exception e) {
                context.getLogger().warn("Problem with database: " + e);
            }
            String failmsg = "Throwing " + FAILURE_RATE + "% exception in bookFlight()";
            context.getLogger().error(failmsg);
            throw new java.rmi.RemoteException(failmsg);
        }
        successful.value = true;
        String refid = Long.toString(Math.abs(rnd.nextInt()), 24).toUpperCase();
        String flightid = Long.toString(Math.abs(rnd.nextInt()), 24).toUpperCase();
        bookingReference.value = refid;
        flightNumber.value = flightid;
        try {
        } catch (Exception e) {
            context.getLogger().warn("Problem with database: " + e);
        }
        context.getLogger().debug(SMID + ": bookFlight() : " + successful.value);
    }

    ;

    public void init(ServiceContext context) {
        try {
            this.context = context;
        } catch (Exception e) {
            context.getLogger().error(e);
        }
    }

    public void destroy() {
        try {
            context = null;
        } catch (Exception e) {
        }
    }
}
