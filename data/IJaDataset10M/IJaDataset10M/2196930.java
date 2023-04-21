package org.jgenesis.swing.models;

import java.beans.*;
import org.jgenesis.swing.editors.BeanManagerEditor;

/**
 * @author root
 */
public class BeanLookupComboBoxModelBeanInfo extends SimpleBeanInfo {

    private static BeanDescriptor getBdescriptor() {
        BeanDescriptor beanDescriptor = new BeanDescriptor(BeanLookupComboBoxModel.class, null);
        return beanDescriptor;
    }

    private static final int PROPERTY_allowNull = 0;

    private static final int PROPERTY_defaultIndex = 1;

    private static final int PROPERTY_defaultItem = 2;

    private static final int PROPERTY_delimiter = 3;

    private static final int PROPERTY_fieldNameData = 4;

    private static final int PROPERTY_fieldNamesDisplay = 5;

    private static final int PROPERTY_selectedItemIndex = 6;

    private static final int PROPERTY_size = 7;

    private static final int PROPERTY_sourceBeanManager = 8;

    private static final int PROPERTY_targetBeanManager = 9;

    private static final int PROPERTY_targetFieldName = 10;

    private static PropertyDescriptor[] getPdescriptor() {
        PropertyDescriptor[] properties = new PropertyDescriptor[11];
        try {
            properties[PROPERTY_allowNull] = new PropertyDescriptor("allowNull", BeanLookupComboBoxModel.class, "isAllowNull", "setAllowNull");
            properties[PROPERTY_defaultIndex] = new PropertyDescriptor("defaultIndex", BeanLookupComboBoxModel.class, "getDefaultIndex", "setDefaultIndex");
            properties[PROPERTY_defaultItem] = new PropertyDescriptor("defaultItem", BeanLookupComboBoxModel.class, "getDefaultItem", "setDefaultItem");
            properties[PROPERTY_defaultItem].setExpert(true);
            properties[PROPERTY_delimiter] = new PropertyDescriptor("delimiter", BeanLookupComboBoxModel.class, "getDelimiter", "setDelimiter");
            properties[PROPERTY_fieldNameData] = new PropertyDescriptor("fieldNameData", BeanLookupComboBoxModel.class, "getFieldNameData", "setFieldNameData");
            properties[PROPERTY_fieldNamesDisplay] = new PropertyDescriptor("fieldNamesDisplay", BeanLookupComboBoxModel.class, "getFieldNamesDisplay", "setFieldNamesDisplay");
            properties[PROPERTY_selectedItemIndex] = new PropertyDescriptor("selectedItemIndex", BeanLookupComboBoxModel.class, "getSelectedItemIndex", null);
            properties[PROPERTY_size] = new PropertyDescriptor("size", BeanLookupComboBoxModel.class, "getSize", null);
            properties[PROPERTY_size].setHidden(true);
            properties[PROPERTY_sourceBeanManager] = new PropertyDescriptor("sourceBeanManager", BeanLookupComboBoxModel.class, "getSourceBeanManager", "setSourceBeanManager");
            properties[PROPERTY_sourceBeanManager].setPropertyEditorClass(BeanManagerEditor.class);
            properties[PROPERTY_targetBeanManager] = new PropertyDescriptor("targetBeanManager", BeanLookupComboBoxModel.class, "getTargetBeanManager", "setTargetBeanManager");
            properties[PROPERTY_targetBeanManager].setPropertyEditorClass(BeanManagerEditor.class);
            properties[PROPERTY_targetFieldName] = new PropertyDescriptor("targetFieldName", BeanLookupComboBoxModel.class, "getTargetFieldName", "setTargetFieldName");
        } catch (IntrospectionException e) {
        }
        return properties;
    }

    private static final int EVENT_listDataListener = 0;

    private static EventSetDescriptor[] getEdescriptor() {
        EventSetDescriptor[] eventSets = new EventSetDescriptor[1];
        try {
            eventSets[EVENT_listDataListener] = new EventSetDescriptor(org.jgenesis.swing.models.BeanLookupComboBoxModel.class, "listDataListener", javax.swing.event.ListDataListener.class, new String[] { "contentsChanged", "intervalAdded", "intervalRemoved" }, "addListDataListener", "removeListDataListener");
        } catch (IntrospectionException e) {
        }
        return eventSets;
    }

    private static final int METHOD_addElement0 = 0;

    private static final int METHOD_getListeners1 = 1;

    private static final int METHOD_getSelectedItem2 = 2;

    private static final int METHOD_insertElementAt3 = 3;

    private static final int METHOD_readExternal4 = 4;

    private static final int METHOD_removeElement5 = 5;

    private static final int METHOD_removeElementAt6 = 6;

    private static final int METHOD_setSelectedItem7 = 7;

    private static final int METHOD_updateElementAt8 = 8;

    private static final int METHOD_writeExternal9 = 9;

    private static MethodDescriptor[] getMdescriptor() {
        MethodDescriptor[] methods = new MethodDescriptor[10];
        try {
            methods[METHOD_addElement0] = new MethodDescriptor(org.jgenesis.swing.models.BeanLookupComboBoxModel.class.getMethod("addElement", new Class[] { java.lang.Object.class }));
            methods[METHOD_addElement0].setDisplayName("");
            methods[METHOD_getListeners1] = new MethodDescriptor(org.jgenesis.swing.models.BeanLookupComboBoxModel.class.getMethod("getListeners", new Class[] { java.lang.Class.class }));
            methods[METHOD_getListeners1].setDisplayName("");
            methods[METHOD_getSelectedItem2] = new MethodDescriptor(org.jgenesis.swing.models.BeanLookupComboBoxModel.class.getMethod("getSelectedItem", new Class[] {}));
            methods[METHOD_getSelectedItem2].setDisplayName("");
            methods[METHOD_insertElementAt3] = new MethodDescriptor(org.jgenesis.swing.models.BeanLookupComboBoxModel.class.getMethod("insertElementAt", new Class[] { java.lang.Object.class, Integer.TYPE }));
            methods[METHOD_insertElementAt3].setDisplayName("");
            methods[METHOD_readExternal4] = new MethodDescriptor(org.jgenesis.swing.models.BeanLookupComboBoxModel.class.getMethod("readExternal", new Class[] { java.io.ObjectInput.class }));
            methods[METHOD_readExternal4].setDisplayName("");
            methods[METHOD_removeElement5] = new MethodDescriptor(org.jgenesis.swing.models.BeanLookupComboBoxModel.class.getMethod("removeElement", new Class[] { java.lang.Object.class }));
            methods[METHOD_removeElement5].setDisplayName("");
            methods[METHOD_removeElementAt6] = new MethodDescriptor(org.jgenesis.swing.models.BeanLookupComboBoxModel.class.getMethod("removeElementAt", new Class[] { Integer.TYPE }));
            methods[METHOD_removeElementAt6].setDisplayName("");
            methods[METHOD_setSelectedItem7] = new MethodDescriptor(org.jgenesis.swing.models.BeanLookupComboBoxModel.class.getMethod("setSelectedItem", new Class[] { Integer.TYPE }));
            methods[METHOD_setSelectedItem7].setDisplayName("");
            methods[METHOD_updateElementAt8] = new MethodDescriptor(org.jgenesis.swing.models.BeanLookupComboBoxModel.class.getMethod("updateElementAt", new Class[] { java.lang.Object.class, Integer.TYPE }));
            methods[METHOD_updateElementAt8].setDisplayName("");
            methods[METHOD_writeExternal9] = new MethodDescriptor(org.jgenesis.swing.models.BeanLookupComboBoxModel.class.getMethod("writeExternal", new Class[] { java.io.ObjectOutput.class }));
            methods[METHOD_writeExternal9].setDisplayName("");
        } catch (Exception e) {
        }
        return methods;
    }

    private static final int defaultPropertyIndex = -1;

    private static final int defaultEventIndex = -1;

    /**
     * Gets the bean's <code>BeanDescriptor</code>s.
     *
     * @return BeanDescriptor describing the editable
     * properties of this bean.  May return null if the
     * information should be obtained by automatic analysis.
     */
    public BeanDescriptor getBeanDescriptor() {
        return getBdescriptor();
    }

    /**
     * Gets the bean's <code>PropertyDescriptor</code>s.
     *
     * @return An array of PropertyDescriptors describing the editable
     * properties supported by this bean.  May return null if the
     * information should be obtained by automatic analysis.
     * <p>
     * If a property is indexed, then its entry in the result array will
     * belong to the IndexedPropertyDescriptor subclass of PropertyDescriptor.
     * A client of getPropertyDescriptors can use "instanceof" to check
     * if a given PropertyDescriptor is an IndexedPropertyDescriptor.
     */
    public PropertyDescriptor[] getPropertyDescriptors() {
        return getPdescriptor();
    }

    /**
     * Gets the bean's <code>EventSetDescriptor</code>s.
     *
     * @return  An array of EventSetDescriptors describing the kinds of
     * events fired by this bean.  May return null if the information
     * should be obtained by automatic analysis.
     */
    public EventSetDescriptor[] getEventSetDescriptors() {
        return getEdescriptor();
    }

    /**
     * Gets the bean's <code>MethodDescriptor</code>s.
     *
     * @return  An array of MethodDescriptors describing the methods
     * implemented by this bean.  May return null if the information
     * should be obtained by automatic analysis.
     */
    public MethodDescriptor[] getMethodDescriptors() {
        return getMdescriptor();
    }

    /**
     * A bean may have a "default" property that is the property that will
     * mostly commonly be initially chosen for update by human's who are
     * customizing the bean.
     * @return  Index of default property in the PropertyDescriptor array
     * 		returned by getPropertyDescriptors.
     * <P>	Returns -1 if there is no default property.
     */
    public int getDefaultPropertyIndex() {
        return defaultPropertyIndex;
    }

    /**
     * A bean may have a "default" event that is the event that will
     * mostly commonly be used by human's when using the bean.
     * @return Index of default event in the EventSetDescriptor array
     *		returned by getEventSetDescriptors.
     * <P>	Returns -1 if there is no default event.
     */
    public int getDefaultEventIndex() {
        return defaultEventIndex;
    }
}
