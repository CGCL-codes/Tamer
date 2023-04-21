package com.icesoft.faces.component.inputfile;

import java.io.Serializable;

/**
 * With state saving, the InputFile component exists only within a Lifecycle,
 * so any information that the UploadServer needs to process a file upload,
 * needs to be accessible without a direct reference to the InputFile.
 * 
 * Fields like sizeMax, uniqueFolder, uploadDirectory, uploadDirectoryAbsolute
 * take precendence over corresponding context-params in the web.xml. These 
 * fields being null means they're not set on the InputFile component, so the
 * UploadServer will use the context-params, or the context-param default values.
 * 
 * @author mcollette
 * @since 1.8
 */
public class UploadConfig implements Serializable {

    private String clientId;

    private String formClientId;

    private Long sizeMax;

    private String fileNamePattern;

    private Boolean uniqueFolder;

    private String uploadDirectory;

    private Boolean uploadDirectoryAbsolute;

    private boolean progressRender;

    private boolean progressListener;

    private boolean failOnEmptyFile;

    /**
     * InputFile uses this for publishing its own property configuration
     */
    public UploadConfig(String clientId, String formClientId, Long sizeMax, String fileNamePattern, Boolean uniqueFolder, String uploadDirectory, Boolean uploadDirectoryAbsolute, boolean progressRender, boolean progressListener, boolean failOnEmptyFile) {
        this.clientId = clientId;
        this.formClientId = formClientId;
        this.sizeMax = sizeMax;
        this.fileNamePattern = fileNamePattern;
        this.uniqueFolder = uniqueFolder;
        this.uploadDirectory = uploadDirectory;
        this.uploadDirectoryAbsolute = uploadDirectoryAbsolute;
        this.progressRender = progressRender;
        this.progressListener = progressListener;
        this.failOnEmptyFile = failOnEmptyFile;
    }

    /**
     * UploadServer uses this to resolve the combination of context-params
     * with the InputFile's properties.
     */
    public UploadConfig(UploadConfig componentUploadConfig, String clientId, long sizeMax, boolean uniqueFolder, String uploadDirectory, boolean uploadDirectoryAbsolute) {
        this.clientId = clientId;
        this.sizeMax = new Long(sizeMax);
        this.uniqueFolder = Boolean.valueOf(uniqueFolder);
        this.uploadDirectory = uploadDirectory;
        this.uploadDirectoryAbsolute = Boolean.valueOf(uploadDirectoryAbsolute);
        this.progressRender = false;
        this.progressListener = false;
        this.failOnEmptyFile = true;
        if (componentUploadConfig != null) {
            if (componentUploadConfig.formClientId != null) {
                this.formClientId = componentUploadConfig.formClientId;
            }
            if (componentUploadConfig.sizeMax != null) {
                this.sizeMax = componentUploadConfig.sizeMax;
            }
            if (componentUploadConfig.fileNamePattern != null) {
                this.fileNamePattern = componentUploadConfig.fileNamePattern;
            }
            if (componentUploadConfig.uniqueFolder != null) {
                this.uniqueFolder = componentUploadConfig.uniqueFolder;
            }
            if (componentUploadConfig.uploadDirectory != null) {
                this.uploadDirectory = componentUploadConfig.uploadDirectory;
            }
            if (componentUploadConfig.uploadDirectoryAbsolute != null) {
                this.uploadDirectoryAbsolute = componentUploadConfig.uploadDirectoryAbsolute;
            }
            this.progressRender = componentUploadConfig.progressRender;
            this.progressListener = componentUploadConfig.progressListener;
            this.failOnEmptyFile = componentUploadConfig.failOnEmptyFile;
        }
    }

    public String getClientId() {
        return clientId;
    }

    public String getFormClientId() {
        return formClientId;
    }

    public Long getSizeMax() {
        return sizeMax;
    }

    public String getFileNamePattern() {
        return fileNamePattern;
    }

    public Boolean getUniqueFolder() {
        return uniqueFolder;
    }

    public String getUploadDirectory() {
        return uploadDirectory;
    }

    public Boolean getUploadDirectoryAbsolute() {
        return uploadDirectoryAbsolute;
    }

    public boolean isProgressRender() {
        return progressRender;
    }

    public boolean isProgressListener() {
        return progressListener;
    }

    public boolean isFailOnEmptyFile() {
        return failOnEmptyFile;
    }

    public String toString() {
        return "UploadConfig: {" + "\n  clientId=" + clientId + ",\n  formClientId=" + formClientId + ",\n  sizeMax=" + sizeMax + ",\n  fileNamePattern=" + fileNamePattern + ",\n  uniqueFolder=" + uniqueFolder + ",\n  uploadDirectory=" + uploadDirectory + ",\n  uploadDirectoryAbsolute=" + uploadDirectoryAbsolute + ",\n  progressRender=" + progressRender + ",\n  progressListener=" + progressListener + ",\n  failOnEmptyFile=" + failOnEmptyFile + "\n}";
    }
}
