package thinwire_wysiwyg.editors.properties;

import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.jface.viewers.ILabelProvider;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.views.properties.IPropertyDescriptor;

public class WrappedPropertyDescriptor implements IPropertyDescriptor, StringObjectConverter {

    StringObjectConverter converter;

    IPropertyDescriptor desc;

    public WrappedPropertyDescriptor(IPropertyDescriptor desc, StringObjectConverter converter) {
        this.desc = desc;
        this.converter = converter;
    }

    public CellEditor createPropertyEditor(Composite arg0) {
        return desc.createPropertyEditor(arg0);
    }

    public Object from(Object what) {
        return converter.from(what);
    }

    public String getCategory() {
        return desc.getCategory();
    }

    public String getDescription() {
        return desc.getDescription();
    }

    public String getDisplayName() {
        return desc.getDisplayName();
    }

    public String[] getFilterFlags() {
        return desc.getFilterFlags();
    }

    public Object getHelpContextIds() {
        return desc.getHelpContextIds();
    }

    public Object getId() {
        return desc.getId();
    }

    public ILabelProvider getLabelProvider() {
        return desc.getLabelProvider();
    }

    public boolean isCompatibleWith(IPropertyDescriptor arg0) {
        return desc.isCompatibleWith(arg0);
    }

    public Object to(Object what) {
        return converter.to(what);
    }
}
