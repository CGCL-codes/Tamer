package de.ibis.permoto.util.typemappingsexporter;

import java.util.Vector;

/**
 * @author Oliver H�hn
 */
public class CustomDir {

    private String path;

    private String name;

    private Vector<CustomFile> files = new Vector<CustomFile>();

    private Vector<CustomDir> dirs = new Vector<CustomDir>();

    public CustomDir(String path, String name) {
        this.path = path;
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public String getPath() {
        return path;
    }

    public void addFile(CustomFile f) {
        files.addElement(f);
    }

    public void addDir(CustomDir d) {
        dirs.addElement(d);
    }

    public Vector<CustomFile> getFiles() {
        return files;
    }

    public Vector<CustomDir> getDirs() {
        return dirs;
    }
}
