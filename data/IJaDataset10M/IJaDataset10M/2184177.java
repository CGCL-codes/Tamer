package org.openconcerto.erp.core.sales.pos.io;

import gnu.io.CommPort;
import gnu.io.CommPortIdentifier;
import gnu.io.SerialPort;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class ESCSerialPrinter extends DefaultTicketPrinter {

    private String port;

    public ESCSerialPrinter(String port) {
        this.port = port;
    }

    public void addToBuffer(String t) {
        addToBuffer(t, NORMAL);
    }

    public void addToBuffer(String t, int mode) {
        this.strings.add(t);
        this.modes.add(mode);
    }

    public void openDrawer() throws Exception {
        SerialPort serialPort = getSerialPort();
        OutputStream out = serialPort.getOutputStream();
        out.write(0x10);
        out.write(0x14);
        out.write(0x01);
        out.write(0x00);
        out.write(0x02);
        out.flush();
        try {
            Thread.sleep(300);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        out.write(0x10);
        out.write(0x14);
        out.write(0x01);
        out.write(0x01);
        out.write(0x02);
        try {
            Thread.sleep(300);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        out.flush();
        out.close();
        serialPort.close();
    }

    public void printBuffer() throws Exception {
        System.out.println("Port " + this.port);
        SerialPort serialPort = getSerialPort();
        OutputStream out = serialPort.getOutputStream();
        out.write(0x1B);
        out.write(0x40);
        out.write(0x1B);
        out.write(0x52);
        out.write(0x01);
        for (int i = 0; i < this.strings.size(); i++) {
            String string = this.strings.get(i);
            int mode = modes.get(i);
            if (mode == BARCODE) {
                System.out.println("Barcode:" + string);
                out.write(0x1D);
                out.write(0x48);
                out.write(0x02);
                out.write(0x1D);
                out.write(0x77);
                out.write(0x02);
                out.write(0x1D);
                out.write(0x68);
                out.write(60);
                out.write(0x1D);
                out.write(0x6B);
                out.write(0x04);
                for (int k = 0; k < string.length(); k++) {
                    char c = string.charAt(k);
                    out.write(c);
                }
                out.write(0x00);
            } else {
                if (mode == NORMAL) {
                    out.write(0x1B);
                    out.write(0x21);
                    out.write(0);
                } else if (mode == BOLD) {
                    out.write(0x1B);
                    out.write(0x21);
                    out.write(8);
                } else if (mode == BOLD_LARGE) {
                    out.write(0x1D);
                    out.write(0x21);
                    out.write(0x11);
                }
                for (int k = 0; k < string.length(); k++) {
                    char c = string.charAt(k);
                    if (c == 'é') {
                        c = 130;
                    } else if (c == 'è') {
                        c = 138;
                    } else if (c == 'ê') {
                        c = 136;
                    } else if (c == 'ù') {
                        c = 151;
                    } else if (c == 'à') {
                        c = 133;
                    } else if (c == 'ç') {
                        c = 135;
                    } else if (c == 'ô') {
                        c = 147;
                    }
                    out.write(c);
                }
            }
            out.write(0x0A);
        }
        out.write(0x0A);
        out.write(0x0A);
        out.write(0x0A);
        out.write(0x0A);
        out.write(0x1D);
        out.write(0x56);
        out.write(0x01);
        out.close();
        serialPort.close();
    }

    private SerialPort getSerialPort() throws Exception {
        if (port == null || port.length() == 0) {
            throw new IllegalStateException("Invalid serial port name: " + port);
        }
        CommPortIdentifier portIdentifier = CommPortIdentifier.getPortIdentifier(this.port);
        if (portIdentifier.isCurrentlyOwned()) {
            throw new IllegalAccessError("Port " + this.port + " is currently in use");
        }
        CommPort commPort = portIdentifier.open("CommUtil", 2000);
        if (!(commPort instanceof SerialPort)) {
            throw new IllegalStateException("Invalid serial port: " + port);
        }
        SerialPort serialPort = (SerialPort) commPort;
        serialPort.setSerialPortParams(9600, SerialPort.DATABITS_8, SerialPort.STOPBITS_1, SerialPort.PARITY_NONE);
        return serialPort;
    }

    public static void main(String[] args) {
        listPorts();
        ESCSerialPrinter prt = new ESCSerialPrinter("COM1:");
        prt.setPort(getSerialPortNames().get(0));
        prt.addToBuffer("ILM INFORMATIQUE", BOLD_LARGE);
        prt.addToBuffer("");
        prt.addToBuffer("22 place de la liberation");
        prt.addToBuffer("80100 ABBEVILLE");
        prt.addToBuffer("Tél: 00 00 00 00 00");
        prt.addToBuffer("Fax: 00 00 00 00 00");
        prt.addToBuffer("");
        SimpleDateFormat df = new SimpleDateFormat("EEEE d MMMM yyyy à HH:mm");
        prt.addToBuffer(formatRight(45, "Le " + df.format(Calendar.getInstance().getTime())));
        prt.addToBuffer("");
        prt.addToBuffer(formatRight(5, "3") + " " + formatLeft(30, "ILM Informatique") + " " + formatRight(8, "3.00"));
        prt.addToBuffer("      =======================================");
        prt.addToBuffer(formatRight(37, "Total") + formatRight(8, "3.00"), BOLD);
        prt.addToBuffer("");
        prt.addToBuffer("Merci de votre visite, à bientôt.");
        prt.addToBuffer("");
        prt.addToBuffer("01 05042010 00002", BARCODE);
        try {
            prt.printBuffer();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void setPort(String string) {
        this.port = string;
    }

    private static void listPorts() {
        java.util.Enumeration<CommPortIdentifier> portEnum = CommPortIdentifier.getPortIdentifiers();
        while (portEnum.hasMoreElements()) {
            CommPortIdentifier portIdentifier = portEnum.nextElement();
            System.out.println("Port: " + portIdentifier.getName() + " Type: " + getPortTypeName(portIdentifier.getPortType()));
        }
    }

    private static List<String> getSerialPortNames() {
        ArrayList<String> r = new ArrayList<String>();
        java.util.Enumeration<CommPortIdentifier> portEnum = CommPortIdentifier.getPortIdentifiers();
        while (portEnum.hasMoreElements()) {
            CommPortIdentifier portIdentifier = portEnum.nextElement();
            System.out.println("Port: " + portIdentifier.getName() + " Type: " + getPortTypeName(portIdentifier.getPortType()));
            if (portIdentifier.getPortType() == CommPortIdentifier.PORT_SERIAL) {
                r.add(portIdentifier.getName());
            }
        }
        return r;
    }

    private static String getPortTypeName(int portType) {
        switch(portType) {
            case CommPortIdentifier.PORT_I2C:
                return "I2C";
            case CommPortIdentifier.PORT_PARALLEL:
                return "Parallel";
            case CommPortIdentifier.PORT_RAW:
                return "Raw";
            case CommPortIdentifier.PORT_RS485:
                return "RS485";
            case CommPortIdentifier.PORT_SERIAL:
                return "Serial";
            default:
                return "unknown type";
        }
    }
}
