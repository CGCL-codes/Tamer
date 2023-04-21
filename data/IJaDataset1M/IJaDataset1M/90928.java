package org.hardtokenmgmt.ws.server.statistics;

import java.io.File;
import org.apache.log4j.DailyRollingFileAppender;

/**
 * Special Daily Rolling Appender that only inserts the
 * header for new files, that doesn't already exists.
 * 
 * 
 * @author Philip Vendil 28 jun 2009
 *
 * @version $Id$
 */
public class StatisticsDailyRollingAppender extends DailyRollingFileAppender {

    /** 
	 * Special method checking if the file already exists and
	 * then chooses not to write the header again.
	 * @see org.apache.log4j.WriterAppender#writeHeader()
	 */
    @Override
    protected void writeHeader() {
        File logFile = new File(getFile());
        if (layout != null && this.qw != null && logFile.length() == 0) {
            String header = layout.getHeader();
            if (header != null) {
                this.qw.write(layout.getHeader());
            }
        }
    }
}
