package de.carne.fs.core.transfer;

import java.io.InputStream;
import java.util.LinkedList;
import de.carne.fs.core.FileScannerInput;
import de.carne.fs.core.FileScannerResultNode;

/**
 * <code>ImageDataHandler</code> implementation supporting the composition of the image data out of various independent
 * data chunks.
 */
public class ChunkedImageDataHandler implements ImageDataHandler {

    private final LinkedList<ChunkedData> CHUNKS = new LinkedList<ChunkedData>();

    /**
	 * Construct empty <code>ChunkedImageDataHandler</code>.
	 */
    public ChunkedImageDataHandler() {
    }

    /**
	 * Construct a <code>ChunkedImageDataHandler</code> containing the same data as the submitted instance.
	 * 
	 * @param handler The <code>ChunkedImageDataHandler</code> to duplicate.
	 */
    public ChunkedImageDataHandler(ChunkedImageDataHandler handler) {
        assert handler != null;
        this.CHUNKS.addAll(handler.CHUNKS);
    }

    /**
	 * Append a <code>FileScannerInput</code> based data chunk.
	 * 
	 * @param input The <code>FileScannerInput</code> to read the data from.
	 * @param start The start position within the input.
	 * @param end The end position within the input.
	 */
    public void appendInput(FileScannerInput input, long start, long end) {
        this.CHUNKS.add(new ChunkedInputData(input, start, end));
    }

    /**
	 * Append a <code>FileScannerResultNode</code> based data chunk.
	 * 
	 * @param node The <code>FileScannerResultNode</code> providing the chunk data.
	 */
    public void appendNode(FileScannerResultNode node) {
        appendInput(node.input(), node.start(), node.end());
    }

    /**
	 * Append a raw byte array based data chunk.
	 * 
	 * @param bs The byte array providing the chunk data.
	 */
    public void appendBytes(byte[] bs) {
        this.CHUNKS.add(new ChunkedByteData(bs));
    }

    @Override
    public InputStream inputStream() {
        return new ChunkedInputStream(this.CHUNKS.toArray(new ChunkedData[this.CHUNKS.size()]));
    }
}
