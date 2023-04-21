package org.datanucleus.jdo.metadata;

import javax.jdo.metadata.ArrayMetadata;
import org.datanucleus.metadata.ArrayMetaData;

/**
 * Implementation of JDO ArrayMetadata object.
 */
public class ArrayMetadataImpl extends AbstractMetadataImpl implements ArrayMetadata {

    public ArrayMetadataImpl(ArrayMetaData internal) {
        super(internal);
    }

    public ArrayMetaData getInternal() {
        return (ArrayMetaData) internalMD;
    }

    public Boolean getDependentElement() {
        return getInternal().isDependentElement();
    }

    public String getElementType() {
        return getInternal().getElementType();
    }

    public Boolean getEmbeddedElement() {
        return getInternal().isEmbeddedElement();
    }

    public Boolean getSerializedElement() {
        return getInternal().isSerializedElement();
    }

    public ArrayMetadata setDependentElement(boolean flag) {
        getInternal().setDependentElement(flag);
        return this;
    }

    public ArrayMetadata setElementType(String type) {
        getInternal().setElementType(type);
        return this;
    }

    public ArrayMetadata setEmbeddedElement(boolean flag) {
        getInternal().setEmbeddedElement(flag);
        return this;
    }

    public ArrayMetadata setSerializedElement(boolean flag) {
        getInternal().setSerializedElement(flag);
        return this;
    }
}
