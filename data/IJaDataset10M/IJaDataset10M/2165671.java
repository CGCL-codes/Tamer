package repast.simphony.agents.designer.model.propertydescriptors;

import org.eclipse.jdt.ui.IJavaElementSearchConstants;
import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.jface.viewers.ILabelProvider;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.views.properties.PropertyDescriptor;
import repast.simphony.agents.designer.model.propertycelleditors.EditableComboBoxCellEditor;
import repast.simphony.agents.designer.model.propertycelleditors.EditableComboBoxLabelProvider;

/**
 * A property descriptor for looking up java types.
 * 
 * @author Peter Friese
 * @version 1.0
 * @since 09.07.2004
 */
public class TypeLookupPropertyDescriptor extends PropertyDescriptor {

    public static final String CHOOSE_USING_TYPE_SELECTOR = "Choose Using Type Selector...";

    public static final String[] DEFAULT_SELECTION_LIST = { TypeLookupPropertyDescriptor.CHOOSE_USING_TYPE_SELECTOR };

    private boolean multipleSelect = false;

    public String value = "";

    public int typesToConsider = IJavaElementSearchConstants.CONSIDER_ALL_TYPES;

    /**
	 * @param id
	 * @param displayName
	 */
    public TypeLookupPropertyDescriptor(Object id, String displayName, boolean multipleSelect, int newTypesToConsider, String newValue) {
        super(id, displayName);
        this.multipleSelect = multipleSelect;
        this.typesToConsider = newTypesToConsider;
        this.value = newValue;
    }

    @Override
    public CellEditor createPropertyEditor(Composite parent) {
        EditableComboBoxCellEditor editor = new EditableComboBoxCellEditor(parent, TypeLookupPropertyDescriptor.DEFAULT_SELECTION_LIST, SWT.DROP_DOWN, this.multipleSelect, null);
        editor.doSetValue(value);
        return editor;
    }

    public ILabelProvider getLabelProvider() {
        if (this.isLabelProviderSet()) {
            return super.getLabelProvider();
        } else {
            return new EditableComboBoxLabelProvider();
        }
    }

    public void doSetValue(Object newText) {
        if (newText instanceof String) {
            this.value = (String) newText;
        }
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
