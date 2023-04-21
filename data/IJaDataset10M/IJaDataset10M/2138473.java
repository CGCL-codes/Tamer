package com.idedeluxe.bc.core.model;

import com.idedeluxe.bc.core.model.struc.Structure;

/**
 * An abstract base class for model elements in general.
 */
public abstract class AbstractElement implements Element {

    private Structure location;

    private Element parent;

    private int byteOffset;

    private int byteOffsetInScope;

    @Override
    public Structure getLocation() {
        return location;
    }

    @Override
    public String getDescription() {
        return getLocation().getDescription();
    }

    @Override
    public Element getParent() {
        return parent;
    }

    @Override
    public int getByteOffset() {
        return byteOffset;
    }

    @Override
    public int getByteOffsetInScope() {
        return byteOffsetInScope;
    }

    @Override
    public Element getRoot() {
        return getParent().getRoot();
    }

    @Override
    public Element lookupChild(Structure key) {
        Element element = getElementOfStructure(key);
        return element.getChild(key);
    }

    @Override
    public void setLocation(Structure location) {
        this.location = location;
    }

    @Override
    public void setParent(Element parent) {
        this.parent = parent;
    }

    @Override
    public void setByteOffset(int byteOffset) {
        this.byteOffset = byteOffset;
    }

    @Override
    public void setByteOffsetInScope(int byteOffsetInScope) {
        this.byteOffsetInScope = byteOffsetInScope;
    }

    private Element getElementOfStructure(Structure structure) {
        Element e = this;
        while (!e.hasStructure(structure)) {
            e = e.getParent();
            if (e == null) throw new StructureException("Could not find an element that owns structure.", structure);
        }
        return e;
    }
}
