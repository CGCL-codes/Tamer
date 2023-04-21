package org.elephantt.webby;

public class SimpleView {

    private final String filename;

    private final Object model;

    private final String contentType;

    public SimpleView(String filename, Object model) {
        this(filename, model, null);
    }

    public SimpleView(String filename, Object model, String contentType) {
        this.filename = filename;
        this.model = model;
        this.contentType = contentType;
    }

    public String getFilename() {
        return filename;
    }

    public Object getModel() {
        return model;
    }

    public String getContentType() {
        return contentType;
    }
}
