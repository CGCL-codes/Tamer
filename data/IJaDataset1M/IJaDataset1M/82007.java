package net.url404.umodulargui.panels;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import net.url404.umodular.SoundComponent;
import net.url404.umodular.components.MixerComponent;
import net.url404.umodulargui.SoundComponentPanel;
import net.url404.umodulargui.SoundModulePanel;

/**
 * An JInternalFrame that presents properties of a MixerComponent.
 *
 * @author makela@url404.net
 */
public class MixerComponentPanel extends SoundComponentPanel {

    private JTextField mainField;

    private JTextField channelField[] = new JTextField[MixerComponent.MIXER_CHANNELS];

    /**
   * Constructor.
   */
    public MixerComponentPanel() {
        super();
    }

    /**
   * Constructor.
   */
    public MixerComponentPanel(SoundComponent soundComponent, SoundModulePanel modulePane) {
        super(soundComponent, modulePane);
    }

    /**
   * Initialize Swing editable fields
   */
    public void initializeFields() {
        JPanel editPane = new JPanel(new BorderLayout());
        editPane.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.anchor = GridBagConstraints.WEST;
        gbc.insets = new Insets(2, 2, 2, 2);
        gbc.gridy = 0;
        gbc.gridx = 0;
        addGlobalParameters(editPane, gbc);
        JLabel mainLabel = new JLabel("Main level");
        mainField = new JTextField(Double.toString(((MixerComponent) getSoundComponent()).getLevel()));
        mainField.setColumns(8);
        mainLabel.setLabelFor(mainField);
        editPane.add(mainLabel, gbc);
        gbc.gridx = 1;
        editPane.add(mainField, gbc);
        for (int c = 0; c < MixerComponent.MIXER_CHANNELS; c++) {
            JLabel label = new JLabel("Channel level #" + (c + 1));
            channelField[c] = new JTextField(Double.toString(((MixerComponent) getSoundComponent()).getChannelLevel(c)));
            channelField[c].setColumns(8);
            label.setLabelFor(channelField[c]);
            gbc.gridy++;
            gbc.gridx = 0;
            editPane.add(label, gbc);
            gbc.gridx = 1;
            editPane.add(channelField[c], gbc);
        }
        addStandardButtons(editPane, gbc);
        getContentPane().add(editPane);
    }

    /**
   * Apply changes
   */
    public void actionPerformed(ActionEvent e) {
        MixerComponent mix = (MixerComponent) getSoundComponent();
        mix.setLevel(Double.parseDouble(mainField.getText()));
        for (int c = 0; c < MixerComponent.MIXER_CHANNELS; c++) {
            mix.setChannelLevel(c, Double.parseDouble(channelField[c].getText()));
        }
        super.actionPerformed(e);
    }
}
