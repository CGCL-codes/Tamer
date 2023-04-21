package org.one.stone.soup.server.http.client;

import java.io.File;
import java.io.FileFilter;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.util.Vector;
import org.one.stone.soup.stringhelper.StringArrayHelper;

public class HttpFile extends File {

    private HttpFileSystemView view;

    private String path;

    private boolean directory;

    private boolean exists = false;

    private long lastModified;

    private long length;

    public HttpFile(File parent, String child) {
        this(null, parent.getPath() + "/" + child, false);
    }

    public HttpFile(String parent, String child) {
        this(null, parent + "/" + child, false);
    }

    public HttpFile(URI uri) {
        this(null, uri.toASCIIString(), false);
    }

    public HttpFile(HttpFileSystemView view, String pathName, boolean isDirectory) {
        super("http file");
        while (pathName.length() > 1 && pathName.charAt(1) == '/') {
            pathName = pathName.substring(1);
        }
        this.directory = isDirectory;
        this.path = pathName;
        this.view = view;
    }

    public boolean canRead() {
        return true;
    }

    public boolean canWrite() {
        return true;
    }

    public int compareTo(File pathname) {
        if (pathname == null) {
            return -1;
        }
        if (pathname.getAbsolutePath().equals(getAbsolutePath())) {
            return 0;
        } else if (pathname.getAbsolutePath().length() >= getAbsolutePath().length()) {
            return 1;
        } else {
            return -1;
        }
    }

    public boolean createNewFile() throws IOException {
        return true;
    }

    public boolean delete() {
        return true;
    }

    public void deleteOnExit() {
    }

    public boolean equals(Object obj) {
        if (obj != null && obj instanceof HttpFile && compareTo((HttpFile) obj) == 0) {
            return true;
        } else {
            return false;
        }
    }

    public boolean exists() {
        return view.exists(this);
    }

    public File getAbsoluteFile() {
        return this;
    }

    public String getAbsolutePath() {
        return view.getBaseUrl() + "/" + path;
    }

    public File getCanonicalFile() throws IOException {
        return getAbsoluteFile();
    }

    public String getCanonicalPath() throws IOException {
        return getAbsolutePath();
    }

    public String getName() {
        String[] parts = getPathParts();
        if (parts.length == 0) {
            return "";
        }
        return parts[parts.length - 1];
    }

    public String getParent() {
        String[] parts = getPathParts();
        if (parts.length < 1) {
            return null;
        } else {
            return StringArrayHelper.arrayToString(parts, "/", 0, parts.length - 1);
        }
    }

    public File getParentFile() {
        String parentName = getParent();
        if (parentName == null) {
            return null;
        }
        return new HttpFile(view, getParent(), true);
    }

    public String getPath() {
        return path;
    }

    public boolean isAbsolute() {
        return false;
    }

    public boolean isDirectory() {
        return directory;
    }

    public boolean isFile() {
        return !directory;
    }

    public boolean isHidden() {
        return false;
    }

    public long lastModified() {
        return lastModified;
    }

    public long length() {
        return length;
    }

    public String[] list() {
        File[] files = view.getFiles(this, true);
        String[] fileNames = new String[files.length];
        for (int loop = 0; loop < files.length; loop++) {
            fileNames[loop] = files[loop].getName();
        }
        return fileNames;
    }

    public String[] list(FilenameFilter filter) {
        File[] files = view.getFiles(this, true);
        Vector fileNames = new Vector();
        for (int loop = 0; loop < files.length; loop++) {
            if (filter.accept(this, files[loop].getName())) {
                fileNames.addElement(files[loop].getName());
            }
        }
        return StringArrayHelper.vectorToStringArray(fileNames);
    }

    public File[] listFiles() {
        return view.getFiles(this, true);
    }

    public File[] listFiles(FileFilter filter) {
        File[] files = view.getFiles(this, true);
        Vector fileMatches = new Vector();
        for (int loop = 0; loop < files.length; loop++) {
            if (filter.accept(files[loop])) {
                fileMatches.addElement(files[loop]);
            }
        }
        return (HttpFile[]) fileMatches.toArray(new HttpFile[fileMatches.size()]);
    }

    public File[] listFiles(FilenameFilter filter) {
        File[] files = view.getFiles(this, true);
        Vector fileMatches = new Vector();
        for (int loop = 0; loop < files.length; loop++) {
            if (filter.accept(this, files[loop].getName())) {
                fileMatches.addElement(files[loop]);
            }
        }
        return (HttpFile[]) fileMatches.toArray(new HttpFile[fileMatches.size()]);
    }

    public boolean mkdir() {
        return false;
    }

    public boolean mkdirs() {
        return false;
    }

    public boolean renameTo(File dest) {
        return view.renameFile(this, (HttpFile) dest);
    }

    public boolean setLastModified(long time) {
        return false;
    }

    public boolean setReadOnly() {
        return false;
    }

    public String toString() {
        return "HttpFile URL:" + view.getBaseUrl() + "/" + path;
    }

    public URI toURI() {
        return null;
    }

    public URL toURL() throws MalformedURLException {
        return new URL(view.getBaseUrl() + path.substring(1));
    }

    private String[] getPathParts() {
        return StringArrayHelper.parseFields(path, '/');
    }

    void _setLastModified(long lastModified) {
        this.lastModified = lastModified;
    }

    void _setLength(long length) {
        this.length = length;
    }

    public OutputStream getOutputStream() throws IOException {
        return view.getOutputStream(this);
    }

    public InputStream getInputStream() throws IOException {
        return view.getInputStream(this);
    }
}
