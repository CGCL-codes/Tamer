package jmri.jmrix.lenz;

import jmri.implementation.AbstractLight;

/**
 * XNetLight.java
 *
 * Implementation of the Light Object for XPressNet
 * NOTE: This is a simplification of the XNetTurnout class.
 * <P>
 *  Based in part on SerialLight.java
 *
 * @author      Paul Bender Copyright (C) 2008-2010
 * @version     $Revision: 1.11 $
 */
public class XNetLight extends AbstractLight implements XNetListener {

    private XNetTrafficController tc = null;

    private XNetLightManager lm = null;

    /**
     * Create a Light object, with only system name.
     * <P>
     * 'systemName' was previously validated in LnLightManager
     */
    public XNetLight(XNetTrafficController tc, XNetLightManager lm, String systemName) {
        super(systemName);
        this.tc = tc;
        this.lm = lm;
        initializeLight(systemName);
    }

    /**
     * Create a Light object, with both system and user names.
     * <P>
     * 'systemName' was previously validated in XNetLightManager
     */
    public XNetLight(XNetTrafficController tc, XNetLightManager lm, String systemName, String userName) {
        super(systemName, userName);
        this.tc = tc;
        this.lm = lm;
        initializeLight(systemName);
    }

    public void dispose() {
        tc.removeXNetListener(XNetInterface.FEEDBACK | XNetInterface.COMMINFO | XNetInterface.CS_INFO, this);
        super.dispose();
    }

    private synchronized void initializeLight(String systemName) {
        mSystemName = systemName;
        mAddress = lm.getBitFromSystemName(systemName);
        setState(OFF);
        tc.addXNetListener(XNetInterface.FEEDBACK | XNetInterface.COMMINFO | XNetInterface.CS_INFO, this);
    }

    /**
     *  System dependent instance variables
     */
    String mSystemName = "";

    int mAddress = 0;

    static final int OFFSENT = 1;

    static final int COMMANDSENT = 2;

    static final int IDLE = 0;

    private int InternalState = IDLE;

    /**
     *  Return the current state of this Light
     */
    public synchronized int getState() {
        return mState;
    }

    /**
     *  Set the current state of this Light
     *     This routine requests the hardware to change.
     */
    public synchronized void setState(int newState) {
        if (newState != ON && newState != OFF) {
            log.warn("Unsupported state " + newState + " requested for light " + mSystemName);
            return;
        }
        XNetMessage msg = XNetMessage.getTurnoutCommandMsg(mAddress, newState == ON, newState == OFF, true);
        InternalState = COMMANDSENT;
        tc.sendXNetMessage(msg, this);
        if (newState != mState) {
            int oldState = mState;
            mState = newState;
            firePropertyChange("KnownState", Integer.valueOf(oldState), Integer.valueOf(newState));
        }
        sendOffMessage();
    }

    public synchronized void message(XNetReply l) {
        if (log.isDebugEnabled()) log.debug("recieved message: " + l);
        if (InternalState == OFFSENT) {
            if (l.isCommErrorMessage()) {
                log.error("Communications error occured - message recieved was: " + l);
                sendOffMessage();
                return;
            } else if (l.isCSBusyMessage()) {
                log.error("Command station busy - message recieved was: " + l);
                sendOffMessage();
                return;
            } else if (l.isOkMessage()) {
                synchronized (this) {
                    InternalState = IDLE;
                }
                return;
            } else if (InternalState == COMMANDSENT) {
                if (l.isCommErrorMessage()) {
                    log.error("Communications error occured - message recieved was: " + l);
                    setState(mState);
                    return;
                } else if (l.isCSBusyMessage()) {
                    log.error("Command station busy - message recieved was: " + l);
                    setState(mState);
                    return;
                } else if (l.isOkMessage()) {
                    sendOffMessage();
                }
                return;
            }
        }
    }

    public void message(XNetMessage l) {
    }

    public void notifyTimeout(XNetMessage msg) {
        if (log.isDebugEnabled()) log.debug("Notified of timeout on message" + msg.toString());
    }

    private synchronized void sendOffMessage() {
        if (log.isDebugEnabled()) log.debug("Sending off message for light " + mAddress + " commanded state= " + mState);
        XNetMessage msg = XNetMessage.getTurnoutCommandMsg(mAddress, mState == ON, mState == OFF, false);
        tc.sendXNetMessage(msg, this);
        synchronized (this) {
            InternalState = OFFSENT;
        }
    }

    static org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(XNetLight.class.getName());
}
