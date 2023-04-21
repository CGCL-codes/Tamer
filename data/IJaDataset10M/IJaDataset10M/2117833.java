package net.sf.lightbound.opencms.taghandlers;

import org.dom4j.Element;
import net.sf.lightbound.controller.RenderObjects;
import net.sf.lightbound.controller.TranslationHelpers;
import net.sf.lightbound.controller.forms.FormElementProcessingData;
import net.sf.lightbound.controller.html.InputTagHandler;
import net.sf.lightbound.opencms.content.OpenCmsLinkTranslator;
import net.sf.lightbound.opencms.content.OpenCmsRequest;

public class OpenCmsInputTagHandler extends InputTagHandler {

    protected static final String ATTR_SRC = "src";

    private final OpenCmsLinkTranslator linkTranslator;

    public OpenCmsInputTagHandler(OpenCmsLinkTranslator linkTranslator) {
        this.linkTranslator = linkTranslator;
    }

    @Override
    public boolean handleAttributes(Element result, RenderObjects renderObjects, TranslationHelpers helpers, FormElementProcessingData processingData) {
        boolean handled = super.handleAttributes(result, renderObjects, helpers, processingData);
        System.out.println("Element: " + result.getName());
        String src = result.attributeValue(ATTR_SRC);
        if (src != null) {
            System.out.println("SRC first: " + src);
            src = linkTranslator.translateURL(src, (OpenCmsRequest) helpers.getRequest());
            System.out.println("SRC then: " + src);
            result.addAttribute(ATTR_SRC, src);
            handled = true;
        }
        return handled;
    }
}
