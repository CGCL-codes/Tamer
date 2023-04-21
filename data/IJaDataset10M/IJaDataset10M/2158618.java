package com.memoire.ant;

import java.io.File;
import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.DirectoryScanner;
import org.apache.tools.ant.Task;

/**
 *
 * @version      $Id: cpp_task.java,v 1.2 2005-08-16 12:58:09 deniger Exp $
 * @author       Fred Deniger
 */
public class cpp_task extends Task {

    protected File destDir_ = null;

    protected File baseDir_ = null;

    protected String includesFiles_ = "*.idl";

    protected String excludesFiles_ = "";

    protected String ext_ = "";

    public cpp_task() {
    }

    public void execute() {
        if (destDir_ == null) throw new BuildException("destDir is no set");
        if (!destDir_.exists()) throw new BuildException("destDir doesn't exists");
        if (!destDir_.isDirectory()) throw new BuildException("destDir is not a directory");
        DirectoryScanner ds = new DirectoryScanner();
        ds.setBasedir(baseDir_);
        ds.setIncludes(new String[] { includesFiles_ });
        ds.setExcludes(new String[] { excludesFiles_ });
        ds.scan();
        String[] files = ds.getIncludedFiles();
        String os = System.getProperty("os.name");
        if (os.startsWith("Win")) {
            System.err.println("To be done...");
        } else {
            for (int i = 0; i < files.length; i++) {
                String fileName = files[i];
                if (!"".equals(ext_)) {
                    if (fileName.lastIndexOf(".") > -1) fileName = fileName.substring(0, fileName.lastIndexOf(".")) + ext_;
                }
                File destFile = new File(destDir_, fileName);
                File fromFile = new File(baseDir_.getAbsolutePath() + File.separator + files[i]);
                if (destFile.lastModified() >= fromFile.lastModified()) continue;
                destFile.getParentFile().mkdirs();
                String[] param = new String[4];
                param[0] = "cpp";
                param[1] = "-I" + baseDir_ + File.separator;
                param[2] = fromFile.getAbsolutePath();
                param[3] = "-o " + destFile.getAbsolutePath();
                log(fromFile + " > " + destFile);
                try {
                    Runtime.getRuntime().exec(param[0] + " " + param[1] + " " + param[2] + " " + param[3]);
                } catch (java.io.IOException e) {
                    System.err.println("Error while executing " + param[0] + " " + param[1] + " " + param[2] + " " + param[3]);
                    System.err.println(e.getMessage());
                }
            }
        }
    }

    public void setBaseDir(File _file) {
        if (_file != null) baseDir_ = _file;
    }

    public void setDestDir(File _file) {
        if (_file != null) destDir_ = _file;
    }

    public void setIncludesFiles(String _incFiles) {
        this.includesFiles_ = _incFiles;
    }

    public void setExt(String _ext) {
        if (_ext != null) ext_ = _ext.trim();
    }

    public void setExcludesFiles(String _excFiles) {
        this.excludesFiles_ = _excFiles;
    }
}
