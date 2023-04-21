package com.c2b2.ipoint.presentation.portlets;

import com.c2b2.ipoint.model.Notification;
import com.c2b2.ipoint.model.Page;
import com.c2b2.ipoint.model.PersistentModelException;
import com.c2b2.ipoint.model.Property;
import com.c2b2.ipoint.presentation.PortletRenderer;
import com.c2b2.ipoint.presentation.PresentationException;
import com.c2b2.ipoint.presentation.forms.fieldtypes.PageField;
import java.io.IOException;
import java.util.List;
import java.util.logging.Level;
import javax.servlet.ServletException;

/**
  * $Id: YouGotMessagesPortlet.java,v 1.2 2006/10/12 12:18:33 steve Exp $
  * 
  * Copyright 2005 C2B2 Consulting Limited. All rights reserved.
  * 
  * This classimplements theYou've Got Messages portlet
  * this portlet indicates whether a user has any unread Notifications.
  * If there are no unread Notifications the portlet doesn't render.
  * 
  * @author $Author: steve $
  * @version $Revision: 1.2 $
  * $Date: 2006/10/12 12:18:33 $
  * 
  */
public class YouGotMessagesPortlet extends PortletRenderer {

    public static final String PAGE_PROPERTY = "NotificationsPage";

    public static final String CURRENT_YOUGOTMESSAGES_PORTLET_PROPERTY = "CurrentYGMPortlet";

    public YouGotMessagesPortlet() {
    }

    public boolean canRender() throws PresentationException {
        boolean result = false;
        try {
            if (super.canRender() && (getUnreadMessages() > 0 || myPortlet.isEditableBy(myPR.getCurrentUser()))) {
                result = true;
            }
        } catch (PersistentModelException e) {
            myLogger.warning("Unable to find out whether the portlet is editable");
        }
        return result;
    }

    public List getValidProperties() {
        List result = super.getValidProperties();
        String notificationsPage = getDefinitiveProperty(PAGE_PROPERTY);
        if (notificationsPage != null) {
            try {
                Page page = Page.findPage(notificationsPage);
                PageField pf = new PageField(PAGE_PROPERTY, "Notifications Page", page, false, false);
                result.add(pf);
            } catch (PersistentModelException e) {
                myLogger.log(Level.WARNING, "Stored property for Page could not be found");
            }
        }
        return result;
    }

    public void initialiseNew() throws PresentationException {
        try {
            myPortlet.storeProperty(PAGE_PROPERTY, Property.getPropertyValue(PAGE_PROPERTY));
        } catch (PersistentModelException e) {
            myLogger.log(Level.WARNING, "Unable to find the mandatory Portal Property " + PAGE_PROPERTY, e);
        }
    }

    public void renderContent() throws PresentationException {
        String jsp = myPortlet.getType().getInclude();
        myPR.setAttribute(CURRENT_YOUGOTMESSAGES_PORTLET_PROPERTY, this);
        try {
            myPR.includeJSP(jsp);
        } catch (IOException e) {
            throw new PresentationException("Unable to write output", e);
        } catch (ServletException e) {
            throw new PresentationException("Unable to process the JSP " + jsp, e);
        }
    }

    public void renderEdit() throws PresentationException {
        renderContent();
    }

    public void renderHelp() throws PresentationException {
    }

    public void renderMinimized() throws PresentationException {
    }

    public void preProcess() throws PresentationException {
        super.preProcess();
        myPR.requireStyle("ygm.css");
        try {
            List msgs = Notification.findUnreadNotificationsFor(myPR.getCurrentUser());
            unreadMessages = msgs.size();
        } catch (PersistentModelException e) {
            myLogger.log(Level.WARNING, "Unable to find the number of Unread Messages", e);
        }
    }

    public int getUnreadMessages() {
        return unreadMessages;
    }

    public String getNotificationPageURL() {
        return myPR.getRequest().getContextPath() + "/" + getDefinitiveProperty(PAGE_PROPERTY) + ".page";
    }

    private int unreadMessages = 0;
}
