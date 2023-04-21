package de.mogwai.kias.web.ajax;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

public class AjaxStopModalFormCommand implements AjaxCommand {

    public Element serializeToElement(Document aDocument) {
        Element theElement = aDocument.createElement(COMMAND_TAG_NAME);
        theElement.setAttribute(COMMAND_TYPE_ATTRIBUTE_NAME, "AjaxStopModalFormCommand");
        return theElement;
    }
}
