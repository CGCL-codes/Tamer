package jmri.jmrix.powerline.simulator;

import jmri.jmrix.powerline.SerialSystemConnectionMemo;
import jmri.jmrix.powerline.SerialPortController;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.PipedInputStream;
import java.io.PipedOutputStream;

/**
 * Implement simulator for powerline serial systems
 * <P>
 * System names are "PLnnn", where nnn is the bit number without padding.
 *
 * This is based on the NCE simulator.
 * 
 * @author	Dave Duchamp Copyright (C) 2004
 * @author	Bob Jacobsen Copyright (C) 2006, 2007, 2008
 * Converted to multiple connection
 * @author kcameron Copyright (C) 2011
 * @version	$Revision: 1.1 $
 */
public class SimulatorAdapter extends SerialPortController implements jmri.jmrix.SerialPortAdapter, Runnable {

    private boolean opened = false;

    private Thread sourceThread;

    private DataOutputStream pout = null;

    private DataInputStream pin = null;

    @SuppressWarnings("unused")
    private DataOutputStream outpipe = null;

    @SuppressWarnings("unused")
    private DataInputStream inpipe = null;

    public SimulatorAdapter() {
        super();
        adaptermemo = new SpecificSystemConnectionMemo();
    }

    @Override
    public SerialSystemConnectionMemo getSystemConnectionMemo() {
        return adaptermemo;
    }

    public void dispose() {
        if (adaptermemo != null) adaptermemo.dispose();
        adaptermemo = null;
    }

    public String openPort(String portName, String appName) {
        try {
            PipedOutputStream tempPipeI = new PipedOutputStream();
            pout = new DataOutputStream(tempPipeI);
            inpipe = new DataInputStream(new PipedInputStream(tempPipeI));
            PipedOutputStream tempPipeO = new PipedOutputStream();
            outpipe = new DataOutputStream(tempPipeO);
            pin = new DataInputStream(new PipedInputStream(tempPipeO));
        } catch (java.io.IOException e) {
            log.error("init (pipe): Exception: " + e.toString());
        }
        opened = true;
        return null;
    }

    /**
	 * set up all of the other objects to simulate operation with an command
	 * station.
	 */
    public void configure() {
        SpecificTrafficController tc = new SpecificTrafficController(adaptermemo);
        adaptermemo.setTrafficController(tc);
        tc.setAdapterMemo(adaptermemo);
        adaptermemo.configureManagers();
        tc.connectPort(this);
        adaptermemo.setSerialAddress(new jmri.jmrix.powerline.SerialAddress(adaptermemo));
        jmri.jmrix.powerline.ActiveFlag.setActive();
        sourceThread = new Thread(this);
        sourceThread.setName("Powerline Simulator");
        sourceThread.setPriority(Thread.MIN_PRIORITY);
        sourceThread.start();
    }

    public DataInputStream getInputStream() {
        if (!opened || pin == null) {
            log.error("getInputStream called before load(), stream not available");
        }
        return pin;
    }

    public DataOutputStream getOutputStream() {
        if (!opened || pout == null) {
            log.error("getOutputStream called before load(), stream not available");
        }
        return pout;
    }

    public boolean status() {
        return opened;
    }

    /**
	 * Get an array of valid baud rates.
	 */
    public String[] validBaudRates() {
        log.debug("validBaudRates should not have been invoked");
        return null;
    }

    public String getCurrentBaudRate() {
        return "";
    }

    public void run() {
        if (log.isInfoEnabled()) log.info("Powerline Simulator Started");
        while (true) {
            try {
                wait(100);
            } catch (Exception e) {
            }
        }
    }

    static org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(SimulatorAdapter.class.getName());
}
