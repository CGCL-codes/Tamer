package org.openlogbooks.webapp.listener;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.http.HttpSessionAttributeListener;
import javax.servlet.http.HttpSessionBindingEvent;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openlogbooks.model.User;

/**
 * UserCounterListener class used to count the current number of active users
 * for the applications. Does this by counting how many user objects are stuffed
 * into the session. It Also grabs these users and exposes them in the servlet
 * context.
 * 
 * @author <a href="mailto:peter.neil@logbookmanager.com">Peter Neil</a>
 * 
 * @web.listener
 */
public class UserCounterListener implements ServletContextListener, HttpSessionAttributeListener {

    public static final String COUNT_KEY = "userCounter";

    public static final String USERS_KEY = "userNames";

    private final transient Log log = LogFactory.getLog(UserCounterListener.class);

    private transient ServletContext servletContext;

    private int counter;

    public synchronized void contextInitialized(ServletContextEvent sce) {
        servletContext = sce.getServletContext();
        servletContext.setAttribute((COUNT_KEY), Integer.toString(counter));
    }

    public synchronized void contextDestroyed(ServletContextEvent event) {
        if (log.isDebugEnabled()) {
            log.debug("context destroyed: " + event.getServletContext().getServerInfo());
        }
        servletContext = null;
        counter = 0;
    }

    synchronized void incrementUserCounter() {
        counter = Integer.parseInt((String) servletContext.getAttribute(COUNT_KEY));
        counter++;
        servletContext.setAttribute(COUNT_KEY, Integer.toString(counter));
        if (log.isDebugEnabled()) {
            log.debug("User Count: " + counter);
        }
    }

    synchronized void decrementUserCounter() {
        int counter = Integer.parseInt((String) servletContext.getAttribute(COUNT_KEY));
        counter--;
        if (counter < 0) {
            counter = 0;
        }
        servletContext.setAttribute(COUNT_KEY, Integer.toString(counter));
        if (log.isDebugEnabled()) {
            log.debug("User Count: " + counter);
        }
    }

    synchronized void addUsername(User user) {
        incrementUserCounter();
    }

    synchronized void removeUsername(Object user) {
        decrementUserCounter();
    }

    /**
	 * This method is designed to catch when user's login and record their name
	 * 
	 * @see javax.servlet.http.HttpSessionAttributeListener#attributeAdded(javax.servlet.http.HttpSessionBindingEvent)
	 */
    public void attributeAdded(HttpSessionBindingEvent event) {
        if (event.getName().equals(org.openlogbooks.Constants.USER_KEY)) {
            addUsername((User) event.getValue());
        }
    }

    /**
	 * When user's logout, remove their name from the hashMap
	 * 
	 * @see javax.servlet.http.HttpSessionAttributeListener#attributeRemoved(javax.servlet.http.HttpSessionBindingEvent)
	 */
    public void attributeRemoved(HttpSessionBindingEvent event) {
        if (event.getName().equals(org.openlogbooks.Constants.USER_KEY)) {
            removeUsername(event.getValue());
        }
    }

    /**
	 * @see javax.servlet.http.HttpSessionAttributeListener#attributeReplaced(javax.servlet.http.HttpSessionBindingEvent)
	 */
    public void attributeReplaced(HttpSessionBindingEvent event) {
        if (log.isDebugEnabled()) {
            log.debug("SessionBindingEvent: " + event.getName());
        }
    }
}
