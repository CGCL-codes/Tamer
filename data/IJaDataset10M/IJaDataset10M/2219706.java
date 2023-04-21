package org.jopera.adapter.xwch.file;

import java.io.File;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
import org.jopera.adapter.xwch.XWCHConnection;
import ch.ethz.jopera.kernel.IJob;
import ch.ethz.jopera.kernel.State;
import ch.ethz.jopera.kernel.dispatcher.ISubSystem;

/**
 * This class models a component that uploads a file to the XWCH warehouse.
 *
 * @author Mark Pruneri <mark.pruneri@usi.ch>
 *
 */
public class XWCH_FILE_UPLOADSubSystem implements ISubSystem {

    private static final org.apache.log4j.Logger log = Logger.getLogger(XWCH_FILE_UPLOADSubSystem.class);

    private XWCHConnection connection;

    private String filename;

    public final void Execute(final IJob job) {
        PropertyConfigurator.configure("/Users/mark/Desktop/workspace/org.jopera.adapter.xwch/log4j.properties");
        filename = (String) job.getActiveCommand().get("FILE");
        try {
            if (filename == null) {
                log.error("Given filename is null.");
                throw new IllegalArgumentException("Given filename is null.");
            } else if (filename.length() == 0) {
                log.warn("Given filename not specified (empty).");
            } else {
                log.debug("File to upload: " + filename);
            }
            final File testFile = new File(filename);
            if (!testFile.exists()) {
                log.error("Given filename '" + filename + "' not found.");
                throw new IllegalArgumentException("Given filename '" + filename + "' not found.");
            }
            if (!testFile.isFile()) {
                log.error("Given filename '" + filename + "' is not a file.");
                throw new IllegalArgumentException("Given filename '" + filename + "' is not a file.");
            }
            connection = new XWCHConnection();
            log.debug("Trying to upload to XWCH warehouse file: " + filename);
            job.getSystemOutput().put("Status", "Trying to upload to XWCH warehouse file: " + filename);
            final String fileId = connection.uploadFile(filename);
            job.getSystemOutput().put("Status", "Uploaded '" + filename + "' to XWCH warehouse.");
            job.getSystemOutput().put("FILE_ID", fileId);
            log.info("Uploaded to XWCH warehouse file: " + filename + " . File id: " + fileId);
            job.setState(State.FINISHED);
        } catch (final Exception ex) {
            log.error(ex.getMessage(), ex);
            job.setState(State.FAILED);
            job.getSystemOutput().put("Status", "Could not upload to XWCH warehouse file: " + filename);
            job.getSystemOutput().put("Error", ex.getMessage());
        } finally {
            job.notifyFinished();
        }
    }

    public final State Signal(final int signal) {
        return State.RUNNING;
    }
}
