package net.sourceforge.javabits.io;

import static net.sourceforge.javabits.lang.Objects.ensureNotNull;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.StringTokenizer;
import net.sourceforge.javabits.error.ErrorHandler;
import net.sourceforge.javabits.lang.IllegalParameterValueException;
import net.sourceforge.javabits.lang.Strings;
import org.codehaus.plexus.util.DirectoryScanner;

/**
 * File utility functions.
 * 
 * @author Jochen Kuhnle
 */
public abstract class Files {

    public static final Set<File> EMPTY_SET = Collections.emptySet();

    public static final File[] EMPTY_ARRAY = {};

    public static final File EMPTY_FILE = new File("");

    public static final File CURRENT_DIR = new File(".");

    public static final File PARENT_DIR = new File("..");

    public static final Set<File> ROOT_DIRS = Collections.unmodifiableSet(new HashSet<File>(Arrays.asList(File.listRoots())));

    /**
     * Returns the base name of a file.
     * <p>
     * The base name is the part of the file name before the last '.'. If the
     * file name contains no '.', the file name itself is returned.
     * 
     * @param file File
     * @return File's base name
     */
    public static final String baseName(File file) {
        return Strings.leftBack(file.getName(), ".", file.getName());
    }

    /**
     * Returns the base path of a file.
     * <p>
     * The base name is the part of the file path before the last '.'. If the
     * file path contains no '.', the file path itself is returned.
     * 
     * @param file File
     * @return File's base name
     */
    public static final String basePath(File file) {
        return Strings.concat(file.getParent(), baseName(file), File.separator);
    }

    /**
     * Returns the extension of a file.
     * <p>
     * The extension is the part of the file name starting with (and including)
     * the last '.'. If the file name contains no '.', an empty string is
     * returned.
     * 
     * @param file File
     * @return File's extension
     */
    public static final String extension(File file) {
        String extension = "";
        String name = file.getName();
        int pos = name.lastIndexOf('.');
        if (pos >= 0) {
            extension = name.substring(pos);
        }
        return extension;
    }

    /**
     * Composes a file based on a directory.
     * <p>
     * If the supplied file is absolute, or the base directory is null, the file
     * is returned unchanged. Otherwise, it is appended to the supplied base
     * directory.
     * 
     * @param directory Base file
     * @param file Offset file
     * @return File based on the base file
     */
    public static final File compose(File directory, File file) {
        File result;
        if (directory == null) {
            if (file == null) {
                result = null;
            } else {
                result = file;
            }
        } else {
            if (file == null) {
                result = directory;
            } else {
                if (file.isAbsolute()) {
                    result = file;
                } else {
                    result = new File(directory, file.getPath());
                }
            }
        }
        return result;
    }

    /**
     * Composes a file based on a directory.
     * <p>
     * If the supplied file is absolute, or the base directory is null, the file
     * is returned unchanged. Otherwise, it is appended to the supplied base
     * directory.
     * 
     * @param directory Base file
     * @param file Offset file
     * @return File based on the base file
     */
    public static final File compose(File directory, String file) {
        return compose(directory, Files.create(file));
    }

    private static final File create(String file) {
        return (file == null) ? null : new File(file);
    }

    /**
     * Resolves a file based on a directory.
     * <p>
     * Composes a file out of a directory and another file, and normalizes it.
     * 
     * @param directory Base file
     * @param file Offset file
     * @return File based on the base file
     */
    public static final File resolve(File directory, File file) {
        return normalize(compose(directory, file));
    }

    /**
     * Resolves a file based on a directory.
     * <p>
     * Composes a file out of a directory and another file, and normalizes it.
     * 
     * @param directory Base file
     * @param file Offset file
     * @return File based on the base file
     */
    public static final File resolve(File directory, String file) {
        return normalize(compose(directory, file));
    }

    public static final File canonicalize(File file) {
        try {
            return file.getCanonicalFile();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Cleans the path name of a file by removing all ".", "//" and as many
     * ".."s as possible.
     * <p>
     * If the path contains more ".."s than real directory names, all ".." are
     * moved to the beginning.
     * <p>
     * Example: cleanPath("dir/../../dir2/../../dir3"): "../../dir3"
     * 
     * @param file File to clean
     * @return Cleaned file
     */
    public static final File normalize(File file) {
        String[] parts = parts(file);
        List<String> partStack = new ArrayList<String>();
        int parentCounter = 0;
        boolean absolute = file.isAbsolute();
        int partStart = parts.length;
        int prefixStart = parts.length;
        for (int i = 0; i < parts.length; ++i) {
            if (parts[i].length() > 0) {
                prefixStart = i;
                partStart = i;
                break;
            }
        }
        if (prefixStart < parts.length) {
            String prefix = parts[prefixStart] + File.separator;
            for (File f : File.listRoots()) {
                if (prefix.equals(f.getPath())) {
                    ++partStart;
                    break;
                }
            }
        }
        for (int i = partStart; i < parts.length; ++i) {
            String part = parts[i];
            if (part.equals("..")) {
                if (partStack.size() == 0) {
                    if (absolute) {
                        throw new IllegalParameterValueException("file", file);
                    }
                    ++parentCounter;
                } else {
                    partStack.remove(partStack.size() - 1);
                }
            } else if (part.length() > 0 && !part.equals(".")) {
                partStack.add(part);
            }
        }
        StringBuilder buf = new StringBuilder();
        if (absolute) {
            buf.append(File.separatorChar);
        }
        for (int i = prefixStart; i < partStart; ++i) {
            buf.append(parts[i]);
            buf.append(File.separatorChar);
        }
        for (int i = 0; i < parentCounter; ++i) {
            buf.append("..");
            buf.append(File.separatorChar);
        }
        for (String part : partStack) {
            buf.append(part);
            buf.append(File.separatorChar);
        }
        if (buf.length() > 1) {
            Strings.cutoff(buf, 1);
        }
        File result = new File(buf.toString());
        return result;
    }

    public static final File child(File directory, File file) {
        File child = null;
        File normalizedDirectory = normalize(directory);
        File normalizedFile = resolve(directory, file);
        if (normalizedFile.getPath().startsWith(normalizedDirectory.getPath() + File.separator)) {
            child = new File(normalizedFile.getPath().substring(normalizedDirectory.getPath().length() + 1));
        }
        return child;
    }

    public static final File local(File directory, File file) {
        File local = child(directory, file);
        if (local == null) {
            if (directory.equals(file)) {
                local = CURRENT_DIR;
            } else {
                local = resolve(directory, file);
            }
        }
        return local;
    }

    public static File newest(Collection<? extends File> fileCollection) {
        File result = null;
        long newestTime = Long.MIN_VALUE;
        for (File file : fileCollection) {
            long lastModified = file.lastModified();
            if (lastModified > newestTime) {
                newestTime = lastModified;
                result = file;
            }
        }
        return result;
    }

    public static File oldest(Collection<? extends File> fileCollection) {
        File result = null;
        long oldestTime = Long.MAX_VALUE;
        for (File file : fileCollection) {
            long lastModified = file.lastModified();
            if (lastModified < oldestTime) {
                oldestTime = lastModified;
                result = file;
            }
        }
        return result;
    }

    public static final File relative(File base, File file) {
        File normalizedBase = normalize(base);
        File normalizedFile = normalize(file);
        if (normalizedBase == null) {
            throw new IllegalArgumentException(String.format("Illegal base file '%s'.", base));
        }
        if (normalizedFile == null) {
            throw new IllegalArgumentException(String.format("Illegal file '%s'.", file));
        }
        File prefix = commonPrefix(normalizedBase, normalizedFile);
        int length = prefix.getPath().length();
        File result = Files.CURRENT_DIR;
        if (length < normalizedBase.getPath().length()) {
            normalizedBase = new File(normalizedBase.getPath().substring(length + 1));
            normalizedFile = new File(normalizedFile.getPath().substring(length + 1));
            String[] parts = parts(normalizedBase);
            StringBuilder buf = new StringBuilder();
            for (int i = 0; i < parts.length; ++i) {
                buf.append("../");
            }
            buf.append(normalizedFile.getPath());
            result = new File(buf.toString());
        }
        return result;
    }

    public static final File replaceExtension(File file, String extension) {
        String baseName = Files.baseName(file);
        return new File(file.getParentFile(), baseName + extension);
    }

    public static final boolean isOutdated(File target, File source, long tolerance) {
        boolean result = false;
        if (!target.exists() || (target.lastModified() + tolerance < source.lastModified())) {
            result = true;
        }
        return result;
    }

    /**
     * Gets the list of source files in the specified directories.
     * <p>
     * All directories are scanned for files, whose names are then added to the
     * generated list.
     * 
     * @param directoryCollection Collection of directories
     * @param scanner Directory scanner to use
     * @return The list of source file names
     */
    public static final <C extends Collection<? super File>> C find(Collection<? extends File> directoryCollection, DirectoryScanner scanner, C targetCollection) {
        for (File root : directoryCollection) {
            if (root.exists()) {
                scanner.setBasedir(root.getPath());
                scanner.scan();
                String[] files = scanner.getIncludedFiles();
                for (int k = 0; k < files.length; ++k) {
                    File f = new File(scanner.getBasedir(), files[k]);
                    targetCollection.add(f);
                }
            }
        }
        return targetCollection;
    }

    public static final List<File> find(Collection<? extends File> directoryCollection, DirectoryScanner scanner) {
        return find(directoryCollection, scanner, new ArrayList<File>());
    }

    public static boolean isChild(File parent, File file) {
        return child(parent, file) != null;
    }

    public static final List<String> partList(File file) {
        List<String> partList = new ArrayList<String>();
        for (StringTokenizer st = new StringTokenizer(file.getPath(), File.separator); st.hasMoreTokens(); ) {
            partList.add(st.nextToken());
        }
        return partList;
    }

    public static final String[] parts(File file) {
        List<String> partList = partList(file);
        return partList.toArray(new String[partList.size()]);
    }

    public static final File fromPartList(List<String> partList) {
        StringBuilder buf = new StringBuilder();
        for (String part : partList) {
            buf.append(part);
            buf.append(File.separatorChar);
        }
        return new File(buf.toString());
    }

    public static final File commonPrefix(File file1, File file2) {
        String[] parts1 = parts(file1);
        String[] parts2 = parts(file2);
        int min = Math.min(parts1.length, parts2.length);
        StringBuilder buf = new StringBuilder();
        for (int i = 0; i < min; ++i) {
            if (!parts1[i].equals(parts2[i])) {
                break;
            }
            buf.append(parts1[i]);
            buf.append(File.separatorChar);
        }
        File result = new File(buf.toString());
        return result;
    }

    public static final File shortestPath(File directory, File file) {
        File result = resolve(directory, file);
        File relative = relative(result, normalize(file));
        if (relative.getPath().length() < result.getPath().length()) {
            result = relative;
        }
        return result;
    }

    public static final boolean isExistingFile(ErrorHandler err, File file) {
        boolean result = true;
        if (!file.exists()) {
            err.error("File '%s' does not exist.", file);
            result = false;
        } else if (!file.isFile()) {
            err.error("'%s' is not a file.", file);
            result = false;
        }
        return result;
    }

    public static final boolean isExistingDirectory(ErrorHandler err, File file) {
        boolean result = true;
        if (!file.exists()) {
            err.error("File '%s' does not exist.", file);
            result = false;
        } else if (!file.isDirectory()) {
            err.error("'%s' is not a directory.", file);
            result = false;
        }
        return result;
    }

    public static final void createParentDirectory(File file) {
        File directory = file.getParentFile();
        if (directory != null) {
            directory.mkdirs();
        }
    }

    public static final void ensureAbsolute(String name, File file) {
        ensureNotNull(name, file);
        if (!file.isAbsolute()) {
            throw new IllegalParameterValueException(name, file);
        }
    }
}
