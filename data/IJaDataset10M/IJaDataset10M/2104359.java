package com.dyuproject.protostuff.codegen;

import java.io.File;
import java.io.Serializable;
import java.util.Properties;

/**
 * Configuration for the protoc-compiled proto w/c ontains the compile options and arguments.
 * 
 * @author David Yu
 * @created Oct 15, 2009
 */
public class Module implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = 8503087348372596796L;

    private String fullClassname;

    private String outputPackage;

    private String generator;

    private String encoding;

    private File outputDir;

    private Properties options = new Properties();

    public Module() {
    }

    public Module(String fullClassname, String outputPackage, String generator, String encoding, File outputDir) {
        this.fullClassname = fullClassname;
        this.outputPackage = outputPackage;
        this.generator = generator;
        this.encoding = encoding;
        this.outputDir = outputDir;
    }

    public Module(String fullClassname, String outputPackage, String generator, String encoding, File outputDir, Properties options) {
        this(fullClassname, outputPackage, generator, encoding, outputDir);
        this.options = options;
    }

    /**
     * @return the fullClassname
     */
    public String getFullClassname() {
        return fullClassname;
    }

    /**
     * @param fullClassname the fullClassname to set
     */
    public void setFullClassname(String fullClassname) {
        this.fullClassname = fullClassname;
    }

    /**
     * @return the outputPackage
     */
    public String getOutputPackage() {
        return outputPackage;
    }

    /**
     * @param outputPackage the outputPackage to set
     */
    public void setOutputPackage(String outputPackage) {
        this.outputPackage = outputPackage;
    }

    /**
     * @return the generator
     */
    public String getGenerator() {
        return generator;
    }

    /**
     * @param generator the generator to set
     */
    public void setGenerator(String generator) {
        this.generator = generator;
    }

    /**
     * @return the encoding
     */
    public String getEncoding() {
        return encoding;
    }

    /**
     * @param encoding the encoding to set
     */
    public void setEncoding(String encoding) {
        this.encoding = encoding;
    }

    /**
     * @return the outputDir
     */
    public File getOutputDir() {
        return outputDir;
    }

    /**
     * @param outputDir the outputDir to set
     */
    public void setOutputDir(File outputDir) {
        this.outputDir = outputDir;
    }

    /**
     * @return the options
     */
    public Properties getOptions() {
        return options;
    }

    /**
     * @param options
     */
    public void setOptions(Properties options) {
        this.options.putAll(options);
    }

    public String getOption(String key) {
        return options.getProperty(key);
    }

    public void setOption(String key, String value) {
        options.setProperty(key, value);
    }
}
