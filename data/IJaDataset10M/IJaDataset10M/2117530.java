package org.itsnat.impl.core.domutil;

import org.itsnat.core.domutil.ElementTableRenderer;
import org.itsnat.core.domutil.ElementTable;
import org.itsnat.impl.core.util.*;
import org.w3c.dom.Element;

/**
 *
 * @author jmarranz
 */
public class ElementTableRendererDefaultImpl implements ElementTableRenderer {

    protected static final ElementTableRendererDefaultImpl SINGLETON = new ElementTableRendererDefaultImpl();

    /**
     * Creates a new instance of ElementTableRendererDefaultImpl
     */
    private ElementTableRendererDefaultImpl() {
    }

    public static ElementTableRendererDefaultImpl newElementTableRendererDefault() {
        return SINGLETON;
    }

    public void renderTable(ElementTable table, int row, int col, Object value, Element cellContentElem, boolean isNew) {
        if (cellContentElem == null) cellContentElem = table.getCellContentElementAt(row, col);
        ElementRendererDefaultImpl.SINGLETON.render(null, value, cellContentElem, isNew);
    }

    public void unrenderTable(ElementTable table, int row, int col, Element cellContentElem) {
    }
}
