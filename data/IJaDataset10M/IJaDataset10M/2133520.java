package org.jcrpg.ui.window.element;

import org.jcrpg.ui.text.TextEntry;
import com.ardor3d.math.ColorRGBA;

public class ChoiceDescription {

    public String key;

    public String id;

    public TextEntry text;

    public ChoiceDescription(String key, String id, String text) {
        super();
        this.id = id;
        this.text = new TextEntry(key + " - " + text, new ColorRGBA(ColorRGBA.BLACK));
        this.key = key;
    }

    public ChoiceDescription(String key, String id, TextEntry text) {
        super();
        this.id = id;
        this.text = text;
        this.key = key;
    }
}
