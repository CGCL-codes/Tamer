package org.openofficesearch.gui;

import java.sql.SQLException;
import java.util.Map;
import java.util.Stack;
import javax.swing.JOptionPane;
import org.jdesktop.application.Action;
import org.openofficesearch.data.BooleanPreferenceValue;
import org.openofficesearch.data.PreferenceHandler;
import org.openofficesearch.data.PreferenceTypes;
import org.openofficesearch.gui.action.ActionException;
import org.openofficesearch.gui.action.SavePreferenceActionSwingCheckboxListener;
import org.openofficesearch.gui.action.UndoableUserAction;

/**
 * A dialog allowing users to specify options values
 * Created on February 23, 2008, 2:40 PM
 * @author Connor Garvey
 * @version 0.1.1
 * @since 0.1.1
 */
public class PreferencesDialog extends javax.swing.JDialog implements IndexerUI {

    private static final long serialVersionUID = 1;

    private Stack<UndoableUserAction> actions;

    /** Creates new form OptionsDialog
   * @param parent The parent component
   * @param modal Indicates whether the dialog should be modal
   */
    public PreferencesDialog(java.awt.Frame parent, boolean modal) {
        super(parent, modal);
        this.actions = new Stack<UndoableUserAction>();
        initComponents();
        this.load();
    }

    private void initComponents() {
        indexSettingsPanel = new javax.swing.JPanel();
        removeDeletedCheckBox = new javax.swing.JCheckBox();
        okButton = new javax.swing.JButton();
        cancelButton = new javax.swing.JButton();
        uiSettingsPanel = new javax.swing.JPanel();
        propellerheadCheckBox = new javax.swing.JCheckBox();
        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setName("Form");
        org.jdesktop.application.ResourceMap resourceMap = org.jdesktop.application.Application.getInstance(org.openofficesearch.gui.MainFrame.class).getContext().getResourceMap(PreferencesDialog.class);
        indexSettingsPanel.setBorder(javax.swing.BorderFactory.createTitledBorder(resourceMap.getString("indexSettingsPanel.border.title")));
        indexSettingsPanel.setName("indexSettingsPanel");
        removeDeletedCheckBox.setText(resourceMap.getString("removeDeletedCheckBox.text"));
        removeDeletedCheckBox.setName("removeDeletedCheckBox");
        this.removeDeletedCheckBox.addItemListener(new SavePreferenceActionSwingCheckboxListener(PreferenceTypes.REMOVE_DELETED_FILES.getID(), this.actions, this));
        javax.swing.GroupLayout indexSettingsPanelLayout = new javax.swing.GroupLayout(indexSettingsPanel);
        indexSettingsPanel.setLayout(indexSettingsPanelLayout);
        indexSettingsPanelLayout.setHorizontalGroup(indexSettingsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(javax.swing.GroupLayout.Alignment.TRAILING, indexSettingsPanelLayout.createSequentialGroup().addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE).addComponent(removeDeletedCheckBox).addContainerGap()));
        indexSettingsPanelLayout.setVerticalGroup(indexSettingsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(indexSettingsPanelLayout.createSequentialGroup().addComponent(removeDeletedCheckBox).addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)));
        javax.swing.ActionMap actionMap = org.jdesktop.application.Application.getInstance(org.openofficesearch.gui.MainFrame.class).getContext().getActionMap(PreferencesDialog.class, this);
        okButton.setAction(actionMap.get("close"));
        okButton.setText(resourceMap.getString("okButton.text"));
        okButton.setName("okButton");
        cancelButton.setAction(actionMap.get("cancel"));
        cancelButton.setText(resourceMap.getString("cancelButton.text"));
        cancelButton.setName("cancelButton");
        uiSettingsPanel.setBorder(javax.swing.BorderFactory.createTitledBorder(resourceMap.getString("uiSettingsPanel.border.title")));
        uiSettingsPanel.setName("uiSettingsPanel");
        propellerheadCheckBox.setText(resourceMap.getString("propellerheadCheckBox.text"));
        propellerheadCheckBox.setName("propellerheadCheckBox");
        this.propellerheadCheckBox.addItemListener(new SavePreferenceActionSwingCheckboxListener(PreferenceTypes.REPORT_WORDS_INDEXED.getID(), this.actions, this));
        javax.swing.GroupLayout uiSettingsPanelLayout = new javax.swing.GroupLayout(uiSettingsPanel);
        uiSettingsPanel.setLayout(uiSettingsPanelLayout);
        uiSettingsPanelLayout.setHorizontalGroup(uiSettingsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(javax.swing.GroupLayout.Alignment.TRAILING, uiSettingsPanelLayout.createSequentialGroup().addContainerGap().addComponent(propellerheadCheckBox, javax.swing.GroupLayout.DEFAULT_SIZE, 199, Short.MAX_VALUE).addContainerGap()));
        uiSettingsPanelLayout.setVerticalGroup(uiSettingsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(uiSettingsPanelLayout.createSequentialGroup().addComponent(propellerheadCheckBox).addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)));
        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup().addContainerGap().addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING).addComponent(uiSettingsPanel, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE).addComponent(indexSettingsPanel, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE).addGroup(layout.createSequentialGroup().addComponent(okButton).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED).addComponent(cancelButton))).addContainerGap()));
        layout.setVerticalGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup().addContainerGap().addComponent(indexSettingsPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED).addComponent(uiSettingsPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE).addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addComponent(cancelButton).addComponent(okButton)).addContainerGap()));
        pack();
    }

    /**
   * Undoes any changes and closes the window
   */
    @Action
    public void cancel() {
        while (!this.actions.isEmpty()) {
            try {
                actions.pop().undoAction();
            } catch (ActionException ex) {
                JOptionPane.showMessageDialog(this, "Could not undo changes\n" + ex.toString(), "Could not undo", JOptionPane.ERROR_MESSAGE);
            }
        }
        this.close();
    }

    /**
   * Closes the window
   */
    @Action
    public void close() {
        this.setVisible(false);
    }

    /**
   * Loads all data
   */
    public void load() {
        Map<String, String> preferences = null;
        try {
            preferences = PreferenceHandler.getAllAsMap();
        } catch (SQLException ex) {
            this.showError("Could not load preferences", "Load error");
        }
        this.propellerheadCheckBox.setSelected(BooleanPreferenceValue.getByDatabaseValue(preferences.get(PreferenceTypes.REPORT_WORDS_INDEXED.getID())).getBooleanValue());
        this.removeDeletedCheckBox.setSelected(BooleanPreferenceValue.getByDatabaseValue(preferences.get(PreferenceTypes.REMOVE_DELETED_FILES.getID())).getBooleanValue());
    }

    public void showError(String error, String title) {
        JOptionPane.showMessageDialog(this, error, title, JOptionPane.ERROR_MESSAGE);
    }

    private javax.swing.JButton cancelButton;

    private javax.swing.JPanel indexSettingsPanel;

    private javax.swing.JButton okButton;

    private javax.swing.JCheckBox propellerheadCheckBox;

    private javax.swing.JCheckBox removeDeletedCheckBox;

    private javax.swing.JPanel uiSettingsPanel;
}
