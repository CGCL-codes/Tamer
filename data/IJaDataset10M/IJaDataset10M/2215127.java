package br.inf.ufrgs.renderxml4desktop.rendering;

import br.inf.ufrgs.renderxml4desktop.rendering.resource.ResourceManager;
import java.awt.Component;
import java.util.HashMap;
import java.util.Map;
import org.jdom.Element;
import br.inf.ufrgs.renderxml4desktop.exceptions.ParsingErrorException;
import br.inf.ufrgs.renderxml4desktop.exceptions.RenderingErrorException;

public abstract class UsiXMLInterfaceRenderer {

    public ResourceManager resourceRetriever = null;

    private Map<String, Object> renderedComponents = null;

    public void addRenderedComponent(String elementId, Object component) {
        this.renderedComponents.put(elementId, component);
    }

    public Object getRenderedComponent(String elementId) {
        return this.renderedComponents.get(elementId);
    }

    public abstract void renderInterface(Element cuiModel) throws RenderingErrorException, ParsingErrorException;

    public void createUserInterface(Element cuiModel, Element contextModel, Element resourceModel) throws RenderingErrorException, ParsingErrorException {
        this.renderedComponents = new HashMap<String, Object>();
        this.resourceRetriever = new ResourceManager(resourceModel);
        this.renderInterface(cuiModel);
    }
}
