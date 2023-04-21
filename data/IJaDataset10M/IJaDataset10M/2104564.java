package org.synthful.io;

import java.io.FileFilter;
import java.io.File;
import java.util.Date;
import org.apache.commons.net.ftp.FTPFile;

/**
 * ListFilesFilter Class.
 */
public class ListFilesFilter implements FileFilter {

    /**
	 * Instantiates a new ListFilesFilter.
	 */
    public ListFilesFilter() {
    }

    /**
	 * Instantiates a new ListFilesFilter.
	 * 
	 * @param glob
	 *            the glob string
	 */
    public ListFilesFilter(String glob) {
        setAcceptNameFilter(glob);
    }

    /**
	 * ********************************************************************
	 * Accept Methods
	 * ********************************************************************.
	 * 
	 * @param file
	 *            the file
	 * 
	 * @return true, if file is acceptable
	 */
    public boolean accept(File file) {
        if (!acceptFileName(file.getName())) return false;
        if (!acceptFileDate(file)) return false;
        if (!acceptFileSize(file)) return false;
        return true;
    }

    /**
	 * Accept to be used for org.apache.commons.net.ftp.FTPFile.
	 * 
	 * @param file
	 *            the file
	 * 
	 * @return true, if FTPFile is acceptable
	 * @see org.apache.commons.net.ftp.FTPFile
	 */
    public boolean accept(FTPFile file) {
        if (!acceptFileName(file.getName())) return false;
        if (!acceptFileDate(file)) return false;
        if (!acceptFileSize(file)) return false;
        return true;
    }

    /**
	 * Accept.
	 * 
	 * @param filename
	 *            the filename
	 * 
	 * @return true, if filename is acceptable
	 */
    public boolean accept(String filename) {
        if (!acceptFileName(filename)) return false;
        return true;
    }

    /**
	 * ********************************************************************
	 * Auxiliary Accept Methods: File Date
	 * ********************************************************************.
	 * 
	 * @param filename
	 *            the filename
	 * 
	 * @return true, if accept file name
	 */
    public boolean acceptFileName(String filename) {
        if (filename.equals(".") || filename.equals("..")) return false;
        if (NameRegexp != null && NameRegexp.length() > 0) if (!filename.matches(NameRegexp)) return false;
        return true;
    }

    /**
	 * ********************************************************************
	 * Auxiliary Accept Methods: File Date
	 * ********************************************************************.
	 * 
	 * @param file
	 *            the file
	 * 
	 * @return true, if accept file date
	 */
    private boolean acceptFileDate(File file) {
        long lmodif = file.lastModified();
        Date dmodif = new Date(lmodif);
        return acceptFileDate(dmodif);
    }

    /**
	 * Accept file date.
	 * 
	 * @param file
	 *            the file
	 * 
	 * @return true, if successful
	 */
    private boolean acceptFileDate(FTPFile file) {
        Date dmodif = file.getTimestamp().getTime();
        return acceptFileDate(dmodif);
    }

    /**
	 * Accept file date.
	 * 
	 * @param dmodif
	 *            the dmodif
	 * 
	 * @return true, if successful
	 */
    private boolean acceptFileDate(Date dmodif) {
        if (Oldest == null && Newest == null) return true;
        if (Oldest != null) if (dmodif.compareTo(Oldest) < 0) return false;
        if (Newest != null) if (dmodif.compareTo(Newest) > 0) return false;
        return true;
    }

    /**
	 * 
	 * @param file
	 *            the file
	 * 
	 * @return true, if accept file size
	 */
    private boolean acceptFileSize(File file) {
        long size = file.length();
        return acceptFileSize(size);
    }

    /**
	 * Accept file size.
	 * 
	 * @param file
	 *            the file
	 * 
	 * @return true, if successful
	 */
    private boolean acceptFileSize(FTPFile file) {
        long size = file.getSize();
        return acceptFileSize(size);
    }

    /**
	 * Accept file size.
	 * 
	 * @param size
	 *            the size
	 * 
	 * @return true, if successful
	 */
    private boolean acceptFileSize(long size) {
        if (Smallest < 0 && Largest < 0) return true;
        if (Smallest >= 0 && size < Smallest) return false;
        if (Largest >= 0 && size > Largest) return false;
        return true;
    }

    /**
	 * ******************************************************************** Set
	 * Filter Methods
	 * ********************************************************************.
	 * 
	 * @param oldest
	 *            the oldest
	 * @param newest
	 *            the newest
	 * 
	 * @return the list files filter
	 */
    public ListFilesFilter setAcceptDateFilter(Date oldest, Date newest) {
        Oldest = oldest;
        Newest = newest;
        return this;
    }

    /**
	 * Sets the accept name filter.
	 * 
	 * @param glob
	 *            the glob
	 * 
	 * @return the list files filter
	 */
    public ListFilesFilter setAcceptNameFilter(String glob) {
        String pattern = glob.replaceAll("" + backslash + '.', "" + backslash + backslash + '.').replaceAll("" + backslash + "*", "" + backslash + '.' + backslash + '*').replaceAll("" + backslash + "?", "" + backslash + '.');
        NameRegexp = '^' + pattern + '$';
        return this;
    }

    /**
	 * Sets the accept name regexp.
	 * 
	 * @param pattern
	 *            the pattern
	 * 
	 * @return the list files filter
	 */
    public ListFilesFilter setAcceptNameRegexp(String pattern) {
        NameRegexp = pattern;
        return this;
    }

    /**
	 * Sets the accept size filter.
	 * 
	 * @param smallest
	 *            the smallest
	 * @param largest
	 *            the largest
	 * 
	 * @return the list files filter
	 */
    public ListFilesFilter setAcceptSizeFilter(long smallest, long largest) {
        Smallest = smallest;
        Largest = largest;
        return this;
    }

    /** The Name regexp. */
    protected String NameRegexp;

    /** The Oldest. */
    protected Date Oldest;

    /** The Newest. */
    protected Date Newest;

    /** The Largest. */
    protected long Smallest = -1, Largest = -1;

    /** The backslash. */
    static char backslash = '\\';
}
