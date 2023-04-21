package com.asl.library.domain.model;

import java.util.Date;
import org.apache.commons.io.FilenameUtils;

/**
 * @author asl
 *
 */
public class FileEntry extends BaseEntity {

    private static final long serialVersionUID = -6433610345808941409L;

    private String fileId;

    private String originalFileName;

    private String path;

    private Date createdDate;

    /**
	 * @return the fileId
	 */
    public String getFileId() {
        return fileId;
    }

    /**
	 * @param fileId the fileId to set
	 */
    public void setFileId(String fileId) {
        this.fileId = fileId;
    }

    /**
	 * @return the originalFileName
	 */
    public String getOriginalFileName() {
        return originalFileName;
    }

    /**
	 * @param originalFileName the originalFileName to set
	 */
    public void setOriginalFileName(String originalFileName) {
        this.originalFileName = originalFileName;
    }

    /**
	 * @return the path
	 */
    public String getPath() {
        return path;
    }

    /**
	 * @param path the path to set
	 */
    public void setPath(String path) {
        this.path = path;
    }

    /**
	 * @return the createdDate
	 */
    public Date getCreatedDate() {
        return createdDate;
    }

    /**
	 * @param createdDate the createdDate to set
	 */
    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }

    public String getFileName() {
        return FilenameUtils.removeExtension(fileId);
    }

    public String getFileExtension() {
        return FilenameUtils.getExtension(fileId);
    }
}
