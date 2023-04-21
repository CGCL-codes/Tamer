package com.carsongee.jsshmacro;

import javax.swing.JComboBox;
import javax.swing.JTextPane;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

/**
 * This class monitors macro document changes
 * scans for variables and updates the underlying macro data
 * object.
 * Note: Used to be an anonymous inner class in JSSHMacroFrame
 * @author Carson Gee
 */
public class MacroDocumentListener implements DocumentListener {

    private MacroListModel macroData;

    private boolean macroChanging;

    private JComboBox macroList;

    private JTextPane macroTxt;

    /**
     * Initialize with data sources/controls that are modified by this
     * object
     * @param macroData The Macros that are to be modified
     * @param macroList The list of macros
     * @param macroTxt The macro text pane monitored by this document listener.
     */
    public MacroDocumentListener(MacroListModel macroData, JComboBox macroList, JTextPane macroTxt) {
        this.macroData = macroData;
        this.macroList = macroList;
        this.macroTxt = macroTxt;
        macroChanging = false;
    }

    public void insertUpdate(DocumentEvent e) {
        if (macroChanging) {
            macroChanging = false;
            return;
        }
        autoSave();
    }

    public void removeUpdate(DocumentEvent e) {
        if (macroChanging) {
            macroChanging = false;
            return;
        }
        autoSave();
    }

    public void changedUpdate(DocumentEvent e) {
    }

    private void autoSave() {
        if (macroList.getSelectedIndex() != -1) macroData.getMacro(macroList.getSelectedIndex()).setMacroText(macroTxt.getText());
    }

    /**
     * This is a flag to set so that the document listener does not do
     * an autosave while the macro is being changed via the
     * MacroSelectionChanged action
     * @param macroChanging true: Ignore next insert/remove update.
     * This property will revert to false after one remove/insert update
     */
    public void setMacroChanging(boolean macroChanging) {
        this.macroChanging = macroChanging;
    }
}
