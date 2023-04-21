package org.xfc.help;

/**
 * 
 * 
 * @author Devon Carew
 */
class XTableOfContents extends XContentsItem {

    private String tocLabel;

    private boolean error;

    XTableOfContents(String title) {
        super(title);
    }

    XTableOfContents(String title, String tocLabel) {
        super(title);
        this.tocLabel = tocLabel;
    }

    public String getTOCLabel() {
        return tocLabel;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public boolean hasError() {
        return error;
    }

    public void setError(boolean error) {
        this.error = error;
    }
}
