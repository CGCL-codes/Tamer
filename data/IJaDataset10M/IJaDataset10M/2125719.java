package be.novelfaces.component.util.element;

import java.io.IOException;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import be.novelfaces.component.util.attribute.AttributesWriterBuilder;

public abstract class ElementWriter<U extends AttributesWriterBuilder> {

    public ElementWriter<U> startElement(UIComponent component) throws IOException {
        getResponseWriter().startElement(getElementName(), component);
        return this;
    }

    public void endElement() throws IOException {
        getResponseWriter().endElement(getElementName());
    }

    public abstract U getAttributesWriterBuilder(UIComponent component);

    protected abstract String getElementName();

    private ResponseWriter getResponseWriter() {
        return FacesContext.getCurrentInstance().getResponseWriter();
    }
}
