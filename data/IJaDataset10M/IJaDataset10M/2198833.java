package net.sourceforge.taverna.publish.local;

import java.io.IOException;
import net.sourceforge.taverna.publish.AbstractRepository;
import net.sourceforge.taverna.publish.PublicationException;
import net.sourceforge.taverna.publish.Repository;
import org.apache.commons.vfs.FileObject;
import org.apache.commons.vfs.FileSystemException;
import org.apache.commons.vfs.FileUtil;
import org.apache.commons.vfs.VFS;

/**
 * This class provides a local file system implementation of the repository.
 * 
 * Last edited by $Author: wassinki $
 * 
 * @author Mark
 * @version $Revision: 1.1 $
 */
public class LocalRepository extends AbstractRepository implements Repository {

    /**
	 * Constructor
	 * 
	 * @param rootFileUrl
	 *            The root directory of the repository.
	 */
    public LocalRepository(String rootFileUrl) {
        try {
            this.setFsManager(VFS.getManager());
            this.root = this.fsManager.resolveFile(rootFileUrl);
            this.fsManager.createFileSystem(root);
        } catch (FileSystemException e) {
            e.printStackTrace();
        }
    }

    /**
	 * 
	 * @see net.sourceforge.taverna.publish.Repository#publish(org.apache.commons.vfs.FileObject[],
	 *      org.apache.commons.vfs.FileObject)
	 */
    public void publish(FileObject[] filelist, FileObject startingDir) throws PublicationException {
        String filedir = startingDir.getName().getPath();
        FileObject destFile = null;
        try {
            for (int i = 0; i < filelist.length; i++) {
                destFile = this.fsManager.resolveFile(filedir + LINE_ENDING);
                FileUtil.copyContent(filelist[i], startingDir);
            }
        } catch (IOException e) {
            throw new PublicationException(e);
        }
    }
}
