package dsb.bar.flowclient.monitor;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import org.apache.commons.lang.Validate;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;
import org.apache.log4j.Logger;
import dsb.bar.flowclient.monitor.FlowclientDataReader;
import dsb.bar.flowclient.monitor.FlowclientDataVerifier;

/**
 * Implementation of the {@link FlowmeterDataReader} interface.
 */
public class FlowclientDataReader {

    private static final Logger logger = Logger.getLogger(FlowclientDataReader.class);

    /** The stream to read from. */
    private InputStream stream;

    private FlowclientDataVerifier verifier;

    public FlowclientDataReader() {
        verifier = new FlowclientDataVerifier();
    }

    public void setInputStream(InputStream stream) {
        Validate.notNull(stream, "stream should not be null");
        this.stream = new BufferedInputStream(stream, 58);
    }

    /**
	 * Read 58 characters from an input stream. The actual information string
	 * which is send by the serial port is 29 characters long. Thus the 58
	 * characters include one complete information string.
	 * 
	 * @return A {@link String} containing the 58 characters.
	 */
    public String readData() {
        Validate.notNull(this.stream, "this.stream should not be null");
        String data = null;
        try {
            final byte[] buffer = new byte[29 * 2];
            while (true) {
                if (stream.available() > 0) {
                    final int n = this.stream.read(buffer);
                    if (n == 29 * 2) {
                        data = new String(buffer);
                        try {
                            this.verifier.verify(data);
                        } catch (IllegalArgumentException e) {
                            this.logger.trace("Verifier disapproved data: " + data);
                            continue;
                        }
                        break;
                    }
                }
            }
            logger.trace("Got data (" + data.length() + " chars): " + data);
        } catch (IOException e) {
            throw new RuntimeException("Could not read from serial port input stream", e);
        }
        return data.toString();
    }

    public String toString() {
        ToStringBuilder builder = new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE);
        builder.append("stream", stream);
        return builder.toString();
    }
}
