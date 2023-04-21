package org.dbe.demos.hotelreservation;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.rmi.RemoteException;
import java.util.Calendar;
import javax.xml.rpc.holders.BigDecimalHolder;
import javax.xml.rpc.holders.BooleanHolder;
import javax.xml.rpc.holders.StringHolder;
import org.dbe.demos.hotelreservation.serviceimpl.HotelReservationImpl;
import org.dbe.servent.Adapter;
import org.dbe.servent.ServiceContext;

/**
 * 
 * @author bob 
 */
public class HotelAdapter implements HotelReservationService, Adapter {

    HotelReservationImpl hotel = new HotelReservationImpl();

    public void cancelReservation(String reservationNumber, BooleanHolder successful) throws RemoteException {
        successful.value = hotel.cancelReservation(reservationNumber);
    }

    public void makeReservation(Calendar checkinDate, Calendar checkOutDate, String roomType, BigInteger numOfAdults, BigInteger numOfChild, String customerName, String customerEmail, BooleanHolder successFull, StringHolder reservationNumber, StringHolder failureReason) throws java.rmi.RemoteException {
        failureReason.value = new java.lang.String();
        reservationNumber.value = "0";
        successFull.value = true;
        try {
            reservationNumber.value = hotel.makeReservation(Integer.parseInt(roomType));
        } catch (Exception e) {
            successFull.value = false;
            failureReason.value = e.getMessage();
        }
    }

    public void advancedAvailabilityChecking(Calendar checkinDate, Calendar checkOutDate, String roomType, BigInteger numOfAdults, BigInteger numOfChild, String customerName, String customerEmail, BooleanHolder available, StringHolder unavailabilityReason, StringHolder newRoomType, BigDecimalHolder dailyPice) throws RemoteException {
        dailyPice.value = new java.math.BigDecimal(0);
        newRoomType.value = roomType;
        unavailabilityReason.value = new java.lang.String();
        available.value = true;
        int room = Integer.parseInt(roomType);
        if (room == 0) {
            room = hotel.findAvailableRoom();
            newRoomType.value = Integer.toString(room);
            if (room == 0) {
                available.value = false;
                unavailabilityReason.value = "There are NO MORE free rooms. We are sorry";
                return;
            }
        }
        if (!hotel.isFreeRoom(room)) {
            available.value = false;
            unavailabilityReason.value = "There are free rooms (" + room + ") we would try with " + hotel.findAvailableRoom() + " type rooms";
            newRoomType.value = Integer.toString(hotel.findAvailableRoom());
            return;
        }
        dailyPice.value = new BigDecimal(hotel.calculate(room, numOfAdults.intValue(), numOfChild.intValue()));
    }

    public void simpleAvailabilityChecking(Calendar checkInDate, Calendar checkOutDate, String roomType, BigInteger numOfAdults, BigInteger numOfChild, String customerName, String customerEmail, BooleanHolder available) throws RemoteException {
        advancedAvailabilityChecking(checkInDate, checkOutDate, roomType, numOfAdults, numOfChild, customerName, customerEmail, available, new StringHolder(), new StringHolder(), new BigDecimalHolder());
    }

    public void init(ServiceContext arg0) {
    }

    public void destroy() {
    }
}
