package net.sf.javadc.tasks.client;

import java.io.IOException;
import net.sf.javadc.config.ConnectionSettings;
import net.sf.javadc.net.client.ConnectionState;
import net.sf.javadc.net.client.ConnectionStatistics;
import net.sf.javadc.tasks.BaseClientTask;
import net.sf.javadc.util.ExtendedBufferedOutputStream;
import org.apache.log4j.Category;

/**
 * @author tw70794
 */
public class SUploadingTask extends BaseClientTask {

    private static final Category logger = Category.getInstance(SUploadingTask.class);

    private ExtendedBufferedOutputStream writer = null;

    private ConnectionStatistics stats;

    /**
     * Create a <code>SUploadingTask</code> instance
     */
    public SUploadingTask() {
    }

    /**
     * Continue the upload
     * 
     * @param size amount of bytes to be uploaded
     */
    private final void continueUpload(int size) {
        logger.debug("Loading buffer with:" + size + "bytes");
        final byte[] buffer = new byte[size];
        try {
            clientConnection.getLocalFile().read(buffer);
            writer.write(buffer);
        } catch (IOException io) {
            logger.error(io.toString());
            clientConnection.disconnect();
        }
        stats.setBytesReceived(stats.getBytesReceived() + size);
        clientConnection.updateConnectionInfo();
    }

    /**
     * Finish the upload
     */
    private final void finishUpload() {
        logger.debug("Transfer done, cleaning up!");
        clientConnection.closeFile();
        if (writer.bytesInBuffer() == 0) {
            try {
                writer.flush();
            } catch (IOException io) {
                logger.error(io.toString());
            }
        }
        logger.debug("Going into idle!");
        clientConnection.setState(ConnectionState.UPLOAD_FINISHED);
    }

    @Override
    protected final void runTaskTemplate() {
        writer = clientConnection.getWriter();
        stats = clientConnection.getStatistics();
        logger.debug("Free in buffer: " + writer.nonBlockingCapacity());
        int size = (int) Math.min(ConnectionSettings.UPLOAD_BLOCK_SIZE, stats.getFileLength() - stats.getBytesReceived());
        logger.debug("File length was:" + stats.getFileLength());
        logger.debug("Bytes received:" + stats.getBytesReceived());
        if (size > 0) {
            continueUpload(size);
        }
        if (stats.getBytesReceived() == stats.getFileLength()) {
            finishUpload();
        }
    }
}
