package com.once.server.data.export.sql;

import java.io.File;
import java.io.FilenameFilter;
import org.apache.log4j.Logger;
import com.once.server.config.ConfigManager;

public class ReportsCompileThread extends Thread {

    private static final Logger m_logger = Logger.getLogger(ReportsCompileThread.class);

    public void run() {
        if (m_logger.isInfoEnabled()) {
            m_logger.info("Starting report templates compilation...");
        }
        processDirectory(ConfigManager.getInstance().getTemplatesRepository());
        if (m_logger.isInfoEnabled()) {
            m_logger.info("Report templates compilation complete.");
        }
    }

    private void processDirectory(String path) {
        deleteOldCompiledReports(path);
        File directory = new File(path);
        File[] child = directory.listFiles();
        for (int i = 0; i < child.length; i++) {
            if (child[i].isDirectory()) {
                processDirectory(child[i].getAbsolutePath());
            } else if (child[i].getAbsolutePath().toLowerCase().endsWith(SQLExportJasperReports.REPORT_TEMPLATE_EXTENSION)) {
                if (m_logger.isInfoEnabled()) {
                    m_logger.info("Compiling " + child[i].getAbsolutePath().substring(ConfigManager.getInstance().getTemplatesRepository().length()).replaceAll("\\\\", "/") + " ...");
                }
                try {
                    File report = new SQLExportJRv2Compiler().compile(child[i]);
                    if (report != null) {
                        if (m_logger.isInfoEnabled()) {
                            m_logger.info("Success");
                        }
                    }
                } catch (Throwable t) {
                    m_logger.error("Unable to compile template", t);
                }
            }
        }
    }

    private void deleteOldCompiledReports(String path) {
        File directory = new File(path);
        File[] child = directory.listFiles(new CompiledReportsFilter());
        for (int i = 0; i < child.length; i++) {
            if (m_logger.isInfoEnabled()) {
                m_logger.info("Deleteing " + child[i].getAbsolutePath().substring(ConfigManager.getInstance().getTemplatesRepository().length()).replaceAll("\\\\", "/") + " ...");
            }
            try {
                child[i].delete();
            } catch (SecurityException ex) {
                if (m_logger.isDebugEnabled()) {
                    m_logger.debug("Unable to delete old report compiled file " + child[i].getAbsolutePath() + "\".", ex);
                } else if (m_logger.isInfoEnabled()) {
                    m_logger.info("Unable to delete old report compiled file \"" + child[i].getAbsolutePath() + "\". (Reason: " + ex.getMessage() + ")");
                }
            }
        }
    }

    class CompiledReportsFilter implements FilenameFilter {

        public boolean accept(File file, String name) {
            return file.isFile() && file.getName().toLowerCase().endsWith(SQLExportJRv2Compiler.COMPILED_REPORT_EXTENSION);
        }
    }
}
