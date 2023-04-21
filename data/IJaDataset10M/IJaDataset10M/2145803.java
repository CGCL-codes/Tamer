package de.mpiwg.vspace.workspace.template.slides.ui.dialog.controller;

public class CSSFile {

    private String filename;

    private String content;

    public CSSFile(String filename, String content) {
        super();
        this.filename = filename;
        this.content = content;
    }

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof CSSFile)) return false;
        if (((CSSFile) obj).getFilename().equals(this.getFilename())) return true;
        return false;
    }
}
