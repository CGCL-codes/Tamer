package org.dbe.demos.bookingservice_13;

import java.math.BigInteger;
import javax.xml.rpc.holders.BooleanHolder;
import javax.xml.rpc.holders.StringHolder;
import org.dbe.servent.Adapter;
import org.dbe.servent.ServiceContext;

public class BookingServiceAdapter implements BookingService, Adapter {

    public void makeMultiBooking(String origin, String target, String startDate, String endDate, String hotelRoomType, BigInteger hotelNumOfAdults, BigInteger hotelNumOfChild, String referencePerson, String receiptType, String contact, String destinationAddress, String emailSubject, BooleanHolder successful, StringHolder hotelReservationNumber, StringHolder flightReservationNumber, StringHolder flightNumber, StringHolder failureReason) throws java.rmi.RemoteException {
    }

    ;

    public void init(ServiceContext context) {
    }

    public void destroy() {
    }
}
