package net.sf.uitags.js;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import net.sf.uitags.util.ArrayUtils;

final class JsFiles {

    private static final Log log = LogFactory.getLog(JsFiles.class);

    private static final String DEBUG_LEVEL_FILE = "util/excludes/initDebug.js";

    private static final String DEBUG_PANEL_FILE = "html/excludes/DebugPanel.js";

    private static final List EMPTY_LIST = new ArrayList();

    private Suites suites;

    private FileFinder fileFinder;

    private List customFileNames = EMPTY_LIST;

    private boolean inDebugMode = false;

    /**
   * Creates an instance which is <b>not</b> in debug mode by default.
   */
    JsFiles(Suites suites, FileFinder fileFinder) {
        this.suites = suites;
        this.fileFinder = fileFinder;
    }

    void setCustomFileNames(String customFileNames) {
        if (customFileNames == null) {
            this.customFileNames = EMPTY_LIST;
        } else {
            String[] fileNamesAsArray = ArrayUtils.toArrayOfTrimmed(customFileNames);
            this.customFileNames = Arrays.asList(fileNamesAsArray);
        }
    }

    void setInDebugMode(boolean inDebugMode) {
        this.inDebugMode = inDebugMode;
    }

    String getContents() throws IOException {
        StringBuffer buffer = new StringBuffer();
        addDebugLevelIfNecessary(buffer);
        addContentsFromSuites(buffer);
        addCustomContents(buffer);
        addDebugPanelIfNecessary(buffer);
        return buffer.toString();
    }

    /**
   * Returns the latest of all files' last modified times. To maintain
   * consistency with J2SE API, 0 is returned if the time is unknown (Servlet
   * API returns -1 if unknown).
   */
    long getLastModified() {
        return Math.max(getTimestampFromDebugFile(), Math.max(getLatestTimestampFromFilesInSuites(), getLatestTimestampFromCustomJsFiles()));
    }

    private long getTimestampFromDebugFile() {
        if (this.fileFinder.supportsFilesNotFromSuites() && this.inDebugMode) {
            try {
                return this.fileFinder.getLastModified(DEBUG_LEVEL_FILE);
            } catch (IOException e) {
                return 0;
            }
        }
        return 0;
    }

    private long getLatestTimestampFromFilesInSuites() {
        long latestTimestamp = 0;
        for (Iterator i = this.suites.getFileNames().iterator(); i.hasNext(); ) {
            String fileName = (String) i.next();
            try {
                latestTimestamp = Math.max(this.fileFinder.getLastModified(fileName), latestTimestamp);
            } catch (IOException e) {
                if (log.isWarnEnabled()) {
                    log.warn("Error trying to get last modified of " + fileName + ".");
                }
            }
        }
        return latestTimestamp;
    }

    private long getLatestTimestampFromCustomJsFiles() {
        if (this.fileFinder.supportsFilesNotFromSuites()) {
            long latestTimestamp = 0;
            for (Iterator i = this.customFileNames.iterator(); i.hasNext(); ) {
                String fileName = (String) i.next();
                try {
                    latestTimestamp = Math.max(this.fileFinder.getLastModified(fileName), latestTimestamp);
                } catch (IOException e) {
                    if (log.isWarnEnabled()) {
                        log.warn("Error trying to get last modified of " + fileName + ".");
                    }
                }
            }
            return latestTimestamp;
        }
        return 0;
    }

    private void addDebugLevelIfNecessary(StringBuffer buffer) throws IOException {
        if (this.fileFinder.supportsFilesNotFromSuites() && this.inDebugMode) {
            buffer.append(this.fileFinder.readContents(DEBUG_LEVEL_FILE));
        }
    }

    private void addDebugPanelIfNecessary(StringBuffer buffer) throws IOException {
        if (this.fileFinder.supportsFilesNotFromSuites() && this.inDebugMode) {
            buffer.append(this.fileFinder.readContents(DEBUG_PANEL_FILE));
        }
    }

    private void addContentsFromSuites(StringBuffer buffer) throws IOException {
        for (Iterator i = this.suites.getFileNames().iterator(); i.hasNext(); ) {
            String fileName = (String) i.next();
            buffer.append(this.fileFinder.readContents(fileName));
        }
    }

    private void addCustomContents(StringBuffer buffer) throws IOException {
        if (this.fileFinder.supportsFilesNotFromSuites()) {
            for (Iterator i = this.customFileNames.iterator(); i.hasNext(); ) {
                String fileName = (String) i.next();
                buffer.append(this.fileFinder.readContents(fileName));
            }
        }
    }
}
