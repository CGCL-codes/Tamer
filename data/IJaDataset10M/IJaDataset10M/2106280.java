package org.icenigrid.gridsam.core.plugin.connector.openpbs;

import java.io.IOException;
import org.apache.commons.vfs.FileObject;
import org.apache.commons.vfs.FileSystemException;
import org.icenigrid.gridsam.core.plugin.FileSystemProvider;
import org.icenigrid.gridsam.core.plugin.JobContext;
import org.icenigrid.gridsam.core.plugin.connector.data.VFSSupport;

/**
 * PBSFileSystemProvider for file system.
 * 
 * @author qiaojian<br>
 * Email: qiaojian@software.ict.ac.cn<br>
 * Update Date : 2006-12-30<br>
 * @version 1.0.1
 */
public class PBSFileSystemProvider implements FileSystemProvider {

    /**
	 * the root file system.
	 */
    private FileObject oRootFileSystem;

    /**
	 * PBSFileSystemProvider initialization.
	 */
    public PBSFileSystemProvider() {
        try {
            oRootFileSystem = VFSSupport.getFileSystemManager().resolveFile("file://");
        } catch (FileSystemException xEx) {
            throw new IllegalStateException("unknown error: local filesystem is not accessible: " + xEx.getMessage());
        }
    }

    /**
	 * get the file system associated with the job.
	 * 
	 * @param pJob
	 *            the job
	 * @return FileObject the root file system the job is associated with.
	 * @throws IOException
	 *             if the file system cannot be accessed.
	 */
    public FileObject getFileSystem(final JobContext pJob) throws IOException {
        return oRootFileSystem;
    }
}
