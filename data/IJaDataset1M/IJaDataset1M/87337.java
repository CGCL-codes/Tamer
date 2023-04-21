package edu.uiuc.itg.virtuallab;

import java.awt.Color;
import java.awt.GridLayout;
import java.text.NumberFormat;
import javax.swing.JLabel;

class StatPanel extends ShadedPanel {

    JLabel xyLabel;

    JLabel wdLabel;

    JLabel kvLabel;

    JLabel mgLabel;

    TapeMeasure tape;

    StatPanel() {
        tape = new TapeMeasure(1, 100);
        xyLabel = new JLabel("X/Y: 0000.0, 0000.0");
        wdLabel = new JLabel("WD:22.0");
        kvLabel = new JLabel("10.0kV");
        mgLabel = new JLabel();
        setBackground(Color.BLACK);
        setForeground(Color.WHITE);
        xyLabel.setForeground(Color.WHITE);
        wdLabel.setForeground(Color.WHITE);
        kvLabel.setForeground(Color.WHITE);
        mgLabel.setForeground(Color.WHITE);
        setXY(0, 0);
        setMag(642);
        setLayout(new GridLayout(1, 4));
        add(xyLabel);
        add(wdLabel);
        add(kvLabel);
        add(mgLabel);
    }

    public void setXY(float x, float y) {
        NumberFormat nf = NumberFormat.getInstance();
        nf.setMaximumFractionDigits(1);
        nf.setMinimumFractionDigits(1);
        nf.setMinimumIntegerDigits(5);
        xyLabel.setText("X/Y: " + nf.format(x) + ", " + nf.format(y));
    }

    public void setMag(int m) {
        mgLabel.setText(m + "x");
    }

    public void setMag(String m) {
        mgLabel.setText(m + "x");
    }
}
