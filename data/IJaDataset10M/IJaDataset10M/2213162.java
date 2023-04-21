package net.sourceforge.cruisecontrol.servlet;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import net.sourceforge.cruisecontrol.LogFile;

/**
 * Streams raw log files.
 * @author <a href="mailto:hak@2mba.dk">Hack Kampbjorn</a>
 */
public class LogFileServlet extends FileServlet {

    /** Creates a new instance of LogFileServlet. */
    public LogFileServlet() {
    }

    protected File getRootDir(ServletConfig servletconfig) throws ServletException {
        File rootDirectory = getLogDir(servletconfig);
        if (rootDirectory == null) {
            String message = "LogFileServlet not configured correctly in web.xml.\n" + "logDir must point to existing directory.\n" + "logDir is currently set to <" + getLogDirParameter(servletconfig) + ">";
            throw new ServletException(message);
        }
        return rootDirectory;
    }

    protected String getMimeType(String filename) {
        return "application/xml";
    }

    LogFile getLogFile(final String subFilePath) {
        int logIndex = subFilePath.lastIndexOf('/');
        String project = subFilePath.substring(0, logIndex);
        String logName = subFilePath.substring(logIndex + 1);
        return new LogFile(new File(getRootDir(), project), logName);
    }

    protected WebFile getSubWebFile(final String subFilePath) {
        return new LogWebFile(getLogFile(subFilePath));
    }

    private static class LogWebFile extends WebFile {

        private LogFile logfile;

        public LogWebFile(LogFile file) {
            super(file.getFile());
            logfile = file;
        }

        protected InputStream getInputStream() throws IOException {
            return logfile.getInputStream();
        }
    }
}
