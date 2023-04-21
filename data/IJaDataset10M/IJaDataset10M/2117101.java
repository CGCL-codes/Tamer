package jmri.jmrix.jmriclient;

import jmri.implementation.AbstractSensor;
import jmri.Sensor;

/**
 * JMRIClient implementation of the Sensor interface.
 * <P>
 *
 * Description:		extend jmri.AbstractSensor for JMRIClient layouts
 * @author			Bob Jacobsen Copyright (C) 2001, 2008
 * @author			Paul Bender Copyright (C) 2010
 * @version			$Revision: 1.4 $
 */
public class JMRIClientSensor extends AbstractSensor implements JMRIClientListener {

    private int _number;

    private JMRIClientTrafficController tc = null;

    /**
	 * JMRIClient sensors use the sensor number on the remote host.
	 */
    public JMRIClientSensor(int number, JMRIClientSystemConnectionMemo memo) {
        super(memo.getSystemPrefix() + "s" + number);
        _number = number;
        tc = memo.getJMRIClientTrafficController();
        tc.addJMRIClientListener(this);
        requestUpdateFromLayout();
    }

    public int getNumber() {
        return _number;
    }

    public void setKnownState(int s) throws jmri.JmriException {
        if ((s & Sensor.ACTIVE) > 0) {
            if ((s & Sensor.INACTIVE) > 0) {
                log.error("Cannot command both ACTIVE and INACTIVE " + s);
                return;
            } else {
                sendMessage(true ^ getInverted());
            }
        } else {
            sendMessage(false ^ getInverted());
        }
        if (_knownState != s) {
            int oldState = _knownState;
            _knownState = s;
            firePropertyChange("KnownState", Integer.valueOf(oldState), Integer.valueOf(_knownState));
        }
    }

    public void requestUpdateFromLayout() {
        String text = "SENSOR " + getSystemName() + "\n";
        tc.sendJMRIClientMessage(new JMRIClientMessage(text), this);
    }

    protected void sendMessage(boolean active) {
        String text;
        if (active) text = "SENSOR " + getSystemName() + " ACTIVE\n"; else text = "SENSOR " + getSystemName() + " INACTIVE\n";
        tc.sendJMRIClientMessage(new JMRIClientMessage(text), this);
    }

    public void reply(JMRIClientReply m) {
        String message = m.toString();
        log.debug("Message Received: " + m);
        if (!message.contains(getSystemName())) return;
        if (m.toString().contains("INACTIVE")) setOwnState(!getInverted() ? jmri.Sensor.INACTIVE : jmri.Sensor.ACTIVE); else if (m.toString().contains("ACTIVE")) setOwnState(!getInverted() ? jmri.Sensor.ACTIVE : jmri.Sensor.INACTIVE); else setOwnState(jmri.Sensor.UNKNOWN);
    }

    public void message(JMRIClientMessage m) {
    }

    static org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(JMRIClientSensor.class.getName());
}
