package com.ekeyman.securesave.action;

import java.io.InputStream;
import org.springframework.security.core.context.SecurityContextHolder;
import com.ekeyman.securesave.security.EkeymanUserDetails;
import com.ekeyman.securesavelib.business.ResourceManager;
import com.ekeyman.securesavelib.domain.User;
import com.ekeyman.securesavelib.dto.FileDownloadResponse;
import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.Preparable;

public class DownloadAction extends ActionSupport implements Preparable {

    private static final long serialVersionUID = 5091338392898496091L;

    private String openidurl;

    private long resourceId;

    private ResourceManager resourceManager;

    private InputStream fileInputStream;

    private String fileType;

    private String fileDisposition;

    private String downloadPassphrase;

    @Override
    public void prepare() throws Exception {
        EkeymanUserDetails ekeymanUserDetails = (EkeymanUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        setOpenidurl(ekeymanUserDetails.getUsername());
    }

    public String execute() throws Exception {
        String result = SUCCESS;
        User user = getResourceManager().getUser(getOpenidurl());
        if (user == null) {
            result = ERROR;
        } else {
            try {
                FileDownloadResponse fileDownloadResponse = getResourceManager().processDownloadFile(resourceId, user, downloadPassphrase);
                fileInputStream = fileDownloadResponse.getFileInputStream();
                fileType = fileDownloadResponse.getFiletype();
                fileDisposition = "attachment;filename=\"" + fileDownloadResponse.getFilename() + "\"";
            } catch (Exception e) {
                result = ERROR;
            }
        }
        return result;
    }

    public void setOpenidurl(String openidurl) {
        this.openidurl = openidurl;
    }

    public InputStream getFileInputStream() {
        return fileInputStream;
    }

    public String getOpenidurl() {
        return openidurl;
    }

    public void setResourceId(long resourceId) {
        this.resourceId = resourceId;
    }

    public long getResourceId() {
        return resourceId;
    }

    public void setFileType(String fileType) {
        this.fileType = fileType;
    }

    public String getFileType() {
        return fileType;
    }

    public void setFileDisposition(String fileDisposition) {
        this.fileDisposition = fileDisposition;
    }

    public String getFileDisposition() {
        return fileDisposition;
    }

    public void setDownloadPassphrase(String downloadPassphrase) {
        this.downloadPassphrase = downloadPassphrase;
    }

    public String getDownloadPassphrase() {
        return downloadPassphrase;
    }

    public void setResourceManager(ResourceManager resourceManager) {
        this.resourceManager = resourceManager;
    }

    public ResourceManager getResourceManager() {
        return resourceManager;
    }
}
