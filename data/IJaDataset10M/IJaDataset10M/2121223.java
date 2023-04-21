package es.devel.opentrats.view.controller;

import es.devel.opentrats.dao.IAppointmentDao;
import es.devel.opentrats.dao.ICustomerDao;
import es.devel.opentrats.dao.exception.AppointmentDaoException;
import es.devel.opentrats.dao.exception.CustomerDaoException;
import es.devel.opentrats.model.Appointment;
import es.devel.opentrats.model.Customer;
import es.devel.opentrats.view.controller.common.OpenTratsAbstractController;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.web.servlet.ModelAndView;

/**
 *
 * @author pau
 */
public class NextAppointmentAbstractController extends OpenTratsAbstractController {

    private IAppointmentDao appointmentDao;

    private ICustomerDao customerDao;

    public NextAppointmentAbstractController() {
    }

    @Override
    protected ModelAndView handleRequestInternal(HttpServletRequest request, HttpServletResponse response) throws Exception {
        String idCustomer = (String) request.getParameter("idCustomer");
        SimpleDateFormat df = new SimpleDateFormat("EEEE dd MMMM yyyy");
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
        PrintWriter out = response.getWriter();
        Appointment appointment = null;
        Customer customer = null;
        if (idCustomer != null && idCustomer.equals("") == false) {
            try {
                appointment = (Appointment) appointmentDao.getNextAppointment(new Long(idCustomer));
            } catch (AppointmentDaoException e) {
                e.printStackTrace();
            }
            try {
                customer = (Customer) customerDao.load(new Long(idCustomer));
            } catch (CustomerDaoException e) {
                e.printStackTrace();
            }
        }
        if (appointment != null) {
            out.print("# D/D�a." + customer.getSurname() + ", " + customer.getName() + " tiene su proxima cita el d�a " + df.format(appointment.getDay()) + "" + " a las " + sdf.format(appointment.getStartHour()) + " </br> ");
            String services = (String) appointment.getService();
            if (services != null) {
                String[] arrayServices = services.split(";");
                out.print("<ul>");
                for (String item : arrayServices) {
                    out.print("<li>" + item + "</li>");
                }
                out.print("</ul>");
            }
        } else {
            out.print("El cliente no tiene citas a la vista.");
        }
        out.flush();
        out.close();
        return null;
    }

    public IAppointmentDao getAppointmentDao() {
        return appointmentDao;
    }

    public void setAppointmentDao(IAppointmentDao appointmentDao) {
        this.appointmentDao = appointmentDao;
    }

    public ICustomerDao getCustomerDao() {
        return customerDao;
    }

    public void setCustomerDao(ICustomerDao customerDao) {
        this.customerDao = customerDao;
    }
}
