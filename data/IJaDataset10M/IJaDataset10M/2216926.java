package taskgraph.tasks.io;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.InterruptedIOException;
import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import taskgraph.Config;
import taskgraph.ports.CharOutputPort;
import taskgraph.tasks.PrimitiveTask;

/**
 * Source task for reading a text file.
 *  
 * <p>The contents is put through to the char output port as-is,
 * without filtering out line separators or any other characters.
 * 
 * @author Armando Blancas
 * @see taskgraph.ports.CharOutputPort
 */
public class ReadTextFile extends PrimitiveTask {

    private static final String DEFAULT_ENCODING = Charset.defaultCharset().name();

    private static final int DEFAULT_CAPACITY = Config.get().bufferCapacity();

    private CharOutputPort output;

    private String filename;

    private String charsetName = DEFAULT_ENCODING;

    private int bufferSize = DEFAULT_CAPACITY;

    /**
	 * This constructor allows creating instances as beans.
	 */
    public ReadTextFile() {
    }

    /**
     * Creates a fully configured instance.
     * 
     * @param output The char output port.
     * @param filename The full path of the file to read as String.
     */
    public ReadTextFile(final CharOutputPort output, final String filename) {
        setOutput(output);
        setFilename(filename);
    }

    /**
     * Creates a fully configured instance.
     * 
     * @param output The char output port.
     * @param filename The full path of the file to read as String.
     * @param charsetName  The name of the character encoding as String.
	 * @throws UnsupportedEncodingException 
     */
    public ReadTextFile(final CharOutputPort output, final String filename, final String charsetName) throws UnsupportedEncodingException {
        setOutput(output);
        setFilename(filename);
        setCharsetName(charsetName);
    }

    /**
	 * Gets the buffer size for reading chunks of characters.
	 * 
	 * @return the bufferSize
	 */
    public int getBufferSize() {
        return bufferSize;
    }

    /**
	 * Gets the name of the character encoding.
	 * 
	 * @return The charsetName value as {@code String}.
	 */
    public String getCharsetName() {
        return charsetName;
    }

    /**
     * Gets the name of the file to read.
     * 
	 * @return The full path of the file as String
	 */
    public String getFilename() {
        return filename;
    }

    /**
     * Gets the output port.
     * 
	 * @return The char output port.
	 */
    public CharOutputPort getOutput() {
        return output;
    }

    /**
	 * Reads the input file and writes it to the output port.
	 *<pre>
	 *open text file
	 *while not EOF
	 *    read characters from file
	 *    write characters to char output port
	 *close text file
	 *close output port
	 *</pre>
	 */
    @Override
    public void run() {
        BufferedReader input = null;
        try {
            input = new BufferedReader(new InputStreamReader(new FileInputStream(filename), charsetName), bufferSize);
            char[] data = new char[bufferSize];
            while (!Thread.currentThread().isInterrupted()) {
                int count = input.read(data, 0, bufferSize);
                if (count == -1) {
                    break;
                }
                output.write(data, 0, count);
            }
        } catch (InterruptedIOException ie) {
        } catch (IOException ioe) {
            log.error("ReadTextFile#run()", ioe);
        } finally {
            try {
                if (input != null) input.close();
                output.close();
            } catch (IOException e) {
            }
        }
    }

    /**
	 * Sets the buffer size for reading chunks of characters.
	 * <p>The argument should not be less than the default. If it is, the
	 * default size is used instead.
	 * 
	 * @param bufferSize the bufferSize to set
	 */
    public void setBufferSize(final int bufferSize) {
        this.bufferSize = (bufferSize < DEFAULT_CAPACITY) ? DEFAULT_CAPACITY : bufferSize;
    }

    /**
	 * Sets the name of the character encoding.
	 * 
	 * @param charsetName The {@code String} value to set as charset name.
	 * @throws UnsupportedEncodingException 
	 */
    public void setCharsetName(final String charsetName) throws UnsupportedEncodingException {
        if (charsetName == null) throw new IllegalArgumentException("charsetName == null");
        if (!Charset.isSupported(charsetName)) {
            throw new UnsupportedEncodingException(charsetName);
        }
        this.charsetName = charsetName;
    }

    /**
	 * Sets the name of the file to read.
	 * @param filename The full path of the file as String.
	 */
    public void setFilename(final String filename) {
        if (filename == null) throw new IllegalArgumentException("filename == null");
        this.filename = filename;
    }

    /**
	 * Sets the output port.
	 * 
	 * @param output The char output port.
	 */
    public void setOutput(final CharOutputPort output) {
        if (output == null) throw new IllegalArgumentException("output == null");
        this.output = output;
    }
}
