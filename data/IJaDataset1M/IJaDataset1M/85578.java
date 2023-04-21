package gnu.text;

import java.io.*;
import java.net.*;
import gnu.mapping.*;

/** A wrapper around a {@code java.io.File} that extends {@code Path}. */
public class FilePath extends Path implements Comparable<FilePath> {

    final File file;

    /** Usually the same as {@code file.toString()}.
   * One important difference: {@code isDirectory} is true
   * if {@code path} ends with the {@code '/'} or the {@code separatorChar}.
   * The original String if constructed from a String.
   */
    final String path;

    private FilePath(File file) {
        this.file = file;
        this.path = file.toString();
    }

    private FilePath(File file, String path) {
        this.file = file;
        this.path = path;
    }

    public static FilePath valueOf(String str) {
        String orig = str;
        return new FilePath(new File(str), orig);
    }

    public static FilePath valueOf(File file) {
        return new FilePath(file);
    }

    public static FilePath coerceToFilePathOrNull(Object path) {
        if (path instanceof FilePath) return (FilePath) path;
        if (path instanceof URIPath) return FilePath.valueOf(new File(((URIPath) path).uri));
        if (path instanceof URI) return FilePath.valueOf(new File((URI) path));
        if (path instanceof File) return FilePath.valueOf((File) path);
        String str;
        if (path instanceof gnu.lists.FString) str = path.toString(); else if (path instanceof String) str = (String) path; else return null;
        return FilePath.valueOf(str);
    }

    public static FilePath makeFilePath(Object arg) {
        FilePath path = coerceToFilePathOrNull(arg);
        if (path == null) throw new WrongType((String) null, WrongType.ARG_CAST, arg, "filepath");
        return path;
    }

    public boolean isAbsolute() {
        return this == Path.userDirPath || file.isAbsolute();
    }

    public boolean isDirectory() {
        if (file.isDirectory()) return true;
        if (!file.exists()) {
            int len = path.length();
            if (len > 0) {
                char last = path.charAt(len - 1);
                if (last == '/' || last == File.separatorChar) return true;
            }
        }
        return false;
    }

    public boolean delete() {
        return toFile().delete();
    }

    public long getLastModified() {
        return file.lastModified();
    }

    public boolean exists() {
        return file.exists();
    }

    public long getContentLength() {
        long length = file.length();
        return length == 0 && !file.exists() ? -1 : length;
    }

    public String getPath() {
        return file.getPath();
    }

    public String getLast() {
        return file.getName();
    }

    public FilePath getParent() {
        File parent = file.getParentFile();
        if (parent == null) return null; else return FilePath.valueOf(parent);
    }

    public int compareTo(FilePath path) {
        return file.compareTo(path.file);
    }

    public boolean equals(Object obj) {
        return obj instanceof FilePath && file.equals(((FilePath) obj).file);
    }

    public int hashCode() {
        return file.hashCode();
    }

    public String toString() {
        return path;
    }

    public File toFile() {
        return file;
    }

    public URL toURL() {
        if (this == Path.userDirPath) return resolve("").toURL();
        if (!isAbsolute()) return getAbsolute().toURL();
        try {
            return file.toURI().toURL();
        } catch (Throwable ex) {
            throw WrappedException.wrapIfNeeded(ex);
        }
    }

    private static URI toUri(File file) {
        try {
            if (file.isAbsolute()) return file.toURI();
            String fname = file.toString();
            char fileSep = File.separatorChar;
            if (fileSep != '/') fname = fname.replace(fileSep, '/');
            return new URI(null, null, fname, null);
        } catch (Throwable ex) {
            throw WrappedException.wrapIfNeeded(ex);
        }
    }

    public URI toUri() {
        if (this == Path.userDirPath) return resolve("").toURI();
        return toUri(file);
    }

    public InputStream openInputStream() throws IOException {
        return new FileInputStream(file);
    }

    public OutputStream openOutputStream() throws IOException {
        return new FileOutputStream(file);
    }

    public String getScheme() {
        return isAbsolute() ? "file" : null;
    }

    public Path resolve(String relative) {
        if (Path.uriSchemeSpecified(relative)) return URLPath.valueOf(relative);
        File rfile = new File(relative);
        if (rfile.isAbsolute()) return FilePath.valueOf(rfile);
        char sep = File.separatorChar;
        if (sep != '/') relative = relative.replace('/', sep);
        File nfile;
        if (this == Path.userDirPath) nfile = new File(System.getProperty("user.dir"), relative); else nfile = new File(isDirectory() ? file : file.getParentFile(), relative);
        return valueOf(nfile);
    }

    public Path getCanonical() {
        try {
            File canon = file.getCanonicalFile();
            if (!canon.equals(file)) return valueOf(canon);
        } catch (Throwable ex) {
        }
        return this;
    }
}
