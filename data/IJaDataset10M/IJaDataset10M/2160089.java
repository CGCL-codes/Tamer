package jmri.jmrix.ncemonitor;

import gnu.io.CommPortIdentifier;
import gnu.io.PortInUseException;
import gnu.io.SerialPort;
import javax.swing.*;
import java.awt.*;
import java.util.Enumeration;
import java.util.ResourceBundle;
import java.util.Vector;
import java.io.IOException;
import java.io.OutputStream;
import java.io.DataInputStream;
import jmri.jmrix.nce.NceSystemConnectionMemo;
import jmri.jmrix.nce.swing.NcePanelInterface;

/**
 * Simple GUI for access to an NCE monitor card
 * <P>
 * When opened, the user must first select a serial port and click "Start".
 * The rest of the GUI then appears.
 *
 * @author			Ken Cameron Copyright (C) 2010
 * derived from - 
 * @author			Bob Jacobsen   Copyright (C) 2001, 2002
 * @version			$Revision: 1.2 $
 */
@edu.umd.cs.findbugs.annotations.SuppressWarnings(value = "IS2_INCONSISTENT_SYNC", justification = "serialStream is access from separate thread, and this class isn't used much")
public class NcePacketMonitorPanel extends jmri.jmrix.AbstractMonPane implements NcePanelInterface {

    ResourceBundle rb = ResourceBundle.getBundle("jmri.jmrix.ncemonitor.NcePacketMonitorBundle");

    Vector<String> portNameVector = null;

    SerialPort activeSerialPort = null;

    NceSystemConnectionMemo memo = null;

    JButton checkButton = new JButton("Init");

    JRadioButton locoSpeedButton = new JRadioButton("Hide loco packets");

    JCheckBox truncateCheckBox = new JCheckBox("+ on");

    public NcePacketMonitorPanel() {
        super();
    }

    public void init() {
    }

    public void initContext(Object context) throws Exception {
        if (context instanceof NceSystemConnectionMemo) {
            try {
                initComponents((NceSystemConnectionMemo) context);
            } catch (Exception e) {
            }
        }
    }

    public String getHelpTarget() {
        return "package.jmri.jmrix.nce.analyzer.NcePacketMonitorFrame";
    }

    public String getTitle() {
        StringBuilder x = new StringBuilder();
        if (memo != null) {
            x.append(memo.getUserName());
        } else {
            x.append("NCE_");
        }
        x.append(": ");
        x.append(rb.getString("Title"));
        return x.toString();
    }

    public void initComponents(NceSystemConnectionMemo m) throws Exception {
        this.memo = m;
        portBox.setToolTipText("Select the port to use");
        portBox.setAlignmentX(JLabel.LEFT_ALIGNMENT);
        Vector<String> v = getPortNames();
        for (int i = 0; i < v.size(); i++) portBox.addItem(v.elementAt(i));
        openPortButton.setText("Open");
        openPortButton.setToolTipText("Configure program to use selected port");
        openPortButton.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                try {
                    openPortButtonActionPerformed(evt);
                } catch (java.lang.UnsatisfiedLinkError ex) {
                    log.error("Error while opening port.  Did you select the right one?\n" + ex);
                }
            }
        });
        add(new JSeparator());
        JPanel p1 = new JPanel();
        p1.setLayout(new FlowLayout());
        p1.add(new JLabel("Serial port: "));
        p1.add(portBox);
        p1.add(openPortButton);
        add(p1);
        add(new JSeparator());
        JPanel p2 = new JPanel();
        {
            JPanel p = new JPanel();
            p.setLayout(new BoxLayout(p, BoxLayout.Y_AXIS));
            checkButton.setToolTipText("?");
            checkButton.setEnabled(false);
            p.add(checkButton);
            checkButton.addActionListener(new java.awt.event.ActionListener() {

                public void actionPerformed(java.awt.event.ActionEvent evt) {
                    sendBytes(new byte[] { (byte) '?' });
                }
            });
            truncateCheckBox.setToolTipText("Check this box to suppress identical packets");
            p.add(truncateCheckBox);
            p2.add(p);
        }
        {
            JPanel p = new JPanel();
            p.setLayout(new BoxLayout(p, BoxLayout.Y_AXIS));
            ButtonGroup g = new ButtonGroup();
            JRadioButton b;
            b = new JRadioButton("Verbose");
            b.setToolTipText("V");
            g.add(b);
            p.add(b);
            b.addActionListener(new java.awt.event.ActionListener() {

                public void actionPerformed(java.awt.event.ActionEvent evt) {
                    sendBytes(new byte[] { (byte) 'V' });
                }
            });
            b = new JRadioButton("Hex with preamble symbol");
            b.setToolTipText("H0");
            g.add(b);
            p.add(b);
            b.addActionListener(new java.awt.event.ActionListener() {

                public void actionPerformed(java.awt.event.ActionEvent evt) {
                    sendBytes(new byte[] { (byte) 'H', (byte) '0' });
                }
            });
            p2.add(p);
            b = new JRadioButton("Hex without preamble symbol");
            b.setToolTipText("H2");
            g.add(b);
            p.add(b);
            b.addActionListener(new java.awt.event.ActionListener() {

                public void actionPerformed(java.awt.event.ActionEvent evt) {
                    sendBytes(new byte[] { (byte) 'H', (byte) '2' });
                }
            });
            p2.add(p);
            b = new JRadioButton("Hex with preamble in hex");
            b.setToolTipText("H4");
            g.add(b);
            p.add(b);
            b.addActionListener(new java.awt.event.ActionListener() {

                public void actionPerformed(java.awt.event.ActionEvent evt) {
                    sendBytes(new byte[] { (byte) 'H', (byte) '4' });
                }
            });
            p2.add(p);
        }
        {
            JPanel p = new JPanel();
            p.setLayout(new BoxLayout(p, BoxLayout.Y_AXIS));
            ButtonGroup g = new ButtonGroup();
            JRadioButton b;
            b = new JRadioButton("Hide acc packets");
            b.setToolTipText("A-");
            g.add(b);
            p.add(b);
            b.addActionListener(new java.awt.event.ActionListener() {

                public void actionPerformed(java.awt.event.ActionEvent evt) {
                    sendBytes(new byte[] { (byte) 'A', (byte) '-' });
                }
            });
            b = new JRadioButton("Show acc packets");
            b.setToolTipText("A+");
            g.add(b);
            p.add(b);
            b.addActionListener(new java.awt.event.ActionListener() {

                public void actionPerformed(java.awt.event.ActionEvent evt) {
                    sendBytes(new byte[] { (byte) 'A', (byte) '+' });
                }
            });
            p2.add(p);
        }
        {
            JPanel p = new JPanel();
            p.setLayout(new BoxLayout(p, BoxLayout.Y_AXIS));
            ButtonGroup g = new ButtonGroup();
            JRadioButton b;
            b = new JRadioButton("Hide idle packets");
            b.setToolTipText("I-");
            g.add(b);
            p.add(b);
            b.addActionListener(new java.awt.event.ActionListener() {

                public void actionPerformed(java.awt.event.ActionEvent evt) {
                    sendBytes(new byte[] { (byte) 'I', (byte) '-' });
                }
            });
            b = new JRadioButton("Show idle packets");
            b.setToolTipText("I+");
            g.add(b);
            p.add(b);
            b.addActionListener(new java.awt.event.ActionListener() {

                public void actionPerformed(java.awt.event.ActionEvent evt) {
                    sendBytes(new byte[] { (byte) 'I', (byte) '+' });
                }
            });
            p2.add(p);
        }
        {
            JPanel p = new JPanel();
            p.setLayout(new BoxLayout(p, BoxLayout.Y_AXIS));
            ButtonGroup g = new ButtonGroup();
            JRadioButton b;
            locoSpeedButton.setToolTipText("L-");
            g.add(locoSpeedButton);
            p.add(locoSpeedButton);
            locoSpeedButton.addActionListener(new java.awt.event.ActionListener() {

                public void actionPerformed(java.awt.event.ActionEvent evt) {
                    sendBytes(new byte[] { (byte) 'L', (byte) '-' });
                }
            });
            b = new JRadioButton("Show loco packets");
            b.setToolTipText("L+");
            g.add(b);
            p.add(b);
            b.addActionListener(new java.awt.event.ActionListener() {

                public void actionPerformed(java.awt.event.ActionEvent evt) {
                    sendBytes(new byte[] { (byte) 'L', (byte) '+' });
                }
            });
            p2.add(p);
        }
        {
            JPanel p = new JPanel();
            p.setLayout(new BoxLayout(p, BoxLayout.Y_AXIS));
            ButtonGroup g = new ButtonGroup();
            JRadioButton b;
            b = new JRadioButton("Hide reset packets");
            b.setToolTipText("R-");
            g.add(b);
            p.add(b);
            b.addActionListener(new java.awt.event.ActionListener() {

                public void actionPerformed(java.awt.event.ActionEvent evt) {
                    sendBytes(new byte[] { (byte) 'R', (byte) '-' });
                }
            });
            b = new JRadioButton("Show reset packets");
            b.setToolTipText("R+");
            g.add(b);
            p.add(b);
            b.addActionListener(new java.awt.event.ActionListener() {

                public void actionPerformed(java.awt.event.ActionEvent evt) {
                    sendBytes(new byte[] { (byte) 'R', (byte) '+' });
                }
            });
            p2.add(p);
        }
        {
            JPanel p = new JPanel();
            p.setLayout(new BoxLayout(p, BoxLayout.Y_AXIS));
            ButtonGroup g = new ButtonGroup();
            JRadioButton b;
            b = new JRadioButton("Hide signal packets");
            b.setToolTipText("S-");
            g.add(b);
            p.add(b);
            b.addActionListener(new java.awt.event.ActionListener() {

                public void actionPerformed(java.awt.event.ActionEvent evt) {
                    sendBytes(new byte[] { (byte) 'S', (byte) '-' });
                }
            });
            b = new JRadioButton("Show signal packets");
            b.setToolTipText("S+");
            g.add(b);
            p.add(b);
            b.addActionListener(new java.awt.event.ActionListener() {

                public void actionPerformed(java.awt.event.ActionEvent evt) {
                    sendBytes(new byte[] { (byte) 'S', (byte) '+' });
                }
            });
            p2.add(p);
        }
        {
            JPanel p = new JPanel();
            p.setLayout(new BoxLayout(p, BoxLayout.Y_AXIS));
            JLabel t = new JLabel("Monitor Command");
            p.add(t);
            ButtonGroup g = new ButtonGroup();
            JRadioButton b;
            b = new JRadioButton("Acc addresses single");
            b.setToolTipText("AS");
            g.add(b);
            p.add(b);
            b.addActionListener(new java.awt.event.ActionListener() {

                public void actionPerformed(java.awt.event.ActionEvent evt) {
                    sendBytes(new byte[] { (byte) 'A', (byte) 'S' });
                }
            });
            b = new JRadioButton("Acc addresses paired");
            b.setToolTipText("AP");
            g.add(b);
            p.add(b);
            b.addActionListener(new java.awt.event.ActionListener() {

                public void actionPerformed(java.awt.event.ActionEvent evt) {
                    sendBytes(new byte[] { (byte) 'A', (byte) 'P' });
                }
            });
            p2.add(p);
        }
        add(p2);
    }

    /**
     * Sends stream of bytes to the command station
     * @param bytes - array of bytes to send
     */
    synchronized void sendBytes(byte[] bytes) {
        try {
            if (ostream == null) {
                throw new IOException("Unable to send data to command station: output stream is null");
            } else {
                for (int i = 0; i < bytes.length; i++) {
                    ostream.write(bytes[i]);
                    wait(3);
                }
                final byte endbyte = 13;
                ostream.write(endbyte);
            }
        } catch (IOException e) {
            log.error("Exception on output: " + e);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            log.error("Interrupted output: " + e);
        }
    }

    /**
	 * Open button has been pushed, create the actual display connection
	 */
    void openPortButtonActionPerformed(java.awt.event.ActionEvent e) {
        log.info("Open button pushed");
        openPortButton.setEnabled(false);
        portBox.setEnabled(false);
        openPort((String) portBox.getSelectedItem(), "JMRI");
        readerThread = new Thread(new Reader());
        readerThread.start();
        checkButton.setEnabled(true);
        log.info("Open button processing complete");
    }

    Thread readerThread;

    protected javax.swing.JComboBox portBox = new javax.swing.JComboBox();

    protected javax.swing.JButton openPortButton = new javax.swing.JButton();

    @SuppressWarnings("deprecation")
    void stopThread(Thread t) {
        t.stop();
    }

    public synchronized void dispose() {
        if (readerThread != null) stopThread(readerThread);
        if (activeSerialPort != null) activeSerialPort.close();
        serialStream = null;
        ostream = null;
        activeSerialPort = null;
        portNameVector = null;
        super.dispose();
    }

    @SuppressWarnings("unchecked")
    public Vector<String> getPortNames() {
        portNameVector = new Vector<String>();
        Enumeration<CommPortIdentifier> portIDs = CommPortIdentifier.getPortIdentifiers();
        while (portIDs.hasMoreElements()) {
            CommPortIdentifier id = portIDs.nextElement();
            if (id.getPortType() != CommPortIdentifier.PORT_PARALLEL) portNameVector.addElement(id.getName());
        }
        return portNameVector;
    }

    public synchronized String openPort(String portName, String appName) {
        try {
            CommPortIdentifier portID = CommPortIdentifier.getPortIdentifier(portName);
            try {
                activeSerialPort = (SerialPort) portID.open(appName, 2000);
            } catch (PortInUseException p) {
                handlePortBusy(p, portName);
                return "Port " + p + " in use already";
            }
            try {
                activeSerialPort.setSerialPortParams(38400, SerialPort.DATABITS_8, SerialPort.STOPBITS_1, SerialPort.PARITY_NONE);
            } catch (gnu.io.UnsupportedCommOperationException e) {
                log.error("Cannot set serial parameters on port " + portName + ": " + e.getMessage());
                return "Cannot set serial parameters on port " + portName + ": " + e.getMessage();
            }
            activeSerialPort.setRTS(true);
            activeSerialPort.setDTR(true);
            activeSerialPort.setFlowControlMode(0);
            log.debug("Serial timeout was observed as: " + activeSerialPort.getReceiveTimeout() + " " + activeSerialPort.isReceiveTimeoutEnabled());
            serialStream = new DataInputStream(activeSerialPort.getInputStream());
            ostream = activeSerialPort.getOutputStream();
            sendBytes(new byte[] { (byte) 'L', (byte) '-', 10, 13 });
            int count = serialStream.available();
            log.debug("input stream shows " + count + " bytes available");
            while (count > 0) {
                serialStream.skip(count);
                count = serialStream.available();
            }
            if (log.isInfoEnabled()) {
                log.info(portName + " port opened at " + activeSerialPort.getBaudRate() + " baud, sees " + " DTR: " + activeSerialPort.isDTR() + " RTS: " + activeSerialPort.isRTS() + " DSR: " + activeSerialPort.isDSR() + " CTS: " + activeSerialPort.isCTS() + "  CD: " + activeSerialPort.isCD());
            }
        } catch (java.io.IOException ex) {
            log.error("IO error while opening port " + portName, ex);
            return "IO error while opening port " + portName + ": " + ex;
        } catch (gnu.io.UnsupportedCommOperationException ex) {
            log.error("Unsupported communications operation while opening port " + portName, ex);
            return "Unsupported communications operation while opening port " + portName + ": " + ex;
        } catch (gnu.io.NoSuchPortException ex) {
            log.error("No such port: " + portName, ex);
            return "No such port: " + portName + ": " + ex;
        }
        return null;
    }

    void handlePortBusy(gnu.io.PortInUseException p, String port) {
        log.error("Port " + p + " in use, cannot open");
    }

    DataInputStream serialStream = null;

    OutputStream ostream = null;

    static org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(NcePacketMonitorPanel.class.getName());

    /**
     * Internal class to handle the separate character-receive thread
     *
     */
    class Reader implements Runnable {

        /**
         * Handle incoming characters.  This is a permanent loop,
         * looking for input messages in character form on the
         * stream connected to the PortController via <code>connectPort</code>.
         * Terminates with the input stream breaking out of the try block.
         */
        public void run() {
            while (true) {
                try {
                    handleIncomingData();
                } catch (java.io.IOException e) {
                    log.warn("run: Exception: " + e.toString());
                }
            }
        }

        static final int maxMsg = 80;

        StringBuffer msg;

        StringBuffer duplicates = new StringBuffer(maxMsg);

        String msgString;

        String matchString = "";

        void handleIncomingData() throws java.io.IOException {
            msg = new StringBuffer(maxMsg);
            int i;
            for (i = 0; i < maxMsg; i++) {
                char char1 = (char) serialStream.readByte();
                if (char1 == 13) {
                    break;
                }
                msg.append(char1);
            }
            msgString = msg.toString();
            if (msgString.equals(matchString) && truncateCheckBox.isSelected()) {
                duplicates.append('+');
            } else {
                matchString = msgString;
                if (duplicates.length() != 0) {
                    duplicates.append('\n');
                    msgString = " " + (new String(duplicates)) + (msgString);
                } else {
                    msgString = "\n" + msgString;
                }
                duplicates.setLength(0);
                Runnable r = new Runnable() {

                    String msgForLater = msgString;

                    public void run() {
                        nextLine(msgForLater, "");
                    }
                };
                javax.swing.SwingUtilities.invokeLater(r);
            }
        }
    }
}
