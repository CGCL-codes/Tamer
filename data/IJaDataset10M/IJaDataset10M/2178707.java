package com.jspx.txweb.support;

import com.jspx.upload.MultipartRequest;
import com.jspx.upload.UploadedFile;
import com.jspx.txweb.annotation.MulRequest;
import com.jspx.txweb.annotation.Operate;

/**
 * Created by IntelliJ IDEA.
 * User: Administrator
 * Date: 2007-8-18
 * Time: 20:46:21
 *  com.jspx.txweb.support.DefaultUploadAction
 */
public class DefaultUploadAction extends MultipartSupport {

    @MulRequest(covering = "false", saveDirectory = "@saveDirectory", fileTypes = "@fileTypes", maxPostSize = "@maxPostSize")
    public void setMultipartRequest(MultipartRequest multipartRequest) {
        this.multipartRequest = multipartRequest;
    }

    @Operate(submit = "submit")
    public void save() throws Exception {
        for (UploadedFile uploadFile : multipartRequest.getFiles()) {
            addActionMessage("upload file:" + uploadFile.getFileName() + "  form " + uploadFile.getOriginal());
        }
    }

    public String execute() throws Exception {
        return SUCCESS;
    }
}
