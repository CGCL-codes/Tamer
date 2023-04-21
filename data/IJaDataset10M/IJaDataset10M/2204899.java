package com.jcorporate.expresso.services.dbobj;

import com.jcorporate.expresso.core.db.DBException;
import com.jcorporate.expresso.core.dbobj.RequestContext;
import com.jcorporate.expresso.core.dbobj.SecuredDBObject;
import com.jcorporate.expresso.core.logging.LogException;
import com.jcorporate.expresso.core.misc.ConfigManager;
import com.jcorporate.expresso.core.misc.ConfigurationException;
import com.jcorporate.expresso.core.misc.EMailSender;
import com.jcorporate.expresso.core.misc.StringUtil;
import com.jcorporate.expresso.core.security.User;
import com.jcorporate.expresso.kernel.util.FastStringBuffer;
import org.apache.log4j.Logger;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.Vector;

/**
 * Event
 *
 * @author        Michael Nash
 */
public class Event extends SecuredDBObject {

    private static Logger log = Logger.getLogger(Event.class);

    /**
     * use this as theEvent parameter in order to generate admin email
     * @see #Event(java.lang.String, java.lang.String, java.lang.String, boolean)
     */
    public static final String SYSTEM_ERROR = "SYSERROR";

    /**
     * @see com.jcorporate.expresso.core.dbobj.SecuredDBObject
     *
     * @throws  DBException
     */
    public Event() throws DBException {
        super();
    }

    /**
     *@param uid the expresso uid
     * @throws DBException upon initialization error
     */
    public Event(int uid) throws DBException {
        super(uid);
    }

    /**
     * For using DBObjects within Controllers.  Initializes based upon the current
     * user and the requested db. [Of course this can be modified later]
     *
     * @param request - The request context handed to you by the framework.
     * @throws DBException upon initialization error
     */
    public Event(RequestContext request) throws DBException {
        super(request);
    }

    /**
     * The one-liner version of event via this special constructor
     * This method is used to create the event and send the notifications all
     * in one step.
     *
     * @param    dbName the database context name
     * @param    theEvent Code of the event to trigger
     * @param    theMessage Detail message associated for the event
     * @param    success True if the event is the success of a task,
     *            false if it indicates failure
     * @throws    DBException If there is problem sending the notification
     * @throws  LogException
     */
    public Event(String dbName, String theEvent, String theMessage, boolean success) throws DBException, LogException {
        this(SecuredDBObject.SYSTEM_ACCOUNT);
        setDataContext(dbName);
        setField("Event", theEvent);
        try {
            retrieve();
        } catch (DBException de) {
            if (!theEvent.equalsIgnoreCase(SYSTEM_ERROR)) {
                new Event(dbName, SYSTEM_ERROR, "Could not find event '" + theEvent + "'", false);
                throw new DBException("No such event '" + theEvent + "'");
            }
        }
        Vector mailMessage = new Vector(1);
        mailMessage.addElement(theMessage);
        sendMail(mailMessage, success);
    }

    /**
     *
     *
     * @throws    DBException If there is a problem retrieving the values
     * @return Vector of valid values
     */
    public Vector getValues() throws DBException {
        return getValuesDefault("Event", "Descrip");
    }

    /**
     * Once an Event has been retrieved, this method allows the e-mail
     * notifications to be sent to each of the appropriate users
     *
     * @param    theMessage A Vector of strings containing the text of
     *             the message to be sent
     * @param    success Is this message a notification of success?
     * @throws    DBException on any error, including a mail system error
     * @throws  LogException
     */
    public void sendMail(Vector theMessage, boolean success) throws DBException, LogException {
        EventMail myEventMails = new EventMail(SecuredDBObject.SYSTEM_ACCOUNT);
        myEventMails.setDataContext(getDataContext());
        EventMail oneEventMail = null;
        User oneUser = new User();
        oneUser.setDataContext(getDataContext());
        FastStringBuffer bigString = new FastStringBuffer(256);
        String oneRecipient = ("");
        for (Enumeration e = theMessage.elements(); e.hasMoreElements(); ) {
            bigString.append((String) e.nextElement());
            bigString.append("\n");
        }
        bigString.append("\nFrom Server:");
        bigString.append(Setup.getValueRequired(getDataContext(), "HTTPServ"));
        bigString.append(", Database/context:");
        bigString.append(getDataContext());
        String dbDescrip = "";
        try {
            dbDescrip = StringUtil.notNull(ConfigManager.getContext(getDataContext()).getDescription());
        } catch (ConfigurationException ce) {
            throw new DBException(ce);
        }
        if (!dbDescrip.equals("")) {
            bigString.append(" (");
            bigString.append(dbDescrip);
            bigString.append(")");
        }
        myEventMails.setField("Event", this.getField("Event"));
        if (success) {
            myEventMails.setField("Success", "Y");
        } else {
            myEventMails.setField("Success", "N");
        }
        int theCount = 0;
        for (Iterator em = myEventMails.searchAndRetrieveList().iterator(); em.hasNext(); ) {
            oneEventMail = (EventMail) em.next();
            theCount++;
            oneUser.clear();
            oneUser.setUid(oneEventMail.getField("ExpUid"));
            if (!oneUser.find()) {
                log.error("Attempting to send event notification " + "for event '" + getField("Event") + "' to " + "user " + oneEventMail.getField("ExpUid") + ", but this user not found in the '" + getDataContext() + "' context.");
                continue;
            }
            oneRecipient = oneUser.getEmail();
            if (log.isInfoEnabled()) {
                log.info("Notifying " + oneRecipient + ":" + this.getField("Descrip"));
            }
            try {
                String subject = null;
                if (success) {
                    subject = (this.getField("Descrip"));
                } else {
                    subject = ("ERROR:" + getField("Descrip"));
                }
                EMailSender ems = new EMailSender();
                ems.setDBName(getDataContext());
                ems.send(oneRecipient, subject, bigString.toString());
            } catch (Exception e) {
                throw new DBException("Error sending e-mail", e);
            }
        }
        if (log.isInfoEnabled()) {
            log.info("Sent " + theCount + " e-mail notifications");
        }
        if (theCount == 0) {
            String successFlag = "N";
            if (success) {
                successFlag = "Y";
            }
            log.warn("No users in db/context '" + getDataContext() + "' were set " + "to receive notice of event '" + getField("Event") + "' with " + " success flag '" + successFlag + "', so " + "no notices were sent.");
        }
    }

    /**
     * Set up the database fields for this object
     *
     * @throws    DBException
     */
    protected synchronized void setupFields() throws DBException {
        setTargetTable("EVENT");
        setDescription("DBevent");
        setCharset("ISO-8859-1");
        addField("Event", "char", 30, false, "eventCode");
        addField("Descrip", "varchar", 80, false, "eventDescrip");
        setStringFilter("Event", "stripFilter");
        setStringFilter("Descrip", "standardFilter");
        addKey("Event");
        addDetail("com.jcorporate.expresso.services.dbobj.EventMail", "Event", "Event");
    }

    /**
     *
     *
     * @throws  DBException
     */
    public synchronized void populateDefaultValues() throws DBException {
        if (log.isInfoEnabled()) {
            log.info("Populating default values for Events in db '" + getDataContext() + "'");
        }
        Event oneEvent = new Event();
        oneEvent.setDataContext(getDataContext());
        oneEvent.setField("Event", SYSTEM_ERROR);
        if (!oneEvent.find()) {
            oneEvent.setField("Descrip", "System Error");
            oneEvent.add();
            if (log.isInfoEnabled()) {
                log.info("Added 'SYSERROR' event");
            }
        } else {
            if (log.isInfoEnabled()) {
                log.info("'SYSERROR' event already set up");
            }
        }
        oneEvent.clear();
        oneEvent.setField("Event", "HEALTH");
        if (!oneEvent.find()) {
            oneEvent.setField("Descrip", "System Health Check");
            oneEvent.add();
            if (log.isInfoEnabled()) {
                log.info("Added 'HEALTH' event");
            }
        } else {
            if (log.isInfoEnabled()) {
                log.info("'HEALTH' event already set up");
            }
        }
        oneEvent.clear();
        oneEvent.setField("Event", "DOWNLOAD");
        if (!oneEvent.find()) {
            oneEvent.setField("Descrip", "User Download Activity");
            oneEvent.add();
            if (log.isInfoEnabled()) {
                log.info("Added 'DOWNLOAD' event");
            }
        } else {
            if (log.isInfoEnabled()) {
                log.info("'DOWNLOAD' event already set up");
            }
        }
        oneEvent.clear();
        oneEvent.setField("Event", "REGISTER");
        if (!oneEvent.find()) {
            oneEvent.setField("Descrip", "User Registration Activity");
            oneEvent.add();
            if (log.isInfoEnabled()) {
                log.info("Added 'REGISTER' event");
            }
        } else {
            if (log.isInfoEnabled()) {
                log.info("'REGISTER' event already set up");
            }
        }
    }
}
