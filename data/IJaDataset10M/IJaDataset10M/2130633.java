package de.carne.fs.provider.zip;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import de.carne.fs.core.FileScannerResultNode;
import de.carne.fs.core.format.FormatStruct;
import de.carne.fs.core.format.FormatStructTransferHandler;
import de.carne.fs.core.format.IntAttribute;

/**
 * ZIP data descriptor structure (without signature).
 */
public class ZIPDataDescriptor2 extends FormatStruct {

    private static final IntAttribute SIGNATURE = new IntAttribute("data descriptor signature", 0, 1, false, 16);

    private static final IntAttribute CRC = new IntAttribute("crc-32", 4, 1, false, 16);

    private static final IntAttribute COMPRESSED_SIZE = new IntAttribute("compressed size", 8, 1, false, 10);

    private static final IntAttribute UNCOMPRESSED_SIZE = new IntAttribute("uncompressed size", 12, 1, false, 10);

    private ZIPDataDescriptor2() {
        super(ZIP.NAME_DATA_DESCRIPTOR, ByteOrder.LITTLE_ENDIAN, SIGNATURE, CRC, COMPRESSED_SIZE, UNCOMPRESSED_SIZE);
    }

    /**
	 * The Format instance
	 */
    public static final ZIPDataDescriptor2 THIS = new ZIPDataDescriptor2();

    @Override
    public boolean validate(ByteBuffer buffer) {
        return super.validate(buffer) && SIGNATURE.validate(buffer, ZIP.SIGNATURE_DATA_DESCRIPTOR);
    }

    @Override
    public FileScannerResultNode decodeBuffer(FileScannerResultNode parent, long position, ByteBuffer buffer) throws IOException {
        final FileScannerResultNode decoded = parent.addFormat(name(), position, position + size());
        decoded.addDataHandler(new FormatStructTransferHandler(decoded, this));
        return decoded;
    }
}
