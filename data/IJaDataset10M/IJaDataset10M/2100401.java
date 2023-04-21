package de.carne.fs.provider.swf;

import java.io.IOException;
import java.nio.ByteOrder;
import java.nio.channels.ReadableByteChannel;
import de.carne.fs.core.FileScannerResultNode;
import de.carne.fs.core.format.FormatDecoder;
import de.carne.io.IncompleteReadException;
import de.carne.nio.BitDecoder;
import de.carne.nio.MSBBitstreamBitBuffer;
import de.carne.util.Debug;

/**
 * TODO
 */
class SWFFrameSize extends FormatDecoder {

    private SWFFrameSize() {
        super(SWF.NAME_FRAME_SIZE, ByteOrder.LITTLE_ENDIAN);
    }

    public static final SWFFrameSize THIS = new SWFFrameSize();

    @Override
    public FileScannerResultNode decode(FileScannerResultNode parent, long position) throws IOException {
        final ReadableByteChannel bytes = parent.input().readableByteChannel(position);
        final BitDecoder bits = new BitDecoder(new MSBBitstreamBitBuffer());
        FileScannerResultNode decoded = null;
        try {
            final int nBits = bits.decodeBits(bytes, 5);
            final int xMin = bits.decodeBits(bytes, nBits);
            final int xMax = bits.decodeBits(bytes, nBits);
            final int yMin = bits.decodeBits(bytes, nBits);
            final int yMax = bits.decodeBits(bytes, nBits);
            decoded = parent.addFormat(name(), position, position + bits.totalIn());
        } catch (final IncompleteReadException e) {
            Debug.exception(e);
        }
        return decoded;
    }
}
