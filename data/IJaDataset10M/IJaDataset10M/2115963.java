package parser;

/**
 * Nestor web parser abstract parent class
 * All source tab extensions should extend this class
 * @author Xu Ye (Leaf)
 */
public abstract class ParsedInfo {

    private String url;

    private String info;

    private String currentURL;

    public ParsedInfo() {
        this.url = "";
        this.info = "";
        this.currentURL = "";
    }

    public String getInfo(String phrase) {
        return info;
    }

    public String getCurrentURL() {
        return currentURL;
    }
}
