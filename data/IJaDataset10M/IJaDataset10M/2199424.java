package org.kommando.core.catalog;

import javax.swing.Icon;
import javax.swing.ImageIcon;

/**
 * @author Peter De Bruycker
 */
public class TextObject extends SimpleCatalogObject {

    private static final Icon TEXT_ICON = new ImageIcon(TextObject.class.getResource("/icons/text.png"));

    public TextObject(String text) {
        super("text:" + text, text, text, TEXT_ICON);
        setExtension(String.class, text);
    }
}
