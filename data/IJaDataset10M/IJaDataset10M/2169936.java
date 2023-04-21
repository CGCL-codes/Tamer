package no.computanse.open4610.usbactions;

import javax.usb.UsbPipe;
import no.computanse.open4610.conn.UsbPipeAction;

/**
 * 
 * @author Tony Olsen
 * 
 */
public class Usb4160InvertedTextOffAction implements UsbPipeAction {

    public Usb4160InvertedTextOffAction() {
        super();
    }

    public void action(UsbPipe pipe) throws Exception {
        int arraySize = 10;
        Integer intTmp = Integer.valueOf(arraySize - 3);
        byte[] rev = new byte[arraySize];
        rev[0] = (byte) 0x00;
        rev[1] = intTmp.byteValue();
        rev[2] = (byte) 0x00;
        rev[3] = (byte) 0x01;
        rev[4] = (byte) 0x00;
        rev[5] = (byte) 0x00;
        rev[6] = (byte) 0x00;
        rev[7] = (byte) 0x1b;
        rev[8] = (byte) 0x48;
        rev[9] = (byte) 0x00;
        pipe.syncSubmit(rev);
    }
}
