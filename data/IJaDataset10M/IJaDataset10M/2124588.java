package org.akrogen.tkui.dom.xul.dom.trees;

import org.akrogen.tkui.dom.xul.dom.XULElement;

/**
 * XUL treecell interface. A single cell in a {@link Tree}. This element should
 * be placed inside a {@link Treerow}. You can set the text for the cell using
 * the label attribute.
 * 
 * @version 1.0.0
 * @author <a href="mailto:angelo.zerr@gmail.com">Angelo ZERR</a>
 * 
 * @see http://developer.mozilla.org/en/docs/XUL:treecell
 */
public interface Treecell extends XULElement {

    public static final String LABEL_ATTR = "label";

    public static final String SRC_ATTR = "src";

    public static final String COMBOBOX_TYPE = "combobox";

    public static final String CHECKBOX_TYPE = "checkbox";

    public static final String TEXTBOX_TYPE = "texbox";

    public static final String DIALOG_TYPE = "dialog";

    /**
	 * Get label : The label that will appear on the element. If this is left
	 * out, no text appears.
	 * 
	 * @return
	 */
    public String getLabel();

    /**
	 * Set label : The label that will appear on the element. If this is left
	 * out, no text appears.
	 * 
	 * @param label
	 */
    public void setLabel(String label);

    /**
	 * get src image : URL of an image to appear in the tree cell. If this
	 * attribute is left out, no image appears. You can have both an image and a
	 * label.
	 * 
	 * @return
	 */
    public String getSrc();

    /**
	 * Set src image : URL of an image to appear in the tree cell. If this
	 * attribute is left out, no image appears. You can have both an image and a
	 * label.
	 * 
	 * @param src
	 */
    public void setSrc(String src);

    public boolean isEditable();

    public void setEditable(boolean editable);

    public boolean isDisabled();

    public void setDisabled(boolean disabled);
}
