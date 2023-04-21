package de.objectcode.time4u.client.ui.dialogs;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Text;
import de.objectcode.time4u.server.api.data.MetaType;

public class MetaStringField extends MetaField {

    private Text m_text;

    public MetaStringField(final String category, final String property) {
        super(category, property);
    }

    @Override
    public Control createControl(final Composite parent) {
        m_text = new Text(parent, SWT.BORDER);
        m_text.setTextLimit(1000);
        return m_text;
    }

    @Override
    public MetaType getType() {
        return MetaType.STRING;
    }

    @Override
    public Object getValue() {
        return m_text.getText();
    }

    @Override
    public void setValue(final Object metaValue) {
        if (metaValue instanceof String) {
            m_text.setText((String) metaValue);
        }
    }
}
