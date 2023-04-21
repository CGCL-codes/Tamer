package org.argetr.resim.property;

import java.lang.reflect.InvocationTargetException;
import org.argetr.resim.ui.parts.CellStringValidator;
import org.eclipse.jface.viewers.ICellEditorValidator;
import org.eclipse.ui.views.properties.IPropertyDescriptor;
import org.eclipse.ui.views.properties.TextPropertyDescriptor;

public class StringPRP extends Property {

    public StringPRP(Object owner, String propertyId, String displayName, String getterMethod, String setterMethod) {
        super(owner, propertyId, displayName, getterMethod, setterMethod);
        try {
            _setterMethod = _owner.getClass().getMethod(setterMethod, String.class);
        } catch (SecurityException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
    }

    @Override
    public Object getValue() {
        try {
            return (String) _getterMethod.invoke(_owner);
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public void setValue(Object value) {
        try {
            _setterMethod.invoke(_owner, new String(value.toString()));
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
    }

    @Override
    public IPropertyDescriptor getDescriptor() {
        ICellEditorValidator lValidator = new CellStringValidator();
        TextPropertyDescriptor lPropDesc = new TextPropertyDescriptor(_id, _displayName);
        lPropDesc.setValidator(lValidator);
        return lPropDesc;
    }
}
