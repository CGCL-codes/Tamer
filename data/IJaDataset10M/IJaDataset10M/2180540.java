package net.sf.ulmac.core.searchsite;

public class SearchSite implements ISearchSite {

    private String fDisplay;

    private String fUrlPre;

    private String fUrlPost;

    private String fSpaceReplacement;

    public SearchSite(String display, String urlPre, String urlPost, String spaceReplacement) {
        fDisplay = display;
        fUrlPre = urlPre;
        fUrlPost = urlPost;
        fSpaceReplacement = spaceReplacement;
    }

    public String getDisplay() {
        return fDisplay;
    }

    @Override
    public String getSpaceReplacement() {
        return fSpaceReplacement;
    }

    public String getUrlPost() {
        return fUrlPost;
    }

    public String getUrlPre() {
        return fUrlPre;
    }

    public void setDisplay(String display) {
        fDisplay = display;
    }

    public void setSpaceReplacement(String spaceReplacement) {
        fSpaceReplacement = spaceReplacement;
    }

    public void setUrlPost(String urlPost) {
        fUrlPost = urlPost;
    }

    public void setUrlPre(String urlPre) {
        fUrlPre = urlPre;
    }
}
