package Cauldron3;

import Cauldron3.LocalVFS.FSInfo;
import Cauldron3.LocalVFS.FileIfc;
import Cauldron3.LocalVFS.FileUnion;
import Cauldron3.LocalVFS.LocalFile;
import Cauldron3.LocalVFS.ZipArchive;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

/**
 * DirectoryBean
 */
public class DirectoryBean {

    private final PropertyChangeSupport pcs = new PropertyChangeSupport(this);

    private FileIfc currenFolder = null;

    public void setDirectory(Object fileObj) {
        FileIfc oldFile = getDirectory();
        if (fileObj instanceof String) {
            throw new IllegalArgumentException("Fix this");
        } else if (fileObj instanceof FileIfc) {
            if (fileObj instanceof FileUnion) {
                currenFolder = (FileUnion) fileObj;
            } else if (fileObj instanceof LocalFile) {
                currenFolder = (LocalFile) fileObj;
            } else if (fileObj instanceof ZipArchive) {
                currenFolder = (ZipArchive) fileObj;
            }
        }
        if (currenFolder != null) {
            pcs.firePropertyChange("directory", oldFile, currenFolder);
        }
    }

    public FileIfc getDirectory() {
        if (currenFolder != null) {
            return currenFolder;
        }
        return new LocalFile(FSInfo.listRootPath());
    }

    public FileIfc getObject() {
        return currenFolder;
    }

    public void addPropertyChangeListener(PropertyChangeListener listener) {
        this.pcs.addPropertyChangeListener(listener);
    }

    public void removePropertyChangeListener(PropertyChangeListener listener) {
        this.pcs.removePropertyChangeListener(listener);
    }
}
