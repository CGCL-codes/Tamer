package net.urlgrey.mythpodcaster.transcode;

import java.io.File;
import java.util.List;
import net.urlgrey.mythpodcaster.dao.MythRecordingsDAO;
import org.springframework.beans.factory.annotation.Required;

/**
 * @author scott
 *
 */
public class ClipLocatorImpl implements ClipLocator {

    private static final String PNG_EXTENSION = ".png";

    private MythRecordingsDAO recordingsDao;

    /**
	 * @param filename
	 * @return
	 */
    public File locateOriginalClip(String filename) {
        final List<String> recordingDirectories = recordingsDao.findRecordingDirectories();
        if (recordingDirectories == null || recordingDirectories.size() == 0) {
            return null;
        }
        for (String directory : recordingDirectories) {
            File clipLocation = new File(directory, filename);
            if (clipLocation.canRead()) {
                return clipLocation;
            }
        }
        return null;
    }

    @Override
    public File locateThumbnailForOriginalClip(String filename) {
        final File clipFile = this.locateOriginalClip(filename);
        if (clipFile != null) {
            final File clipThumbnailFile = new File(clipFile.getParentFile(), clipFile.getName() + PNG_EXTENSION);
            if (clipThumbnailFile.canRead()) {
                return clipThumbnailFile;
            }
        }
        return null;
    }

    @Required
    public void setRecordingsDao(MythRecordingsDAO recordingsDao) {
        this.recordingsDao = recordingsDao;
    }
}
