package it.ame.permflow.IO;

import it.ame.permflow.IO.serial.*;
import java.io.IOException;

public class DummyDevice implements Device {

    StreamEncapsulator genThread;

    SerialThread serialThread;

    public DummyDevice(String genThread, String serialThread) throws Exception {
        this.genThread = (StreamEncapsulator) Class.forName(genThread).newInstance();
        this.serialThread = (SerialThread) Class.forName(serialThread).newInstance();
        handshake();
    }

    private void handshake() throws IOException {
        assert genThread != null && serialThread != null;
        serialThread.connect(genThread);
    }

    public void send(byte[] data) throws IOException {
        serialThread.sendToSerial(data);
    }

    public void flush() throws IOException {
    }
}
