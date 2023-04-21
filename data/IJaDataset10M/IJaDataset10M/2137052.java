package net.sourceforge.projects.erlangantlib;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import org.apache.tools.ant.BuildException;

/**
 * Object of that class performs real work while
 * executing erlc tasks e.g. compilation of Erlang
 * source files during the options; logging is performed
 * by one of the fields.
 * 
 * @author Piotr Paradzinski
 */
public class ErlangCompilator {

    /** Logger sending messages to appropriate output */
    private TaskLogger logger;

    /** Options specifying compilation details  */
    private ErlangCompilerOptions options;

    /** Separator used by system in file path */
    private static final String IN_PATH_SEPARATOR = System.getProperty("file.separator");

    /**
	 * Sets the field options storing information
	 * besed on compilation of erlang source files
	 * is processed
	 * @param options
	 */
    public void setOptions(ErlangCompilerOptions options) {
        this.options = options;
    }

    /**
	 * Sets the field logger with object
	 * performing logging to the Apache Ant
	 * standard output
	 * @param logger object performing logging
	 */
    public void setLogger(TaskLogger logger) {
        this.logger = logger;
    }

    /**
	 * Extract compilation options and perform compilation.
	 */
    public void execute() {
        assert logger != null;
        assert options != null;
        logger.verbose("Base directory " + options.getSrcFilesBaseDir());
        logger.verbose("Compilation output mode: " + (options.isFlat() ? "flat" : "hierarchical"));
        List<String> erlangSourseFiles = applySkipNonErlangSourseFiles();
        int fileCount = erlangSourseFiles.size();
        if (fileCount > 0) logger.standard("Compiling " + fileCount + " source file" + ((fileCount == 1) ? "" : "s") + " to " + options.getOutputDir());
        BufferedReader err = null;
        List<String> outputMsgs = null;
        String sourceFile = null;
        boolean success = true;
        for (String fileName : erlangSourseFiles) {
            try {
                outputMsgs = new ArrayList<String>();
                sourceFile = options.getSrcFilesBaseDir() + IN_PATH_SEPARATOR + fileName;
                String command = "erlc" + " -o " + extractOutputDir(fileName) + " " + sourceFile;
                logger.verbose("Compiling: " + sourceFile);
                Process process = Runtime.getRuntime().exec(command);
                String currentLine = null;
                err = new BufferedReader(new InputStreamReader(process.getInputStream()));
                while ((currentLine = err.readLine()) != null) {
                    outputMsgs.add(currentLine);
                }
                int processResult = process.waitFor();
                if (processResult != 0) {
                    success = false;
                }
            } catch (Exception e) {
                throw new BuildException(e);
            } finally {
                if (outputMsgs != null) {
                    for (String msg : outputMsgs) {
                        if (msg.contains("Unknown extension")) {
                            logger.standard(sourceFile);
                        }
                        logger.standard(msg);
                    }
                }
                if (err != null) {
                    try {
                        err.close();
                        err = null;
                    } catch (IOException e) {
                        throw new BuildException(e);
                    }
                }
            }
        }
        if (success == false) {
            throw new BuildException("Compilation failed. See the compiler error output for details.");
        }
    }

    /**
	 * Util method - repack the filenames form array to
	 * list removing non files that are not erlang source files
	 * Filtering is swithced off in option isSipNonErlangSourceFiles
	 *  
	 * @return list containing names of soruce files from array
	 */
    private List<String> applySkipNonErlangSourseFiles() {
        ArrayList<String> erlangSourseFiles = new ArrayList<String>();
        for (String s : options.getSourceFiles()) {
            if (options.isSkipNonErlangSourseFiles() && (!s.endsWith(".erl"))) {
                continue;
            }
            erlangSourseFiles.add(s);
        }
        return erlangSourseFiles;
    }

    /**
	 * Util method - gets the full path to output directory. 
	 * If flag flat is set to true it returns
	 * output directory specified by user, otherwise
	 * calculate appropriate directory according to
	 * source file name.
	 * 
	 * @param srcFile source file name
	 * @return output directory name
	 */
    private String extractOutputDir(String srcFile) {
        if ((!options.isFlat()) && srcFile.contains(IN_PATH_SEPARATOR)) {
            int indx = srcFile.lastIndexOf(IN_PATH_SEPARATOR);
            String outPutDirName = options.getOutputDir() + IN_PATH_SEPARATOR + srcFile.substring(0, indx);
            File outFile = new File(outPutDirName);
            if (!outFile.exists()) {
                logger.verbose("Creating directory " + outPutDirName);
                outFile.mkdirs();
            }
            return outPutDirName;
        }
        return options.getOutputDir();
    }
}
