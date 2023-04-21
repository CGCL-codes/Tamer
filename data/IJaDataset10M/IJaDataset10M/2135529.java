package com.volantis.mcs.protocols.trans;

import com.volantis.mcs.dom.Document;
import com.volantis.mcs.dom.Element;
import com.volantis.mcs.dom.WalkingDOMVisitorStub;
import com.volantis.mcs.protocols.DOMHelper;
import com.volantis.mcs.protocols.css.emulator.CSSConstants;
import com.volantis.mcs.protocols.css.emulator.StyleEmulationRenderer;
import java.util.ArrayList;

/**
 * Visitor used to render styling emulation on elements. The visitor also
 * manages the deletion of elements.
 *
 */
public class StyleEmulationRenderingVisitor extends WalkingDOMVisitorStub {

    private StyleEmulationRenderer styleEmulationRenderer = null;

    private ArrayList elementsToBeCollapsed = null;

    public StyleEmulationRenderingVisitor(StyleEmulationRenderer renderer) {
        styleEmulationRenderer = renderer;
        elementsToBeCollapsed = new ArrayList();
    }

    public void visit(Element element) {
        styleEmulationRenderer.applyStyleToElement(element);
        if (element.getName().equals(CSSConstants.STYLE_ELEMENT)) {
            elementsToBeCollapsed.add(element);
        }
    }

    /**
     * After we have visited all elements see if any of them have been marked
     * for "collapse" - (Paul D doesn't like the word delete).
     *
     * @param element
     */
    public void afterChildren(Document element) {
        DOMHelper.collapseElements(elementsToBeCollapsed);
    }
}
