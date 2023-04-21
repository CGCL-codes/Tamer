package org.garret.ptl.template.tags;

import org.garret.ptl.template.*;

/**
 * Blah blah
 *
 * @author Andrey Subbotin
 */
public class TextArea extends InputField {

    private final boolean doEscape;

    public TextArea(TemplateComponent parent, String id) {
        this(parent, id, true);
    }

    public TextArea(TemplateComponent parent, String id, boolean doEscape) {
        super(parent, id);
        this.doEscape = doEscape;
    }

    public void renderHtml(IPageContext context, ITagAttributes attributes) {
        attributes.setAttribute("name", id());
        if (disabled) attributes.setAttribute("disabled", "true");
        attributes.setValue((doEscape) ? PtlPage.escapeText(value()) : value());
    }
}
