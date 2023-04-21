package org.exmaralda.coma.root;

import javax.swing.table.DefaultTableCellRenderer;
import org.jdom.Element;

/**
 * coma2/org.sfb538.coma2/CommTableRenderer.java
 * @author woerner
 * 
 */
public class CommTableRenderer extends DefaultTableCellRenderer {

    /**
	 * 
	 */
    public CommTableRenderer() {
        super();
    }

    @Override
    public void setValue(Object value) {
        if (value instanceof Element) {
            setText(((Element) value).getAttributeValue("Name"));
        } else {
            setText(value.getClass().getName());
        }
    }
}
