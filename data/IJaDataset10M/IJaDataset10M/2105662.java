package uk.ac.gla.terrier.utility.io;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.io.InputStream;
import java.io.FileNotFoundException;
import java.io.RandomAccessFile;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import uk.ac.gla.terrier.utility.Files.FSCapability;

/** This is a Terrier File Abstraction Layer implementation of the local file system. The file system implementation for the 
 * "file:" scheme can handle all file system capabilities (READ,WRITE,RANDOM_READ,RANDOM_WRITE,LS_DIR and STAT).
 * Generally speaking, this is a lightweight wrapper around the following four Java SE classes:
 * java.io.File and java.io.FileInputStream and java.io.FileOutputStream, java.io.RandomAccessFile.
 * <p></p>
 * <p>On Windows, this FileSystem also advertises support for all the single-letter file systems, eg A:, C:, D:</p>
 * @since 2.1
 * @author Craig Macdonald
 * @version $Revision: 1.6 $ */
public class LocalFileSystem implements FileSystem, FSCapability {

    protected static class LocalRandomAccessFile extends RandomAccessFile implements RandomDataOutput {

        public LocalRandomAccessFile(String name, String mode) throws FileNotFoundException {
            super(name, mode);
        }
    }

    protected String normalise(String filename) {
        return filename.replaceFirst("^file:", "");
    }

    public String name() {
        return "local";
    }

    public byte capabilities() {
        return READ | WRITE | RANDOM_READ | RANDOM_WRITE | LS_DIR | STAT | DEL_ON_EXIT;
    }

    public String[] schemes() {
        if (System.getProperty("os.name").toLowerCase().indexOf("windows") != -1) return new String[] { "file", "a", "b", "c", "d", "e", "f", "g", "h", "i", "j", "k", "l", "m", "n", "o", "p", "q", "r", "s", "t", "u", "v", "w", "x", "y", "z" };
        return new String[] { "file" };
    }

    public boolean exists(String filename) {
        return new File(normalise(filename)).exists();
    }

    /** returns true if filename can be read */
    public boolean canRead(String filename) {
        return new File(normalise(filename)).canRead();
    }

    /** returns true if filename can be written to */
    public boolean canWrite(String filename) {
        return new File(normalise(filename)).canWrite();
    }

    public InputStream openFileStream(String filename) throws IOException {
        return new FileInputStream(normalise(filename));
    }

    public OutputStream writeFileStream(String filename) throws IOException {
        return new FileOutputStream(normalise(filename));
    }

    public RandomDataInput openFileRandom(String filename) throws IOException {
        return new LocalRandomAccessFile(normalise(filename), "r");
    }

    public RandomDataOutput writeFileRandom(String filename) throws IOException {
        return new LocalRandomAccessFile(normalise(filename), "rw");
    }

    public boolean delete(String filename) {
        return new File(normalise(filename)).delete();
    }

    public boolean deleteOnExit(String pathname) throws IOException {
        new File(normalise(pathname)).deleteOnExit();
        return true;
    }

    public boolean mkdir(String filename) {
        return new File(normalise(filename)).mkdir();
    }

    public boolean isDirectory(String path) {
        return new File(normalise(path)).isDirectory();
    }

    public long length(String filename) {
        return new File(normalise(filename)).length();
    }

    /** rename a file/dir to another name, on the same file system */
    public boolean rename(String source, String destination) throws IOException {
        return new File(normalise(source)).renameTo(new File(normalise(destination)));
    }

    /** whats the parent path to this path - eg directory containing a file */
    public String getParent(String path) throws IOException {
        return new File(normalise(path)).getParent();
    }

    /** list contents of a directory etc */
    public String[] list(String path) throws IOException {
        return new File(normalise(path)).list();
    }
}
