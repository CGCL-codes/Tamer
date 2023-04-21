package com.c2b2.ipoint.presentation.portlets.admin;

import com.c2b2.ipoint.messaging.PreferencesMessenger;
import com.c2b2.ipoint.model.Group;
import com.c2b2.ipoint.model.PersistentModelException;
import com.c2b2.ipoint.presentation.PortletRenderer;
import com.c2b2.ipoint.presentation.PresentationException;
import com.c2b2.ipoint.presentation.forms.fieldtypes.NumberField;
import com.c2b2.ipoint.util.PortalMailer;
import com.c2b2.ipoint.util.PortalMailerException;
import java.io.IOException;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import javax.mail.Address;
import javax.servlet.ServletException;

public class MailerPortlet extends PortletRenderer {

    public MailerPortlet() {
    }

    public void initialiseNew() throws PresentationException {
        try {
            myPortlet.storeProperty(MESSAGE_ROWS_PROPERTY, "20");
            myPortlet.storeProperty(MESSAGE_COLUMNS_PROPERTY, "60");
        } catch (PersistentModelException e) {
            throw new PresentationException("Failed to initialise the portlet");
        }
    }

    public void renderContent() throws PresentationException {
        myPR.setAttribute("CurrentMailerPortlet", this);
        try {
            myPR.includeJSP(myPortlet.getType().getInclude());
        } catch (IOException e) {
            throw new PresentationException("Unable to render the portlet", e);
        } catch (ServletException e) {
            throw new PresentationException("Unable to render the portlet", e);
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
        if (myPR.isPost()) {
            String mailerportletID = (String) myPR.getParameter("mailtogroup_portlet");
            if (mailerportletID != null && mailerportletID.equals(Long.toString(myPortlet.getID()))) {
                handlePost();
            }
        }
        String messageRows = getDefinitiveProperty(MESSAGE_ROWS_PROPERTY);
        if (messageRows != null) {
            myMessageRows = Integer.parseInt(messageRows);
        }
        String messageCols = getDefinitiveProperty(MESSAGE_COLUMNS_PROPERTY);
        if (messageCols != null) {
            myMessageColumns = Integer.parseInt(messageCols);
        }
        myPR.requireStyle("mailtogroup.css");
    }

    public List getValidProperties() {
        List result = super.getValidProperties();
        result.add(new NumberField(MESSAGE_ROWS_PROPERTY, "Message Rows", getDefinitiveProperty(MESSAGE_ROWS_PROPERTY)));
        result.add(new NumberField(MESSAGE_COLUMNS_PROPERTY, "Message Columns", getDefinitiveProperty(MESSAGE_COLUMNS_PROPERTY)));
        return result;
    }

    public int getMessageRows() {
        return myMessageRows;
    }

    public int getMessageColumns() {
        return myMessageColumns;
    }

    public List<Group> getAllGroups() throws PersistentModelException {
        return Group.getAllGroups();
    }

    private void handlePost() throws PresentationException {
        String groupID = (String) myPR.getParameter("groupid");
        String subject = (String) myPR.getParameter("subject");
        String message = (String) myPR.getParameter("message");
        if (groupID != null && message != null && subject != null) {
            try {
                Group group = Group.findGroup(Long.parseLong(groupID));
                myMailer = new PreferencesMessenger();
                mySent = myMailer.sendMail(subject, message, group);
                if (!mySent) {
                    myError = "Failed to send all the messages";
                }
            } catch (PersistentModelException e) {
                throw new PresentationException("Unable to find the group to mail messages to", e);
            }
        }
    }

    public boolean getSent() {
        return mySent;
    }

    public String getError() {
        return myError;
    }

    public Collection getSentAddresses() {
        return myMailer.getSuccesses();
    }

    public Collection getFailedAddresses() {
        return myMailer.getFailures();
    }

    private int myMessageRows = 20;

    private int myMessageColumns = 60;

    private boolean mySent = false;

    private String myError = "";

    private PreferencesMessenger myMailer;

    private static final String MESSAGE_ROWS_PROPERTY = "MessageRows";

    private static final String MESSAGE_COLUMNS_PROPERTY = "MessageColumns";
}
