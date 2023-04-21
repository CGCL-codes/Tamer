package com.smartwish.documentburster.polling;

import java.io.File;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.sadun.util.polling.BasePollManager;
import org.sadun.util.polling.FileMovedEvent;
import com.smartwish.documentburster.job.CliJob;

public class NewFileEventHandler extends BasePollManager {

    private static Log log = LogFactory.getLog(NewFileEventHandler.class);

    private CliJob cliJob;

    public NewFileEventHandler(CliJob job) {
        this.cliJob = job;
    }

    public void fileMoved(FileMovedEvent evt) {
        String filePath = evt.getPath().getAbsolutePath();
        log.debug("evt.getPath().getAbsolutePath() = " + filePath);
        try {
            cliJob.doBurst(filePath);
        } catch (Exception e) {
            log.fatal("Error bursting file '" + filePath + "'", e);
        } finally {
            File receivedFile = new File(filePath);
            if ((receivedFile.exists()) && (!receivedFile.delete())) log.warn("Could not delete received file ' " + filePath + "'!");
        }
    }
}
