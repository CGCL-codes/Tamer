package jmri.jmrix.can.cbus;

import jmri.implementation.AbstractSensor;
import jmri.Sensor;
import jmri.jmrix.can.CanListener;
import jmri.jmrix.can.CanMessage;
import jmri.jmrix.can.CanReply;
import jmri.jmrix.can.TrafficController;

/**
 * Extend jmri.AbstractSensor for CBUS controls.
 * <P>
 * @author			Bob Jacobsen Copyright (C) 2008
 * @version         $Revision: 1.6 $
 */
public class CbusSensor extends AbstractSensor implements CanListener {

    CbusAddress addrActive;

    CbusAddress addrInactive;

    public CbusSensor(String systemName, String userName) {
        super(systemName, userName);
        init(systemName);
    }

    public CbusSensor(String systemName) {
        super(systemName);
        init(systemName);
    }

    /**
     * Common initialization for both constructors.
     * <p>
     * 
     */
    private void init(String systemName) {
        CbusAddress a = new CbusAddress(systemName.substring(2, systemName.length()));
        CbusAddress[] v = a.split();
        if (v == null) {
            log.error("Did not find usable system name: " + systemName);
            return;
        }
        switch(v.length) {
            case 1:
                addrActive = v[0];
                if (systemName.substring(2, 3).equals("+")) {
                    addrInactive = new CbusAddress("-" + systemName.substring(3, systemName.length()));
                } else if (systemName.substring(2, 3).equals("-")) {
                    addrInactive = new CbusAddress("+" + systemName.substring(3, systemName.length()));
                } else {
                    log.error("can't make 2nd event from systemname " + systemName);
                    return;
                }
                break;
            case 2:
                addrActive = v[0];
                addrInactive = v[1];
                break;
            default:
                log.error("Can't parse CbusSensor system name: " + systemName);
                return;
        }
        TrafficController.instance().addCanListener(this);
    }

    /**
     * Request an update on status by sending CBUS message.
     * <p>
     * There is no known way to do this, so the request is
     * just ignored.
     */
    public void requestUpdateFromLayout() {
    }

    /**
     * User request to set the state, which means that we broadcast that to
     * all listeners by putting it out on CBUS.
     * In turn, the code in this class should use setOwnState to handle
     * internal sets and bean notifies.
     * @param s
     * @throws JmriException
     */
    public void setKnownState(int s) throws jmri.JmriException {
        CanMessage m;
        if (s == Sensor.ACTIVE) {
            m = addrActive.makeMessage();
            TrafficController.instance().sendCanMessage(m, this);
            setOwnState(Sensor.ACTIVE);
        } else if (s == Sensor.INACTIVE) {
            m = addrInactive.makeMessage();
            TrafficController.instance().sendCanMessage(m, this);
            setOwnState(Sensor.INACTIVE);
        }
    }

    /**
     * Track layout status from messages being sent to CAN
     * @param f
     */
    public void message(CanMessage f) {
        if (addrActive.match(f)) {
            setOwnState(Sensor.ACTIVE);
        } else if (addrInactive.match(f)) {
            setOwnState(Sensor.INACTIVE);
        }
    }

    /**
     * Track layout status from messages being received from CAN
     * @param f
     */
    public void reply(CanReply f) {
        if (addrActive.match(f)) {
            setOwnState(Sensor.ACTIVE);
        } else if (addrInactive.match(f)) {
            setOwnState(Sensor.INACTIVE);
        }
    }

    public void dispose() {
        TrafficController.instance().removeCanListener(this);
        super.dispose();
    }

    static org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(CbusSensor.class.getName());
}
