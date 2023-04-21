package jmri.jmrix.lenz;

import jmri.Sensor;

/**
 * Manage the XPressNet specific Sensor implementation.
 *
 * System names are "XSnnn", where nnn is the sensor number without padding.
 *
 * @author			Paul Bender Copyright (C) 2003-2010
 * @version			$Revision: 2.17 $
 */
public class XNetSensorManager extends jmri.managers.AbstractSensorManager implements XNetListener {

    public String getSystemPrefix() {
        return prefix;
    }

    protected String prefix = null;

    protected XNetTrafficController tc = null;

    @Deprecated
    public static XNetSensorManager instance() {
        return mInstance;
    }

    private static XNetSensorManager mInstance = null;

    public void dispose() {
        tc.removeXNetListener(XNetInterface.FEEDBACK, this);
        super.dispose();
    }

    public Sensor createNewSensor(String systemName, String userName) {
        return new XNetSensor(systemName, userName, tc);
    }

    public XNetSensorManager(XNetTrafficController controller, String prefix) {
        tc = controller;
        tc.addXNetListener(XNetInterface.FEEDBACK, this);
        this.prefix = prefix;
    }

    public void message(XNetReply l) {
        if (log.isDebugEnabled()) log.debug("recieved message: " + l);
        if (l.isFeedbackBroadcastMessage()) {
            int numDataBytes = l.getElement(0) & 0x0f;
            for (int i = 1; i < numDataBytes; i += 2) {
                if (l.getFeedbackMessageType(i) == 2) {
                    int address = l.getFeedbackEncoderMsgAddr(i);
                    if (log.isDebugEnabled()) log.debug("Message for feedback encoder " + address);
                    int firstaddress = ((address) * 8) + 1;
                    for (int j = 0; j < 8; j++) {
                        String s = prefix + typeLetter() + (firstaddress + j);
                        if (null == getBySystemName(s)) {
                            ((XNetSensor) provideSensor(s)).initmessage(l);
                        } else {
                            ((XNetSensor) getBySystemName(s)).message(l);
                        }
                    }
                }
            }
        }
    }

    public void message(XNetMessage l) {
    }

    public void notifyTimeout(XNetMessage msg) {
        if (log.isDebugEnabled()) log.debug("Notified of timeout on message" + msg.toString());
    }

    public boolean allowMultipleAdditions(String systemName) {
        return true;
    }

    /**
     * Does not enforce any rules on the encoder or input values.
    **/
    public String getNextValidAddress(String curAddress, String prefix) {
        int encoderAddress = 0;
        int seperator = 0;
        int input = 0;
        int iName;
        if (curAddress.contains(":")) {
            seperator = curAddress.indexOf(":");
            try {
                encoderAddress = Integer.valueOf(curAddress.substring(0, seperator)).intValue();
                input = Integer.valueOf(curAddress.substring(seperator + 1)).intValue();
            } catch (NumberFormatException ex) {
                log.error("Unable to convert " + curAddress + " into the cab and input format of nn:xx");
                jmri.InstanceManager.getDefault(jmri.UserPreferencesManager.class).showInfoMessage("Error", "Unable to convert " + curAddress + " to a valid Hardware Address of nn:xx", "" + ex, "", true, false, org.apache.log4j.Level.ERROR);
                return null;
            }
            iName = ((encoderAddress - 1) * 8) + input;
        } else {
            try {
                iName = Integer.parseInt(curAddress);
            } catch (NumberFormatException ex) {
                log.error("Unable to convert " + curAddress + " Hardware Address to a number");
                jmri.InstanceManager.getDefault(jmri.UserPreferencesManager.class).showInfoMessage("Error", "Unable to convert " + curAddress + " to a valid Hardware Address", "" + ex, "", true, false, org.apache.log4j.Level.ERROR);
                return null;
            }
        }
        Sensor s = getBySystemName(prefix + typeLetter() + iName);
        if (s != null) {
            for (int x = 1; x < 10; x++) {
                iName = iName + 1;
                s = getBySystemName(prefix + typeLetter() + iName);
                if (s == null) {
                    return Integer.toString(iName);
                }
            }
            return null;
        } else {
            return Integer.toString(iName);
        }
    }

    static org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(XNetSensorManager.class.getName());
}
