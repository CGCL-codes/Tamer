package com.vayoodoot.bengine;

import org.apache.log4j.Logger;
import java.io.File;
import java.io.IOException;
import java.io.FileFilter;
import com.vayoodoot.cache.DirectoryPreviewManager;
import com.vayoodoot.security.SecureDirectoryListingFilter;

/**
 * Created by IntelliJ IDEA.
 * User: Sachin Shetty
 * Date: Jul 15, 2007
 * Time: 12:29:28 PM
 * To change this template use File | Settings | File Templates.
 */
public class DirectoryPreviewJob implements BackgroundJob {

    private String directoryName;

    private static Logger logger = Logger.getLogger(DirectoryPreviewJob.class);

    public DirectoryPreviewJob(String directoryName) {
        this.directoryName = directoryName;
    }

    private void processDir(File dir) {
        logger.debug("Processing dir: " + dir.getAbsolutePath());
        File files[] = dir.listFiles((FileFilter) SecureDirectoryListingFilter.getSecureDirectoryListingFilter());
        try {
            DirectoryPreviewManager.createPreviewFile(dir.getAbsolutePath());
            if (files != null) {
                for (int i = 0; i < files.length; i++) {
                    if (files[i].isDirectory()) {
                        processDir(files[i]);
                    }
                }
            }
        } catch (IOException ie) {
            logger.fatal("Exception in creating preview file" + ie, ie);
        }
        logger.info("Finished dir: " + dir.getAbsolutePath());
    }

    public void run() {
        logger.info("Running DirectoryPreviewJob for: " + directoryName);
        File dir = new File(directoryName);
        processDir(dir);
        logger.info("Finished DirectoryPreviewJob for: " + directoryName);
    }

    public static void main(String args[]) throws Exception {
        DirectoryPreviewJob directoryPreviewJob = new DirectoryPreviewJob("C:\\SHARE1\\bikes\\bike");
        directoryPreviewJob.run();
    }
}
