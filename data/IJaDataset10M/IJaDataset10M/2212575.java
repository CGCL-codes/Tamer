package org.jpox.samples.one_one.unidir_2;

/**
 * Newspaper.
 * 
 * @version $Revision: 1.1 $
 */
public class Newspaper extends MediaWork {

    private String editor;

    private String style;

    public Newspaper(String name, int freq, String editor, String style) {
        super(name, freq);
        this.editor = editor;
        this.style = style;
    }

    public String toString() {
        return "Newspaper : " + name + " style=" + style + " editor=" + editor;
    }
}
