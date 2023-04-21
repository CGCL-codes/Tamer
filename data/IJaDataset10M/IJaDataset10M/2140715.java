package org.drftpd.tools.ant;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.Project;
import org.apache.tools.ant.Task;
import org.apache.tools.ant.types.FileSet;

/**
 * @author djb61
 * @version $Id: ResourceTask.java 1887 2008-03-02 16:10:37Z djb61 $
 */
public class ResourceTask extends Task {

    public static final boolean isWin32 = System.getProperty("os.name").startsWith("Windows");

    private File _baseDir;

    private File _resourceDir;

    private long _longDate = 0L;

    private boolean _slavePlugin;

    private ArrayList<String> _filePatterns;

    /**
	 * @param aBaseDir base directory for project
	 */
    public final void setBaseDir(final File aBaseDir) {
        _baseDir = aBaseDir;
    }

    /**
	 * @param aResourceDir base directory for resources
	 */
    public final void setResourceDir(final File aResourceDir) {
        _resourceDir = aResourceDir;
    }

    /**
	 * @see org.apache.tools.ant.Task#execute()
	 */
    @Override
    public void execute() throws BuildException {
        _slavePlugin = getProject().getProperty("slave.plugin").equalsIgnoreCase("true");
        FileSet slaveFiles = (FileSet) getProject().getReference("slave.fileset");
        _filePatterns = new ArrayList<String>();
        SimpleDateFormat simpleBuildDate = new SimpleDateFormat("yyyy.MM.dd HH:mm:ss.SSS");
        Date buildDate = null;
        try {
            buildDate = simpleBuildDate.parse(getProject().getProperty("build.plugins.start"));
        } catch (ParseException e) {
            throw new BuildException("Plugin build timestamp not set correctly");
        }
        _longDate = buildDate.getTime();
        findResources(_resourceDir);
        if (_slavePlugin) {
            String[] patterns = (String[]) _filePatterns.toArray(new String[_filePatterns.size()]);
            slaveFiles.appendIncludes(patterns);
        }
    }

    /**
	 * Recursively scans a directory for resource files
	 * and adds their entries to the installed resource
	 * files.
	 * 
	 * @param dir directory to search for resources
	 * @throws BuildException
	 */
    private void findResources(File dir) throws BuildException {
        if (!dir.isDirectory()) throw new BuildException(dir.getPath() + " is not a directory");
        for (File file : dir.listFiles()) {
            if (file.getName().startsWith(".")) {
                continue;
            } else if (file.isFile()) {
                copyResource(file);
            } else if (file.isDirectory()) {
                findResources(file);
            }
        }
    }

    /**
	 * Copies a resource file into the installed directory
	 * hierarchy, if the file already exists and was created
	 * during this build session the contents are appended
	 * to the existing file, if the file is from an earlier
	 * build session it will be deleted and replaced
	 * 
	 * @param file resource file to be copied
	 */
    private void copyResource(File file) {
        String relativePath = file.getPath().substring(_resourceDir.getPath().length() + 1);
        File newFile = new File(_baseDir, relativePath);
        if (!newFile.getParentFile().exists()) {
            newFile.getParentFile().mkdirs();
        }
        if (newFile.lastModified() == 0L || newFile.lastModified() < _longDate) {
            newFile.delete();
        }
        FileInputStream fis = null;
        BufferedReader input = null;
        StringBuilder output = new StringBuilder();
        try {
            fis = new FileInputStream(file);
            input = new BufferedReader(new InputStreamReader(fis));
            while (input.ready()) {
                output.append(input.readLine());
                output.append("\n");
            }
        } catch (FileNotFoundException e) {
            log("Resource file appears to have been deleted, skipping: " + file.getName(), Project.MSG_ERR);
        } catch (IOException e) {
            log("Failed to load resources from: " + file.getName(), Project.MSG_ERR);
        } finally {
            try {
                input.close();
            } catch (IOException e) {
            }
            try {
                fis.close();
            } catch (IOException e) {
            }
        }
        FileWriter outputWriter = null;
        try {
            outputWriter = new FileWriter(newFile, true);
            outputWriter.write(output.toString() + "\n");
            outputWriter.flush();
            if (_slavePlugin) {
                _filePatterns.add(relativePath);
            }
        } catch (FileNotFoundException e) {
            log("Cannot write resource file to: " + newFile.getParent(), Project.MSG_ERR);
        } catch (IOException e) {
            log("Error writing resource file: " + newFile.getName(), Project.MSG_ERR);
        } finally {
            try {
                outputWriter.close();
            } catch (IOException e) {
            }
        }
        if (!isWin32) {
            if (newFile.getName().endsWith(".sh")) {
                String[] cmdArray = { "chmod", "755", newFile.getAbsolutePath() };
                try {
                    Process p = Runtime.getRuntime().exec(cmdArray);
                    p.waitFor();
                    if (p.exitValue() != 0) {
                        log("Error chmodding file: " + newFile.getAbsolutePath(), Project.MSG_ERR);
                    }
                } catch (IOException e) {
                    log("Error chmodding file: " + newFile.getAbsolutePath(), Project.MSG_ERR);
                } catch (InterruptedException e) {
                    log("Chmod process was interrupted on file: " + newFile.getAbsolutePath(), Project.MSG_ERR);
                }
            }
        }
    }
}
