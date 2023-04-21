package org.omegat.convert.v20to21.data;

import java.io.Serializable;

/**
 * Options for OpenXML filter.
 * Serializable to allow saving to / reading from configuration file.
 * <p>
 * OpenDoc filter have the following options
 * ([+] means default on).
 * Translatable elements:
 * <ul>
 * <li>[] Hidden text (Word)
 * <li>[+] Comments (Word, Excel)
 * <li>[+] Footnotes (Word)
 * <li>[+] Endnotes (Words)
 * <li>[+] Header (Words)
 * <li>[+] Footer (Words)
 * <li>[+] Slide comments (PowerPoint)
 * <li>[] Slide Masters (PowerPoint)
 * </ul>
 * @author Didier Briel
 */
public class OpenXMLOptions implements Serializable {

    /** Hold value of properties. */
    private boolean translateHiddenText = false;

    private boolean translateComments = true;

    private boolean translateFootnotes = true;

    private boolean translateEndnotes = true;

    private boolean translateHeaders = true;

    private boolean translateFooters = true;

    private boolean translateExcelComments = true;

    private boolean translateSlideComments = true;

    private boolean translateSlideMasters = false;

    /**
     * Returns whether Hidden Text should be translated.
     */
    public boolean getTranslateHiddenText() {
        return this.translateHiddenText;
    }

    /**
     * Sets whether Hidden Text should be translated.
     */
    public void setTranslateHiddenText(boolean translateHiddenText) {
        this.translateHiddenText = translateHiddenText;
    }

    /**
     * Returns whether Commments should be translated.
     */
    public boolean getTranslateComments() {
        return this.translateComments;
    }

    /**
     * Sets whether Comments should be translated.
     */
    public void setTranslateComments(boolean translateComments) {
        this.translateComments = translateComments;
    }

    /**
     * Returns whether Footnotes should be translated.
     */
    public boolean getTranslateFootnotes() {
        return this.translateFootnotes;
    }

    /**
     * Sets whether Footnotes should be translated.
     */
    public void setTranslateFootnotes(boolean translateFootnotes) {
        this.translateFootnotes = translateFootnotes;
    }

    /**
     * Returns whether Endnotes should be translated.
     */
    public boolean getTranslateEndnotes() {
        return this.translateEndnotes;
    }

    /**
     * Sets whether Footnotes should be translated.
     */
    public void setTranslateEndnotes(boolean translateEndnotes) {
        this.translateEndnotes = translateEndnotes;
    }

    /**
     * Returns whether Headers should be translated.
     */
    public boolean getTranslateHeaders() {
        return this.translateHeaders;
    }

    /**
     * Sets whether Headers should be translated.
     */
    public void setTranslateHeaders(boolean translateHeaders) {
        this.translateHeaders = translateHeaders;
    }

    /**
     * Returns whether Footers should be translated.
     */
    public boolean getTranslateFooters() {
        return this.translateFooters;
    }

    /**
     * Sets whether Footers should be translated.
     */
    public void setTranslateFooters(boolean translateFooters) {
        this.translateFooters = translateFooters;
    }

    /**
     * Returns whether Excel Comments should be translated.
     */
    public boolean getTranslateExcelComments() {
        return this.translateExcelComments;
    }

    /**
     * Sets whether Excel Comments should be translated.
     */
    public void setTranslateExcelComments(boolean translateExcelComments) {
        this.translateExcelComments = translateExcelComments;
    }

    /**
     * Returns whether Slide Comments should be translated.
     */
    public boolean getTranslateSlideComments() {
        return this.translateSlideComments;
    }

    /**
     * Sets whether Slide Comments should be translated.
     */
    public void setTranslateSlideComments(boolean translateSlideComments) {
        this.translateSlideComments = translateSlideComments;
    }

    /**
     * Returns whether Slide Masters should be translated.
     */
    public boolean getTranslateSlideMasters() {
        return this.translateSlideMasters;
    }

    /**
     * Sets whether Slide Masters should be translated.
     */
    public void setTranslateSlideMasters(boolean translateSlideMasters) {
        this.translateSlideMasters = translateSlideMasters;
    }
}
