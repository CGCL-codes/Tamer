package tuwien.auto.calimero.knxnetip.util;

import tuwien.auto.calimero.exception.KNXFormatException;

/**
 * Description Information Block (DIB).
 * <p>
 * A DIB is used to return device specific information.<br>
 * This DIB is a common base for more detailed description formats contained in DIBs. For
 * usage of the different description information available, refer to the DIB subtypes.
 * <p>
 * The currently known valid descriptor type codes (KNXnet/IP core specification v1.2) are
 * defined as available DIB constants.
 * 
 * @author B. Malinowsky
 */
public abstract class DIB {

    /**
	 * Description type code for device information e.g. KNX medium.
	 * <p>
	 */
    public static final short DEVICE_INFO = 0x01;

    /**
	 * Description type code for further data defined by device manufacturer.
	 * <p>
	 */
    public static final short MFR_DATA = 0xFE;

    /**
	 * Description type code for service families supported by the device.
	 * <p>
	 */
    public static final short SUPP_SVC_FAMILIES = 0x02;

    final short size;

    final short type;

    /**
	 * Creates a new DIB out of a byte array.
	 * <p>
	 * 
	 * @param data byte array containing DIB structure
	 * @param offset start offset of DIB in <code>data</code>
	 * @throws KNXFormatException if no DIB found or invalid structure
	 */
    protected DIB(byte[] data, int offset) throws KNXFormatException {
        if (data.length - offset < 2) throw new KNXFormatException("buffer too short for DIB header");
        size = (short) (data[offset] & 0xFF);
        type = (short) (data[offset + 1] & 0xFF);
        if (size > data.length - offset) throw new KNXFormatException("DIB size bigger than actual data length", size);
    }

    /**
	 * Returns the description type code of this DIB.
	 * <p>
	 * The type code specifies which kind of description information is contained in the
	 * DIB.
	 * 
	 * @return description type code as unsigned byte
	 */
    public final short getDescTypeCode() {
        return type;
    }

    /**
	 * Returns the structure length of this DIB in bytes.
	 * <p>
	 * 
	 * @return structure length as unsigned byte
	 */
    public final short getStructLength() {
        return size;
    }

    /**
	 * Returns the byte representation of the whole DIB structure.
	 * <p>
	 * 
	 * @return byte array containing structure
	 */
    public byte[] toByteArray() {
        final byte[] buf = new byte[size];
        buf[0] = (byte) size;
        buf[1] = (byte) type;
        return buf;
    }
}
