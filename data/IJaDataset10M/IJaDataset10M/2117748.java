package org.jsynthlib.synthdrivers.BehringerFCB1010;

import org.jsynthlib.core.*;

/** FCB1010ParamModel. This model is used for all parameters of the FCB1010 and includes.
* masks to manipulate either the last seven bits of a byte, the first bit, or all
* 8 bits. It also includes a reverse parameter that allows the values to be reversed
* low to high.
*
* @author Jeff Weber
*/
class FCB1010ParamModel extends ParamModel {

    /** Mask supplied for manipulating the 7 least significant bits of the byte at the given offset
    * of the sysex record.
    */
    static final byte LSB_MASK = (byte) 0x7F;

    /** Mask supplied for manipulating the first bit of the byte at the given offset
    * of the sysex record.
    */
    static final byte MSB_MASK = (byte) 0x80;

    /** Mask supplied for manipulating all 8 bits of the byte at the given offset
        * of the sysex record.
        */
    static final byte BYTE = (byte) 0xFF;

    /** The length of a single preset within the FCB1010 sysex record.
        */
    private static final int PR_LENGTH = 16;

    /** The offset within the FCB1010 sysex record of the currently edited preset.
        */
    private static int prOffset = 0;

    /** Variable holding the mask for this instance of FCB1010ParamModel.
        */
    private byte mask;

    /** Variable holding the number of bits to be shifted to set or get the value
        * for this instance of FCB1010ParamModel.
        */
    private int power = 0;

    /** Boolean indicating whether the value is to be reversed or not. Applicable only
        * when MSB_MASK is in effect (see method definition for reverseMSB (int patchVal)).
        */
    private boolean reverse;

    /** Constructs a FCB1010ParamModel given the patch, the offset into the first preset,
        * the mask representing the parameter, and a boolean (reverse, used only with the MSB mask).
        * Normally, the MSB values are reversed (0 for 1 and 1 for 0). To prevent this from
        * happening a value of false can be set for the reverse parameter.
        */
    FCB1010ParamModel(Patch p, int offset, byte mask, boolean reverse) {
        super(p, offset);
        this.mask = mask;
        this.reverse = reverse;
        prOffset = 0;
        int bitPos = 1;
        int rem = 0;
        while (rem == 0) {
            bitPos *= 2;
            power += 1;
            rem = mask % bitPos;
        }
        power -= 1;
    }

    /** Constructs a FCB1010ParamModel given the patch, the offset into the first preset,
        * and the mask representing the parameter.
        */
    FCB1010ParamModel(Patch p, int offset, byte mask) {
        this(p, offset, mask, true);
    }

    /** Sets the offset into the sysex record for the current preset,
        * given the bank and preset numbers.
        */
    static void setPreset(int bank, int preset) {
        prOffset = ((bank * 10) + preset) * PR_LENGTH;
    }

    /** The FCB1010 MIDI Memory Dump has a special format that needs to be decoded for
        * interpretation and further application in a MIDI editor. The problem: the internal memory
        * appearance of 8 bit (1 byte) needs to be transmitted via MIDI in 7 bit.
        * The transmitted bytes' MSBs are always zero. For this reason the first 7 bytes' MSBs
        * are transmitted altogether in the 8th byte. This pattern repeats throughout the
        * sysex record. deNibblize decodes this format by taking the least significant 7 bits
        * of every eighth byte and applying them to each of the most significant bit of the
        * previous seven bytes.
        */
    static byte[] deNibblize(byte[] array, int hdrSize) {
        byte endOfSysex = (byte) 0xF7;
        byte[] deNibblizedArray = new byte[array.length];
        System.arraycopy(array, 0, deNibblizedArray, 0, hdrSize);
        byte[] msbArray = new byte[array.length / 8];
        int i = 0;
        int j = 0;
        int k = 0;
        for (j = 0; array[j + hdrSize] != endOfSysex; j++) {
            if ((j + 1) % 8 != 0) {
                deNibblizedArray[hdrSize + k++] = array[j + hdrSize];
            } else {
                msbArray[i++] = array[j + hdrSize];
            }
        }
        deNibblizedArray[hdrSize + k] = endOfSysex;
        i = -1;
        for (j = 0; j <= k - 7; j = j + 7) {
            for (int l = 0; l < 7; l++) {
            }
            i++;
            for (int l = 0; l < 7; l++) {
                byte cByte = (byte) (msbArray[i] << 7 - l);
                cByte = (byte) (cByte & 0x80);
                cByte = (byte) (deNibblizedArray[j + hdrSize + l] | cByte);
                deNibblizedArray[j + hdrSize + l] = cByte;
            }
        }
        deNibblizedArray[hdrSize + k] = endOfSysex;
        byte returnArray[] = new byte[hdrSize + k + 1];
        System.arraycopy(deNibblizedArray, 0, returnArray, 0, returnArray.length);
        return returnArray;
    }

    /** Takes the bytes of the sysex record in groups of 7. For each group, takes the
        * most significant bit from each byte and copies it into an eight byte. The msbs
        * of the seven data bytes are then set to 0.
        */
    static byte[] reNibblize(byte[] array, int hdrSize) {
        byte[] reNibblizedArray = new byte[Constants.HDR_SIZE + Constants.FCB1010_NATIVE_SIZE + 1];
        System.arraycopy(array, 0, reNibblizedArray, 0, hdrSize);
        int j = 0;
        for (int i = 0; i < array.length - hdrSize; i++) {
            if ((j + 1) % 8 == 0) {
                int cByte = 0x00;
                for (int k = 1; k < 8; k++) {
                    int dByte = reNibblizedArray[hdrSize + j - k];
                    dByte = (dByte & 0x80);
                    dByte = (dByte >>> k);
                    cByte = (cByte | dByte);
                    reNibblizedArray[hdrSize + j - k] = (byte) (reNibblizedArray[hdrSize + j - k] & 0x7f);
                }
                reNibblizedArray[hdrSize + j++] = (byte) cByte;
            }
            reNibblizedArray[hdrSize + j++] = array[hdrSize + i];
        }
        byte endOfSysex = (byte) 0xF7;
        reNibblizedArray[reNibblizedArray.length - 1] = endOfSysex;
        return reNibblizedArray;
    }

    /** Updates the bits defined by mask within the byte in the sysex record
        * defined by offset with the value supplied by i.
        */
    public void set(int i) {
        i = reverseMSB(i);
        byte[] denibblizedArray = deNibblize(this.patch.sysex, Constants.HDR_SIZE);
        byte newPatchVal = (byte) ((denibblizedArray[getOffset()] & (~mask)) | ((i << power) & mask));
        denibblizedArray[getOffset()] = newPatchVal;
        byte[] renibblizedArray = reNibblize(denibblizedArray, Constants.HDR_SIZE);
        System.arraycopy(renibblizedArray, 0, this.patch.sysex, 0, this.patch.sysex.length);
    }

    /** Gets the value of the bits defined by mask within the byte in the
        * sysex record defined by offset and returns the value as in int.
        */
    public int get() {
        byte[] denibblizedArray = deNibblize(this.patch.sysex, Constants.HDR_SIZE);
        int patchVal = (byte) (denibblizedArray[getOffset()] & mask);
        patchVal = patchVal & (int) 0x000000FF;
        patchVal = (patchVal >>> power);
        patchVal = reverseMSB(patchVal);
        return patchVal;
    }

    /** Calculates and returns the offset into the sysex record to the get() and set() methods.
        * If the BYTE mask is in effect, the offset is equal to the assigned offset plus the size
        * of the sysex header. If the MSB_MASK or LSB_MASK is in effect, the offset is equal to
        * the assigned offset, plus the size of the sysex header, plus the offset of the current
        * preset.
        */
    private int getOffset() {
        int realOffset;
        if (this.mask == BYTE) {
            realOffset = Constants.HDR_SIZE + ofs;
        } else {
            realOffset = Constants.HDR_SIZE + prOffset + ofs;
        }
        return realOffset;
    }

    /** If the reverse parameter and the MSB_MASK are in effect for the current instance, swaps
        * the value of patchVal 0 for 1 and 1 for zero and returns the result. Otherwise
        * the value of patchVal is returned unchanged..
        */
    private int reverseMSB(int patchVal) {
        if (reverse) {
            if (mask == MSB_MASK) {
                if (patchVal == 0) {
                    patchVal = 1;
                } else {
                    patchVal = 0;
                }
            }
        }
        return patchVal;
    }
}
