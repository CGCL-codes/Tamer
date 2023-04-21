package com.genia.toolbox.portlet.editor.gui.dialog;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import com.genia.toolbox.basics.editor.gui.dialog.AbstractSettingsDialog;
import com.genia.toolbox.portlet.editor.gui.PortletEditorGUI;
import com.genia.toolbox.portlet.editor.gui.dialog.panel.PortletSelectionPanel;
import com.genia.toolbox.portlet.editor.model.bean.PortletInitialSettings;

/**
 * Sub-portlet creation dialog.
 */
@SuppressWarnings("serial")
public class SubPortletCreationDialog extends AbstractSettingsDialog<PortletEditorGUI, PortletInitialSettings> {

    /**
   * The selection panel.
   */
    private PortletSelectionPanel selectionPanel = null;

    /**
   * Constructor.
   * 
   * @param portletEditorGUI
   *          The portletEditorGUI.
   */
    public SubPortletCreationDialog(PortletEditorGUI portletEditorGUI) {
        super(portletEditorGUI);
    }

    /**
   * Get the title key.
   * 
   * @return the title key.
   */
    @Override
    public String getTitleKey() {
        return "com.genia.toolbox.portlet.editor.gui.dialog.PortletCreationDialog.Title";
    }

    /**
   * Get the ok button key.
   * 
   * @return the ok button key.
   */
    @Override
    public String getOkKey() {
        return "com.genia.toolbox.portlet.editor.gui.dialog.PortletSelectorDialog.button.OK";
    }

    /**
   * Get the ok cancel key.
   * 
   * @return the ok cancel key.
   */
    @Override
    public String getCancelKey() {
        return "com.genia.toolbox.portlet.editor.gui.dialog.PortletSelectorDialog.button.Cancel";
    }

    /**
   * Get the width.
   * 
   * @return the width.
   */
    @Override
    public int getWidth() {
        return 500;
    }

    /**
   * Get the height.
   * 
   * @return the height.
   */
    @Override
    public int getHeight() {
        return 300;
    }

    /**
   * Create the main panel.
   * 
   * @return The main panel.
   */
    @Override
    public JPanel createMainPanel() {
        JPanel panel = new JPanel();
        GridBagLayout layout = new GridBagLayout();
        GridBagConstraints constraints = new GridBagConstraints();
        panel.setLayout(layout);
        panel.setBorder(BorderFactory.createTitledBorder(this.getGui().getController().notifyTranslation("com.genia.toolbox.portlet.editor.gui.dialog.PortletCreationDialog.Border")));
        JLabel labelPortlet = new JLabel(this.getGui().getController().notifyTranslation("com.genia.toolbox.portlet.editor.gui.dialog.PortletCreationDialog.label.base"));
        constraints.gridx = 1;
        constraints.gridy = 1;
        constraints.gridwidth = 2;
        constraints.gridheight = 1;
        constraints.weightx = 1;
        constraints.weighty = 0;
        constraints.fill = GridBagConstraints.HORIZONTAL;
        constraints.insets = new Insets(0, 5, 5, 5);
        panel.add(labelPortlet, constraints);
        this.selectionPanel = new PortletSelectionPanel(this.getGui(), true);
        constraints.gridx = 1;
        constraints.gridy = 2;
        constraints.gridwidth = 2;
        constraints.gridheight = 1;
        constraints.weightx = 1;
        constraints.weighty = 1;
        constraints.fill = GridBagConstraints.BOTH;
        constraints.insets = new Insets(0, 15, 5, 5);
        panel.add(this.selectionPanel, constraints);
        return panel;
    }

    /**
   * Process the event of an user clicking on the OK button.
   */
    @Override
    public void doOk() {
        this.setSettings(this.selectionPanel.getSettings());
        this.dispose();
    }
}
