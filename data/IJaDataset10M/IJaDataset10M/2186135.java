package net.sf.elbe.ui.valueproviders;

import net.sf.elbe.core.events.EventRegistry;
import net.sf.elbe.core.events.ModelModifier;
import net.sf.elbe.core.internal.model.Attribute;
import net.sf.elbe.core.jobs.CreateValuesJob;
import net.sf.elbe.core.jobs.DeleteAttributesValueJob;
import net.sf.elbe.core.jobs.ModifyValueJob;
import net.sf.elbe.core.model.IAttribute;
import net.sf.elbe.core.model.IConnection;
import net.sf.elbe.core.model.IEntry;
import net.sf.elbe.core.model.IValue;
import net.sf.elbe.core.model.ModelModificationException;
import net.sf.elbe.core.model.AttributeHierachie;
import net.sf.elbe.core.model.schema.Schema;
import net.sf.elbe.core.utils.LdifUtils;
import net.sf.elbe.ui.ELBEUIConstants;
import net.sf.elbe.ui.ELBEUIPlugin;
import net.sf.elbe.ui.dialogs.TextDialog;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;

public class TextValueProvider extends AbstractDialogCellEditor implements ValueProvider, ModelModifier {

    public TextValueProvider(Composite parent) {
        super(parent);
    }

    public CellEditor getCellEditor() {
        return this;
    }

    public String getCellEditorName() {
        return "Text Editor";
    }

    public ImageDescriptor getCellEditorImageDescriptor() {
        return ELBEUIPlugin.getDefault().getImageDescriptor(ELBEUIConstants.IMG_TEXTEDITOR);
    }

    public Object openDialogBox(Control cellEditorWindow) {
        Object value = getValue();
        if (value != null && value instanceof String) {
            TextDialog dialog = new TextDialog(cellEditorWindow.getShell(), (String) value);
            if (dialog.open() == TextDialog.OK && !"".equals(dialog.getText())) {
                return dialog.getText();
            }
        }
        return null;
    }

    public String getDisplayValue(AttributeHierachie ah) {
        Object obj = this.getRawValue(ah);
        return obj == null ? "NULL" : obj.toString();
    }

    public String getDisplayValue(IValue value) {
        Object obj = this.getRawValue(value);
        return obj == null ? "NULL" : obj.toString();
    }

    public Object getEmptyRawValue(IEntry entry, String attributeDescription) {
        if (entry == null || attributeDescription == null) {
            return null;
        }
        if (entry.getConnection().getSchema().getAttributeTypeDescription(attributeDescription).getSyntaxDescription().isString()) {
            return IValue.EMPTY_STRING_VALUE;
        } else {
            return IValue.EMPTY_BINARY_VALUE;
        }
    }

    public Object getRawValue(AttributeHierachie ah) {
        if (ah == null) {
            return null;
        } else if (ah.size() == 1 && ah.getAttribute().getValueSize() == 0) {
            return getEmptyRawValue(ah.getAttribute().getEntry(), ah.getAttribute().getDescription());
        } else if (ah.size() == 1 && ah.getAttribute().getValueSize() == 1) {
            return getRawValue(ah.getAttribute().getValues()[0]);
        } else {
            return null;
        }
    }

    public Object getRawValue(IValue value) {
        if (value == null) {
            return null;
        } else if (value.isString()) {
            return value.getStringValue();
        } else if (value.isBinary()) {
            return isEditable(value.getBinaryValue()) ? value.getStringValue() : null;
        } else {
            return null;
        }
    }

    public Object getRawValue(IConnection connection, Schema schema, Object value) {
        if (value == null) {
            return null;
        } else if (value instanceof String) {
            return value;
        } else if (value instanceof byte[]) {
            String s = LdifUtils.utf8decode((byte[]) value);
            for (int i = 0; i < s.length(); i++) {
                if (Character.isISOControl(s.charAt(i)) && s.charAt(i) != '\n' && s.charAt(i) != '\r') {
                    return null;
                }
            }
            return s;
        } else {
            return null;
        }
    }

    private boolean isEditable(byte[] b) {
        if (b == null) {
            return false;
        }
        for (int i = 0; i < b.length; i++) {
            if (!(b[i] == '\n' || b[i] == '\r' || (b[i] >= ' ' && b[i] <= ''))) {
                return false;
            }
        }
        return true;
    }

    public void create(IEntry entry, String attributeDescription, Object newRawValue) throws ModelModificationException {
        if (entry != null && attributeDescription != null && newRawValue != null && newRawValue instanceof String) {
            if (entry.getAttribute(attributeDescription) != null) {
                this.modify(entry.getAttribute(attributeDescription), newRawValue);
            } else {
                EventRegistry.suspendEventFireingInCurrentThread();
                IAttribute attribute = new Attribute(entry, attributeDescription);
                entry.addAttribute(attribute, this);
                EventRegistry.resumeEventFireingInCurrentThread();
                Object newValue;
                if (entry.getConnection().getSchema().getAttributeTypeDescription(attributeDescription).getSyntaxDescription().isString()) {
                    newValue = (String) newRawValue;
                } else {
                    newValue = LdifUtils.utf8encode((String) newRawValue);
                }
                new CreateValuesJob(attribute, newValue).execute();
            }
        }
    }

    private void modify(IAttribute attribute, Object newRawValue) throws ModelModificationException {
        if (attribute != null && newRawValue != null && newRawValue instanceof String) {
            if (attribute.getValueSize() == 0) {
                String newValue = (String) newRawValue;
                new CreateValuesJob(attribute, newValue).execute();
            } else if (attribute.getValueSize() == 1) {
                this.modify(attribute.getValues()[0], newRawValue);
            }
        }
    }

    public void modify(IValue oldValue, Object newRawValue) throws ModelModificationException {
        if (oldValue != null && newRawValue != null && newRawValue instanceof String) {
            String newValue = (String) newRawValue;
            IAttribute attribute = oldValue.getAttribute();
            if (!oldValue.getStringValue().equals(newValue)) {
                if (oldValue.isEmpty()) {
                    EventRegistry.suspendEventFireingInCurrentThread();
                    attribute.deleteEmptyValue(this);
                    EventRegistry.resumeEventFireingInCurrentThread();
                    new CreateValuesJob(attribute, newValue).execute();
                } else {
                    new ModifyValueJob(attribute, oldValue, newValue).execute();
                }
            }
        }
    }

    public void delete(AttributeHierachie ah) throws ModelModificationException {
        if (ah != null) {
            new DeleteAttributesValueJob(ah).execute();
        }
    }

    public void delete(IValue oldValue) throws ModelModificationException {
        if (oldValue != null) {
            new DeleteAttributesValueJob(oldValue).execute();
        }
    }
}
