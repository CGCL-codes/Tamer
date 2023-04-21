package co.edu.unal.ungrid.image.dicom.scpecg;

import java.io.IOException;
import co.edu.unal.ungrid.image.dicom.core.BinaryInputStream;

/**
 * <p>
 * A class to encapsulate an SCP-ECG record header.
 * </p>
 * 
 * 
 */
public class RecordHeader {

    /**
	 * @uml.property name="crc"
	 */
    private int crc;

    /**
	 * @uml.property name="recordLength"
	 */
    private long recordLength;

    public int getCRC() {
        return crc;
    }

    /**
	 * @return the recordLength
	 * @uml.property name="recordLength"
	 */
    public long getRecordLength() {
        return recordLength;
    }

    /**
	 * <p>
	 * Read a header from a stream.
	 * </p>
	 * 
	 * @param i
	 *            the input stream
	 */
    public long read(BinaryInputStream i) throws IOException {
        long bytesRead = 0;
        crc = i.readUnsigned16();
        bytesRead += 2;
        recordLength = i.readUnsigned32();
        bytesRead += 4;
        return bytesRead;
    }

    /**
	 * <p>
	 * Dump the record header as a <code>String</code>.
	 * </p>
	 * 
	 * @return the header as a <code>String</code>
	 */
    public String toString() {
        return "CRC = " + crc + " dec (0x" + Integer.toHexString(crc) + ")\n" + "Record Length = " + recordLength + " dec (0x" + Long.toHexString(recordLength) + ")\n";
    }
}
