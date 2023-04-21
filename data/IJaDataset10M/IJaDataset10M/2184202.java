package nl.kbna.dioscuri.module.video;

import java.util.Arrays;

/**
 * Graphics registers
 * Controls how the CPU accesses video RAM<BR>
 * Consists of 9 8-bit registers; these are accessed via a pair of registers, <BR>
 *  the Address Register [0x3CE] and the Data Register [0x3CF]
 */
public class GraphicsController {

    int index;

    byte[] latch = new byte[4];

    byte setReset;

    byte enableSetReset;

    byte colourCompare;

    byte dataOperation;

    byte dataRotate;

    byte readMapSelect;

    byte shift256Reg;

    byte hostOddEvenEnable;

    byte readMode;

    byte writeMode;

    byte memoryMapSelect;

    byte chainOddEvenEnable;

    byte alphaNumDisable;

    byte colourDontCare;

    byte bitMask;

    protected static final byte colourCompareTable[][] = { { 0x00, 0x00, 0x00, 0x00 }, { (byte) 0xFF, 0x00, 0x00, 0x00 }, { 0x00, (byte) 0xFF, 0x00, 0x00 }, { (byte) 0xFF, (byte) 0xFF, 0x00, 0x00 }, { 0x00, 0x00, (byte) 0xFF, 0x00 }, { (byte) 0xFF, 0x00, (byte) 0xFF, 0x00 }, { 0x00, (byte) 0xFF, (byte) 0xFF, 0x00 }, { (byte) 0xFF, (byte) 0xFF, (byte) 0xFF, 0x00 }, { 0x00, 0x00, 0x00, (byte) 0xFF }, { (byte) 0xFF, 0x00, 0x00, (byte) 0xFF }, { 0x00, (byte) 0xFF, 0x00, (byte) 0xFF }, { (byte) 0xFF, (byte) 0xFF, 0x00, (byte) 0xFF }, { 0x00, 0x00, (byte) 0xFF, (byte) 0xFF }, { (byte) 0xFF, 0x00, (byte) 0xFF, (byte) 0xFF }, { 0x00, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF }, { (byte) 0xFF, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF } };

    /**
     * Return variables to default values
     */
    protected void reset() {
        index = 0;
        Arrays.fill(latch, (byte) 0);
        setReset = 0;
        enableSetReset = 0;
        colourCompare = 0;
        dataOperation = 0;
        dataRotate = 0;
        readMapSelect = 0;
        shift256Reg = 0;
        hostOddEvenEnable = 0;
        readMode = 0;
        writeMode = 0;
        memoryMapSelect = 2;
        chainOddEvenEnable = 0;
        alphaNumDisable = 0;
        colourDontCare = 0;
        bitMask = 0;
    }
}
