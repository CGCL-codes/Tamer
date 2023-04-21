package certforge.util.servlet;

import java.util.Enumeration;
import javax.servlet.ServletRequestEvent;
import javax.servlet.ServletRequestListener;
import javax.servlet.http.HttpServletRequest;
import javax.sql.DataSource;
import certforge.util.db.Session;

public class SessionRequestListener implements ServletRequestListener {

    private static final String SESSION = Session.class.getName();

    public static Session getSession(DataSource dataSource, String suffix) {
        HttpServletRequest req = Servlets.getRequest();
        Object value = req.getAttribute(SESSION + "." + suffix);
        if (value instanceof Session) {
            return (Session) value;
        }
        try {
            Session session = new Session(dataSource.getConnection());
            req.setAttribute(SESSION + "." + suffix, session);
            return session;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void requestInitialized(ServletRequestEvent event) {
    }

    @Override
    public void requestDestroyed(ServletRequestEvent event) {
        Enumeration<?> e = event.getServletRequest().getAttributeNames();
        while (e.hasMoreElements()) {
            String name = (String) e.nextElement();
            if (name.startsWith(SESSION)) {
                Object value = event.getServletRequest().getAttribute(name);
                if (value instanceof Session) {
                    ((Session) value).close();
                }
            }
        }
    }
}
