package org.scubatoolkit;

import java.awt.Color;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextField;
import org.scubatoolkit.utils.GUIUtilities;

/**
 * Description of the Class
 * 
 * @author @version
 */
public class NitrCalcWindow extends JDialog implements ActionListener {

    /**
	 * Constructor for the NitrCalcWindow object
	 * 
	 * @param nitroxCalculator
	 *            Description of the Parameter
	 * @param title
	 *            Description of the Parameter
	 */
    public NitrCalcWindow(AppWindow parent, String title) {
        super(parent, title, true);
        this.parent = parent;
        units.addActionListener(this);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        mainPane = new JPanel();
        leftPane = new JPanel();
        leftPane.setLayout(new BoxLayout(leftPane, BoxLayout.Y_AXIS));
        rightPane = new JPanel();
        rightPane.setLayout(new GridLayout(4, 3));
        rightPane.add(new JLabel("Units: "));
        rightPane.add(units);
        rightPane.add(new JLabel());
        modButton = new JRadioButton("MOD", true);
        modButton.setActionCommand("m");
        modButton.addActionListener(this);
        percentOxygenButton = new JRadioButton("% Oxygen");
        percentOxygenButton.setActionCommand("o");
        percentOxygenButton.addActionListener(this);
        maxOxygenPPButton = new JRadioButton("Max. Oxygen PP");
        maxOxygenPPButton.setActionCommand("p");
        maxOxygenPPButton.addActionListener(this);
        calculationGroup = new ButtonGroup();
        calculationGroup.add(modButton);
        calculationGroup.add(percentOxygenButton);
        calculationGroup.add(maxOxygenPPButton);
        JLabel calculateLabel = new JLabel("Calculate:");
        leftPane.add(calculateLabel);
        leftPane.add(modButton);
        leftPane.add(percentOxygenButton);
        leftPane.add(maxOxygenPPButton);
        modLabel = new JLabel("MOD:");
        modUnitLabel = GUIUtilities.getDepthUnitLabel((UnitsSystem) units.getSelectedItem());
        poLabel = new JLabel("Percent Oxygen:");
        poUnitLabel = new JLabel(" %");
        maxLabel = new JLabel("Max. PPO2:");
        maxUnitLabel = new JLabel(" ATA");
        modText = new JTextField();
        modText.setEditable(false);
        modText.setBackground(Color.lightGray);
        modText.addActionListener(textFieldListener);
        poText = new JTextField();
        poText.addActionListener(textFieldListener);
        maxText = new JTextField();
        maxText.addActionListener(textFieldListener);
        rightPane.add(modLabel);
        rightPane.add(modText);
        rightPane.add(modUnitLabel);
        rightPane.add(poLabel);
        rightPane.add(poText);
        rightPane.add(poUnitLabel);
        rightPane.add(maxLabel);
        rightPane.add(maxText);
        rightPane.add(maxUnitLabel);
        mainPane.add(leftPane);
        mainPane.add(rightPane);
        setContentPane(mainPane);
        pack();
        setResizable(false);
        setVisible(true);
    }

    private void setUnitsLabels(UnitsSystem unitsSystem) {
        String lengthUnit = "ft";
        if (unitsSystem == UnitsSystem.Metric) {
            lengthUnit = "m";
        }
        modUnitLabel.setText(lengthUnit);
    }

    /**
	 * Sets the calculation attribute of the NitrCalcWindow object
	 * 
	 * @param c
	 *            The new calculation value
	 */
    public void setCalculation(String c) {
        if (c == "m") {
            modText.setEditable(false);
            modText.setBackground(Color.lightGray);
            poText.setEditable(true);
            poText.setBackground(Color.white);
            maxText.setEditable(true);
            maxText.setBackground(Color.white);
        }
        if (c == "o") {
            modText.setEditable(true);
            modText.setBackground(Color.white);
            poText.setEditable(false);
            poText.setBackground(Color.lightGray);
            maxText.setEditable(true);
            maxText.setBackground(Color.white);
        }
        if (c == "p") {
            modText.setEditable(true);
            modText.setBackground(Color.white);
            poText.setEditable(true);
            poText.setBackground(Color.white);
            maxText.setEditable(false);
            maxText.setBackground(Color.lightGray);
        }
    }

    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == units) {
            setUnitsLabels((UnitsSystem) units.getSelectedItem());
        }
        setCalculation(e.getActionCommand());
    }

    /**
	 * Description of the Class
	 * 
	 * @author @version
	 */
    public class tfListener implements ActionListener {

        /**
		 * Description of the Method
		 * 
		 * @param e
		 *            Description of the Parameter
		 */
        public void actionPerformed(ActionEvent e) {
            if (modButton.isSelected()) {
                try {
                    Gas nitroxGas = new Gas(Double.parseDouble(poText.getText()) / 100);
                    double gasMOD = nitroxGas.getMOD(Double.parseDouble(maxText.getText()), (UnitsSystem) units.getSelectedItem());
                    modText.setText(Double.toString(Scuba.round(gasMOD, Settings.getInstance().getIntegerSetting("decimal.places", 0))));
                } catch (NumberFormatException nfe) {
                    modText.setText("Error!");
                }
            }
            if (percentOxygenButton.isSelected()) {
                try {
                    double mod = Double.parseDouble(modText.getText());
                    double ata = Double.parseDouble(maxText.getText());
                    poText.setText(Double.toString(Scuba.round(Gas.calculatePercentOxygen(mod, ata, (UnitsSystem) units.getSelectedItem()), Settings.getInstance().getIntegerSetting("decimal.places", 0))));
                } catch (NumberFormatException nfe) {
                    poText.setText("Error!");
                }
            }
            if (maxOxygenPPButton.isSelected()) {
                try {
                    double mod = Double.parseDouble(modText.getText());
                    double fo = Double.parseDouble(poText.getText()) / 100;
                    maxText.setText(Double.toString(Scuba.round(Gas.calculateATA(mod, fo, (UnitsSystem) units.getSelectedItem()), Settings.getInstance().getIntegerSetting("decimal.places", 0))));
                } catch (NumberFormatException nfe) {
                    maxText.setText("Error!");
                }
            }
        }
    }

    private tfListener textFieldListener = new tfListener();

    private AppWindow parent;

    private JLabel modLabel, poLabel, maxLabel, modUnitLabel, poUnitLabel, maxUnitLabel;

    private JRadioButton modButton, percentOxygenButton, maxOxygenPPButton;

    private JTextField modText, poText, maxText;

    private JPanel mainPane, leftPane, rightPane;

    private ButtonGroup calculationGroup;

    private JComboBox units = GUIUtilities.getUnitsCombo();
}
