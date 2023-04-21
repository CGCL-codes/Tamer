package it.hotel.model.booking.manager;

import it.hotel.controller.booking.DTO.CriteriaDTO;
import it.hotel.controller.booking.DTO.SolutionBookingDTO;
import it.hotel.controller.booking.DTO.SolutionHotelDTO;
import it.hotel.model.CalendarUtils;
import it.hotel.model.abstrakt.manager.AbstractManager;
import it.hotel.model.abstrakt.manager.IhDAO;
import it.hotel.model.booking.Booking;
import it.hotel.model.booking.BookingHelper;
import it.hotel.model.booking.CompletedBooking;
import it.hotel.model.booking.ConfirmedBooking;
import it.hotel.model.booking.UnconfirmedBooking;
import it.hotel.model.customer.Customer;
import it.hotel.model.customer.manager.ICustomerManager;
import it.hotel.model.hotel.Hotel;
import it.hotel.model.room.Room;
import it.hotel.model.room.manager.IRoomManager;
import it.hotel.model.structure.Structure;
import it.hotel.model.structure.manager.IStructureManager;
import it.hotel.model.typology.Typology;
import it.hotel.model.typology.manager.ITypologyManager;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

/**
 * Business layer for the management of bookings for the frontend backend.
 */
public class BookingManager extends AbstractManager implements IBookingManager {

    private IRoomManager roomManager;

    private IStructureManager structureManager;

    private ITypologyManager typologyManager;

    private IhDAO confirmedBookingDAO;

    private IhDAO completedBookingDAO;

    private IhDAO unconfirmedBookingDAO;

    private BookingHelper helper;

    private ICustomerManager customerManager;

    /**
	 * Get a list of all the vacant rooms according to the passed in criteria.
	 * 
	 * @param deginDate
	 * @param finishDate
	 * @param typologyId - id of typology that you want to search for 
	 * @return a collection which contains the available rooms or an empty list
	 */
    @Transactional(readOnly = true, propagation = Propagation.REQUIRED)
    public Collection<Room> getVacantRooms(GregorianCalendar beginDate, GregorianCalendar finishDate, int typologyId) {
        List<Room> freeRooms = new ArrayList<Room>();
        Map toSearch = new HashMap();
        Map typologies = new HashMap<String, Typology>();
        Room room = new Room();
        Typology typology = (Typology) typologyManager.get(typologyId);
        room.setTypology(typology);
        typologies.put("typology", typology);
        Collection<Room> rooms = roomManager.searchAll(typologies);
        Collection<Room> roomsOccupied = ((IBookingDAO) super.getDAO()).searchRoomNotAvailable(beginDate, finishDate, toSearch);
        if (roomsOccupied.size() == 0) {
            return rooms;
        }
        for (Room currentRoom : rooms) {
            if (isAvailable(currentRoom, roomsOccupied)) {
                freeRooms.add(currentRoom);
            }
        }
        return freeRooms;
    }

    /**
	 * Determines whether a room is vacant for the given dates
	 * 
	 * @param beginDate
	 * @param finishDate
	 * @param room room you want to verify
	 * @return true if the specified room is available, false otherwise
	 */
    @Transactional(readOnly = true, propagation = Propagation.REQUIRED)
    public boolean isRoomVacant(GregorianCalendar beginDate, GregorianCalendar finishDate, Room room) {
        boolean isVacant = true;
        ArrayList<Booking> booking = (ArrayList<Booking>) getAllBookings();
        ArrayList<ConfirmedBooking> confirmedbooking = (ArrayList<ConfirmedBooking>) getAllConfirmedBookings();
        booking.addAll(confirmedbooking);
        GregorianCalendar bookingStartDate;
        GregorianCalendar bookingEndDate;
        int bookingRoomId = 0;
        boolean isDateBetween = false;
        for (int j = 0; j < booking.size(); j++) {
            bookingRoomId = booking.get(j).getRoom().getId();
            bookingStartDate = booking.get(j).getBeginDate();
            bookingEndDate = booking.get(j).getFinishDate();
            isDateBetween = CalendarUtils.isDateBetween(bookingStartDate, bookingEndDate, beginDate, finishDate);
            if (bookingRoomId == room.getId() && !isDateBetween) {
                return false;
            }
        }
        return isVacant;
    }

    public Map<String, Room> searchFreeRooms(Structure structure, GregorianCalendar beginDate, GregorianCalendar finishDate) {
        Map roomsMap = new HashMap();
        roomsMap = ((IBookingDAO) super.getDAO()).searchFreeRoomsForStructure(structure, beginDate, finishDate);
        return roomsMap;
    }

    /**
	 * Is the given room vacant for the given dates
	 * 
	 * @param room
	 * @param beginDate
	 * @param finishDate
	 * @return true if the room is available on the specified date range, false otherwise   
	 */
    @Transactional(readOnly = false, propagation = Propagation.REQUIRED)
    public boolean isVacant(Room room, GregorianCalendar beginDate, GregorianCalendar finishDate) {
        Map toSearch = new HashMap();
        Collection<Room> rooms = ((IBookingDAO) super.getDAO()).searchRoomNotAvailable(beginDate, finishDate, toSearch);
        if (isAvailable(room, rooms)) {
            return true;
        }
        return false;
    }

    /**
	 * Checks in a booking with the given id
	 * @param bookingId
	 * @return true for a completed check-in with success
	 */
    @Transactional(readOnly = false, propagation = Propagation.REQUIRED)
    public boolean checkIn(int bookingId) {
        Booking booking = (Booking) get(bookingId);
        this.checkIn(booking);
        return true;
    }

    /**
	 * Takes a booking and checks in the that booking to create a confirmedbooking.
	 * You can only checkin in someone if the start date is today.
	 * 
	 * @param booking booking you want to checkin
	 * @return confirmedBooking the new confirmed booking if successful or null otherwise 
	 */
    @Transactional(readOnly = false, propagation = Propagation.REQUIRED)
    public ConfirmedBooking checkIn(Booking booking) {
        GregorianCalendar today = (GregorianCalendar) GregorianCalendar.getInstance();
        GregorianCalendar beginDate = booking.getBeginDate();
        if ((beginDate.get(Calendar.YEAR) != today.get(Calendar.YEAR)) || (beginDate.get(Calendar.MONTH) != today.get(Calendar.MONTH)) || (beginDate.get(Calendar.DAY_OF_MONTH) != today.get(Calendar.DAY_OF_MONTH))) {
            return null;
        }
        ConfirmedBooking confirmed;
        confirmed = new ConfirmedBooking(booking);
        confirmedBookingDAO.save(confirmed);
        getDAO().remove(booking);
        Customer customer = booking.getCustomer();
        customerManager.add(customer);
        return confirmed;
    }

    /**
	 * Takes an unconfirmedbooking and creates a booking into the system
	 * @param unconfirmedBooking
	 * @return the booking which has been enabled
	 */
    @Transactional(readOnly = false, propagation = Propagation.REQUIRED)
    public Booking enableBooking(UnconfirmedBooking unconfirmedBooking) {
        Booking booking;
        booking = new Booking(unconfirmedBooking);
        getDAO().save(booking);
        unconfirmedBookingDAO.remove(unconfirmedBooking);
        return booking;
    }

    @Transactional(readOnly = false, propagation = Propagation.REQUIRED)
    public CompletedBooking checkOut(int confirmedBookingId) {
        ConfirmedBooking confirmed = (ConfirmedBooking) get(confirmedBookingId);
        updateConfirmedBookingWithTodaysDateIfNecessary(confirmed);
        CompletedBooking completed = new CompletedBooking(confirmed);
        completedBookingDAO.save(completed);
        getDAO().remove(confirmed);
        return completed;
    }

    /**
     * Updates a confirmedbookings finish date to the current day.
     *  
     * @param confirmedBooking
     * @return true if the specified confirmed booking doesn't end on the current day
     */
    @Transactional(readOnly = false, propagation = Propagation.REQUIRED)
    public boolean updateConfirmedBookingWithTodaysDateIfNecessary(ConfirmedBooking confirmedBooking) {
        if (CalendarUtils.isDateAfterToday(confirmedBooking.getBeginDate())) {
            return false;
        }
        if (!CalendarUtils.IsToday(confirmedBooking.getFinishDate())) {
            confirmedBooking.setFinishDate(CalendarUtils.GetToday());
            confirmedBooking.CalculateTotal();
            return true;
        }
        return true;
    }

    /**
	 * @return a collection which contains all bookings into the database
	 *         or an empty list
	 */
    @Transactional(readOnly = true, propagation = Propagation.REQUIRED)
    public Collection getAllBookings() {
        List<Booking> bookings = (List<Booking>) (((IBookingDAO) getDAO()).getAllBooking());
        return bookings;
    }

    /**
	 * Gets a list of all booking and confirmedBookings
	 * 
	 * @return a collection which contains all bookings and confirmed bookings
	 *         or an empty list 
	 */
    @Transactional(readOnly = true, propagation = Propagation.REQUIRED)
    public Collection getAllBookingAndConfirmedBookings() {
        List<Booking> bookings = (List<Booking>) this.getAllBookings();
        List<ConfirmedBooking> confirmedBookings = (List<ConfirmedBooking>) this.getAllConfirmedBookings();
        bookings.addAll(confirmedBookings);
        return bookings;
    }

    /**
	 * 
	 * @return a collection which contains all confirmed bookings
	 *         or an empty list
	 */
    @Transactional(readOnly = true, propagation = Propagation.REQUIRED)
    public Collection getAllConfirmedBookings() {
        return ((IBookingDAO) getDAO()).getAllConfirmedBooking();
    }

    /**
	 * @return a collection which contains all completed bookings
	 *         or an empty list
	 */
    @Transactional(readOnly = true, propagation = Propagation.REQUIRED)
    public Collection getAllCompletedBookings() {
        return ((IBookingDAO) getDAO()).getAllCompletedBooking();
    }

    /**
	 * @return a collection which contains all unconfirmed bookings
	 *         or an empty list
	 */
    @Transactional(readOnly = true, propagation = Propagation.REQUIRED)
    public Collection getAllUnconfirmedBookings() {
        return ((IBookingDAO) getDAO()).getAllUnconfirmedBooking();
    }

    /**
	 * @param confirmedBookingId
	 * @return the confirmed booking or null if not found
	 */
    @Transactional(readOnly = true, propagation = Propagation.REQUIRED)
    public ConfirmedBooking getConfirmedBooking(int confirmedBookingId) {
        ConfirmedBooking confirmedBooking = ((IBookingDAO) getDAO()).getConfirmedBooking(confirmedBookingId);
        confirmedBooking.CalculateTotal();
        return confirmedBooking;
    }

    /**
     * 
     * @param completedBookingId
     * @return the completed booking or null otherwise
     */
    @Transactional(readOnly = true, propagation = Propagation.REQUIRED)
    public CompletedBooking getCompletedBooking(int completedBookingId) {
        CompletedBooking completedBooking = ((IBookingDAO) getDAO()).getCompletedBooking(completedBookingId);
        return completedBooking;
    }

    @Transactional(readOnly = true, propagation = Propagation.REQUIRED)
    public Collection searchAll(Map toSearch) {
        List<Booking> bookings = (List<Booking>) ((IBookingDAO) getDAO()).search(toSearch);
        return bookings;
    }

    /**
	 * Gets the possible solutions for the hotels based on the given criteria
	 * @param criteria the criteria for the booking
	 * @param begindate
	 * @param finishdate
	 * @return a map which contains the available solution based on customer requests
	 *         for each hotel in the specified destination or an empty map 
	 */
    @Transactional(readOnly = true, propagation = Propagation.REQUIRED)
    public Map<Hotel, List> getSolutionsFromCriteria(CriteriaDTO criteria, GregorianCalendar begindate, GregorianCalendar finishdate) {
        Map result = new HashMap<Structure, List>();
        Collection<Structure> structures = structureManager.searchAll("city", criteria.getDestinazione());
        for (Structure structure : structures) {
            SolutionHotelDTO solutions = getSolutionsForStructureFromCriteria(criteria, structure, begindate, finishdate);
            if (null != solutions) {
                result.put(structure, solutions);
            }
        }
        return result;
    }

    @Transactional(readOnly = true, propagation = Propagation.REQUIRED)
    public SolutionHotelDTO getSolutionsForStructureFromCriteria(CriteriaDTO dto, Structure structure, GregorianCalendar begindate, GregorianCalendar finishdate) {
        GregorianCalendar begindateCalendar = dto.getBeginDateAsDate();
        GregorianCalendar finishdateCalendar = dto.getFinishDateAsDate();
        Collection<Room> roomResult = this.getVacantRoomsForStructure(structure, begindateCalendar, finishdateCalendar);
        List<SolutionBookingDTO> solutionsAvailable = isCriteriaMatching(roomResult, dto.getPersons(), dto.getRooms(), begindate, finishdate);
        if (solutionsAvailable.size() > 0) {
            SolutionHotelDTO solution = new SolutionHotelDTO(solutionsAvailable);
            if (solution.getMinPrice().compareTo(dto.getPrice()) < 0 || dto.getPrice().equals(0.0)) {
                return solution;
            }
        }
        return null;
    }

    /**
	 * Gets the vacant rooms for the giveh hotel for the given dates
	 * @param hotel
	 * @param beginDate
	 * @param finishDate
	 * @return a collection which contains the available rooms or an empty list
	 */
    @Transactional(readOnly = true, propagation = Propagation.REQUIRED)
    public Collection<Room> getVacantRoomsForStructure(Structure structure, GregorianCalendar beginDate, GregorianCalendar finishDate) {
        Map toSearch = new HashMap();
        List<Room> freeRooms = new ArrayList<Room>();
        Collection<Room> rooms = roomManager.searchAll("structure", structure);
        Collection<Room> roomsOccupied = ((IBookingDAO) super.getDAO()).searchRoomNotAvailable(beginDate, finishDate, toSearch);
        if (roomsOccupied.size() == 0) {
            return rooms;
        }
        for (Room camera : rooms) {
            if (isAvailable(camera, roomsOccupied)) {
                freeRooms.add(camera);
            }
        }
        return freeRooms;
    }

    /**
	 * Restituisce l'insieme di camere libere secondo i criteri
	 * specificati dalla prenotazione quali data di inizio e fine,
	 * hotel, tipologia e numero di ospiti.
	 * @param combinationId
	 * @return a map which contains the available rooms or an empty map
	 */
    @Transactional(readOnly = true, propagation = Propagation.REQUIRED)
    public Map getRoomsByCombination(String combinationId, GregorianCalendar beginDate, GregorianCalendar finishDate, Structure structure) {
        Integer[] combinations = helper.parse(combinationId);
        Map<Typology, Collection<Room>> roomsByComb = new HashMap<Typology, Collection<Room>>();
        List<Typology> typologies = new ArrayList<Typology>();
        for (int i = 0; i < combinations.length; i++) {
            if (combinations[i] != 0) {
                typologies = typologyManager.getTypolgyByBeds(i + 1, structure);
                for (Typology typology : typologies) {
                    int tipologyID = typology.getId();
                    Collection<Room> rooms = this.getVacantRooms(beginDate, finishDate, tipologyID);
                    if (rooms.size() != 0) roomsByComb.put(typology, rooms);
                }
            }
        }
        return roomsByComb;
    }

    /**
	 * Aggiunge un booking nella lista dei booking se effettivamente la
	 * o le camere richieste sono disponibili nelle date specificate.
	 * Adds a booking to the bookings list if the request room is actually 
	 * available. 
	 * @param booking
	 * @return true if the specified rooms are available in the specified date range
	 */
    @Transactional(readOnly = false, propagation = Propagation.REQUIRED)
    public boolean checkAndEdit(Booking booking) {
        Booking tempBooking = (Booking) this.get(booking.getId());
        this.remove(tempBooking);
        if (!this.isVacant(booking.getRoom(), booking.getBeginDate(), booking.getFinishDate())) {
            this.add(tempBooking);
            return false;
        } else {
            this.add(booking);
        }
        return true;
    }

    public void setCustomerManager(ICustomerManager customerManager) {
        this.customerManager = customerManager;
    }

    public void setUnconfirmedBookingDAO(IhDAO unconfirmedBookingDAO) {
        this.unconfirmedBookingDAO = unconfirmedBookingDAO;
    }

    public void setHelper(BookingHelper helper) {
        this.helper = helper;
    }

    public void setCompletedBookingDAO(IhDAO completedBookingDAO) {
        this.completedBookingDAO = completedBookingDAO;
    }

    public void setConfirmedBookingDAO(IhDAO dao) {
        this.confirmedBookingDAO = dao;
    }

    public void setRoomManager(IRoomManager roomManager) {
        this.roomManager = roomManager;
    }

    public void setTypologyManager(ITypologyManager typologyManager) {
        this.typologyManager = typologyManager;
    }

    public void setStructureManager(IStructureManager structureManager) {
        this.structureManager = structureManager;
    }

    private List<SolutionBookingDTO> isCriteriaMatching(Collection<Room> roomResult, int persons, int rooms, GregorianCalendar begindate, GregorianCalendar finishdate) {
        Map<Integer, Integer> roomsGroupedByBeds = this.groupRooms(roomResult);
        List<SolutionBookingDTO> result = new ArrayList<SolutionBookingDTO>();
        if (roomResult.size() >= rooms) {
            List<Integer[]> combinations = helper.getFromRooms(rooms, persons);
            for (Integer[] combination : combinations) {
                if (isCombinationAvailable(combination, roomsGroupedByBeds)) {
                    result.add(new SolutionBookingDTO(combination, this.getMinMaxPrices(combination, roomResult, begindate, finishdate)));
                }
            }
        }
        return result;
    }

    /**
	 * Given a list of bookings, sets their type to checkinable if their start date is today
	 * @param bookings
	 */
    @Transactional(readOnly = true, propagation = Propagation.REQUIRED)
    public void IdentifyCheckinableBookings(List<Booking> bookings) {
        ;
        for (Booking booking : bookings) {
            if ((booking.getType().compareTo(Booking.TYPE_BOOKING) == 0) && CalendarUtils.IsToday(booking.getBeginDate())) {
                booking.setType(Booking.TYPE_CHECKINABLE);
            }
        }
    }

    private BigDecimal[] getMinMaxPrices(Integer[] combinations, Collection<Room> roomResult, GregorianCalendar begindate, GregorianCalendar finishdate) {
        BigDecimal[] prices = new BigDecimal[] { BigDecimal.ZERO, BigDecimal.ZERO };
        for (int i = 0; i < combinations.length; i++) {
            BigDecimal[] priceByBeds = getPriceByNumberOfBeds(roomResult, i + 1, combinations[i], begindate, finishdate);
            prices[0] = prices[0].add(priceByBeds[0]);
            prices[1] = prices[1].add(priceByBeds[1]);
        }
        return prices;
    }

    /**
	 * Is the given room included in the list of occupied rooms
	 * 
	 * @param room - room you want to verify
	 * @param roomsOccupied - list of occupied rooms
	 * @return true if the specified room is booking
	 */
    private boolean isAvailable(Room room, Collection<Room> roomsOccupied) {
        if (roomsOccupied == null) {
            return true;
        } else {
            for (Room roomOccupied : roomsOccupied) {
                if (roomOccupied.getId() == room.getId()) {
                    return false;
                }
            }
        }
        return true;
    }

    /**
	 * Calculates the price of the rooms based on the number of beds
	 * 
	 * @param roomResult
	 * @param beds
	 * @param numberOfRooms
	 * @return an array which contains the minimum and maximum price for the specified
	 *         typology
	 */
    private BigDecimal[] getPriceByNumberOfBeds(Collection<Room> roomResult, int beds, int numberOfRooms, GregorianCalendar begindate, GregorianCalendar finishdate) {
        if (numberOfRooms != 0) {
            BigDecimal[] minMax = new BigDecimal[] { new BigDecimal(Integer.MAX_VALUE), BigDecimal.ZERO };
            GregorianCalendar date = begindate;
            for (Room room : roomResult) {
                BigDecimal price = maxpriceTypology(room.getTypology(), begindate, finishdate);
                if (room.getTypology().getBeds() == beds) {
                    if (minPriceTypology(room.getTypology(), begindate, finishdate).compareTo(minMax[0]) == -1) {
                        minMax[0] = minPriceTypology(room.getTypology(), begindate, finishdate);
                    }
                    if (maxpriceTypology(room.getTypology(), begindate, finishdate).compareTo(minMax[1]) == 1) {
                        minMax[1] = maxpriceTypology(room.getTypology(), begindate, finishdate);
                    }
                }
            }
            minMax[0] = minMax[0].multiply(new BigDecimal(numberOfRooms));
            minMax[1] = minMax[1].multiply(new BigDecimal(numberOfRooms));
            return minMax;
        } else return new BigDecimal[] { BigDecimal.ZERO, BigDecimal.ZERO };
    }

    /**
	 * Verifies if the specific combination (based on the BookingHelper) is available in the hotel for the 
	 * available rooms for specific period
	 * 
	 * @param combinations
	 * @param roomsGroupedByBeds
	 * @return true if the specified combination is available, false otherwise
	 */
    private boolean isCombinationAvailable(Integer[] combinations, Map<Integer, Integer> roomsGroupedByBeds) {
        for (int i = 0; i < combinations.length; i++) {
            if (combinations[i] != 0) {
                if (roomsGroupedByBeds.get(i + 1) == null) {
                    return false;
                }
                if (roomsGroupedByBeds.get(i + 1) < combinations[i]) {
                    return false;
                }
            }
        }
        return true;
    }

    /**
	 * Groups the available rooms into a map of beds and rooms
	 * @param roomResult
	 * @return a map which contains the amount of rooms as a key and the corresponding number of beds as a field
	 *       
	 */
    private Map<Integer, Integer> groupRooms(Collection<Room> roomResult) {
        Map<Integer, Integer> roomsGroupedByBeds = new HashMap<Integer, Integer>();
        for (Room room : roomResult) {
            int beds = room.getTypology().getBeds();
            if (roomsGroupedByBeds.get(beds) == null) {
                roomsGroupedByBeds.put(beds, 1);
            } else {
                roomsGroupedByBeds.put(beds, (roomsGroupedByBeds.get(beds) + 1));
            }
        }
        return roomsGroupedByBeds;
    }

    /**
	 * Gets the maxPrice for the typology. 
	 * If we run out of prices we will use the last price available.
	 * @param typology
	 * @param begindate
	 * @param finishdate
	 */
    private BigDecimal maxpriceTypology(Typology typology, GregorianCalendar begindate, GregorianCalendar finishdate) {
        BigDecimal price = new BigDecimal(0);
        double priceDouble = 0.0;
        double typologyPrice = 0.0;
        GregorianCalendar date = begindate;
        int day = CalendarUtils.GetNumberOfDays(begindate, finishdate);
        for (int i = 0; i < (day + 1); i++) {
            priceDouble = price.doubleValue();
            BigDecimal priceForDate = typology.getPrice(date);
            if (priceForDate != null) {
                typologyPrice = priceForDate.doubleValue();
            }
            if (typologyPrice > priceDouble) {
                if (priceForDate != null) {
                    price = priceForDate;
                }
            }
            date = CalendarUtils.nexDay(date);
        }
        return price;
    }

    /**
	 * Gets the min price for the typology
	 * @param typology
	 * @param begindate
	 * @param finishdate
	 */
    private BigDecimal minPriceTypology(Typology typology, GregorianCalendar begindate, GregorianCalendar finishdate) {
        BigDecimal price = new BigDecimal(1000000);
        double priceDouble = 0.0;
        double typologyPrice = 0.0;
        GregorianCalendar date = begindate;
        int day = CalendarUtils.GetNumberOfDays(begindate, finishdate);
        for (int i = 0; i < (day + 1); i++) {
            priceDouble = price.doubleValue();
            BigDecimal priceForDate = typology.getPrice(date);
            if (priceForDate != null) {
                typologyPrice = priceForDate.doubleValue();
            }
            if (typologyPrice < priceDouble) {
                if (priceForDate != null) {
                    price = priceForDate;
                }
            }
            date = CalendarUtils.nexDay(date);
        }
        return price;
    }
}
