package openvspkm.util.servlet;

import java.util.Enumeration;
import javax.servlet.ServletRequestEvent;
import javax.servlet.ServletRequestListener;
import org.hibernate.Session;

public class CloseSessionRequestListener implements ServletRequestListener {

    public static final String SESSION = Session.class.getName();

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
