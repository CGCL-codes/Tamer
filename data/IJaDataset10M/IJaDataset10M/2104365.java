package uk.ac.cam.ch.wwmm.checkcml.cli;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class FileLoader {

    private List<File> fileList = new ArrayList<File>();

    private int recursionDepth = 1;

    public FileLoader() {
    }

    /**
     * If the string passed does not point to a valid file the argument is considered illegal.
     * 
     * @param file_path
     * @throws IllegalArgumentException
     */
    public void loadFile(String file_path) throws IllegalArgumentException {
        File file = new File(file_path);
        if (file.isFile()) {
            this.fileList.add(file);
        } else {
            throw new IllegalArgumentException();
        }
    }

    /**
     * If the string passed does not point to a valid directory the argument is considered illegal.
     * 
     * @param dir_path
     * @throws IllegalArgumentException
     */
    public void loadDirectory(String dir_path) throws IllegalArgumentException {
        File dir = new File(dir_path);
        if (dir.isDirectory()) {
            recurse(dir, this.recursionDepth);
        } else {
            throw new IllegalArgumentException();
        }
    }

    private void recurse(File file, int depth) {
        if (!file.isHidden()) {
            if (depth != 0) {
                if (file.isDirectory()) {
                    File[] fileArray = file.listFiles();
                    for (File f : fileArray) {
                        recurse(f, (depth - 1));
                    }
                }
            }
            if (file.isFile()) {
                this.fileList.add(file);
            }
        }
    }

    public List<File> getList() {
        return this.fileList;
    }

    public void setRecursion(int i) {
        this.recursionDepth = i;
    }
}
