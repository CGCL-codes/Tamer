package test.it.hotel.controller.booking;

import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;
import it.hotel.model.booking.Booking;
import it.hotel.model.customer.Customer;
import it.hotel.model.hotel.Hotel;
import it.hotel.model.room.Room;
import java.util.ArrayList;
import java.util.Collection;
import test.it.hotel.controller.HotelModelAndViewTests;

public class TestBookingCheckinController extends HotelModelAndViewTests {

    public void setUp() throws Exception {
        super.setUp();
    }

    public void testBookingSimpleFormCreate() throws Exception {
        Collection<Hotel> hotels = new ArrayList<Hotel>();
        Collection<Booking> lista = new ArrayList<Booking>();
        expect(bookingManager.get(1)).andReturn(booking);
        req.addParameter("idBooking", "1");
        req.setMethod("GET");
        req.setRequestURI("http://localhost:8080/hotel/Booking/editCheckin.htm");
        replay(bookingManager);
        mvc = bookingCheckinFormController.handleRequest(req, resp);
        verify(bookingManager);
        assertTrue(mvc.getModel().containsKey("hotels"));
        assertTrue(mvc.getModel().containsKey("booking"));
    }

    public void testRoomSimpleFormCreate() throws Exception {
        Collection<Room> rooms = new ArrayList<Room>();
        Collection<Hotel> hotels = new ArrayList<Hotel>();
        Collection<Booking> books = new ArrayList<Booking>();
        expect(bookingManager.get(1)).andReturn(booking);
        req.setMethod("POST");
        customer = new Customer();
        customer.setId(1);
        req.setRequestURI("http://localhost:8080/hotel/Booking/checkin.htm");
        req.addParameter("begindate", "17/3/2008");
        req.addParameter("finishdate", "21/12/2008");
        req.addParameter("roomId", "1");
        req.addParameter("hotelId", "1");
        req.addParameter("accomodation", "PN");
        req.addParameter("customerId", "2");
        req.addParameter("idBooking", "1");
        req.addParameter("customer", "customer");
        replay(bookingManager);
        mvc = bookingCheckinFormController.handleRequest(req, resp);
        verify(bookingManager);
    }
}
