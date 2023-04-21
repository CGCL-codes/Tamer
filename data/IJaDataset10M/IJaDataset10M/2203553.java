package org.openofficesearch.io;

import java.io.File;
import java.io.FileFilter;
import java.io.FileNotFoundException;
import java.util.List;
import java.util.LinkedList;
import java.io.IOException;
import java.util.Iterator;
import javax.naming.OperationNotSupportedException;
import org.openofficesearch.plugin.ReaderPluginManager;

/**
 * Reads the files in a directory recursively, returning one word at a time,
 * automatically moving from one file to the next<br />
 * Created: 2005
 * @author Connor Garvey
 * @version 0.1.1
 * @since 0.0.1
 */
public class RecursiveDirectoryReader implements DirectoryReader {

    private File directory;

    private List<File> documents;

    private FileFilter fileFilter;

    private DocumentReader reader;

    /**
   * No argument constructor is declared private because we don't want to use it
   */
    private RecursiveDirectoryReader() throws OperationNotSupportedException {
        throw new OperationNotSupportedException();
    }

    /**
   * Constructor, creates a new instance of this class
   * @param directory File The directory to be read
   * @throws java.io.FileNotFoundException Thrown if the directory could not be
   *         found or if a file could not be found
   * @throws org.openofficesearch.io.IsNotDirectoryException Thrown if the
   *         parameter is not a directory
   * @throws org.openofficesearch.io.FileReadAccessException Thrown if the
   *         reader could not obtain read access to a file
   */
    public RecursiveDirectoryReader(File directory) throws FileNotFoundException, IsNotDirectoryException, FileReadAccessException {
        if (directory == null) {
            throw new IllegalArgumentException("The directory to be read can't be null");
        }
        if (!directory.exists()) {
            throw new FileNotFoundException();
        }
        if (!directory.isDirectory()) {
            throw new IsNotDirectoryException(directory.getPath());
        }
        if (!directory.canRead()) {
            throw new FileReadAccessException(directory.getPath());
        }
        this.fileFilter = ReaderPluginManager.getFileAndDirectoryFilter();
        this.directory = directory;
        this.documents = this.getChildren(directory);
    }

    /**
   * Loads a reader for the next document in the collection. The document is
   * removed from the collection before a read is attempted so that if reading
   * fails, this method can be called on the next document
   * @return File The path of the document loaded
   * @throws IllegalArgumentException Thrown if there was a problem with one of
   *         the file objects in the file list
   * @throws IOException Thrown if a file could not be read
   * @throws IsNotFileException Thrown if a file is not a file
   * @throws DocumentParseException Thrown if a file could not be parsed
   */
    private File loadNextReader() throws DocumentParseException, IllegalArgumentException, IOException, IsNotFileException, EndOfDirectoryException {
        ReaderPluginManager.unloadDocumentReaders();
        File currentDocument = null;
        if (this.documents.size() > 0) {
            currentDocument = this.documents.remove(0);
            for (DocumentReader currentReader : ReaderPluginManager.getDocumentReaders()) {
                if (currentReader.test(currentDocument)) {
                    currentReader.load(currentDocument);
                    this.reader = currentReader;
                    return currentDocument;
                }
            }
        } else {
            throw new EndOfDirectoryException();
        }
        return null;
    }

    /**
   * Gets a list of all of the documents that are children of a file
   * @param file File The file for which the children are to be found
   * @return List A list of all of the children and sub-children of the file. If
   *         the file does not have any children, an empty list is returned.
   */
    private List<File> getChildren(File file) {
        List<File> allChildren = new LinkedList<File>();
        if (!file.isDirectory()) {
            return allChildren;
        }
        File[] directChildren = file.listFiles(this.fileFilter);
        if ((directChildren == null) || (directChildren.length == 0)) {
            return allChildren;
        }
        String fileParent = file.getPath();
        File currentFile = null;
        List<File> currentChildren = null;
        Iterator<File> it = null;
        for (int i = 0; i < directChildren.length; i++) {
            currentFile = directChildren[i];
            if (currentFile.isDirectory()) {
                currentChildren = this.getChildren(currentFile);
                it = currentChildren.iterator();
                while (it.hasNext()) {
                    allChildren.add(it.next());
                }
            } else {
                allChildren.add(currentFile);
            }
        }
        return allChildren;
    }

    /**
   * Retrieves the next word from the directory
   * @return The next word in the directory
   * @throws java.lang.IllegalStateException Thrown if the application is not in
   *    a state to read
   * @throws org.openofficesearch.io.EndOfDocumentException Thrown if the end of
   *    the directory has been reached
   * @throws java.io.IOException Thrown if a file in the directory could not be
   *    read
   * @throws org.openofficesearch.io.DocumentParseException Thrown if a file in
   *    the directory could not be parsed
   */
    @Override
    public String getNextWord() throws IllegalStateException, EndOfDocumentException, IOException, DocumentParseException {
        if (this.reader == null) {
            throw new IllegalStateException("A document must be loaded before " + "reading can begin");
        }
        String word = this.reader.getNextWord();
        return word;
    }

    /**
   * Moves the reader to the beginning of the directory
   */
    @Override
    public void reset() {
        this.unload();
        this.documents = this.getChildren(this.directory);
    }

    private void unload() {
        this.documents = null;
        this.reader = null;
        ReaderPluginManager.unloadDocumentReaders();
    }

    /**
   * Loads the next document in the directory
   * @return The next file in the directory
   * @throws org.openofficesearch.io.EndOfDirectoryException Thrown if the end
   *    of the dircectory has been reached
   * @throws java.io.IOException Thrown if the next file could not be read
   */
    @Override
    public File loadNextDocument() throws EndOfDirectoryException, IOException {
        File loadedDocument = null;
        while (true) {
            try {
                loadedDocument = this.loadNextReader();
                return loadedDocument;
            } catch (EndOfDirectoryException ex) {
                throw ex;
            } catch (IsNotFileException ex) {
            } catch (IOException ex) {
                throw ex;
            } catch (IllegalArgumentException ex) {
            } catch (DocumentParseException ex) {
            }
        }
    }
}
