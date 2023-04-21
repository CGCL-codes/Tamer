package com.tony.common.web.upload;

import java.io.Serializable;
import org.apache.struts.upload.FormFile;

/**
  * <p>Title: </p>
  *
  * <p>Description: </p>
  *
  * <p>Copyright: Copyright (c) 2006</p>
  *
  * <p>Company: </p>
  *
  * @author not attributable
  * @version 1.0
  */
public class UploadFile implements Serializable {

    private FormFile file;

    private boolean valid;

    public boolean isValid() {
        return valid;
    }

    public void setValid(boolean valid) {
        this.valid = valid;
    }

    public FormFile getFile() {
        return file;
    }

    public void setFile(FormFile file) {
        this.file = file;
    }

    private String attachTitle;

    public String getAttachTitle() {
        return (attachTitle == null || attachTitle.trim().length() < 1) ? "" : attachTitle;
    }

    public void setAttachTitle(String attachTitle) {
        this.attachTitle = attachTitle;
    }

    private String businessCode;

    private String serialId;

    public String getBusinessCode() {
        return (businessCode == null || businessCode.trim().length() < 1) ? "" : businessCode;
    }

    public void setBusinessCode(String businessCode) {
        this.businessCode = businessCode;
    }

    public String getSerialId() {
        return (serialId == null || serialId.trim().length() < 1) ? "" : serialId;
    }

    public void setSerialId(String serialId) {
        this.serialId = serialId;
    }

    private String businessSubCode;

    private String businessType;

    private String originalPath;

    private String originalName;

    private String serverPath;

    private String serverName;

    private String providerEmp;

    private String uploadDate;

    private String remark;

    private String belongingOrgan;

    public String getBusinessSubCode() {
        return (businessSubCode == null || businessSubCode.trim().length() < 1) ? "" : businessSubCode;
    }

    public void setBusinessSubCode(String businessSubCode) {
        this.businessSubCode = businessSubCode;
    }

    public String getBusinessType() {
        return (businessType == null || businessType.trim().length() < 1) ? "" : businessType;
    }

    public void setBusinessType(String businessType) {
        this.businessType = businessType;
    }

    public String getOriginalPath() {
        return (originalPath == null || originalPath.trim().length() < 1) ? "" : originalPath;
    }

    public void setOriginalPath(String originalPath) {
        this.originalPath = originalPath;
    }

    public String getOriginalName() {
        return (originalName == null || originalName.trim().length() < 1) ? "" : originalName;
    }

    public void setOriginalName(String originalName) {
        this.originalName = originalName;
    }

    public String getServerPath() {
        return (serverPath == null || serverPath.trim().length() < 1) ? "" : serverPath;
    }

    public void setServerPath(String serverPath) {
        this.serverPath = serverPath;
    }

    public String getServerName() {
        return (serverName == null || serverName.trim().length() < 1) ? "" : serverName;
    }

    public void setServerName(String serverName) {
        this.serverName = serverName;
    }

    public String getProviderEmp() {
        return (providerEmp == null || providerEmp.trim().length() < 1) ? "" : providerEmp;
    }

    public void setProviderEmp(String providerEmp) {
        this.providerEmp = providerEmp;
    }

    public String getUploadDate() {
        return (uploadDate == null || uploadDate.trim().length() < 1) ? "" : uploadDate;
    }

    public void setUploadDate(String uploadDate) {
        this.uploadDate = uploadDate;
    }

    public String getRemark() {
        return (remark == null || remark.trim().length() < 1) ? "" : remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getBelongingOrgan() {
        return (belongingOrgan == null || belongingOrgan.trim().length() < 1) ? "" : belongingOrgan;
    }

    public void setBelongingOrgan(String belongingOrgan) {
        this.belongingOrgan = belongingOrgan;
    }

    public void reset() {
        this.setAttachTitle(null);
        this.setBelongingOrgan(null);
        this.setBusinessCode(null);
        this.setBusinessSubCode(null);
        this.setBusinessType(null);
        this.setFile(null);
        this.setOriginalName(null);
        this.setOriginalPath(null);
        this.setProviderEmp(null);
        this.setRemark(null);
        this.setSerialId(null);
        this.setServerName(null);
        this.setServerPath(null);
        this.setUploadDate(null);
    }
}
