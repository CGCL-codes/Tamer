package com.asl.library.controllers.upload;

import java.io.Serializable;
import org.springframework.web.multipart.MultipartFile;

/**
 * @author asl
 *
 */
public class UploadFormBean implements Serializable {

    private static final long serialVersionUID = -5616695783783998496L;

    private MultipartFile file;

    private String name;

    private String category;

    private String tags;

    public MultipartFile getFile() {
        return file;
    }

    public void setFile(MultipartFile file) {
        this.file = file;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    /**
	 * @return the tags
	 */
    public String getTags() {
        return tags;
    }

    /**
	 * @param tags the tags to set
	 */
    public void setTags(String tags) {
        this.tags = tags;
    }
}
