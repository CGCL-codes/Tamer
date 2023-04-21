package ikube.action;

import ikube.IConstants;
import ikube.model.IndexContext;
import ikube.toolkit.FileUtilities;
import java.io.File;
import org.apache.commons.io.FileSystemUtils;

/**
 * This action checks that the disk is not full, the one where the indexes are, if it is then this instance will close down.
 * 
 * @author Michael Couck
 * @since 02.06.11
 * @version 01.00
 */
public class DiskFull extends Action<IndexContext<?>, Boolean> {

    /** The minimum space that we will accept to carry on, 10 gig. */
    private static final long MINIMUM_FREE_SPACE = 10000;

    /**
	 * {@inheritDoc}
	 */
    @Override
    public boolean executeInternal(final IndexContext<?> indexContext) {
        ikube.model.Action action = null;
        try {
            action = start(indexContext.getIndexName(), "");
            File indexesDirectory = FileUtilities.getFile(indexContext.getIndexDirectoryPath(), Boolean.TRUE);
            if (!indexesDirectory.exists() || !indexesDirectory.isDirectory()) {
                return Boolean.FALSE;
            }
            String drive = null;
            String directoryPath = indexesDirectory.getAbsolutePath();
            if (directoryPath.startsWith(IConstants.SEP + IConstants.SEP) || directoryPath.startsWith(IConstants.BCK_SEP + IConstants.BCK_SEP)) {
            } else if (directoryPath.startsWith(IConstants.SEP)) {
                drive = IConstants.SEP;
            } else {
                char driveCharacter = indexesDirectory.getAbsolutePath().charAt(0);
                drive = driveCharacter + ":";
            }
            try {
                if (drive != null) {
                    long freeSpaceKilobytes = FileSystemUtils.freeSpaceKb(drive);
                    long freeSpaceMegabytes = freeSpaceKilobytes / 1000;
                    logger.debug("Free space : " + freeSpaceMegabytes + ", " + MINIMUM_FREE_SPACE);
                    if (freeSpaceMegabytes < MINIMUM_FREE_SPACE) {
                        String subject = "No more disk space on server!";
                        String body = "We have run out of disk space on this driver : " + indexesDirectory;
                        body += "This server will exit to save the machine : " + freeSpaceMegabytes;
                        logger.error(subject + " " + body);
                        sendNotification(subject, body);
                        return Boolean.TRUE;
                    }
                }
            } catch (Exception e) {
                logger.error("Exception looking for the free space : " + indexesDirectory, e);
            }
        } finally {
            stop(action);
        }
        return Boolean.FALSE;
    }
}
