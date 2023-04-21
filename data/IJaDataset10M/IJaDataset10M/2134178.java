package org.globus.ftp.dc;

import java.io.DataOutputStream;
import java.io.OutputStream;
import java.io.IOException;
import org.globus.ftp.Buffer;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class EBlockImageDCWriter extends EBlockAware implements DataChannelWriter {

    static Log logger = LogFactory.getLog(EBlockImageDCWriter.class.getName());

    protected DataOutputStream output;

    public void setDataStream(OutputStream out) {
        output = new DataOutputStream(out);
    }

    public void write(Buffer buf) throws IOException {
        long offset = buf.getOffset();
        if (offset < 0) {
            throw new IOException("Invalid offset: " + offset);
        }
        output.writeByte(0);
        logger.debug("buffer length: " + buf.getLength());
        output.writeLong(buf.getLength());
        logger.debug("offset: " + offset);
        output.writeLong(offset);
        output.write(buf.getBuffer(), 0, buf.getLength());
        logger.debug("wrote the buffer");
    }

    public void endOfData() throws IOException {
        byte desc;
        synchronized (context) {
            if (context.eodsTransferred == 0) {
                desc = EOF | EOD;
                output.writeByte(desc);
                output.writeLong(0);
                output.writeLong(context.eodsTotal);
                logger.debug("wrote EOF (expected EODS: " + context.eodsTotal + ") and EOD");
            } else {
                desc = EOD;
                output.writeByte(desc);
                output.writeLong(0);
                output.writeLong(0);
                logger.debug("wrote EOD");
            }
            context.eodTransferred();
        }
        output.flush();
    }

    public static void close(DataOutputStream myOutput) throws IOException {
        byte desc;
        desc = WILL_CLOSE;
        myOutput.writeByte(desc);
        myOutput.writeLong(0);
        myOutput.writeLong(0);
        logger.debug("Wrote WILL_CLOSE, closing the socket");
        myOutput.close();
    }

    public void close() throws IOException {
        close(output);
    }
}
