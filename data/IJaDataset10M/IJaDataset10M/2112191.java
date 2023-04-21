package org.akrogen.tkui.dom.xul.dom.listboxes;

import org.akrogen.tkui.dom.xul.dom.XULElement;

/**
 * XUL listheader interface. A header for a single column in a {@link Listbox}.
 * 
 * @version 1.0.0
 * @author <a href="mailto:angelo.zerr@gmail.com">Angelo ZERR</a>
 * 
 * @see http://developer.mozilla.org/en/docs/XUL:listheader
 */
public interface Listheader extends XULElement {

    public static final String LABEL_ATTR = "label";

    public String getLabel();

    public void setLabel(String label);
}
