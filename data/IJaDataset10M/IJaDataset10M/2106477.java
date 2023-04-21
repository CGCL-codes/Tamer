package net.cattaka.swing.datainputpanel;

import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.HashSet;
import java.util.Iterator;
import javax.swing.JCheckBox;
import javax.swing.JComponent;

public class DIPInfoSwitch implements DIPInfo, ItemListener {

    private String label;

    private boolean defaultData;

    private JCheckBox checkBox;

    private HashSet<DIPInfo> enableComponents = new HashSet<DIPInfo>();

    private HashSet<DIPInfo> disnableComponents = new HashSet<DIPInfo>();

    public DIPInfoSwitch(String label, boolean defaultData, DIPInfo[] enableComs, DIPInfo[] disnableComs) throws InvalidDataTypeException {
        super();
        if (label == null) throw new NullPointerException();
        this.label = "";
        this.defaultData = defaultData;
        this.checkBox = new JCheckBox(label);
        this.checkBox.addItemListener(this);
        for (int i = 0; i < enableComs.length; i++) {
            enableComponents.add(enableComs[i]);
        }
        for (int i = 0; i < disnableComs.length; i++) {
            disnableComponents.add(disnableComs[i]);
        }
        setValue(defaultData);
    }

    public boolean getBooleanValue() {
        return checkBox.isSelected();
    }

    public Object getValue() {
        return checkBox.isSelected();
    }

    public void setValue(boolean value) {
        checkBox.setSelected(value);
        switchEnable(value);
    }

    private void switchEnable(boolean value) {
        Iterator<DIPInfo> ite;
        ite = enableComponents.iterator();
        while (ite.hasNext()) {
            ite.next().setEnable(value);
        }
        ite = disnableComponents.iterator();
        while (ite.hasNext()) {
            ite.next().setEnable(!value);
        }
    }

    public void makeDefault() {
        setValue(defaultData);
    }

    public String getLabel() {
        return label;
    }

    public JComponent getComponent() {
        return checkBox;
    }

    public boolean isEnable() {
        return checkBox.isEnabled();
    }

    public void setEnable(boolean enable) {
        checkBox.setEnabled(enable);
    }

    public void itemStateChanged(ItemEvent e) {
        if (e.getSource() == checkBox) {
            boolean f = checkBox.isSelected();
            switchEnable(f);
        }
    }
}
