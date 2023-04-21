package org.rivalry.core.reporter;

import java.io.File;

/**
 * Defines methods required by a reporter.
 */
public interface Reporter {

    /**
     * Write a report.
     */
    void writeReport();

    /**
     * Write the report to a file.
     * 
     * @param file File.
     */
    void writeToFile(final File file);
}
