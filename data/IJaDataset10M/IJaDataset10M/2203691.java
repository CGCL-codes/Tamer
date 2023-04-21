package com.boxysystems.libraryfinder.model;

import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

/**
 * Visitor class for visiting library files (.jar & .zip) in a specified folder
 * for a matching input pattern.
 *
 * @author Siddique Hameed
 * @version 1.0
 */
public class LibraryVisitor extends Observable {

    private Pattern pattern;

    private Set<LibraryFinderResult> results = new TreeSet<LibraryFinderResult>();

    private boolean cancelled = false;

    private LibraryFinderQuery query;

    private LibraryFileFilter filter = new LibraryFileFilter();

    public LibraryVisitor(LibraryFinderQuery query) throws LibraryFinderException {
        this.query = query;
        compilePattern(query.getFileNamePattern(), query.isCaseSensitive(), query.isRegex());
        if (query.getExcludedFolders() != null) {
            filter = new LibraryFileFilterWithExcludedFolders(query.getExcludedFolders());
        }
    }

    public LibraryVisitor(LibraryFinderQuery query, Observer observer) throws LibraryFinderException {
        this(query);
        addObserver(observer);
    }

    private void compilePattern(String patternStr, boolean caseSensitive, boolean regex) throws LibraryFinderException {
        try {
            if (!regex) {
                patternStr = PatternUtil.generateFileNamePattern(patternStr);
            }
            if (!caseSensitive) {
                pattern = Pattern.compile(patternStr, Pattern.CASE_INSENSITIVE);
            } else {
                pattern = Pattern.compile(patternStr);
            }
        } catch (PatternSyntaxException e) {
            throw new LibraryFinderException(Constants.INVALID_PATTERN_MSG_PREFIX + e.getMessage());
        }
    }

    public void visit(ZipFile zip, ZipEntry entry) {
        Matcher matcher = pattern.matcher(entry.getName());
        if (matcher.matches()) {
            LibraryFinderResult result = new LibraryFinderResult(new File(zip.getName()).getAbsolutePath(), new File(zip.getName()).length());
            results.add(result);
        }
    }

    public Set<LibraryFinderResult> getResults() {
        return results;
    }

    private void visitLibrary(File libraryFile) {
        ZipFile zip = null;
        try {
            zip = new ZipFile(libraryFile, ZipFile.OPEN_READ);
            for (Enumeration entries = zip.entries(); entries.hasMoreElements(); ) {
                ZipEntry entry = (ZipEntry) entries.nextElement();
                visit(zip, entry);
            }
        } catch (Throwable e) {
            System.err.println(Constants.ERR_MSG_PREFIX + libraryFile.getAbsolutePath());
        } finally {
            if (zip != null) {
                try {
                    zip.close();
                } catch (IOException e) {
                }
            }
        }
    }

    public void visitLibraries() throws LibraryFinderException {
        File directory = new File(query.getSearchPath());
        validate(directory);
        visitLibraries(directory);
        sendMessageToObservers(Constants.ACTION_COMPLETED_MESSAGE);
    }

    private void visitLibraries(File directory) throws LibraryFinderException {
        File[] files = directory.listFiles(filter);
        if (files != null) {
            for (File file : files) {
                sendMessageToObservers(Constants.SEARCH_MSG_PREFIX + file.getAbsolutePath());
                if (cancelled) {
                    return;
                }
                if (file.isDirectory()) {
                    visitLibraries(file);
                } else {
                    visitLibrary(file);
                }
            }
        }
    }

    private void validate(File directory) throws LibraryFinderException {
        if (directory.exists() && !directory.isDirectory()) {
            throw new LibraryFinderException(Constants.PATH_NOT_VALID_MESSAGE);
        } else if (!directory.exists()) {
            throw new LibraryFinderException(Constants.PATH_NOT_FOUND_MESSAGE);
        }
    }

    private boolean hasObservers() {
        return countObservers() > 0;
    }

    private void sendMessageToObservers(Object message) {
        if (hasObservers() && message != null) {
            setChanged();
            notifyObservers(message);
        }
    }

    public void setCancelled(boolean cancelled) {
        this.cancelled = cancelled;
    }

    public Pattern getPattern() {
        return pattern;
    }
}
