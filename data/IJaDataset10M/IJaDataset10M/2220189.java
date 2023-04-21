package org.jsynthlib.synthdrivers.MIDIboxFM;

import org.jsynthlib.core.Driver;
import org.jsynthlib.core.Logger;

public class MIDIboxFMSlowSender {

    public void sendSysEx(Driver driver, byte[] buffer, int delay) {
        try {
            driver.send(buffer);
        } catch (Exception ex) {
            ex.printStackTrace();
            Logger.reportStatus(ex);
        }
        try {
            Thread.sleep(delay);
        } catch (Exception e) {
        }
    }

    public void sendParameter(Driver driver, int parameter, byte value, int delay) {
        byte[] b = new byte[12];
        b[0] = (byte) 0xF0;
        b[1] = (byte) 0x00;
        b[2] = (byte) 0x00;
        b[3] = (byte) 0x7e;
        b[4] = (byte) 0x49;
        b[5] = (byte) (driver.getDeviceID() - 1);
        b[6] = (byte) 0x06;
        b[7] = (byte) 0x00;
        b[8] = (byte) (parameter >= 0x80 ? 0x01 : 0x00);
        b[9] = (byte) (parameter & 0x7f);
        b[10] = value;
        b[11] = (byte) 0xF7;
        sendSysEx(driver, b, delay);
    }
}
