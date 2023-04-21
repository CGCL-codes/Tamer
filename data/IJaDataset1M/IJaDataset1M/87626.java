package jcontrol.conect.gui.manufacturer;

import jcontrol.conect.gui.design.*;
import jcontrol.conect.data.*;
import jcontrol.conect.tools.*;
import jcontrol.conect.data.settings.*;
import jcontrol.conect.data.database.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;
import javax.swing.tree.*;
import javax.swing.table.*;
import javax.swing.text.*;
import javax.swing.event.*;
import javax.swing.border.BevelBorder;
import javax.swing.plaf.basic.BasicComboBoxRenderer;
import java.sql.*;

/**
 * A <code>ContainerItem</code> is a <code>DragDropItem</code> that may
 * contain other drag-drop-items. 
 * 
 * @version 1.0 2000-6-6
 * @author  Witali Sauter
 */
public class ParameterTypeContainerItem extends AbstractContainerItem {

    /** The <code>DragDropItem</code> for the ParameterTypeContainerItem. */
    protected DragDropItem source;

    /** Reference to the <code>ParameterType</code> belonging to this 
     * container.*/
    protected jcontrol.conect.data.ParameterType parameterType;

    /** <code>ParameterListOfValues</code> for this 
     * <code>ParameterType</code>. */
    protected Hashtable parameterListOfValues;

    /** <code>ParameterAtomicType</code> for this <code>ParameterType</code>.*/
    protected ParameterAtomicType parameterAtomicType;

    /**
     * Constructs a new <code>ParameterTypeContainerItem</code> object.
     *
     * @param source the <code>DragDropItem</code> for the ContainerItem
     * @param leafContainer <code>true</code>, if this container shall be a 
     *                      leaf-container
     * @param rootContainer <code>true</code>, if this container shall be a 
     *                      root-container
     */
    public ParameterTypeContainerItem(DragDropItem source, boolean rootContainer, boolean leafContainer) {
        super(source, rootContainer, leafContainer);
        this.source = source;
    }

    /**
     * Makes a deep copy of this object.
     * @return a copy of <code>this</code> 
     */
    public Object clone() {
        ParameterTypeContainerItem clone = new ParameterTypeContainerItem(source, rootContainer, leafContainer);
        clone.setParameterType(parameterType);
        clone.setParameterAtomicType(parameterAtomicType);
        clone.setParameterListOfValues(parameterListOfValues);
        return clone;
    }

    /**
     * Compares one ParameterTypeContainerItem to another. 
     * Two item are equal if all there attributes are equal.
     *
     * @param o an object to compare this item to
     * @return <code>true</code>, if both items are equal
     */
    public boolean equals(Object o) {
        if (!(o instanceof ParameterTypeContainerItem)) return false;
        ParameterTypeContainerItem ci = (ParameterTypeContainerItem) o;
        if (ci.getParameterType() != this.getParameterType()) return false;
        return true;
    }

    /**
     * Get the value of parameterType.
     *
     * @return Value of parameterType.
     */
    public jcontrol.conect.data.ParameterType getParameterType() {
        return parameterType;
    }

    /**
     * Set the value of parameterType.
     *
     * @param v  Value to assign to parameterType.
     */
    public void setParameterType(jcontrol.conect.data.ParameterType v) {
        this.parameterType = v;
    }

    /**
     * Get the value of parameterAtomicType.
     * @return Value of parameterAtomicType.
     */
    public ParameterAtomicType getParameterAtomicType() {
        return parameterAtomicType;
    }

    /**
     * Set the value of parameterAtomicType.
     * @param v  Value to assign to parameterAtomicType.
     */
    public void setParameterAtomicType(ParameterAtomicType v) {
        this.parameterAtomicType = v;
    }

    /**
     * Get the value of parameterListOfValues.
     * @return Value of parameterListOfValues.
     */
    public Hashtable getParameterListOfValues() {
        return parameterListOfValues;
    }

    /**
     * Set the value of parameterListOfValues.
     * @param v  Value to assign to parameterListOfValues.
     */
    public void setParameterListOfValues(Hashtable v) {
        this.parameterListOfValues = v;
    }

    /**
     * Gets the <code>DragDropItem</code> object of this 
     * <code>ParameterTypeContainerItem</code>.
     *
     * @return the <code>DragDropItem</code> object of this 
     *         <code>ParameterTypeContainerItem</code>.
     */
    public DragDropItem getDragDropItem() {
        return source;
    }

    /**
     */
    public Object[] getTableRowArray() {
        Vector row = new Vector();
        row.addElement(this);
        row.addElement(parameterType.getParameterTypeName());
        row.addElement(parameterType.getParameterAtomicType().getAtomicTypeName());
        row.addElement(String.valueOf(parameterType.getParameterMinimumValue()));
        row.addElement(String.valueOf(parameterType.getParameterMaximumValue()));
        row.addElement(String.valueOf(parameterType.getParameterTypeSize()));
        return row.toArray();
    }

    /**
     * Pops up a user-dialog for modifying the parameters of this container.
     *
     * @param box the <code>DesignBox</code> this container is part of
     */
    public void modify(DesignBox box) {
        ModifyDialog md = new ModifyDialog(box);
        md.show();
    }

    /**
     * Pops up a user-dialog for modifying the parameters of a new container
     * and inserts it into a <code>DesignBox</code> 
     *
     * @param box the <code>DesignBox</code> this container will be inserted to
     * @param dest the destination-path the new container shall be inserted
     */
    public void modifyAndInsert(DesignBox box, TreePath dest) {
        ModifyDialog md = new ModifyDialog(box, dest, true);
        md.show();
    }

    /**
     * Pops up a user-dialog for specifying the parameters of a new container
     * and inserts it into a <code>DesignBox</code> 
     *
     * @param box the <code>DesignBox</code> this container will be inserted to
     * @param dest the destination-path the new container shall be inserted
     */
    public void createAndInsert(DesignBox box, TreePath dest) {
        parameterListOfValues = new Hashtable();
        CreateDialog cd = new CreateDialog(box, dest);
        cd.show();
    }

    /**
     * Returns a string describing this container
     *
     * @return a string describing this container
     */
    public String toString() {
        StringBuffer buf = new StringBuffer(name + ":[");
        if (parameterType != null) {
            buf.append(parameterType.toString());
            buf.append("]");
        } else {
            buf.append("empty");
        }
        return buf.toString();
    }

    /**
     * This dialog is used for specifying the parameters of the container-item
     * when creating it.
     */
    protected class CreateDialog extends DefaultCreateDialog {

        /** Text field to enter the name of the <code>ParameterType</code>. */
        protected JTextField nameField;

        /** Combo box to select <code>AtomicType</code> of 
         * <code>ParameterType</code>. */
        public JComboBox atomicTypeBox;

        /** Text field to enter the size of the <code>ParameterType</code>. */
        protected JTextField sizeField;

        /** Label that shows the dimension of the atomic type size. */
        protected JLabel dimLabel;

        /** Button so set "enum". */
        JButton enumButton;

        /** Combo box to select low access of <code>ParameterType</code>. */
        JComboBox lowAccess;

        /** Combo box to select high access of <code>ParameterType</code>. */
        JComboBox highAccess;

        /** Text area for description. */
        JTextArea descriptionArea;

        /** Layout manager for the min. and max. value fields. */
        CardLayout minMaxLayout;

        /** Panel for the min. and max. value fields. */
        JPanel minMaxPanel;

        /** Text field for minimum value. */
        JTextField minValueField;

        /** Text field for maximum value. */
        JTextField maxValueField;

        /** 
         * Initialize a new <code>CreateDialog</code>.
         *
         * @param box  <code>DesignBox<\code>
         * @param dest TreePath wehre Icon schould be added
         */
        protected CreateDialog(DesignBox box, TreePath dest) {
            super(box, dest);
            setTitle(dialogTitle + ParameterTypeContainerItem.this.getType());
            initialize();
            pack();
        }

        /**
         * Initialize a new <code>CreationDialog</code>.
         */
        protected CreateDialog() {
            this(null, null);
        }

        /**
         * Initializes the gui-components for the dialog.
         */
        protected void initialize() {
            JTabbedPane tabbedPane = new JTabbedPane();
            JPanel generalPanel = getGeneralPanel();
            tabbedPane.addTab(resources.getString("ptci.tabb.general"), generalPanel);
            JPanel settingsPanel = getSettingsPanel();
            tabbedPane.addTab(resources.getString("ptci.tabb.settings"), settingsPanel);
            cp.add(tabbedPane, BorderLayout.CENTER);
            atomicTypeBox.setSelectedIndex(1);
            atomicTypeBox.setSelectedIndex(0);
        }

        /**
         * Creates a panel where general settings for the 
         * <code>ParameterType</code> can be set.
         *
         * @return a panel for settings.
         */
        private JPanel getGeneralPanel() {
            GridBagConstraints gbc = new GridBagConstraints();
            GridBagLayout gbl = new GridBagLayout();
            JPanel panel = new JPanel(gbl);
            panel.setBorder(BorderFactory.createEtchedBorder());
            gbc.anchor = GridBagConstraints.WEST;
            gbc.gridwidth = 1;
            JLabel nameLabel = new JLabel(resources.getString("ptci.general.label.name"));
            panel.add(nameLabel, gbc);
            panel.add(Box.createHorizontalStrut(10));
            nameField = new JTextField(30);
            gbc.gridwidth = GridBagConstraints.REMAINDER;
            panel.add(nameField, gbc);
            JLabel atomicTypeLabel = new JLabel(resources.getString("ptci.general.label.atomicType"));
            gbc.gridwidth = 1;
            panel.add(atomicTypeLabel, gbc);
            panel.add(Box.createHorizontalStrut(10));
            Collection c = Settings.getParameterAtomicTypes().values();
            ParameterAtomicType[] atomicTypes = new ParameterAtomicType[c.size()];
            c.toArray(atomicTypes);
            Arrays.sort(atomicTypes, new Comparator() {

                public int compare(Object o1, Object o2) {
                    int comp;
                    comp = ((ParameterAtomicType) o1).getAtomicTypeName().compareTo(((ParameterAtomicType) o2).getAtomicTypeName());
                    return comp;
                }
            });
            atomicTypeBox = new JComboBox(atomicTypes);
            atomicTypeBox.setRenderer(new BasicComboBoxRenderer() {

                public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
                    super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                    String n = (value == null) ? "" : new String(((ParameterAtomicType) value).getAtomicTypeName());
                    setText((value == null) ? "" : (n.length() > 40) ? n.substring(0, 40) + "..." : n);
                    return this;
                }
            });
            atomicTypeBox.addItemListener(new ItemListener() {

                public void itemStateChanged(ItemEvent e) {
                    ParameterTypeContainerItem.this.parameterAtomicType = (ParameterAtomicType) atomicTypeBox.getSelectedItem();
                    sizeField.setEditable(isSizeFieldEditable());
                    setDimLabel();
                    enumButton.setEnabled(isEnumButtonEnabled());
                    toggleMinMaxFields();
                }
            });
            gbc.gridwidth = 1;
            gbc.fill = GridBagConstraints.HORIZONTAL;
            panel.add(atomicTypeBox, gbc);
            panel.add(Box.createHorizontalStrut(5));
            enumButton = new JButton(resources.getString("ptci.general.label.enum"));
            enumButton.addActionListener(this);
            gbc.gridwidth = GridBagConstraints.REMAINDER;
            gbc.anchor = GridBagConstraints.EAST;
            gbc.fill = GridBagConstraints.NONE;
            panel.add(enumButton, gbc);
            JLabel sizeLabel = new JLabel(resources.getString("ptci.general.label.size"));
            gbc.gridwidth = 1;
            gbc.anchor = GridBagConstraints.WEST;
            panel.add(sizeLabel, gbc);
            panel.add(Box.createHorizontalStrut(10));
            sizeField = new JTextField(15);
            sizeField.setDocument(new IntegerDocument(0, 32767));
            sizeField.addCaretListener(new CaretListener() {

                public void caretUpdate(CaretEvent e) {
                    toggleMinMaxFields();
                    enumButton.setEnabled(isEnumButtonEnabled());
                }
            });
            panel.add(sizeField, gbc);
            panel.add(Box.createHorizontalStrut(2));
            dimLabel = new JLabel();
            gbc.gridwidth = GridBagConstraints.REMAINDER;
            panel.add(dimLabel, gbc);
            panel.add(Box.createVerticalStrut(10), gbc);
            JLabel lowAccessLabel = new JLabel(resources.getString("ptci.general.label.lowAccess"));
            gbc.gridwidth = 1;
            panel.add(lowAccessLabel, gbc);
            panel.add(Box.createHorizontalStrut(10));
            lowAccess = new JComboBox();
            lowAccess.insertItemAt(resources.getString("ptci.general.label.accessNone"), 0);
            lowAccess.insertItemAt(resources.getString("ptci.general.label.accessRead"), 1);
            lowAccess.insertItemAt(resources.getString("ptci.general.label.accessRW"), 2);
            gbc.gridwidth = GridBagConstraints.REMAINDER;
            panel.add(lowAccess, gbc);
            JLabel highAccessLabel = new JLabel(resources.getString("ptci.general.label.highAccess"));
            gbc.gridwidth = 1;
            panel.add(highAccessLabel, gbc);
            panel.add(Box.createHorizontalStrut(10));
            highAccess = new JComboBox();
            highAccess.insertItemAt(resources.getString("ptci.general.label.accessNone"), 0);
            highAccess.insertItemAt(resources.getString("ptci.general.label.accessRead"), 1);
            highAccess.insertItemAt(resources.getString("ptci.general.label.accessRW"), 2);
            gbc.gridwidth = GridBagConstraints.REMAINDER;
            panel.add(highAccess, gbc);
            return panel;
        }

        /** 
         * Help methode to proof if sizeField is editable. This methode proof
         * what kind of atomic type this <code>ParameterType</code> is and 
         * decide the sizeField is editable or not.
         *
         * @return <code>true</code> if sizeField can be edit.
         */
        private boolean isSizeFieldEditable() {
            boolean editable = true;
            if ((parameterAtomicType.getAtomicTypeNumber() == 0) || (parameterAtomicType.getAtomicTypeNumber() == 6)) {
                sizeField.setText("0");
                editable = false;
            } else if ((parameterAtomicType.getAtomicTypeNumber() == 1) || (parameterAtomicType.getAtomicTypeNumber() == 2) || (parameterAtomicType.getAtomicTypeNumber() == 4)) {
                sizeField.setDocument(new IntegerDocument(0, 32));
            } else if (parameterAtomicType.getAtomicTypeNumber() == 3) {
                sizeField.setDocument(new IntegerDocument(1, 254));
            } else if (parameterAtomicType.getAtomicTypeNumber() == 5) {
                sizeField.setDocument(new IntegerDocument(0, 4095));
            }
            return editable;
        }

        /** 
         * Help methode to set the dimension of the atomic type size.
         */
        private void setDimLabel() {
            if ((parameterAtomicType.getAtomicTypeNumber() == 3) || (parameterAtomicType.getAtomicTypeNumber() == 5)) dimLabel.setText(resources.getString("ptci.general.label.bytes")); else dimLabel.setText(resources.getString("ptci.general.label.bits"));
        }

        /** 
         * Help methode to proof if <code>enumButton</code> is enabled. 
         * This methode proof what kind of atomic type this 
         * <code>ParameterType</code> is and  decide wether the enumButton is 
         * enabled or not.
         *
         * @return <code>true</code> if enumButton is enabled.
         */
        private boolean isEnumButtonEnabled() {
            boolean enabled = false;
            if (((parameterAtomicType.getAtomicTypeNumber() == 4) || (parameterAtomicType.getAtomicTypeNumber() == 5)) && !sizeField.getText().equals("")) enabled = true;
            return enabled;
        }

        /**
         * Creates a panel where settings for the 
         * <code>ParameterType</code> can be set.
         *
         * @return a panel for settings.
         */
        private JPanel getSettingsPanel() {
            GridBagConstraints gbc = new GridBagConstraints();
            GridBagLayout gbl = new GridBagLayout();
            JPanel panel = new JPanel(gbl);
            panel.setBorder(BorderFactory.createEtchedBorder());
            gbc.anchor = GridBagConstraints.WEST;
            gbc.gridwidth = 1;
            JLabel descriptionLabel = new JLabel(resources.getString("ptci.settings.label.description"));
            panel.add(descriptionLabel, gbc);
            panel.add(Box.createHorizontalStrut(10), gbc);
            descriptionArea = new JTextArea(10, 30);
            JScrollPane scrollPane = new JScrollPane(descriptionArea);
            gbc.gridheight = 3;
            gbc.gridwidth = GridBagConstraints.REMAINDER;
            panel.add(scrollPane, gbc);
            gbc.gridheight = 1;
            panel.add(Box.createVerticalStrut(10), gbc);
            gbc.fill = GridBagConstraints.HORIZONTAL;
            panel.add(new JSeparator(SwingConstants.HORIZONTAL), gbc);
            panel.add(Box.createVerticalStrut(10), gbc);
            minMaxPanel = getMinMaxFields();
            gbc.gridwidth = GridBagConstraints.REMAINDER;
            panel.add(minMaxPanel, gbc);
            return panel;
        }

        /**
         * Creates a panel with a <code<CardLayout</code> to show and hide
         * the min. and max. value text fields.
         *
         * @return <code>JPanel</code> with min. and max value text fields.
         */
        private JPanel getMinMaxFields() {
            minMaxLayout = new CardLayout();
            JPanel panel = new JPanel(minMaxLayout);
            GridBagConstraints gbc = new GridBagConstraints();
            GridBagLayout gbl = new GridBagLayout();
            JPanel subPanel1 = new JPanel(gbl);
            JLabel minValueLabel = new JLabel(resources.getString("ptci.settings.label.minValue"));
            gbc.gridwidth = 1;
            subPanel1.add(minValueLabel, gbc);
            subPanel1.add(Box.createHorizontalStrut(10), gbc);
            gbc.gridwidth = GridBagConstraints.REMAINDER;
            gbc.fill = GridBagConstraints.NONE;
            minValueField = new JTextField(10);
            subPanel1.add(minValueField, gbc);
            JLabel maxValueLabel = new JLabel(resources.getString("ptci.settings.label.maxValue"));
            gbc.gridwidth = 1;
            subPanel1.add(maxValueLabel, gbc);
            subPanel1.add(Box.createHorizontalStrut(10), gbc);
            gbc.gridwidth = GridBagConstraints.REMAINDER;
            gbc.fill = GridBagConstraints.NONE;
            maxValueField = new JTextField(10);
            subPanel1.add(maxValueField, gbc);
            panel.add("subPanel1", subPanel1);
            JPanel subPanel2 = new JPanel(gbl);
            gbc.gridwidth = 1;
            subPanel2.add(new JLabel(""), gbc);
            subPanel2.add(Box.createHorizontalStrut(10), gbc);
            gbc.gridwidth = GridBagConstraints.REMAINDER;
            gbc.fill = GridBagConstraints.NONE;
            subPanel2.add(new JLabel(""), gbc);
            gbc.gridwidth = 1;
            subPanel2.add(new JLabel(""), gbc);
            subPanel2.add(Box.createHorizontalStrut(10), gbc);
            gbc.gridwidth = GridBagConstraints.REMAINDER;
            gbc.fill = GridBagConstraints.NONE;
            subPanel2.add(new JLabel(""), gbc);
            panel.add("subPanel2", subPanel2);
            return panel;
        }

        /**
         * Help methode to add the minimum and maximum text field if 
         * atomic type of this <code>ParameterAtomicType<code> is
         * <code>unsigned</code> or <code>signed</code>.
         */
        private void toggleMinMaxFields() {
            if ((parameterAtomicType.getAtomicTypeNumber() == 1) || (parameterAtomicType.getAtomicTypeNumber() == 2)) {
                if (sizeField.getText().equals("")) {
                    minValueField.setEditable(false);
                    maxValueField.setEditable(false);
                } else {
                    minValueField.setEditable(true);
                    maxValueField.setEditable(true);
                    if (parameterAtomicType.getAtomicTypeNumber() == 1) {
                        int maxNumber = Integer.parseInt(sizeField.getText());
                        maxNumber = (int) Math.pow(2, maxNumber) - 1;
                        minValueField.setDocument(new IntegerDocument(0, maxNumber));
                        maxValueField.setDocument(new IntegerDocument(0, maxNumber));
                    } else if (parameterAtomicType.getAtomicTypeNumber() == 2) {
                        int minNumber = Integer.parseInt(sizeField.getText());
                        minNumber = (int) Math.pow(2, minNumber - 1);
                        minValueField.setDocument(new IntegerDocument(minNumber * -1, minNumber - 1));
                        maxValueField.setDocument(new IntegerDocument(minNumber * -1, minNumber - 1));
                    }
                }
                minMaxLayout.show(minMaxPanel, "subPanel1");
            } else minMaxLayout.show(minMaxPanel, "subPanel2");
        }

        /**
         * Proof if all necessary <code>ParameterType<\code> data was entered.
         *
         * @return <code>true<\code> if all necessary data was entered.
         */
        protected boolean allDataEntered() {
            final String DELIMITER = ", ";
            boolean entered = true;
            String message = resources.getString("dialog.message.enter");
            if (nameField.getText().equals("")) {
                entered = false;
                message += resources.getString("ptci.general.label.name");
            }
            if (parameterAtomicType == null) {
                if (!entered) message += DELIMITER + "\n" + resources.getString("ptci.general.label.atomicType"); else {
                    entered = false;
                    message += resources.getString("ptci.general.label.atomicType");
                }
            }
            if ((!(parameterAtomicType.getAtomicTypeNumber() == 0)) && (!(parameterAtomicType.getAtomicTypeNumber() == 6)) && (sizeField.getText().equals(""))) {
                if (!entered) message += DELIMITER + "\n" + resources.getString("ptci.general.label.size"); else {
                    entered = false;
                    message += resources.getString("ptci.general.label.size");
                }
            }
            if (lowAccess.getSelectedItem() == null) {
                if (!entered) message += DELIMITER + "\n" + resources.getString("ptci.general.label.lowAccess"); else {
                    entered = false;
                    message += resources.getString("ptci.general.label.lowAccess");
                }
            }
            if (highAccess.getSelectedItem() == null) {
                if (!entered) message += DELIMITER + "\n" + resources.getString("ptci.general.label.highAccess"); else {
                    entered = false;
                    message += resources.getString("ptci.general.label.highAccess");
                }
            }
            if ((parameterAtomicType.getAtomicTypeNumber() == 1) || (parameterAtomicType.getAtomicTypeNumber() == 2)) {
                if (minValueField.getText().equals("")) {
                    if (!entered) message += DELIMITER + "\n" + resources.getString("ptci.settings.label.minValue"); else {
                        entered = false;
                        message += resources.getString("ptci.settings.label.minValue");
                    }
                }
                if (maxValueField.getText().equals("")) {
                    if (!entered) message += DELIMITER + "\n" + resources.getString("ptci.settings.label.maxValue"); else {
                        entered = false;
                        message += resources.getString("ptci.settings.label.maxValue");
                    }
                }
            }
            if (!entered) JOptionPane.showMessageDialog(this, message, resources.getString("dialog.message.enter.title"), JOptionPane.ERROR_MESSAGE); else {
                entered = checkAccesses();
                if (entered && ((parameterAtomicType.getAtomicTypeNumber() == 1) || (parameterAtomicType.getAtomicTypeNumber() == 2))) {
                    entered = checkValues();
                }
            }
            return entered;
        }

        /**
         * Proof if the low access value is lower than the high acces value.
         *
         * @return <true> if low acess value is lower.
         */
        private boolean checkAccesses() {
            boolean lower = true;
            String message;
            if (lowAccess.getSelectedIndex() > highAccess.getSelectedIndex()) {
                lower = false;
                message = resources.getString("dialog.message.highAccess");
                JOptionPane.showMessageDialog(this, message, null, JOptionPane.ERROR_MESSAGE);
            }
            return lower;
        }

        /**
         * Proof if the minmum value is lower than the maximum value.
         *
         * @return <true> if minimum value is lower.
         */
        private boolean checkValues() {
            boolean lower = true;
            String message;
            int min, max;
            min = Integer.parseInt(minValueField.getText());
            max = Integer.parseInt(maxValueField.getText());
            if (min > max) {
                lower = false;
                message = resources.getString("dialog.message.maxValue");
                JOptionPane.showMessageDialog(this, message, null, JOptionPane.ERROR_MESSAGE);
            }
            return lower;
        }

        /**
         * Sets the data of the <code>ParameterType</code>.
         */
        protected void setObjectData() {
            parameterType.setParameterTypeId(Math.abs(this.hashCode()));
            parameterType.setParameterTypeName(nameField.getText());
            parameterType.setAtomicTypeNumber(parameterAtomicType.getAtomicTypeNumber());
            parameterType.setParameterAtomicType(parameterAtomicType);
            if ((parameterAtomicType.getAtomicTypeNumber() == 1) || (parameterAtomicType.getAtomicTypeNumber() == 2)) {
                parameterType.setParameterMinimumValue(Integer.parseInt(minValueField.getText()));
                parameterType.setParameterMaximumValue(Integer.parseInt(maxValueField.getText()));
            }
            parameterType.setParameterTypeSize(Integer.parseInt(sizeField.getText()));
            parameterType.setParameterTypeLowAccess(lowAccess.getSelectedIndex());
            parameterType.setParameterTypeHighAccess(highAccess.getSelectedIndex());
            Enumeration e = parameterListOfValues.elements();
            ParameterListOfValues listOfValues;
            while (e.hasMoreElements()) {
                listOfValues = (ParameterListOfValues) e.nextElement();
                listOfValues.setParameterTypeId(parameterType.getParameterTypeId());
                listOfValues.setParameterType(parameterType);
            }
            ParameterListOfValues[] listOfValuesArray = new ParameterListOfValues[parameterListOfValues.size()];
            Collection c = parameterListOfValues.values();
            c.toArray(listOfValuesArray);
            parameterType.setParameterListOfValues(listOfValuesArray);
            parameterType.setParameterTypeDescription(descriptionArea.getText());
        }

        /**
         * Implements abstract method of <code>DefaultCreationDialog</code>.
         * Creates a new <code>ParameterType</code> object and initializes 
         * it with the actual settings.
         * This object is inserted into the respective DesignBoxes. 
         * ProjectEvents are fired if necessary.
         */
        protected void insertObjectItem() {
            parameterType = new jcontrol.conect.data.ParameterType();
            setObjectData();
            ParameterTypeContainerItem newItem = (ParameterTypeContainerItem) ParameterTypeContainerItem.this.clone();
            newItem.setName(parameterType.getParameterTypeName());
            box.insertNode(newItem, dest);
        }

        public void actionPerformed(ActionEvent e) {
            if (e.getSource() == enumButton) {
                EnumDialog enumDialog = new EnumDialog(Integer.parseInt(sizeField.getText()));
            } else super.actionPerformed(e);
        }
    }

    /**
     * This dialog is used for specifying the parameters of the container-item
     * when modifying it.
     */
    protected class ModifyDialog extends CreateDialog {

        /** <code>true<\code> if icon schould be inserted into tree. */
        boolean insert = false;

        /** 
         * Initialize a new <code>ModifyDialog</code>.
         *
         * @param box    <code>DesignBox<\code>
         * @param dest   TreePath wehre Icon schould be added
         * @param insert <code>true<\code> if icon schould be inserted into \
         *               tree
         */
        protected ModifyDialog(DesignBox box, TreePath dest, boolean insert) {
            super(box, dest);
            this.insert = insert;
            setModifyData();
            dialogTitle = resources.getString("dialog.title.modify");
            setTitle(dialogTitle + parameterType.getParameterTypeName());
        }

        /** 
         * Initialize a new <code>ModifyDialog</code>.
         *
         * @param box <code>DesignBox<\code>
         */
        protected ModifyDialog(DesignBox box) {
            super();
            setBox(box);
            setModifyData();
            dialogTitle = resources.getString("dialog.title.modify");
            setTitle(dialogTitle + parameterType.getParameterTypeName());
        }

        /**
         * Sets the data of the <code>ParameterType</code> that schould be 
         * modified.
         */
        protected void setModifyData() {
            nameField.setText(parameterType.getParameterTypeName());
            ParameterAtomicType atomicType = parameterType.getParameterAtomicType();
            atomicTypeBox.setSelectedItem(atomicType);
            if ((atomicType.getAtomicTypeNumber() == 1) || (atomicType.getAtomicTypeNumber() == 2)) {
                minValueField.setText(String.valueOf(parameterType.getParameterMinimumValue()));
                maxValueField.setText(String.valueOf(parameterType.getParameterMaximumValue()));
            }
            sizeField.setText(String.valueOf(parameterType.getParameterTypeSize()));
            lowAccess.setSelectedIndex(parameterType.getParameterTypeLowAccess());
            highAccess.setSelectedIndex(parameterType.getParameterTypeHighAccess());
            descriptionArea.setText(parameterType.getParameterTypeDescription());
        }

        /**
         * Ends the dialog. If the <code>OK</code>-Button has been pressed
         * a new or modified <code>ContainerItem</code> is inserted to the 
         * design-box. Overrirdes methode <code>endDialog<\code> of
         * <code>CreationDialog.<\code>
         *
         * @param cancelled <code>true</code>, if dialog was cancelled.
         */
        protected void endDialog(boolean cancelled) {
            boolean error = false;
            if (cancelled) {
                setVisible(false);
                dispose();
                return;
            }
            setVisible(false);
            dispose();
            if (insert) super.endDialog(cancelled); else {
                setObjectData();
                ParameterTypeContainerItem.this.setName(parameterType.getParameterTypeName());
                box.updateTableEntry(ParameterTypeContainerItem.this);
            }
        }
    }

    /**
     * This Dialog is used to insert, delete and modify 
     * <code>ParameterListOfValues</code> for the <code>ParameterType</code>.
     */
    protected class EnumDialog extends JDialog implements ActionListener {

        /** <code>ResourceBundle<\code> for Dialog. */
        protected ResourceBundle resources = ManufacturerWorkSuite.getResources();

        /** <code>ConfigurableTable</code> to display 
         * <code>ParameterListOfValues</code>. */
        protected ConfigurableTable configurableTable;

        /** Button to edit <code>ParameterListOfValues</code>. */
        protected JButton editButton;

        /** Button to insert new <code>ParameterListOfValues</code>. */
        protected JButton newButton;

        /** Button to delete <code>ParameterListOfValues</code>. */
        protected JButton deleteButton;

        /** ok Button for dialog. */
        protected JButton okButton;

        /** cancel Button for dialog. */
        protected JButton cancelButton;

        /** Size for the <code> ParameterListOfValues</code>. */
        protected int size;

        /** max. display order. */
        protected int maxDisplayOrder;

        /** Entered <code>ParameterListOfValue</code>. */
        protected Hashtable tempParameterListOfValues;

        /**
         * Constructor.
         *
         * @param size size for the <code>ParameterListOfValues</code>.
         */
        protected EnumDialog(int size) {
            super();
            tempParameterListOfValues = (Hashtable) parameterListOfValues.clone();
            this.size = size;
            setTitle(resources.getString("ptci.enum.dialog.title"));
            initUi();
            pack();
            show();
        }

        /**
         * Inits the user interface.
         */
        protected void initUi() {
            JPanel panel = new JPanel(new BorderLayout());
            JPanel tablePanel = getTablePanel();
            panel.add(tablePanel, BorderLayout.CENTER);
            JPanel buttonPanel = getButtonPanel();
            panel.add(buttonPanel, BorderLayout.SOUTH);
            getContentPane().add(panel);
        }

        /**
         * Creates a panel with a <code>ConfigurableTable</code> where the
         * <code>ParameteristOfValues</code> displayed.
         */
        protected JPanel getTablePanel() {
            GridBagConstraints gbc = new GridBagConstraints();
            GridBagLayout gbl = new GridBagLayout();
            JPanel panel = new JPanel(gbl);
            panel.setBorder(BorderFactory.createEtchedBorder());
            SelectTableModel model = new SelectTableModel();
            TableHeaderDescriptor headerDescriptor = Constants.PARAMETER_LIST_OF_VALUES_TABLE;
            model.setColumnIdentifiers(headerDescriptor.getIds());
            DefaultListSelectionModel listSelectionModel = new DefaultListSelectionModel();
            listSelectionModel.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
            configurableTable = new ConfigurableTable(model, headerDescriptor);
            configurableTable.setDefaultRenderer(ParameterListOfValues.class, new ParameterListOfValuesCellRenderer());
            configurableTable.setSelectionModel(listSelectionModel);
            setTableEntries();
            JScrollPane scrollPane = new JScrollPane(configurableTable);
            scrollPane.setPreferredSize(new Dimension(headerDescriptor.getTotalWidth(), 200));
            gbc.anchor = GridBagConstraints.WEST;
            gbc.gridwidth = 1;
            gbc.gridheight = 10;
            panel.add(scrollPane, gbc);
            panel.add(Box.createHorizontalStrut(10), gbc);
            editButton = new JButton(resources.getString("ptci.enum.button.edit"));
            editButton.addActionListener(this);
            gbc.gridheight = 1;
            gbc.gridwidth = GridBagConstraints.REMAINDER;
            gbc.fill = GridBagConstraints.HORIZONTAL;
            panel.add(editButton, gbc);
            newButton = new JButton(resources.getString("ptci.enum.button.new"));
            newButton.addActionListener(this);
            panel.add(newButton, gbc);
            deleteButton = new JButton(resources.getString("ptci.enum.button.delete"));
            deleteButton.addActionListener(this);
            panel.add(deleteButton, gbc);
            toggleEditDeleteButton();
            return panel;
        }

        /**
         * Help methode to set the entries of the configurableTable.
         */
        protected void setTableEntries() {
            DefaultTableModel model = (DefaultTableModel) configurableTable.getModel();
            Hashtable values = tempParameterListOfValues;
            Enumeration e = values.elements();
            Vector row;
            ParameterListOfValues value;
            while (e.hasMoreElements()) {
                row = new Vector();
                value = (ParameterListOfValues) e.nextElement();
                row.add(Constants.parameterListOfValuesIcon);
                row.add(value);
                if (parameterAtomicType.getAtomicTypeNumber() == 4) {
                    row.add(String.valueOf(value.getRealValue()));
                } else if (parameterAtomicType.getAtomicTypeNumber() == 5) {
                    String hexString = "";
                    byte[] byteValue = value.getBinaryValue();
                    for (int i = 0; i < value.getBinaryValueLength(); i++) {
                        hexString += Integer.toHexString((byteValue[i] & 0xff) | 0x100).substring(1);
                    }
                    row.add(hexString);
                }
                row.add(String.valueOf(value.getDisplayOrder()));
                model.addRow(row);
            }
        }

        /**
         * Enable the <code>editButton</code> and <code>deleteButton</code>
         * if the <code>configurableTable</code> has got some entries.
         */
        protected void toggleEditDeleteButton() {
            if (configurableTable.getRowCount() > 0) {
                editButton.setEnabled(true);
                deleteButton.setEnabled(true);
            } else {
                editButton.setEnabled(false);
                deleteButton.setEnabled(false);
            }
        }

        /**
         * Creates a panel with buttons.
         */
        protected JPanel getButtonPanel() {
            JPanel panel = new JPanel();
            okButton = new JButton(resources.getString("dialog.button.ok"));
            okButton.addActionListener(this);
            panel.add(okButton);
            cancelButton = new JButton(resources.getString("dialog.button.cancel"));
            cancelButton.addActionListener(this);
            panel.add(cancelButton);
            return panel;
        }

        /**
         * Creates and inserts a new <code>ParameterListOfValues</code>.
         */
        protected void newParameterListOfValues() {
            ParameterListOfValues value = new ParameterListOfValues();
            value.setParameterValueId(value.hashCode());
            value.setDisplayOrder(maxDisplayOrder++);
            Random ran = new Random();
            while (parameterListOfValues.containsKey(new Integer(value.getParameterValueId()))) {
                int i = ran.nextInt();
                value.setParameterValueId(i);
            }
            boolean cancel = false;
            EditEnumDialog editEnum = new EditEnumDialog(value, size);
            if (value.getDisplayOrder() != -1) {
                tempParameterListOfValues.put(new Integer(value.getParameterValueId()), value);
                if (value.getDisplayOrder() > maxDisplayOrder) {
                    maxDisplayOrder = value.getDisplayOrder();
                }
                DefaultTableModel model = (DefaultTableModel) configurableTable.getModel();
                Vector row = new Vector();
                row.add(Constants.parameterListOfValuesIcon);
                row.add(value);
                if (parameterAtomicType.getAtomicTypeNumber() == 4) {
                    row.add(String.valueOf(value.getRealValue()));
                } else if (parameterAtomicType.getAtomicTypeNumber() == 5) {
                    String hexString = "";
                    byte[] byteValue = value.getBinaryValue();
                    for (int i = 0; i < value.getBinaryValueLength(); i++) {
                        hexString += Integer.toHexString((byteValue[i] & 0xff) | 0x100).substring(1);
                    }
                    row.add(hexString);
                }
                row.add(String.valueOf(value.getDisplayOrder()));
                model.addRow(row);
            }
            toggleEditDeleteButton();
        }

        /**
         * Help methode to edit selected <code>ParameterListOfValues</code>.
         */
        protected void editParameterListOfValues() {
            DefaultTableModel model = (DefaultTableModel) configurableTable.getModel();
            int selectedRow = configurableTable.getSelectedRow();
            if (selectedRow >= 0) {
                ParameterListOfValues value = (ParameterListOfValues) model.getValueAt(selectedRow, 1);
                boolean cancel = false;
                EditEnumDialog dialog = new EditEnumDialog(value, size);
                Vector row = new Vector();
                row.add(Constants.parameterListOfValuesIcon);
                row.add(value);
                if (parameterAtomicType.getAtomicTypeNumber() == 4) {
                    row.add(String.valueOf(value.getRealValue()));
                } else if (parameterAtomicType.getAtomicTypeNumber() == 5) {
                    String hexString = "";
                    byte[] byteValue = value.getBinaryValue();
                    for (int i = 0; i < value.getBinaryValueLength(); i++) {
                        hexString += Integer.toHexString((byteValue[i] & 0xff) | 0x100).substring(1);
                    }
                    row.add(hexString);
                }
                row.add(String.valueOf(value.getDisplayOrder()));
                model.removeRow(selectedRow);
                model.insertRow(selectedRow, row);
            }
        }

        /**
         * Deletes a <code>ParameterListOfValues</code>.
         */
        protected void deleteParameterListOfValues() {
            DefaultTableModel model = (DefaultTableModel) configurableTable.getModel();
            int selectedRow = configurableTable.getSelectedRow();
            if (selectedRow >= 0) {
                ParameterListOfValues value = (ParameterListOfValues) model.getValueAt(selectedRow, 1);
                model.removeRow(selectedRow);
                tempParameterListOfValues.remove(new Integer(value.getParameterValueId()));
            }
        }

        public void actionPerformed(ActionEvent e) {
            if (e.getSource() == okButton) {
                parameterListOfValues = tempParameterListOfValues;
                dispose();
            } else if (e.getSource() == cancelButton) dispose(); else if (e.getSource() == editButton) editParameterListOfValues(); else if (e.getSource() == newButton) newParameterListOfValues(); else if (e.getSource() == deleteButton) deleteParameterListOfValues();
        }

        /**
         * The <code>TableModel</code> is used for the tables to
         * select a <code>ParameterListOfVlaues</code>.
         */
        protected class SelectTableModel extends DefaultTableModel {

            public SelectTableModel() {
                super();
            }

            public SelectTableModel(Object[][] data, Object[] columnNames) {
                super(data, columnNames);
            }

            public Class getColumnClass(int col) {
                Vector v = (Vector) dataVector.elementAt(0);
                return v.elementAt(col).getClass();
            }

            public boolean isCellEditable(int row, int col) {
                return true;
            }
        }

        /**
         * The <code>ParameterListOfValuesCellRenderer</code> is used for 
         * displaying and rendering cells of ParameterListOfValues-columns.
         */
        class ParameterListOfValuesCellRenderer extends DefaultTableCellRenderer {

            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                JLabel label = (JLabel) super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                label.setText(((ParameterListOfValues) value).getDisplayValue());
                return label;
            }
        }

        /**
         * Dialog used to set values of <code>ParameterListOfValues</code>.
         */
        class EditEnumDialog extends JDialog implements ActionListener {

            /** The <code>ParameterListOfValues</code> that schould be edited.
             */
            ParameterListOfValues parameterListOfValues;

            /** Text field to enter the displayed value of the
             * <code>ParameterListOfValues</code>. */
            JTextField displayedValueField;

            /** Text field to enter the real value of the
             * <code>ParameterListOfValues</code>. */
            JTextField realValueField;

            /** Text field to enter the display order of the
             * <code>ParameterListOfValues</code>. */
            JTextField displayOrderField;

            /** OK button for dialog. */
            JButton okButton;

            /** Cancel button for dialog. */
            JButton cancelButton;

            /** The size of the <code>ParameterListofValues|/code>. */
            int size;

            /**
             * Constructor. Creates a Dialog to edit 
             * <code>ParameterListOfValues</code>
             *
             * @param parameterListOValues the 
             * <code>ParameterListOfValues<code> that schould be edited.
             * @param size size of <code>ParameterListOfValues</code>.
             */
            EditEnumDialog(ParameterListOfValues parameterListOfValues, int size) {
                super();
                this.setModal(true);
                this.size = size;
                this.setTitle(resources.getString("ptci.enumEdit.dialog.title"));
                this.parameterListOfValues = parameterListOfValues;
                initUi();
                this.pack();
                this.show();
            }

            /** Creates the panel where the values of the 
             * <code>ParameterListOfValues</code> can be set.
             */
            void initUi() {
                this.getContentPane().add(getMainPanel(), BorderLayout.CENTER);
                this.getContentPane().add(getButtonPanel(), BorderLayout.SOUTH);
            }

            /**
             * Creates a panel where settings for the 
             * <code>ParameterAtomicType</code> can be made.
             */
            protected JPanel getMainPanel() {
                GridBagConstraints gbc = new GridBagConstraints();
                GridBagLayout gbl = new GridBagLayout();
                JPanel panel = new JPanel(gbl);
                panel.setBorder(BorderFactory.createEtchedBorder());
                JLabel displayedValueLabel = new JLabel(resources.getString("ptci.enumEdit.label.displayedValue"));
                gbc.anchor = GridBagConstraints.WEST;
                gbc.gridwidth = 1;
                panel.add(displayedValueLabel, gbc);
                panel.add(Box.createHorizontalStrut(10));
                displayedValueField = new JTextField(parameterListOfValues.getDisplayValue(), 20);
                gbc.gridwidth = GridBagConstraints.REMAINDER;
                panel.add(displayedValueField, gbc);
                JLabel realValueLabel = new JLabel(resources.getString("ptci.enumEdit.label.realValue"));
                gbc.gridwidth = 1;
                panel.add(realValueLabel, gbc);
                panel.add(Box.createHorizontalStrut(10), gbc);
                realValueField = new JTextField(10);
                if (parameterAtomicType.getAtomicTypeNumber() == 4) {
                    realValueField.setDocument(new IntegerDocument());
                    realValueField.setText(String.valueOf(parameterListOfValues.getRealValue()));
                } else if (parameterAtomicType.getAtomicTypeNumber() == 5) {
                    realValueField.setDocument(new IntegerDocument(16));
                    String hexString = "";
                    byte[] binaryValue = parameterListOfValues.getBinaryValue();
                    if (binaryValue != null) {
                        for (int i = 0; i < size; i++) {
                            hexString += Integer.toHexString((binaryValue[i] & 0xff) | 0x100).substring(1);
                        }
                        realValueField.setText(hexString);
                    }
                }
                gbc.gridwidth = GridBagConstraints.REMAINDER;
                panel.add(realValueField, gbc);
                JLabel displayOrderLabel = new JLabel(resources.getString("ptci.enumEdit.label.displayOrder"));
                gbc.gridwidth = 1;
                panel.add(displayOrderLabel, gbc);
                panel.add(Box.createHorizontalStrut(10), gbc);
                displayOrderField = new JTextField(10);
                displayOrderField.setDocument(new IntegerDocument(0, 32767));
                displayOrderField.setText(String.valueOf(parameterListOfValues.getDisplayOrder()));
                displayOrderField.setText(String.valueOf(parameterListOfValues.getDisplayOrder()));
                gbc.gridwidth = GridBagConstraints.REMAINDER;
                panel.add(displayOrderField, gbc);
                return panel;
            }

            /**
             * Creates a panel with buttons for this dialog.
             */
            protected JPanel getButtonPanel() {
                JPanel panel = new JPanel();
                okButton = new JButton(resources.getString("dialog.button.ok"));
                okButton.addActionListener(this);
                panel.add(okButton);
                cancelButton = new JButton(resources.getString("dialog.button.cancel"));
                cancelButton.addActionListener(this);
                panel.add(cancelButton);
                return panel;
            }

            /**
             * Help methode to proof if all data entered.
             *
             * @return <code>true</code> if all neccesary data was entered.
             */
            protected boolean allDataEntered() {
                final String DELIMITER = ", ";
                boolean entered = true;
                String message = resources.getString("dialog.message.enter");
                if (displayedValueField.getText().equals("")) {
                    entered = false;
                    message += resources.getString("ptci.enumEdit.label.displayedValue");
                }
                if (realValueField.getText().equals("")) {
                    if (!entered) message += DELIMITER + "\n" + resources.getString("ptci.enumEdit.label.realValue"); else {
                        entered = false;
                        message += resources.getString("ptci.enumedit.label.realValue");
                    }
                }
                if (displayOrderField.getText().equals("")) {
                    if (!entered) message += DELIMITER + "\n" + resources.getString("ptci.enumEdit.label.displayOrder"); else {
                        entered = false;
                        message += resources.getString("ptci.enumedit.label.displayOrder");
                    }
                }
                if (!entered) JOptionPane.showMessageDialog(this, message, resources.getString("dialog.message.enter.title"), JOptionPane.ERROR_MESSAGE);
                return entered;
            }

            /**
             * Help methode to check if display order value is unique.
             *
             * @return <code>true</code> if display order value is unique.
             */
            protected boolean checkDisplayOrder() {
                boolean unique = true;
                Enumeration e = ParameterTypeContainerItem.this.parameterListOfValues.elements();
                ParameterListOfValues value;
                while (unique && e.hasMoreElements()) {
                    value = (ParameterListOfValues) e.nextElement();
                    if ((value.getDisplayOrder() == Integer.parseInt(displayOrderField.getText())) && value != parameterListOfValues) unique = false;
                }
                return unique;
            }

            public void actionPerformed(ActionEvent e) {
                if (e.getSource() == cancelButton) {
                    parameterListOfValues.setDisplayOrder(-1);
                } else if (e.getSource() == okButton) {
                    if (allDataEntered()) {
                        parameterListOfValues.setDisplayValue(displayedValueField.getText());
                        if (parameterAtomicType.getAtomicTypeNumber() == 5) {
                            if ((realValueField.getText().length() / 2) > size) {
                                JOptionPane.showMessageDialog(this, resources.getString("dialog.message.characters"), null, JOptionPane.ERROR_MESSAGE);
                                return;
                            } else {
                                String hexValue = realValueField.getText();
                                int length = hexValue.length();
                                for (int i = 0; i < ((size * 2) - length); i++) hexValue = "0" + hexValue;
                                byte[] bytes = new byte[size];
                                int index = 0;
                                for (int i = 0; i < size; i++) {
                                    bytes[i] = (byte) Integer.parseInt(hexValue.substring(index, index + 2), 16);
                                    index += 2;
                                }
                                parameterListOfValues.setBinaryValue(bytes);
                                parameterListOfValues.setBinaryValueLength(size);
                            }
                        } else {
                            parameterListOfValues.setRealValue(Integer.parseInt(realValueField.getText()));
                        }
                        if (checkDisplayOrder()) {
                            parameterListOfValues.setDisplayOrder(Integer.parseInt(displayOrderField.getText()));
                        } else {
                            JOptionPane.showMessageDialog(this, resources.getString("dialog.message.displayOrder"), null, JOptionPane.ERROR_MESSAGE);
                            return;
                        }
                    } else {
                        return;
                    }
                }
                this.dispose();
            }
        }
    }
}
