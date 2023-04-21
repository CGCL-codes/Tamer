package org.mss.mozilla.model.mozilla;

/**
 * MK 07.09.2008
 * 
 * URLModel 
 * Store Static and Dynamic URLs from Mozilla Wizard
 * 
 * @author Markus
 *
 */
public class URLModel {

    private int position;

    private String LinkName;

    private String URL;

    private String Type;

    public URLModel(int position, String LinkName, String URL, String Type) {
        this.position = position;
        this.LinkName = LinkName;
        this.URL = URL;
        this.Type = Type;
    }

    public URLModel() {
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public String getLinkName() {
        return LinkName;
    }

    public void setLinkName(String linkName) {
        LinkName = linkName;
    }

    public String getURL() {
        return URL;
    }

    public void setURL(String url) {
        URL = url;
    }

    public String getType() {
        return Type;
    }

    public void setType(String type) {
        Type = type;
    }
}
