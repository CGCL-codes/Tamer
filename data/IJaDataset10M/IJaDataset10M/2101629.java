package infoviewer;

/**
 * a TitledURLEntry stores an URL together with a title string and possibly a scrollbar
 * value. If the URL represents an HTML document, the title (from the &lt;TITLE&gt; tag) will be
 * stored there. The URL is stored as string. No checking for
 * MalformedURLExceptions is done on the URL.
 */
public class TitledURLEntry implements Cloneable {

    private String title = null;

    private String url = null;

    private int scrollBarPos = -1;

    /** new TitledURLEntry with title and url */
    public TitledURLEntry(String title, String url) {
        this.title = title;
        this.url = url;
    }

    public TitledURLEntry(String title, String url, int scrPos) {
        this(title, url);
        scrollBarPos = scrPos;
    }

    public String getTitle() {
        return title;
    }

    public String getURL() {
        return url;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setURL(String url) {
        this.url = url;
    }

    public int getScrollBarPos() {
        return scrollBarPos;
    }

    public void setScrollBarPos(int newPos) {
        scrollBarPos = newPos;
    }

    public boolean equals(TitledURLEntry other) {
        if (!url.equals(other.url)) return false;
        return true;
    }

    public TitledURLEntry getClone() {
        TitledURLEntry e = new TitledURLEntry(title, url);
        e.scrollBarPos = this.scrollBarPos;
        return e;
    }

    public Object clone() {
        return getClone();
    }

    public String toString() {
        return url;
    }
}
