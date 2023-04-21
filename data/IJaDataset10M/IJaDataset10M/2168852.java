package it.ame.permflow.IO;

import gnu.io.*;
import java.io.InputStream;
import java.util.ArrayList;
import it.ame.permflow.gui.GraphHUD;
import it.ame.permflow.util.*;

public abstract class StreamEncapsulator implements SerialPortEventListener {

    final ArrayList<Byte> buffer;

    StreamEncapsulator() {
        buffer = new ArrayList<Byte>();
    }

    ArrayList<Byte> getBuffer() {
        return buffer;
    }

    abstract void generateMessages();

    public void serialEvent(SerialPortEvent event) {
        switch(event.getEventType()) {
            case SerialPortEvent.OUTPUT_BUFFER_EMPTY:
                outputBufferEmpty(event);
                break;
            case SerialPortEvent.DATA_AVAILABLE:
                dataAvailable(event);
                break;
        }
    }

    private void dataAvailable(SerialPortEvent event) {
        try {
            InputStream is = ((SerialPort) event.getSource()).getInputStream();
            if (!GraphHUD.running && (this instanceof HumSensorEncapsulator || this instanceof PermSensorEncapsulator)) {
                byte[] b = new byte[is.available()];
                is.read(b);
            } else {
                byte[] b = new byte[is.available()];
                is.read(b);
                for (byte aB : b) buffer.add(aB);
                generateMessages();
            }
        } catch (Exception e) {
            Logger.reportException(e);
            e.printStackTrace();
        }
    }

    private void outputBufferEmpty(SerialPortEvent event) {
    }
}
