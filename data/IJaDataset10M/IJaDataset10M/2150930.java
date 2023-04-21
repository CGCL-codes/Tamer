package org.adapit.wctoolkit.fomda.graphinterfaces.feature.transformationdescriptor;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.LinkedHashMap;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import org.adapit.wctoolkit.uml.ext.fomda.metamodel.transformation.TransformationDescriptor;
import org.adapit.wctoolkit.uml.ext.fomda.metamodel.transformation.TransformationParameter;
import org.adapit.wctoolkit.uml.ext.fomda.metamodel.transformation.TransformationParameter.TransformationParameterKind;

@SuppressWarnings({ "serial", "unchecked" })
public class OutputParameterPane extends JPanel {

    public org.adapit.wctoolkit.fomda.features.i18n.I18N_FomdaProfile messages = org.adapit.wctoolkit.fomda.features.i18n.I18N_FomdaProfile.getInstance();

    protected JLabel nameLabel, typeLabel, tableLabel, retTypeLabel;

    protected JTextField nameTField, typeTField, retTypeTField;

    protected JButton addButton, deleteButton;

    protected GenericParameterTable table;

    protected LinkedHashMap parameterDescriptors;

    protected TransformationDescriptor transformationDescriptor;

    protected JScrollPane tableScroll;

    public OutputParameterPane(TransformationDescriptor td) {
        super();
        this.transformationDescriptor = td;
        if (td.getOutputParameters() == null) {
            td.setOutputParameters(new LinkedHashMap<Object, TransformationParameter>());
        }
        parameterDescriptors = td.getOutputParameters();
        setLayout(new BorderLayout());
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        setPanel();
        updateTable();
    }

    private int yAlign = 10;

    private Dimension fieldLabelMinSize = new Dimension(210, 22);

    private JPanel tablePanel;

    private void setPanel() {
        JPanel namePanel = new JPanel(new BorderLayout());
        nameLabel = new JLabel();
        nameLabel.setText(messages.getMessage("Parameter_Name") + ":");
        nameLabel.setHorizontalAlignment(SwingConstants.LEFT);
        nameLabel.setPreferredSize(fieldLabelMinSize);
        namePanel.add(nameLabel, BorderLayout.WEST);
        nameTField = new JTextField();
        nameTField.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                addOutputParameter();
            }
        });
        namePanel.add(nameTField, BorderLayout.CENTER);
        JPanel typePanel = new JPanel(new BorderLayout());
        typeLabel = new JLabel();
        typeLabel.setText(messages.getMessage("Parameter_Type") + ":");
        typeLabel.setHorizontalAlignment(SwingConstants.LEFT);
        typeLabel.setPreferredSize(fieldLabelMinSize);
        typePanel.add(typeLabel, BorderLayout.WEST);
        typeTField = new JTextField();
        typeTField.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                addOutputParameter();
            }
        });
        typePanel.add(typeTField, BorderLayout.CENTER);
        JPanel fieldPanel = new JPanel(new GridLayout(2, 1));
        fieldPanel.add(namePanel);
        fieldPanel.add(typePanel);
        this.add(fieldPanel, BorderLayout.NORTH);
        tablePanel = new JPanel(new BorderLayout());
        add(tablePanel, BorderLayout.CENTER);
        JPanel buttonsPanel = new JPanel();
        buttonsPanel.setLayout(new FlowLayout());
        buttonsPanel.setBounds(10, yAlign + 60, 510, 30);
        tablePanel.add(buttonsPanel, BorderLayout.NORTH);
        addButton = new JButton(messages.getMessage("Add"));
        addButton.setHorizontalAlignment(SwingConstants.CENTER);
        addButton.setBounds(125, yAlign + 60, 100, 22);
        addButton.addMouseListener(new MouseAdapter() {

            @Override
            public void mousePressed(MouseEvent e) {
                addOutputParameter();
            }
        });
        buttonsPanel.add(addButton);
        deleteButton = new JButton();
        deleteButton.setText(messages.getMessage("Remove"));
        deleteButton.setBounds(275, yAlign + 60, 100, 22);
        deleteButton.addMouseListener(new MouseAdapter() {

            public void mouseReleased(MouseEvent e) {
                deleteInputParameter();
            }
        });
        buttonsPanel.add(deleteButton);
        tablePanel.add(new JScrollPane(getTable()), BorderLayout.CENTER);
        updateTable();
    }

    protected void addOutputParameter() {
        try {
            String pName = OutputParameterPane.this.nameTField.getText();
            String pType = OutputParameterPane.this.typeTField.getText();
            TransformationParameter tp = new TransformationParameter(OutputParameterPane.this.transformationDescriptor);
            tp.setName(pName);
            tp.setType(pType);
            tp.setTransformationParameterKind(TransformationParameterKind.OUTPUT);
            transformationDescriptor.addOutputParameter(tp.getName(), tp);
            OutputParameterPane.this.nameTField.setText("");
            OutputParameterPane.this.typeTField.setText("");
            updateTable();
            EnabledExecutionTypesPanel.showWarningWhenClassNotFound(pType);
            nameTField.requestFocus();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    protected void deleteInputParameter() {
        try {
            int index = table.getSelectedRow();
            if (index >= 0 && getTable().getOrderedParameters() != null && getTable().getOrderedParameters().size() - 1 >= index) {
                transformationDescriptor.removeInputParameter((TransformationParameter) getTable().getOrderedParameters().get(index));
                updateTable();
            }
        } catch (Exception e1) {
            e1.printStackTrace();
        }
    }

    public void updateTable() {
        try {
            table.setModel(transformationDescriptor.getOutputParameters());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    protected GenericParameterTable getTable() {
        if (table == null) table = new GenericParameterTable();
        return table;
    }

    public JButton getAddButton() {
        return addButton;
    }

    public void setAddButton(JButton addButton) {
        this.addButton = addButton;
    }

    public JButton getDeleteButton() {
        return deleteButton;
    }

    public void setDeleteButton(JButton deleteButton) {
        this.deleteButton = deleteButton;
    }

    public JLabel getNameLabel() {
        return nameLabel;
    }

    public void setNameLabel(JLabel nameLabel) {
        this.nameLabel = nameLabel;
    }

    public JTextField getNameTField() {
        return nameTField;
    }

    public void setNameTField(JTextField nameTField) {
        this.nameTField = nameTField;
    }

    public LinkedHashMap getParameterDescriptors() {
        return parameterDescriptors;
    }

    public void setParameterDescriptors(LinkedHashMap parameterDescriptors) {
        this.parameterDescriptors = parameterDescriptors;
    }

    public JLabel getRetTypeLabel() {
        return retTypeLabel;
    }

    public void setRetTypeLabel(JLabel retTypeLabel) {
        this.retTypeLabel = retTypeLabel;
    }

    public JTextField getRetTypeTField() {
        return retTypeTField;
    }

    public void setRetTypeTField(JTextField retTypeTField) {
        this.retTypeTField = retTypeTField;
    }

    public JLabel getTableLabel() {
        return tableLabel;
    }

    public void setTableLabel(JLabel tableLabel) {
        this.tableLabel = tableLabel;
    }

    public JScrollPane getTableScroll() {
        return tableScroll;
    }

    public void setTableScroll(JScrollPane tableScroll) {
        this.tableScroll = tableScroll;
    }

    public TransformationDescriptor getTransformationDescriptor() {
        return transformationDescriptor;
    }

    public void setTransformationDescriptor(TransformationDescriptor transformationDescriptor) {
        this.transformationDescriptor = transformationDescriptor;
    }

    public JLabel getTypeLabel() {
        return typeLabel;
    }

    public void setTypeLabel(JLabel typeLabel) {
        this.typeLabel = typeLabel;
    }

    public JTextField getTypeTField() {
        return typeTField;
    }

    public void setTypeTField(JTextField typeTField) {
        this.typeTField = typeTField;
    }

    public void refresh(TransformationDescriptor transformationDescriptor2) {
        this.transformationDescriptor = transformationDescriptor2;
        updateTable();
        updateUI();
    }
}
