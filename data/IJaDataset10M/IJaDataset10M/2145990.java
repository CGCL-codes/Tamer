package net.sf.brightside.travelsystem.metamodel.beans;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;
import java.util.ArrayList;
import java.util.Date;
import net.sf.brightside.travelsystem.metamodel.Airline;
import net.sf.brightside.travelsystem.metamodel.Airport;
import net.sf.brightside.travelsystem.metamodel.Plain;
import net.sf.brightside.travelsystem.metamodel.Reservation;
import org.easymock.EasyMock;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

public class FlightBeanTest {

    private FlightBean flight;

    @BeforeMethod
    public void setUp() {
        flight = new FlightBean();
        flight.setReservations(new ArrayList<Reservation>());
    }

    @Test
    public void testDate() {
        Date d = new Date();
        flight.setDepartureTime(d);
        assertEquals(d, flight.getDepartureTime());
    }

    @Test
    public void testReservationAssociation() {
        Reservation rb = EasyMock.createStrictMock(Reservation.class);
        assertFalse(flight.getReservations().contains(rb));
        flight.getReservations().add(rb);
        assertTrue(flight.getReservations().contains(rb));
    }

    @Test
    public void testAirline() {
        Airline a = EasyMock.createStrictMock(Airline.class);
        flight.setAirline(a);
        assertEquals(a, flight.getAirline());
    }

    @Test
    public void testArrivalAirport() {
        Airport a = EasyMock.createStrictMock(Airport.class);
        flight.setArrivalAirport(a);
        assertEquals(a, flight.getArrivalAirport());
    }

    @Test
    public void testDepartureAirport() {
        Airport a = EasyMock.createStrictMock(Airport.class);
        flight.setDepartureAirport(a);
        assertEquals(a, flight.getDepartureAirport());
    }

    @Test
    public void testNumber() {
        Integer number = 34;
        flight.setNumber(number);
        assertEquals(number, flight.getNumber());
    }

    @Test
    public void testPlain() {
        Plain a = EasyMock.createStrictMock(Plain.class);
        flight.setPlain(a);
        assertEquals(a, flight.getPlain());
    }

    @Test
    public void testDepartureTime() {
        Date d = new Date();
        flight.setDepartureTime(d);
        assertEquals(d, flight.getDepartureTime());
    }

    @Test
    public void testArrivalTime() {
        Date d = new Date();
        flight.setArrivalTime(d);
        assertEquals(d, flight.getArrivalTime());
    }
}
