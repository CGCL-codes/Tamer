package de.intarsys.pdf.cos;

import java.util.Map;
import de.intarsys.tools.collection.EmptyIterator;

/**
 * Primitive COS datatypes. These objects are "immutable" in their PDF
 * semantics. The container may change!
 * 
 */
public abstract class COSPrimitiveObject extends COSObject {

    protected COSPrimitiveObject() {
        super();
    }

    @Override
    public void addObjectListener(ICOSObjectListener listener) {
    }

    @Override
    public java.util.Iterator basicIterator() {
        return EmptyIterator.UNIQUE;
    }

    @Override
    public COSObject copyDeep() {
        return copyShallow();
    }

    @Override
    public COSObject copyDeep(Map copied) {
        return copyShallow();
    }

    @Override
    public boolean isDangling() {
        return false;
    }

    @Override
    public boolean isObjectListenerAvailable() {
        return false;
    }

    @Override
    public boolean isPrimitive() {
        return true;
    }

    @Override
    public java.util.Iterator iterator() {
        return EmptyIterator.UNIQUE;
    }

    @Override
    protected void registerWith(COSDocument doc) {
    }

    @Override
    public void removeObjectListener(ICOSObjectListener listener) {
    }

    @Override
    protected void triggerChanged(Object slot, Object oldValue, Object newValue) {
    }
}
