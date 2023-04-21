package edu.gatech.ealf.config;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.LinkedList;
import java.util.List;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JColorChooser;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSeparator;
import javax.swing.JTabbedPane;
import javax.swing.JTextField;
import javax.swing.border.TitledBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import edu.gatech.ealf.EasyAccessibilityLookAndFeel;
import edu.gatech.ealf.components.ColorSwatch;
import edu.gatech.ealf.ettplaf.ExtendedToolTipLookAndFeel;
import edu.gatech.ealf.keyboardassistanceplaf.KeyboardAssistanceLookAndFeel;
import edu.gatech.ealf.magiclenseplaf.MagicLenseLookAndFeel;
import edu.gatech.ealf.ttsplaf.TextToSpeechLookAndFeel;

public class EALFConfigPanel extends JPanel implements ActionListener {

    private static final long serialVersionUID = 1L;

    private JPanel ealfPanel = new JPanel();

    private JPanel zoomPanel = new JPanel();

    private JPanel extendedToolTipPanel = new JPanel();

    private JPanel textToSpeechPanel = new JPanel();

    private JPanel keyboardAssistancePanel = new JPanel();

    private List<JComponent> zoomOptions = new LinkedList<JComponent>();

    private List<JComponent> extendedToolTipOptions = new LinkedList<JComponent>();

    private List<JComponent> textToSpeechOptions = new LinkedList<JComponent>();

    private List<JComponent> keyboardAssistanceOptions = new LinkedList<JComponent>();

    private JCheckBox ealfEnabled;

    private JCheckBox zoomEnabled;

    private JTextField zoomFactor;

    private JCheckBox extendedToolTipEnabled;

    private JTextField toolTipDelayMs;

    private JTextField extendedToolTipDelayMs;

    private JColorChooser foregroundColor;

    private JColorChooser backgroundColor;

    private JButton foregroundColorButton;

    private JButton backgroundColorButton;

    private JDialog foregroundColorDialog;

    private JDialog backgroundColorDialog;

    private ColorSwatch foregroundColorSwatch;

    private ColorSwatch backgroundColorSwatch;

    private JCheckBox textToSpeechEnabled;

    private JComboBox voice;

    private JCheckBox keyboardAssistanceEnabled;

    private JCheckBox kaSpeechEnabled;

    private JCheckBox kaDisplayEnabled;

    private JTextField kaZoomFactor;

    private JButton saveButton;

    private JButton cancelButton;

    private MagicLenseLookAndFeel zoomInstance;

    private ExtendedToolTipLookAndFeel ettInstance;

    private TextToSpeechLookAndFeel ttsInstance;

    private KeyboardAssistanceLookAndFeel kaInstance;

    private JTabbedPane tabPane;

    public EALFConfigPanel() {
        zoomInstance = MagicLenseLookAndFeel.getInstance();
        ettInstance = ExtendedToolTipLookAndFeel.getInstance();
        ttsInstance = TextToSpeechLookAndFeel.getInstance();
        kaInstance = KeyboardAssistanceLookAndFeel.getInstance();
        tabPane = new JTabbedPane();
        setLayout(new FlowLayout());
        setMaximumSize(new Dimension(600, 600));
        setMinimumSize(new Dimension(200, 200));
        setPreferredSize(new Dimension(500, 450));
        Dimension subPanelSize = new Dimension(500, 200);
        Dimension ealfPanelSize = new Dimension(500, 50);
        ealfPanel.setLayout(new BoxLayout(ealfPanel, BoxLayout.X_AXIS));
        ealfPanel.setBorder(new TitledBorder("Easy Accessibility Look and Feel"));
        ealfPanel.setMinimumSize(ealfPanelSize);
        ealfPanel.setPreferredSize(ealfPanelSize);
        zoomPanel.setLayout(new FlowLayout());
        zoomPanel.setMinimumSize(subPanelSize);
        zoomPanel.setPreferredSize(subPanelSize);
        extendedToolTipPanel.setLayout(new GridLayout(5, 2));
        extendedToolTipPanel.setMinimumSize(subPanelSize);
        extendedToolTipPanel.setPreferredSize(subPanelSize);
        textToSpeechPanel.setLayout(new FlowLayout());
        textToSpeechPanel.setMinimumSize(subPanelSize);
        textToSpeechPanel.setPreferredSize(subPanelSize);
        keyboardAssistancePanel.setLayout(new GridLayout(4, 2));
        keyboardAssistancePanel.setMinimumSize(subPanelSize);
        keyboardAssistancePanel.setPreferredSize(subPanelSize);
        saveButton = new JButton("Save Settings");
        saveButton.setName("EALF.save");
        saveButton.addActionListener(this);
        cancelButton = new JButton("Cancel");
        cancelButton.setName("EALF.cancel");
        cancelButton.addActionListener(this);
        ealfEnabled = new JCheckBox("Enable", EasyAccessibilityLookAndFeel.isEnabled());
        ealfEnabled.setToolTipText("Enables or disables all accessibility features of the EasyAccessibilityLookAndFeel");
        ealfEnabled.addChangeListener(new ChangeListener() {

            public void stateChanged(ChangeEvent e) {
                setComponentsEnabled();
            }
        });
        ealfPanel.add(ealfEnabled);
        zoomEnabled = new JCheckBox("Enable", zoomInstance.isEnabled());
        zoomEnabled.setToolTipText("Enables or disables zooming of widgets.");
        zoomEnabled.addChangeListener(new ChangeListener() {

            public void stateChanged(ChangeEvent e) {
                setComponentsEnabled(zoomOptions);
            }
        });
        JSeparator zoomSeparator = new JSeparator(JSeparator.VERTICAL);
        zoomSeparator.setMaximumSize(new Dimension(5, 10000));
        zoomFactor = new JTextField("" + zoomInstance.getZoomFactor());
        zoomFactor.setMaximumSize(new Dimension(50, 25));
        zoomFactor.setToolTipText("The multiplicative factor by which widgets will be zoomed. Must be a floating point or integral number");
        JLabel zoomFactorLabel = new JLabel("Zoom Factor: ");
        zoomFactorLabel.setLabelFor(zoomFactor);
        zoomOptions.add(zoomFactor);
        zoomPanel.add(zoomEnabled);
        zoomPanel.add(zoomSeparator);
        zoomPanel.add(zoomFactorLabel);
        zoomPanel.add(zoomFactor);
        extendedToolTipEnabled = new JCheckBox("Enable", ettInstance.isEnabled());
        extendedToolTipEnabled.setToolTipText("Enables or disables display of Extended Tool Tips.");
        extendedToolTipEnabled.addChangeListener(new ChangeListener() {

            public void stateChanged(ChangeEvent e) {
                setComponentsEnabled(extendedToolTipOptions);
            }
        });
        JLabel extendedToolTipEnabledLabel = new JLabel("Extended Tool Tips: ");
        extendedToolTipEnabledLabel.setLabelFor(extendedToolTipEnabled);
        toolTipDelayMs = new JTextField("" + ettInstance.toolTipDelayMs);
        toolTipDelayMs.setToolTipText("The number of milliseconds before the regular Tool Tip will appear.");
        toolTipDelayMs.setMaximumSize(new Dimension(50, 25));
        JLabel toolTipDelayMsLabel = new JLabel("Tool Tip Delay (ms): ");
        toolTipDelayMsLabel.setLabelFor(toolTipDelayMs);
        extendedToolTipDelayMs = new JTextField("" + ettInstance.extendedToolTipDelayMs);
        extendedToolTipDelayMs.setToolTipText("The number of milliseconds before the regular Extended Tool Tip will appear.");
        extendedToolTipDelayMs.setMaximumSize(new Dimension(50, 25));
        JLabel extendedToolTipDelayMsLabel = new JLabel("Extended Tool Tip Delay (ms): ");
        extendedToolTipDelayMsLabel.setLabelFor(extendedToolTipDelayMs);
        JPanel foregroundColorPanel = new JPanel();
        foregroundColorPanel.setLayout(new BoxLayout(foregroundColorPanel, BoxLayout.X_AXIS));
        JPanel backgroundColorPanel = new JPanel();
        backgroundColorPanel.setLayout(new BoxLayout(backgroundColorPanel, BoxLayout.X_AXIS));
        foregroundColor = new JColorChooser(ettInstance.foregroundColor);
        backgroundColor = new JColorChooser(ettInstance.backgroundColor);
        foregroundColorSwatch = new ColorSwatch(ettInstance.foregroundColor);
        foregroundColorSwatch.setMaximumSize(new Dimension(20, 10000));
        backgroundColorSwatch = new ColorSwatch(ettInstance.backgroundColor);
        backgroundColorSwatch.setMaximumSize(new Dimension(20, 10000));
        foregroundColorDialog = JColorChooser.createDialog(extendedToolTipPanel, "Choose Foreground Color", true, foregroundColor, new ColorChooserActionListener(foregroundColor, foregroundColorSwatch), null);
        backgroundColorDialog = JColorChooser.createDialog(extendedToolTipPanel, "Choose Background Color", true, backgroundColor, new ColorChooserActionListener(backgroundColor, backgroundColorSwatch), null);
        foregroundColorButton = new JButton("Choose Color...");
        foregroundColorButton.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                foregroundColorDialog.setVisible(true);
            }
        });
        JLabel foregroundColorButtonLabel = new JLabel("Foreground Color: ");
        foregroundColorButtonLabel.setLabelFor(foregroundColorButton);
        backgroundColorButton = new JButton("Choose Color...");
        backgroundColorButton.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                backgroundColorDialog.setVisible(true);
            }
        });
        JLabel backgroundColorButtonLabel = new JLabel("Background Color: ");
        backgroundColorButtonLabel.setLabelFor(backgroundColorButton);
        foregroundColorPanel.add(foregroundColorSwatch);
        foregroundColorPanel.add(foregroundColorButton);
        backgroundColorPanel.add(backgroundColorSwatch);
        backgroundColorPanel.add(backgroundColorButton);
        extendedToolTipOptions.add(toolTipDelayMs);
        extendedToolTipOptions.add(extendedToolTipDelayMs);
        extendedToolTipOptions.add(foregroundColorButton);
        extendedToolTipOptions.add(backgroundColorButton);
        extendedToolTipPanel.add(extendedToolTipEnabledLabel);
        extendedToolTipPanel.add(extendedToolTipEnabled);
        extendedToolTipPanel.add(toolTipDelayMsLabel);
        extendedToolTipPanel.add(toolTipDelayMs);
        extendedToolTipPanel.add(extendedToolTipDelayMsLabel);
        extendedToolTipPanel.add(extendedToolTipDelayMs);
        extendedToolTipPanel.add(foregroundColorButtonLabel);
        extendedToolTipPanel.add(foregroundColorPanel);
        extendedToolTipPanel.add(backgroundColorButtonLabel);
        extendedToolTipPanel.add(backgroundColorPanel);
        textToSpeechEnabled = new JCheckBox("Enable", ttsInstance.isEnabled());
        textToSpeechEnabled.setToolTipText("Enables or disables speech output.");
        textToSpeechEnabled.addChangeListener(new ChangeListener() {

            public void stateChanged(ChangeEvent e) {
                setComponentsEnabled(textToSpeechOptions);
            }
        });
        JSeparator ttsSeparator = new JSeparator(JSeparator.VERTICAL);
        ttsSeparator.setMaximumSize(new Dimension(5, 10000));
        voice = new JComboBox(new String[] { TextToSpeechLookAndFeel.KEVIN16, TextToSpeechLookAndFeel.KEVIN });
        voice.setSelectedItem(ttsInstance.getVoiceString());
        voice.setToolTipText("The voice that will be used for speech synthesis.");
        voice.setMaximumSize(new Dimension(200, 25));
        JLabel voiceLabel = new JLabel("Voice: ");
        voiceLabel.setLabelFor(voiceLabel);
        textToSpeechOptions.add(voice);
        textToSpeechPanel.add(textToSpeechEnabled);
        textToSpeechPanel.add(ttsSeparator);
        textToSpeechPanel.add(voiceLabel);
        textToSpeechPanel.add(voice);
        keyboardAssistanceEnabled = new JCheckBox("Enable", kaInstance.isEnabled());
        keyboardAssistanceEnabled.setToolTipText("Enables or disables keyboard assistance.");
        keyboardAssistanceEnabled.addChangeListener(new ChangeListener() {

            public void stateChanged(ChangeEvent e) {
                setComponentsEnabled(keyboardAssistanceOptions);
            }
        });
        JLabel keyboardAssistanceEnabledLabel = new JLabel("Keyboard Assistance: ");
        keyboardAssistanceEnabledLabel.setLabelFor(keyboardAssistanceEnabled);
        kaDisplayEnabled = new JCheckBox("Enable", kaInstance.isDisplayEnabled());
        kaDisplayEnabled.setToolTipText("When enabled, displays each character typed magnified on the screen.");
        JLabel kaDisplayEnabledLabel = new JLabel("Display Key Strokes: ");
        kaDisplayEnabledLabel.setLabelFor(kaDisplayEnabled);
        kaSpeechEnabled = new JCheckBox("Enable", kaInstance.isSpeechEnabled());
        kaSpeechEnabled.setToolTipText("When enabled, speaks each character typed.");
        JLabel kaSpeechEnabledLabel = new JLabel("Speak Key Strokes: ");
        kaSpeechEnabledLabel.setLabelFor(kaSpeechEnabled);
        kaZoomFactor = new JTextField("" + kaInstance.getZoomFactor());
        kaZoomFactor.setToolTipText("The multiplicative factor by which key strokes will be magnified. Must be a floating point or integral number.");
        JLabel kaZoomFactorLabel = new JLabel("Zoom Factor: ");
        kaZoomFactorLabel.setLabelFor(kaZoomFactor);
        keyboardAssistanceOptions.add(kaDisplayEnabled);
        keyboardAssistanceOptions.add(kaSpeechEnabled);
        keyboardAssistanceOptions.add(kaZoomFactor);
        keyboardAssistancePanel.add(keyboardAssistanceEnabledLabel);
        keyboardAssistancePanel.add(keyboardAssistanceEnabled);
        keyboardAssistancePanel.add(kaDisplayEnabledLabel);
        keyboardAssistancePanel.add(kaDisplayEnabled);
        keyboardAssistancePanel.add(kaSpeechEnabledLabel);
        keyboardAssistancePanel.add(kaSpeechEnabled);
        keyboardAssistancePanel.add(kaZoomFactorLabel);
        keyboardAssistancePanel.add(kaZoomFactor);
        setComponentsEnabled();
        add(ealfPanel);
        tabPane.add("Widget Zooming", zoomPanel);
        tabPane.add("Extended Tool Tips", extendedToolTipPanel);
        tabPane.add("Text To Speech", textToSpeechPanel);
        tabPane.add("Keyboard Assistance", keyboardAssistancePanel);
        add(tabPane);
        add(saveButton);
        add(cancelButton);
    }

    public void setComponentsEnabled() {
        boolean ealfEn = ealfEnabled.isSelected();
        zoomEnabled.setEnabled(ealfEn);
        textToSpeechEnabled.setEnabled(ealfEn);
        extendedToolTipEnabled.setEnabled(ealfEn);
        keyboardAssistanceEnabled.setEnabled(ealfEn);
        setComponentsEnabled(zoomOptions);
        setComponentsEnabled(textToSpeechOptions);
        setComponentsEnabled(extendedToolTipOptions);
        setComponentsEnabled(keyboardAssistanceOptions);
    }

    public void setComponentsEnabled(List<JComponent> components) {
        boolean enabled = false;
        if (components == zoomOptions) {
            enabled = zoomEnabled.isSelected() && zoomEnabled.isEnabled();
        } else if (components == textToSpeechOptions) {
            enabled = textToSpeechEnabled.isSelected() && textToSpeechEnabled.isEnabled();
        } else if (components == extendedToolTipOptions) {
            enabled = extendedToolTipEnabled.isSelected() && extendedToolTipEnabled.isEnabled();
        } else if (components == keyboardAssistanceOptions) {
            enabled = keyboardAssistanceEnabled.isSelected() && keyboardAssistanceEnabled.isEnabled();
        }
        for (JComponent c : components) {
            c.setEnabled(enabled);
        }
    }

    public void actionPerformed(ActionEvent e) {
        JButton b = (JButton) e.getSource();
        boolean dispose = true;
        if (b.getName().equals("EALF.save")) {
            if (ealfEnabled.isSelected() && !EasyAccessibilityLookAndFeel.isEnabled()) {
                EasyAccessibilityLookAndFeel.install();
            } else if (!ealfEnabled.isSelected() && EasyAccessibilityLookAndFeel.isEnabled()) {
                EasyAccessibilityLookAndFeel.uninstall();
            }
            if (zoomEnabled.isSelected() && !zoomInstance.isEnabled() && EasyAccessibilityLookAndFeel.isEnabled()) {
                zoomInstance.install();
            } else if (!zoomEnabled.isSelected() && zoomInstance.isEnabled() && EasyAccessibilityLookAndFeel.isEnabled()) {
                zoomInstance.uninstall();
            }
            if (extendedToolTipEnabled.isSelected() && !ettInstance.isEnabled() && EasyAccessibilityLookAndFeel.isEnabled()) {
                ettInstance.install();
            } else if (!extendedToolTipEnabled.isSelected() && ettInstance.isEnabled() && EasyAccessibilityLookAndFeel.isEnabled()) {
                ettInstance.uninstall();
            }
            if (textToSpeechEnabled.isSelected() && !ttsInstance.isEnabled() && EasyAccessibilityLookAndFeel.isEnabled()) {
                ttsInstance.install();
            } else if (!textToSpeechEnabled.isSelected() && ttsInstance.isEnabled() && EasyAccessibilityLookAndFeel.isEnabled()) {
                ttsInstance.uninstall();
            }
            if (keyboardAssistanceEnabled.isSelected() && !kaInstance.isEnabled() && EasyAccessibilityLookAndFeel.isEnabled()) {
                kaInstance.install();
            } else if (!keyboardAssistanceEnabled.isSelected() && kaInstance.isEnabled() && EasyAccessibilityLookAndFeel.isEnabled()) {
                kaInstance.uninstall();
            }
            try {
                double d = Double.parseDouble(zoomFactor.getText());
                if (d < 0.25) {
                    JOptionPane.showMessageDialog(this, "Zoom Factor must be greater than 0.25", "Error", JOptionPane.ERROR_MESSAGE);
                    dispose = false;
                } else {
                    zoomInstance.setZoomFactor(d);
                }
            } catch (NumberFormatException e2) {
                JOptionPane.showMessageDialog(this, "Invalid Number for Zoom Factor", "Error", JOptionPane.ERROR_MESSAGE);
                dispose = false;
            }
            try {
                int toolTipDelay = Integer.parseInt(toolTipDelayMs.getText());
                ettInstance.toolTipDelayMs = toolTipDelay;
            } catch (NumberFormatException e2) {
                JOptionPane.showMessageDialog(this, "Tool Tip Delay must be an integral number.", "Error", JOptionPane.ERROR_MESSAGE);
                dispose = false;
            }
            try {
                int extendedToolTipDelay = Integer.parseInt(extendedToolTipDelayMs.getText());
                ettInstance.extendedToolTipDelayMs = extendedToolTipDelay;
            } catch (NumberFormatException e2) {
                JOptionPane.showMessageDialog(this, "Extended Tool Tip Delay must be an integral number.", "Error", JOptionPane.ERROR_MESSAGE);
                dispose = false;
            }
            ettInstance.foregroundColor = foregroundColor.getColor();
            ettInstance.backgroundColor = backgroundColor.getColor();
            ttsInstance.setVoiceString((String) voice.getSelectedItem());
            try {
                double d = Double.parseDouble(kaZoomFactor.getText());
                if (d < 0.25) {
                    JOptionPane.showMessageDialog(this, "Zoom Factor must be greater than 0.25", "Error", JOptionPane.ERROR_MESSAGE);
                    dispose = false;
                } else {
                    kaInstance.setZoomFactor((float) d);
                }
            } catch (NumberFormatException e2) {
                JOptionPane.showMessageDialog(this, "Invalid Number for Zoom Factor", "Error", JOptionPane.ERROR_MESSAGE);
                dispose = false;
            }
            kaInstance.setDisplayEnabled(kaDisplayEnabled.isSelected());
            kaInstance.setSpeechEnabled(kaSpeechEnabled.isSelected());
            EasyAccessibilityLookAndFeel.saveConfig();
        } else if (b.getName().equals("EALF.cancel")) {
        }
        JFrame frame = (JFrame) (this.getRootPane().getTopLevelAncestor());
        if (dispose) {
            frame.dispose();
        }
    }
}
