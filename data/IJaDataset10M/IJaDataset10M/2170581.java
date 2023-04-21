package org.synthful.automata;

import java.util.Collection;
import java.util.Date;
import java.util.Enumeration;
import java.util.GregorianCalendar;
import java.util.Hashtable;
import java.util.Vector;
import java.util.WeakHashMap;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jdom.Attribute;
import org.jdom.Element;
import org.synthful.xml.XmlParser;
import org.synthful.xml.pjx.PjxParser;

/**
 * @author Blessed Geek
 */
public class StateTurnPike implements StatusConstants {

    /**
     * Open.
     * 
     * @return true, if Open successful
     */
    public boolean open() {
        TransitionQueue.clear();
        boolean active = Active;
        Active = true;
        StartedMillis = System.currentTimeMillis();
        return active;
    }

    /**
     * Close.
     * 
     * @return true, if Close successful
     */
    public boolean close() {
        TransitionQueue.clear();
        boolean active = Active;
        Active = false;
        return active;
    }

    /**
     * Checks if is open.
     * 
     * @return true, if is open
     */
    public boolean isOpen() {
        return Active;
    }

    /**
     * Opened since.
     * 
     * @return Opened since as long
     */
    public long OpenedSince() {
        return StartedMillis;
    }

    /**
     * Update calendar with current time.
     */
    public void updateCalendarWithCurrentTime() {
        Calendar.setTimeInMillis(System.currentTimeMillis());
    }

    /**
     * Status is active.
     * 
     * @return true, if Status is active successful
     */
    public boolean StatusIsActive() {
        return Status == ACTIVE;
    }

    /**
     * Status is paused.
     * 
     * @return true, if Status is paused successful
     */
    public boolean StatusIsPaused() {
        return Status == PAUSED;
    }

    /**
     * Status.
     * 
     * @return Status as String
     */
    public String status() {
        switch(Status) {
            case ACTIVE:
                return "Active";
            case INACTIVE:
                return "Inactive";
            case PAUSED:
                return "Paused";
        }
        return "Inactive";
    }

    /**
     * Gets the CurrentState.
     * 
     * @return the CurrentState as State
     */
    public State getCurrentState() {
        if (CurrentState == null) CurrentState = RootState;
        return CurrentState;
    }

    /**
     * Sets current state and returns previous state.
     * 
     * @param state
     * @return
     */
    public State setCurrentState(State state) {
        LOG.info("setCurrentState:" + state);
        PreviousState = CurrentState;
        CurrentState = state;
        return PreviousState;
    }

    /**
     * Gets the PreviousState.
     * 
     * @return the PreviousState as State
     */
    public State getPreviousState() {
        if (PreviousState == null) PreviousState = RootState;
        return PreviousState;
    }

    /**
     * Since the messages are accumulated, need to get the latest for each
     * target. Using hash to set latest message for each target.
     * 
     * @param messages
     */
    public void cumulateMessages(Vector messages) {
        for (int i = 0; i < messages.size(); i++) {
            Object o = messages.get(i);
            if (o instanceof Message) {
                Message message = (Message) o;
                String target = message.getTarget();
                Messages.put(target, message);
            }
        }
    }

    /**
     * Gets the Messages.
     * 
     * @return the Messages as String
     */
    public String getMessages() {
        return getCumulatedMessages();
    }

    /**
     * Gets the CumulatedMessages.
     * 
     * @return the CumulatedMessages as String
     */
    public String getCumulatedMessages() {
        StringBuilder messagesbuf = new StringBuilder();
        Enumeration en = Messages.elements();
        while (en.hasMoreElements()) {
            Object o = en.nextElement();
            if (o instanceof Message) messagesbuf.append(PjxParser.resolveElement(((Message) o).getXmlElement(), true));
        }
        messagesbuf.insert(0, "<messages>").append("</messages>");
        return messagesbuf.toString();
    }

    /**
     * Gets the Dialogs.
     * 
     * @return the Dialogs as Vector
     */
    public Vector getDialogs() {
        return Dialogs;
    }

    /**
     * Sets the dialogs.
     * 
     * @param dialogs
     *            the Dialogs
     */
    public void setDialogs(Vector dialogs) {
        Dialogs.addAll(dialogs);
    }

    /**
     * Gets the FistParser.
     * 
     * @return the FistParser as FiStXParser
     */
    public FiStXParser getFistParser() {
        return FistParser;
    }

    /**
     * Gets the PjxParser.
     * 
     * @return the PjxParser as PjxParser
     */
    public PjxParser getPjxParser() {
        return PjxParser;
    }

    private static final Log LOG = LogFactory.getLog(StateTurnPike.class);

    /** Variable Active. */
    protected boolean Active;

    /** Variable Messages. */
    protected final Hashtable Messages = new Hashtable();

    /** Variable Dialogs. */
    protected final Vector Dialogs = new Vector();

    /** Variable FistParser. */
    protected final FiStXParser FistParser = new FiStXParser();

    /** Variable PjxParser. */
    protected final PjxParser PjxParser = new PjxParser();

    /** Variable StartedMillis. */
    long StartedMillis;

    /** Variable Status. */
    public char Status;

    /** Variable RootState. */
    public State RootState;

    /** Variable CurrentState. */
    public State CurrentState;

    /** Variable PreviousState. */
    public State PreviousState;

    /** Variable IOErrorState. */
    public State IOErrorState;

    /** Variable TransitionQueue. */
    public final Vector TransitionQueue = new Vector();

    /** Variable Calendar. */
    public final GregorianCalendar Calendar = new GregorianCalendar();

    /** Variable Datetime. */
    public Date Datetime;
}
