package be.novelfaces.component.input;

import javax.faces.component.UIComponent;
import javax.faces.component.UIInput;
import javax.faces.component.UIOutput;
import javax.faces.context.FacesContext;
import javax.faces.convert.ConverterException;
import be.novelfaces.component.AbstractRenderer;
import be.novelfaces.component.util.RenderUtils;
import be.novelfaces.component.util.RenderUtilsFactory;

public abstract class AbstractInputRenderer<T extends UIInput> extends AbstractRenderer<T> {

    @Override
    public void decodeComponent(FacesContext facesContext, T component) {
        RenderUtils renderUtils = RenderUtilsFactory.getInstance();
        renderUtils.decodeUIInput(facesContext, component);
    }

    @Override
    public Object getConvertedValue(FacesContext facesContext, UIComponent component, Object val) throws ConverterException {
        RenderUtils renderUtils = RenderUtilsFactory.getInstance();
        renderUtils.checkParamValidity(facesContext, component, UIOutput.class);
        return renderUtils.getConvertedUIOutputValue(facesContext, (UIOutput) component, val);
    }
}
