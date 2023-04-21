package com.es.manager.attributes;

import com.artemis.Entity;
import com.es.components.gamelogic.ChildManager;
import com.es.components.render.Scale;
import com.es.manager.ResourceManager;
import com.es.manager.attributes.values.BooleanValues;
import com.es.manager.attributes.values.FloatValues;
import com.es.manager.attributes.values.StringValues;
import com.es.state.GameWorld;
import org.w3c.dom.Element;
import java.util.ArrayList;

/**
 * User: snorre
 * Date: 14.02.12
 * Time: 00:10
 */
public class ChildAttribute extends EntityAttribute {

    public static final String TAG = "child";

    private StringValues id;

    private StringValues type;

    private FloatValues offsetX;

    private FloatValues offsetY;

    private FloatValues offsetRotation;

    private BooleanValues applyScale;

    private ArrayList<Float> scales;

    public ChildAttribute(ResourceManager resourceManager, Element element) {
        super(resourceManager, element);
    }

    @Override
    protected void parseElement() {
        id = getStringValue("value");
        type = getStringValue("type");
        offsetX = getFloatValue("x");
        offsetY = getFloatValue("y");
        offsetRotation = getFloatValue("rotation");
        applyScale = getBooleanValue("applyScale");
        scales = new ArrayList<Float>(id.getLength());
    }

    @Override
    protected String getTag() {
        return TAG;
    }

    @Override
    public void modifyEntity(GameWorld world, Entity entity, Entity parent) {
        if (!id.isEmpty()) {
            scales.clear();
            for (int i = 0; i < id.getLength(); i++) {
                Scale scale = (Scale) entity.getComponent(Scale.TYPE);
                if (applyScale.getValue(i) && scale != null) {
                    scales.add(scale.getValue());
                } else {
                    scales.add(1f);
                }
            }
            if (entity.isPooled()) {
                ChildManager manager = (ChildManager) entity.getComponent(ChildManager.TYPE);
                manager.init(getResourceManager(), world, entity, id.getValues(), type.getValues(), offsetX.getValues(), offsetY.getValues(), offsetRotation.getValues(), offsetX.getSpreads(), offsetY.getSpreads(), offsetRotation.getSpreads(), scales);
            } else {
                entity.addComponent(new ChildManager(getResourceManager(), world, entity, id.getValues(), type.getValues(), offsetX.getValues(), offsetY.getValues(), offsetRotation.getValues(), offsetX.getSpreads(), offsetY.getSpreads(), offsetRotation.getSpreads(), scales));
            }
        }
    }
}
