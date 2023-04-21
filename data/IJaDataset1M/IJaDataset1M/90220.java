package no.computanse.open4610.usbactions;

import static org.junit.Assert.*;
import java.util.List;
import no.computanse.open4610.usbactions.Usb4160UploadLogoHelper;
import org.junit.Test;

public class Usb4160UploadLogoHelperTest {

    @Test
    public void splitMultipleSets() {
        Usb4160UploadLogoHelper helper = new Usb4160UploadLogoHelper();
        byte[] rev = create(1050);
        List<byte[]> split = helper.split(rev);
        assertEquals(3, split.size());
        assertEquals((byte) 0xFC, split.get(0)[1]);
    }

    @Test
    public void splitOneSets() {
        Usb4160UploadLogoHelper helper = new Usb4160UploadLogoHelper();
        byte[] rev = create(783);
        List<byte[]> split = helper.split(rev);
        assertEquals(2, split.size());
        assertEquals((byte) 0x0C, split.get(0)[1]);
    }

    @Test
    public void splitOneSingle() {
        Usb4160UploadLogoHelper helper = new Usb4160UploadLogoHelper();
        byte[] rev = create(120);
        List<byte[]> split = helper.split(rev);
        assertEquals(1, split.size());
        assertEquals(Integer.valueOf(117).byteValue(), split.get(0)[1]);
        assertEquals((byte) 0x1B, split.get(0)[117]);
        assertEquals((byte) 0x29, split.get(0)[118]);
        assertEquals((byte) 0x00, split.get(0)[119]);
    }

    private byte[] create(int size) {
        byte[] rev = new byte[size];
        for (int i = 0; i < rev.length; i++) {
            rev[i] = (byte) 0xFA;
        }
        rev[0] = (byte) 0x01;
        rev[1] = (byte) 0x00;
        rev[2] = (byte) 0x03;
        rev[3] = (byte) 0x01;
        rev[4] = (byte) 0x00;
        rev[5] = (byte) 0x00;
        rev[6] = (byte) 0x00;
        rev[7] = (byte) 0x1D;
        rev[8] = (byte) 0x2A;
        rev[9] = (byte) 1;
        rev[10] = (byte) 2;
        rev[11] = (byte) 4;
        int arraySize = rev.length;
        rev[arraySize - 3] = (byte) 0x1B;
        rev[arraySize - 2] = (byte) 0x29;
        rev[arraySize - 1] = (byte) 0x00;
        return rev;
    }
}
