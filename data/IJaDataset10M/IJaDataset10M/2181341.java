package net.sf.lanwork.nfs;

import java.io.Serializable;

/**
 * The FileInfo object stores the information of a file 
 * such as whether it is readable, its size. It is unmodifiable!
 * @author Thomas Ting
 * @version 0.1 2008.12.13
 */
public class FileInfo implements Serializable {

    private static final long serialVersionUID = 444085927563234646L;

    /**
	 * @param readable Whether it is readable and can be downloaded.
	 * @param size The size of it.
	 * @param dir Whether it is a directory or a file.
	 */
    public FileInfo(String name, boolean readable, long size, boolean dir) {
        this.name = name;
        this.readable = readable;
        this.size = size;
        this.dir = dir;
    }

    public boolean isReadable() {
        return readable;
    }

    public long getSize() {
        return size;
    }

    public boolean isDirectory() {
        return dir;
    }

    public String getName() {
        return name;
    }

    private final String name;

    private final boolean readable;

    private final long size;

    private final boolean dir;

    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + (dir ? 1231 : 1237);
        result = prime * result + ((name == null) ? 0 : name.hashCode());
        result = prime * result + (readable ? 1231 : 1237);
        result = prime * result + (int) (size ^ (size >>> 32));
        return result;
    }

    public boolean equals(Object obj) {
        if (!(obj instanceof FileInfo)) return false;
        FileInfo other = (FileInfo) obj;
        if (other.dir == dir && other.readable == readable && other.name.equals(name) && other.size == size) return true;
        return false;
    }
}
