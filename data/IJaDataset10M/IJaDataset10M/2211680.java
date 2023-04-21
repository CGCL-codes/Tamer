package addressbook.servlet.model;

import javax.servlet.ServletContextAttributeEvent;
import javax.servlet.ServletContextAttributeListener;
import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;
import rogatkin.DataConv;
import addressbook.servlet.AddressBookProcessor;

/**
 * @author Dmitriy
 *
 */
public class OperationsFinalizer implements ServletContextAttributeListener, HttpSessionListener {

    public void attributeAdded(ServletContextAttributeEvent ae) {
    }

    public void attributeRemoved(ServletContextAttributeEvent ae) {
        if (AddressBookProcessor.FOLDEROPER.equals(ae.getName())) {
            Object val = ae.getValue();
            if (val instanceof FolderOperations) ((FolderOperations) val).saveAll();
        } else if (AddressBookProcessor.LOGOPER.equals(ae.getName())) {
            Object val = ae.getValue();
            if (val instanceof LogOperations) ((LogOperations) val).unload(null);
        }
    }

    public void attributeReplaced(ServletContextAttributeEvent ae) {
    }

    public void sessionCreated(HttpSessionEvent se) {
    }

    public void sessionDestroyed(HttpSessionEvent se) {
        try {
            HttpSession s = se.getSession();
            FolderOperations fo = (FolderOperations) s.getAttribute(AddressBookProcessor.ATTR_SESSION_FOLDER_OPER);
            if (fo != null) fo.save();
            String user = (String) s.getAttribute(AddressBookProcessor.HV_USER_ID);
            if (user != null) {
                LogOperations lo = (LogOperations) s.getServletContext().getAttribute(AddressBookProcessor.LOGOPER);
                if (lo != null) lo.logLogOut(user);
            } else System.err.println("Can't log logout");
        } catch (Exception e) {
            System.err.println("A problem's happened at processing a session destroy: " + e);
            e.printStackTrace();
        }
    }
}
