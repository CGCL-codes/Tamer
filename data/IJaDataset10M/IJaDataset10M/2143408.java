package com.armatiek.infofuze.stream.filesystem.filefilter;

import java.io.Serializable;
import java.util.List;
import org.apache.commons.io.IOCase;
import com.armatiek.infofuze.stream.filesystem.FileIf;

/**
 * Filters files based on the suffix (what the filename ends with).
 * This is used in retrieving all the files of a particular type.
 * 
 * Based on code from Apache Commons IO version 2.01.
 * The main difference is that this class filters objects that 
 * implement {@link com.armatiek.infofuze.stream.filesystem.FileIf}, 
 * not {@link java.io.File}.
 * 
 * Original author Stephen Colebourne, Federico Barbieri,
 * Serge Knystautas, Peter Donald
 */
public class SuffixFileFilter implements IOFileFilter, Serializable {

    private static final long serialVersionUID = 1L;

    /** The filename suffixes to search for */
    private final String[] suffixes;

    /** Whether the comparison is case sensitive. */
    private final IOCase caseSensitivity;

    /**
     * Constructs a new Suffix file filter for a single extension.
     * 
     * @param suffix  the suffix to allow, must not be null
     * @throws IllegalArgumentException if the suffix is null
     */
    public SuffixFileFilter(String suffix) {
        this(suffix, IOCase.SENSITIVE);
    }

    /**
     * Constructs a new Suffix file filter for a single extension
     * specifying case-sensitivity.
     *
     * @param suffix  the suffix to allow, must not be null
     * @param caseSensitivity  how to handle case sensitivity, null means case-sensitive
     * @throws IllegalArgumentException if the suffix is null
     */
    public SuffixFileFilter(String suffix, IOCase caseSensitivity) {
        if (suffix == null) {
            throw new IllegalArgumentException("The suffix must not be null");
        }
        this.suffixes = new String[] { suffix };
        this.caseSensitivity = (caseSensitivity == null ? IOCase.SENSITIVE : caseSensitivity);
    }

    /**
     * Constructs a new Suffix file filter for an array of suffixs.
     * <p>
     * The array is not cloned, so could be changed after constructing the
     * instance. This would be inadvisable however.
     * 
     * @param suffixes  the suffixes to allow, must not be null
     * @throws IllegalArgumentException if the suffix array is null
     */
    public SuffixFileFilter(String[] suffixes) {
        this(suffixes, IOCase.SENSITIVE);
    }

    /**
     * Constructs a new Suffix file filter for an array of suffixs
     * specifying case-sensitivity.
     * <p>
     * The array is not cloned, so could be changed after constructing the
     * instance. This would be inadvisable however.
     * 
     * @param suffixes  the suffixes to allow, must not be null
     * @param caseSensitivity  how to handle case sensitivity, null means case-sensitive
     * @throws IllegalArgumentException if the suffix array is null
     */
    public SuffixFileFilter(String[] suffixes, IOCase caseSensitivity) {
        if (suffixes == null) {
            throw new IllegalArgumentException("The array of suffixes must not be null");
        }
        this.suffixes = new String[suffixes.length];
        System.arraycopy(suffixes, 0, this.suffixes, 0, suffixes.length);
        this.caseSensitivity = (caseSensitivity == null ? IOCase.SENSITIVE : caseSensitivity);
    }

    /**
     * Constructs a new Suffix file filter for a list of suffixes.
     * 
     * @param suffixes  the suffixes to allow, must not be null
     * @throws IllegalArgumentException if the suffix list is null
     * @throws ClassCastException if the list does not contain Strings
     */
    public SuffixFileFilter(List<String> suffixes) {
        this(suffixes, IOCase.SENSITIVE);
    }

    /**
     * Constructs a new Suffix file filter for a list of suffixes
     * specifying case-sensitivity.
     * 
     * @param suffixes  the suffixes to allow, must not be null
     * @param caseSensitivity  how to handle case sensitivity, null means case-sensitive
     * @throws IllegalArgumentException if the suffix list is null
     * @throws ClassCastException if the list does not contain Strings
     */
    public SuffixFileFilter(List<String> suffixes, IOCase caseSensitivity) {
        if (suffixes == null) {
            throw new IllegalArgumentException("The list of suffixes must not be null");
        }
        this.suffixes = suffixes.toArray(new String[suffixes.size()]);
        this.caseSensitivity = (caseSensitivity == null ? IOCase.SENSITIVE : caseSensitivity);
    }

    /**
     * Checks to see if the filename ends with the suffix.
     * 
     * @param file  the File to check
     * @return true if the filename ends with one of our suffixes
     */
    @Override
    public boolean accept(FileIf file) {
        String name = file.getName();
        for (String suffix : this.suffixes) {
            if (caseSensitivity.checkEndsWith(name, suffix)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Provide a String representaion of this file filter.
     *
     * @return a String representaion
     */
    @Override
    public String toString() {
        StringBuilder buffer = new StringBuilder();
        buffer.append(super.toString());
        buffer.append("(");
        if (suffixes != null) {
            for (int i = 0; i < suffixes.length; i++) {
                if (i > 0) {
                    buffer.append(",");
                }
                buffer.append(suffixes[i]);
            }
        }
        buffer.append(")");
        return buffer.toString();
    }
}
