package mobat.bonesa.visualization.properties;

import java.awt.Color;
import java.awt.Graphics;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyDescriptor;
import java.beans.PropertyEditor;
import java.util.ArrayList;
import java.util.HashMap;
import javax.swing.DefaultCellEditor;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableCellRenderer;
import mobat.bonesa.properties.IPropertiesByString;
import mobat.bonesa.visualization.IBonesaPropertyObject;
import mobat.bonesa.visualization.IBonesaVisualization;
import mobat.tuning.store.IAlgorithmDescription;
import com.l2fprod.common.beans.editor.BooleanPropertyEditor;
import com.l2fprod.common.beans.editor.DoublePropertyEditor;
import com.l2fprod.common.beans.editor.IntegerPropertyEditor;
import com.l2fprod.common.beans.editor.StringPropertyEditor;
import com.l2fprod.common.propertysheet.Property;
import com.l2fprod.common.propertysheet.PropertyEditorFactory;
import com.l2fprod.common.propertysheet.PropertyEditorRegistry;
import com.l2fprod.common.propertysheet.PropertyRendererFactory;
import com.l2fprod.common.propertysheet.PropertyRendererRegistry;
import com.l2fprod.common.propertysheet.PropertySheetPanel;
import com.l2fprod.common.swing.renderer.BooleanCellRenderer;

public class BonesaVisualizationPropertyPanel extends PropertySheetPanel implements PropertyRendererFactory, PropertyEditorFactory {

    private static final long serialVersionUID = 1L;

    private HashMap<Class, TableCellRenderer> renderers = new HashMap<Class, TableCellRenderer>();

    private HashMap<Class, PropertyEditor> editors = new HashMap<Class, PropertyEditor>();

    private boolean inWriteReadLoop = false;

    private IBonesaPropertyObject o;

    private PropertyWithOptionsEditor pwoe = new PropertyWithOptionsEditor();

    public BonesaVisualizationPropertyPanel() {
        super();
        super.setEditorFactory(this);
        super.setRendererFactory(this);
        DoubleArrayEditor dae = new DoubleArrayEditor();
        editors.put(double[].class, dae);
        renderers.put(double[].class, dae);
        renderers.put(boolean.class, new BooleanCellRenderer());
        editors.put(boolean.class, new BooleanPropertyEditor());
        editors.put(int.class, new IntegerPropertyEditor());
        editors.put(String.class, new StringPropertyEditor());
        editors.put(double.class, new DoublePropertyEditor());
        ColorEditor ce = new ColorEditor();
        renderers.put(Color.class, ce);
        editors.put(Color.class, ce);
        IAlgorithmDescriptionEditor iade = new IAlgorithmDescriptionEditor();
        editors.put(IAlgorithmDescription.class, iade);
        renderers.put(IAlgorithmDescription.class, iade);
    }

    @Override
    public TableCellRenderer createTableCellRenderer(Property arg0) {
        Class<?> type = arg0.getType();
        if (type == null) {
            return new DefaultTableCellRenderer();
        }
        TableCellRenderer tr = createTableCellRenderer(type);
        if (tr != null) {
            renderers.put(type, tr);
            return tr;
        }
        return new DefaultTableCellRenderer();
    }

    @Override
    public TableCellRenderer createTableCellRenderer(Class arg0) {
        if (renderers.containsKey(arg0)) {
            return renderers.get(arg0);
        } else {
            if (arg0.getSuperclass() != null && createTableCellRenderer(arg0.getSuperclass()) != null) {
                return createTableCellRenderer(arg0.getSuperclass());
            }
            Class<?>[] intf = arg0.getInterfaces();
            for (Class<?> c : intf) {
                if (createTableCellRenderer(c) != null) {
                    return createTableCellRenderer(c);
                }
            }
            return null;
        }
    }

    public PropertyEditor createPropertyEditor(Class arg0) {
        if (editors.containsKey(arg0)) {
            return editors.get(arg0);
        } else {
            if (arg0.getSuperclass() != null && createPropertyEditor(arg0.getSuperclass()) != null) {
                return createPropertyEditor(arg0.getSuperclass());
            }
            Class<?>[] intf = arg0.getInterfaces();
            for (Class<?> c : intf) {
                if (createPropertyEditor(c) != null) {
                    return createPropertyEditor(c);
                }
            }
            return null;
        }
    }

    @Override
    public PropertyEditor createPropertyEditor(Property arg0) {
        if (arg0 instanceof PropertyDescriptorAdapter) {
            PropertyDescriptorAdapter pda = (PropertyDescriptorAdapter) arg0;
            if (pda.getOptions(this.o) != null) {
                pwoe.setSource(pda, this.o);
                return pwoe;
            }
        }
        PropertyEditor pe = createPropertyEditor(arg0.getType());
        if (pe != null) {
            editors.put(arg0.getType(), pe);
            return pe;
        }
        return null;
    }

    public IBonesaPropertyObject getObject() {
        return o;
    }

    public void setObject(IBonesaPropertyObject o) {
        if (o != null) {
            this.inWriteReadLoop = true;
            this.o = o;
            ArrayList<PropertyDescriptor> properties = o.getProperties();
            setProperties(properties.toArray(new PropertyDescriptor[properties.size()]));
            readFromObject(o);
            this.inWriteReadLoop = false;
        }
    }

    public void setProperties(PropertyDescriptor[] descriptors) {
        Property[] properties = new Property[descriptors.length];
        for (int i = 0, c = descriptors.length; i < c; i++) {
            properties[i] = new PropertyDescriptorAdapter(descriptors[i]);
        }
        setProperties(properties);
    }

    public void propertyChange(PropertyChangeEvent evt) {
        repaint();
        if (!inWriteReadLoop && ((evt.getNewValue() == null && evt.getOldValue() != null) || !evt.getNewValue().equals(evt.getOldValue()))) {
            inWriteReadLoop = true;
            try {
                writeToObject(o);
                readFromObject(o);
            } catch (Exception e) {
            }
            firePropertyChange(evt.getPropertyName(), evt.getOldValue(), evt.getNewValue());
            inWriteReadLoop = false;
        }
    }
}
