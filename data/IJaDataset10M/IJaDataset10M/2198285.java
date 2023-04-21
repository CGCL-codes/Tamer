package it.hotel.model.booking.manager;

import it.hotel.controller.booking.DTO.CriteriaDTO;
import it.hotel.controller.booking.DTO.SolutionHotelDTO;
import it.hotel.model.abstrakt.manager.IhDAO;
import it.hotel.model.abstrakt.manager.IhManager;
import it.hotel.model.booking.Booking;
import it.hotel.model.booking.CompletedBooking;
import it.hotel.model.booking.ConfirmedBooking;
import it.hotel.model.booking.UnconfirmedBooking;
import it.hotel.model.room.Room;
import it.hotel.model.structure.Structure;
import java.util.Collection;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Map;

public interface IBookingManager extends IhManager {

    public Collection<Room> getVacantRooms(GregorianCalendar beginDate, GregorianCalendar finishDate, int typologyId);

    public boolean isVacant(Room room, GregorianCalendar beginDate, GregorianCalendar finishDate);

    public boolean checkIn(int id);

    public ConfirmedBooking checkIn(Booking booking);

    public void setConfirmedBookingDAO(IhDAO dao);

    public Collection getAllBookings();

    public Collection getAllConfirmedBookings();

    public CompletedBooking checkOut(int id);

    public Collection getAllBookingAndConfirmedBookings();

    public Collection getAllCompletedBookings();

    public ConfirmedBooking getConfirmedBooking(int id);

    public CompletedBooking getCompletedBooking(int id);

    public Map getSolutionsFromCriteria(CriteriaDTO dto, GregorianCalendar begindate, GregorianCalendar finishdate);

    public SolutionHotelDTO getSolutionsForStructureFromCriteria(CriteriaDTO dto, Structure structure, GregorianCalendar begindate, GregorianCalendar finishdate);

    public boolean updateConfirmedBookingWithTodaysDateIfNecessary(ConfirmedBooking confirmed);

    public Map getRoomsByCombination(String combinationId, GregorianCalendar beginDate, GregorianCalendar finishDate, Structure structure);

    public boolean checkAndEdit(Booking booking);

    public Collection<Room> getVacantRoomsForStructure(Structure structure, GregorianCalendar beginDate, GregorianCalendar finishDate);

    public Collection getAllUnconfirmedBookings();

    public Booking enableBooking(UnconfirmedBooking unConfirmed);

    public boolean isRoomVacant(GregorianCalendar beginDate, GregorianCalendar finishDate, Room room);

    public void IdentifyCheckinableBookings(List<Booking> bookings);

    public Map<String, Room> searchFreeRooms(Structure structure, GregorianCalendar beginDate, GregorianCalendar finishDate);
}
