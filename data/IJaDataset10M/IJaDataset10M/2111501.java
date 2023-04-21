package net.sf.elbe.ui.valueproviders;

import net.sf.elbe.core.events.ModelModifier;
import net.sf.elbe.core.model.AttributeHierachie;
import net.sf.elbe.core.model.IConnection;
import net.sf.elbe.core.model.IEntry;
import net.sf.elbe.core.model.IValue;
import net.sf.elbe.core.model.ModelModificationException;
import net.sf.elbe.core.model.schema.Schema;
import net.sf.elbe.ui.ELBEUIConstants;
import net.sf.elbe.ui.ELBEUIPlugin;
import net.sf.elbe.ui.dialogs.AddressDialog;
import net.sf.elbe.ui.dialogs.TextDialog;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;

public class AddressValueProvider extends AbstractDialogCellEditor implements ValueProvider, ModelModifier {

    private TextValueProvider delegate;

    public AddressValueProvider(Composite parent) {
        super(parent);
        this.delegate = new TextValueProvider(parent);
    }

    public CellEditor getCellEditor() {
        return this;
    }

    public String getCellEditorName() {
        return "Address Editor";
    }

    public ImageDescriptor getCellEditorImageDescriptor() {
        return ELBEUIPlugin.getDefault().getImageDescriptor(ELBEUIConstants.IMG_ADDRESSEDITOR);
    }

    public Object openDialogBox(Control cellEditorWindow) {
        Object value = getValue();
        if (value != null && value instanceof String) {
            AddressDialog dialog = new AddressDialog(cellEditorWindow.getShell(), (String) value);
            if (dialog.open() == TextDialog.OK && !"".equals(dialog.getText())) {
                return dialog.getText();
            }
        }
        return null;
    }

    public String getDisplayValue(AttributeHierachie ah) {
        if (ah == null) {
            return "NULL";
        } else if (ah.size() == 1 && ah.getAttribute().getValueSize() == 1) {
            return getDisplayValue(ah.getAttribute().getValues()[0]);
        } else {
            return "not displayable";
        }
    }

    public String getDisplayValue(IValue value) {
        String displayValue = delegate.getDisplayValue(value);
        if (!ELBEUIPlugin.getDefault().getPreferenceStore().getBoolean(ELBEUIConstants.PREFERENCE_SHOW_RAW_VALUES)) {
            displayValue = displayValue.replaceAll("\\$", ", ");
        }
        return displayValue;
    }

    public void create(IEntry entry, String attributeDescription, Object newRawValue) throws ModelModificationException {
        delegate.create(entry, attributeDescription, newRawValue);
    }

    public void delete(AttributeHierachie ah) throws ModelModificationException {
        delegate.delete(ah);
    }

    public void delete(IValue oldValue) throws ModelModificationException {
        delegate.delete(oldValue);
    }

    public Object getEmptyRawValue(IEntry entry, String attributeDescription) {
        return delegate.getEmptyRawValue(entry, attributeDescription);
    }

    public Object getRawValue(AttributeHierachie ah) {
        return delegate.getRawValue(ah);
    }

    public Object getRawValue(IValue value) {
        return delegate.getRawValue(value);
    }

    public Object getRawValue(IConnection connection, Schema schema, Object value) {
        return delegate.getRawValue(connection, schema, value);
    }

    public void modify(IValue oldValue, Object newRawValue) throws ModelModificationException {
        delegate.modify(oldValue, newRawValue);
    }
}
