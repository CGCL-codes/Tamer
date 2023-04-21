package org.openremote.controller.protocol.lutron.model;

import org.apache.log4j.Logger;
import org.openremote.controller.protocol.lutron.LutronHomeWorksAddress;
import org.openremote.controller.protocol.lutron.LutronHomeWorksCommandBuilder;
import org.openremote.controller.protocol.lutron.LutronHomeWorksGateway;

/**
 * Represents a dimmer device on the Lutron bus.
 * 
 * @author <a href="mailto:eric@openremote.org">Eric Bariaux</a>
 */
public class Dimmer extends HomeWorksDevice {

    /**
   * Lutron logger. Uses a common category for all Lutron related logging.
   */
    private static final Logger log = Logger.getLogger(LutronHomeWorksCommandBuilder.LUTRON_LOG_CATEGORY);

    /**
	 * Current level, as reported by the system. Null if we don't have this info.
	 */
    private Integer level;

    public Dimmer(LutronHomeWorksGateway gateway, LutronHomeWorksAddress address) {
        super(gateway, address);
    }

    /**
	 * Starts raising the value of the dimmer
	 */
    public void raise() {
        this.gateway.sendCommand("RAISEDIM", address, null);
    }

    /**
   * Starts lowering the value of the dimmer
   */
    public void lower() {
        this.gateway.sendCommand("LOWERDIM", address, null);
    }

    /**
   * Stops raising or lowering the value of the dimmer
   */
    public void stop() {
        this.gateway.sendCommand("STOPDIM", address, null);
    }

    /**
	 * Immediately sets the value of the dimmer to the given value
	 * 
	 * @param level Level to set the dimmer to, expressed in %
	 */
    public void fade(Integer level) {
        this.gateway.sendCommand("FADEDIM, " + level + ", 1, 0", address, null);
    }

    /**
	 * Requests level of dimmer from Lutron processor.
	 */
    public void queryLevel() {
        this.gateway.sendCommand("RDL", address, null);
    }

    @Override
    public void processUpdate(String info) {
        try {
            level = (int) Float.parseFloat(info);
        } catch (NumberFormatException e) {
            log.warn("Invalid feedback received " + info, e);
        }
        super.processUpdate(info);
    }

    /**
   * Returns the currently known level of the dimmer
   * @return current level
   */
    public Integer getLevel() {
        return level;
    }
}
