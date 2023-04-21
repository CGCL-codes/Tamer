package com.controltier.ctl.types;

import org.apache.tools.ant.util.FileNameMapper;

/**
 * @ant.type = capitalizemapper
 */
public class CapitalizeMapper implements FileNameMapper {

    /**
     * Ignored.
     * @param from ignored.
     */
    public void setFrom(String from) {
    }

    /**
     * Ignored.
     * @param to ignored.
     */
    public void setTo(String to) {
    }

    /**
     * Returns an one-element array containing the source file name in upper case.
     * @param sourceFileName the name to map.
     * @return the source filename in a one-element array.
     */
    public String[] mapFileName(String sourceFileName) {
        if (sourceFileName.length() == 0) return new String[] { sourceFileName };
        return new String[] { sourceFileName.toUpperCase() };
    }
}
