package org.bm.firestarter.propertysheet;

import java.awt.Component;
import java.beans.BeanInfo;
import java.beans.Introspector;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.beans.PropertyEditor;
import java.beans.PropertyEditorSupport;
import java.lang.reflect.Method;
import javax.swing.JComponent;
import org.bm.firestarter.propertysheet.editor.EditorRegistration;
import org.bm.firestarter.propertysheet.editor.MPropertyEditorSupport;

/**
 * <p>
 *   <code>BeanEditorSupport</code> works as a liason between an underlying bean object
 *   and the bean's component editors. Using with a different bean requires a new instance. Once
 * </p>
 * <p>
 *   Contains a filtered list model, which allows for <code>ListDataEvent</code>s to be fired.
 *   This is typically done when the filter properties change.
 * </p>
 *
 * @author Elisha Peterson
 */
public class BeanEditorSupport extends FilteredPropertyList implements PropertyChangeListener {

    /** Object of this class. */
    protected Object bean;

    /** The info of the bean. */
    protected BeanInfo info;

    /** List of property editors. */
    protected PropertyEditor[] editors;

    /** List of component editors. */
    protected Component[] components;

    public BeanEditorSupport() {
    }

    /**
     * Constructs for specified bean.
     * @param bean the underlying object.
     */
    public BeanEditorSupport(Object bean) {
        if (bean == null) {
            throw new IllegalArgumentException("BeanEditorSupport cannot be constructed with a null object!");
        }
        this.bean = bean;
        info = getBeanInfo(bean.getClass());
        items = info.getPropertyDescriptors();
        refilter();
    }

    /** Retrieves the BeanInfo for a Class */
    public static BeanInfo getBeanInfo(Class cls) {
        BeanInfo beanInfo = null;
        try {
            beanInfo = Introspector.getBeanInfo(cls);
        } catch (Exception ex) {
            System.out.println("Error in bean introspection for class " + cls);
            ex.printStackTrace();
        }
        return beanInfo;
    }

    /**
     * Constructs the visual components that are used for editing. This should be
     * called immediately after any refilter command.
     */
    public void initEditors() {
        editors = new PropertyEditor[getSize()];
        components = new Component[getSize()];
        for (int i = 0; i < getSize(); i++) {
            editors[i] = EditorRegistration.getEditor(bean, getElementAt(i));
            editors[i].addPropertyChangeListener(this);
            if (editors[i].supportsCustomEditor()) {
                components[i] = editors[i].getCustomEditor();
            } else {
                components[i] = new DefaultPropertyComponent(bean, getElementAt(i));
            }
            if (getElementAt(i).getWriteMethod() == null || getElementAt(i).getReadMethod() == null) {
                components[i].setEnabled(false);
            }
            try {
                ((JComponent) components[i]).setBorder(null);
            } catch (Exception e) {
                System.out.println("Editor is not a JComponent!");
            }
        }
    }

    /** @return the underlying object. */
    public Object getBean() {
        return bean;
    }

    /**
     * Returns the Java type info for the property at the given row.
     */
    public Class getPropertyType(int i) {
        return getElementAt(i).getPropertyType();
    }

    /**
     * Returns value at given position.
     */
    public Object getValue(int pos) {
        Object value = null;
        Method getter = getElementAt(pos).getReadMethod();
        if (getter != null) {
            Class[] paramTypes = getter.getParameterTypes();
            Object[] args = new Object[paramTypes.length];
            try {
                value = getter.invoke(bean);
            } catch (Exception e) {
                System.out.println("Error in method invocation");
                System.out.println("  bean: " + bean.toString());
                System.out.println("  getter: " + getter.getName());
                System.out.println("  getter args: ");
                for (int i = 0; i < args.length; i++) System.out.println("\t" + "type: " + paramTypes[i] + " value: " + args[i]);
                e.printStackTrace();
            }
        }
        return value;
    }

    /** Sets property in given row. */
    public void setValue(int pos, Object value) {
        Method setter = getElementAt(pos).getWriteMethod();
        if (setter != null) {
            try {
                if (value.getClass().isArray()) {
                    System.out.print("setting underlying value [");
                    Object[] array = (Object[]) value;
                    for (int i = 0; i < array.length; i++) {
                        System.out.print((i == 0 ? "" : ", ") + array[i].toString());
                    }
                    System.out.println("]");
                } else {
                }
                setter.invoke(bean, value);
            } catch (Exception ex) {
                System.err.println("Unable to invoke Setter: " + setter);
            }
        }
    }

    /** @return editor component at specified position. */
    public Component getComponent(int pos) {
        return components[pos];
    }

    /** Individual editors will fire property changes that are handled by this class. */
    public void propertyChange(PropertyChangeEvent evt) {
        Object source = evt.getSource();
        for (int i = 0; i < getSize(); i++) {
            if (editors[i] instanceof PropertyEditorSupport && source == ((PropertyEditorSupport) editors[i]).getSource()) {
                Object oldValue = ((MPropertyEditorSupport) source).getValue();
                Object newValue = ((MPropertyEditorSupport) source).getNewValue();
                setValue(i, newValue);
                editors[i].setValue(newValue);
                pcs.firePropertyChange(getElementAt(i).getDisplayName(), oldValue, newValue);
            } else if (source == editors[i]) {
                Object oldValue = getValue(i);
                Object newValue = editors[i].getValue();
                setValue(i, newValue);
                pcs.firePropertyChange(getElementAt(i).getDisplayName(), evt.getOldValue(), evt.getNewValue());
            }
        }
    }

    @Override
    protected void fireContentsChanged(Object source, int index0, int index1) {
        if (index0 != index1) initEditors();
        super.fireContentsChanged(source, index0, index1);
    }

    @Override
    protected void fireIntervalAdded(Object source, int index0, int index1) {
        initEditors();
        super.fireIntervalAdded(source, index0, index1);
    }

    @Override
    protected void fireIntervalRemoved(Object source, int index0, int index1) {
        initEditors();
        super.fireIntervalRemoved(source, index0, index1);
    }

    PropertyChangeSupport pcs = new PropertyChangeSupport(this);

    public synchronized void removePropertyChangeListener(String propertyName, PropertyChangeListener listener) {
        pcs.removePropertyChangeListener(propertyName, listener);
    }

    public synchronized void removePropertyChangeListener(PropertyChangeListener listener) {
        pcs.removePropertyChangeListener(listener);
    }

    public synchronized void addPropertyChangeListener(String propertyName, PropertyChangeListener listener) {
        pcs.addPropertyChangeListener(propertyName, listener);
    }

    public synchronized void addPropertyChangeListener(PropertyChangeListener listener) {
        pcs.addPropertyChangeListener(listener);
    }
}
