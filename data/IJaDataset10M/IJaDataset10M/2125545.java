package net.sf.traser.identification;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import net.sf.traser.configuration.ConfigurationException;

/**
 *
 * @author Marcell Szathmári
 */
public class RFIDReaderFeigWrapper extends AbstractIdentificator {

    /**
     * The real reader that is wrapped.
     */
    private RFIDReaderFeig reader;

    /** The name of hte property specifying the serial port to use. */
    private static final String PORT = "port";

    /** The name of hte property specifying the poll frequency. */
    private static final String POLLFREQ = "resolution";

    /** The polling thread. */
    private Thread process;

    /** The runnable task the process executes. */
    private Runnable task;

    /** Flag for exiting the poll cycle. */
    private Boolean finish;

    /** The interval, the wrapper polls the reader in miliseconds. */
    private int pollFreq = 500;

    {
        task = new Runnable() {

            public void run() {
                Thread.currentThread().setPriority(1);
                while (!finish) {
                    try {
                        if (cnt > 0 && reader != null) {
                            try {
                                RFIDEvent rfidEvent = (RFIDEvent) reader.doInventory().get(0);
                                if (rfidEvent.status == RFIDEvent.RFID_SPOTTED) {
                                    notifyListeners(reader.doReadID_URI(rfidEvent.uid));
                                }
                            } catch (RFIDReaderException ex) {
                                if (!process.isInterrupted()) {
                                    Logger.getLogger(RFIDReaderFeigWrapper.class.getName()).log(Level.SEVERE, "An error occured during reading.", ex);
                                }
                            }
                        }
                        Thread.sleep(pollFreq);
                    } catch (InterruptedException ex) {
                        System.out.println("Process: " + this.toString() + " interrupted");
                    }
                }
                System.out.println("Reader process finished.");
            }
        };
    }

    @Override
    public void configure() {
        super.configure();
        updatePollFreq();
        updateReader();
    }

    @Override
    public void finish() {
        System.out.println("Shutting down reader!");
        stop();
        reader.closeSerialPort();
    }

    @Override
    public void start() {
        super.start();
        startReader();
    }

    @Override
    public void stop() {
        super.stop();
        stopReader();
    }

    private void startReader() {
        if (started) {
            finish = false;
            process = new Thread(task);
            process.start();
        }
    }

    private void stopReader() {
        finish = true;
        if (process != null) {
            try {
                process.interrupt();
                process.join();
            } catch (InterruptedException ex) {
                Logger.getLogger(RFIDReaderFeigWrapper.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    /** */
    private int cnt = 0;

    @Override
    protected void register() {
        if (cnt == 0) {
            startReader();
        }
        cnt++;
    }

    @Override
    protected void unRegister() {
        cnt--;
        if (cnt == 0) {
            stopReader();
        }
    }

    @Override
    protected void changed(String property) {
        if (POLLFREQ.equals(property)) {
            updatePollFreq();
        } else if (PORT.equals(property)) {
            stopReader();
            if (reader != null) {
                reader.closeSerialPort();
            }
            updateReader();
            startReader();
        }
    }

    /**
     * Updates the pollFreq.
     * @throws java.lang.NumberFormatException
     */
    private void updatePollFreq() throws NumberFormatException {
        String pFreq = get(POLLFREQ);
        if (pFreq != null && !"".equals(pFreq)) {
            pollFreq = Integer.parseInt(pFreq);
        }
    }

    /**
     * Updates the port of the reader.
     * @throws net.sf.traser.configuration.ConfigurationException
     */
    private void updateReader() throws ConfigurationException {
        if (get(PORT) == null) {
            throw new ConfigurationException("A <port> element must be specified " + "for the RFIDReaderFeig to work.");
        } else {
            try {
                reader = new RFIDReaderFeig(get(PORT));
            } catch (IOException ex) {
                throw new ConfigurationException("The rfid reader could not be initialized, see original exception for details.", ex);
            }
        }
    }

    public String[] getPropertyNames() {
        return new String[] { POLLFREQ, PORT };
    }
}
