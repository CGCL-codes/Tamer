package com.sun.ebxml.registry.ant;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.DirectoryScanner;
import org.apache.tools.ant.Project;
import org.apache.tools.ant.taskdefs.MatchingTask;

/**
 * @author Fabian Ritzmann
 */
public class InstallEndorsed extends MatchingTask {

    public void execute() throws BuildException {
        String endorsedDirs = getSystemProperty("java.endorsed.dirs");
        String pathSeparator = getSystemProperty("path.separator");
        String endorsedDir = extractFirstEntry(endorsedDirs, pathSeparator);
        this.fileset.setDir(this.project.getBaseDir());
        DirectoryScanner scanner = this.fileset.getDirectoryScanner(this.project);
        scanner.scan();
        createDir(endorsedDir);
        String[] files = scanner.getIncludedFiles();
        for (int i = 0; i < files.length; i++) {
            if (!fileExists(files[i], endorsedDir)) {
                copyFile(files[i], endorsedDir);
            } else {
                log("Not installing file \"" + (new File(files[i])).getName() + "\" to directory \"" + endorsedDir + "\", file already exists", Project.MSG_VERBOSE);
            }
        }
    }

    /**
     * Method createDir.
     * @param endorsedDir
     */
    private void createDir(String endorsedDir) throws BuildException {
        try {
            File dir = new File(endorsedDir);
            dir.mkdirs();
        } catch (Exception e) {
            throw new BuildException("Failed to create directory \"" + endorsedDir + "\"");
        }
    }

    private File getDestFile(String sourceFilename, String destDirname) throws BuildException {
        try {
            File sourceFile = new File(sourceFilename);
            String sourceBasename = sourceFile.getName();
            return new File(destDirname, sourceBasename);
        } catch (Exception e) {
            throw new BuildException("Could not create destination filename from source filename \"" + sourceFilename + "\" and destination directory \"" + destDirname + "\"", e);
        }
    }

    /**
     * Method fileExists.
     * @param string
     * @param endorsedDir
     * @return boolean
     */
    private boolean fileExists(String sourceFilename, String destDirname) throws BuildException {
        try {
            File destFile = getDestFile(sourceFilename, destDirname);
            return destFile.exists();
        } catch (SecurityException e) {
            throw new BuildException("Could not determine if file \"" + sourceFilename + "\" exists in directory \"" + destDirname + "\"", e);
        }
    }

    /**
     * Method copyFile.
     * @param string
     * @param endorsedDir
     */
    private void copyFile(String sourceFilename, String destDirname) throws BuildException {
        log("Copying file " + sourceFilename + " to " + destDirname);
        File destFile = getDestFile(sourceFilename, destDirname);
        InputStream inStream = null;
        OutputStream outStream = null;
        try {
            inStream = new BufferedInputStream(new FileInputStream(sourceFilename));
            outStream = new BufferedOutputStream(new FileOutputStream(destFile));
            byte[] buffer = new byte[1024];
            int n = 0;
            while ((n = inStream.read(buffer)) != -1) outStream.write(buffer, 0, n);
        } catch (Exception e) {
            throw new BuildException("Failed to copy file \"" + sourceFilename + "\" to directory \"" + destDirname + "\"");
        } finally {
            try {
                if (inStream != null) inStream.close();
            } catch (IOException e) {
            }
            try {
                if (outStream != null) outStream.close();
            } catch (IOException e) {
            }
        }
    }

    private String getSystemProperty(String key) throws BuildException {
        try {
            String value = System.getProperty(key);
            if ((value == null) || (value.length() == 0)) throw new BuildException("System property \"" + key + "\" could not be found.");
            return value;
        } catch (SecurityException e) {
            throw new BuildException("Could not access system properties.", e);
        }
    }

    private String extractFirstEntry(String entries, String separator) throws BuildException {
        try {
            int endFirstDir = entries.indexOf(separator);
            if (endFirstDir == -1) endFirstDir = entries.length();
            return entries.substring(0, endFirstDir);
        } catch (IndexOutOfBoundsException e) {
            throw new BuildException("Implementation error. Substring boundaries were not calculated correctly.", e);
        }
    }
}
