package datastructure;

import files.*;
import folder.*;
import java.util.List;
import level.*;
import touchcomponent.*;

/**
 *
 * @author Claire
 */
public class DataStructure extends TouchComponent {

    private List<Level> myLevelList;

    private List<FileList> myFileListList;

    private Folder myWanderingFolder;

    private File myWanderingFile;

    private WorkZone myWorkZone;

    public DataStructure() {
    }

    public void setWanderingFolder(Folder folder) {
    }

    public Folder getWanderingFolder() {
        return myWanderingFolder;
    }

    public void setWanderingFile(File file) {
        myWanderingFile = file;
    }

    public File getWanderingFile() {
        return myWanderingFile;
    }

    public void addLevel(Level level) {
    }

    public void removeLevel(Level level) {
    }

    public void addFileList(FileList fileList) {
        myFileListList.add(fileList);
        this.add(fileList);
    }

    public void removeFileList(FileList fileList) {
    }

    public void openFolder(Folder folder) {
    }

    public void closeFolder(Folder folder) {
    }
}
