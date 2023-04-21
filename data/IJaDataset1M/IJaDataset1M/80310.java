package messy.tool.editor;

import java.io.*;

public class Lock implements Serializable {

    private String UID;

    private int size;

    public Lock(String UID, int size) {
        this.UID = UID;
        this.size = size;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int newSize) {
        size = newSize;
    }

    public String getUID() {
        return UID;
    }

    public void setUID(String newUID) {
        UID = newUID;
    }
}
