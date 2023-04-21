package org.iebrowser;

public class Bookmark {

    private String title;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    private String url;

    public Bookmark(String title, String url) {
        super();
        this.title = title;
        this.url = url;
    }

    public String toString() {
        return title + "~~~" + url;
    }

    public static Bookmark fromString(String bk) {
        String[] parts = bk.split("~~~");
        if (parts.length != 2) return null;
        return new Bookmark(parts[0], parts[1]);
    }
}
