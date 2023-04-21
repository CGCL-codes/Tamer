package in.jquickfinder.filesearch;

import in.jquickfinder.filters.DirectoryFileFilter;
import in.jquickfinder.filters.FalseFileFilter;
import in.jquickfinder.filters.TrueFileFilter;
import in.jquickfinder.filters.IOFileFilter;
import java.io.File;
import java.io.FileFilter;
import java.util.Collection;
import java.util.LinkedList;

/**
 *
 * @author Administrator
 */
public class SearchFileRecursive {

    public static Collection searchFiles(File directoryPath) {
        IOFileFilter filter = TrueFileFilter.INSTANCE;
        Collection listFiles = listFiles(directoryPath, filter, TrueFileFilter.INSTANCE);
        return listFiles;
    }

    private static Collection listFiles(File directory, IOFileFilter fileFilter, IOFileFilter dirFilter) {
        if (!directory.isDirectory()) {
            throw new IllegalArgumentException("Parameter 'directory' is not a directory");
        }
        if (fileFilter == null) {
            throw new NullPointerException("Parameter 'fileFilter' is null");
        }
        IOFileFilter effFileFilter = FileFilterUtils.and(fileFilter, FileFilterUtils.notFileFilter(DirectoryFileFilter.INSTANCE));
        IOFileFilter effDirFilter;
        if (dirFilter == null) {
            effDirFilter = FalseFileFilter.INSTANCE;
        } else {
            effDirFilter = FileFilterUtils.and(dirFilter, DirectoryFileFilter.INSTANCE);
        }
        Collection<File> files = new LinkedList<File>();
        innerListFiles(files, directory, FileFilterUtils.or(effFileFilter, effDirFilter));
        return files;
    }

    private static void innerListFiles(Collection<File> files, File directory, IOFileFilter filter) {
        File[] found = directory.listFiles((FileFilter) filter);
        if (found != null) {
            for (File file : found) {
                if (file.isDirectory()) {
                    innerListFiles(files, file, filter);
                } else {
                    files.add(file);
                }
            }
        }
    }
}
