package com.es.manager.attributes;

import com.artemis.Entity;
import com.es.manager.ResourceManager;
import com.es.manager.attributes.values.StringValues;
import com.es.state.GameWorld;
import org.w3c.dom.Element;

/**
 * User: snorre
 * Date: 14.02.12
 * Time: 00:10
 */
public class NameAttribute extends EntityAttribute {

    public static final String TAG = "name";

    private StringValues value;

    public NameAttribute(ResourceManager resourceManager, Element element) {
        super(resourceManager, element);
    }

    @Override
    protected void parseElement() {
        value = getStringValue("value");
    }

    @Override
    protected String getTag() {
        return TAG;
    }

    @Override
    public void modifyEntity(GameWorld world, Entity entity, Entity parent) {
    }

    public String getValue() {
        return value.isEmpty() ? "" : value.getValue();
    }
}
