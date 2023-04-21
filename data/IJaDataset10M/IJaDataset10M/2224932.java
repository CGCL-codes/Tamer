package net.sf.elbe.ui.editors.ldif.actions;

import net.sf.elbe.core.model.schema.Schema;
import net.sf.elbe.ui.editors.ldif.LdifEditor;
import net.sf.elbe.ui.valueproviders.AbstractDialogCellEditor;
import net.sf.elbe.ui.valueproviders.ValueProvider;

public class OpenBestValueEditorAction extends AbstractOpenValueEditorAction {

    public OpenBestValueEditorAction(LdifEditor editor) {
        super(editor);
    }

    public void update() {
        super.setEnabled(isEditableLineSelected());
        Schema schema = getSchema();
        String attributeDescription = getAttributeDescription();
        Object oldValue = getValue();
        if (attributeDescription != null) {
            valueProvider = manager.getCurrentValueProvider(schema, attributeDescription);
            Object rawValue = valueProvider.getRawValue(null, schema, oldValue);
            if (!(valueProvider instanceof AbstractDialogCellEditor) || rawValue == null) {
                ValueProvider[] vps = manager.getAlternativeValueProvider(schema, attributeDescription);
                for (int i = 0; i < vps.length && (!(valueProvider instanceof AbstractDialogCellEditor) || rawValue == null); i++) {
                    valueProvider = vps[i];
                    rawValue = valueProvider.getRawValue(null, schema, oldValue);
                }
            }
        }
        if (valueProvider != null) {
            setText(valueProvider.getCellEditorName());
            setImageDescriptor(valueProvider.getCellEditorImageDescriptor());
        }
    }
}
