package org.nakedobjects.webapp.view.edit;

import org.nakedobjects.webapp.AbstractElementProcessor;
import org.nakedobjects.webapp.processor.Request;

public class FormEntry extends AbstractElementProcessor {

    public void process(Request request) {
        EditFieldBlock block = (EditFieldBlock) request.getBlockContent();
        String field = request.getRequiredProperty(FIELD);
        String value = request.getRequiredProperty(VALUE);
        boolean isHidden = request.isRequested(HIDDEN, true);
        block.exclude(field);
        String content = "refernce <input type=\"" + (isHidden ? "hidden" : "text") + "\" disabled=\"disabled\" name=\"" + field + "\" value=\"" + value + "\" />";
        block.replaceContent(field, content);
    }

    public String getName() {
        return "form-entry";
    }
}
