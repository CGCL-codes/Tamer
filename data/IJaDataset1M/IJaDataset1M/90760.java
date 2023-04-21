package spidr.datamodel;

import java.io.Serializable;

public class Image implements Serializable {

    private String title;

    private String source;

    private String preview;

    private String fullres;

    public void setPreview(String preview) {
        this.preview = preview;
    }

    public String getPreview() {
        return preview;
    }

    public void setFullres(String fullres) {
        this.fullres = fullres;
    }

    public String getFullres() {
        return fullres;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTitle() {
        return title;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getSource() {
        return source;
    }
}
