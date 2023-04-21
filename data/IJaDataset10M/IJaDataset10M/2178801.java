package net.adrianromero.tpv.scale;

import gnu.io.*;
import java.io.*;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.ParseException;
import java.util.Locale;
import java.util.TooManyListenersException;
import net.adrianromero.tpv.forms.AppLocal;
import net.adrianromero.tpv.util.EmergencyExit;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;

public class ScaleKernRPB implements Scale, SerialPortEventListener {

    private CommPortIdentifier m_PortIdScale;

    private SerialPort m_CommPortScale;

    private String m_sPortScale;

    private OutputStream m_out;

    private InputStream m_in;

    private static final int SCALE_READY = 0;

    private static final int SCALE_READING = 1;

    private int m_iStatusScale;

    private String m_readout;

    private int m_newlines;

    /** Creates a new instance of ScaleComm
     * @param sPortPrinter
     * @throws ScaleException
     */
    public ScaleKernRPB(String sPortPrinter) throws ScaleException {
        m_sPortScale = sPortPrinter;
        m_out = null;
        m_in = null;
        open_port();
        m_iStatusScale = SCALE_READY;
    }

    @Override
    public double readPrecWeight() {
        synchronized (this) {
            m_readout = "";
            if (m_in == null) {
                return 0.0;
            }
            if (m_iStatusScale != SCALE_READY) {
                try {
                    wait(2000);
                } catch (InterruptedException e) {
                    Logger.getLogger(this.getClass().getName()).log(Level.WARN, "wait for scale ready interrupted" + e.getMessage());
                }
                if (m_iStatusScale != SCALE_READY) {
                    m_iStatusScale = SCALE_READY;
                }
            }
            write(new byte[] { 'P', 0x0d, 0x0a });
            m_newlines = 0;
            flush();
            try {
                wait(2000);
            } catch (InterruptedException e) {
                Logger.getLogger(this.getClass().getName()).log(Level.WARN, "wait for value interrupted" + e.getMessage());
            }
            if (m_iStatusScale == SCALE_READY && m_readout.length() > 130) {
                double dWeight = 0.0;
                DecimalFormat wformat = new DecimalFormat("#0.0#", new DecimalFormatSymbols(Locale.US));
                try {
                    dWeight = wformat.parse(m_readout.substring(125, 130)).doubleValue();
                } catch (ParseException ex) {
                    Logger.getLogger(this.getClass().getName()).log(Level.WARN, "Unexpected scale parse error: " + ex.getMessage());
                }
                return dWeight;
            } else {
                m_iStatusScale = SCALE_READY;
                return 0.0;
            }
        }
    }

    @Override
    public double readWeight() {
        return Math.rint(readPrecWeight() * 1000) / 1000;
    }

    /**
     * readWeight is currently designed to always return kilogram nonrespecting the
     * unit delivered by the scale
     * 
     * @return the unit delivered  by the scale
     */
    public boolean isKilogram() {
        return true;
    }

    /**
     * acts like pressing the tare button
     */
    public void tare() {
        write(new byte[] { 't' });
        flush();
    }

    private void flush() {
        try {
            m_out.flush();
        } catch (IOException e) {
            Logger.getLogger(this.getClass().getName()).log(Level.WARN, "Flush failed: " + e.getMessage());
        }
    }

    @Override
    public void close() {
        m_in = null;
        m_out = null;
        try {
            m_CommPortScale.close();
        } catch (Exception e) {
        }
    }

    private void open_port() throws ScaleException {
        try {
            CommPortIdentifier.getPortIdentifiers();
            m_PortIdScale = CommPortIdentifier.getPortIdentifier(m_sPortScale);
            m_CommPortScale = (SerialPort) m_PortIdScale.open("POSPER_SCALEKERN", 1000);
            m_CommPortScale.addEventListener(this);
            m_CommPortScale.notifyOnDataAvailable(true);
            m_CommPortScale.setSerialPortParams(9600, SerialPort.DATABITS_8, SerialPort.STOPBITS_1, SerialPort.PARITY_NONE);
            m_CommPortScale.setFlowControlMode(SerialPort.FLOWCONTROL_NONE);
            m_in = m_CommPortScale.getInputStream();
            m_out = m_CommPortScale.getOutputStream();
        } catch (UnsupportedCommOperationException ex) {
            Logger.getLogger(this.getClass().getName()).log(Level.WARN, "Unhandled Exception: " + ex.getMessage());
            ex.printStackTrace();
        } catch (TooManyListenersException ex) {
            Logger.getLogger(this.getClass().getName()).log(Level.WARN, "Too many Listeners for serial port: " + m_sPortScale);
        } catch (IOException ex) {
            Logger.getLogger(this.getClass().getName()).log(Level.WARN, "Unhandled Exception: " + ex.getMessage());
            ex.printStackTrace();
        } catch (PortInUseException ex) {
            Logger.getLogger(this.getClass().getName()).log(Level.WARN, "Serial port in use: " + m_sPortScale);
        } catch (NoSuchPortException ex) {
            if (m_sPortScale != null) {
                Logger.getLogger(this.getClass().getName()).log(Level.WARN, "Serial port not found: " + m_sPortScale);
            }
        } finally {
            if (m_in == null) {
                throw new ScaleException("KernSeries scale could not open port");
            }
        }
    }

    private void write(byte[] data) {
        try {
            if (m_out == null) {
                open_port();
                m_out.write(data);
            }
        } catch (IOException e) {
            Logger.getLogger(this.getClass().getName()).log(Level.WARN, "Scale write failed: " + e.getMessage());
            e.printStackTrace();
        } catch (ScaleException ex) {
            Logger.getLogger(this.getClass().getName()).log(Level.WARN, "Open port for scale failed: " + ex.getMessage());
        }
    }

    @Override
    public void serialEvent(SerialPortEvent evt) {
        switch(evt.getEventType()) {
            case SerialPortEvent.BI:
            case SerialPortEvent.OE:
            case SerialPortEvent.FE:
            case SerialPortEvent.PE:
            case SerialPortEvent.CD:
            case SerialPortEvent.CTS:
            case SerialPortEvent.DSR:
            case SerialPortEvent.RI:
            case SerialPortEvent.OUTPUT_BUFFER_EMPTY:
                break;
            case SerialPortEvent.DATA_AVAILABLE:
                try {
                    while (m_in.available() > 0) {
                        int b = m_in.read();
                        m_iStatusScale = SCALE_READING;
                        m_readout += (char) b;
                        if (b == 0x0a) {
                            m_newlines += 1;
                        }
                        if (m_newlines == 10) {
                            synchronized (this) {
                                m_iStatusScale = SCALE_READY;
                                notifyAll();
                            }
                        }
                    }
                } catch (IOException e) {
                    Logger.getLogger(this.getClass().getName()).log(Level.WARN, "Unhandled Exception: " + e.getMessage());
                    if (e.getMessage().equals("No error in nativeavailable")) {
                        EmergencyExit.exit(AppLocal.getInstance().getIntString("message.usbscaleemergency"));
                    }
                }
                break;
        }
    }
}
