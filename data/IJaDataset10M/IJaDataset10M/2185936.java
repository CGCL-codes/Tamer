package net.sf.logdistiller.publishers;

import java.io.*;
import org.apache.commons.io.IOUtils;
import net.sf.logdistiller.*;

/**
 * Save report of a LogDistillation in a file.
 * <p>
 * Parameters:
 * <ul>
 * <li><code>filename</code> (default: <code>report.[ext]</code> for global report, or
 * <code><i>[group id].[ext]</i></code> for group report): the file name (relative to output directory)
 * </ul>
 */
public class FilePublisher extends Publisher {

    public String getId() {
        return "file";
    }

    public void publish(LogDistillation logdistillation, LogDistiller.Report report) throws IOException {
        ReportFormat format = ReportFormats.getReportFormat(report.getFormat());
        String filename = report.getParam("filename", "result." + format.getFileExtension());
        Writer out = new FileWriter(logdistillation.newDestinationFile(filename));
        try {
            format.report(logdistillation, out);
        } finally {
            IOUtils.closeQuietly(out);
        }
    }

    public void publish(LogDistillation.Group group, LogDistiller.Report report) throws IOException {
        ReportFormat format = ReportFormats.getReportFormat(report.getFormat());
        String filename = report.getParam("filename");
        if (filename == null) {
            filename = group.getDefinition().getId() + "." + format.getFileExtension();
        }
        Writer out = new FileWriter(group.getLogdistillation().newDestinationFile(filename));
        try {
            format.report(group, out);
        } finally {
            IOUtils.closeQuietly(out);
        }
    }
}
