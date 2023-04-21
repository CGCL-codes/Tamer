package org.jcvi.common.core.io;

import java.io.File;
import java.io.IOException;
import org.jcvi.common.core.util.LIFOQueue;

/**
 * {@code FileUtil} is a Utility class
 * containing common static methods to work with {@link File}
 * objects.
 * @author dkatzel
 *
 */
public final class FileUtil {

    /**
     * The Unix separator character.  We need to explicitly hardcode
     * this value since we need to check against unix and windows
     * and not rely on the OS to tell us which one to use.
     * 
     */
    private static final char UNIX_SEPARATOR = '/';

    /**
     * The Windows separator character.  We need to explicitly hardcode
     * this value since we need to check against unix and windows
     * and not rely on the OS to tell us which one to use.
     */
    private static final char WINDOWS_SEPARATOR = '\\';

    /**
	 * Creates a String of the relative path from the given root to the other given file.
	 * @param root the File to start from.
	 * @param otherFile the File to get to.
	 * @return a String of containing the relative file path required
	 * to traverse the file system to get from one file to the other.
	 * @throws IOException if there is a problem finding the location of either
	 * file on the file system.
	 */
    public static String createRelavitePathFrom(File root, File otherFile) throws IOException {
        return createRelavitePathFrom(root, otherFile, File.separatorChar);
    }

    /**
	 * Creates a String of the relative path from the given root to the other given file.
	 * @param root the File to start from.
	 * @param otherFile the File to get to.
	 * @param pathSeparator the path separator for this File system.
	 * @return a String of containing the relative file path required
	 * to traverse the file system to get from one file to the other.
	 * @throws IOException if there is a problem finding the location of either
	 * file on the file system.
	 */
    public static String createRelavitePathFrom(File root, File otherFile, char pathSeparator) throws IOException {
        LIFOQueue<String> rootStack = getCanonicalStackFor(root);
        LIFOQueue<String> otherStack = getCanonicalStackFor(otherFile);
        while (!rootStack.isEmpty() && !otherStack.isEmpty() && rootStack.peek().equals(otherStack.peek())) {
            rootStack.remove();
            otherStack.remove();
        }
        StringBuilder relativePath = new StringBuilder();
        while (!rootStack.isEmpty()) {
            relativePath.append("..").append(pathSeparator);
            rootStack.remove();
        }
        while (!otherStack.isEmpty()) {
            relativePath.append(otherStack.remove()).append(pathSeparator);
        }
        if (relativePath.length() == 0) {
            return "";
        }
        return relativePath.substring(0, relativePath.length() - 1);
    }

    private static LIFOQueue<String> getCanonicalStackFor(File f) throws IOException {
        LIFOQueue<String> stack = new LIFOQueue<String>();
        File currentCanonicalPath = f.getCanonicalFile();
        while (currentCanonicalPath != null) {
            stack.add(currentCanonicalPath.getName());
            currentCanonicalPath = currentCanonicalPath.getParentFile();
        }
        return stack;
    }

    private static int indexOfLastSeparator(String filename) {
        int lastUnixPos = filename.lastIndexOf(UNIX_SEPARATOR);
        int lastWindowsPos = filename.lastIndexOf(WINDOWS_SEPARATOR);
        return Math.max(lastUnixPos, lastWindowsPos);
    }

    private static int indexOfExtension(String filename) {
        int extensionPos = filename.lastIndexOf('.');
        int lastSeparator = indexOfLastSeparator(filename);
        return (lastSeparator > extensionPos ? -1 : extensionPos);
    }

    /**
	 * Get the extension part of a file.  For example,
	 * if the file points to a file named "Readme.txt"
	 * then this method will return "txt".
	 * @param file the file to get the extension for.  If this
	 * value is null, then null will be returned.
	 * @return the String of the extension, or null
	 * if the file is null, or the empty string "" if there is no extension.
	 */
    public static String getExtension(File file) {
        if (file == null) {
            return null;
        }
        String filename = file.getName();
        return getExtension(filename);
    }

    /**
	 * Get the extension part of a filepath.  For example,
	 * if the given filepath is to a file named "Readme.txt" or
	 * "/path/to/Readme.txt" 
	 * then this method will return "txt".
	 * @param filepath the filepath to get the extension for.  If this
	 * value is null, then null will be returned.
	 * @return the String of the extension, or null
	 * if filepath is null, or the empty string "" if there is no extension.
	 */
    public static String getExtension(String filepath) {
        if (filepath == null) {
            return null;
        }
        int extensionIndex = indexOfExtension(filepath);
        if (extensionIndex == -1) {
            return "";
        } else {
            return filepath.substring(extensionIndex + 1);
        }
    }

    /**
	 * Get the base portion of the file name from the given {@link File}.  For example,
	 * if the given {@link File} points to a file named "Readme.txt" or
	 * "/path/to/Readme.txt" 
	 * then this method will return "Readme".
	 * @param file the {@link File} to get the base name for.  If this
	 * value is null, then null will be returned.
	 * @return the String of the base name, or null
	 * if File is null, or the empty string "" if there is no basepath.
	 */
    public static String getBaseName(File file) {
        if (file == null) {
            return null;
        }
        return getBaseName(file.getName());
    }

    /**
	 * Get the base portion of the file name from the given filepath.  For example,
	 * if the given filepath is to a file named "Readme.txt" or
	 * "/path/to/Readme.txt" 
	 * then this method will return "Readme".
	 * @param filepath the filepath to get the base name for.  If this
	 * value is null, then null will be returned.
	 * @return the String of the base name, or null
	 * if filepath is null, or the empty string "" if there is no basepath.
	 */
    public static String getBaseName(String filename) {
        if (filename == null) {
            return null;
        }
        int lastSeparatorIndex = indexOfLastSeparator(filename);
        int extensionIndex = indexOfExtension(filename);
        if (extensionIndex == -1) {
            return filename.substring(lastSeparatorIndex + 1);
        } else {
            return filename.substring(lastSeparatorIndex + 1, extensionIndex);
        }
    }
}
