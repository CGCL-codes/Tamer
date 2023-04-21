package org.jcvi.util;

import java.io.File;
import java.io.FileFilter;
import java.io.Serializable;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Queue;
import org.jcvi.Builder;

/**
 * {@code FileIterator} is an {@link Iterator} for File objects.
 * @author dkatzel
 *
 *
 */
public abstract class FileIterator implements Iterator<File>, Iterable<File> {

    /**
     * Create a new {@link FileIteratorBuilder} instance that will create
     * a file iterator that will recursively iterate files in a depth
     * first manner. 
     * @param rootDir the root directory to start iterating from.
     * @return a new FileIteratorBuilder instance (will never be null)
     */
    public static FileIteratorBuilder createDepthFirstFileIteratorBuilder(File rootDir) {
        return new DepthFirstFileIteratorBuilder(rootDir);
    }

    /**
     * Create a new {@link FileIteratorBuilder} instance that will create
     * a file iterator that will recursively iterate files in a breadth
     * first manner. 
     * @param rootDir the root directory to start iterating from.
     * @return a new FileIteratorBuilder instance (will never be null)
     */
    public static FileIteratorBuilder createBreadthFirstFileIteratorBuilder(File rootDir) {
        return new BreadthFirstFileIteratorBuilder(rootDir);
    }

    /**
     * Create a new {@link FileIteratorBuilder} instance that will create
     * a file iterator that will only iterate files in the given directory
     * @param rootDir the directory iterate.
     * @return a new FileIteratorBuilder instance (will never be null)
     */
    public static FileIteratorBuilder createNonRecursiveFileIteratorBuilder(File dir) {
        return new NonRecursiveFileIteratorBuilder(dir);
    }

    private static final class FileNameComparator implements Comparator<File>, Serializable {

        /**
         * 
         */
        private static final long serialVersionUID = 4585888483429023724L;

        @Override
        public int compare(File o1, File o2) {
            return o1.getName().compareTo(o2.getName());
        }
    }

    /**
     * A FileFilter that only accepts files that are not 
     * directories.
     * @author dkatzel
     */
    private static final class NonDirectoryFileFilter implements FileFilter {

        @Override
        public boolean accept(File file) {
            return !file.isDirectory();
        }
    }

    private static final class NonHiddenFileFilter implements FileFilter {

        @Override
        public boolean accept(File file) {
            return !file.isHidden();
        }
    }

    private static final class NullFileFilter implements FileFilter {

        @Override
        public boolean accept(File file) {
            return true;
        }
    }

    private static final FileFilter NON_DIRECTORY_FILTER = new NonDirectoryFileFilter();

    private static final FileFilter NON_HIDDEN_FILTER = new NonHiddenFileFilter();

    private static final FileFilter NULL_FILTER = new NullFileFilter();

    private static final FileNameComparator FILE_NAME_SORTER = new FileNameComparator();

    private Iterator<File> fileIterator;

    private final FileFilter fileFilter;

    private File nextFile;

    private final File rootDir;

    protected FileIterator(File rootDir, FileFilter fileFilter) {
        if (rootDir == null) {
            throw new NullPointerException("rootDir can not be null");
        }
        if (!rootDir.isDirectory()) {
            throw new IllegalArgumentException("rootDir must be a directory");
        }
        if (fileFilter == null) {
            throw new NullPointerException("fileFilter can not be null");
        }
        this.fileFilter = fileFilter;
        this.rootDir = rootDir;
        setUpInitialState(rootDir);
    }

    protected void setUpInitialState(File rootDir) {
        updateFileIterator(rootDir);
        nextFile = getNextFile();
    }

    protected void updateFileIterator(File rootDir) {
        fileIterator = getFilesFor(rootDir);
    }

    private Iterator<File> getFilesFor(File dir) {
        final File[] listFiles = dir.listFiles(fileFilter);
        if (listFiles == null) {
            return EmptyIterator.createEmptyIterator();
        }
        Arrays.sort(listFiles, FILE_NAME_SORTER);
        return new ArrayIterable<File>(listFiles).iterator();
    }

    /**
     * Returns a new iterator with the same
     * parameters as this.
     */
    @Override
    public Iterator<File> iterator() {
        return createNewInstance(this.rootDir, fileFilter);
    }

    protected abstract Iterator<File> createNewInstance(File root, FileFilter fileFilter);

    protected File getNextFile() {
        if (fileIterator.hasNext()) {
            return fileIterator.next();
        }
        return null;
    }

    @Override
    public boolean hasNext() {
        return nextFile != null;
    }

    @Override
    public File next() {
        if (!hasNext()) {
            throw new NoSuchElementException("no more files");
        }
        File fileToReturn = nextFile;
        nextFile = getNextFile();
        return fileToReturn;
    }

    @Override
    public void remove() {
        throw new UnsupportedOperationException("can not remove");
    }

    private static class NonRecursiveFileIterator extends FileIterator {

        protected NonRecursiveFileIterator(File rootDir, FileFilter fileFilter) {
            super(rootDir, fileFilter);
        }

        @Override
        protected Iterator<File> createNewInstance(File root, FileFilter fileFilter) {
            return new NonRecursiveFileIterator(root, fileFilter);
        }
    }

    /**
     * RecursiveFileIterator adds recursive abilities to FileIterator.
     * @author dkatzel
     *
     *
     */
    private abstract static class RecursiveFileIterator extends FileIterator {

        /**
         * A FileFilter that only accepts files that ARE 
         * directories.
         * @author dkatzel
         */
        private static final class DirectoryFileFilter implements FileFilter {

            @Override
            public boolean accept(File file) {
                return file.isDirectory();
            }
        }

        private static final DirectoryFileFilter DIRECTORY_FILTER = new DirectoryFileFilter();

        private Queue<File> dirIterator;

        protected RecursiveFileIterator(File rootDir, FileFilter fileFilter) {
            super(rootDir, fileFilter);
        }

        /**
         * Create an empty Queue of Files
         * that will be used to track the directories
         * to recursively iterate over.  The implementation
         * of the Queue returned will determine the order
         * the directories will be iterated.
         * @return a new empty Queue.
         */
        protected abstract Queue<File> createDirectoryIterator();

        private List<File> getSubdirectoriesFor(File dir) {
            final File[] listFiles = dir.listFiles(DIRECTORY_FILTER);
            if (listFiles == null) {
                return Collections.emptyList();
            }
            Arrays.sort(listFiles, FILE_NAME_SORTER);
            return Arrays.asList(listFiles);
        }

        @Override
        protected void setUpInitialState(File rootDir) {
            dirIterator = createDirectoryIterator();
            dirIterator.addAll(getSubdirectoriesFor(rootDir));
            super.setUpInitialState(rootDir);
        }

        @Override
        protected File getNextFile() {
            File nextFile = super.getNextFile();
            if (nextFile == null && hasMoreSubDirs()) {
                return getNextFromSubDir();
            }
            return nextFile;
        }

        private boolean hasMoreSubDirs() {
            return !dirIterator.isEmpty();
        }

        private File getNextFromSubDir() {
            File newDir = dirIterator.poll();
            updateFileIterator(newDir);
            dirIterator.addAll(getSubdirectoriesFor(newDir));
            return getNextFile();
        }
    }

    private static class DepthFirstFileIterator extends RecursiveFileIterator {

        public DepthFirstFileIterator(File rootDir, FileFilter fileFilter) {
            super(rootDir, fileFilter);
        }

        @Override
        protected Queue<File> createDirectoryIterator() {
            return new LIFOQueue<File>();
        }

        @Override
        protected Iterator<File> createNewInstance(File root, FileFilter fileFilter) {
            return new DepthFirstFileIterator(root, fileFilter);
        }
    }

    private static class BreadthFirstFileIterator extends RecursiveFileIterator {

        public BreadthFirstFileIterator(File rootDir, FileFilter fileFilter) {
            super(rootDir, fileFilter);
        }

        @Override
        protected Queue<File> createDirectoryIterator() {
            return new FIFOQueue<File>();
        }

        @Override
        protected Iterator<File> createNewInstance(File root, FileFilter fileFilter) {
            return new BreadthFirstFileIterator(root, fileFilter);
        }
    }

    /**
     * {@code FileIteratorBuilder} is used to build a new instance of a 
     * {@link FileIterator}.
     * @author dkatzel
     *
     *
     */
    public abstract static class FileIteratorBuilder implements Builder<FileIterator> {

        private boolean includeDirectories = false;

        private boolean includeHiddenFiles = false;

        private FileFilter userDefinedFileFilter = null;

        private final File rootDir;

        public FileIteratorBuilder(File rootdir) {
            this.rootDir = rootdir;
        }

        /**
         * Should the iterator include the actual subdirectory directory files
         * during iteration.  Defaults to {@code false} if this method is not set.
         * <p/>For Example:<p/>
         * If the File Directory structure looks like this:
         * <pre>
         * +root
         *  | file1
         *  | file2
         *  +subdir
         *    | file3
         * </pre>
         * If this iterator is NOT recursive and if includeDirectories is
         * set to {@code true}, {@code file1}, {@code file2} and the file object
         * that represents {@code subdir} will be iterated over. If the iterator
         * is NOT recursive and if includeDirectories is set to {@code false}
         * then only {@code file1} and {@code file2} will be iterated over.
         * <p>
         * If this iterator IS recursive and if includeDirectories is
         * set to {@code true}, then {@code file1}, {@code file2} and the file object
         * that represents {@code subdir} and {@code file3} will be iterated over. 
         * 
         * If the iterator
         * IS recursive and if includeDirectories is set to {@code false}
         * then only {@code file1}, {@code file2} and {@code file3} will be iterated over.
         * 
         * @param includeDirectories should the iterator include 
         * the actual subdirectory directory files.
         * during iteration.
         * @return this.
         */
        public FileIteratorBuilder includeDirectories(boolean includeDirectories) {
            this.includeDirectories = includeDirectories;
            return this;
        }

        /**
         * Should hidden files be included in the file iteration. 
         * Defaults to {@code false} if this method is not set.
         * @param includeHiddenFiles {@code true} if hidden files should be included;
         * {@code false} otherwise.
         * @return this.
         */
        public FileIteratorBuilder includeHiddenFiles(boolean includeHiddenFiles) {
            this.includeHiddenFiles = includeHiddenFiles;
            return this;
        }

        /**
         * Add additional constraints to the fileIterator by specifying 
         * a {@link FileFilter} that will be used to further restrict what
         * files to iterate.  The given Filter (if any) is applied after
         * the constraints set by {@link #includeDirectories(boolean)}
         * and {@link #includeHiddenFiles(boolean)}.
         * @param fileFilter a {@link FileFilter} instance to further
         * filter the files to iterate; passing in {@code null}
         * means do not do any additional filtering.
         * @return this.
         */
        public FileIteratorBuilder fileFilter(FileFilter fileFilter) {
            this.userDefinedFileFilter = fileFilter;
            return this;
        }

        protected abstract FileIterator createFileIterator(File rootDir, FileFilter fileFilter);

        /**
         * Constructs a new FileIterator using the options set so far.
         * @return a new FileIterator (never null).
         */
        public FileIterator build() {
            FileFilter fileFilter;
            if (includeHiddenFiles) {
                if (includeDirectories) {
                    fileFilter = NULL_FILTER;
                } else {
                    fileFilter = NON_DIRECTORY_FILTER;
                }
            } else {
                if (includeDirectories) {
                    fileFilter = NON_HIDDEN_FILTER;
                } else {
                    fileFilter = new MultipleFileFilter(NON_DIRECTORY_FILTER, NON_HIDDEN_FILTER);
                }
            }
            if (userDefinedFileFilter != null) {
                fileFilter = new MultipleFileFilter(fileFilter, userDefinedFileFilter);
            }
            return createFileIterator(rootDir, fileFilter);
        }
    }

    private static final class BreadthFirstFileIteratorBuilder extends FileIteratorBuilder {

        /**
         * @param rootdir
         */
        public BreadthFirstFileIteratorBuilder(File rootdir) {
            super(rootdir);
        }

        /**
        * {@inheritDoc}
        */
        @Override
        protected FileIterator createFileIterator(File rootDir, FileFilter fileFilter) {
            return new BreadthFirstFileIterator(rootDir, fileFilter);
        }
    }

    private static final class DepthFirstFileIteratorBuilder extends FileIteratorBuilder {

        /**
         * @param rootdir
         */
        public DepthFirstFileIteratorBuilder(File rootdir) {
            super(rootdir);
        }

        /**
        * {@inheritDoc}
        */
        @Override
        protected FileIterator createFileIterator(File rootDir, FileFilter fileFilter) {
            return new DepthFirstFileIterator(rootDir, fileFilter);
        }
    }

    private static final class NonRecursiveFileIteratorBuilder extends FileIteratorBuilder {

        /**
         * @param rootdir
         */
        public NonRecursiveFileIteratorBuilder(File rootdir) {
            super(rootdir);
        }

        /**
        * {@inheritDoc}
        */
        @Override
        protected FileIterator createFileIterator(File rootDir, FileFilter fileFilter) {
            return new NonRecursiveFileIterator(rootDir, fileFilter);
        }
    }
}
