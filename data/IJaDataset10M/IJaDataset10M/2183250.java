package org.loon.framework.android.game.core.resource;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.net.URI;
import java.net.URL;
import org.loon.framework.android.game.core.LSystem;
import org.loon.framework.android.game.utils.StringUtils;

public class SDRes extends DataRes implements Resource {

    public SDRes(String path) {
        if (isMoutedSD()) {
            File f = android.os.Environment.getExternalStorageDirectory();
            String tmp = f.getPath();
            if (path.startsWith("/")) {
                path = path.substring(1);
            }
            if (!tmp.endsWith("/")) {
                path = tmp + "/" + path;
            } else {
                path = tmp + path;
            }
        } else {
            path = LSystem.getCacheFileName();
            path = StringUtils.replaceIgnoreCase(path, "\\", "/");
            if (path.startsWith("/") || path.startsWith("\\")) {
                path = path.substring(1, path.length());
            }
        }
        this.path = path;
        this.name = "sdcard://" + path;
    }

    public static final boolean isMoutedSD() {
        String sdState = android.os.Environment.getExternalStorageState();
        return sdState.equals(android.os.Environment.MEDIA_MOUNTED);
    }

    public InputStream getInputStream() {
        try {
            if (in != null) {
                return in;
            }
            return (in = new FileInputStream(new File(path)));
        } catch (FileNotFoundException e) {
            throw new RuntimeException("file " + name + " not found !", e);
        }
    }

    public String getResourceName() {
        return name;
    }

    public URI getURI() {
        try {
            if (uri != null) {
                return uri;
            }
            return (uri = new URL(path).toURI());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        SDRes other = (SDRes) obj;
        if (name == null) {
            if (other.name != null) {
                return false;
            }
        } else if (!name.equals(other.name)) {
            return false;
        }
        return true;
    }
}
