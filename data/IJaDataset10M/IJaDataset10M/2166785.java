package com.acarter.scenemonitor.propertydescriptor.definitions.renderstate;

import com.acarter.propertytable.Property;
import com.acarter.propertytable.PropertySection;
import com.acarter.propertytable.PropertySectionState;
import com.acarter.propertytable.propertyobject.BooleanPropertyObject;
import com.acarter.propertytable.propertyobject.EnumPropertyObject;
import com.acarter.propertytable.propertyobject.BooleanPropertyObject.I_BooleanPropertyObjectListener;
import com.acarter.propertytable.propertyobject.EnumPropertyObject.I_EnumPropertyObjectListener;
import com.acarter.scenemonitor.propertydescriptor.enums.E_CullMode;
import com.jme.scene.state.CullState;
import com.jme.scene.state.RenderState;

/**
 * @author Carter
 * 
 */
public class CullStatePropertyPage extends RenderStatePropertyPage {

    /**
     * 
     */
    public CullStatePropertyPage() {
        PropertySection section = new PropertySection("Cull State");
        section.addProperty(new Property("Cull Mode", new EnumPropertyObject<E_CullMode>()));
        section.addProperty(new Property("Flipped Culling", new BooleanPropertyObject()));
        section.setState(PropertySectionState.EXPANDED);
        model.addPropertySection(0, section);
    }

    /**
     * 
     * @param table
     * @param object
     */
    @SuppressWarnings("unchecked")
    @Override
    protected void updateListeners(final RenderState renderstate) {
        super.updateListeners(renderstate);
        if (!(renderstate instanceof CullState)) return;
        final CullState state = (CullState) renderstate;
        EnumPropertyObject<E_CullMode> face = (EnumPropertyObject<E_CullMode>) model.getPropertySection("Cull State").getProperty("Cull Mode").getPropertyObject();
        face.SetListener(new I_EnumPropertyObjectListener<E_CullMode>() {

            public E_CullMode readValue() {
                return E_CullMode.getConstant(state.getCullMode());
            }

            public void saveValue(E_CullMode value) {
                state.setCullMode(value.getType());
            }
        });
        BooleanPropertyObject wind = (BooleanPropertyObject) model.getPropertySection("Cull State").getProperty("Flipped Culling").getPropertyObject();
        wind.SetListener(new I_BooleanPropertyObjectListener() {

            public boolean readValue() {
                return CullState.isFlippedCulling();
            }

            public void saveValue(boolean value) {
                CullState.setFlippedCulling(value);
            }
        });
    }
}
