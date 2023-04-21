package hu.ihash.web.server.service;

import java.io.File;

/**
 * A common interface for sharing local files on a webserver.
 *
 * @author Gergely Kiss
 */
public interface ISharingService {

    /**
	 * Returns a unique path representing the shared file.
	 * 
	 * @param localFile
	 * @return
	 */
    String getSharedPath(File localFile);

    /**
	 * Returns the local file from the unique path.
	 * 
	 * @param sharedPath
	 * @return
	 */
    File getLocalFile(String sharedPath);
}
