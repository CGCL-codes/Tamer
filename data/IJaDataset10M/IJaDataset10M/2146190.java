package mekhangar.design.gui;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.JTabbedPane;
import mekhangar.design.MechDesign;

public class MechDesignerFrame extends JFrame {

    private MechDesigner designer;

    private JTabbedPane tabs;

    private JPanel mainPanel;

    private JPanel armorPanel;

    private JPanel statusPanel;

    public MechDesignerFrame(MechDesigner designer) {
        this.designer = designer;
        Container contentPane = getContentPane();
        contentPane.setLayout(new BorderLayout());
        initTabs();
        contentPane.add(tabs, BorderLayout.CENTER);
        initStatusPanel();
        contentPane.add(statusPanel, BorderLayout.SOUTH);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        pack();
    }

    private void initTabs() {
        tabs = new JTabbedPane();
        initMainTab();
        initArmorTab();
        initEquipmentTab();
    }

    private void initMainTab() {
        JLabel label = null;
        CustomComboBox comboBox = null;
        IntegerButton integerButton = null;
        mainPanel = new JPanel();
        mainPanel.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.anchor = GridBagConstraints.LINE_START;
        gbc.insets = new Insets(3, 3, 3, 3);
        int yPos = 0;
        gbc.gridx = 0;
        gbc.gridy = yPos;
        gbc.gridwidth = 1;
        label = new JLabel("Tonnage:");
        mainPanel.add(label, gbc);
        gbc.gridx = 1;
        gbc.gridy = yPos;
        gbc.gridwidth = 3;
        comboBox = new CustomComboBox(designer.getTonnageModel());
        mainPanel.add(comboBox, gbc);
        yPos++;
        gbc.gridx = 0;
        gbc.gridy = yPos;
        gbc.gridwidth = 1;
        label = new JLabel("Movement:");
        mainPanel.add(label, gbc);
        gbc.gridx = 1;
        gbc.gridy = yPos;
        label = new MovementLabel(designer.getWalkingMPModel(), designer.getRunningMPModel(), designer.getJumpingMPModel());
        mainPanel.add(label, gbc);
        gbc.gridx = 2;
        gbc.gridy = yPos;
        integerButton = new IntegerButton(designer.getWalkingMPModel(), true);
        mainPanel.add(integerButton, gbc);
        gbc.gridx = 3;
        gbc.gridy = yPos;
        integerButton = new IntegerButton(designer.getWalkingMPModel(), false);
        mainPanel.add(integerButton, gbc);
        yPos++;
        gbc.gridx = 0;
        gbc.gridy = yPos;
        gbc.gridwidth = 1;
        label = new JLabel("Tech Level:");
        mainPanel.add(label, gbc);
        gbc.gridx = 1;
        gbc.gridy = yPos;
        gbc.gridwidth = 3;
        comboBox = new CustomComboBox(designer.getTechLevelModel());
        mainPanel.add(comboBox, gbc);
        yPos++;
        gbc.gridx = 0;
        gbc.gridy = yPos;
        gbc.gridwidth = 1;
        label = new JLabel("Tech Base:");
        mainPanel.add(label, gbc);
        gbc.gridx = 1;
        gbc.gridy = yPos;
        gbc.gridwidth = 3;
        comboBox = new CustomComboBox(designer.getTechBaseModel());
        mainPanel.add(comboBox, gbc);
        yPos++;
        gbc.gridx = 0;
        gbc.gridy = yPos;
        gbc.gridwidth = 1;
        label = new JLabel("Chassis:");
        mainPanel.add(label, gbc);
        gbc.gridx = 1;
        gbc.gridy = yPos;
        gbc.gridwidth = 3;
        comboBox = new CustomComboBox(designer.getChassisTypeModel());
        mainPanel.add(comboBox, gbc);
        yPos++;
        gbc.gridx = 0;
        gbc.gridy = yPos;
        gbc.gridwidth = 1;
        label = new JLabel("Myomer:");
        mainPanel.add(label, gbc);
        gbc.gridx = 1;
        gbc.gridy = yPos;
        gbc.gridwidth = 3;
        comboBox = new CustomComboBox(designer.getMyomerTypeModel());
        mainPanel.add(comboBox, gbc);
        yPos++;
        gbc.gridx = 0;
        gbc.gridy = yPos;
        gbc.gridwidth = 1;
        label = new JLabel("Internals:");
        mainPanel.add(label, gbc);
        gbc.gridx = 1;
        gbc.gridy = yPos;
        gbc.gridwidth = 3;
        comboBox = new CustomComboBox(designer.getInternalTypeModel());
        mainPanel.add(comboBox, gbc);
        yPos++;
        gbc.gridx = 0;
        gbc.gridy = yPos;
        gbc.gridwidth = 1;
        label = new JLabel("Armor:");
        mainPanel.add(label, gbc);
        gbc.gridx = 1;
        gbc.gridy = yPos;
        gbc.gridwidth = 3;
        comboBox = new CustomComboBox(designer.getArmorTypeModel());
        mainPanel.add(comboBox, gbc);
        yPos++;
        gbc.gridx = 0;
        gbc.gridy = yPos;
        gbc.gridwidth = 1;
        label = new JLabel("Engine:");
        mainPanel.add(label, gbc);
        gbc.gridx = 1;
        gbc.gridy = yPos;
        gbc.gridwidth = 3;
        comboBox = new CustomComboBox(designer.getEngineTypeModel());
        mainPanel.add(comboBox, gbc);
        yPos++;
        gbc.gridx = 0;
        gbc.gridy = yPos;
        gbc.gridwidth = 1;
        label = new JLabel("Heat Sinks:");
        mainPanel.add(label, gbc);
        gbc.gridx = 1;
        gbc.gridy = yPos;
        gbc.gridwidth = 3;
        comboBox = new CustomComboBox(designer.getHeatSinkTypeModel());
        mainPanel.add(comboBox, gbc);
        yPos++;
        gbc.gridx = 0;
        gbc.gridy = yPos;
        gbc.gridwidth = 1;
        label = new JLabel("Cockpit:");
        mainPanel.add(label, gbc);
        gbc.gridx = 1;
        gbc.gridy = yPos;
        gbc.gridwidth = 3;
        comboBox = new CustomComboBox(designer.getCockpitTypeModel());
        mainPanel.add(comboBox, gbc);
        yPos++;
        gbc.gridx = 0;
        gbc.gridy = yPos;
        gbc.gridwidth = 1;
        label = new JLabel("Gyro:");
        mainPanel.add(label, gbc);
        gbc.gridx = 1;
        gbc.gridy = yPos;
        gbc.gridwidth = 3;
        comboBox = new CustomComboBox(designer.getGyroTypeModel());
        mainPanel.add(comboBox, gbc);
        yPos++;
        JPanel panel = new JPanel();
        panel.setLayout(new GridBagLayout());
        GridBagConstraints gbc2 = new GridBagConstraints();
        gbc2.anchor = GridBagConstraints.FIRST_LINE_START;
        gbc2.weightx = 1.0;
        gbc2.weighty = 1.0;
        panel.add(mainPanel, gbc2);
        tabs.add("Main", panel);
    }

    private void initArmorTab() {
        JLabel nameLabel = null;
        IntegerLabel valueLabel = null;
        JSlider slider = null;
        armorPanel = new JPanel();
        armorPanel.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.anchor = GridBagConstraints.FIRST_LINE_START;
        gbc.insets = new Insets(3, 3, 3, 3);
        for (int i = 0; i < MechDesign.ARMOR_LOCS.length; i++) {
            String loc = MechDesign.ARMOR_LOCS[i];
            slider = new JSlider(designer.getLocationArmorModel(loc));
            slider.setSnapToTicks(true);
            valueLabel = new IntegerLabel(designer.getLocationArmorModel(loc));
            gbc.gridy = i;
            if (i > 3) {
                gbc.gridy = i - 3;
            }
            if (!getDesign().isRearSide(loc)) {
                gbc.gridx = 0;
                nameLabel = new JLabel(MechDesigner.ARMOR_NAMES[i]);
                armorPanel.add(nameLabel, gbc);
            }
            gbc.gridx = 1;
            if (getDesign().isRearSide(loc)) {
                gbc.gridx = 3;
            }
            armorPanel.add(slider, gbc);
            gbc.gridx = 2;
            if (getDesign().isRearSide(loc)) {
                gbc.gridx = 4;
            }
            armorPanel.add(valueLabel, gbc);
        }
        JPanel panel = new JPanel();
        panel.setLayout(new GridBagLayout());
        GridBagConstraints gbc2 = new GridBagConstraints();
        gbc2.anchor = GridBagConstraints.FIRST_LINE_START;
        gbc2.weightx = 1.0;
        gbc2.weighty = 1.0;
        panel.add(armorPanel, gbc2);
        tabs.add("Armor", panel);
    }

    private void initEquipmentTab() {
        tabs.add("Equipment", new EquipmentEditorPanel(designer));
    }

    private void initStatusPanel() {
        statusPanel = new JPanel();
        JLabel label = new JLabel("Weight Used:");
        statusPanel.add(label);
        TonnageUsageLabel usageLabel = new TonnageUsageLabel(designer.getUsedTonnageValueModel(), designer.getTonnageValueModel());
        statusPanel.add(usageLabel);
    }

    private MechDesign getDesign() {
        return designer.getDesign();
    }
}
