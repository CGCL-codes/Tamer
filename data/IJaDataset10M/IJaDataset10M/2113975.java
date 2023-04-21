package com.hs.utils;

import gnu.io.CommPort;
import gnu.io.CommPortIdentifier;
import gnu.io.SerialPort;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import com.hs.core.comm.FetchingCardDiagram;

public class TwoWaySerialComm {

    void connect(String portName) throws Exception {
        CommPortIdentifier portIdentifier = CommPortIdentifier.getPortIdentifier(portName);
        if (portIdentifier.isCurrentlyOwned()) {
            System.out.println("Error: Port is currently in use");
        } else {
            CommPort commPort = portIdentifier.open(this.getClass().getName(), 2000);
            if (commPort instanceof SerialPort) {
                SerialPort serialPort = (SerialPort) commPort;
                serialPort.setSerialPortParams(19200, SerialPort.DATABITS_8, SerialPort.STOPBITS_1, SerialPort.PARITY_EVEN);
                InputStream in = serialPort.getInputStream();
                OutputStream out = serialPort.getOutputStream();
                (new Thread(new SerialReader(in))).start();
                (new Thread(new SerialWriter(out))).start();
            } else {
                System.out.println("Error: Only serial ports are handled by this example.");
            }
        }
    }

    /** */
    public static class SerialReader implements Runnable {

        InputStream in;

        public SerialReader(InputStream in) {
            this.in = in;
        }

        public void run() {
            byte[] buffer = new byte[128];
            int len = -1;
            try {
                while ((len = this.in.read(buffer)) > -1) {
                    if (len > 2) {
                        for (int i = 0; i <= len; i++) {
                            System.out.print(buffer[i]);
                            System.out.print(",");
                        }
                        System.out.println();
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /** */
    public static class SerialWriter implements Runnable {

        OutputStream out;

        private FetchingCardDiagram fcd = new FetchingCardDiagram();

        private byte[] bs = fcd.getSequence();

        public SerialWriter(OutputStream out) {
            this.out = out;
        }

        public void run() {
            while (true) {
                try {
                    out.write(bs);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public static void main(String[] args) {
        try {
            (new TwoWaySerialComm()).connect("COM1");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
