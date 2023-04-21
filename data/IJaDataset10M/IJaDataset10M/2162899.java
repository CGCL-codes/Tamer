package mecca.object;

import java.sql.Blob;

/**
 * @author Shaiful Nizam Tajul
 * @version 1.01
 */
public class Image {

    private String id;

    private String filename;

    private Blob blob;

    public Image() {
        id = "0";
        filename = "";
        blob = null;
    }

    public void setId(String s) {
        if (s == null) {
            id = "0";
        } else {
            id = s;
        }
    }

    public String getId() {
        return id;
    }

    public void setFilename(String s) {
        if (s == null) {
            filename = "";
        } else {
            filename = s;
        }
    }

    public String getFilename() {
        return filename;
    }

    public void setBlob(Blob b) {
        blob = b;
    }

    public Blob getBlob() {
        return blob;
    }
}
