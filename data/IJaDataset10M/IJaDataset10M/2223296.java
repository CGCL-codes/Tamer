package org.apache.isis.extensions.wicket.ui.components.scalars.string;

import org.apache.isis.extensions.wicket.model.models.ScalarModel;
import org.apache.isis.extensions.wicket.ui.components.scalars.ScalarPanelTextFieldParseableAbstract;

/**
 * Panel for rendering scalars of type {@link String}.
 */
public class StringPanel extends ScalarPanelTextFieldParseableAbstract {

    private static final long serialVersionUID = 1L;

    private static final String ID_SCALAR_VALUE = "scalarValue";

    public StringPanel(String id, final ScalarModel scalarModel) {
        super(id, ID_SCALAR_VALUE, scalarModel);
    }

    @Override
    protected void addSemantics() {
        super.addSemantics();
    }
}
