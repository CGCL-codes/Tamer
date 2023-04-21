package com.hifiremote.jp1;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.util.ArrayList;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.DefaultComboBoxModel;
import javax.swing.DefaultListCellRenderer;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JFormattedTextField;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.ScrollPaneConstants;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.text.NumberFormatter;

/**
 * The Class SpecialFunctionDialog.
 */
public class SpecialFunctionDialog extends JDialog implements ActionListener, FocusListener, ItemListener, ListSelectionListener {

    /**
   * Show dialog.
   * 
   * @param frame
   *          the frame
   * @param function
   *          the function
   * @param config
   *          the config
   * @return the special protocol function
   */
    public static SpecialProtocolFunction showDialog(JFrame frame, SpecialProtocolFunction function, RemoteConfiguration config) {
        if (dialog == null) dialog = new SpecialFunctionDialog(frame);
        dialog.setRemoteConfiguration(config);
        dialog.setFunction(function);
        dialog.pack();
        dialog.setLocationRelativeTo(frame);
        dialog.setVisible(true);
        return dialog.function;
    }

    public static void reset() {
        if (dialog != null) {
            dialog.dispose();
            dialog = null;
        }
    }

    /**
   * Adds the to box.
   * 
   * @param j
   *          the j
   * @param c
   *          the c
   */
    private void addToBox(JComponent j, Container c) {
        j.setAlignmentX(Component.LEFT_ALIGNMENT);
        c.add(j);
    }

    /**
   * Instantiates a new special function dialog.
   * 
   * @param frame
   *          the frame
   */
    private SpecialFunctionDialog(JFrame frame) {
        super(frame, "Special Function", true);
        JComponent contentPane = (JComponent) getContentPane();
        contentPane.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        contentPane.setLayout(new BoxLayout(contentPane, BoxLayout.PAGE_AXIS));
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 0));
        addToBox(panel, contentPane);
        panel.setBorder(BorderFactory.createTitledBorder("Bound Key"));
        panel.add(new JLabel("Device:"));
        panel.add(boundDevice);
        panel.add(Box.createHorizontalStrut(5));
        panel.add(new JLabel("Key:"));
        panel.add(boundKey);
        boundKey.addActionListener(this);
        shift.addActionListener(this);
        panel.add(shift);
        xShift.addActionListener(this);
        panel.add(xShift);
        paramPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        paramPanel.add(Box.createHorizontalStrut(5));
        addToBox(paramPanel, contentPane);
        paramPanel.setBorder(BorderFactory.createTitledBorder("Parameters"));
        Box box = Box.createVerticalBox();
        addToBox(new JLabel("Type:"), box);
        addToBox(type, box);
        type.setRenderer(new FunctionTypeRenderer());
        type.addActionListener(this);
        paramPanel.add(box);
        paramPanel.add(parameterCard);
        panel = new JPanel();
        parameterCard.add(panel, "Empty");
        panel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        parameterCard.add(panel, "ModeName");
        box = Box.createVerticalBox();
        addToBox(new JLabel("Text:"), box);
        addToBox(modeName, box);
        panel.add(box);
        panel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        parameterCard.add(panel, "Multiplex");
        box = Box.createVerticalBox();
        addToBox(new JLabel("Device Type:"), box);
        addToBox(deviceType, box);
        panel.add(box);
        DecimalFormat format = new DecimalFormat("0000");
        format.setParseIntegerOnly(true);
        format.setGroupingUsed(false);
        NumberFormatter formatter = new NumberFormatter(format);
        formatter.setValueClass(Integer.class);
        formatter.setMinimum(new Integer(0));
        formatter.setMaximum(new Integer(2047));
        setupCode = new JFormattedTextField(formatter);
        setupCode.setColumns(4);
        FocusSelector.selectOnFocus(setupCode);
        box = Box.createVerticalBox();
        addToBox(new JLabel("Setup Code:"), box);
        addToBox(setupCode, box);
        panel.add(box);
        panel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        parameterCard.add(panel, "Duration");
        box = Box.createVerticalBox();
        addToBox(new JLabel("Duration:"), box);
        addToBox(duration, box);
        panel.add(box);
        panel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        parameterCard.add(panel, "ToadTog");
        box = Box.createVerticalBox();
        addToBox(new JLabel("Toggle #:"), box);
        Integer[] toggles = { 0, 1, 2, 3, 4, 5, 6, 7 };
        toggle = new JComboBox(toggles);
        addToBox(toggle, box);
        panel.add(box);
        box = Box.createVerticalBox();
        addToBox(new JLabel("Condition:"), box);
        addToBox(condition, box);
        condition.addActionListener(this);
        panel.add(box);
        panel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        parameterCard.add(panel, "Pause");
        box = Box.createVerticalBox();
        addToBox(new JLabel("Duration:"), box);
        Box durationBox = Box.createHorizontalBox();
        formatter = new NumberFormatter(PauseFunction.pauseFormat);
        formatter.setValueClass(Float.class);
        pauseDuration = new PauseField(formatter);
        durationBox.add(pauseDuration);
        durationBox.add(Box.createHorizontalStrut(5));
        durationBox.add(new JLabel("secs"));
        addToBox(durationBox, box);
        panel.add(box);
        noteBox = Box.createVerticalBox();
        JLabel label = new JLabel("Note: A decimal point is");
        Font font = label.getFont();
        Font font2 = font.deriveFont(font.getSize2D() - 1);
        label.setFont(font2);
        noteBox.add(label);
        label = new JLabel("allowed in the duration");
        label.setFont(font2);
        noteBox.add(label);
        label = new JLabel("value");
        label.setFont(font2);
        noteBox.add(label);
        panel.add(Box.createHorizontalStrut(10));
        panel.add(noteBox);
        paramPanel.add(Box.createVerticalStrut(noteBox.getPreferredSize().height));
        panel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        parameterCard.add(panel, "UDSM");
        box = Box.createVerticalBox();
        addToBox(new JLabel("Key w/ Macro:"), box);
        addToBox(macroKey, box);
        panel.add(box);
        macroKey.setRenderer(keyCodeRenderer);
        panel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        parameterCard.add(panel, "ULDKP");
        box = Box.createVerticalBox();
        addToBox(new JLabel("Duration:"), box);
        Integer[] durations = { 0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15 };
        uldkpDuration = new JComboBox(durations);
        addToBox(uldkpDuration, box);
        panel.add(box);
        box = Box.createVerticalBox();
        addToBox(firstKeyLabel, box);
        addToBox(firstMacroKey, box);
        panel.add(box);
        firstMacroKey.setRenderer(keyCodeRenderer);
        box = Box.createVerticalBox();
        addToBox(secondKeyLabel, box);
        addToBox(secondMacroKey, box);
        panel.add(box);
        secondMacroKey.setRenderer(keyCodeRenderer);
        macroBox = Box.createHorizontalBox();
        macroBox.setBorder(BorderFactory.createTitledBorder("Macro Definition:"));
        addToBox(macroBox, contentPane);
        JPanel availableBox = new JPanel(new BorderLayout());
        macroBox.add(availableBox);
        availableBox.add(availableLabel, BorderLayout.NORTH);
        availableButtons.setFixedCellWidth(100);
        availableBox.add(new JScrollPane(availableButtons), BorderLayout.CENTER);
        availableButtons.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        availableButtons.addListSelectionListener(this);
        activeColor = availableButtons.getBackground();
        availableButtons.setEnabled(false);
        disabledColor = availableButtons.getBackground();
        inactiveColor = new Color(216, 228, 248);
        panel = new JPanel(new GridLayout(3, 2, 2, 2));
        availableBox.add(panel, BorderLayout.SOUTH);
        add.addActionListener(this);
        panel.add(add);
        insert.addActionListener(this);
        panel.add(insert);
        addShift.addActionListener(this);
        panel.add(addShift);
        insertShift.addActionListener(this);
        panel.add(insertShift);
        addXShift.addActionListener(this);
        panel.add(addXShift);
        insertXShift.addActionListener(this);
        panel.add(insertXShift);
        macroBox.add(Box.createHorizontalStrut(10));
        firstKeysPanel = new JPanel(new BorderLayout());
        macroBox.add(firstKeysPanel);
        firstKeysPanel.add(firstMacroLabel, BorderLayout.NORTH);
        firstMacroButtons.setFixedCellWidth(100);
        firstKeysPanel.add(new JScrollPane(firstMacroButtons), BorderLayout.CENTER);
        firstMacroButtons.setModel(new DefaultListModel());
        firstMacroButtons.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        firstMacroButtons.setCellRenderer(macroButtonRenderer);
        firstMacroButtons.addListSelectionListener(this);
        firstMacroButtons.addFocusListener(this);
        JPanel buttonBox = new JPanel(new GridLayout(3, 2, 2, 2));
        firstKeysPanel.add(buttonBox, BorderLayout.SOUTH);
        firstMoveUp.addActionListener(this);
        buttonBox.add(firstMoveUp);
        firstMoveDown.addActionListener(this);
        buttonBox.add(firstMoveDown);
        firstRemove.addActionListener(this);
        buttonBox.add(firstRemove);
        firstClear.addActionListener(this);
        buttonBox.add(firstClear);
        macroBox.add(Box.createHorizontalStrut(10));
        secondKeysPanel = new JPanel(new BorderLayout());
        macroBox.add(secondKeysPanel);
        secondKeysPanel.add(secondMacroLabel, BorderLayout.NORTH);
        secondMacroButtons.setFixedCellWidth(100);
        secondKeysPanel.add(new JScrollPane(secondMacroButtons), BorderLayout.CENTER);
        secondMacroButtons.setModel(new DefaultListModel());
        secondMacroButtons.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        secondMacroButtons.setCellRenderer(macroButtonRenderer);
        secondMacroButtons.addListSelectionListener(this);
        secondMacroButtons.addFocusListener(this);
        buttonBox = new JPanel(new GridLayout(3, 2, 2, 2));
        secondKeysPanel.add(buttonBox, BorderLayout.SOUTH);
        secondMoveUp.addActionListener(this);
        buttonBox.add(secondMoveUp);
        secondMoveDown.addActionListener(this);
        buttonBox.add(secondMoveDown);
        secondRemove.addActionListener(this);
        buttonBox.add(secondRemove);
        secondClear.addActionListener(this);
        buttonBox.add(secondClear);
        panel = new JPanel(new BorderLayout());
        addToBox(panel, contentPane);
        panel.setBorder(BorderFactory.createTitledBorder("Notes"));
        notes.setLineWrap(true);
        panel.add(new JScrollPane(notes, ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS, ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER));
        panel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        addToBox(panel, contentPane);
        okButton.addActionListener(this);
        panel.add(okButton);
        cancelButton.addActionListener(this);
        panel.add(cancelButton);
    }

    /**
   * Sets the remote configuration.
   * 
   * @param config
   *          the new remote configuration
   */
    private void setRemoteConfiguration(RemoteConfiguration config) {
        this.config = config;
        Remote remote = config.getRemote();
        shift.setText(remote.getShiftLabel());
        xShift.setText(remote.getXShiftLabel());
        xShift.setVisible(remote.getXShiftEnabled());
        boundDevice.setModel(new DefaultComboBoxModel(remote.getDeviceButtons()));
        boundKey.setModel(new DefaultComboBoxModel(remote.getMacroButtons()));
        deviceType.setModel(new DefaultComboBoxModel(remote.getDeviceTypes()));
        java.util.List<String> specialFunctionsByUserName = new ArrayList<String>();
        for (SpecialProtocol sp : config.getSpecialProtocols()) {
            for (int i = 0; i < sp.getFunctions().length; i++) {
                specialFunctionsByUserName.add(sp.getUserFunctions()[i]);
                specialFunctionsByRDFName.add(sp.getFunctions()[i]);
            }
        }
        type.setModel(new DefaultComboBoxModel(specialFunctionsByUserName.toArray()));
        keyCodeRenderer.setRemote(remote);
        java.util.List<Integer> macroKeys = new ArrayList<Integer>();
        for (Macro macro : config.getMacros()) macroKeys.add(new Integer(macro.getKeyCode()));
        DefaultComboBoxModel model = new DefaultComboBoxModel(macroKeys.toArray());
        macroKey.setModel(model);
        firstMacroKey.setModel(model);
        secondMacroKey.setModel(model);
        java.util.List<Button> buttons = remote.getButtons();
        DefaultListModel listModel = new DefaultListModel();
        for (Button b : buttons) {
            if (b.canAssignToMacro() || b.canAssignShiftedToMacro() || b.canAssignXShiftedToMacro()) {
                listModel.addElement(b);
            }
        }
        availableButtons.setModel(listModel);
        macroButtonRenderer.setRemote(remote);
    }

    /**
   * Sets the function.
   * 
   * @param function
   *          the new function
   */
    private void setFunction(SpecialProtocolFunction function) {
        this.function = null;
        if (function == null) {
            cmd = null;
            boundDevice.setSelectedIndex(-1);
            boundKey.setSelectedIndex(-1);
            shift.setSelected(false);
            xShift.setSelected(false);
            if (type.getItemCount() > 0) {
                type.setSelectedIndex(0);
            }
            notes.setText("");
            setupCode.setValue(null);
            setDuration(0);
            setPauseDuration(null);
            setULDKPDuration(0);
            setModeName("");
            setToggle(0);
            setCondition(0);
            ((DefaultListModel) firstMacroButtons.getModel()).clear();
            ((DefaultListModel) secondMacroButtons.getModel()).clear();
            return;
        }
        cmd = function.getCmd();
        if (cmd != null) cmd = new Hex(cmd);
        boundDevice.setSelectedIndex(function.getDeviceButtonIndex());
        shift.setSelected(false);
        xShift.setSelected(false);
        setButton(function.getKeyCode(), boundKey, shift, xShift);
        type.setSelectedItem(function.getType(config));
        function.update(this);
        notes.setText(function.getNotes());
    }

    /**
   * Sets the button.
   * 
   * @param code
   *          the code
   * @param comboBox
   *          the combo box
   * @param shiftBox
   *          the shift box
   * @param xShiftBox
   *          the x shift box
   */
    private void setButton(int code, JComboBox comboBox, JCheckBox shiftBox, JCheckBox xShiftBox) {
        Remote remote = config.getRemote();
        Button b = remote.getButton(code);
        if (b == null) {
            int base = code & 0x3F;
            if (base != 0) {
                b = remote.getButton(base);
                if ((base | remote.getShiftMask()) == code) {
                    shiftBox.setEnabled(b.allowsShiftedMacro());
                    shiftBox.setSelected(true);
                    comboBox.setSelectedItem(b);
                    return;
                }
                if (remote.getXShiftEnabled() && ((base | remote.getXShiftMask()) == code)) {
                    xShiftBox.setEnabled(remote.getXShiftEnabled() & b.allowsXShiftedMacro());
                    xShiftBox.setSelected(true);
                    comboBox.setSelectedItem(b);
                    return;
                }
            }
            b = remote.getButton(code & ~remote.getShiftMask());
            if (b != null) shiftBox.setSelected(true); else if (remote.getXShiftEnabled()) {
                b = remote.getButton(code ^ ~remote.getXShiftMask());
                if (b != null) xShiftBox.setSelected(true);
            }
        }
        shiftBox.setEnabled(b.allowsShiftedKeyMove());
        xShiftBox.setEnabled(b.allowsXShiftedKeyMove());
        if (b.getIsXShifted()) xShiftBox.setSelected(true); else if (b.getIsShifted()) shiftBox.setSelected(true);
        comboBox.removeActionListener(this);
        comboBox.setSelectedItem(b);
        comboBox.addActionListener(this);
    }

    /**
   * Gets the key code.
   * 
   * @param comboBox
   *          the combo box
   * @param shiftBox
   *          the shift box
   * @param xShiftBox
   *          the x shift box
   * @return the key code
   */
    private int getKeyCode(JComboBox comboBox, JCheckBox shiftBox, JCheckBox xShiftBox) {
        int keyCode = ((Button) comboBox.getSelectedItem()).getKeyCode();
        if (shiftBox.isSelected()) keyCode |= config.getRemote().getShiftMask(); else if (xShiftBox.isSelected()) keyCode |= config.getRemote().getXShiftMask();
        return keyCode;
    }

    /**
   * Show warning.
   * 
   * @param message
   *          the message
   */
    private void showWarning(String message) {
        JOptionPane.showMessageDialog(this, message, "Missing Information", JOptionPane.ERROR_MESSAGE);
    }

    public void actionPerformed(ActionEvent event) {
        Object source = event.getSource();
        Remote remote = config.getRemote();
        Button b = (Button) boundKey.getSelectedItem();
        if (source == type) {
            String typeStr = specialFunctionsByRDFName.get(type.getSelectedIndex());
            String cardStr = typeStr;
            if (typeStr.equals("DKP")) {
                cardStr = "Duration";
                enableMacros(true);
                firstMacroLabel.setText("Single keys:");
                secondMacroLabel.setText("Double keys:");
            } else if (typeStr.equals("DSM")) {
                cardStr = "Empty";
                enableMacros(true, false);
                setTarget(firstMacroButtons);
                firstMacroLabel.setText("Keys:");
            } else if (typeStr.equals("LKP")) {
                cardStr = "Duration";
                enableMacros(true);
                firstMacroLabel.setText("Short keys:");
                secondMacroLabel.setText("Long keys:");
            } else if (typeStr.equals("Pause")) {
                cardStr = "Pause";
                enableMacros(false);
            } else if (typeStr.equals("ToadTog")) {
                enableMacros(true);
                int i = condition.getSelectedIndex();
                if (i == -1) i = 0;
                condition.setSelectedIndex(i);
            } else if (typeStr.equals("ULKP")) {
                cardStr = "ULDKP";
                firstKeyLabel.setText("Short Key:");
                secondKeyLabel.setText("Long Key:");
                enableMacros(false);
            } else if (typeStr.equals("UDKP")) {
                cardStr = "ULDKP";
                firstKeyLabel.setText("Single Key:");
                secondKeyLabel.setText("Double Key:");
                enableMacros(false);
            } else enableMacros(false);
            CardLayout cl = (CardLayout) parameterCard.getLayout();
            cl.show(parameterCard, cardStr);
            noteBox.setVisible(typeStr.equals("Pause"));
        } else if (source == condition) {
            int i = condition.getSelectedIndex();
            firstMacroLabel.setText(ToadTogFunction.onStrings[i]);
            secondMacroLabel.setText(ToadTogFunction.offStrings[i]);
        } else if (source == okButton) {
            int deviceIndex = boundDevice.getSelectedIndex();
            if (deviceIndex == -1) {
                showWarning("You must select a device for the bound key.");
                return;
            }
            if (boundKey.getSelectedItem() == null) {
                showWarning("You must select a key for the bound key.");
                return;
            }
            int keyCode = getKeyCode(boundKey, shift, xShift);
            String typeStr = (String) type.getSelectedItem();
            SpecialProtocol protocol = null;
            for (SpecialProtocol sp : config.getSpecialProtocols()) {
                for (String func : sp.getUserFunctions()) {
                    if (func.equals(typeStr)) {
                        protocol = sp;
                        break;
                    }
                }
                if (protocol != null) break;
            }
            if (protocol == null) {
                showWarning("No special protocol found for type " + typeStr);
                return;
            }
            Hex hex = protocol.createHex(this);
            if (hex == null) {
                return;
            }
            if (protocol.isInternal()) {
                Macro macro = new Macro(keyCode, hex, notes.getText());
                macro.setSequenceNumber(protocol.getInternalSerial());
                macro.setDeviceIndex(deviceIndex);
                function = protocol.createFunction(macro);
            } else {
                DeviceType devType = null;
                int setupCode = 0;
                if (protocol.getDeviceType() != null) {
                    devType = protocol.getDeviceType();
                    setupCode = protocol.getSetupCode();
                } else {
                    DeviceUpgrade deviceUpgrade = protocol.getDeviceUpgrade(config.getDeviceUpgrades());
                    devType = deviceUpgrade.getDeviceType();
                    setupCode = deviceUpgrade.getSetupCode();
                }
                KeyMove km = new KeyMove(keyCode, deviceIndex, devType.getNumber(), setupCode, hex, notes.getText());
                function = protocol.createFunction(km);
            }
            if (function == null) return;
            setVisible(false);
        } else if (source == cancelButton) {
            function = null;
            setVisible(false);
        } else if (source == shift) {
            if (shift.isSelected()) {
                xShift.setSelected(false);
            } else if (b != null && remote.getXShiftEnabled()) {
                xShift.setSelected(b.needsShift(Button.MACRO_BIND));
            }
        } else if (source == xShift) {
            if (xShift.isSelected()) {
                shift.setSelected(false);
            } else if (b != null) {
                shift.setSelected(b.needsShift(Button.MACRO_BIND));
            }
        } else if (source == boundKey) {
            if (b != null) {
                b.setShiftBoxes(Button.MACRO_BIND, shift, xShift);
            }
        } else if (source == add) {
            addKey(0);
        } else if (source == insert) {
            insertKey(0);
        } else if (source == addShift) {
            addKey(remote.getShiftMask());
        } else if (source == insertShift) {
            insertKey(remote.getShiftMask());
        } else if (source == addXShift) {
            addKey(remote.getXShiftMask());
        } else if (source == insertXShift) {
            insertKey(remote.getXShiftMask());
        } else if ((source == firstMoveUp) || (source == secondMoveUp)) {
            JList list = firstMacroButtons;
            if (source == secondMoveUp) list = secondMacroButtons;
            int index = list.getSelectedIndex();
            swap(list, index, index - 1);
        } else if ((source == firstMoveDown) || (source == secondMoveDown)) {
            JList list = firstMacroButtons;
            if (source == secondMoveDown) list = secondMacroButtons;
            int index = list.getSelectedIndex();
            swap(list, index, index + 1);
        } else if ((source == firstRemove) || (source == secondRemove)) {
            JList list = firstMacroButtons;
            if (source == secondRemove) list = secondMacroButtons;
            int index = list.getSelectedIndex();
            DefaultListModel model = (DefaultListModel) list.getModel();
            model.removeElementAt(index);
            int last = model.getSize() - 1;
            if (index > last) index = last;
            list.setSelectedIndex(index);
        } else if (source == firstClear) {
            ((DefaultListModel) firstMacroButtons.getModel()).clear();
        } else if (source == secondClear) {
            ((DefaultListModel) secondMacroButtons.getModel()).clear();
        }
    }

    /**
   * Gets the selected key code.
   * 
   * @return the selected key code
   */
    private int getSelectedKeyCode() {
        return ((Button) availableButtons.getSelectedValue()).getKeyCode();
    }

    /**
   * Adds the key.
   * 
   * @param mask
   *          the mask
   */
    private void addKey(int mask) {
        Integer value = new Integer(getSelectedKeyCode() | mask);
        ((DefaultListModel) targetList.getModel()).addElement(value);
    }

    /**
   * Insert key.
   * 
   * @param mask
   *          the mask
   */
    private void insertKey(int mask) {
        Integer value = new Integer(getSelectedKeyCode() | mask);
        int index = targetList.getSelectedIndex();
        DefaultListModel model = (DefaultListModel) targetList.getModel();
        if (index == -1) model.add(0, value); else model.add(index, value);
        targetList.setSelectedIndex(index + 1);
        targetList.ensureIndexIsVisible(index + 1);
    }

    /**
   * Swap.
   * 
   * @param list
   *          the list
   * @param index1
   *          the index1
   * @param index2
   *          the index2
   */
    private void swap(JList list, int index1, int index2) {
        DefaultListModel model = (DefaultListModel) list.getModel();
        Object o1 = model.get(index1);
        Object o2 = model.get(index2);
        model.set(index1, o2);
        model.set(index2, o1);
        list.setSelectedIndex(index2);
        list.ensureIndexIsVisible(index2);
    }

    /**
   * Enable macros.
   * 
   * @param flag
   *          the flag
   */
    private void enableMacros(boolean flag) {
        enableMacros(flag, flag);
    }

    /**
   * Enable macros.
   * 
   * @param mainFlag
   *          the main flag
   * @param secondFlag
   *          the second flag
   */
    private void enableMacros(boolean mainFlag, boolean secondFlag) {
        if (!mainFlag) secondFlag = false;
        macroBox.setEnabled(mainFlag);
        availableLabel.setEnabled(mainFlag);
        availableButtons.setEnabled(mainFlag);
        add.setEnabled(mainFlag);
        insert.setEnabled(mainFlag);
        addShift.setEnabled(mainFlag);
        insertShift.setEnabled(mainFlag);
        addXShift.setEnabled(mainFlag);
        insertXShift.setEnabled(mainFlag);
        firstKeysPanel.setEnabled(mainFlag);
        firstMacroLabel.setEnabled(mainFlag);
        firstMacroButtons.setEnabled(mainFlag);
        firstMoveUp.setEnabled(mainFlag);
        firstMoveDown.setEnabled(mainFlag);
        firstRemove.setEnabled(mainFlag);
        firstClear.setEnabled(mainFlag);
        secondKeysPanel.setEnabled(secondFlag);
        secondMacroLabel.setEnabled(secondFlag);
        secondMacroButtons.setEnabled(secondFlag);
        secondMoveUp.setEnabled(secondFlag);
        secondMoveDown.setEnabled(secondFlag);
        secondRemove.setEnabled(secondFlag);
        secondClear.setEnabled(secondFlag);
        if (firstMacroButtons.isEnabled()) {
            targetList = firstMacroButtons;
            targetList.setBackground(activeColor);
        } else firstMacroButtons.setBackground(disabledColor);
        if (secondMacroButtons.isEnabled()) secondMacroButtons.setBackground(inactiveColor); else secondMacroButtons.setBackground(disabledColor);
    }

    public void itemStateChanged(ItemEvent e) {
        if (e.getStateChange() != ItemEvent.SELECTED) return;
    }

    public void focusGained(FocusEvent e) {
        Object source = e.getSource();
        setTarget((JList) source);
    }

    /**
   * Sets the target.
   * 
   * @param list
   *          the new target
   */
    private void setTarget(JList list) {
        if ((targetList != list) && targetList.isEnabled()) targetList.setBackground(inactiveColor);
        targetList = list;
        targetList.setBackground(activeColor);
    }

    public void focusLost(FocusEvent e) {
    }

    public void valueChanged(ListSelectionEvent e) {
        if (e.getValueIsAdjusting()) return;
        enableButtons();
    }

    /**
   * Enable buttons.
   */
    private void enableButtons() {
        int limit = 15;
        if (config.getRemote().getAdvCodeBindFormat() == AdvancedCode.BindFormat.LONG) limit = 255;
        DefaultListModel listModel = (DefaultListModel) firstMacroButtons.getModel();
        boolean moreRoom = listModel.getSize() < limit;
        Button b = (Button) availableButtons.getSelectedValue();
        boolean canAdd = (b != null) && moreRoom;
        add.setEnabled(canAdd && b.canAssignToMacro());
        insert.setEnabled(canAdd && b.canAssignToMacro());
        addShift.setEnabled(canAdd && b.canAssignShiftedToMacro());
        insertShift.setEnabled(canAdd && b.canAssignShiftedToMacro());
        boolean xShiftEnabled = config.getRemote().getXShiftEnabled();
        addXShift.setEnabled(xShiftEnabled && canAdd && b.canAssignXShiftedToMacro());
        insertXShift.setEnabled(xShiftEnabled && canAdd && b.canAssignXShiftedToMacro());
        int selected = firstMacroButtons.getSelectedIndex();
        firstMoveUp.setEnabled(selected > 0);
        firstMoveDown.setEnabled((selected != -1) && (selected < (listModel.getSize() - 1)));
        firstRemove.setEnabled(selected != -1);
        firstClear.setEnabled(listModel.getSize() > 0);
        listModel = (DefaultListModel) secondMacroButtons.getModel();
        moreRoom = listModel.getSize() < limit;
        canAdd = (b != null) && moreRoom;
        boolean isEnabled = secondMacroButtons.isEnabled();
        selected = secondMacroButtons.getSelectedIndex();
        secondMoveUp.setEnabled(isEnabled && (selected > 0));
        secondMoveDown.setEnabled(isEnabled && (selected != -1) && (selected < (listModel.getSize() - 1)));
        secondRemove.setEnabled(isEnabled && (selected != -1));
        secondClear.setEnabled(isEnabled && (listModel.getSize() > 0));
    }

    /**
   * Sets the macro buttons.
   * 
   * @param keyCodes
   *          the key codes
   * @param list
   *          the list
   */
    private void setMacroButtons(Integer[] keyCodes, JList list) {
        DefaultListModel model = (DefaultListModel) list.getModel();
        model.clear();
        for (Integer keyCode : keyCodes) model.addElement(keyCode);
        list.setSelectedIndex(-1);
    }

    /**
   * Gets the macro buttons.
   * 
   * @param list
   *          the list
   * @return the macro buttons
   */
    private Integer[] getMacroButtons(JList list) {
        DefaultListModel model = (DefaultListModel) list.getModel();
        Integer[] keyCodes = new Integer[model.size()];
        for (int i = 0; i < keyCodes.length; ++i) keyCodes[i] = (Integer) model.elementAt(i);
        return keyCodes;
    }

    /**
   * The Class FunctionTypeRenderer.
   */
    public class FunctionTypeRenderer extends DefaultListCellRenderer {

        /**
     * Instantiates a new function type renderer.
     */
        public FunctionTypeRenderer() {
        }

        public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
            String text = (String) value;
            if ((text != null) && (text.length() > 0) && (text.charAt(0) == 'U')) text = text.substring(1);
            return super.getListCellRendererComponent(list, text, index, isSelected, cellHasFocus);
        }
    }

    /** The bound device. */
    private JComboBox boundDevice = new JComboBox();

    /** The bound key. */
    private JComboBox boundKey = new JComboBox();

    /** The shift. */
    private JCheckBox shift = new JCheckBox();

    /** The x shift. */
    private JCheckBox xShift = new JCheckBox();

    /** The type. */
    private JComboBox type = new JComboBox();

    /**
   * Gets the type.
   * 
   * @return the type
   */
    public String getType() {
        return specialFunctionsByRDFName.get(type.getSelectedIndex());
    }

    /** The parameter card. */
    private JPanel parameterCard = new JPanel(new CardLayout());

    /** The mode name. */
    private JTextField modeName = new JTextField(10);

    /**
   * Sets the mode name.
   * 
   * @param text
   *          the new mode name
   */
    public void setModeName(String text) {
        modeName.setText(text);
    }

    /**
   * Gets the mode name.
   * 
   * @return the mode name
   */
    public String getModeName() {
        return modeName.getText();
    }

    /** The device type. */
    private JComboBox deviceType = new JComboBox();

    /**
   * Sets the device type.
   * 
   * @param deviceTypeIndex
   *          the new device type
   */
    public void setDeviceType(int deviceTypeIndex) {
        deviceType.setSelectedIndex(deviceTypeIndex);
    }

    /**
   * Gets the device type.
   * 
   * @return the device type
   */
    public int getDeviceType() {
        return deviceType.getSelectedIndex();
    }

    /** The setup code. */
    private JFormattedTextField setupCode = null;

    /**
   * Sets the setup code.
   * 
   * @param code
   *          the new setup code
   */
    public void setSetupCode(int code) {
        setupCode.setValue(new Integer(code));
    }

    /**
   * Gets the setup code.
   * 
   * @return the setup code
   */
    public int getSetupCode() {
        return ((Integer) setupCode.getValue()).intValue();
    }

    /** The duration. */
    private JSpinner duration = new JSpinner(new WrappingSpinnerNumberModel(0, 0, 15, 1));

    /**
   * Sets the duration.
   * 
   * @param d
   *          the new duration
   */
    public void setDuration(int d) {
        duration.setValue(new Integer(d));
    }

    /**
   * Gets the duration.
   * 
   * @return the duration
   */
    public int getDuration() {
        return ((Integer) duration.getValue()).intValue();
    }

    private class PauseField extends JFormattedTextField {

        public PauseField(NumberFormatter nf) {
            super(nf);
            setColumns(8);
            setFocusLostBehavior(JFormattedTextField.PERSIST);
        }

        protected void processFocusEvent(FocusEvent e) {
            super.processFocusEvent(e);
            if (e.getID() == FocusEvent.FOCUS_GAINED) {
                selectAll();
            }
        }
    }

    private PauseField pauseDuration = null;

    public Float getPauseDuration() {
        if (pauseDuration.getText().isEmpty() || !pauseDuration.isEditValid()) {
            showWarning("The pause value must be a number.  A decimal\n" + "point is allowed.  Examples of valid entries are\n" + "0.6, 2, 3.45.");
            return null;
        }
        try {
            pauseDuration.commitEdit();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return (Float) pauseDuration.getValue();
    }

    public void setPauseDuration(Float f) {
        if (f == null) {
            pauseDuration.setText(null);
        } else {
            pauseDuration.setValue(new Float(f));
        }
    }

    /** The toggle. */
    private JComboBox toggle = null;

    /**
   * Sets the toggle.
   * 
   * @param t
   *          the new toggle
   */
    public void setToggle(int t) {
        toggle.setSelectedIndex(t);
    }

    /**
   * Gets the toggle.
   * 
   * @return the toggle
   */
    public int getToggle() {
        return toggle.getSelectedIndex();
    }

    /** The condition. */
    private JComboBox condition = new JComboBox(ToadTogFunction.styleStrings);

    /**
   * Sets the condition.
   * 
   * @param c
   *          the new condition
   */
    public void setCondition(int c) {
        condition.setSelectedIndex(c);
    }

    /**
   * Gets the condition.
   * 
   * @return the condition
   */
    public int getCondition() {
        return condition.getSelectedIndex();
    }

    /** The macro key. */
    private JComboBox macroKey = new JComboBox();

    /**
   * Sets the macro key.
   * 
   * @param keyCode
   *          the new macro key
   */
    public void setMacroKey(int keyCode) {
        macroKey.setSelectedItem(new Integer(keyCode));
    }

    /**
   * Gets the macro key.
   * 
   * @return the macro key
   */
    public int getMacroKey() {
        return ((Integer) macroKey.getSelectedItem()).intValue();
    }

    /** The key code renderer. */
    private KeyCodeListRenderer keyCodeRenderer = new KeyCodeListRenderer();

    /** The uldkp duration. */
    private JComboBox uldkpDuration = null;

    /**
   * Sets the uLDKP duration.
   * 
   * @param d
   *          the new uLDKP duration
   */
    public void setULDKPDuration(int d) {
        uldkpDuration.setSelectedIndex(d);
    }

    /**
   * Gets the uLDKP duration.
   * 
   * @return the uLDKP duration
   */
    public int getULDKPDuration() {
        return uldkpDuration.getSelectedIndex();
    }

    /** The first key label. */
    private JLabel firstKeyLabel = new JLabel();

    /** The first macro key. */
    private JComboBox firstMacroKey = new JComboBox();

    /**
   * Sets the first macro key.
   * 
   * @param keyCode
   *          the new first macro key
   */
    public void setFirstMacroKey(int keyCode) {
        firstMacroKey.setSelectedItem(new Integer(keyCode));
    }

    /**
   * Gets the first macro key.
   * 
   * @return the first macro key
   */
    public int getFirstMacroKey() {
        return ((Integer) firstMacroKey.getSelectedItem()).intValue();
    }

    /** The second key label. */
    private JLabel secondKeyLabel = new JLabel();

    /** The second macro key. */
    private JComboBox secondMacroKey = new JComboBox();

    /**
   * Sets the second macro key.
   * 
   * @param keyCode
   *          the new second macro key
   */
    public void setSecondMacroKey(int keyCode) {
        secondMacroKey.setSelectedItem(new Integer(keyCode));
    }

    /**
   * Gets the second macro key.
   * 
   * @return the second macro key
   */
    public int getSecondMacroKey() {
        return ((Integer) secondMacroKey.getSelectedItem()).intValue();
    }

    /** The macro box. */
    private Box macroBox = null;

    /** The available label. */
    private JLabel availableLabel = new JLabel("Available keys:");

    /** The available buttons. */
    private JList availableButtons = new JList();

    /** The add. */
    private JButton add = new JButton("Add");

    /** The insert. */
    private JButton insert = new JButton("Insert");

    /** The add shift. */
    private JButton addShift = new JButton("Add Shift");

    /** The insert shift. */
    private JButton insertShift = new JButton("Ins Shift");

    /** The add x shift. */
    private JButton addXShift = new JButton("Add xShift");

    /** The insert x shift. */
    private JButton insertXShift = new JButton("Ins xShift");

    /** The macro button renderer. */
    private MacroButtonRenderer macroButtonRenderer = new MacroButtonRenderer();

    /** The first keys panel. */
    private JPanel firstKeysPanel = null;

    /** The first macro label. */
    private JLabel firstMacroLabel = new JLabel("Short keys:");

    /** The first macro buttons. */
    private JList firstMacroButtons = new JList();

    /** The target list. */
    private JList targetList = firstMacroButtons;

    /**
   * Sets the first macro buttons.
   * 
   * @param keyCodes
   *          the new first macro buttons
   */
    public void setFirstMacroButtons(Integer[] keyCodes) {
        setMacroButtons(keyCodes, firstMacroButtons);
    }

    /**
   * Gets the first macro buttons.
   * 
   * @return the first macro buttons
   */
    public Integer[] getFirstMacroButtons() {
        return getMacroButtons(firstMacroButtons);
    }

    /** The first move up. */
    private JButton firstMoveUp = new JButton("Move up");

    /** The first move down. */
    private JButton firstMoveDown = new JButton("Move down");

    /** The first remove. */
    private JButton firstRemove = new JButton("Remove");

    /** The first clear. */
    private JButton firstClear = new JButton("Clear");

    /** The second keys panel. */
    private JPanel secondKeysPanel = null;

    /** The second macro label. */
    private JLabel secondMacroLabel = new JLabel("Long keys:");

    /** The second macro buttons. */
    private JList secondMacroButtons = new JList();

    /**
   * Sets the second macro buttons.
   * 
   * @param keyCodes
   *          the new second macro buttons
   */
    public void setSecondMacroButtons(Integer[] keyCodes) {
        setMacroButtons(keyCodes, secondMacroButtons);
    }

    /**
   * Gets the second macro buttons.
   * 
   * @return the second macro buttons
   */
    public Integer[] getSecondMacroButtons() {
        return getMacroButtons(secondMacroButtons);
    }

    /** The second move up. */
    private JButton secondMoveUp = new JButton("Move up");

    /** The second move down. */
    private JButton secondMoveDown = new JButton("Move down");

    /** The second remove. */
    private JButton secondRemove = new JButton("Remove");

    /** The second clear. */
    private JButton secondClear = new JButton("Clear");

    /** The notes. */
    private JTextArea notes = new JTextArea(2, 2);

    /** The ok button. */
    private JButton okButton = new JButton("OK");

    /** The cancel button. */
    private JButton cancelButton = new JButton("Cancel");

    private JPanel paramPanel = null;

    /** The config. */
    private RemoteConfiguration config = null;

    public RemoteConfiguration getRemoteConfiguration() {
        return config;
    }

    /** The function. */
    private SpecialProtocolFunction function = null;

    /** The cmd. */
    private Hex cmd = null;

    /** The dialog. */
    public static SpecialFunctionDialog dialog = null;

    /** The active color. */
    private Color activeColor;

    /** The inactive color. */
    private Color inactiveColor;

    /** The disabled color. */
    private Color disabledColor;

    private java.util.List<String> specialFunctionsByRDFName = new ArrayList<String>();

    private Box noteBox = null;
}
