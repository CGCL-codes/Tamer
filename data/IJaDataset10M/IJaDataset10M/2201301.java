package dsb.bar.flowmeter.monitor.impl;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import javax.inject.Inject;
import org.apache.commons.lang.Validate;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;
import org.slf4j.Logger;
import dsb.bar.flowmeter.monitor.FlowmeterDataReader;
import dsb.bar.flowmeter.monitor.FlowmeterDataVerifier;

/**
 * Implementation of the {@link FlowmeterDataReader} interface.
 */
public class FlowmeterDataReaderImpl implements FlowmeterDataReader {

    @Inject
    private Logger logger;

    /** The stream to read from. */
    private InputStream stream;

    @Inject
    private FlowmeterDataVerifier verifier;

    @Override
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
    @Override
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

    @Override
    public String toString() {
        ToStringBuilder builder = new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE);
        builder.append("stream", stream);
        return builder.toString();
    }
}
