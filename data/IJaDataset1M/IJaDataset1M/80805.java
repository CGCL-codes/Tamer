package com.boxysystems.libraryfinder.model;

/**
 * Class representing value/query object used as input to the library finder
 *
 * @author Siddique Hameed
 * @since Feb 3, 2007
 */
public class LibraryFinderQuery {

    private String searchPath;

    private String fileNamePattern;

    private boolean regex;

    private boolean caseSensitive;

    private boolean verboseMode;

    private String excludedFolders;

    public LibraryFinderQuery(String searchPath, String fileNamePattern) {
        this.searchPath = searchPath.trim();
        this.fileNamePattern = fileNamePattern.trim();
    }

    public String getSearchPath() {
        return searchPath;
    }

    public String getFileNamePattern() {
        return fileNamePattern;
    }

    public void setFileNamePattern(String fileNamePattern) {
        this.fileNamePattern = fileNamePattern;
    }

    public boolean isRegex() {
        return regex;
    }

    public void setRegex(boolean regex) {
        this.regex = regex;
    }

    public boolean isCaseSensitive() {
        return caseSensitive;
    }

    public void setCaseSensitive(boolean caseSensitive) {
        this.caseSensitive = caseSensitive;
    }

    public boolean isVerboseMode() {
        return verboseMode;
    }

    public void setVerboseMode(boolean verboseMode) {
        this.verboseMode = verboseMode;
    }

    public String getExcludedFolders() {
        return excludedFolders;
    }

    public void setExcludedFolders(String excludedFolders) {
        this.excludedFolders = excludedFolders;
    }
}
