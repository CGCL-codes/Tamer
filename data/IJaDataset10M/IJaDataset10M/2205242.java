package blue.orchestra.editor.blueSynthBuilder;

import java.beans.IntrospectionException;
import java.beans.PropertyDescriptor;
import java.beans.SimpleBeanInfo;

class BSBDropdownViewBeanInfo extends SimpleBeanInfo {

    public PropertyDescriptor[] getPropertyDescriptors() {
        try {
            PropertyDescriptor objName = new PropertyDescriptor("objectName", BSBDropdownView.class);
            PropertyDescriptor items = new PropertyDescriptor("dropdownView", BSBDropdownView.class);
            PropertyDescriptor automatable = new PropertyDescriptor("automationAllowed", BSBDropdownView.class);
            PropertyDescriptor randomizable = new PropertyDescriptor("randomizable", BSBDropdownView.class);
            items.setPropertyEditorClass(DropdownItemsPropertyEditor.class);
            return new PropertyDescriptor[] { objName, items, automatable, randomizable };
        } catch (IntrospectionException e) {
            e.printStackTrace();
        }
        return null;
    }
}
