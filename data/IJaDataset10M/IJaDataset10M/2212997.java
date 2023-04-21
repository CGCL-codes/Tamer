package co.fxl.gui.upload.impl;

import co.fxl.gui.upload.api.IUpload;

public class UploadImpl implements IUpload {

    private String url;

    private String name;

    private String description;

    private boolean isFileUpload;

    public UploadImpl(String text, String text2, String text3, boolean isFileUpload) {
        url = text;
        name = text2;
        description = text3;
        this.isFileUpload = isFileUpload;
    }

    @Override
    public String uRL() {
        return url;
    }

    @Override
    public String name() {
        return name;
    }

    @Override
    public String description() {
        return description;
    }

    @Override
    public boolean isFileUpload() {
        return isFileUpload;
    }
}
