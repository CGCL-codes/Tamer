package cz.cvut.fel.mvod.zip;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.ZipOutputStream;

/**
 * Representation of directory in filesystem.
 * @author jakub
 */
class Directory implements FileSystemUnit {

    private File directory;

    private String relativePath;

    private List<FileSystemUnit> list = null;

    /**
	 * Creates new instance of <code>Directory</code>.
	 * @param absolutePath system dependent path in file system
	 * @param relativePath path of this directory in zip file
	 */
    public Directory(String absolutePath, String relativePath) {
        this(new File(absolutePath), relativePath);
    }

    /**
	 * Creates new instance of <code>Directory</code>.
	 * @param directory 
	 * @param relativePath path of this directory in zip file
	 */
    public Directory(File directory, String relativePath) {
        if (!directory.isDirectory()) {
            throw new IllegalArgumentException("File is not a directory.");
        }
        this.directory = directory;
        this.relativePath = (relativePath == null ? "" : relativePath) + directory.getName() + "/";
    }

    /**
	 * {@inheritDoc}
	 */
    public String getRelativePath() {
        return relativePath;
    }

    public List<FileSystemUnit> getDirList() {
        if (list == null) {
            list = new ArrayList<FileSystemUnit>();
            String[] children = directory.list();
            for (String filename : children) {
                FileContainer file = new FileContainer(directory.getAbsolutePath() + "/" + filename, relativePath);
                if (file.isDirectory()) {
                    list.add(new Directory(file, relativePath));
                } else {
                    list.add(file);
                }
            }
        }
        return list;
    }

    /**
	 * {@inheritDoc}
	 */
    public void write(ZipOutputStream out) throws IOException {
        getDirList();
        for (FileSystemUnit file : list) {
            file.write(out);
        }
    }
}
