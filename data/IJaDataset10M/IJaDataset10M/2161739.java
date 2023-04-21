package game.data.visualizedata;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

class OptionsPanelVectorPlot2 extends OptionsPanel {

    public OptionsPanelVectorPlot2(Graf graf) {
        super(graf);
        initComponents();
        super.add(rightPanel, BorderLayout.CENTER);
    }

    private void initComponents() {
        GridBagLayout lay = new GridBagLayout();
        GridBagConstraints c = new GridBagConstraints();
        c.fill = GridBagConstraints.HORIZONTAL;
        c.ipady = 10;
        c.ipadx = 10;
        rightPanel = new JPanel(lay);
        ButtonGroup rbtngAxisX = new ButtonGroup();
        JRadioButton rbtnValue = new JRadioButton("Value", true);
        JRadioButton rbtnNormValue = new JRadioButton("Normalized Value", false);
        JLabel labX = new JLabel("Axis X:");
        JLabel labY = new JLabel("Axis Y:");
        JLabel labDeltaX = new JLabel("Angle:");
        JLabel labDeltaY = new JLabel("Length:");
        JComboBox comboX = new JComboBox(graf.seriesIName);
        JComboBox comboY = new JComboBox(graf.seriesIName);
        JComboBox comboDeltaX = new JComboBox(graf.seriesIName);
        JComboBox comboDeltaY = new JComboBox(graf.seriesIName);
        if (graf.seriesICount >= 4) {
            comboX.setSelectedIndex(0);
            comboY.setSelectedIndex(1);
            comboDeltaX.setSelectedIndex(2);
            comboDeltaY.setSelectedIndex(3);
        }
        rbtngAxisX.add(rbtnValue);
        rbtngAxisX.add(rbtnNormValue);
        c.gridx = 0;
        c.gridy = 0;
        c.gridx = 0;
        c.gridy = 1;
        rightPanel.add(rbtnValue, c);
        c.gridx = 0;
        c.gridy = 2;
        rightPanel.add(rbtnNormValue, c);
        c.gridx = 1;
        c.gridy = 0;
        rightPanel.add(labX, c);
        c.gridx = 1;
        c.gridy = 1;
        rightPanel.add(labY, c);
        c.gridx = 2;
        c.gridy = 0;
        rightPanel.add(comboX, c);
        c.gridx = 2;
        c.gridy = 1;
        rightPanel.add(comboY, c);
        c.gridx = 3;
        c.gridy = 0;
        rightPanel.add(labDeltaX, c);
        c.gridx = 3;
        c.gridy = 1;
        rightPanel.add(labDeltaY, c);
        c.gridx = 4;
        c.gridy = 0;
        rightPanel.add(comboDeltaX, c);
        c.gridx = 4;
        c.gridy = 1;
        rightPanel.add(comboDeltaY, c);
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
        comboX.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                comboXActionPerformed(e);
            }
        });
        comboY.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                comboYActionPerformed(e);
            }
        });
        comboDeltaX.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                comboDeltaXActionPerformed(e);
            }
        });
        comboDeltaY.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                comboDeltaYActionPerformed(e);
            }
        });
    }

    private void rbtnValueActionPerformed(ActionEvent e) {
        ((VectorPlot2) graf).setNormal(false);
        graf.changeDataset();
    }

    private void rbtnNormValueActionPerformed(ActionEvent e) {
        ((VectorPlot2) graf).setNormal(true);
        graf.changeDataset();
    }

    private void comboXActionPerformed(ActionEvent e) {
        JComboBox box = (JComboBox) e.getSource();
        ((VectorPlot2) graf).setX(box.getSelectedIndex());
        graf.changeDataset();
    }

    private void comboYActionPerformed(ActionEvent e) {
        JComboBox box = (JComboBox) e.getSource();
        ((VectorPlot2) graf).setY(box.getSelectedIndex());
        graf.changeDataset();
    }

    private void comboDeltaXActionPerformed(ActionEvent e) {
        JComboBox box = (JComboBox) e.getSource();
        ((VectorPlot2) graf).setAlfa(box.getSelectedIndex());
        graf.changeDataset();
    }

    private void comboDeltaYActionPerformed(ActionEvent e) {
        JComboBox box = (JComboBox) e.getSource();
        ((VectorPlot2) graf).setLength(box.getSelectedIndex());
        graf.changeDataset();
    }
}
