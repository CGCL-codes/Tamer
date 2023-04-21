package de.mogwai.kias.web.service;

import de.mogwai.kias.forms.FormLocator;
import de.mogwai.kias.forms.FormProcessor;
import de.mogwai.kias.resource.FormResourceLocator;
import de.mogwai.kias.web.serializer.WebFormSerializer;

/**
 * Context.
 * 
 * @author Mirko Sertic <mail@mirkosertic.de>
 */
public class WebServiceContext {

    private FormLocator formLocator;

    private FormResourceLocator resourceLocator;

    private FormProcessor formProcessor;

    private WebFormSerializer serializer;

    public FormLocator getFormLocator() {
        return formLocator;
    }

    public void setFormLocator(FormLocator formLocator) {
        this.formLocator = formLocator;
    }

    public FormProcessor getFormProcessor() {
        return formProcessor;
    }

    public void setFormProcessor(FormProcessor formProcessor) {
        this.formProcessor = formProcessor;
    }

    public FormResourceLocator getResourceLocator() {
        return resourceLocator;
    }

    public void setResourceLocator(FormResourceLocator resourceLocator) {
        this.resourceLocator = resourceLocator;
    }

    public WebFormSerializer getSerializer() {
        return serializer;
    }

    public void setSerializer(WebFormSerializer serializer) {
        this.serializer = serializer;
    }
}
