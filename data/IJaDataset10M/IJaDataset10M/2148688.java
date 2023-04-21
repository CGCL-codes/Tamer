package org.ikasan.connector.util.chunking.io;

import java.io.IOException;
import java.io.InputStream;
import org.ikasan.connector.util.chunking.process.ChunkHandleException;
import org.ikasan.connector.util.chunking.process.ChunkHandler;
import org.ikasan.connector.util.chunking.provider.ChunkableDataSourceException;

/**
 * InputStream consumer class that chunks the data from the InputStream into
 * regular chunks, and passes each off to a <code>ChunkHandler</code> to
 * process
 * 
 * @author Ikasan Development Team
 * 
 */
public class ChunkingInputStreamConsumer {

    /**
     * Handler to process newly separated chunk
     */
    private ChunkHandler chunkHandler;

    /**
     * Constructor
     * 
     * @param chunkHandler
     */
    public ChunkingInputStreamConsumer(ChunkHandler chunkHandler) {
        super();
        this.chunkHandler = chunkHandler;
    }

    /**
     * Consumes the input stream, chunking into regular sized chunks (allowing
     * for a smaller remainder) passing each off to a handler for further
     * processing
     * 
     * @param inputStream
     * @param chunkSize
     * @param noOfChunks
     * @throws ChunkableDataSourceException
     */
    public void consumeInputStream(InputStream inputStream, int chunkSize, long noOfChunks) throws ChunkableDataSourceException {
        if (inputStream == null) {
            return;
        }
        try {
            boolean streamClosed = false;
            long chunkCount = 0;
            while (!streamClosed) {
                int bytesRead = 0;
                int bytesToRead = chunkSize;
                byte[] input = new byte[bytesToRead];
                while (bytesRead < bytesToRead) {
                    int result = inputStream.read(input, bytesRead, bytesToRead - bytesRead);
                    if (result == -1) {
                        streamClosed = true;
                        break;
                    }
                    bytesRead += result;
                }
                if (streamClosed) {
                    byte[] smaller = new byte[bytesRead];
                    System.arraycopy(input, 0, smaller, 0, bytesRead);
                    input = smaller;
                }
                byte[] chunk = input;
                chunkCount = chunkCount + 1;
                try {
                    chunkHandler.handleChunk(chunk, chunkCount, noOfChunks);
                } catch (ChunkHandleException che) {
                    throw new ChunkableDataSourceException("Exception handling data sourced from ChunkingInputStream", che);
                }
            }
        } catch (IOException e) {
            throw new ChunkableDataSourceException(e.getMessage(), e);
        }
    }
}
