package tools.archive;

import java.io.File;
import java.util.ArrayList;
import java.util.logging.Level;
import myComponents.MyUsefulFunctions;
import tools.MySeriesLogger;

/**
 *
 * @author ssoldatos
 */
public abstract class AbstractArchiveFile implements ArchiveConstants {

    ArrayList<String> extractedFiles = new ArrayList<String>();

    File archivedFile;

    boolean result = false;

    boolean isValidType(String entryName, int type) {
        MySeriesLogger.logger.log(Level.INFO, "Check if {0} is valid type", entryName);
        switch(type) {
            case NONE:
                MySeriesLogger.logger.log(Level.INFO, "Type none");
                return false;
            case ALL:
                MySeriesLogger.logger.log(Level.INFO, "Type all");
                return true;
            case SUBTITLES:
                MySeriesLogger.logger.log(Level.INFO, "Type subtitle");
                return MyUsefulFunctions.isSubtitle(entryName);
        }
        return false;
    }

    abstract void unzip(String directory, int type) throws Exception;

    abstract ArrayList<String> getEntries(int type) throws Exception;
}
