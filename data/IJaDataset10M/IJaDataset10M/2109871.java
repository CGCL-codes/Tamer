package gui;

import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Map.Entry;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JTextField;
import models.ModelComponent;
import data.DataContent;
import data.DataContentListener;
import data.PropertyEditEvent;
import data.TypedProperty;

public class DataContentPropertyEditor extends JComponent implements DataContentListener, ModelComponent {

    DataContent content;

    JButton newButton;

    public DataContentPropertyEditor(DataContent c) {
        content = c;
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        newButton = new JButton("Add");
        newButton.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                DataContentPropertyEditor.this.remove(newButton);
                DataContentPropertyEditor.this.add(new PropertyEditRow("", new TypedProperty(""), true));
                revalidate();
                repaint();
            }
        });
        this.addHierarchyListener(new ModelComponent.ModelComponentInitializer(this));
    }

    @Override
    public JComponent asJComponent() {
        return this;
    }

    @Override
    public void initialize() {
        content.addDataContentListener(this);
        reload();
    }

    @Override
    public void uninitialize() {
        content.removeDataContentListener(this);
    }

    protected void reload() {
        removeAll();
        for (Entry<String, TypedProperty> property : content.getUserProperties().entrySet()) {
            add(new PropertyEditRow(property.getKey(), property.getValue(), true));
        }
        if (newButton.getParent() != this) {
            add(newButton);
        }
    }

    public void userPropertyAdded(PropertyEditEvent e) {
        reload();
        revalidate();
        repaint();
    }

    public void userPropertyChanged(PropertyEditEvent e) {
        reload();
        revalidate();
        repaint();
    }

    public void userPropertyRemoved(PropertyEditEvent e) {
        reload();
        revalidate();
        repaint();
    }

    protected class PropertyEditRow extends JComponent {

        String name;

        TypedProperty value;

        boolean nameEditable;

        public PropertyEditRow(String name, TypedProperty value, boolean nameEditable) {
            this.name = name;
            this.value = value;
            setLayout(new GridLayout(1, 2));
            JTextField nameField = new JTextField(name);
            nameField.setEditable(nameEditable);
            nameField.addActionListener(new ActionListener() {

                public void actionPerformed(ActionEvent e) {
                    JTextField nameField = (JTextField) e.getSource();
                    if (nameField.isEditable()) {
                        content.removeUserProperty(PropertyEditRow.this.name);
                        content.putUserProperty(nameField.getText(), PropertyEditRow.this.value);
                    }
                }
            });
            add(nameField);
            JComponent valueField = value.generateGUIComponent();
            add(valueField);
        }
    }
}
