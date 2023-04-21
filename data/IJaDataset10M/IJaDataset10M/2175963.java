package be.novelfaces.component.util.element;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import be.novelfaces.component.util.attribute.PanelAttributesWriterBuilder;

public class PanelWriter extends ElementWriter<PanelAttributesWriterBuilder> {

    private final ElementType element;

    public static PanelWriter create(ElementType element) {
        return new PanelWriter(element);
    }

    public static PanelWriter create() {
        return new PanelWriter(ElementType.SPAN);
    }

    private PanelWriter(final ElementType element) {
        if (element.isPanel()) {
            this.element = element;
        } else {
            this.element = ElementType.SPAN;
        }
    }

    @Override
    public PanelAttributesWriterBuilder getAttributesWriterBuilder(UIComponent component) {
        return new PanelAttributesWriterBuilder(getFacesContext(), component, this);
    }

    @Override
    protected String getElementName() {
        return element.getName();
    }

    private FacesContext getFacesContext() {
        return FacesContext.getCurrentInstance();
    }
}
