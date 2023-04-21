package com.acarter.scenemonitor.propertydescriptor.definitions.renderstate;

import com.acarter.propertytable.Property;
import com.acarter.propertytable.PropertySection;
import com.acarter.propertytable.PropertySectionState;
import com.acarter.propertytable.propertyobject.BooleanPropertyObject;
import com.acarter.propertytable.propertyobject.EnumPropertyObject;
import com.acarter.propertytable.propertyobject.FloatPropertyObject;
import com.acarter.propertytable.propertyobject.BooleanPropertyObject.I_BooleanPropertyObjectListener;
import com.acarter.propertytable.propertyobject.EnumPropertyObject.I_EnumPropertyObjectListener;
import com.acarter.propertytable.propertyobject.FloatPropertyObject.I_FloatPropertyObjectListener;
import com.acarter.scenemonitor.propertydescriptor.propertyobject.ColorRGBAPropertyObject;
import com.acarter.scenemonitor.propertydescriptor.propertyobject.ColorRGBAPropertyObject.I_ColorRGBAPropertyObjectListener;
import com.jme.renderer.ColorRGBA;
import com.jme.scene.state.BlendState;
import com.jme.scene.state.RenderState;
import com.jme.scene.state.BlendState.BlendEquation;
import com.jme.scene.state.BlendState.DestinationFunction;
import com.jme.scene.state.BlendState.SourceFunction;
import com.jme.scene.state.BlendState.TestFunction;

/**
 * @author Carter
 *
 */
public class BlendStatePropertyPage extends RenderStatePropertyPage {

    /**
	 * 
	 */
    public BlendStatePropertyPage() {
        PropertySection section = new PropertySection("Blend State");
        section.addProperty(new Property("Test Enabled", new BooleanPropertyObject()));
        section.addProperty(new Property("Blend Enabled", new BooleanPropertyObject()));
        section.addProperty(new Property("Blend Equation Alpha", new EnumPropertyObject<BlendEquation>()));
        section.addProperty(new Property("Blend Equation RGB", new EnumPropertyObject<BlendEquation>()));
        section.addProperty(new Property("Dest Function Alpha", new EnumPropertyObject<DestinationFunction>()));
        section.addProperty(new Property("Dest Function RGB", new EnumPropertyObject<DestinationFunction>()));
        section.addProperty(new Property("Source Function Alpha", new EnumPropertyObject<SourceFunction>()));
        section.addProperty(new Property("Source Function RGB", new EnumPropertyObject<SourceFunction>()));
        section.addProperty(new Property("Constant Color", new ColorRGBAPropertyObject()));
        section.addProperty(new Property("Test Function", new EnumPropertyObject<TestFunction>()));
        section.addProperty(new Property("Reference", new FloatPropertyObject()));
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
        if (!(renderstate instanceof BlendState)) return;
        final BlendState state = (BlendState) renderstate;
        BooleanPropertyObject test = (BooleanPropertyObject) model.getPropertySection("Blend State").getProperty("Test Enabled").getPropertyObject();
        test.SetListener(new I_BooleanPropertyObjectListener() {

            public boolean readValue() {
                return state.isTestEnabled();
            }

            public void saveValue(boolean value) {
                state.setTestEnabled(value);
            }
        });
        BooleanPropertyObject blend = (BooleanPropertyObject) model.getPropertySection("Blend State").getProperty("Blend Enabled").getPropertyObject();
        blend.SetListener(new I_BooleanPropertyObjectListener() {

            public boolean readValue() {
                return state.isBlendEnabled();
            }

            public void saveValue(boolean value) {
                state.setBlendEnabled(value);
            }
        });
        EnumPropertyObject<BlendEquation> bea = (EnumPropertyObject<BlendEquation>) model.getPropertySection("Blend State").getProperty("Blend Equation Alpha").getPropertyObject();
        bea.SetListener(new I_EnumPropertyObjectListener<BlendEquation>() {

            public BlendEquation readValue() {
                return state.getBlendEquationAlpha();
            }

            public void saveValue(BlendEquation value) {
                state.setBlendEquationAlpha(value);
            }
        });
        EnumPropertyObject<BlendEquation> bergb = (EnumPropertyObject<BlendEquation>) model.getPropertySection("Blend State").getProperty("Blend Equation RGB").getPropertyObject();
        bergb.SetListener(new I_EnumPropertyObjectListener<BlendEquation>() {

            public BlendEquation readValue() {
                return state.getBlendEquationRGB();
            }

            public void saveValue(BlendEquation value) {
                state.setBlendEquationRGB(value);
            }
        });
        EnumPropertyObject<DestinationFunction> dfa = (EnumPropertyObject<DestinationFunction>) model.getPropertySection("Blend State").getProperty("Dest Function Alpha").getPropertyObject();
        dfa.SetListener(new I_EnumPropertyObjectListener<DestinationFunction>() {

            public DestinationFunction readValue() {
                return state.getDestinationFunctionAlpha();
            }

            public void saveValue(DestinationFunction value) {
                state.setDestinationFunctionAlpha(value);
            }
        });
        EnumPropertyObject<DestinationFunction> dfrgb = (EnumPropertyObject<DestinationFunction>) model.getPropertySection("Blend State").getProperty("Dest Function RGB").getPropertyObject();
        dfrgb.SetListener(new I_EnumPropertyObjectListener<DestinationFunction>() {

            public DestinationFunction readValue() {
                return state.getDestinationFunctionRGB();
            }

            public void saveValue(DestinationFunction value) {
                state.setDestinationFunctionRGB(value);
            }
        });
        EnumPropertyObject<SourceFunction> sfa = (EnumPropertyObject<SourceFunction>) model.getPropertySection("Blend State").getProperty("Source Function Alpha").getPropertyObject();
        sfa.SetListener(new I_EnumPropertyObjectListener<SourceFunction>() {

            public SourceFunction readValue() {
                return state.getSourceFunctionAlpha();
            }

            public void saveValue(SourceFunction value) {
                state.setSourceFunctionAlpha(value);
            }
        });
        EnumPropertyObject<SourceFunction> sfrgb = (EnumPropertyObject<SourceFunction>) model.getPropertySection("Blend State").getProperty("Source Function RGB").getPropertyObject();
        sfrgb.SetListener(new I_EnumPropertyObjectListener<SourceFunction>() {

            public SourceFunction readValue() {
                return state.getSourceFunctionRGB();
            }

            public void saveValue(SourceFunction value) {
                state.setSourceFunctionRGB(value);
            }
        });
        ColorRGBAPropertyObject color = (ColorRGBAPropertyObject) model.getPropertySection("Blend State").getProperty("Constant Color").getPropertyObject();
        color.SetListener(new I_ColorRGBAPropertyObjectListener() {

            public ColorRGBA readValue() {
                return state.getConstantColor();
            }

            public void saveValue(ColorRGBA value) {
                state.setConstantColor(value);
            }
        });
        EnumPropertyObject<TestFunction> testfunction = (EnumPropertyObject<TestFunction>) model.getPropertySection("Blend State").getProperty("Test Function").getPropertyObject();
        testfunction.SetListener(new I_EnumPropertyObjectListener<TestFunction>() {

            public TestFunction readValue() {
                return state.getTestFunction();
            }

            public void saveValue(TestFunction value) {
                state.setTestFunction(value);
            }
        });
        FloatPropertyObject reference = (FloatPropertyObject) model.getPropertySection("Blend State").getProperty("Reference").getPropertyObject();
        reference.SetListener(new I_FloatPropertyObjectListener() {

            public float readValue() {
                return state.getReference();
            }

            public void saveValue(float value) {
                state.setReference(value);
            }
        });
    }
}
