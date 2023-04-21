package consciouscode.util;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.StringTokenizer;

/**
    Navigates the <code>.class</code> files in a classpath.

    The iteration returns the fully-qualified class names.
*/
public class ClassPathIterator implements Iterator<String> {

    public static final String EXTENSION = ".class";

    /**
       Create an iterator that returns the classes in a single directory tree.

       @param directory must indicate a valid classpath directory; it is
       assumed that the files within this directory are arranged in accordance
       with their package structure.
    */
    public ClassPathIterator(File directory) throws IOException {
        if (!directory.isDirectory() || !directory.canRead()) {
            String message = "Parameter 'directory' is not a readable directory: " + directory;
            throw new IllegalArgumentException(message);
        }
        addDirectory(directory);
        myClassNameIterator = myClassNameList.iterator();
    }

    /**
       Create an iterator that returns the classes in a list of directories.

       @param pathList must be a standard Java class path, where each directory
       has a structure matching the class package hierarchy. Multiple directory
       roots are indicated using the platform's {@link File#pathSeparator}.
       Any elements that do not identify a readable directory are ignored.
    */
    public ClassPathIterator(String pathList) throws IOException {
        StringTokenizer tokenizer = new StringTokenizer(pathList, File.pathSeparator);
        while (tokenizer.hasMoreTokens()) {
            File directory = new File(tokenizer.nextToken());
            if (directory.isDirectory() && directory.canRead()) {
                addDirectory(directory);
            }
        }
        myClassNameIterator = myClassNameList.iterator();
    }

    /**
       Return the fully-qualified name of the next class.
    */
    public final String nextClassName() {
        return myClassNameIterator.next();
    }

    public boolean hasNext() {
        return myClassNameIterator.hasNext();
    }

    /**
       Return a String holding the fully-qualified name of the next class.
    */
    public String next() {
        return myClassNameIterator.next();
    }

    /**
       Unsupported operation.
       @throws UnsupportedOperationException in all cases.
     */
    public void remove() throws UnsupportedOperationException {
        throw new UnsupportedOperationException();
    }

    private void addDirectory(File directory) throws IOException {
        if (!directory.isDirectory() || !directory.canRead()) {
            String message = "Parameter 'directory' is not a readable directory: " + directory;
            throw new IllegalArgumentException(message);
        }
        accumulateClassNames(directory, directory.getAbsolutePath());
    }

    private void accumulateClassNames(File directory, final String rootPath) throws IOException {
        File[] files = directory.listFiles();
        if (files == null) {
            throw new IOException("Unable to list files in directory " + directory);
        }
        for (int i = 0; i < files.length; i++) {
            File file = files[i];
            String fileName = file.getName();
            if (fileName.endsWith(EXTENSION) && file.isFile() && file.canRead()) {
                noteClassNameForFile(file, rootPath);
            }
        }
        for (int i = 0; i < files.length; i++) {
            File file = files[i];
            if (file.isDirectory()) {
                accumulateClassNames(file, rootPath);
            }
        }
    }

    private void noteClassNameForFile(File file, String rootPath) {
        String fullPath = file.getAbsolutePath();
        if (fullPath.startsWith(rootPath)) {
            String classNameAsPath = fullPath.substring(rootPath.length() + 1, fullPath.length() - EXTENSION.length());
            String className = classNameAsPath.replace(File.separatorChar, '.');
            myClassNameList.add(className);
        }
    }

    private ArrayList<String> myClassNameList = new ArrayList<String>();

    private Iterator<String> myClassNameIterator;
}
