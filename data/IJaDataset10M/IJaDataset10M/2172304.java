package org.openremote.controller.protocol.lutron;

import org.apache.log4j.Logger;

/**
 * 
 * @author <a href="mailto:eric@openremote.org">Eric Bariaux</a>
 *
 */
public class Keypad extends HomeWorksDevice {

    /**
   * Lutron logger. Uses a common category for all Lutron related logging.
   */
    private static final Logger log = Logger.getLogger(LutronHomeWorksCommandBuilder.LUTRON_LOG_CATEGORY);

    /**
   * Current status of key LEDs, as reported by the system. Null if we don't have this info.
   */
    private Integer[] ledStatuses;

    public Keypad(LutronHomeWorksGateway gateway, LutronHomeWorksAddress address) {
        super(gateway, address);
    }

    public void press(Integer key) {
        this.gateway.sendCommand("KBP", address, Integer.toString(key));
    }

    public void release(Integer key) {
        this.gateway.sendCommand("KBR", address, Integer.toString(key));
    }

    public void hold(Integer key) {
        this.gateway.sendCommand("KBH", address, Integer.toString(key));
    }

    public void doubleTap(Integer key) {
        this.gateway.sendCommand("KBDT", address, Integer.toString(key));
    }

    public void queryLedStatus() {
        this.gateway.sendCommand("RKLS", address, null);
    }

    @Override
    public void processUpdate(String info) {
        log.info("Will update keypad (" + address + ") status with string " + info);
        if (!info.matches("^[0-3]{24}$")) {
            log.warn("Invalid feedback received " + info);
        }
        if (ledStatuses == null) {
            ledStatuses = new Integer[24];
        }
        for (int i = 0; i < info.length(); i++) {
            try {
                ledStatuses[i] = Integer.parseInt(info.substring(i, i + 1));
            } catch (NumberFormatException e) {
                log.warn("Invalid feedback received " + info + ", skipping to next LED", e);
            }
        }
    }

    public Integer[] getLedStatuses() {
        return ledStatuses;
    }
}
