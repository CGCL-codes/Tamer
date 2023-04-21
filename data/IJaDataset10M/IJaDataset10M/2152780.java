package org.openofficesearch.io;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import org.apache.commons.io.filefilter.IOFileFilter;

/**
 * A file filter that uses the test method of one or more document readers to
 * determine whether to accept a file<br />
 * Created: Feb 10, 2008 5:10:07 PM
 * @author Connor Garvey
 * @version 0.1.1
 * @since 0.1.0
 */
public class ReaderFileFilter implements IOFileFilter {

    private List<DocumentReader> readers;

    /**
   * Creates a new reader file filter
   */
    protected ReaderFileFilter() {
        this.setReaders(new ArrayList<DocumentReader>());
    }

    /**
   * Creates a new reader file filter
   * @param readers The document readers this file filter will use to determine
   *    which files to accept
   */
    protected ReaderFileFilter(List<DocumentReader> readers) {
        this.setReaders(readers);
    }

    /**
   * Tests if a specified file should be included in a file list.
   *
   * @param dir the directory in which the file was found.
   * @param name the name of the file.
   * @return <code>true</code> if and only if the name should be included in
   *   the file list; <code>false</code> otherwise.
   */
    public boolean accept(File dir, String name) {
        if ((name == null) || (name.equals(""))) {
            return false;
        }
        File file = new File(dir.getPath() + File.separator + name);
        return this.accept(file);
    }

    /**
   * Determines whether a document is an OpenOffice document
   * @param file The file to be tested
   * @return True if the document is an OpenOffice document, false otherwise
   */
    public boolean accept(File file) {
        for (DocumentReader reader : this.readers) {
            if (reader.test(file)) {
                return true;
            }
        }
        return false;
    }

    /**
   * Adds a reader to be used to test files
   * @param reader The reader to be added
   */
    public void addReader(DocumentReader reader) {
        this.readers.add(reader);
    }

    /**
   * Sets the list of readers
   * @param readers A list of document readers to be used to test files
   */
    public void setReaders(List<DocumentReader> readers) {
        this.readers = readers;
    }
}
