package net.sf.komodo.core.blackboard;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import org.apache.commons.io.FilenameUtils;

/**
 * Represents a file, the basic unit of work that connects a programmer's
 * project to a filesystem and the basic unit of work available to a parser.
 * This file can be annotated with zero or more Abstract Syntax Trees.
 * 
 * 
 */
public class SourceFile {

    private File file;

    /**
	 * Create a new ProjectFile from a given file. There are no ASTs associated with
	 * a newly created ProjectFile.
	 */
    public SourceFile(File file) {
        this.file = file;
    }

    /**
	 * Get the base name of the ProjectFile. Given the file /foo/bar.txt, the base
	 * name is 'bar'.
	 * 
	 * @return the base name of the file
	 */
    public String getBasename() {
        return FilenameUtils.getBaseName(this.file.getName());
    }

    /**
	 * Get the file extension of the ProjectFile. Given the file /foo/bar.txt, the
	 * extension is 'txt'.
	 * 
	 * @return the extension of the ProjectFile
	 */
    public String getExtension() {
        return FilenameUtils.getExtension(this.file.getName());
    }

    /**
	 * Get the location of the ProjectFile. The location is the directory of the
	 * file; given the file /foo/bar.txt, the location is '/foo/'.
	 * 
	 * @return the location of the file
	 */
    public String getLocation() {
        return FilenameUtils.getFullPath(this.file.getAbsolutePath());
    }

    /**
	 * Get the URI of the file as a string
	 * @return the URI of the file
	 */
    public String getURI() {
        return this.file.toURI().toASCIIString();
    }

    /**
	 * Get the File object represented by this ProjectFile, for use in Input/Output streams and readers.
	 * @return the File object representing this ProjectFile
	 */
    public File getFile() {
        return this.file;
    }

    /**
	 * Get an InputStream to this SourceFile.  The contents may be cached.
	 * @return an InputStream to the SourceFile
	 * @throws IOException if there was an exception while creating the InputStream
	 */
    public InputStream getInputStream() throws IOException {
        return new BufferedInputStream(new FileInputStream(this.file));
    }
}
