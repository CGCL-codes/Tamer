package fhj.itm05.seminarswe.web;

import java.util.*;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import fhj.itm05.seminarswe.database.BookingDAOHsqldb;

/**
 * Takes the right booking for deletion
 * @author H.Pittesser
 * @version 0.1 alpha
 */
public class BookingDeleteController implements Controller {

    public Map<String, Object> handleRequest(HttpServletRequest request, HttpServletResponse response, ServletContext context) {
        Map<String, Object> bookings = new HashMap<String, Object>();
        bookings.put("urls", ResourceBundle.getBundle(Dispatcher.URLS_BUNDLE_PATH));
        try {
            int id = Integer.parseInt(request.getParameter("id"));
            bookings.put("booking", BookingDAOHsqldb.getInstance().getBooking(id));
        } catch (NumberFormatException nfe) {
            System.out.println("ERROR: ID format invalid");
            nfe.printStackTrace();
        }
        return bookings;
    }
}
