package it.hotel.controller.frontend.booking;

import it.hotel.controller.booking.DTO.CriteriaDTO;
import it.hotel.model.CalendarUtils;
import it.hotel.model.booking.Booking;
import it.hotel.model.booking.UnconfirmedBooking;
import it.hotel.model.booking.manager.IBookingManager;
import it.hotel.model.customer.Customer;
import it.hotel.model.customer.manager.ICustomerManager;
import it.hotel.model.mail.Mail;
import it.hotel.model.room.Room;
import it.hotel.model.structure.Structure;
import it.hotel.model.structure.manager.IStructureManager;
import it.hotel.model.typology.Typology;
import it.hotel.model.typology.manager.ITypologyManager;
import java.util.ArrayList;
import java.util.Collection;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.mail.MailException;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.BindException;
import org.springframework.validation.Errors;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.AbstractWizardFormController;

/**
 * 
 *
 */
public class BookingWizardFrontEnd extends AbstractWizardFormController {

    private IBookingManager bookingManager;

    private ICustomerManager customerManager;

    private IStructureManager structureManager;

    private ITypologyManager typologyManager;

    private String failurePage;

    private Mail mail;

    public BookingWizardFrontEnd() {
        super();
    }

    /**
	 * @throws
	 * @return
	 */
    @SuppressWarnings("unchecked")
    protected Map referenceData(HttpServletRequest req, Object command, Errors errors, int page) throws Exception {
        Map map = new HashMap();
        BookingFrontendDTO booking = (BookingFrontendDTO) command;
        if (page == 0) {
            CriteriaDTO criteria = (CriteriaDTO) req.getSession().getAttribute("dto");
            createBookingFromCriteria(booking, criteria);
            Collection structuresCollection = structureManager.searchAll("city", criteria.getDestinazione());
            map.put("structures", structuresCollection);
        }
        if (page == 1) {
            CriteriaDTO criteria = (CriteriaDTO) req.getSession().getAttribute("dto");
            createBookingFromCriteria(booking, criteria);
            Structure structure = (Structure) structureManager.get(booking.getStructure().getId());
            map.put("result", bookingManager.getSolutionsForStructureFromCriteria(criteria, booking.getStructure(), booking.getBeginDate(), booking.getFinishDate()));
            map.put("structure", structure);
        }
        if (page == 2) {
            ((BookingFrontendDTO) command).setRoomsToSelect(performPageTwo(command));
        }
        if (page == 3) {
            performPageThree(req, command);
            Customer customer = this.addAndGetCustomer(req, command);
            ((BookingFrontendDTO) command).setCustomer(customer);
            List<Room> roomsToBooking = ((BookingFrontendDTO) command).getRoomsToBooking();
            Long days = CalendarUtils.daysBetween(((Booking) command).getBeginDate(), ((Booking) command).getFinishDate());
        }
        this.setProperties(command);
        if ((page == 3) && (booking.getCustomer() != null)) {
            HttpServletResponse res = null;
            BindException arg3 = null;
            processFinish(req, res, command, arg3);
        }
        return map;
    }

    /**
	 * 
	 */
    protected void validatePage(Object command, Errors errors, int page, boolean finish) {
        if (page == 2) {
            getValidator().validate(command, errors);
        }
    }

    /**
	 * @param
	 */
    @Override
    protected void onBind(HttpServletRequest req, Object command) throws Exception {
        if (new Integer(2).equals(req.getSession().getAttribute(getPageSessionAttributeName()))) {
            this.performPageThree(req, command);
        }
        super.onBind(req, command);
    }

    /**
	 * @throws
	 */
    @Override
    protected void onBindAndValidate(HttpServletRequest request, Object command, BindException errors, int page) throws Exception {
        request.setAttribute("errors", errors.getAllErrors());
        super.onBindAndValidate(request, command, errors, page);
    }

    /**
     * 
     * @param req
     * @param command
     */
    private void performPageThree(HttpServletRequest req, Object command) {
        BookingFrontendDTO booking = (BookingFrontendDTO) command;
        Map<Typology, List<Room>> rooms = this.performPageTwo(command);
        List<Room> roomsToBooking = new ArrayList<Room>();
        List<Typology> tipologie = (List<Typology>) typologyManager.searchAll("structure", booking.getStructure());
        for (Typology tipologia : tipologie) {
            if (req.getParameter(tipologia.getName()) != null) {
                int typologyNumberRoom = Integer.parseInt(req.getParameter(tipologia.getName()));
                List<Room> roomsByTypology = this.getRoomsFromTypology(rooms, tipologia);
                for (int i = 0; i < typologyNumberRoom; i++) {
                    roomsToBooking.add(roomsByTypology.get(i));
                }
            }
        }
        booking.setRoomsToBooking(roomsToBooking);
    }

    private List<Room> getRoomsFromTypology(Map<Typology, List<Room>> rooms, Typology tipologia) {
        Set<Typology> keys = rooms.keySet();
        for (Typology key : keys) {
            if (tipologia.getId() == key.getId()) {
                return rooms.get(key);
            }
        }
        return null;
    }

    private Customer addAndGetCustomer(HttpServletRequest req, Object command) {
        BookingFrontendDTO prenotazione = (BookingFrontendDTO) command;
        Customer customer = new Customer();
        customer.setName(prenotazione.getName());
        customer.setSurname(prenotazione.getSurname());
        customer.setMail(prenotazione.getMail());
        customer.setStructure(prenotazione.getStructure());
        prenotazione.setCustomer(customer);
        return customer;
    }

    private Map performPageTwo(Object command) {
        Booking booking = (Booking) command;
        String combinazioneId = booking.getCombinationId();
        Map roomsByTypology = bookingManager.getRoomsByCombination(combinazioneId, booking.getBeginDate(), booking.getFinishDate(), booking.getStructure());
        return roomsByTypology;
    }

    private void setProperties(Object command) {
        Booking booking = (Booking) command;
        if (booking.getStructure().getId() != 0) {
            booking.setStructure((Structure) structureManager.get(booking.getStructure().getId()));
        }
    }

    @Override
    @Transactional(rollbackFor = { org.springframework.mail.MailException.class })
    protected ModelAndView processFinish(HttpServletRequest req, HttpServletResponse res, Object command, BindException arg3) throws MailException {
        if (req.getAttribute("booking") == null) {
            this.setProperties(command);
            BookingFrontendDTO booking = (BookingFrontendDTO) command;
            String begindate = CalendarUtils.GetDateAsString(booking.getBeginDate());
            String finishdate = CalendarUtils.GetDateAsString(booking.getFinishDate());
            String code = booking.getStructure().getId() + booking.getName() + booking.getSurname() + begindate + finishdate;
            customerManager.add(booking.getCustomer());
            for (Room room : booking.getRoomsToBooking()) {
                Booking bookingSingleRoom = new UnconfirmedBooking();
                bookingSingleRoom.setAccomodation(booking.getAccomodation());
                bookingSingleRoom.setBeginDate(booking.getBeginDate());
                bookingSingleRoom.setCustomer(booking.getCustomer());
                bookingSingleRoom.setFinishDate(booking.getFinishDate());
                bookingSingleRoom.setStructure(booking.getStructure());
                bookingSingleRoom.setRoom(room);
                bookingSingleRoom.setCode(code);
                bookingManager.add(bookingSingleRoom);
                try {
                    ArrayList<Booking> bookings = (ArrayList<Booking>) bookingManager.searchByExample(bookingSingleRoom);
                    req.setAttribute("booking", bookings.get(0));
                    mail.sendMailHotel(bookingSingleRoom);
                    mail.sendMailCustomer(bookingSingleRoom);
                } catch (MailException e) {
                    System.out.println(e.getMessage());
                    bookingManager.remove(bookingSingleRoom.getId());
                    throw e;
                }
            }
            return new ModelAndView(getSuccessView(), "bookingFrontendDTO", booking);
        } else {
            req.removeAttribute("booking");
            return null;
        }
    }

    private void createBookingFromCriteria(BookingFrontendDTO booking, CriteriaDTO criteria) {
        booking.setBeginDate(CalendarUtils.GetGregorianCalendar(criteria.getBegindate()));
        booking.setFinishDate(CalendarUtils.GetGregorianCalendar(criteria.getFinishdate()));
        booking.setBegindate(criteria.getBegindate());
        booking.setFinishdate(criteria.getFinishdate());
        GregorianCalendar begindate = CalendarUtils.GetGregorianCalendar(criteria.getBegindate());
        GregorianCalendar endDate = CalendarUtils.GetGregorianCalendar(criteria.getFinishdate());
    }

    public String getSuccessView() {
        return getPages()[getPages().length - 1];
    }

    @Resource(name = "customerRawManager")
    public void setCustomerManager(ICustomerManager customerManager) {
        this.customerManager = customerManager;
    }

    @Resource(name = "localizedTypologyManager")
    public void setTypologyManager(ITypologyManager typologyManager) {
        this.typologyManager = typologyManager;
    }

    @Resource(name = "bookingRawManager")
    public void setBookingManager(IBookingManager bookingManager) {
        this.bookingManager = bookingManager;
    }

    @Resource(name = "mail")
    public void setMail(Mail mail) {
        this.mail = mail;
    }

    public String getFailurePage() {
        return failurePage;
    }

    public void setFailurePage(String failurePage) {
        this.failurePage = failurePage;
    }

    public IStructureManager getStructureManager() {
        return structureManager;
    }

    @Resource(name = "localizedStructureManager")
    public void setStructureManager(IStructureManager structureManager) {
        this.structureManager = structureManager;
    }
}
