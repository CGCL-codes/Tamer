package beastcalc;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.net.URL;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JSpinner;
import javax.swing.JTabbedPane;
import javax.swing.JTextField;
import javax.swing.SpinnerNumberModel;

public class SettingsFrame extends JDialog implements ActionListener {

    /**
	 * 
	 */
    private static final long serialVersionUID = 1L;

    private static int PIC_DIMENSION = 300;

    private JCheckBox[] checkBoxes;

    private JTextField[] textFields;

    private JSpinner[] spinner;

    private JRadioButton degrees;

    private ConfigurationFile newConfigFile, configFile;

    public SettingsFrame(ConfigurationFile configFile) {
        super();
        this.configFile = configFile;
        checkBoxes = new JCheckBox[8];
        textFields = new JTextField[5];
        spinner = new JSpinner[2];
        this.getContentPane().setLayout(new BoxLayout(this.getContentPane(), BoxLayout.PAGE_AXIS));
        JTabbedPane tabbedPane = new JTabbedPane();
        tabbedPane.add("General", createGeneralSettingsPanel());
        tabbedPane.add("BeastCalculator", createCalculatorSettingsPanel());
        tabbedPane.add("BeastGraph", createGraphSettingsPanel());
        this.getContentPane().add(tabbedPane);
        JPanel tempFlow = new JPanel(new FlowLayout());
        JButton button = new JButton("Save");
        button.addActionListener(this);
        tempFlow.add(button);
        button = new JButton("Cancel");
        button.addActionListener(this);
        tempFlow.add(button);
        button = new JButton("Reset to default");
        button.addActionListener(this);
        tempFlow.add(button);
        this.getContentPane().add(tempFlow);
        this.pack();
        this.setResizable(false);
        this.setTitle("Settings");
    }

    private JPanel createGeneralSettingsPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.LINE_AXIS));
        JPanel tempPanel = new JPanel();
        URL url = Main.class.getResource("/resources/images/general.png");
        JLabel label = new JLabel(new ImageIcon(url));
        label.setOpaque(true);
        label.setPreferredSize(new Dimension(PIC_DIMENSION, PIC_DIMENSION));
        label.setBackground(Color.WHITE);
        tempPanel.add(label);
        panel.add(tempPanel);
        tempPanel = new JPanel();
        tempPanel.setLayout(new BoxLayout(tempPanel, BoxLayout.PAGE_AXIS));
        JPanel tempFlow = new JPanel(new FlowLayout());
        checkBoxes[6] = new JCheckBox("Show BeastCalc in System Tray.");
        checkBoxes[6].setSelected(configFile.trayIcon == 0);
        checkBoxes[6].addItemListener(new ItemListener() {

            public void itemStateChanged(ItemEvent e) {
                if (!checkBoxes[6].isSelected()) {
                    checkBoxes[2].setSelected(true);
                    checkBoxes[2].setEnabled(false);
                } else {
                    checkBoxes[2].setEnabled(true);
                }
            }
        });
        checkBoxes[6].setEnabled(configFile.trayIcon != 2);
        tempFlow.add(checkBoxes[6]);
        tempPanel.add(tempFlow);
        if (configFile.trayIcon == 2) {
            tempFlow = new JPanel(new FlowLayout());
            label = new JLabel("Sorry, your system does not support the SystemTray.");
            tempFlow.add(label);
            tempPanel.add(tempFlow);
        }
        tempFlow = new JPanel(new FlowLayout());
        label = new JLabel("Decimal Precision:");
        tempFlow.add(label);
        spinner[1] = new JSpinner(new SpinnerNumberModel(configFile.decimalPrecision > 15 ? 0 : configFile.decimalPrecision, 0, 15, 1));
        ((JSpinner.DefaultEditor) spinner[1].getEditor()).getTextField().setEditable(false);
        spinner[1].setEnabled(configFile.decimalPrecision == 16 ? false : true);
        checkBoxes[4] = new JCheckBox("Float");
        checkBoxes[4].addItemListener(new ItemListener() {

            public void itemStateChanged(ItemEvent e) {
                spinner[1].setEnabled(!checkBoxes[4].isSelected());
            }
        });
        checkBoxes[4].setSelected(configFile.decimalPrecision == 16 ? true : false);
        tempFlow.add(checkBoxes[4]);
        tempFlow.add(spinner[1]);
        tempPanel.add(tempFlow);
        tempFlow = new JPanel(new FlowLayout());
        ButtonGroup group = new ButtonGroup();
        degrees = new JRadioButton("Radians", configFile.radians);
        tempFlow.add(degrees);
        group.add(degrees);
        degrees = new JRadioButton("Degrees", !configFile.radians);
        tempFlow.add(degrees);
        group.add(degrees);
        tempPanel.add(tempFlow);
        tempFlow = new JPanel(new FlowLayout());
        checkBoxes[7] = new JCheckBox("Show negative tip on startup.");
        checkBoxes[7].setSelected(configFile.showNegativeTip);
        tempFlow.add(checkBoxes[7]);
        tempPanel.add(tempFlow);
        panel.add(tempPanel);
        return panel;
    }

    private JPanel createGraphSettingsPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.LINE_AXIS));
        JPanel tempPanel = new JPanel();
        URL url = this.getClass().getResource("/resources/images/graph.png");
        JLabel label = new JLabel(new ImageIcon(url));
        label.setOpaque(true);
        label.setPreferredSize(new Dimension(PIC_DIMENSION, PIC_DIMENSION));
        label.setBackground(Color.WHITE);
        tempPanel.add(label);
        panel.add(tempPanel);
        tempPanel = new JPanel();
        tempPanel.setLayout(new BoxLayout(tempPanel, BoxLayout.PAGE_AXIS));
        JPanel tempFlow = new JPanel(new FlowLayout());
        checkBoxes[0] = new JCheckBox("Open Graph on startup.");
        checkBoxes[0].setSelected(configFile.graphOnOpen);
        tempFlow.add(checkBoxes[0]);
        tempPanel.add(tempFlow);
        tempFlow = new JPanel(new FlowLayout());
        checkBoxes[1] = new JCheckBox("Graph always on top.");
        checkBoxes[1].setSelected(configFile.graphAlwaysOnTop);
        tempFlow.add(checkBoxes[1]);
        tempPanel.add(tempFlow);
        tempFlow = new JPanel(new FlowLayout());
        label = new JLabel("Graph Start X:");
        tempFlow.add(label);
        textFields[2] = new JTextField("" + configFile.graphScreenX, 3);
        tempFlow.add(textFields[2]);
        tempPanel.add(tempFlow);
        tempFlow = new JPanel(new FlowLayout());
        label = new JLabel("Graph Start Y:");
        tempFlow.add(label);
        textFields[3] = new JTextField("" + configFile.graphScreenY, 3);
        tempFlow.add(textFields[3]);
        tempPanel.add(tempFlow);
        tempFlow = new JPanel(new FlowLayout());
        label = new JLabel("Graph Default Size:");
        tempFlow.add(label);
        textFields[4] = new JTextField("" + configFile.graphSize, 3);
        tempFlow.add(textFields[4]);
        tempPanel.add(tempFlow);
        panel.add(tempPanel);
        return panel;
    }

    private JPanel createCalculatorSettingsPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.LINE_AXIS));
        JPanel tempPanel = new JPanel();
        URL url = this.getClass().getResource("/resources/images/calculator.png");
        JLabel label = new JLabel(new ImageIcon(url));
        label.setOpaque(true);
        label.setPreferredSize(new Dimension(PIC_DIMENSION, PIC_DIMENSION));
        label.setBackground(Color.WHITE);
        tempPanel.add(label);
        panel.add(tempPanel);
        tempPanel = new JPanel();
        tempPanel.setLayout(new BoxLayout(tempPanel, BoxLayout.PAGE_AXIS));
        JPanel tempFlow = new JPanel(new FlowLayout());
        checkBoxes[2] = new JCheckBox("Open Calculator on startup.");
        checkBoxes[2].setSelected(configFile.trayIcon == ConfigurationFile.SUPPORTED_WANTED ? configFile.calcOnOpen : true);
        checkBoxes[2].setEnabled(configFile.trayIcon == ConfigurationFile.SUPPORTED_WANTED);
        tempFlow.add(checkBoxes[2]);
        tempPanel.add(tempFlow);
        tempFlow = new JPanel(new FlowLayout());
        checkBoxes[3] = new JCheckBox("Calculator always on top.");
        checkBoxes[3].setSelected(configFile.calcAlwaysOnTop);
        tempFlow.add(checkBoxes[3]);
        tempPanel.add(tempFlow);
        tempFlow = new JPanel(new FlowLayout());
        checkBoxes[5] = new JCheckBox("Calculator output wrap-around.");
        checkBoxes[5].setSelected(configFile.calcWrapAround);
        tempFlow.add(checkBoxes[5]);
        tempPanel.add(tempFlow);
        tempFlow = new JPanel(new FlowLayout());
        label = new JLabel("Calc Start X:");
        tempFlow.add(label);
        textFields[0] = new JTextField("" + configFile.calcScreenX, 3);
        tempFlow.add(textFields[0]);
        tempPanel.add(tempFlow);
        tempFlow = new JPanel(new FlowLayout());
        label = new JLabel("Calc Start Y:");
        tempFlow.add(label);
        textFields[1] = new JTextField("" + configFile.calcScreenY, 3);
        tempFlow.add(textFields[1]);
        tempPanel.add(tempFlow);
        tempFlow = new JPanel(new FlowLayout());
        label = new JLabel("Calculator Output Lines:");
        tempFlow.add(label);
        spinner[0] = new JSpinner(new SpinnerNumberModel(configFile.outputLines, 3, 10, 1));
        ((JSpinner.DefaultEditor) spinner[0].getEditor()).getTextField().setEditable(false);
        tempFlow.add(spinner[0]);
        tempPanel.add(tempFlow);
        panel.add(tempPanel);
        return panel;
    }

    public ConfigurationFile getNewConfigurationFile() {
        return newConfigFile;
    }

    public void actionPerformed(ActionEvent e) {
        if (((JButton) e.getSource()).getText().equals("Save")) {
            try {
                String exceptions = "";
                newConfigFile = new ConfigurationFile();
                newConfigFile.graphOnOpen = checkBoxes[0].isSelected();
                newConfigFile.graphAlwaysOnTop = checkBoxes[1].isSelected();
                newConfigFile.calcOnOpen = checkBoxes[2].isSelected();
                newConfigFile.calcAlwaysOnTop = checkBoxes[3].isSelected();
                newConfigFile.calcWrapAround = checkBoxes[5].isSelected();
                newConfigFile.showNegativeTip = checkBoxes[7].isSelected();
                int i = Integer.parseInt(textFields[0].getText());
                if (i > Main.screenWidth || i < 0) exceptions += "invalid calc start x\n"; else newConfigFile.calcScreenX = i;
                i = Integer.parseInt(textFields[1].getText());
                if (i > Main.screenWidth || i < 0) exceptions += "invalid calc start y\n"; else newConfigFile.calcScreenY = i;
                i = Integer.parseInt(textFields[2].getText());
                if (i > Main.screenWidth || i < 0) exceptions += "invalid graph start x\n"; else newConfigFile.graphScreenX = i;
                i = Integer.parseInt(textFields[3].getText());
                if (i > Main.screenWidth || i < 0) exceptions += "invalid calc start y\n"; else newConfigFile.graphScreenY = i;
                i = Integer.parseInt(textFields[4].getText());
                if (i < 440 || i > Main.screenHeight || i > Main.screenWidth) exceptions += "invalid graph size\n"; else newConfigFile.graphSize = i;
                i = (Integer) spinner[0].getValue();
                newConfigFile.outputLines = i;
                if (checkBoxes[4].isSelected()) newConfigFile.decimalPrecision = 16; else newConfigFile.decimalPrecision = (Integer) spinner[1].getValue();
                newConfigFile.radians = !degrees.isSelected();
                if (checkBoxes[6].isEnabled()) newConfigFile.trayIcon = checkBoxes[6].isSelected() ? 0 : 1;
                if (exceptions.length() > 0) throw new Exception(exceptions);
                this.setVisible(false);
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Screen positions and sizes must be positive integers only.", "Error", JOptionPane.INFORMATION_MESSAGE);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, ex.getMessage(), "Error", JOptionPane.INFORMATION_MESSAGE);
            }
        } else if (((JButton) e.getSource()).getText().equals("Cancel")) {
            this.setVisible(false);
        } else if (((JButton) e.getSource()).getText().equals("Reset to default")) {
            int i = JOptionPane.showConfirmDialog(this, "This will erase all current settings and reset them to the default.  Would you like to continue?", "Confirmation", JOptionPane.YES_NO_OPTION);
            if (i == 0) {
                newConfigFile = new ConfigurationFile();
                this.setVisible(false);
            }
        }
    }
}
