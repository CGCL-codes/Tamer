package matlab;

import java.io.DataOutputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.Date;

/**
 * Matlab header, 128 bytes
 * @author risto
 *
 */
class Header {

    public String description;

    public long subsysDataOffset;

    public short version, endian;

    /**
	 * Parse header from data
	 */
    public Header(byte[] data) {
        description = new String(Arrays.copyOfRange(data, 0, 116));
        subsysDataOffset = Binary.longFrom(data, 116);
        version = Binary.shortFrom(data, 116 + 8);
        endian = Binary.shortFrom(data, 116 + 8 + 2);
    }

    /**
	 * Construct new header
	 */
    public Header() {
        description = String.format("MATLAB 5.0 MAT-file, Source: Matlab5File.java, Date: %s", new Date());
        if (description.length() > 116) description = description.substring(0, 116);
        subsysDataOffset = 0;
        version = 0x0100;
        endian = ('M' << 8) + 'I';
    }

    /**
	 * Write header to output stream
	 * @param os
	 * @throws IOException
	 */
    public void writeTo(DataOutputStream os) throws IOException {
        System.out.format("HEADER %-116s\n", description);
        os.writeBytes(String.format("%-116s", description));
        os.writeLong(0);
        os.writeShort(version);
        os.writeShort(endian);
    }
}
