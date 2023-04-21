package game.data.visualizedata;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

class OptionsPanelBoxAndWhisker extends OptionsPanel {

    public OptionsPanelBoxAndWhisker(Graf graf) {
        super(graf);
        initComponents();
        super.add(rightPanel, BorderLayout.CENTER);
    }

    private void initComponents() {
        rightPanel = new JPanel(new GridLayout(3, 1));
        ButtonGroup rbtngAxisX = new ButtonGroup();
        JLabel labAxisX = new JLabel("Axis X:", SwingConstants.CENTER);
        JRadioButton rbtnValue = new JRadioButton("Value", true);
        JRadioButton rbtnNormValue = new JRadioButton("Normalized Value", false);
        rbtngAxisX.add(rbtnValue);
        rbtngAxisX.add(rbtnNormValue);
        rightPanel.add(labAxisX);
        rightPanel.add(rbtnValue);
        rightPanel.add(rbtnNormValue);
        rbtnValue.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                rbtnValueActionPerformed(e);
            }
        });
        rbtnNormValue.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                rbtnNormValueActionPerformed(e);
            }
        });
    }

    private void rbtnValueActionPerformed(ActionEvent e) {
        ((BoxAndWhisker) graf).setNormal(false);
        graf.changeDataset();
    }

    private void rbtnNormValueActionPerformed(ActionEvent e) {
        ((BoxAndWhisker) graf).setNormal(true);
        graf.changeDataset();
    }
}
