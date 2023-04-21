package it.hotel.controller.booking;

import it.hotel.controller.booking.DTO.VacancyDTO;
import it.hotel.controller.user.IUserContainer;
import it.hotel.model.booking.Booking;
import it.hotel.model.booking.manager.IBookingManager;
import it.hotel.model.customer.Customer;
import it.hotel.model.customer.manager.ICustomerManager;
import it.hotel.model.room.Room;
import it.hotel.model.room.manager.IRoomManager;
import it.hotel.model.structure.Structure;
import it.hotel.model.structure.manager.IStructureManager;
import it.hotel.model.typology.manager.ITypologyManager;
import it.hotel.system.SystemUtils;
import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Map;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.validation.BindException;
import org.springframework.validation.Errors;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.AbstractWizardFormController;

/**
 * 
 *
 */
public class BookingWizardFormController extends AbstractWizardFormController {

    private IBookingManager bookingManager;

    private IRoomManager roomManager;

    private ICustomerManager customerManager;

    private IStructureManager structureManager;

    private ITypologyManager typologyManager;

    private IUserContainer userContainer;

    private String failurePage;

    /**
	 * @throws
	 * @return
	 */
    protected Map referenceData(HttpServletRequest req, Object command, Errors errors, int page) throws Exception {
        int idHotel = userContainer.getUser().getStructureId();
        Map map = new HashMap();
        Booking booking = (Booking) command;
        if (page == 0) {
            VacancyDTO dto = (VacancyDTO) req.getSession().getAttribute("backofficeDTO");
            booking.setBeginDate(dto.getBeginDate());
            booking.setFinishDate(dto.getFinishDate());
            map.put("begindate", dto.getBegindate());
            map.put("finishdate", dto.getFinishdate());
            map.put("typologies", typologyManager.getTypologiesFromStructure(idHotel));
            map.put("camere", bookingManager.getVacantRooms(dto.getBeginDate(), dto.getFinishDate(), dto.getTypologyId()));
        }
        if (page == 1) {
            ArrayList<Structure> hotels = new ArrayList<Structure>();
            hotels.add((Structure) structureManager.get(idHotel));
            map.put("hotels", hotels);
        }
        if (page == 3) {
            ArrayList<Structure> hotels = new ArrayList<Structure>();
            hotels.add((Structure) structureManager.get(idHotel));
            map.put("hotels", hotels);
            ;
        }
        String id = req.getParameter("id");
        if (booking.getId() == 0 && id != null && !"0".equals(id)) {
            booking = (Booking) bookingManager.get(Integer.parseInt(id));
            map.put("booking", booking);
        }
        this.setProperties(command);
        map.put("errors", errors.getAllErrors());
        return map;
    }

    public String getSuccessView() {
        return getPages()[getPages().length - 1];
    }

    @Resource(name = "bookingManager")
    public void setBookingManager(IBookingManager bookingManager) {
        this.bookingManager = bookingManager;
    }

    @Resource(name = "roomManager")
    public void setRoomManager(IRoomManager roomManager) {
        this.roomManager = roomManager;
    }

    @Resource(name = "customerManager")
    public void setCustomerManager(ICustomerManager customerManager) {
        this.customerManager = customerManager;
    }

    @Resource(name = "structureManager")
    public void setStructureManager(IStructureManager structureManager) {
        this.structureManager = structureManager;
    }

    @Resource(name = "typologyManager")
    public void setTypologyManager(ITypologyManager typologyManager) {
        this.typologyManager = typologyManager;
    }

    @Resource(name = "userContainer")
    public void setUserContainer(IUserContainer userContainer) {
        this.userContainer = userContainer;
    }

    /**
     * 
     * @param command
     */
    private void setProperties(Object command) {
        Booking booking = (Booking) command;
        if (booking.getRoomId() != 0) {
            Room room = (Room) roomManager.get(booking.getRoomId());
            booking.setRoom(room);
        }
        if (booking.getCustomerId() != 0) {
            booking.setCustomer((Customer) customerManager.get(booking.getCustomerId()));
        }
        if (booking.getStructure().getId() != 0) {
            booking.setStructure((Structure) structureManager.get(booking.getStructure().getId()));
        }
    }

    /**
	 * @throws
	 * @return
	 */
    @Override
    protected ModelAndView processFinish(HttpServletRequest arg0, HttpServletResponse arg1, Object command, BindException arg3) throws Exception {
        this.setProperties(command);
        Booking booking = (Booking) command;
        GregorianCalendar begindateCalendar = booking.getBeginDate();
        GregorianCalendar finishdateCalendar = booking.getFinishDate();
        if (!bookingManager.isVacant(booking.getRoom(), begindateCalendar, finishdateCalendar)) {
            return new ModelAndView(getFailurePage(), "roomNumber", booking.getRoom().getNumber());
        }
        bookingManager.add(command);
        SystemUtils.print("success", getSuccessView());
        return new ModelAndView(getSuccessView());
    }

    protected void validatePage(Object command, Errors errors, int page, boolean finish) {
        getValidator().validate(command, errors);
    }

    public String getFailurePage() {
        return failurePage;
    }

    public void setFailurePage(String failurePage) {
        this.failurePage = failurePage;
    }
}
