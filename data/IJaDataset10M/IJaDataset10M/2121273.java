package com.boxysystems.libraryfinder.model;

/**
 * Class representing the result 
 * User: Siddique Hameed
 * Date: Apr 2, 2006
 * Time: 4:18:39 PM
 */
public class LibraryFinderResult implements Comparable {

    private String libraryPath;

    private long fileSize;

    public LibraryFinderResult(String libraryPath, long fileSize) {
        this.libraryPath = libraryPath;
        this.fileSize = fileSize;
    }

    public String getLibraryPath() {
        return libraryPath;
    }

    public long getFileSize() {
        return fileSize;
    }

    public String toString() {
        return getLibraryPath() + " (" + Math.round(getFileSize() / 1024) + " KB)";
    }

    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        final LibraryFinderResult that = (LibraryFinderResult) o;
        final long fileSize = getFileSize();
        final String libraryPath = getLibraryPath();
        return fileSize == that.getFileSize() && !(libraryPath != null ? !libraryPath.equals(that.getLibraryPath()) : that.getLibraryPath() != null);
    }

    public int compareTo(Object o) {
        if (o instanceof LibraryFinderResult) {
            LibraryFinderResult that = (LibraryFinderResult) o;
            return this.getLibraryPath().toLowerCase().compareTo(that.getLibraryPath().toLowerCase());
        }
        return 0;
    }
}
