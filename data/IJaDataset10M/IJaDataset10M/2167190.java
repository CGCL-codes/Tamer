package com.entelience.probe;

import java.io.InputStream;
import java.lang.reflect.Constructor;

/**
 * Expand - a class for un-compressing a given input
 * to create a suitable output for FileProbe#process
 *
 * This is used for compression formats which are not archive formats.
 */
public abstract class Expand {

    private static org.apache.log4j.Logger _logger = com.entelience.util.Logs.getProbeLogger();

    protected final String command;

    protected String className;

    /**
     * Construct an Expand object using the specified, external, command
     * to uncompress files.
     * @param command path to command
     */
    public Expand(String command) throws Exception {
        this.command = command;
    }

    public void setClassName(String value) {
        className = value;
    }

    /**
     * returns the expand command
     */
    public String getCommand() {
        return command;
    }

    /**
     * Must call this after routine in 
     * order to close all resources associated with this
     * operation.
     */
    protected abstract void close();

    /**
     * Now expand the file...
     * @param inFile file to use for the expansion.
     * @return opened InputStream containing uncompressed file contents.
     */
    protected abstract InputStream expand(LocalFileState inFile) throws Exception;

    /**
     * factory method for instanicating extract subclasses
     */
    protected static final Expand factory(String className, String command) throws Exception {
        Class clazz = Class.forName(className);
        Class clConstructor[] = { java.lang.String.class };
        Constructor cExpand = clazz.getConstructor(clConstructor);
        Object oParams[] = { command };
        Expand exp = (Expand) cExpand.newInstance(oParams);
        exp.setClassName(className);
        return exp;
    }

    /**
     * Clone the expand
     */
    @Override
    public Object clone() {
        try {
            return Expand.factory(className, command);
        } catch (Exception e) {
            _logger.error("Failed to clone the Expand (" + className + ", " + command + ")");
        }
        return null;
    }
}
