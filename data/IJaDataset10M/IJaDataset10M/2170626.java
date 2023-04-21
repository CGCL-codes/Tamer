package org.xsocket;

import java.io.IOException;
import java.nio.BufferUnderflowException;
import java.nio.ByteBuffer;
import java.nio.channels.WritableByteChannel;
import java.util.LinkedList;

/**
 * Helper class to parse a ByteBuffer queue for a delimiter and 
 * to extract data based on a length field or the delimiter 
 * 
 * @author grro@xsocket.org
 */
final class ByteBufferParser {

    private static final WritableByteChannel DEV0 = new WritableByteChannel() {

        public boolean isOpen() {
            return true;
        }

        ;

        public int write(ByteBuffer buffer) throws IOException {
            return buffer.remaining();
        }

        ;

        public void close() throws IOException {
        }

        ;
    };

    /**
	 * returns a index, which gives the position 
	 * of a record by using the delimiter
	 * 
	 * @param bufferQueue the queue
	 * @param delimiter the delimiter
	 * 
	 * @return the index 
	 */
    public Index find(LinkedList<ByteBuffer> bufferQueue, String delimiter) {
        return find(bufferQueue, new Index(delimiter));
    }

    /**
	 * returns a index, which gives the position
	 * of a record by using the delimiter
	 * 
	 * @param bufferQueue the queue
	 * @param index index the index
	 * 
	 * @return the index 
	 */
    public Index find(LinkedList<ByteBuffer> bufferQueue, Index index) {
        int queueSize = bufferQueue.size();
        for (int bufNr = index.scannedBuffers; (bufNr < queueSize) && (!index.hasDelimiterFound); bufNr++) {
            ByteBuffer buffer = bufferQueue.get(bufNr);
            int savedPos = buffer.position();
            int savedLimit = buffer.limit();
            bufferLoop: for (int pos = 0; (buffer.hasRemaining() && !index.hasDelimiterFound); pos++) {
                byte b = buffer.get();
                index.readBytes++;
                if (index.delimiterPos > 0) {
                    if (b == index.delimiterBytes[index.delimiterPos]) {
                        if ((index.delimiterPos + 1) == index.delimiterLength) {
                            index.hasDelimiterFound = true;
                            break bufferLoop;
                        }
                        index.delimiterPos++;
                    } else {
                        index.delimiterPos = 0;
                    }
                } else if (index.delimiterPos == 0) {
                    if (b == index.delimiterBytes[index.delimiterPos]) {
                        index.delimiterPos++;
                        if (index.delimiterLength == 1) {
                            index.hasDelimiterFound = true;
                            break bufferLoop;
                        }
                    }
                }
            }
            index.scannedBuffers++;
            buffer.position(savedPos);
            buffer.limit(savedLimit);
        }
        return index;
    }

    /**
	 * extracts the record from the given buffer by using a length field 
	 * 
	 * @param inOutBuffer  the buffer, which contains the data. The extracted data will be removed 
	 * @param length        the length to read
	 * @param outChannel    the channel to write the available bytes 
 	 * @throws BufferUnderflowException if the delimiter has not been found 
	 * @throws IOException If some other I/O error occurs 
	 */
    public void extract(LinkedList<ByteBuffer> inOutBuffer, int length, WritableByteChannel outChannel) throws IOException, BufferUnderflowException {
        int remainingToExtract = length;
        ByteBuffer buffer = null;
        do {
            buffer = inOutBuffer.remove();
            if (buffer == null) {
                throw new BufferUnderflowException();
            }
            int bufLength = buffer.limit() - buffer.position();
            if (remainingToExtract >= bufLength) {
                outChannel.write(buffer);
                remainingToExtract -= bufLength;
            } else {
                int savedLimit = buffer.limit();
                buffer.limit(buffer.position() + remainingToExtract);
                ByteBuffer leftPart = buffer.slice();
                outChannel.write(leftPart);
                buffer.position(buffer.limit());
                buffer.limit(savedLimit);
                ByteBuffer rightPart = buffer.slice();
                inOutBuffer.addFirst(rightPart);
                return;
            }
        } while (remainingToExtract > 0);
    }

    /**
	 * extracts the record from the given buffer by using the index
	 * 
	 * @param inOutBuffer   the buffer, which contains the data. The extracted data will be removed 
	 * @param index         the index
	 * @param outChannel    the channel to write the available bytes 
	 * @throws IOException If some other I/O error occurs
 
	 */
    public void extract(LinkedList<ByteBuffer> inOutBuffer, Index index, WritableByteChannel outChannel) throws IOException {
        assert (index.isValid) : "Index is invalid";
        assert (index.hasDelimiterFound());
        extract(inOutBuffer, index.getReadBytes() - index.getDelimiterLength(), outChannel);
        extract(inOutBuffer, index.getDelimiterLength(), ByteBufferParser.DEV0);
    }

    /**
	 * the index to mark the position of the delimiter 
	 *
	 */
    static final class Index {

        public static final int NULL = -1;

        private boolean isValid = true;

        private boolean hasDelimiterFound = false;

        private String delimiter = null;

        private byte[] delimiterBytes = null;

        private int delimiterLength = 0;

        private int delimiterPos = 0;

        private int scannedBuffers = 0;

        private int readBytes = 0;

        Index(String delimiter) {
            this.delimiter = delimiter;
            delimiterBytes = delimiter.getBytes();
            this.delimiterLength = delimiterBytes.length;
        }

        boolean hasDelimiterFound() {
            return hasDelimiterFound;
        }

        int getReadBytes() {
            return readBytes;
        }

        String getDelimiter() {
            return delimiter;
        }

        int getDelimiterLength() {
            return delimiterLength;
        }

        int getDelimiterPos() {
            return delimiterPos;
        }
    }
}
