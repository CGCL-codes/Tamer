package edu.regis.jprobe.jni;

import java.io.Serializable;
import edu.regis.jprobe.model.Utilities;

public class OSLibInfo implements Serializable {

    private String name;

    private String path;

    private long size;

    public OSLibInfo(String name, String path, long size) {
        this.name = name;
        this.path = path;
        this.size = size;
    }

    /**
	 * @return the name
	 */
    public String getName() {
        return name;
    }

    /**
	 * @param name the name to set
	 */
    public void setName(String name) {
        this.name = name;
    }

    /**
	 * @return the path
	 */
    public String getPath() {
        return path;
    }

    /**
	 * @param path the path to set
	 */
    public void setPath(String path) {
        this.path = path;
    }

    /**
	 * @return the size
	 */
    public long getSize() {
        return size;
    }

    /**
	 * @param size the size to set
	 */
    public void setSize(long size) {
        this.size = size;
    }

    public String toString() {
        return name + " (" + path + ") Size(" + Utilities.format(size) + ")";
    }
}
