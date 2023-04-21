package com.shpimp.dmoz.util;

import java.io.*;
import java.util.*;

/**
 * Copyright &copy Jeffrey Paul Prickett. All rights reserved.
 */
public class FilterProperties {

    public static final String STRUCTURE_INPUT_FILE = "structure.input.file";

    public static final String STRUCTURE_OUTPUT_FILE = "structure.output.file";

    public static final String CONTENT_INPUT_FILE = "content.input.file";

    public static final String CONTENT_OUTPUT_FILE = "content.output.file";

    public static final String RDF_FILTER_FILE = "rdf.filter";

    public static final String PROCESS_CONTENT_DIRECTIVE = "process.content";

    public static final String PROCESS_STRUCTURE_DIRECTIVE = "process.structure";

    private static final String TRUE_VALUE = "true";

    private static Properties props = null;

    /**
     * The purpose of this method is to set and load the properties that
     * will be used to parse the dmoz xml files.
     * @param newval The location of the property file as a string.
     */
    public static void setProperties(String propertyFile) throws Exception {
        if (propertyFile != null) {
            File pFile = new File(propertyFile);
            if (pFile.exists() && pFile.canRead()) {
                FileInputStream fin = new FileInputStream(pFile);
                props = new Properties();
                props.load((InputStream) fin);
            } else if (!pFile.exists()) {
                throw new IOException("property file named \"" + propertyFile + "\" does not exist.");
            } else if (!pFile.canRead()) {
                throw new IOException("property file named \"" + propertyFile + "\" is not readable.");
            } else {
                throw new IOException("Unexpected exception -" + " should not occur");
            }
        } else {
            throw new Exception("propertyFile == null");
        }
    }

    public static String getStructureInputFile() {
        if (props != null) {
            return props.getProperty(STRUCTURE_INPUT_FILE);
        } else {
            return null;
        }
    }

    public static String getStructureOutputFile() {
        if (props != null) {
            return props.getProperty(STRUCTURE_OUTPUT_FILE);
        } else {
            return null;
        }
    }

    public static String getContentInputFile() {
        if (props != null) {
            return props.getProperty(CONTENT_INPUT_FILE);
        } else {
            return null;
        }
    }

    public static String getContentOutputFile() {
        if (props != null) {
            return props.getProperty(CONTENT_OUTPUT_FILE);
        } else {
            return null;
        }
    }

    public static String getRDFFilterFile() {
        if (props != null) {
            return props.getProperty(RDF_FILTER_FILE);
        } else {
            return null;
        }
    }

    public static boolean processContent() {
        if (props != null) {
            String rawValue = props.getProperty(PROCESS_CONTENT_DIRECTIVE);
            if (rawValue != null) {
                rawValue = rawValue.trim();
                rawValue = rawValue.toLowerCase();
                return rawValue.equals(TRUE_VALUE);
            } else {
                return false;
            }
        } else {
            return false;
        }
    }

    public static boolean processStructure() {
        if (props != null) {
            String rawValue = props.getProperty(PROCESS_STRUCTURE_DIRECTIVE);
            if (rawValue != null) {
                rawValue = rawValue.trim();
                rawValue = rawValue.toLowerCase();
                return rawValue.equals(TRUE_VALUE);
            } else {
                return false;
            }
        } else {
            return false;
        }
    }
}
