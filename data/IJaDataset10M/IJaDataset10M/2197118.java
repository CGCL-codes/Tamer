package org.crap4j;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import org.kohsuke.args4j.Option;

public class Options {

    public static final String defaultServer = "http://www.crap4j.org/benchmark/";

    private String projectDir;

    private String libClasspaths;

    private String testClassDirs;

    private String classDirs;

    private String sourceDirs;

    /** Config files are an alternative to specifying all the other parameters * */
    private String configFile;

    private String outputDir;

    private boolean debug;

    private boolean dontTest;

    private boolean downloadAverages = true;

    private String server;

    public List<String> getClassDirs() {
        return returnAsList(classDirs);
    }

    @Option(name = "-c", usage = "Project class directories separated by path char")
    public void setClassDirs(String classDirs) {
        this.classDirs = classDirs;
    }

    public List<String> getLibClasspaths() {
        return returnAsList(libClasspaths);
    }

    @Option(name = "-l", usage = "Project classpath separated by path char")
    public void setLibClasspaths(String libClasspaths) {
        this.libClasspaths = libClasspaths;
    }

    public String getProjectDir() {
        return projectDir;
    }

    @Option(name = "-p", usage = "Project root directory")
    public void setProjectDir(String projectDir) {
        this.projectDir = projectDir;
    }

    public List<String> getSourceDirs() {
        return returnAsList(sourceDirs);
    }

    @Option(name = "-s", usage = "Project source directories separated by path char")
    public void setSourceDirs(String sourceDirs) {
        this.sourceDirs = sourceDirs;
    }

    public List<String> getTestClassDirs() {
        return returnAsList(testClassDirs);
    }

    @Option(name = "-t", usage = "Project test directories separated by path char")
    public void setTestClassDirs(String testClassDirs) {
        this.testClassDirs = testClassDirs;
    }

    public String getConfigFile() {
        return configFile;
    }

    @Option(name = "-f", usage = "Config File with project directories. Copy the projectConfig.sample in docs for your project.")
    public void setConfigFile(String configFile) throws FileNotFoundException {
        this.configFile = configFile;
        if (configFile != null) {
            readConfigFile();
        }
    }

    public String getOutputDir() {
        return outputDir;
    }

    @Option(name = "-o", usage = "The directory where reports can be dumped. By default it is <projectDir>/agitar/report/crap4j.")
    public void setOutputDir(String outputDir) {
        this.outputDir = outputDir;
    }

    public String getServer() {
        if (this.server == null || this.server.length() == 0) this.server = defaultServer;
        return server;
    }

    @Option(name = "-h", usage = "The server and path to retrieve benchmarking data. By default it is http://www.crap4j.org/benchmark/.")
    public void setServer(String server) {
        this.server = server;
    }

    public boolean getDebug() {
        return debug;
    }

    @Option(name = "-debug", usage = "Turn on debugging output to console (false).")
    public void setDebug(boolean debug) {
        this.debug = debug;
    }

    public boolean getDontTest() {
        return dontTest;
    }

    @Option(name = "-dontTest", usage = "Turn off test running phase (false).")
    public void setDontTest(boolean dontTest) {
        this.dontTest = dontTest;
    }

    public boolean getDownloadAverages() {
        return downloadAverages;
    }

    @Option(name = "-downloadAverages", usage = "Download average crap benchmark data (true).")
    public void setDownloadAverages(boolean downloadAverages) {
        this.downloadAverages = downloadAverages;
    }

    private List<String> returnAsList(String prop) {
        if (prop == null) return Collections.emptyList();
        return Arrays.asList(prop.split(File.pathSeparator));
    }

    public boolean valid() {
        return projectDir != null && classDirs.length() > 0;
    }

    @Override
    public String toString() {
        StringBuilder b = new StringBuilder();
        if (configFile != null) {
            b.append("-f ").append(configFile);
        }
        b.append("-p ").append(getProjectDir()).append(" -s ").append(join(getSourceDirs())).append(" -c ").append(join(getClassDirs())).append(" -t ").append(join(getTestClassDirs())).append(" -l ").append(join(getLibClasspaths())).append(" -o ").append(getOutputDir()).append(" -debug ").append(getDebug()).append(" -dontTest ").append(getDontTest());
        return b.toString();
    }

    public String join(List<String> l) {
        StringBuffer b = new StringBuffer();
        boolean first = true;
        for (String s : l) {
            if (!first) b.append(":"); else first = false;
            b.append(s);
        }
        return b.toString();
    }

    protected void readConfigFile() throws FileNotFoundException {
        parseLines(getLinesFromConfigFile());
    }

    private void parseLines(List<String> lines) {
        int lineNumber = 1;
        for (String line : lines) {
            if (isComment(line) || isBlank(line)) continue;
            checkAssignment(lineNumber, line);
            String[] propertyAndValue = parseLine(line);
            setProperty(propertyAndValue[0], propertyAndValue[1]);
            lineNumber++;
        }
    }

    private void checkAssignment(int lineNumber, String line) {
        if (!isAssignment(line)) throw new IllegalArgumentException("Config file is not well-formed. Bad line #(" + lineNumber + "): " + line + ".");
    }

    private String[] parseLine(String line) {
        String property = null;
        String value = null;
        String[] namesAndValues = line.split("=");
        if (namesAndValues.length == 2) {
            property = namesAndValues[0];
            value = namesAndValues[1];
        }
        return new String[] { property, value };
    }

    private boolean isBlank(String string) {
        return string.length() == 0;
    }

    protected void setProperty(String property, String value) {
        if (isEmpty(property, value)) return;
        if (property.equals("projectDir")) setProjectDir(value); else if (property.equals("libClasspaths")) setLibClasspaths(value); else if (property.equals("testClassDirs")) setTestClassDirs(value); else if (property.equals("classDirs")) setClassDirs(value); else if (property.equals("sourceDirs")) setSourceDirs(value); else throw new IllegalArgumentException("Unknown property in ConfigFile: " + property + ", with value:" + value);
    }

    private boolean isEmpty(String property, String value) {
        return property == null || value == null;
    }

    private boolean isAssignment(String string) {
        return string.indexOf('=') != -1;
    }

    private boolean isComment(String string) {
        return string.startsWith("#");
    }

    private List<String> getLinesFromConfigFile() throws FileNotFoundException {
        File f = new File(configFile);
        checkConfigFileExists(f);
        BufferedReader r = null;
        try {
            r = new BufferedReader(new FileReader(f));
            List<String> lines = new ArrayList<String>();
            String line;
            try {
                while ((line = r.readLine()) != null) {
                    lines.add(line);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            return lines;
        } finally {
            try {
                r.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void checkConfigFileExists(File f) throws FileNotFoundException {
        if (!f.exists()) throw new FileNotFoundException("Cannot find configFile for project: " + configFile);
    }
}
