package org.apache.myfaces.webapp;

import java.util.Enumeration;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.servlet.ServletContext;
import javax.servlet.ServletContextAttributeEvent;
import javax.servlet.ServletContextAttributeListener;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.ServletRequest;
import javax.servlet.ServletRequestAttributeEvent;
import javax.servlet.ServletRequestAttributeListener;
import javax.servlet.ServletRequestEvent;
import javax.servlet.ServletRequestListener;
import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpSessionAttributeListener;
import javax.servlet.http.HttpSessionBindingEvent;
import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;
import org.apache.myfaces.config.ManagedBeanDestroyer;
import org.apache.myfaces.config.RuntimeConfig;
import org.apache.myfaces.config.annotation.LifecycleProvider;

/**
 * Listens to
 *   - removing, replacing of attributes in context, session and request
 *   - destroying of context, session and request
 * for the ManagedBeanDestroyer to assure right destruction of managed beans in those scopes.
 * 
 * This listener is not registered in a tld or web.xml, but will be called by StartupServletContextListener.
 * 
 * @author Jakob Korherr (latest modification by $Author$)
 * @version $Revision$ $Date$
 * @since 2.0
 */
public class ManagedBeanDestroyerListener implements HttpSessionAttributeListener, HttpSessionListener, ServletContextListener, ServletContextAttributeListener, ServletRequestListener, ServletRequestAttributeListener {

    private ManagedBeanDestroyer destroyer = new ManagedBeanDestroyer();

    public void attributeAdded(HttpSessionBindingEvent event) {
    }

    public void attributeRemoved(HttpSessionBindingEvent event) {
        destroyer.destroy(event.getName(), event.getValue());
    }

    public void attributeReplaced(HttpSessionBindingEvent event) {
        destroyer.destroy(event.getName(), event.getValue());
    }

    public void sessionCreated(HttpSessionEvent event) {
    }

    @SuppressWarnings("unchecked")
    public void sessionDestroyed(HttpSessionEvent event) {
        HttpSession session = event.getSession();
        Enumeration<String> attributes = session.getAttributeNames();
        if (!attributes.hasMoreElements()) {
            return;
        }
        LifecycleProvider provider = destroyer.getCurrentLifecycleProvider();
        while (attributes.hasMoreElements()) {
            String name = attributes.nextElement();
            Object value = session.getAttribute(name);
            destroyer.destroy(name, value, provider);
        }
    }

    public void attributeAdded(ServletContextAttributeEvent event) {
    }

    public void attributeRemoved(ServletContextAttributeEvent event) {
        destroyer.destroy(event.getName(), event.getValue());
    }

    public void attributeReplaced(ServletContextAttributeEvent event) {
        destroyer.destroy(event.getName(), event.getValue());
    }

    public void contextInitialized(ServletContextEvent event) {
        FacesContext facesContext = FacesContext.getCurrentInstance();
        if (facesContext == null) {
            return;
        }
        ExternalContext externalContext = facesContext.getExternalContext();
        RuntimeConfig config = RuntimeConfig.getCurrentInstance(externalContext);
        destroyer.setRuntimeConfig(config);
    }

    @SuppressWarnings("unchecked")
    public void contextDestroyed(ServletContextEvent event) {
        ServletContext ctx = event.getServletContext();
        Enumeration<String> attributes = ctx.getAttributeNames();
        if (!attributes.hasMoreElements()) {
            return;
        }
        LifecycleProvider provider = destroyer.getCurrentLifecycleProvider();
        while (attributes.hasMoreElements()) {
            String name = attributes.nextElement();
            Object value = ctx.getAttribute(name);
            destroyer.destroy(name, value, provider);
        }
    }

    public void attributeAdded(ServletRequestAttributeEvent event) {
    }

    public void attributeRemoved(ServletRequestAttributeEvent event) {
        destroyer.destroy(event.getName(), event.getValue());
    }

    public void attributeReplaced(ServletRequestAttributeEvent event) {
        destroyer.destroy(event.getName(), event.getValue());
    }

    public void requestInitialized(ServletRequestEvent event) {
    }

    @SuppressWarnings("unchecked")
    public void requestDestroyed(ServletRequestEvent event) {
        ServletRequest request = event.getServletRequest();
        Enumeration<String> attributes = request.getAttributeNames();
        if (!attributes.hasMoreElements()) {
            return;
        }
        LifecycleProvider provider = destroyer.getCurrentLifecycleProvider();
        while (attributes.hasMoreElements()) {
            String name = attributes.nextElement();
            Object value = request.getAttribute(name);
            destroyer.destroy(name, value, provider);
        }
    }
}
