package org.rg.scanner.extractors;

import java.net.URL;

/**
 * Represents a hyperlink extracted from a document.
 * @author mjberry
 */
public final class ExtractedLink {

    /** this is the maximum number of links we will parse out of a page. */
    public static final int MAX_LINKS = 50000;

    /** the URL of the hyperlinked document. */
    private final URL _url;

    /** the anchor text. */
    private final String _anchorText;

    /** the index in the stripped content where anchorText starts */
    private int _startPt;

    /** the index in the stripped content where anchorText endss */
    private int _endPt;

    /**
     * Creates a new ExtractedLink object.
     * @param url the URL of the hyperlinked document.
     * @param anchorText the anchor text.
     */
    public ExtractedLink(final URL url, final String anchorText) {
        _url = url;
        _anchorText = anchorText;
    }

    /**
     * The starting point for this link.
     * @param start starting point.
     */
    public void setStartPt(int start) {
        this._startPt = start;
    }

    /**
     * the ending point.
     * @param end the end point.
     */
    public void setEndPt(int end) {
        this._endPt = end;
    }

    /**
     * @return the starting point.
     */
    public int getStartPt() {
        return this._startPt;
    }

    /**
     * @return return the end point.
     */
    public int getEndPt() {
        return this._endPt;
    }

    /**
     * Returns the URL of the hyperlinked document.
     * @return the URL of the hyperlinked document.
     */
    public final URL getUrl() {
        return _url;
    }

    /**
     * Returns the anchor text.
     * @return the anchor text.
     */
    public final String getAnchorText() {
        return _anchorText;
    }

    /**
     * Returns the string representation of this object. Note that the
     * particular content and formatting are subject to change.
     * @return the string representation of this object.
     */
    public final String toString() {
        final StringBuffer sb = new StringBuffer();
        sb.append("ExtractedLink: ");
        sb.append("URL = ").append(_url.toString());
        sb.append(", anchor = ").append(_anchorText);
        return sb.toString();
    }
}
