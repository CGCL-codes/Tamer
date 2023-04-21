package net.techwatch.fsindex;

import java.io.File;
import java.util.Iterator;
import java.util.List;
import net.techwatch.fsindex.event.FileSystemEvent;

/**
 * @author wiv
 *
 */
public class DirectoriesChecker extends FileSystemChecker {

    /**
	 * Default constructor
	 */
    public DirectoriesChecker() {
        super();
    }

    @SuppressWarnings("unchecked")
    public void run() {
        Iterator dirIt = getFileSystemLObjectList().iterator();
        while (dirIt.hasNext()) {
            FileSystemObject fsObj = (FileSystemObject) dirIt.next();
            checkForDeletion(fsObj);
            if (checkForUpdate(fsObj)) {
                File f = new File(fsObj.getPath());
                long id = fsObj.getId();
                List children = getFileSystemDataManager().getChildren(id);
                for (File file : f.listFiles(new ObjectFileFilter())) {
                    if (!children.contains(file.getName())) {
                        FileSystemEvent event = new FileSystemEvent(this);
                        event.setParentId(id);
                        event.setFile(file.isFile());
                        event.setLastUpdate(file.lastModified());
                        event.setName(file.getName());
                        event.setParentPath(getAbsolutePath(f));
                        getFileSystemMonitor().postCreate(event);
                        if (file.isDirectory()) addDirectory(file, id);
                    }
                }
            }
        }
        getCountDownLatch().countDown();
    }

    /**
	 * @param file
	 * @param parentId
	 */
    private void addDirectory(File file, long parentId) {
        for (File f : file.listFiles(new ObjectFileFilter())) {
            FileSystemEvent event = new FileSystemEvent(this);
            event.setFile(f.isFile());
            event.setLastUpdate(f.lastModified());
            event.setParentId(parentId);
            event.setName(f.getName());
            String absolutePath = getAbsolutePath(f);
            event.setParentPath(absolutePath.substring(0, absolutePath.lastIndexOf("/")));
            getFileSystemMonitor().postCreate(event);
            if (f.isDirectory()) addDirectory(f, event.getId());
        }
    }

    /**
	 * Build the absolute path to this file by a system independent way.
	 * @param file
	 * @return A standard representation of this absolute path.
	 */
    String getAbsolutePath(File file) {
        String path = file.getAbsolutePath();
        String regex = null;
        if (System.getProperty("os.name").toLowerCase().contains("windows")) regex = File.separator + File.separator; else regex = File.separator;
        return path.replaceAll(regex, "/");
    }
}
