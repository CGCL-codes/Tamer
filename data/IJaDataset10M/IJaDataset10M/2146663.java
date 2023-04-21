package be.novelfaces.component.panel;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.faces.context.FacesContext;
import org.richfaces.cdk.annotations.JsfRenderer;
import be.novelfaces.component.AbstractRenderer;
import be.novelfaces.component.util.RenderUtilsFactory;
import be.novelfaces.component.util.element.ElementType;
import be.novelfaces.component.util.element.PanelWriter;

@JsfRenderer(family = "be.novelfaces.component", type = "be.novelfaces.component.PanelRenderer")
public class PanelRenderer extends AbstractRenderer<Panel> {

    private static final Logger log = Logger.getLogger(PanelRenderer.class.getName());

    @Override
    public void encodeBeginComponent(FacesContext facesContext, Panel panel) throws IOException {
        PanelWriter.create(getElementType(panel)).startElement(panel).getAttributesWriterBuilder(panel).withAllAttributesForPanel().writeAttributes();
    }

    @Override
    public void encodeEndComponent(FacesContext facesContext, Panel panel) throws IOException {
        PanelWriter.create(getElementType(panel)).endElement();
    }

    ElementType getElementType(Panel panel) {
        try {
            return ElementType.from(panel.getType());
        } catch (Exception e) {
            if (log.isLoggable(Level.WARNING)) {
                log.log(Level.WARNING, "Wrong attribute value for type: " + panel.getType() + ". Falling back to span element! UiComponent: " + RenderUtilsFactory.getInstance().getPathToComponent(panel), e);
            }
            return ElementType.SPAN;
        }
    }

    @Override
    protected void decodeComponent(FacesContext facesContext, Panel component) {
    }

    @Override
    protected Class<Panel> getComponentClass() {
        return Panel.class;
    }
}
