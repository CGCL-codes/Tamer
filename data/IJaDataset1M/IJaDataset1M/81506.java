package org.wicketrad.panel;

import org.apache.wicket.markup.html.form.Check;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;

/**
 * Simple panel that creates a Check with a provided Model for the check.
 *
 */
public class CheckPanel extends Panel {

    public CheckPanel(String id, IModel model) {
        super(id);
        this.add(new Check("check", model));
    }
}
