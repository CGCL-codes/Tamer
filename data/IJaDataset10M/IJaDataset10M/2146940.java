package de.fzi.injectj.util.doc;

/**
 * A inject comment tag
 * 
 * @author <a href="mailto:mies@fzi.de">Sebastian Mies</a>
 */
class InjectTag {

    private String name;

    private String text;

    private InjectTag[] insidetags;

    public InjectTag(String name, String text) {
        this.name = name;
        this.text = text;
    }

    public String name() {
        return name;
    }

    public String text() {
        return text;
    }
}
