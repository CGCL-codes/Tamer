package net.sourceforge.freejava.vfs.impl.url;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import net.sourceforge.freejava.vfs.AbstractFile;
import net.sourceforge.freejava.vfs.IVolume;
import net.sourceforge.freejava.vfs.VFSException;
import net.sourceforge.freejava.vfs.path.IPath;

public class URLFile extends AbstractFile.TransientPath {

    private final URL url;

    public URLFile(URL url) {
        super(url.getPath());
        this.url = url;
    }

    @Override
    public IVolume getVolume() {
        return URLVolume.getInstance();
    }

    @Override
    public IPath getPath() {
        return new URLPath("");
    }

    @Override
    public URLFile clone() {
        return new URLFile(url).populate(this);
    }

    @Override
    protected URLFile populate(Object obj) {
        super.populate(obj);
        return this;
    }

    @Override
    public Long getLastModifiedTime() {
        try {
            return url.openConnection().getLastModified();
        } catch (IOException e) {
            return null;
        }
    }

    @Override
    public boolean isBlob() {
        return true;
    }

    @Override
    public boolean isTree() {
        return true;
    }

    @Override
    public boolean isReadable() {
        return true;
    }

    static String _getName(URL url) {
        String name = url.getFile();
        int slash = name.lastIndexOf('/');
        if (slash != -1) name = name.substring(slash + 1);
        return name;
    }

    @Override
    public boolean delete() {
        try {
            URLConnection connection = url.openConnection();
            if (connection instanceof HttpURLConnection) {
                HttpURLConnection http = (HttpURLConnection) connection;
                http.setRequestMethod("DELETE");
                int responseCode = http.getResponseCode();
                if (responseCode < 300) return true;
                if (responseCode == 404) return false;
                throw new IOException(http.getResponseMessage());
            } else return false;
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public Long getLength() {
        try {
            URLConnection connection = url.openConnection();
            long length = connection.getContentLength();
            return length == -1 ? null : length;
        } catch (IOException e) {
            return null;
        }
    }

    @Override
    public URLFile getChild(String entryName) throws VFSException {
        URL url;
        try {
            url = new URL(this.url, entryName);
        } catch (MalformedURLException e) {
            throw new RuntimeException(e.getMessage(), e);
        }
        return new URLFile(url);
    }
}
