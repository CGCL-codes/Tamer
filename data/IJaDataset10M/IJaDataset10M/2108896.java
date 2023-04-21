package com.acv.connector.ocean.command.dummy.impl;

import com.vacv.ocean.comm.OceanCommunicationException;
import com.vacv.ocean.data.Reservation;
import com.vacv.ocean.protocol.OceanResponse;
import com.vacv.ocean.protocol.request.BookingRQ;
import com.vacv.ocean.protocol.response.BookingRS;

/**
 * 
 */
public class DummyGenericBookResponseGenerator extends DummyGenericPriceResponseGenerator {

    public DummyGenericBookResponseGenerator(BookingRQ bookingRQ) {
        super(bookingRQ);
    }

    public OceanResponse generate() throws OceanCommunicationException {
        BookingRS response = (BookingRS) super.generate();
        Reservation reservation = new Reservation();
        reservation.setID(1234567);
        reservation.setLatestVersion(true);
        response.setReservation(reservation);
        return response;
    }
}
