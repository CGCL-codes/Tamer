package ch.oblivion.comixviewer.engine;

import java.io.File;
import java.io.InputStream;
import java.net.URL;

/**
 * Contains one comic page with a reference to the image. A Page is
 * always associated with a Profile.
 * 
 * @author Stefan Gmeiner
 */
public abstract class Page {

    private final int pageId;

    private final Profile profile;

    private final URL pageUrl;

    private final URL imageUrl;

    private final URL previousUrl;

    private final URL nextUrl;

    private String title;

    private String description;

    private File imageName;

    private int previousPageId;

    private int nextPageId;

    protected Page(int pageId, Profile profile, URL pageUrl, URL imageUrl, URL previousUrl, URL nextUrl, String title, String description) {
        this.pageId = pageId;
        this.profile = profile;
        this.pageUrl = pageUrl;
        this.imageUrl = imageUrl;
        this.previousUrl = previousUrl;
        this.nextUrl = nextUrl;
        this.title = title;
        this.description = description;
    }

    public int getPageId() {
        return pageId;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public URL getPageUrl() {
        return pageUrl;
    }

    public InputStream getImage() {
        return null;
    }

    public Profile getProfile() {
        return profile;
    }

    public Page getNextPage() {
        Page nextPage = null;
        if (nextPageId != 0) {
            nextPage = profile.getPage(nextPageId);
        }
        if (nextPage == null && nextUrl != null) {
            nextPage = profile.createPage(nextUrl, this, null);
        }
        return nextPage;
    }

    public Page getPreviousPage() {
        Page previousPage = null;
        if (previousPageId != 0) {
            previousPage = profile.getPage(previousPageId);
        }
        if (previousPage == null && previousUrl != null) {
            previousPage = profile.createPage(previousUrl, this, null);
        }
        return previousPage;
    }

    int getPreviousPageId() {
        return previousPageId;
    }

    void setPreviousPageId(int previousPageId) {
        this.previousPageId = previousPageId;
    }

    int getNextPageId() {
        return nextPageId;
    }

    void setNextPageId(int nextPageId) {
        this.nextPageId = nextPageId;
    }
}
