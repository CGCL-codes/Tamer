package rbnb;

import java.io.IOException;
import java.io.InputStream;
import java.util.LinkedList;

/**
 * HTTPInputStream.java (  RBNB )
 * Created: Jan 21, 2010
 * @author Michael Nekrasov
 * 
 * Description:	A wrapper for an Input Stream that provides
 * 				useful methods for reading the packet as both a 
 * 				string and byte array
 *
 */
public class HTTPInputStream extends InputStream {

    private InputStream is;

    private LinkedList<Integer> buffer = new LinkedList<Integer>();

    private boolean bufferInput = false;

    private long bytesLastRead = 0;

    /**
	 * Create a new HTTP Wrapper for given Input Stream
	 * @param is Input Stream for HTTP connection
	 * @throws IOException
	 */
    public HTTPInputStream(InputStream is) throws IOException {
        this.is = is;
    }

    /** 
	 * Start recording bytes read
	 */
    public void startBuffering() {
        bufferInput = true;
        buffer.clear();
    }

    /**
	 * Stop recording bytes read
	 */
    public void stopBuffering() {
        bufferInput = false;
    }

    /**
	 * Returns the Buffer, clearing it
	 * @return the buffer
	 */
    public byte[] getBufferContent() {
        byte[] result = new byte[buffer.size()];
        int i = 0;
        while (!buffer.isEmpty()) {
            result[i] = buffer.removeFirst().byteValue();
            i++;
        }
        return result;
    }

    /**
	 * Read in a byte of data from the stream
	 * @return byte read
	 */
    @Override
    public int read() throws IOException {
        int byteRead = is.read();
        if (bufferInput) buffer.add(byteRead);
        return byteRead;
    }

    /**
	 * Read in an entire line of characters
	 * @return the string read
	 * @throws IOException
	 */
    public String readLine() throws IOException {
        String line = "";
        bytesLastRead = 1;
        int c = read();
        if (c == -1) return null;
        while (c != -1 && c != (int) '\n') {
            if (c != '\r') line += (char) c;
            c = read();
            bytesLastRead++;
        }
        return line;
    }

    /**
	 * Remove the last line read from the buffer
	 */
    public void undoLastRead() {
        while (bytesLastRead > 0) {
            buffer.removeLast();
            bytesLastRead--;
        }
    }
}
