package jmri.jmrix.secsi;

import jmri.implementation.AbstractTurnout;
import jmri.Turnout;

/**
 * SerialTurnout.java
 *
 *  This object doesn't listen to the SECSI serial communications.  This is because
 *  it should be the only object that is sending messages for this turnout;
 *  more than one Turnout object pointing to a single device is not allowed.
 *
 * Description:		extend jmri.AbstractTurnout for SECSI serial layouts
 * @author			Bob Jacobsen Copyright (C) 2003, 2006, 2007
 * @version			$Revision: 1.4 $
 */
public class SerialTurnout extends AbstractTurnout {

    /**
     * Create a Turnout object, with both system and user names.
     * <P>
     * 'systemName' was previously validated in SerialTurnoutManager
     */
    public SerialTurnout(String systemName, String userName) {
        super(systemName, userName);
        tSystemName = systemName;
        tBit = SerialAddress.getBitFromSystemName(systemName);
    }

    /**
     * Handle a request to change state by sending a turnout command
     */
    protected void forwardCommandChangeToLayout(int s) {
        if ((s & Turnout.CLOSED) > 0) {
            if ((s & Turnout.THROWN) > 0) {
                log.error("Cannot command both CLOSED and THROWN " + s);
                return;
            } else {
                sendMessage(true ^ getInverted());
            }
        } else {
            sendMessage(false ^ getInverted());
        }
    }

    protected void turnoutPushbuttonLockout(boolean _pushButtonLockout) {
        if (log.isDebugEnabled()) log.debug("Send command to " + (_pushButtonLockout ? "Lock" : "Unlock") + " Pushbutton");
    }

    public void dispose() {
        super.dispose();
    }

    String tSystemName;

    int tBit;

    protected void sendMessage(boolean closed) {
        SerialNode tNode = SerialAddress.getNodeFromSystemName(tSystemName);
        if (tNode == null) {
            return;
        }
        tNode.setOutputBit(tBit, closed);
    }

    static org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(SerialTurnout.class.getName());
}
