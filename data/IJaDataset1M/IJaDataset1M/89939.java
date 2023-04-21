package net.bajawa.scrapers.moviedb.model;

import java.io.Serializable;

public class Trailer implements Serializable {

    private static final long serialVersionUID = -1462665285485322013L;

    private String source;

    private String url;

    public Trailer(String source, String url) {
        this.source = source;
        this.url = url;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
