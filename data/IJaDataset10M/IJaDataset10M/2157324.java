package scrobblerj.tag;

import java.net.MalformedURLException;
import java.net.URL;

public class Tag {

    private String tagName;

    private URL url;

    public Tag(String tagName) {
        this.tagName = tagName;
        try {
            url = new URL("http://www.last.fm/tag/" + tagName);
        } catch (MalformedURLException e) {
        }
    }

    public String getTagName() {
        return tagName;
    }

    public URL getURL() {
        return url;
    }
}
