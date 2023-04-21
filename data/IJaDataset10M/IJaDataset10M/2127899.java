package org.unicore.outcome;

import java.util.Date;
import org.unicore.sets.XFileSet;

/**
 * Information about a directory in the listing from a {@link ListDirectory_Outcome}.
 * <p>
 * A directory in a listing. This contains information
 * about itself and entries for each of the files in the directory.
 *
 * @see ListDirectory_Outcome
 * 
 * @author S. van den Berghe (fecit)
 *
 * @since AJO 3
 *
 * @version $Id: XDirectory.java,v 1.2 2004/05/26 16:31:44 svenvdb Exp $
 * 
 **/
public class XDirectory extends XFile {

    static final long serialVersionUID = 2118340419194662585L;

    /**
     * Create a new XDirectory.
     *
     * @param name              The name of the directory (full path to)
     * @param size              The size of the directory in bytes.
     * @param modify_date       The time of last changes to directory
     * @param owns              true if the Xlogin owns the directory
     * @param can_write         true if the Xlogin can write to the directory
     * @param can_read          true if the Xlogin can read the directory
     * @param can_execute       true if the Xlogin can  execute the directory
     * @param extra_information Any further information aboutthe directory returned by the target OS.
     * @param files             The files in this directory.
     *
     **/
    public XDirectory(String name, int size, Date modify_date, boolean owns, boolean can_write, boolean can_read, boolean can_execute, String extra_information, XFileSet files) {
        super(name, size, modify_date, owns, can_write, can_read, can_execute, extra_information);
        this.files = files;
    }

    /**
     * Create a new XDirectory.
     *
     **/
    public XDirectory() {
        super();
    }

    private XFileSet files;

    /**
     * Return the files in the directory
     *
     **/
    public XFileSet getFiles() {
        if (files == null) files = new XFileSet();
        return files;
    }

    /**
     * Set the files in the directory
     *
     **/
    public void setFiles(XFileSet files) {
        this.files = files;
    }

    /**
     * Add a file to this directory
     *
     **/
    public void addFile(XFile file) {
        if (files == null) {
            files = new XFileSet();
        }
        files.add(file);
    }
}
