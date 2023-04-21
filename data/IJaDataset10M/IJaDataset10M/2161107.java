package org.webfort.components;

import org.ujoframework.Ujo;
import org.ujoframework.UjoProperty;
import org.ujoframework.core.UjoManager;
import org.ujoframework.implementation.factory.FactoryProperty;

/**
 * Input Tag
 * @author pavel
 */
public class TagInput extends Tag {

    public static final UjoProperty<TagInput, String> attrType = newProperty("type", String.class);

    public static final UjoProperty<TagInput, Object> attrValue = newProperty("value", Object.class);

    public static final UjoProperty<TagInput, String> attrName = newProperty("name", String.class);

    public TagInput(Ujo factory, UjoProperty property) {
        super(factory, new FactoryProperty("input", property.getType()));
        set(attrType, property.getName());
    }

    /** Getter based on one UjoProperty */
    @SuppressWarnings("unchecked")
    @Override
    public <UJO extends Tag, VALUE> VALUE get(UjoProperty<UJO, VALUE> property) {
        return (VALUE) UjoManager.getValue(this, property);
    }

    public String getType() {
        return get(attrType);
    }
}
