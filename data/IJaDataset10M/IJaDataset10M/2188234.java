package libjdc.dc.filelist;

import java.util.LinkedList;

/**
 *
 * @author root
 */
public class Directory {

    private String name;

    private LinkedList<File> files;

    private LinkedList<Directory> dirs;

    /** Creates a new instance of Directory */
    public Directory(String name) {
        this.name = name;
        this.files = new LinkedList<File>();
        this.dirs = new LinkedList<Directory>();
    }

    public String getName() {
        return name;
    }

    public LinkedList<File> getFiles() {
        return files;
    }

    public LinkedList<Directory> getDirs() {
        return dirs;
    }
}
