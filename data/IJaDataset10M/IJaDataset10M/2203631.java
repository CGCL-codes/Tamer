package org.jamwiki.model;

/**
 *
 * @author dfisla
 */
public class WikiImageResource implements Comparable<WikiImageResource> {

    private String name;

    private String parentName;

    private String path;

    private String URL;

    private int isThumb = 0;

    private Integer size = -1;

    public WikiImageResource() {
    }

    public WikiImageResource(String fileName, String path) {
        this.name = fileName;
        this.path = path;
    }

    public String getName() {
        return name;
    }

    public void setName(String fileName) {
        this.name = fileName;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getURL() {
        return URL;
    }

    public void setURL(String URL) {
        this.URL = URL;
    }

    public int getIsThumb() {
        return isThumb;
    }

    public void setIsThumb(int isThumb) {
        this.isThumb = isThumb;
    }

    public String getParentName() {
        return parentName;
    }

    public void setParentName(String parentName) {
        this.parentName = parentName;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public int compareTo(WikiImageResource n) {
        return size.compareTo(n.size);
    }
}
