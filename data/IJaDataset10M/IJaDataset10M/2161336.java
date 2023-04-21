package com.hk.svr.processor;

import java.util.List;
import com.hk.bean.CmpFile;

public class CmpArticleUploadFileResult {

    private boolean fileUploadError;

    private List<CmpFile> cmpFileList;

    private int outOfSizeErrorNum;

    private int fmtErrorNum;

    public void addOutOfSizeErrorNum(int add) {
        this.setOutOfSizeErrorNum(this.getOutOfSizeErrorNum() + add);
    }

    public void addFmtErrorNum(int add) {
        this.setFmtErrorNum(this.getFmtErrorNum() + add);
    }

    public void setCmpFileList(List<CmpFile> cmpFileList) {
        this.cmpFileList = cmpFileList;
    }

    public List<CmpFile> getCmpFileList() {
        return cmpFileList;
    }

    public void setFileUploadError(boolean fileUploadError) {
        this.fileUploadError = fileUploadError;
    }

    public boolean isFileUploadError() {
        return fileUploadError;
    }

    public int getOutOfSizeErrorNum() {
        return outOfSizeErrorNum;
    }

    public void setOutOfSizeErrorNum(int outOfSizeErrorNum) {
        this.outOfSizeErrorNum = outOfSizeErrorNum;
    }

    public int getFmtErrorNum() {
        return fmtErrorNum;
    }

    public void setFmtErrorNum(int fmtErrorNum) {
        this.fmtErrorNum = fmtErrorNum;
    }
}
