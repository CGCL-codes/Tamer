package com.sun.j2ee.blueprints.consumerwebsite;

import java.beans.Beans;
import java.util.*;
import javax.servlet.http.*;
import javax.servlet.*;
import com.sun.j2ee.blueprints.signon.web.SignOnFilter;
import com.sun.j2ee.blueprints.customer.*;
import com.sun.j2ee.blueprints.consumerwebsite.actions.*;

/**
 * This class will bind with the current session and notify the Adventure
 * Builder Back end when a SignOn has occured.
 * 
 * This allows for a loose coupling of the SignOn component and the Adventure
 * Builder Application. Ensure the neccessary setup is done when a user signs
 * in.
 */
public class SignOnNotifier implements java.io.Serializable, HttpSessionAttributeListener {

    public SignOnNotifier() {
    }

    public void attributeRemoved(HttpSessionBindingEvent se) {
    }

    /**
	 * 
	 * Process an attribute added
	 * 
	 */
    public void attributeAdded(HttpSessionBindingEvent se) {
        processEvent(se);
    }

    /**
	 * Process the update
	 */
    public void attributeReplaced(HttpSessionBindingEvent se) {
        processEvent(se);
    }

    private void processEvent(HttpSessionBindingEvent se) {
        HttpSession session = se.getSession();
        String name = se.getName();
        if (name.equals(SignOnFilter.SIGNED_ON_USER)) {
            boolean aSignOn = ((Boolean) se.getValue()).booleanValue();
            if (aSignOn) {
                AdventureComponentManager acm = (AdventureComponentManager) session.getAttribute(AdventureKeys.COMPONENT_MANAGER);
                CustomerFacade facade = null;
                if (acm != null) {
                    facade = acm.getCustomerFacade(session);
                    CustomerHTMLAction action = new CustomerHTMLAction();
                    CustomerBean bean = null;
                    try {
                        bean = action.readAccount(session, facade);
                    } catch (Exception cex) {
                        cex.printStackTrace();
                    }
                    session.setAttribute(AdventureKeys.CUSTOMER_BEAN, bean);
                }
            }
        }
    }
}
