package edu.fit.it.blue;

import java.util.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.border.*;

/**
  'Limit' panel class on 'Configure' tabbed pane.
 */
public class blueConfigureTabbedPaneLimitPanel {

    private blueBrowseForFilename fileChooser;

    private blueIntegerComboBox sizeLimitSimpleMaxEntries;

    private blueIntegerComboBox sizeLimitAdvancedSoft;

    private blueIntegerComboBox sizeLimitAdvancedHard;

    private blueIntegerComboBox sizeLimitAdvancedUnchecked;

    public JPanel panel;

    /**
    Constructs 'Limit' panel for the configure the tabbed pane.
  */
    public blueConfigureTabbedPaneLimitPanel() {
        fileChooser = new blueBrowseForFilename();
        panel = new JPanel();
        GridBagLayout panelLayout = new GridBagLayout();
        GridBagConstraints panelGridBagConstraints = new GridBagConstraints();
        panel.setLayout(panelLayout);
        TitledBorder sizeLimitPanelBorder = BorderFactory.createTitledBorder("Result-set Size Limits");
        sizeLimitPanelBorder.setTitlePosition(TitledBorder.TOP);
        JPanel sizeLimitPanel = new JPanel();
        sizeLimitPanel.setBorder(sizeLimitPanelBorder);
        GridBagLayout sizeLimitPanelLayout = new GridBagLayout();
        GridBagConstraints sizeLimitPanelGridBagConstraints = new GridBagConstraints();
        sizeLimitPanel.setLayout(sizeLimitPanelLayout);
        JPanel sizeLimitRadioButtonPanel = new JPanel(new GridLayout(1, 0));
        TitledBorder sizeLimitRadioButtonBorder = BorderFactory.createTitledBorder("Mode");
        sizeLimitRadioButtonPanel.setBorder(sizeLimitRadioButtonBorder);
        ButtonGroup sizeLimitRadioButtonGroup = new ButtonGroup();
        JRadioButton sizeLimitSimpleRadioButton = new JRadioButton("simple");
        JRadioButton sizeLimitAdvancedRadioButton = new JRadioButton("advanced");
        ActionListener sizeLimitRadioButtonListener = new ActionListener() {

            public void actionPerformed(ActionEvent ev) {
                AbstractButton src = (AbstractButton) ev.getSource();
                if (src.getText().equals("simple")) {
                    sizeLimitSimpleMaxEntries.comboBox.setEnabled(true);
                    sizeLimitSimpleMaxEntries.label.setEnabled(true);
                    sizeLimitAdvancedSoft.comboBox.setEnabled(false);
                    sizeLimitAdvancedSoft.label.setEnabled(false);
                    sizeLimitAdvancedHard.comboBox.setEnabled(false);
                    sizeLimitAdvancedHard.label.setEnabled(false);
                    sizeLimitAdvancedUnchecked.comboBox.setEnabled(false);
                    sizeLimitAdvancedUnchecked.label.setEnabled(false);
                } else {
                    sizeLimitSimpleMaxEntries.comboBox.setEnabled(false);
                    sizeLimitSimpleMaxEntries.label.setEnabled(false);
                    sizeLimitAdvancedSoft.comboBox.setEnabled(true);
                    sizeLimitAdvancedSoft.label.setEnabled(true);
                    sizeLimitAdvancedHard.comboBox.setEnabled(true);
                    sizeLimitAdvancedHard.label.setEnabled(true);
                    sizeLimitAdvancedUnchecked.comboBox.setEnabled(true);
                    sizeLimitAdvancedUnchecked.label.setEnabled(true);
                }
            }
        };
        sizeLimitSimpleRadioButton.addActionListener(sizeLimitRadioButtonListener);
        sizeLimitAdvancedRadioButton.addActionListener(sizeLimitRadioButtonListener);
        sizeLimitRadioButtonPanel.add(sizeLimitSimpleRadioButton);
        sizeLimitRadioButtonGroup.add(sizeLimitSimpleRadioButton);
        sizeLimitRadioButtonPanel.add(sizeLimitAdvancedRadioButton);
        sizeLimitRadioButtonGroup.add(sizeLimitAdvancedRadioButton);
        sizeLimitSimpleRadioButton.setSelected(true);
        sizeLimitSimpleMaxEntries = new blueIntegerComboBox("Maximum number of entries returned", "sizelimit <integer> - optional - Specify the maximum number of entries" + " to return from a search operation. The default size limit is 500");
        sizeLimitAdvancedSoft = new blueIntegerComboBox("Server suggested size limit", "sizelimit size.soft=<integer> - optional - If no size limit is explicitly" + "requested by the client, the soft limit is used");
        sizeLimitAdvancedSoft.label.setEnabled(false);
        sizeLimitAdvancedSoft.comboBox.setEnabled(false);
        sizeLimitAdvancedHard = new blueIntegerComboBox("Server required size limit", "sizelimit size.hard=<integer> - optional - If the requested size limit" + " exceeds the hard limit, an \"Unwilling to perform\" is returned to" + " the client");
        sizeLimitAdvancedHard.label.setEnabled(false);
        sizeLimitAdvancedHard.comboBox.setEnabled(false);
        sizeLimitAdvancedUnchecked = new blueIntegerComboBox("Search candidate limit", "sizelimit size.unchecked=<integer> - optional - Sets a limit on the " + "number of candidates a search request is allowed to examine");
        sizeLimitAdvancedUnchecked.label.setEnabled(false);
        sizeLimitAdvancedUnchecked.comboBox.setEnabled(false);
        sizeLimitPanelGridBagConstraints.fill = GridBagConstraints.HORIZONTAL;
        sizeLimitPanelGridBagConstraints.weightx = 1.0;
        sizeLimitPanelGridBagConstraints.gridwidth = GridBagConstraints.REMAINDER;
        sizeLimitPanelLayout.setConstraints(sizeLimitRadioButtonPanel, sizeLimitPanelGridBagConstraints);
        sizeLimitPanel.add(sizeLimitRadioButtonPanel);
        sizeLimitPanelGridBagConstraints.weightx = 1.0;
        sizeLimitPanelGridBagConstraints.gridwidth = 1;
        sizeLimitPanelLayout.setConstraints(sizeLimitSimpleMaxEntries.label, sizeLimitPanelGridBagConstraints);
        sizeLimitPanel.add(sizeLimitSimpleMaxEntries.label);
        sizeLimitPanelGridBagConstraints.fill = GridBagConstraints.BOTH;
        sizeLimitPanelGridBagConstraints.weightx = 1.0;
        sizeLimitPanelGridBagConstraints.gridwidth = GridBagConstraints.REMAINDER;
        sizeLimitPanelLayout.setConstraints(sizeLimitSimpleMaxEntries.comboBox, sizeLimitPanelGridBagConstraints);
        sizeLimitPanel.add(sizeLimitSimpleMaxEntries.comboBox);
        sizeLimitPanelGridBagConstraints.fill = GridBagConstraints.BOTH;
        sizeLimitPanelGridBagConstraints.weightx = 1.0;
        sizeLimitPanelGridBagConstraints.gridwidth = 1;
        sizeLimitPanelLayout.setConstraints(sizeLimitAdvancedSoft.label, sizeLimitPanelGridBagConstraints);
        sizeLimitPanel.add(sizeLimitAdvancedSoft.label);
        sizeLimitPanelGridBagConstraints.fill = GridBagConstraints.BOTH;
        sizeLimitPanelGridBagConstraints.weightx = 1.0;
        sizeLimitPanelGridBagConstraints.gridwidth = GridBagConstraints.REMAINDER;
        sizeLimitPanelLayout.setConstraints(sizeLimitAdvancedSoft.comboBox, sizeLimitPanelGridBagConstraints);
        sizeLimitPanel.add(sizeLimitAdvancedSoft.comboBox);
        sizeLimitPanelGridBagConstraints.fill = GridBagConstraints.BOTH;
        sizeLimitPanelGridBagConstraints.weightx = 1.0;
        sizeLimitPanelGridBagConstraints.gridwidth = 1;
        sizeLimitPanelLayout.setConstraints(sizeLimitAdvancedHard.label, sizeLimitPanelGridBagConstraints);
        sizeLimitPanel.add(sizeLimitAdvancedHard.label);
        sizeLimitPanelGridBagConstraints.fill = GridBagConstraints.BOTH;
        sizeLimitPanelGridBagConstraints.weightx = 1.0;
        sizeLimitPanelGridBagConstraints.gridwidth = GridBagConstraints.REMAINDER;
        sizeLimitPanelLayout.setConstraints(sizeLimitAdvancedHard.comboBox, sizeLimitPanelGridBagConstraints);
        sizeLimitPanel.add(sizeLimitAdvancedHard.comboBox);
        sizeLimitPanelGridBagConstraints.fill = GridBagConstraints.BOTH;
        sizeLimitPanelGridBagConstraints.weightx = 1.0;
        sizeLimitPanelGridBagConstraints.gridwidth = 1;
        sizeLimitPanelLayout.setConstraints(sizeLimitAdvancedUnchecked.label, sizeLimitPanelGridBagConstraints);
        sizeLimitPanel.add(sizeLimitAdvancedUnchecked.label);
        sizeLimitPanelGridBagConstraints.fill = GridBagConstraints.BOTH;
        sizeLimitPanelGridBagConstraints.weightx = 1.0;
        sizeLimitPanelGridBagConstraints.gridwidth = GridBagConstraints.REMAINDER;
        sizeLimitPanelLayout.setConstraints(sizeLimitAdvancedUnchecked.comboBox, sizeLimitPanelGridBagConstraints);
        sizeLimitPanel.add(sizeLimitAdvancedUnchecked.comboBox);
        panelGridBagConstraints.fill = GridBagConstraints.HORIZONTAL;
        panelGridBagConstraints.anchor = GridBagConstraints.NORTHWEST;
        panelGridBagConstraints.weightx = GridBagConstraints.REMAINDER;
        panelLayout.setConstraints(sizeLimitPanel, panelGridBagConstraints);
        panel.add(sizeLimitPanel);
    }
}
