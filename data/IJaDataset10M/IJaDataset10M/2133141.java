package com.izforge.izpack.gui;

import javax.swing.*;
import java.util.TreeMap;

/**
 * The icons database class.
 *
 * @author Julien Ponge October 27, 2002
 */
public class IconsDatabase extends TreeMap {

    private static final long serialVersionUID = 3257567287145083446L;

    /**
     * The constructor.
     */
    public IconsDatabase() {
        super();
    }

    /**
     * Convenience method to retrieve an element.
     *
     * @param key The icon key.
     * @return The icon as an ImageIcon object.
     */
    public ImageIcon getImageIcon(String key) {
        return (ImageIcon) get(key);
    }
}
