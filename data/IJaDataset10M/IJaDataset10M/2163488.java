package org.yacmmf.adaptor.map;

import org.yacmmf.core.AttributeListener;
import org.yacmmf.core.AttributeMetaDataKey;
import org.yacmmf.core.AttributeVisitor;
import org.yacmmf.core.Component;
import org.yacmmf.core.MetaComponent;
import org.yacmmf.core.ReferenceAttribute;

public class MapComponentAttribute implements ReferenceAttribute {

    @Override
    public void add(AttributeListener attributeListener) {
    }

    public Component get(Component component) {
        return null;
    }

    public MetaComponent getMetaComponent() {
        return null;
    }

    public <M> M getMetaData(AttributeMetaDataKey<M> key) {
        return null;
    }

    public String getName() {
        return null;
    }

    @Override
    public boolean isGettable() {
        return false;
    }

    public boolean isSettable() {
        return false;
    }

    public void set(Component component, Component value) {
    }

    public void visit(AttributeVisitor visitor) {
    }
}
