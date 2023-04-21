package de.simpleworks.jmeter.visualizer.rstatd.writer;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import org.apache.jorphan.logging.LoggingManager;
import org.apache.log.Logger;
import de.simpleworks.jmeter.protocol.rstatd.common.RstatdStatsTime;
import de.simpleworks.jmeter.protocol.rstatd.common.UtilIO;

/**
 * @author Marcin Brzoza
 * @since Feb 24, 2008
 */
final class RstatdStatsTimeWriterObject extends RstatdStatsTimeWriterBase {

    private static final String BACKUP_EXTENTION = ".backup";

    private static final Logger logger = LoggingManager.getLoggerForClass();

    private ObjectOutputStream output;

    RstatdStatsTimeWriterObject(final File _file, final int _limit) {
        super(_file, _limit);
    }

    protected final void streamOpen(final File _file) throws IOException {
        if (logger.isDebugEnabled()) {
            logger.debug("open ObjectOutputStream for file: \"" + _file.getAbsolutePath() + "\".");
        }
        if (_file.exists()) {
            final String filename = _file.getAbsolutePath();
            final File backupFile = new File(filename + BACKUP_EXTENTION);
            _file.renameTo(backupFile);
            if (logger.isInfoEnabled()) {
                logger.info("moved old data file: \"" + filename + "\" to: \"" + backupFile.getAbsolutePath() + "\".");
            }
        }
        output = new ObjectOutputStream(new BufferedOutputStream(new FileOutputStream(_file, false)));
    }

    protected final void writeTimes(final RstatdStatsTime _times) throws IOException {
        if (logger.isDebugEnabled()) {
            logger.debug("write: \"" + _times + "\" to ObjectOutputStream.");
        }
        _times.writeExternal(output);
        output.flush();
    }

    protected final void streamClose() {
        if (logger.isDebugEnabled()) {
            logger.debug("close ObjectOutputStream.");
        }
        UtilIO.close(output);
        output = null;
    }
}
