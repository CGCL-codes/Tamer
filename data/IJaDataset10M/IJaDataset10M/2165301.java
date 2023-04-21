package freets.gui.manufacturer;

import freets.gui.design.*;
import freets.data.*;
import freets.tools.*;
import freets.data.settings.*;
import freets.data.database.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;
import javax.swing.tree.*;
import javax.swing.table.*;
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
public class PeiContainerItem extends AbstractContainerItem {

    /** The <code>DragDropItem</code> for the ProductContainerItem. */
    protected DragDropItem source;

    /** Reference to the <code>ApplicationProgram</code> belonging to this 
     * container.*/
    protected ApplicationProgram applicationProgram;

    /** Referenc to the <code>Mask<code> that belongs to this program. */
    protected Mask mask;

    /** Reference to the <code>Symbol</code> belonging to the 
     * <code>HwProduct</code>. */
    protected Symbol symbol;

    /**
     * Constructs a new <code>PeiContainerItem</code> object.
     *
     * @param source the <code>DragDropItem</code> for the ContainerItem
     * @param leafContainer <code>true</code>, if this container shall be a 
     *                      leaf-container
     * @param rootContainer <code>true</code>, if this container shall be a 
     *                      root-container
     */
    public PeiContainerItem(DragDropItem source, boolean rootContainer, boolean leafContainer) {
        super(source, rootContainer, leafContainer);
        this.source = source;
    }

    /**
     * Make a deep copy of this object.
     * @return a copy of <code>this</code> 
     */
    public Object clone() {
        PeiContainerItem clone = new PeiContainerItem(source, rootContainer, leafContainer);
        clone.setApplicationProgram(applicationProgram);
        clone.setMask(mask);
        clone.setSymbol(symbol);
        return clone;
    }

    /**
     * Compares one PeiContainerItem to another. 
     * Two item are equal if all there attributes are equal.
     *
     * @param o an object to compare this item to
     * @return <code>true</code>, if both items are equal
     */
    public boolean equals(Object o) {
        if (!(o instanceof PeiContainerItem)) return false;
        PeiContainerItem ci = (PeiContainerItem) o;
        if (ci.getApplicationProgram() != this.getApplicationProgram()) return false;
        return true;
    }

    /**
     * Get the value of applicationProgram.
     *
     * @return Value of applicationProgram.
     */
    public ApplicationProgram getApplicationProgram() {
        return applicationProgram;
    }

    /**
     * Set the value of applicationProgram.
     *
     * @param v  Value to assign to applicationProgram.
     */
    public void setApplicationProgram(ApplicationProgram v) {
        this.applicationProgram = v;
    }

    /**
     * Get the value of mask.
     * @return Value of mask.
     */
    public Mask getMask() {
        return mask;
    }

    /**
     * Set the value of mask.
     * @param v  Value to assign to mask.
     */
    public void setMask(Mask v) {
        this.mask = v;
    }

    /**
     * Get the value of symbol.
     * @return Value of symbol.
     */
    public Symbol getSymbol() {
        return symbol;
    }

    /**
     * Set the value of symbol.
     * @param v  Value to assign to symbol.
     */
    public void setSymbol(Symbol v) {
        this.symbol = v;
    }

    /**
     * Gets the <code>DragDropItem</code> object of this 
     * <code>PeiContainerItem</code>.
     *
     * @return the <code>DragDropItem</code> object of this 
     *         <code>ApplicationContainerItem</code>.
     */
    public DragDropItem getDragDropItem() {
        return source;
    }

    /**
     */
    public Object[] getTableRowArray() {
        Vector row = new Vector();
        row.addElement(this);
        row.addElement(applicationProgram.getProgramName());
        row.addElement(new Integer(applicationProgram.getDeviceType()));
        row.addElement(applicationProgram.getProgramVersionNumber());
        row.addElement(new Boolean(applicationProgram.getLinkable()));
        row.addElement(applicationProgram.getMask().getMaskVersionName());
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
        if (applicationProgram != null) {
            buf.append(applicationProgram.toString());
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

        /** Text field for the program name. */
        protected JTextField nameField;

        /** Combo box to select mask. */
        protected JComboBox maskVersion;

        /** Panel for the bcu style radio buttons. */
        protected JPanel bcuStylePanel;

        /** Radio button for bcu 1 style. */
        protected JRadioButton bcu1StyleButton;

        /** Radio button for bcu 2 style. */
        protected JRadioButton bcu2StyleButton;

        /** CardLayout for the bcuStylePanel. */
        protected CardLayout cardLayout;

        /** Check box for the linkable flag. */
        protected JCheckBox linkableFlag;

        /** Text field for the program version. */
        protected JTextField versionField;

        /** Label to display address of program version. */
        protected JLabel programVersionAddress;

        /** Text field for the device type. */
        protected JTextField deviceField;

        /** Label to display address of device type. */
        protected JLabel deviceTypeAddress;

        /** Combo box for the pei type. */
        protected JComboBox peiTypeBox;

        /** Text area for description of the 
         * <code>ApplicationProgram</code>. */
        protected JTextArea descriptionArea;

        /** Panel for general settings. */
        protected JPanel generalPanel;

        /** Panel for the description. */
        protected JPanel descriptionPanel;

        /** <code>ConfigurableTable</code> with symbols. */
        ConfigurableTable symbolTable;

        /** ScrollPane with icon of selected <code>Symbol</code>. */
        JScrollPane symbolScrollPane;

        /** <code>ConfigurableTable</code> with fixup functions. */
        ConfigurableTable fixupTable;

        JButton editButton;

        JButton insertButton;

        JButton deleteButton;

        /** Tabb for settings. */
        protected JTabbedPane tabbedPane;

        JPanel fixupsPanel;

        /** 
         * Initialize a new <code>CreateDialog</code>.
         *
         * @param box  <code>DesignBox<\code>
         * @param dest TreePath wehre Icon schould be added
         */
        protected CreateDialog(DesignBox box, TreePath dest) {
            super(box, dest);
            setTitle(dialogTitle + PeiContainerItem.this.getType());
            initialize();
            pack();
            tabbedPane.remove(tabbedPane.indexOfTab(resources.getString("api.tabb.fixup")));
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
            tabbedPane = new JTabbedPane();
            generalPanel = getGeneralPanel();
            tabbedPane.addTab(resources.getString("api.tabb.general"), generalPanel);
            descriptionPanel = getDescriptionPanel();
            tabbedPane.addTab(resources.getString("api.tabb.description"), descriptionPanel);
            JPanel symbolsPanel = getSymbolPanel();
            tabbedPane.addTab(resources.getString("api.tabb.symbol"), symbolsPanel);
            fixupsPanel = getFixupPanel();
            tabbedPane.addTab(resources.getString("api.tabb.fixup"), fixupsPanel);
            getContentPane().add(tabbedPane);
        }

        /**
         * Creates a panel where general settings for the 
         * <code>ApplicationProgram</code> can be set.
         *
         * @return a panel for settings.
         */
        protected JPanel getGeneralPanel() {
            GridBagConstraints gbc = new GridBagConstraints();
            GridBagLayout gbl = new GridBagLayout();
            JPanel panel = new JPanel(gbl);
            panel.setBorder(BorderFactory.createEtchedBorder());
            gbc.anchor = GridBagConstraints.WEST;
            gbc.gridwidth = 1;
            gbc.gridheight = 2;
            symbolScrollPane = new JScrollPane();
            if (symbol != null) {
                JLabel iconLabel = new JLabel(freets.gui.GeneralWorkSuite.createImageIcon(symbol.getSymbolFilename()));
                symbolScrollPane.setViewportView(iconLabel);
            }
            symbolScrollPane.setPreferredSize(new Dimension(40, 40));
            panel.add(symbolScrollPane, gbc);
            panel.add(Box.createHorizontalStrut(10), gbc);
            JLabel nameLabel = new JLabel(resources.getString("api.general.progName.label"));
            gbc.gridheight = 1;
            panel.add(nameLabel, gbc);
            panel.add(Box.createHorizontalStrut(10), gbc);
            nameField = new JTextField(35);
            gbc.gridwidth = GridBagConstraints.REMAINDER;
            gbc.fill = GridBagConstraints.HORIZONTAL;
            panel.add(nameField, gbc);
            gbc.fill = GridBagConstraints.NONE;
            panel.add(Box.createVerticalStrut(15), gbc);
            gbc.gridwidth = 1;
            gbc.gridheight = 2;
            panel.add(new JLabel(), gbc);
            panel.add(Box.createHorizontalStrut(10), gbc);
            JLabel maskVersionLabel = new JLabel(resources.getString("api.general.maskVersion.label"));
            panel.add(maskVersionLabel, gbc);
            panel.add(Box.createHorizontalStrut(10), gbc);
            maskVersion = new JComboBox();
            maskVersion.setRenderer(new DefaultListCellRenderer() {

                public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
                    JLabel label = (JLabel) super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                    Mask mask = (Mask) value;
                    String versionName = mask.getMaskVersionName();
                    label.setText(versionName + " ($" + Integer.toHexString(mask.getMaskVersion()) + ")");
                    return label;
                }
            });
            Hashtable masks = Settings.getMasks();
            maskVersion.addItem(masks.get(new Integer(498)));
            maskVersion.addItem(masks.get(new Integer(58132)));
            maskVersion.setSelectedIndex(0);
            gbc.fill = GridBagConstraints.HORIZONTAL;
            panel.add(maskVersion, gbc);
            panel.add(Box.createHorizontalStrut(10));
            gbc.fill = GridBagConstraints.NONE;
            gbc.gridwidth = GridBagConstraints.REMAINDER;
            gbc.gridheight = 2;
            cardLayout = new CardLayout();
            bcuStylePanel = new JPanel(cardLayout);
            JPanel radioPanel = new JPanel(gbl);
            gbc.gridwidth = GridBagConstraints.REMAINDER;
            ButtonGroup buttonGroup = new ButtonGroup();
            bcu1StyleButton = new JRadioButton(resources.getString("api.general.button.bcu1"));
            bcu1StyleButton.setSelected(true);
            bcu2StyleButton = new JRadioButton(resources.getString("api.general.button.bcu2"));
            buttonGroup.add(bcu1StyleButton);
            buttonGroup.add(bcu2StyleButton);
            radioPanel.add(bcu1StyleButton, gbc);
            radioPanel.add(bcu2StyleButton, gbc);
            bcuStylePanel.add("radioPanel", radioPanel);
            bcuStylePanel.add("dummyPanel", new JPanel());
            cardLayout.show(bcuStylePanel, "dummyPanel");
            panel.add(bcuStylePanel, gbc);
            gbc.gridwidth = 1;
            gbc.gridheight = 1;
            panel.add(new JLabel(), gbc);
            panel.add(Box.createHorizontalStrut(10), gbc);
            panel.add(new JLabel(), gbc);
            panel.add(Box.createHorizontalStrut(10), gbc);
            linkableFlag = new JCheckBox(resources.getString("api.general.linkable.label"));
            gbc.gridwidth = GridBagConstraints.REMAINDER;
            linkableFlag.addActionListener(new ActionListener() {

                public void actionPerformed(ActionEvent e) {
                    if (linkableFlag.isSelected()) {
                        tabbedPane.addTab(resources.getString("api.tabb.fixup"), fixupsPanel);
                        tabbedPane.invalidate();
                    } else {
                        tabbedPane.remove(tabbedPane.indexOfTab(resources.getString("api.tabb.fixup")));
                    }
                }
            });
            panel.add(linkableFlag, gbc);
            panel.add(Box.createVerticalStrut(15), gbc);
            gbc.gridwidth = 1;
            panel.add(new JLabel(), gbc);
            panel.add(Box.createHorizontalStrut(10), gbc);
            JLabel programVersionLabel = new JLabel(resources.getString("api.general.progVersion.label"));
            gbc.fill = GridBagConstraints.HORIZONTAL;
            panel.add(programVersionLabel, gbc);
            panel.add(Box.createHorizontalStrut(10), gbc);
            versionField = new JTextField(25);
            versionField.setDocument(new DoubleDocument(0.0, 15.15));
            panel.add(versionField, gbc);
            gbc.gridwidth = GridBagConstraints.REMAINDER;
            programVersionAddress = new JLabel();
            panel.add(programVersionAddress, gbc);
            gbc.gridwidth = 1;
            panel.add(new JLabel(), gbc);
            panel.add(Box.createHorizontalStrut(10), gbc);
            JLabel deviceTypeLabel = new JLabel(resources.getString("api.general.deviceType.label"));
            gbc.fill = GridBagConstraints.NONE;
            panel.add(deviceTypeLabel, gbc);
            panel.add(Box.createHorizontalStrut(10), gbc);
            deviceField = new JTextField(20);
            deviceField.setDocument(new IntegerDocument(0, 65535));
            gbc.fill = GridBagConstraints.HORIZONTAL;
            panel.add(deviceField, gbc);
            gbc.gridwidth = GridBagConstraints.REMAINDER;
            gbc.fill = GridBagConstraints.NONE;
            deviceTypeAddress = new JLabel();
            panel.add(deviceTypeAddress, gbc);
            maskVersion.addItemListener(new ItemListener() {

                public void itemStateChanged(ItemEvent e) {
                    Mask mask = (Mask) e.getItem();
                    switch(mask.getMaskId()) {
                        case 499:
                            programVersionAddress.setText(resources.getString("api.general.eeprom.prog"));
                            deviceTypeAddress.setText(resources.getString("api.general.eeprom.dev"));
                            cardLayout.show(bcuStylePanel, "radioPanel");
                            break;
                        case 113:
                        case 250:
                        case 497:
                        case 701:
                        case 703:
                        case 2034:
                        case 2320:
                        case 2321:
                            cardLayout.show(bcuStylePanel, "dummyPanel");
                            programVersionAddress.setText(resources.getString("api.general.eeprom.prog"));
                            deviceTypeAddress.setText(resources.getString("api.general.eeprom.dev"));
                            break;
                        default:
                            programVersionAddress.setText("");
                            deviceTypeAddress.setText("");
                            break;
                    }
                }
            });
            panel.add(Box.createVerticalStrut(15), gbc);
            gbc.gridwidth = 1;
            panel.add(new JLabel(), gbc);
            panel.add(Box.createHorizontalStrut(10), gbc);
            panel.add(new JLabel(), gbc);
            panel.add(Box.createHorizontalStrut(10), gbc);
            JLabel peiTypeHeader = new JLabel(resources.getString("api.general.nr.label") + "    " + resources.getString("api.general.peiName.label"));
            gbc.gridwidth = GridBagConstraints.REMAINDER;
            panel.add(peiTypeHeader, gbc);
            gbc.gridwidth = 1;
            panel.add(new JLabel(), gbc);
            panel.add(Box.createHorizontalStrut(10), gbc);
            JLabel peiTypeLabel = new JLabel(resources.getString("api.general.peiType.label"));
            panel.add(peiTypeLabel, gbc);
            panel.add(Box.createHorizontalStrut(10), gbc);
            Collection c = Settings.getPeiTypes().values();
            PeiType[] peiTypes = new PeiType[c.size()];
            c.toArray(peiTypes);
            Arrays.sort(peiTypes, new Comparator() {

                public int compare(Object o1, Object o2) {
                    int comp = 0;
                    if ((((PeiType) o1).getPeiTypeId()) < (((PeiType) o2).getPeiTypeId())) comp = -1; else if ((((PeiType) o1).getPeiTypeId()) == (((PeiType) o2).getPeiTypeId())) comp = 0; else if ((((PeiType) o1).getPeiTypeId()) > (((PeiType) o2).getPeiTypeId())) comp = 1;
                    return comp;
                }
            });
            peiTypeBox = new JComboBox(peiTypes);
            peiTypeBox.setRenderer(new BasicComboBoxRenderer() {

                public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
                    super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                    String n = (value == null) ? "" : new String(((PeiType) value).getPeiTypeId() + "    " + ((PeiType) value).getPeiTypeName());
                    setText((value == null) ? "" : (n.length() > 40) ? n.substring(0, 40) + "..." : n);
                    return this;
                }
            });
            gbc.gridwidth = GridBagConstraints.REMAINDER;
            gbc.fill = GridBagConstraints.HORIZONTAL;
            panel.add(peiTypeBox, gbc);
            return panel;
        }

        /**
         * Creates a panel where the description for the 
         * <code>ApplicationProgram</code> can be set.
         *
         * @return a panel for settings.
         */
        private JPanel getDescriptionPanel() {
            GridBagConstraints gbc = new GridBagConstraints();
            GridBagLayout gbl = new GridBagLayout();
            JPanel panel = new JPanel(gbl);
            panel.setBorder(BorderFactory.createEtchedBorder());
            descriptionArea = new JTextArea(100, 100);
            JScrollPane scrollPane = new JScrollPane(descriptionArea);
            scrollPane.setPreferredSize(generalPanel.getPreferredSize());
            gbc.anchor = GridBagConstraints.WEST;
            gbc.gridwidth = GridBagConstraints.REMAINDER;
            gbc.fill = GridBagConstraints.BOTH;
            panel.add(scrollPane, gbc);
            return panel;
        }

        /**
         * Panel to select a <code>Symbol</code> for the 
         * <code>ApplicationProgram</code>.
         *
         * @return a panel with symbols.
         */
        private JPanel getSymbolPanel() {
            GridBagConstraints gbc = new GridBagConstraints();
            GridBagLayout gbl = new GridBagLayout();
            JPanel panel = new JPanel(gbl);
            panel.setBorder(BorderFactory.createEtchedBorder());
            gbc.anchor = GridBagConstraints.WEST;
            gbc.gridwidth = GridBagConstraints.REMAINDER;
            Hashtable symbols = Settings.getSymbols();
            Collection c = symbols.values();
            Symbol[] symbolArray = new Symbol[symbols.size()];
            c.toArray(symbolArray);
            Arrays.sort(symbolArray);
            Object[][] values = new Object[symbolArray.length][2];
            for (int i = 0; i < symbolArray.length; i++) {
                values[i][0] = freets.gui.GeneralWorkSuite.createImageIcon(symbolArray[i].getSymbolFilename());
                values[i][1] = symbolArray[i];
            }
            TableHeaderDescriptor symbolDescriptor = Constants.SYMBOL_SELECT;
            ItemTableModel model = new ItemTableModel(values, symbolDescriptor.getIds());
            DefaultListSelectionModel listSelectionModel = new DefaultListSelectionModel();
            listSelectionModel.addListSelectionListener(new ListSelectionListener() {

                public void valueChanged(ListSelectionEvent e) {
                    int selectedRow = symbolTable.getSelectedRow();
                    symbol = (Symbol) symbolTable.getValueAt(selectedRow, 1);
                    JLabel iconLabel = new JLabel(freets.gui.GeneralWorkSuite.createImageIcon(symbol.getSymbolFilename()));
                    symbolScrollPane.setViewportView(iconLabel);
                }
            });
            listSelectionModel.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
            symbolTable = new ConfigurableTable(model, symbolDescriptor);
            symbolTable.setSelectionModel(listSelectionModel);
            symbolTable.setDefaultRenderer(Symbol.class, new DefaultTableCellRenderer() {

                public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                    JLabel label = (JLabel) super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                    label.setText(((Symbol) value).getSymbolName());
                    return label;
                }
            });
            symbolTable.setShowHorizontalLines(false);
            JScrollPane tablePane = new JScrollPane(symbolTable);
            tablePane.setPreferredSize(new Dimension(symbolDescriptor.getTotalWidth() + 10, 200));
            panel.add(tablePane, gbc);
            return panel;
        }

        /**
         * Put <code>AddressFixup</code>s of <code>ApplicationProgram</code>
         * into the <code>ConfigurableTable</code>.
         */
        private void setTableEntries() {
            if (applicationProgram != null) {
                AddressFixup[] addressFixups = applicationProgram.getAddressFixups();
                DefaultTableModel model = (DefaultTableModel) fixupTable.getModel();
                Object[] row = new Object[3];
                for (int i = 0; i < addressFixups.length; i++) {
                    row[0] = Constants.parameterAddressIcon;
                    row[1] = addressFixups[i];
                    row[2] = new Integer(addressFixups[i].getFixupAddress());
                    model.addRow(row);
                }
            }
        }

        /**
         * Panel to select <code>AddressFixup</code>s for the 
         * <code>ApplicationProgram</code>.
         *
         * @return a panel with fixups.
         */
        private JPanel getFixupPanel() {
            GridBagConstraints gbc = new GridBagConstraints();
            GridBagLayout gbl = new GridBagLayout();
            JPanel panel = new JPanel(gbl);
            panel.setBorder(BorderFactory.createEtchedBorder());
            gbc.gridwidth = GridBagConstraints.REMAINDER;
            gbc.anchor = GridBagConstraints.WEST;
            JLabel fixupLabel = new JLabel(resources.getString("api.fixup.function.label"));
            panel.add(fixupLabel, gbc);
            gbc.gridwidth = 1;
            ItemTableModel model = new ItemTableModel();
            TableHeaderDescriptor headerDescriptor = Constants.FIXUP_FUNCTION_TABLE;
            model.setColumnIdentifiers(headerDescriptor.getIds());
            DefaultListSelectionModel listSelectionModel = new DefaultListSelectionModel();
            listSelectionModel.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
            fixupTable = new ConfigurableTable(model, headerDescriptor);
            fixupTable.setDefaultRenderer(AddressFixup.class, new DefaultTableCellRenderer() {

                public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                    JLabel label = (JLabel) super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                    label.setText(((AddressFixup) value).getFixupName());
                    return label;
                }
            });
            fixupTable.setSelectionModel(listSelectionModel);
            setTableEntries();
            JScrollPane scrollPane = new JScrollPane(fixupTable);
            scrollPane.setPreferredSize(new Dimension(headerDescriptor.getTotalWidth() + 5, 200));
            gbc.anchor = GridBagConstraints.WEST;
            gbc.fill = GridBagConstraints.VERTICAL;
            gbc.gridwidth = 1;
            gbc.gridheight = 10;
            panel.add(scrollPane, gbc);
            panel.add(Box.createHorizontalStrut(10), gbc);
            editButton = new JButton(resources.getString("api.fixup.button.edit"));
            editButton.addActionListener(this);
            gbc.gridheight = 1;
            gbc.gridwidth = GridBagConstraints.REMAINDER;
            gbc.fill = GridBagConstraints.HORIZONTAL;
            panel.add(editButton, gbc);
            insertButton = new JButton(resources.getString("api.fixup.button.insert"));
            insertButton.addActionListener(this);
            panel.add(insertButton, gbc);
            deleteButton = new JButton(resources.getString("api.fixup.button.delete"));
            deleteButton.addActionListener(this);
            panel.add(deleteButton, gbc);
            toggleEditDeleteButton();
            return panel;
        }

        /**
         * Enable the <code>editButton</code> and <code>deleteButton</code>
         * if the <code>ConfigurableTable</code> with 
         * <code>AddressFixup</code>s has got some entries.
         */
        protected void toggleEditDeleteButton() {
            if (fixupTable.getRowCount() > 0) {
                editButton.setEnabled(true);
                deleteButton.setEnabled(true);
            } else {
                editButton.setEnabled(false);
                deleteButton.setEnabled(false);
            }
        }

        /**
         * The <code>TableModel</code> is used for the tables to
         * select a <code>Symbol</code> or <code>AddressFixup</code>.
         */
        class ItemTableModel extends DefaultTableModel {

            public ItemTableModel() {
                super();
            }

            public ItemTableModel(Object[][] data, Object[] columnNames) {
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
         * Proof if all necessary <code>ApplicationProgram<\code> data was 
         * entered.
         *
         * @return <code>true<\code> if all necessary data was entered.
         */
        protected boolean allDataEntered() {
            final String DELIMITER = ", ";
            boolean entered = true;
            String message = resources.getString("dialog.message.enter");
            if (nameField.getText().equals("")) {
                entered = false;
                message += resources.getString("api.general.progName.label");
            }
            if (maskVersion.getSelectedItem() == null) {
                if (!entered) message += DELIMITER + "\n" + resources.getString("api.general.maskVersion.label"); else {
                    entered = false;
                    message += resources.getString("api.general.maskVersion.label");
                }
            }
            if (peiTypeBox.getSelectedItem() == null) {
                if (!entered) message += DELIMITER + "\n" + resources.getString("api.general.peiType.label"); else {
                    entered = false;
                    message += resources.getString("api.general.peiType.label");
                }
            }
            if (versionField.getText().equals("")) {
                if (!entered) message += DELIMITER + "\n" + resources.getString("api.general.progVersion.label"); else {
                    entered = false;
                    message += resources.getString("api.general.progVersion.label");
                }
            }
            if (deviceField.getText().equals("")) {
                if (!entered) message += DELIMITER + "\n" + resources.getString("api.general.deviceType.label"); else {
                    entered = false;
                    message += resources.getString("api.general.deviceType.label");
                }
            }
            if (!entered) JOptionPane.showMessageDialog(this, message, resources.getString("dialog.message.enter.title"), JOptionPane.ERROR_MESSAGE);
            return entered;
        }

        /**
         * Sets the data of the <code>ApplicationProgram</code>.
         */
        protected void setObjectData() {
            applicationProgram.setProgramId(Math.abs(this.hashCode()));
            ManufacturerToolDataManager dm = ManufacturerToolDataManager.getDefaultDataManager();
            applicationProgram.setManufacturer(dm.data.manufacturer);
            applicationProgram.setManufacturerId(applicationProgram.getManufacturer().getManufacturerId());
            if (symbol != null) {
                applicationProgram.setSymbolId(symbol.getSymbolId());
                applicationProgram.setSymbol(symbol);
            }
            applicationProgram.setMask((Mask) maskVersion.getSelectedItem());
            applicationProgram.setMaskId(applicationProgram.getMask().getMaskId());
            AddressFixup[] addressFixups;
            int rows = fixupTable.getRowCount();
            addressFixups = new AddressFixup[rows];
            for (int i = 0; i < rows; i++) {
                addressFixups[i] = (AddressFixup) fixupTable.getValueAt(i, 1);
            }
            applicationProgram.setAddressFixups(addressFixups);
            applicationProgram.setProgramName(nameField.getText());
            applicationProgram.setProgramVersion(versionField.getText());
            applicationProgram.setLinkable(linkableFlag.isSelected());
            applicationProgram.setDeviceType(Integer.parseInt(deviceField.getText()));
            applicationProgram.setPeiType(((PeiType) peiTypeBox.getSelectedItem()).getPeiTypeId());
            applicationProgram.setDescription(descriptionArea.getText());
            applicationProgram.setDynamicManagement(true);
            applicationProgram.setProgramType(1);
            applicationProgram.setProgramStyle(0);
        }

        /**
         * Implements abstract method of <code>DefaultCreationDialog</code>.
         * Creates a new <code>ApplicationProgram</code> object and 
         * initializes it with the actual settings.
         * This object is inserted into the respective DesignBoxes. 
         * ProjectEvents are fired if necessary.
         */
        protected void insertObjectItem() {
            applicationProgram = new ApplicationProgram();
            setObjectData();
            PeiContainerItem newItem = (PeiContainerItem) PeiContainerItem.this.clone();
            newItem.setName(applicationProgram.getProgramName());
            box.insertNode(newItem, dest);
        }

        public void actionPerformed(ActionEvent e) {
            if (e.getSource() == insertButton) {
                AddressFixupSelectDialog selectDialog = new AddressFixupSelectDialog(this);
                selectDialog.show();
            } else if (e.getSource() == deleteButton) {
                DefaultTableModel model = (DefaultTableModel) fixupTable.getModel();
                int selectedRow = fixupTable.getSelectedRow();
                if (selectedRow >= 0) {
                    model.removeRow(selectedRow);
                }
                toggleEditDeleteButton();
            } else if (e.getSource() == editButton) {
                DefaultTableModel model = (DefaultTableModel) fixupTable.getModel();
                int selectedRow = fixupTable.getSelectedRow();
                if (selectedRow >= 0) {
                    AddressFixup value = (AddressFixup) model.getValueAt(selectedRow, 1);
                    AddressFixupSelectDialog selectDialog = new AddressFixupSelectDialog(this, value);
                    selectDialog.show();
                }
            } else super.actionPerformed(e);
        }

        /**
         * Inner class to select <code>AddressFixup</code>s for an 
         * <code>ApplicationProgram</code>.
         */
        protected class AddressFixupSelectDialog extends JDialog implements ActionListener {

            /** <code>ComboBox</code> with selected fixup function. */
            protected JComboBox functionBox;

            /** TextField to display eeprom address of fixup function. */
            protected JTextField eepromAddressField;

            /** TextField to enter new address for fixup function. */
            protected JTextField addressField;

            /** OK button. */
            protected JButton okButton;

            /** Cancel button. */
            protected JButton cancelButton;

            /** The <code>AddressFixup</code> that schould be edited. */
            protected AddressFixup addressFixup;

            /**
             * Default constructor.
             *
             * @param owner The owner of this dialog.
             */
            protected AddressFixupSelectDialog(JDialog owner) {
                super(owner);
                this.addressFixup = null;
                initUI();
                this.pack();
            }

            /**
             * Constructor to edit an <code>AddressFixup</code>.
             *
             * @param owner The owner of this dialog.
             * @param editFixup <code>AddressFixup that schould be edited.
             */
            protected AddressFixupSelectDialog(JDialog owner, AddressFixup editFixup) {
                this(owner);
                this.addressFixup = editFixup;
                Vector functions = ((Mask) maskVersion.getSelectedItem()).getMaskEntries();
                Enumeration e = functions.elements();
                boolean found = false;
                MaskEntry entry = null;
                while (e.hasMoreElements() && !found) {
                    entry = (MaskEntry) e.nextElement();
                    if (entry.getMaskEntryId() == this.addressFixup.getFixupType()) found = true;
                }
                if (found) functionBox.setSelectedItem(entry);
                addressField.setText(String.valueOf(this.addressFixup.getFixupAddress()));
            }

            /**
             * Create the user interface.
             */
            protected void initUI() {
                Container contentPane = this.getContentPane();
                contentPane.add(getMainPanel(), BorderLayout.CENTER);
                contentPane.add(getButtonPanel(), BorderLayout.SOUTH);
            }

            /**
             * Panel where <code>AddressFixup</code>s can be selected.
             */
            protected JPanel getMainPanel() {
                GridBagConstraints gbc = new GridBagConstraints();
                GridBagLayout gbl = new GridBagLayout();
                JPanel panel = new JPanel(gbl);
                panel.setBorder(BorderFactory.createEtchedBorder());
                JLabel programNameLabel = new JLabel(resources.getString("api.fixup.dialog.progName"));
                gbc.anchor = GridBagConstraints.WEST;
                gbc.gridwidth = 1;
                panel.add(programNameLabel, gbc);
                panel.add(Box.createHorizontalStrut(10), gbc);
                JTextField programNameField = new JTextField(25);
                programNameField.setText(nameField.getText());
                programNameField.setEditable(false);
                gbc.gridwidth = GridBagConstraints.REMAINDER;
                panel.add(programNameField, gbc);
                JLabel eepromAddressLabel = new JLabel(resources.getString("api.fixup.dialog.eepromAddress"));
                gbc.gridwidth = 1;
                panel.add(eepromAddressLabel, gbc);
                panel.add(Box.createHorizontalStrut(10), gbc);
                eepromAddressField = new JTextField(10);
                eepromAddressField.setEditable(false);
                gbc.gridwidth = GridBagConstraints.REMAINDER;
                panel.add(eepromAddressField, gbc);
                JLabel functionNameLabel = new JLabel(resources.getString("api.fixup.dialog.functionName"));
                gbc.gridwidth = 1;
                panel.add(functionNameLabel, gbc);
                panel.add(Box.createHorizontalStrut(10), gbc);
                Vector functions = ((Mask) maskVersion.getSelectedItem()).getMaskEntries();
                MaskEntry[] functionArray = new MaskEntry[functions.size()];
                functions.toArray(functionArray);
                Arrays.sort(functionArray);
                functionBox = new JComboBox(functionArray);
                functionBox.setRenderer(new DefaultListCellRenderer() {

                    public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
                        if (value != null) {
                            JLabel label = (JLabel) super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                            label.setText(((MaskEntry) value).getMaskEntryName());
                            return label;
                        } else return new JLabel();
                    }
                });
                functionBox.addItemListener(new ItemListener() {

                    public void itemStateChanged(ItemEvent e) {
                        MaskEntry entry = (MaskEntry) ((JComboBox) e.getSource()).getSelectedItem();
                        eepromAddressField.setText(String.valueOf(entry.getMaskEntryAddress()));
                    }
                });
                gbc.gridwidth = GridBagConstraints.REMAINDER;
                panel.add(functionBox, gbc);
                JLabel addressLabel = new JLabel(resources.getString("api.fixup.dialog.address"));
                gbc.gridwidth = 1;
                panel.add(addressLabel, gbc);
                panel.add(Box.createHorizontalStrut(10), gbc);
                addressField = new JTextField(10);
                addressField.setDocument(new IntegerDocument(-32767, 32767));
                addressField.setText("0");
                gbc.gridwidth = GridBagConstraints.REMAINDER;
                panel.add(addressField, gbc);
                return panel;
            }

            /**
             * Panel with buttons.
             */
            protected JPanel getButtonPanel() {
                JPanel panel = new JPanel();
                okButton = new JButton(resources.getString("dialog.button.ok"));
                okButton.addActionListener(this);
                cancelButton = new JButton(resources.getString("dialog.button.cancel"));
                cancelButton.addActionListener(this);
                panel.add(okButton);
                panel.add(cancelButton);
                return panel;
            }

            public void actionPerformed(ActionEvent e) {
                if (e.getSource() == okButton) {
                    if (functionBox.getSelectedItem() == null) {
                        JOptionPane.showMessageDialog(this, resources.getString("api.fixup.dialog.error.message"), resources.getString("api.fixup.dialog.error.title"), JOptionPane.ERROR_MESSAGE);
                    } else {
                        MaskEntry currentFixup = (MaskEntry) functionBox.getSelectedItem();
                        AddressFixup tmpAddressFixup = new AddressFixup();
                        tmpAddressFixup.setFixupType(currentFixup.getMaskEntryId());
                        tmpAddressFixup.setFixupName(currentFixup.getMaskEntryName());
                        tmpAddressFixup.setFixupAddress(Integer.parseInt(addressField.getText()));
                        DefaultTableModel model = (DefaultTableModel) fixupTable.getModel();
                        if (addressFixup == null) {
                            tmpAddressFixup.setFixupId(tmpAddressFixup.hashCode());
                            Vector row = new Vector();
                            row.addElement(Constants.parameterAddressIcon);
                            row.addElement(tmpAddressFixup);
                            row.addElement(new Integer(tmpAddressFixup.getFixupAddress()));
                            model.addRow(row);
                            addressFixup = tmpAddressFixup;
                        } else {
                            addressFixup = tmpAddressFixup;
                            int selectedRow = fixupTable.getSelectedRow();
                            model.setValueAt(addressFixup, selectedRow, 1);
                            model.setValueAt(new Integer(addressFixup.getFixupAddress()), selectedRow, 2);
                        }
                    }
                    this.dispose();
                } else if (e.getSource() == cancelButton) {
                    this.dispose();
                }
                toggleEditDeleteButton();
            }
        }
    }

    /**
     * This dialog is used for specifying the parameters of the container-item
     * when modifying it.
     */
    protected class ModifyDialog extends CreateDialog {

        boolean insert;

        /** 
         * Initialize a new <code>ModifyDialog</code>.
         *
         * @param box    <code>DesignBox<\code>
         * @param dest   TreePath wehre Icon schould be added
         * @param insert <code>true<\code> if icon schould be inserted into \
         *               tree
         */
        public ModifyDialog(DesignBox box, TreePath dest, boolean insert) {
            super(box, dest);
            this.insert = insert;
            setModifyData();
            dialogTitle = resources.getString("dialog.title.modify");
            setTitle(dialogTitle + applicationProgram.getProgramName());
        }

        /** 
         * Initialize a new <code>ModifyDialog</code>.
         *
         * @param box <code>DesignBox<\code>
         */
        public ModifyDialog(DesignBox box) {
            super();
            setBox(box);
            setModifyData();
            dialogTitle = resources.getString("dialog.title.modify");
            setTitle(dialogTitle + applicationProgram.getProgramName());
        }

        /**
         * Sets the data of the <code>ApplicationProgram</code> that schould 
         * be modified.
         */
        void setModifyData() {
            nameField.setText(applicationProgram.getProgramName());
            maskVersion.setSelectedItem(applicationProgram.getMask());
            if (applicationProgram.getLinkable()) linkableFlag.doClick();
            versionField.setText(applicationProgram.getProgramVersion());
            deviceField.setText(String.valueOf(applicationProgram.getDeviceType()));
            PeiType peiType = (PeiType) Settings.getPeiTypes().get(new Integer(applicationProgram.getPeiType()));
            peiTypeBox.setSelectedItem(peiType);
            descriptionArea.setText(applicationProgram.getDescription());
            if (applicationProgram.getSymbol() != null) {
                symbol = applicationProgram.getSymbol();
                boolean symbolFound = false;
                int rows = symbolTable.getRowCount();
                int currentRow = 0;
                while (!symbolFound && currentRow < rows) {
                    Symbol tmpSymbol = (Symbol) symbolTable.getValueAt(currentRow, 1);
                    if (tmpSymbol == symbol) {
                        symbolTable.setRowSelectionInterval(currentRow, currentRow);
                        symbolFound = true;
                    } else {
                        currentRow++;
                    }
                }
            }
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
                this$0.setName(applicationProgram.getProgramName());
                box.updateTableEntry(PeiContainerItem.this);
            }
        }
    }
}
