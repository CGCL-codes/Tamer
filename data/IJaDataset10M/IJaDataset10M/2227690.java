package net.sf.colorer.handlers;

public class TextRegion extends RegionDefine {

    public static final String HRD_TEXT_CLASS = "text";

    public TextRegion(String stext, String etext, String sback, String eback) {
        this.stext = stext;
        this.etext = etext;
        this.sback = sback;
        this.eback = eback;
    }

    /** Textual mapping */
    public String stext;

    /** Textual mapping */
    public String etext;

    /** Textual mapping */
    public String sback;

    /** Textual mapping */
    public String eback;
}

;
