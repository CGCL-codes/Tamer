package org.rjam.alert;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.net.URL;
import java.util.Date;
import javax.swing.JPanel;
import org.rjam.gui.beans.Field;
import org.rjam.gui.beans.Row;
import org.rjam.gui.beans.Value;
import org.rjam.gui.fieldeditors.DateTimeFieldEditor;
import org.rjam.gui.fieldeditors.FieldEditor;

/**
 *
 * @author  Tony Bringardner
 */
public class SimpleRulePanel extends javax.swing.JPanel implements IRuleView {

    /**
	 * 
	 */
    private static final long serialVersionUID = 1L;

    public static final String PROPERTY_DELETE_ACTION = "DeleteAction";

    private static final String NOT_SELECTED = "Not Selected";

    private Rule _rule;

    private boolean showConjuction = false;

    private String[] fieldNames;

    private JPanel editor;

    private Row row;

    /** Creates new form RulePanel */
    public SimpleRulePanel() {
        initComponents();
        opCombo.setModel(new javax.swing.DefaultComboBoxModel(Rule.OPERATORS));
        conjunctionCombo.setModel(new javax.swing.DefaultComboBoxModel(Rule.CONJUNCTIONS));
        setRule(new Rule());
    }

    public SimpleRulePanel(Rule rule, Row row) {
        this();
        this.row = row;
        setRule(rule);
    }

    public SimpleRulePanel(Rule rule2, Row row, boolean b) {
        this(rule2, row);
        setShowConjuction(b);
        updateView();
    }

    private void setRule(Rule rule) {
        URL url = getClass().getResource("/org/rjam/images/delete.jpeg");
        if (url != null) {
            deleteButton.setIcon(new javax.swing.ImageIcon(url));
        }
        this._rule = rule;
        scriptEditor.setText(rule.getScript());
        field2Combo.setSelectedItem(rule.getTargetFieldName());
        fieldCombo.setSelectedItem(rule.getFieldName());
        if (rule.getScript() != null) {
            expressionButton.setSelected(true);
            FieldButton.setSelected(false);
            valueButton.setSelected(false);
        } else if (rule.getTargetFieldName() != null) {
            FieldButton.setSelected(true);
            expressionButton.setSelected(false);
            valueButton.setSelected(false);
        } else {
            valueButton.setSelected(true);
            expressionButton.setSelected(false);
            FieldButton.setSelected(false);
        }
        updateView();
    }

    public void setFieldNames(String[] names) {
        this.fieldNames = names;
        fieldCombo.setModel(new javax.swing.DefaultComboBoxModel(names));
        String[] names2 = new String[names.length + 1];
        System.arraycopy(names, 0, names2, 1, names.length);
        names2[0] = NOT_SELECTED;
        field2Combo.setModel(new javax.swing.DefaultComboBoxModel(names2));
        if (_rule != null) {
            fieldCombo.setSelectedItem(_rule.getFieldName());
            String tmp = _rule.getTargetFieldName();
            if (tmp == null || tmp.length() == 0) {
                field2Combo.setSelectedItem(NOT_SELECTED);
            } else {
                field2Combo.setSelectedItem(tmp);
            }
        }
    }

    public String[] getFieldNames() {
        return fieldNames;
    }

    public void updateView() {
        opCombo.setSelectedItem(_rule.getOperator());
        String tmp = _rule.getConjunction();
        if (tmp != null) {
            conjunctionCombo.setSelectedItem(tmp);
        }
        conjunctionCombo.setVisible(showConjuction);
        tmp = (String) fieldCombo.getSelectedItem();
        String nm = _rule.getFieldName();
        if (tmp == null || !tmp.equals(nm)) {
            fieldCombo.setSelectedItem(nm);
        }
        editorPanel.removeAll();
        if (valueButton.isSelected()) {
            editorPanel.add(getEditor());
        } else if (FieldButton.isSelected()) {
            tmp = (String) field2Combo.getSelectedItem();
            nm = _rule.getTargetFieldName();
            if (nm == null) {
                field2Combo.setSelectedItem(NOT_SELECTED);
            } else {
                if (!nm.equals(tmp)) {
                    field2Combo.setSelectedItem(nm);
                }
            }
            editorPanel.add(field2Combo);
        } else if (expressionButton.isSelected()) {
            tmp = scriptEditor.getText();
            nm = _rule.getScript();
            if (nm != null && !nm.equals(tmp)) {
                scriptEditor.setText(nm);
            }
            editorPanel.add(scriptEditor);
        } else {
            System.out.println("nothing selected???");
        }
        revalidate();
    }

    public Rule getRule() {
        return _rule;
    }

    public boolean isShowConjuction() {
        return showConjuction;
    }

    public void setShowConjuction(boolean showConjuction) {
        if ((this.showConjuction = showConjuction)) {
            if (_rule.getConjunction() == null) {
                _rule.setConjunction("and");
                conjunctionCombo.setSelectedItem("and");
            }
        }
    }

    public JPanel getEditor() {
        if (editor == null) {
            String nm = _rule.getFieldName();
            Value val = null;
            if (row != null) {
                if ((val = row.getValue(nm)) == null) {
                    Field fld = Field.getFieldByLabel(nm);
                    if (fld != null) {
                        val = row.getValue(fld.getName());
                    }
                }
            }
            if (val == null) {
                val = _rule.getValue();
            } else {
                _rule.setValue(val.toString());
            }
            Class<?> type = val.getField().getType();
            if (type.isAssignableFrom(Date.class)) {
                editor = new DateTimeFieldEditor(val);
            } else {
                editor = new FieldEditor(val);
                editor.addPropertyChangeListener(val.getField().getName(), new PropertyChangeListener() {

                    public void propertyChange(PropertyChangeEvent evt) {
                        _rule.setValue(evt.getNewValue().toString());
                    }
                });
            }
        }
        return editor;
    }

    private void initComponents() {
        targetTypeButtonGroup = new javax.swing.ButtonGroup();
        scriptEditor = new javax.swing.JTextField();
        field2Combo = new javax.swing.JComboBox();
        conjunctionCombo = new javax.swing.JComboBox();
        fieldCombo = new javax.swing.JComboBox();
        opCombo = new javax.swing.JComboBox();
        jPanel1 = new javax.swing.JPanel();
        buttonPanel = new javax.swing.JPanel();
        valueButton = new javax.swing.JRadioButton();
        FieldButton = new javax.swing.JRadioButton();
        expressionButton = new javax.swing.JRadioButton();
        editorPanel = new javax.swing.JPanel();
        deleteButton = new javax.swing.JButton();
        scriptEditor.setText("stdDev * 3");
        scriptEditor.setPreferredSize(new java.awt.Dimension(120, 24));
        scriptEditor.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                scriptEditorActionPerformed(evt);
            }
        });
        scriptEditor.addFocusListener(new java.awt.event.FocusAdapter() {

            public void focusLost(java.awt.event.FocusEvent evt) {
                scriptEditorFocusLost(evt);
            }
        });
        field2Combo.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Min", "Max", "Ave", "SampleSize" }));
        field2Combo.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                field2ComboActionPerformed(evt);
            }
        });
        setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.RIGHT));
        conjunctionCombo.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "and", "or" }));
        conjunctionCombo.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                conjunctionComboActionPerformed(evt);
            }
        });
        add(conjunctionCombo);
        fieldCombo.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Count", "Min", "Max", "Ave", "SampleSize" }));
        fieldCombo.setToolTipText("Select a field");
        fieldCombo.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                fieldComboActionPerformed(evt);
            }
        });
        add(fieldCombo);
        opCombo.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "<", "<=", "=", "!=", ">=", ">" }));
        opCombo.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                opComboActionPerformed(evt);
            }
        });
        add(opCombo);
        jPanel1.setLayout(new java.awt.BorderLayout());
        targetTypeButtonGroup.add(valueButton);
        valueButton.setSelected(true);
        valueButton.setText("Value");
        valueButton.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                valueButtonActionPerformed(evt);
            }
        });
        buttonPanel.add(valueButton);
        targetTypeButtonGroup.add(FieldButton);
        FieldButton.setText("Field");
        FieldButton.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                FieldButtonActionPerformed(evt);
            }
        });
        buttonPanel.add(FieldButton);
        targetTypeButtonGroup.add(expressionButton);
        expressionButton.setText("Expression");
        expressionButton.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                expressionButtonActionPerformed(evt);
            }
        });
        buttonPanel.add(expressionButton);
        jPanel1.add(buttonPanel, java.awt.BorderLayout.NORTH);
        jPanel1.add(editorPanel, java.awt.BorderLayout.CENTER);
        add(jPanel1);
        deleteButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/org/rjam/images/delete.png")));
        deleteButton.setToolTipText("Delete this rule");
        deleteButton.setPreferredSize(new java.awt.Dimension(25, 25));
        deleteButton.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                deleteButtonActionPerformed(evt);
            }
        });
        add(deleteButton);
    }

    private void expressionButtonActionPerformed(java.awt.event.ActionEvent evt) {
        editorPanel.removeAll();
        _rule.setFieldName(null);
        editorPanel.add(scriptEditor);
        updateView();
    }

    private void FieldButtonActionPerformed(java.awt.event.ActionEvent evt) {
        editorPanel.removeAll();
        editorPanel.add(field2Combo);
        editorPanel.updateUI();
        _rule.setScript(null);
        updateView();
    }

    private void valueButtonActionPerformed(java.awt.event.ActionEvent evt) {
        editorPanel.removeAll();
        editorPanel.add(getEditor());
        _rule.setTargetFieldName(null);
        _rule.setScript(null);
        updateView();
    }

    private void scriptEditorFocusLost(java.awt.event.FocusEvent evt) {
        scriptEditorActionPerformed(null);
    }

    private void scriptEditorActionPerformed(java.awt.event.ActionEvent evt) {
        if (expressionButton.isSelected()) {
            _rule.setScript(scriptEditor.getText());
        } else {
            _rule.setScript(null);
        }
    }

    private void field2ComboActionPerformed(java.awt.event.ActionEvent evt) {
        String nm = (String) field2Combo.getSelectedItem();
        if (NOT_SELECTED.equals(nm) || !FieldButton.isSelected()) {
            _rule.setTargetFieldName(null);
        } else {
            _rule.setTargetFieldName(nm);
        }
    }

    private void opComboActionPerformed(java.awt.event.ActionEvent evt) {
        _rule.setOperator((String) opCombo.getSelectedItem());
    }

    /**
	 * When this changes it effects opcode, field and script
	 * @param evt
	 */
    private void fieldComboActionPerformed(java.awt.event.ActionEvent evt) {
        String nm = (String) fieldCombo.getSelectedItem();
        _rule.setFieldName(nm);
        if (editor != null) {
            remove(editor);
            editor = null;
        }
        updateView();
    }

    private void conjunctionComboActionPerformed(java.awt.event.ActionEvent evt) {
        _rule.setConjunction((String) conjunctionCombo.getSelectedItem());
    }

    private void deleteButtonActionPerformed(java.awt.event.ActionEvent evt) {
        firePropertyChange(PROPERTY_DELETE_ACTION, true, false);
    }

    private javax.swing.JRadioButton FieldButton;

    private javax.swing.JPanel buttonPanel;

    private javax.swing.JComboBox conjunctionCombo;

    private javax.swing.JButton deleteButton;

    private javax.swing.JPanel editorPanel;

    private javax.swing.JRadioButton expressionButton;

    private javax.swing.JComboBox field2Combo;

    private javax.swing.JComboBox fieldCombo;

    private javax.swing.JPanel jPanel1;

    private javax.swing.JComboBox opCombo;

    private javax.swing.JTextField scriptEditor;

    private javax.swing.ButtonGroup targetTypeButtonGroup;

    private javax.swing.JRadioButton valueButton;
}
