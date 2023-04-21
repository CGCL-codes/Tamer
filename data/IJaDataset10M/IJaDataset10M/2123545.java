package com.jachsoft.cbir;

public class ImageDatabaseEntry {

    private String url;

    private ImageContentDescriptor descriptor;

    public ImageContentDescriptor getDescriptor() {
        return descriptor;
    }

    public void setDescriptor(ImageContentDescriptor descriptor) {
        this.descriptor = descriptor;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
