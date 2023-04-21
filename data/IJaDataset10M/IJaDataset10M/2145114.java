package org.ztemplates.html;

public class ZLink {

    private final String text;

    private final String href;

    private String title;

    public ZLink(String text, String href, String title) {
        super();
        this.text = text;
        this.href = href;
        this.title = title;
    }

    public ZLink(String text, String href) {
        super();
        this.text = text;
        this.href = href;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getText() {
        return text;
    }

    public String getHref() {
        return href;
    }
}
