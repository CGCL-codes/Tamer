package org.palo.api.impl;

import org.palo.api.Consolidation;
import org.palo.api.Element;
import org.palo.api.ElementNode;

/**
 * <code>ElementNode2</code>
 * <b>INTERNAL CLASS ONLY</b>
 *
 * @author ArndHouben
 * @version $Id: ElementNode2.java,v 1.2 2008/02/27 15:34:09 ArndHouben Exp $
 **/
class ElementNode2 extends ElementNode {

    /**
	 * Constructs a new <code>ElementNode</code>
	 * @param element the {@link Element} to wrap.
	 */
    ElementNode2(Element element) {
        this(element, null);
    }

    /**
	 * Constructs a new <code>ElementNode</code>
	 * @param element the {@link Element} to wrap.
	 * @param consolidation the {@link Consolidation} of this node.
	 */
    ElementNode2(Element element, Consolidation consolidation) {
        this(element, consolidation, -1);
    }

    /**
	 * Constructs a new <code>ElementNode</code>
	 * @param element the {@link Element} to wrap.
	 * @param consolidation the {@link Consolidation} of this node.
	 * @param index index in parent (optional)
	 */
    ElementNode2(Element element, Consolidation consolidation, int index) {
        super(element, consolidation, index);
    }

    public final synchronized boolean hasChildren() {
        return (element.getChildCount() > 0);
    }

    public final synchronized ElementNode[] getChildren() {
        if (element.getChildCount() > 0 && children.isEmpty()) {
            Element[] _children = element.getChildren();
            Consolidation consolidations[] = element.getConsolidations();
            for (int i = 0; i < _children.length; ++i) {
                if (_children[i] == null) continue;
                ElementNode child = new ElementNode2(_children[i], consolidations[i]);
                addChild(child);
            }
        }
        return (ElementNode[]) children.toArray(new ElementNode[children.size()]);
    }
}
