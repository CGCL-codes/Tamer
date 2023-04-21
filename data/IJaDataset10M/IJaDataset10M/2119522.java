package com.once.scheduler.task;

import java.io.File;
import org.apache.log4j.Logger;
import com.once.scheduler.IScheduleTask;
import com.once.server.config.ConfigManager;

public class ExportsCleanup implements IScheduleTask {

    private static final Logger m_logger = Logger.getLogger(ExportsCleanup.class);

    private int m_deletedFilesCount;

    public boolean wasSuccessful() {
        return (true);
    }

    public String getName() {
        return ("ExportsCleanup");
    }

    public String getDescription() {
        return "Export files cleanup";
    }

    public void run() {
        if (m_logger.isInfoEnabled()) {
            m_logger.info("Cleaning up old exported files...");
        }
        m_deletedFilesCount = 0;
        cleanDirectory(ConfigManager.getInstance().getExportsRepository(), false);
        if (m_logger.isInfoEnabled()) {
            m_logger.info("Export cleanup complete (" + m_deletedFilesCount + " file" + ((m_deletedFilesCount != 1) ? "s" : "") + " deleted).");
        }
    }

    public void cleanDirectory(String path, boolean deleteSelf) {
        File dir = new File(path);
        if (!dir.exists() || !dir.isDirectory()) {
            return;
        }
        String[] files = dir.list();
        for (int i = 0; i < files.length; i++) {
            File file = new File(path + "/" + files[i]);
            if (file.isDirectory()) {
                cleanDirectory(file.getAbsolutePath(), true);
            }
            try {
                file.delete();
                m_deletedFilesCount++;
                m_logger.info("Export file \"" + file.getAbsolutePath() + "\" was deleted.");
            } catch (SecurityException except) {
                if (m_logger.isDebugEnabled() == true) {
                    m_logger.debug("Unable to delete the export file " + file.getAbsolutePath() + "\".", except);
                } else {
                    if (m_logger.isInfoEnabled() == true) {
                        m_logger.info("Unable to delete the export file \"" + file.getAbsolutePath() + "\". (Reason: " + except.getMessage() + ")");
                    }
                }
            }
        }
        if (deleteSelf) {
            try {
                dir.delete();
                m_logger.info("Export directory \"" + dir.getAbsolutePath() + "\" was deleted.");
            } catch (SecurityException except) {
                if (m_logger.isDebugEnabled() == true) {
                    m_logger.debug("Unable to delete the export directory " + dir.getAbsolutePath() + "\".", except);
                } else {
                    if (m_logger.isInfoEnabled() == true) {
                        m_logger.info("Unable to delete the export directory \"" + dir.getAbsolutePath() + "\". (Reason: " + except.getMessage() + ")");
                    }
                }
            }
        }
    }
}
