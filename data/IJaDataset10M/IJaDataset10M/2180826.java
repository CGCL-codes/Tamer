package com.neighborsrc.ant;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.Task;
import org.apache.tools.ant.taskdefs.Get;

public class XbMozGetTask extends GetTaskParent {

    public static final String CALENDAR_FILE_PREFIX = "calendar";

    public static final String LIGHTNING_FILE_PREFIX = "lightning";

    public static final String SUNBIRD_FILE_PREFIX = "sunbird";

    private String targetOs = "linux-i686";

    private String locality = "en-US";

    private File srcExpand = null;

    /**
     * The purpose of this method is to return the task name.
     */
    public String getTaskName() {
        return "xbmozget";
    }

    /**
     * This is the method that returns the file name for the get task.
     */
    protected String getFilename() {
        StringBuffer filename = new StringBuffer();
        filename.append(filePrefix);
        filename.append("-");
        filename.append(version);
        filename.append(".");
        if (filePrefix.equals(LIGHTNING_FILE_PREFIX)) {
            filename.append(targetOs);
            filename.append(".");
        }
        filename.append(fileSuffix);
        return filename.toString();
    }

    /**
     * This returns the base url for the software package and version.
     */
    private String getSrcBaseUrl() {
        StringBuffer srcUrl = new StringBuffer();
        srcUrl.append(mirror);
        srcUrl.append("/");
        if (filePrefix.equals(LIGHTNING_FILE_PREFIX)) {
            srcUrl.append(CALENDAR_FILE_PREFIX);
            srcUrl.append("/");
            srcUrl.append(LIGHTNING_FILE_PREFIX);
        } else {
            srcUrl.append(filePrefix);
        }
        srcUrl.append("/releases/");
        srcUrl.append(version);
        srcUrl.append("/");
        return srcUrl.toString();
    }

    /**
     * This returns the directory name of the directory into which the 
     * files will reside when expanded (not the same as expandDir), could
     * be and most likely will be a subdirectory of expand dir.
     */
    protected String getExpandedDirName() {
        return filePrefix;
    }

    /**
     * This is the method that returns the source url from which we
     * want to download.
     */
    protected URL getSrc() throws MalformedURLException {
        StringBuffer srcUrl = new StringBuffer();
        srcUrl.append(getSrcBaseUrl());
        if (!filePrefix.equals(LIGHTNING_FILE_PREFIX)) {
            srcUrl.append(targetOs);
            srcUrl.append("/");
            srcUrl.append(locality);
            srcUrl.append("/");
        }
        srcUrl.append(getFilename());
        return new URL(srcUrl.toString());
    }

    /**
     * This method is used to set the flag that tells us to also download
     * the source code from the mozilla mirror site.
     */
    public void setSrcexpand(File newSrc) {
        srcExpand = newSrc;
    }

    /**
     * This method returns the source code tarball filename.
     */
    private String getSourceCodeFilename() {
        StringBuffer srcUrl = new StringBuffer();
        if (filePrefix.equals(LIGHTNING_FILE_PREFIX)) {
            srcUrl.append(LIGHTNING_FILE_PREFIX);
            srcUrl.append("-");
            srcUrl.append(SUNBIRD_FILE_PREFIX);
        } else {
            srcUrl.append(filePrefix);
        }
        srcUrl.append("-");
        srcUrl.append(version);
        srcUrl.append("-source.tar.bz2");
        return srcUrl.toString();
    }

    /**
     * This method returns the url where we can download the mozilla source
     * code.
     */
    private URL getSourceCodeSrc() throws MalformedURLException {
        StringBuffer srcUrl = new StringBuffer();
        srcUrl.append(getSrcBaseUrl());
        srcUrl.append("source/");
        srcUrl.append(getSourceCodeFilename());
        return new URL(srcUrl.toString());
    }

    /**
     * This method returns the file that we want to store the source 
     * code in.
     */
    private File getSourceCodeDest() {
        StringBuffer destFile = new StringBuffer();
        destFile.append(downloadDir);
        destFile.append("/");
        destFile.append(getSourceCodeFilename());
        return new File(destFile.toString());
    }

    /**
     * This is the method that actually executes the task.
     */
    public void execute() throws BuildException {
        if (filePrefix.equals(LIGHTNING_FILE_PREFIX)) {
            expandDir = new File(expandDir, LIGHTNING_FILE_PREFIX);
        }
        super.execute();
        try {
            if (srcExpand != null) {
                Get antGet = new Get();
                antGet.setTaskName(getTaskName());
                antGet.setProject(getProject());
                antGet.setSrc(getSourceCodeSrc());
                antGet.setDest(getSourceCodeDest());
                antGet.setUseTimestamp(true);
                antGet.execute();
                expandDownloadIfNeeded(new File(srcExpand, getExpandedDirName()), getSourceCodeDest(), new File(srcExpand, filePrefix), getSourceCodeFilename());
            }
        } catch (Exception exc) {
            throw new BuildException(exc);
        }
    }
}
