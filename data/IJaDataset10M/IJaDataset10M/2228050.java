package com.kwoksys.action.files;

import com.kwoksys.action.base.BaseTemplate;
import com.kwoksys.biz.files.dto.File;
import com.kwoksys.framework.system.AppPaths;

/**
 * Template for Delete File.
 */
public class FileDeleteTemplate extends BaseTemplate {

    private String formAction;

    private String formCancelAction;

    private File file;

    public FileDeleteTemplate() {
        super(FileDeleteTemplate.class);
    }

    public void applyTemplate() {
        request.setAttribute("FileDeleteTemplate_fileName", file.getLogicalName());
        request.setAttribute("FileDeleteTemplate_fileTitle", file.getTitle());
        request.setAttribute("FileDeleteTemplate_fileSize", file.getFormattedFileSize(requestContext));
        request.setAttribute("FileDeleteTemplate_formAction", formAction);
        request.setAttribute("FileDeleteTemplate_formCancelAction", formCancelAction);
        request.setAttribute("FileDeleteTemplate_deleteImage", AppPaths.ROOT + AppPaths.DELETE_ICON);
    }

    public String getFormAction() {
        return formAction;
    }

    public void setFormAction(String formAction) {
        this.formAction = formAction;
    }

    public String getFormCancelAction() {
        return formCancelAction;
    }

    public void setFormCancelAction(String formCancelAction) {
        this.formCancelAction = formCancelAction;
    }

    public void setFile(File file) {
        this.file = file;
    }
}
