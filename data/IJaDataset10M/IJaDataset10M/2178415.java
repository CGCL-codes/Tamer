package se.kth.cid.conzilla.component;

import se.kth.cid.conzilla.metadata.*;
import se.kth.cid.component.MetaData;
import se.kth.cid.component.MetaDataUtils;
import se.kth.cid.conzilla.util.*;
import se.kth.cid.neuron.*;
import se.kth.cid.util.*;
import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;

public class DataDisplayerPanel extends AbstractListDisplayer {

    Vector tags;

    Vector values;

    ItemListener listener;

    Neuron neuron;

    NeuronType type;

    String[] allowedTabs;

    class IllegalString {

        String s;

        public IllegalString(String s) {
            this.s = s;
        }

        public String getString() {
            return s;
        }

        public boolean equals(Object o) {
            if (o instanceof String) return s.equals(o); else return super.equals(o);
        }
    }

    class GridModel extends ListGridTableModel {

        public GridModel() {
        }

        public int getRowCount() {
            return tags.size();
        }

        public int getColumnCount() {
            return 2;
        }

        public Object getValueAt(int row, int col) {
            if (col == 0) return tags.get(row);
            return values.get(row);
        }

        public String getTitle(int col) {
            if (col == 0) return "Tag";
            return "Value";
        }
    }

    class IllegalStringRenderer extends StringRenderer {

        public IllegalStringRenderer() {
        }

        public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
            if (value instanceof String) return super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
            setText(((IllegalString) value).getString());
            if (isSelected) {
                this.setBackground(list.getSelectionBackground());
                this.setForeground(Color.red);
            } else {
                this.setBackground(Color.white);
                this.setForeground(Color.red);
            }
            return this;
        }
    }

    public DataDisplayerPanel(Neuron neuron, NeuronType type, boolean editable, MetaDataEditListener editListener, String metaDataField) {
        super(editable, editListener, metaDataField);
        this.neuron = neuron;
        this.type = type;
        if (type != null) allowedTabs = type.getDataTags();
        listener = new ItemListener() {

            public void itemStateChanged(ItemEvent e) {
                if (e.getStateChange() == ItemEvent.SELECTED) fireEdited();
                JComboBox src = (JComboBox) e.getSource();
                if (src.getSelectedItem() instanceof IllegalString) src.setForeground(Color.red); else src.setForeground(Color.black);
            }
        };
        tags = new Vector();
        values = new Vector();
        DataValue[] vals = neuron.getDataValues();
        for (int i = 0; i < vals.length; i++) addItem(vals[i], i);
        setModel(new GridModel());
    }

    public DataValue[] getDataValues(boolean resetEdited) {
        if (resetEdited) edited = false;
        DataValueImpl[] vals = new DataValueImpl[tags.size()];
        for (int i = 0; i < vals.length; i++) {
            String tag;
            if (editable) tag = (String) ((JComboBox) tags.get(i)).getSelectedItem(); else tag = ((StringPanel) tags.get(i)).getString(resetEdited, false);
            vals[i] = new DataValueImpl(tag, ((StringPanel) values.get(i)).getString(resetEdited, false));
        }
        return vals;
    }

    protected boolean isItemEdited() {
        for (int i = 0; i < tags.size(); i++) {
            if (((StringPanel) values.get(i)).isEdited()) return true;
        }
        return false;
    }

    protected void removeItemImpl(int index) {
        if (editable) tags.remove(index); else removeAndDetach(tags, index);
        removeAndDetach(values, index);
    }

    protected void createItemImpl(int index) {
        addItem(new DataValueImpl(null, null), index);
    }

    void addItem(DataValue value, int index) {
        String predicate = value.predicateURI();
        String objectValue = value.objectValue();
        if (editable) {
            JComboBox tag = new JComboBox();
            int found = -1;
            if (allowedTabs != null) {
                if (predicate == null) predicate = allowedTabs[0];
                for (int i = 0; i < allowedTabs.length; i++) {
                    if (allowedTabs[i].equals(predicate)) found = i;
                    tag.addItem(allowedTabs[i]);
                }
            }
            if (found == -1) {
                IllegalString str = new IllegalString(value.predicateURI());
                tag.addItem(str);
                tag.setSelectedItem(str);
            } else tag.setSelectedItem(value.predicateURI());
            tag.setRenderer(new IllegalStringRenderer());
            tag.setBackground(Color.white);
            if (tag.getSelectedItem() instanceof IllegalString) tag.setForeground(Color.red);
            tag.addItemListener(listener);
            tags.add(index, tag);
        } else {
            StringPanel tag = new StringPanel(predicate, false, false, editListener, metaDataField);
            tags.add(index, tag);
        }
        StringPanel valueField = new StringPanel(objectValue, false, editable, editListener, metaDataField);
        values.add(index, valueField);
    }
}
