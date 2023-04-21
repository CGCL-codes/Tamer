package org.mbari.vcr;

import gnu.io.NoSuchPortException;
import gnu.io.PortInUseException;
import gnu.io.SerialPortEvent;
import gnu.io.SerialPortEventListener;
import gnu.io.UnsupportedCommOperationException;
import java.io.IOException;
import org.mbari.vcr.rs422.VCR;

public class VCRTestRxtx2 extends VCRTestRxtx1 {

    public VCRTestRxtx2(String serialPortName) throws NoSuchPortException, PortInUseException, UnsupportedCommOperationException, IOException {
        super(serialPortName);
    }

    public VCRTestRxtx2(String serialPortName, long receiveTimeout) throws NoSuchPortException, PortInUseException, UnsupportedCommOperationException, IOException {
        super(serialPortName, receiveTimeout);
    }

    void init(String serialPortName) throws NoSuchPortException, PortInUseException, UnsupportedCommOperationException, IOException {
        super.init(serialPortName);
        serialPort.notifyOnDataAvailable(true);
        try {
            serialPort.addEventListener(new SerialPortEventListener() {

                public void serialEvent(SerialPortEvent evt) {
                    System.out.println(evt);
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
                            byte[] readBuffer = new byte[20];
                            try {
                                while (in.available() > 0) {
                                    int numBytes = in.read(readBuffer);
                                    System.out.print(new String(readBuffer));
                                }
                            } catch (IOException e) {
                            }
                            break;
                    }
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        int ok = 0;
        try {
            VCRTestRxtx1 test = new VCRTestRxtx1("COM2", 500);
            test.send(VCR.GET_STATUS);
            test.send(VCR.GET_TIMECODE);
            test.send(VCR.PLAY_FWD);
            test.send(VCR.GET_STATUS);
            test.send(VCR.GET_TIMECODE);
            Thread.sleep(2000);
            test.send(VCR.STOP);
            test.send(VCR.GET_TIMECODE);
            Thread.sleep(2000);
            test.send(VCR.GET_TIMECODE1);
            Thread.sleep(2000);
            test.send(VCR.GET_TIMECODE2);
            Thread.sleep(2000);
            test.send(VCR.GET_STATUS);
            Thread.sleep(2000);
        } catch (Exception e) {
            e.printStackTrace();
            ok = -1;
        }
        System.exit(ok);
    }
}
