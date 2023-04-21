package net.ar.guia.render.templates.html;

import net.ar.guia.helpers.*;

public class HtmlTemplateFactory {

    public static HtmlTemplate createTemplateFromResource(String aName, String aResourceName) {
        try {
            HtmlTemplate theTemplate = new HtmlTemplate(aName, GuiaHelper.getResourceAsStringBuffer(aResourceName));
            return theTemplate;
        } catch (GuiaException e) {
            throw new GuiaException("Cannot create template from the resource: " + aResourceName, e);
        }
    }

    public static KeyPositionHtmlTemplate createKeyPositionTemplateFromResource(String aName, String aResourceName) {
        return new KeyPositionHtmlTemplate(createTemplateFromResource(aName, aResourceName));
    }
}
