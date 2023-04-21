package com.fundboss.display;

import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import model.market.MarketConfig;

public class SpecialParamPanel extends JPanel {

    public SpecialParamPanel(final MarketConfig config) {
        setLayout(new GridLayout(0, 2));
        JLabel title = new JLabel("Special");
        title.setFont(new Font("Arial", Font.BOLD, 18));
        add(title);
        add(new JLabel(""));
        add(new JLabel("Shock investor worth"));
        final JComboBox shockWorth = new JComboBox(new Object[] { ".5", "0", "-.125", "-.25", "-.5", "-.75" });
        shockWorth.setSelectedItem("0");
        config.setSubtractFractionShock(0);
        add(shockWorth);
        shockWorth.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                String option = (String) shockWorth.getSelectedItem();
                config.setSubtractFractionShock(Double.parseDouble(option));
            }
        });
        add(new JLabel("Value Shock Asset 0:"));
        final JComboBox asset0Shock = new JComboBox(new Object[] { "-80%", "-40%", "0%", "40%", "100%" });
        asset0Shock.setSelectedItem("0%");
        config.setAsset0Shock(0);
        asset0Shock.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                Number number = AssetsParamPanel.getPercent(asset0Shock.getSelectedItem());
                config.setAsset0Shock((Double) number);
            }
        });
        add(asset0Shock);
        add(new JLabel("Oscillate Risk free?"));
        final JCheckBox oscillateRiskFree = new JCheckBox("", false);
        config.setOscillateRiskFree(false);
        oscillateRiskFree.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                config.setOscillateRiskFree(oscillateRiskFree.isSelected());
            }
        });
        add(oscillateRiskFree);
    }
}
