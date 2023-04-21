package org.cumt.view.editors.classes;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import javax.swing.BorderFactory;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import org.cumt.Messages;
import org.cumt.misc.Editor;
import org.cumt.misc.ValidationError;
import org.cumt.model.Model;
import org.cumt.model.classes.Field;
import org.cumt.model.types.Type;
import org.cumt.view.utils.TextEditor;
import org.cumt.view.utils.TypeListCellRenderer;
import ar.com.da.swing.FormLayout;

@SuppressWarnings("serial")
public class SimpleFieldEditor extends JPanel implements Editor<Field> {

    private JTextField name = new JTextField();

    private JComboBox type = new JComboBox();

    private TextEditor description = new TextEditor();

    private Collection<? extends Type> availableTypes = null;

    public SimpleFieldEditor() {
        setLayout(new BorderLayout());
        FormLayout formLayout = new FormLayout();
        formLayout.setHGap(5);
        formLayout.setVGap(5);
        formLayout.setLabelSectionWidth(120);
        JPanel header = new JPanel(formLayout);
        JLabel label = new JLabel(Messages.get("fieldEditor.name"));
        label.setLabelFor(name);
        name.setPreferredSize(new Dimension(160, 22));
        formLayout.add(name, label, header);
        label = new JLabel(Messages.get("fieldEditor.type"));
        label.setLabelFor(type);
        type.setRenderer(new TypeListCellRenderer());
        type.setPreferredSize(new Dimension(160, 22));
        formLayout.add(type, label, header);
        add(header, BorderLayout.NORTH);
        description.setBorder(BorderFactory.createTitledBorder(Messages.get("simpleFieldEditor.description")));
        description.setPreferredSize(new Dimension(100, 150));
        add(description, BorderLayout.CENTER);
    }

    public void setModel(Model model) {
        if (model != null) {
            if (availableTypes == null) {
                List<Type> types = model.search(Type.class);
                Collections.sort(types, new Comparator<Type>() {

                    public int compare(Type o1, Type o2) {
                        return o1.getName().compareTo(o2.getName());
                    }
                });
                type.setModel(new DefaultComboBoxModel(types.toArray()));
            } else {
                type.setModel(new DefaultComboBoxModel(availableTypes.toArray()));
            }
        }
    }

    public void setAvailableTypes(Collection<? extends Type> types) {
        this.availableTypes = types;
    }

    public void set(Field field) {
        if (field == null) {
            name.setText("");
            type.getModel().setSelectedItem(null);
            description.setText("");
        } else {
            name.setText(field.getName());
            type.getModel().setSelectedItem(field.getType());
            description.setText(field.getDescription());
        }
    }

    public List<ValidationError> validateEdition() {
        List<ValidationError> errors = new ArrayList<ValidationError>();
        if (name.getText().trim().equals("")) {
            errors.add(new ValidationError("fieldEditor.error.nameRequired"));
        }
        return errors;
    }

    public void get(Field field) {
        field.setName(name.getText());
        field.setType((Type) type.getModel().getSelectedItem());
        field.setDescription(description.getText());
    }

    public JComponent getEditorComponent() {
        return this;
    }
}
