package org.sergioveloso.spott.command;

import java.util.Date;
import java.util.GregorianCalendar;
import javax.servlet.http.HttpServletRequest;
import org.sergioveloso.spott.model.db.DAOFactory;
import org.sergioveloso.spott.model.entity.Person;
import org.sergioveloso.spott.model.db.DAOFactory;
import org.sergioveloso.spott.util.LoggerFacade;
import org.sergioveloso.spott.exception.DAOException;

public class CommandHelper {

    private static CommandHelper instance = null;

    private CommandHelper() {
    }

    public static synchronized CommandHelper getInstance() {
        if (null == instance) {
            instance = new CommandHelper();
        }
        return instance;
    }

    /** Given a string 'DD-MM-YYYY', returns the corresponding Date object
	 *  Example: 05-04-2006
	 *  
	 *  Will return null if there is any problem...
	 */
    public Date parseRequestDate(String requestDate) {
        Date dt = null;
        try {
            String[] tokens = requestDate.split("-");
            int day = Integer.parseInt(tokens[0]);
            int month = Integer.parseInt(tokens[1]) - 1;
            int year = Integer.parseInt(tokens[2]);
            dt = new GregorianCalendar(year, month, day).getTime();
        } catch (Throwable ignored) {
        }
        return dt;
    }

    /** Return the Person in the database corresponding to the logged user 
	 *
	 *  IMPORTANT: Will return null if there is no Session, no logged user 
	 *             or even if *any* error occurs. 
	 *
	 */
    public Person getCurrentUser(HttpServletRequest req) {
        Person user = null;
        if (null != req.getSession(false)) {
            try {
                Long personID = (Long) req.getSession().getAttribute("userID");
                user = DAOFactory.createPersonHibernateDAO().getPersonById(personID);
            } catch (DAOException de) {
                LoggerFacade.log("CommandHelper got DAOException: " + de.getMessage() + " and will return NULL.");
            } catch (Throwable t) {
                LoggerFacade.log("CommandHelper got Throwable: " + t.getMessage() + " and will return NULL.");
            }
        } else {
            LoggerFacade.log("CommandHelper found no Session to query... returning NULL.");
        }
        return user;
    }
}
