package edu.gsbme.wasabi.Observatory;

import org.w3c.dom.Element;

/**
 * This data structure can be used to pass data when a update operation in the observation pattern is called.
 * @author David
 *
 */
public class ObserveData {

    public ModOperation type;

    public Element old_element;

    public Element new_element;

    public Element to_delete;

    public String find_text;

    public String replace_text;

    public void dispose() {
        type = null;
        old_element = null;
        new_element = null;
        to_delete = null;
    }
}
