package zipfs;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.TreeMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Pattern;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import org.das2.util.filesystem.FileObject;
import org.das2.util.filesystem.FileSystem;

/**
 * A filesystem to read data from zip files.
 * @author Ed Jackson
 */
public class ZipFileSystem extends FileSystem {

    private ZipFile zipFile;

    private TreeMap<String, ZipFileObject> filemap = new TreeMap<String, ZipFileObject>();

    protected ZipFileSystem(URI root) throws IOException {
        super(root);
        if (!("file".equals(root.getScheme()))) {
            throw new IllegalArgumentException("Cannot access non-local zip file: " + root);
        }
        if (!(root.toString().endsWith(".zip") || root.toString().endsWith(".ZIP"))) {
            throw new IllegalArgumentException("expected zip file to end with zip: " + root.toString());
        }
        File f;
        f = new File(root);
        zipFile = new ZipFile(f);
        filemap.put("/", new ZipFileObject(this, null, null));
        Enumeration<? extends ZipEntry> contents = zipFile.entries();
        while (contents.hasMoreElements()) {
            ZipEntry entry = contents.nextElement();
            String entryName = "/" + entry.getName();
            addZipEntry(entryName, entry);
        }
    }

    private void addZipEntry(String name, ZipEntry entry) {
        String parentName = name.substring(0, name.lastIndexOf("/", name.length() - 2) + 1);
        if (!filemap.containsKey(parentName)) addZipEntry(parentName, null);
        String n = null;
        if (entry == null) {
            n = name;
            if (n.endsWith("/")) n = n.substring(0, n.length() - 1);
            n = n.substring(n.lastIndexOf("/"));
        }
        ZipFileObject zfo = new ZipFileObject(this, entry, filemap.get(parentName), n);
        filemap.put(name, zfo);
        filemap.get(parentName).addChildObject(zfo);
    }

    protected ZipFile getZipFile() {
        return zipFile;
    }

    @Override
    public FileObject getFileObject(String filename) {
        String f = FileSystem.toCanonicalFilename(filename);
        if (!f.startsWith("/")) f = "/" + f;
        if (filemap.containsKey(f)) {
            return filemap.get(f);
        } else if (filemap.containsKey(f + "/")) {
            return filemap.get(f + "/");
        } else {
            return new ZipFileObject(this, null, null, filename);
        }
    }

    @Override
    public boolean isDirectory(String filename) throws IOException {
        String f = FileSystem.toCanonicalFilename(filename);
        if (filemap.containsKey(f)) return filemap.get(f).isFolder();
        f = FileSystem.toCanonicalFolderName(filename);
        if (filemap.containsKey(f)) return filemap.get(f).isFolder();
        throw new FileNotFoundException("No such file in zip: " + filename);
    }

    @Override
    public String[] listDirectory(String directory) throws IOException {
        String dname = FileSystem.toCanonicalFolderName(directory);
        if (!isDirectory(dname)) {
            throw new IllegalArgumentException("Not a folder in zip file: " + dname);
        }
        FileObject[] contents = filemap.get(dname).getChildren();
        String[] results = new String[contents.length];
        for (int i = 0; i < contents.length; ++i) {
            String s = contents[i].getNameExt();
            results[i] = s.substring(s.lastIndexOf("/", s.length() - 2) + 1);
        }
        return results;
    }

    @Override
    public String[] listDirectory(String directory, String regex) throws IOException {
        directory = toCanonicalFilename(directory);
        String[] listing = listDirectory(directory);
        Pattern pattern = Pattern.compile(regex + "/?");
        ArrayList result = new ArrayList();
        for (int i = 0; i < listing.length; i++) {
            if (pattern.matcher(listing[i]).matches()) {
                result.add(listing[i]);
            }
        }
        return (String[]) result.toArray(new String[result.size()]);
    }

    @Override
    public File getLocalRoot() {
        File localCacheDir = settings().getLocalCacheDir();
        String zipCacheName = localCacheDir.getAbsolutePath() + "/zip" + zipFile.getName().substring(0, zipFile.getName().length() - 4) + "/";
        File zipCache = new File(zipCacheName);
        return zipCache;
    }

    @Override
    public String toString() {
        return "zipfs " + zipFile.getName();
    }
}
