package com.sourceforge.oraclewicket.markup.html.template.panel;

import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;

/**
 * HTML page from which all panels in the applications should extend
 *
 * @author Andrew Hall
 *
 */
public abstract class StandardTemplatePanel extends Panel {

    private static final long serialVersionUID = 1L;

    /**
     * Construct.
     *
     * @param id
     *            component id
     */
    public StandardTemplatePanel(final String id) {
        super(id);
    }

    /**
     * Construct.
     *
     * @param id
     *            component id
     * @param model
     *            the model
     */
    public StandardTemplatePanel(final String id, final IModel<?> model) {
        super(id, model);
    }
}
