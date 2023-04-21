package synthdrivers.YamahaTX7;

import core.*;

class VoiceSender extends SysexSender {

    int parameter;

    int para_high = 0;

    byte[] b = new byte[7];

    public VoiceSender(int param) {
        parameter = param;
        if (parameter >= 128) {
            para_high = 0x01;
            parameter -= 128;
        }
        b[0] = (byte) 0xF0;
        b[1] = (byte) 0x43;
        b[3] = (byte) para_high;
        b[4] = (byte) parameter;
        b[6] = (byte) 0xF7;
    }

    public byte[] generate(int value) {
        b[2] = (byte) (0x10 + channel - 1);
        b[5] = (byte) value;
        return b;
    }
}

class PerformanceSender extends SysexSender {

    Patch patch;

    int parameter;

    byte[] b = new byte[7];

    public PerformanceSender(Patch p, int param) {
        patch = p;
        parameter = param;
        b[0] = (byte) 0xF0;
        b[1] = (byte) 0x43;
        b[3] = (byte) 0x04;
        b[4] = (byte) parameter;
        b[6] = (byte) 0xF7;
    }

    public byte[] generate(int value) {
        b[2] = (byte) (0x10 + channel - 1);
        b[5] = (byte) value;
        return b;
    }
}
