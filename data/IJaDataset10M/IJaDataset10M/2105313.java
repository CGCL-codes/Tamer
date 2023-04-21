package net.sourceforge.javautil.common;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import net.sourceforge.javautil.common.exception.ThrowableManagerRegistry;
import net.sourceforge.javautil.common.io.IVirtualArtifact;
import net.sourceforge.javautil.common.io.IVirtualFile;
import net.sourceforge.javautil.common.shutdown.Shutdown;

/**
 * Utilities/methods for dealing with {@link File}'s.
 * 
 * @author ponder
 * @author $Author: ponderator $
 * @version $Id: FileUtil.java 2297 2010-06-16 00:13:14Z ponderator $
 */
public class FileUtil {

    /**
	 * This assumes default buffer.
	 * 
	 * @see #copy(File, File, byte[])
	 */
    public static void copy(File src, File dst) {
        copy(src, dst, null);
    }

    /**
	 * @param src The source file to copy
	 * @param dst The destination file to copy to
	 * @param buffer The buffer to use for copying, or null for default buffer
	 */
    public static void copy(File src, File dst, byte[] buffer) {
        try {
            IOUtil.transfer(new FileInputStream(src), new FileOutputStream(dst), buffer, true);
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    /**
	 * This assumes the default buffer.
	 * 
	 * @see #read(File, byte[])
	 */
    public static byte[] read(File file) {
        return read(file, null);
    }

    /**
	 * @param file The file to read
	 * @param buffer The buffer to use for reading
	 * @return A byte[] representing the contents of the file
	 */
    public static byte[] read(File file, byte[] buffer) {
        try {
            return IOUtil.read(new FileInputStream(file), buffer);
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    /**
	 * @param root A root file
	 * @param sub A file that is a sub directory or file of the root file
	 * @return A relative path from the end of the root file path to sub file 
	 */
    public static String getRelativePath(File root, File sub) {
        String subPath = sub.getPath().substring(root.getPath().length());
        if (subPath.startsWith("\\") || subPath.startsWith("/")) subPath = subPath.substring(1);
        return subPath;
    }

    /**
	 * This will call {@link #appendFiles(File, boolean, List)} with a new
	 * array and return it.
	 */
    public static List<File> getFiles(File directory, boolean recursive) {
        return appendFiles(directory, recursive, new ArrayList<File>());
    }

    /**
	 * @param directory The directory to get a list of files from
	 * @param recursive True if the list should be recursive, otherwise false
	 * @param files An array to append the list of files to
	 * @return The same array that was passed
	 */
    public static List<File> appendFiles(File file, boolean recursive, List<File> files) {
        if (file.isDirectory()) {
            for (File f : file.listFiles()) {
                files.add(f);
                if (f.isDirectory() && recursive) appendFiles(f, recursive, files);
            }
        } else files.add(file);
        return files;
    }

    /**
	 * Facility method for calling and having {@link IOException}'s wrapped
	 * in {@link RuntimeException}'s.
	 * 
	 * @param prefix The prefix of the new temporary file
	 * @param suffix The suffix of the new temporary file
	 * @return A file pointing to the new temporary file created
	 */
    public static File createTemporaryFile(String prefix, String suffix, boolean deleteOnExit) {
        try {
            File tmp = File.createTempFile(prefix, suffix);
            if (deleteOnExit) Shutdown.registerForDeletion(tmp);
            return tmp;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
	 * @param prefix The prefix for the new temporary directory
	 * @param suffix The suffix for the new temporary directory
	 * @return A directory pointing to the new temporary directory
	 */
    public static File createTemporaryDirectory(String prefix, String suffix, boolean deleteOnExit) {
        File file = createTemporaryFile(prefix, suffix, deleteOnExit);
        file.delete();
        if (!file.mkdirs()) throw new RuntimeException("Could not make temporary directory: " + file);
        return file;
    }

    /**
	 * Transfer the byte's in the input stream and store them
	 * in the file provided.
	 * 
	 * @param is An arbitrary input stream
	 * @param target The destination file
	 * @return The same target file
	 */
    public static File write(InputStream is, File target) {
        try {
            FileOutputStream out = new FileOutputStream(target);
            IOUtil.transfer(is, out);
            out.close();
        } catch (IOException e) {
            throw ThrowableManagerRegistry.caught(e);
        }
        return target;
    }

    /**
	 * @param file The zip/archive
	 * @param entry The entry in the archive
	 * @param target The file to write the extracted contents to
	 * @return The same target file
	 */
    public static File extract(ZipFile file, ZipEntry entry, File target) {
        try {
            return write(file.getInputStream(entry), target);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
	 * Remove a file or a directory recursively. 
	 * 
	 * @param file The file to delete
	 */
    public static boolean delete(File file) {
        if (file.isDirectory()) {
            for (File sub : file.listFiles()) delete(sub);
            if (!file.delete()) return false;
        } else {
            if (!file.delete()) return false;
        }
        return true;
    }

    /**
	 * A facility, for wrapping {@link MalformedURLException}'s
	 * into {@link RuntimeException}'s.
	 * 
	 * @param file The file to get the URL from
	 * @return A URL pointing to the file (same as calling {@link URI#toURL()} on
	 *  {@link File#toURI()})
	 */
    public static URL toURL(File file) {
        try {
            return file.toURI().toURL();
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        }
    }
}
