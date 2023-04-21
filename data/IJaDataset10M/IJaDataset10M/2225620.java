package javablock.flowchart.blockEditors;

import config.translator;
import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import javablock.flowchart.*;
import javablock.flowchart.blocks.scrBlock;
import javablock.flowchart.blocks.structBlock;
import javax.swing.*;
import widgets.ComboText;

/**
 *
 * @author razi
 */
public class structEditor extends javax.swing.JPanel implements BlockEditor {

    private final ImageIcon delIcon;

    /** Creates new form structEditor */
    public structEditor() {
        initComponents();
        delIcon = new javax.swing.ImageIcon(getClass().getResource("/icons/16/list-remove.png"));
        makeList();
    }

    @SuppressWarnings("unchecked")
    private void initComponents() {
        addField = new javax.swing.JButton();
        name = new javax.swing.JTextField();
        jLabel1 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        fieldsPane = new javax.swing.JPanel();
        iface = new javax.swing.JCheckBox();
        addField.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/16/document-new.png")));
        java.util.ResourceBundle bundle = java.util.ResourceBundle.getBundle("config/lang/lang");
        addField.setText(bundle.getString("structEditor.add"));
        addField.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                addFieldActionPerformed(evt);
            }
        });
        setBorder(javax.swing.BorderFactory.createTitledBorder(bundle.getString("structEditor.struct")));
        jLabel1.setText(bundle.getString("structEditor.name"));
        fieldsPane.setLayout(new java.awt.GridLayout(200, 1));
        jScrollPane1.setViewportView(fieldsPane);
        iface.setText(bundle.getString("structEditor.genInterface"));
        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(layout.createSequentialGroup().addComponent(jLabel1).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addComponent(name, javax.swing.GroupLayout.DEFAULT_SIZE, 128, Short.MAX_VALUE)).addGroup(layout.createSequentialGroup().addComponent(iface).addContainerGap(102, Short.MAX_VALUE)).addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 245, Short.MAX_VALUE));
        layout.setVerticalGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(layout.createSequentialGroup().addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE).addComponent(jLabel1).addComponent(name, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 154, Short.MAX_VALUE).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addComponent(iface)));
    }

    private void addFieldActionPerformed(java.awt.event.ActionEvent evt) {
        Field f = new Field(this);
        fields.add(f);
        makeList();
    }

    private javax.swing.JButton addField;

    private javax.swing.JPanel fieldsPane;

    private javax.swing.JCheckBox iface;

    private javax.swing.JLabel jLabel1;

    private javax.swing.JScrollPane jScrollPane1;

    private javax.swing.JTextField name;

    structBlock editing;

    List<Field> fields = new ArrayList();

    void makeList() {
        fieldsPane.removeAll();
        for (Field field : fields) fieldsPane.add(field);
        fieldsPane.add(addField);
        repaint();
    }

    void removeField(Field f) {
        fields.remove(f);
        makeList();
    }

    public void setEditedBlock(JBlock b) {
        if (b == editing) return;
        if (editing != null) finnishEdit();
        editing = (structBlock) b;
        fields.clear();
        for (String f[] : editing.getFields()) {
            Field field = new Field(this);
            field.name.setText(f[0]);
            field.type.setSelectedItem(addField);
            for (ComboText c : types) {
                if (c.getValue().equals(editing.type)) {
                    field.type.setSelectedItem(c);
                    break;
                }
            }
            fields.add(field);
        }
        makeList();
        name.setText(editing.name);
        iface.setSelected(editing.genInterface);
    }

    public void finnishEdit() {
        saveBlock();
        editing = null;
    }

    public boolean changes() {
        return true;
    }

    public void saveBlock() {
        editing.clear();
        editing.genInterface = this.iface.isSelected();
        editing.name = name.getText();
        for (Field field : fields) {
            if (field.name.getText().length() == 0) continue;
            editing.addField(field.name.getText(), ((ComboText) field.type.getSelectedItem()).getValue());
        }
        editing.shape();
    }

    public JBlock getBlock() {
        return editing;
    }

    class Field extends JPanel {

        JComboBox type;

        JTextField name;

        structEditor p;

        Field t;

        Field(final structEditor p) {
            this.p = p;
            type = new JComboBox();
            for (ComboText t : types) {
                type.addItem(t);
            }
            name = new JTextField();
            JButton delete = new JButton();
            delete.setIcon(delIcon);
            this.setLayout(new BorderLayout());
            add(name, BorderLayout.CENTER);
            add(type, BorderLayout.WEST);
            add(delete, BorderLayout.EAST);
            t = this;
            delete.addActionListener(new ActionListener() {

                public void actionPerformed(ActionEvent e) {
                    p.removeField(t);
                }
            });
        }
    }

    public enum DataType {

        INTEGER, NUMBER, STRING, CHARARRAY, LOGIC, ANY
    }

    ComboText types[] = { new ComboText(translator.get("ioEditor.typeNumber"), "NUMBER"), new ComboText(translator.get("ioEditor.typeInteger"), "INTEGER"), new ComboText(translator.get("ioEditor.typeString"), "STRING"), new ComboText(translator.get("ioEditor.typeLogic"), "LOGIC"), new ComboText(translator.get("ioEditor.typeAny"), "ANY") };
}
