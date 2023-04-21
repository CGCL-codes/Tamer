package com.volantis.mcs.dissection.impl;

import com.volantis.mcs.dissection.DissectionException;
import com.volantis.mcs.dissection.DissectableAreaIdentity;
import com.volantis.mcs.dissection.DissectableAreaAttributes;
import com.volantis.mcs.dissection.links.ShardLinkGroupAttributes;
import com.volantis.mcs.dissection.links.ShardLinkAttributes;
import com.volantis.mcs.dissection.links.ShardLinkConditionalAttributes;
import com.volantis.mcs.dissection.dom.ElementType;
import com.volantis.mcs.dissection.dom.DissectableElement;
import com.volantis.mcs.dissection.dom.DissectionElementTypes;
import com.volantis.mcs.dissection.dom.DocumentVisitor;
import com.volantis.mcs.dom.Element;
import com.volantis.mcs.dom.NodeIteratee;
import com.volantis.mcs.dom.Node;
import com.volantis.mcs.protocols.dissection.DissectionConstants;
import com.volantis.shared.iteration.IterationAction;
import com.volantis.shared.throwable.ExtendedRuntimeException;

/**
 * This class encapsulates DOM Element for purposes of dissection.
 */
public class DOMDissectableElementImpl extends DOMDissectableNodeAbstract implements DissectableElement {

    protected final Element element;

    protected final DOMDissectableDocumentImpl document;

    protected final ElementType type;

    private final boolean atomic;

    public DOMDissectableElementImpl(Element element, DOMDissectableDocumentImpl document, boolean atomic) {
        this.element = element;
        this.document = document;
        this.atomic = atomic;
        this.type = (ElementType) DissectionElementTypes.getAllTypes().get(this.element.getName());
        initializeNodeAnnotation();
    }

    private void initializeNodeAnnotation() {
        if (getNodeAnnotation() != null) {
            throw new RuntimeException("Node annotation already initialized");
        }
        if (type == null || type == DissectionElementTypes.getPlainElementType()) {
        } else if (type == DissectionElementTypes.getDissectableAreaType()) {
            DissectableAreaAttributes annotation = new DissectableAreaAttributes();
            annotation.setIdentity(new DissectableAreaIdentity(element.getAttributeValue(DissectionConstants.INCLUSION_PATH_ATTRIBUTE), element.getAttributeValue(DissectionConstants.DISSECTING_PANE_NAME_ATTRIBUTE)));
            setNodeAnnotation(annotation);
        } else if (type == DissectionElementTypes.getKeepTogetherType()) {
            throw new RuntimeException("Not implemented");
        } else if (type == DissectionElementTypes.getShardLinkConditionalType()) {
            setNodeAnnotation((ShardLinkConditionalAttributes) element.getAnnotation());
        } else if (type == DissectionElementTypes.getShardLinkGroupType()) {
            setNodeAnnotation((ShardLinkGroupAttributes) element.getAnnotation());
        } else if (type == DissectionElementTypes.getShardLinkType()) {
            setNodeAnnotation((ShardLinkAttributes) element.getAnnotation());
        }
    }

    protected void visitChildren(DocumentVisitor visitor) throws DissectionException {
        try {
            element.forEachChild(new DissectableElementIteratee(visitor));
        } catch (ExtendedRuntimeException e) {
            throw new DissectionException(e);
        }
    }

    protected ElementType getElementType() {
        return (type != null) ? type : DissectionElementTypes.getPlainElementType();
    }

    public boolean isAtomic() {
        return atomic;
    }

    /**
     * This is only used during debugging
     * @return
     */
    public String toString() {
        return element.getName();
    }

    protected void accept(DocumentVisitor visitor) throws DissectionException {
        getElementType().invoke(visitor, this);
    }

    protected DOMDissectableNodeAbstract getNext() {
        return document.getDissectableNode(element.getNext());
    }

    protected Element getDOMElement() {
        return element;
    }

    protected Node getDOMHead() {
        return element.getHead();
    }

    protected String getDOMName() {
        return element.getName();
    }

    private final class DissectableElementIteratee implements NodeIteratee {

        DocumentVisitor visitor;

        public DissectableElementIteratee(DocumentVisitor visitor) {
            this.visitor = visitor;
        }

        public IterationAction next(Node node) {
            try {
                DOMDissectableNodeAbstract dnode = document.getDissectableNode(node);
                dnode.accept(visitor);
            } catch (DissectionException e) {
                throw new ExtendedRuntimeException(e);
            }
            return IterationAction.CONTINUE;
        }
    }
}
