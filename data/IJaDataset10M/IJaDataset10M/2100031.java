package codeanticode.prodmx;

import processing.core.*;
import processing.serial.*;

public class DMX {

    public DMX(PApplet parent, String port, int rate, int size) {
        this.parent = parent;
        serial = new Serial(parent, port, rate);
        universeSize = size;
        int dataSize = universeSize;
        dataSize++;
        message = new byte[universeSize + 6];
        message[0] = DMX_PRO_MESSAGE_START;
        message[1] = DMX_PRO_SEND_PACKET;
        message[2] = (byte) (dataSize & 255);
        message[3] = (byte) ((dataSize >> 8) & 255);
        message[4] = 0;
        for (int i = 0; i < universeSize; i++) {
            message[5 + i] = 0;
        }
        message[5 + universeSize] = DMX_PRO_MESSAGE_END;
    }

    public void setDMXChannel(int channel, int value) {
        if (message[channel + 5] != (byte) value) {
            message[channel + 5] = (byte) value;
            serial.write(message);
        }
    }

    public void setDMXChannels(int[] values) {
        boolean diff = false;
        for (int channel = 0; channel < universeSize; channel++) {
            byte value = (byte) values[channel];
            if (message[channel + 5] != value) {
                message[channel + 5] = value;
                diff = true;
            }
        }
        if (diff) serial.write(message);
    }

    private PApplet parent;

    private Serial serial;

    private int universeSize;

    private byte[] message;

    private byte DMX_PRO_MESSAGE_START = (byte) (0x7E);

    private byte DMX_PRO_MESSAGE_END = (byte) (0xE7);

    private byte DMX_PRO_SEND_PACKET = (byte) (6);
}
