package gov.sns.apps.xyzcorrelator;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.event.*;
import javax.swing.colorchooser.*;
import java.text.NumberFormat;
import java.text.DecimalFormat;
import javax.swing.text.Document;
import gov.sns.tools.swing.*;

/** 
 * Color legend display for xy plot with color as the 'z' axis.
 * The reason for using ColorChooserPanel is maybe in the future the
 * colors can be set by user.
 * @version   0.1  24 Mar 2003
 * @author C.M. Chu
*/
public class ColorLegend extends AbstractColorChooserPanel {

    Color[] myColors;

    JButton[] colorButtons = { new JButton(), new JButton(), new JButton(), new JButton(), new JButton(), new JButton(), new JButton(), new JButton(), new JButton(), new JButton(), new JButton(), new JButton() };

    DecimalField zMaxField;

    DecimalField zMinField;

    private NumberFormat numberFormat;

    protected BubbleColor myBubbleColor;

    JLabel colorLabelMax, colorLabelMin;

    JLabel[] colorLabel = { new JLabel(), new JLabel(), new JLabel(), new JLabel(), new JLabel(), new JLabel(), new JLabel(), new JLabel(), new JLabel(), new JLabel() };

    public ColorLegend(BubbleColor bubbleColors) {
        super();
        myBubbleColor = bubbleColors;
        myColors = bubbleColors.getColors();
        numberFormat = NumberFormat.getNumberInstance();
        ((DecimalFormat) numberFormat).setMaximumFractionDigits(3);
    }

    public void updateChooser() {
        Color color = getColorFromModel();
        for (int i = 0; i < myColors.length; i++) {
            if (color.equals(myColors[i])) colorButtons[i].setSelected(true);
        }
    }

    protected void buildChooser() {
        setLayout(new GridLayout(0, 2));
        ButtonGroup boxOfCrayons = new ButtonGroup();
        ColorListener cl = new ColorListener();
        double lower, upper;
        String sl, su;
        JLabel zMax_label = new JLabel("ZMax:");
        zMaxField = new DecimalField();
        zMaxField.setColumns(9);
        zMaxField.setValue(myBubbleColor.getZMax());
        zMaxField.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        zMaxField.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                myBubbleColor.setZMax(zMaxField.getValue());
                setColorLabels();
            }
        });
        add(zMax_label);
        add(zMaxField);
        for (int i = myColors.length - 1; i > 0; i--) {
            colorButtons[i].setPreferredSize(new Dimension(60, 15));
            colorButtons[i].setMaximumSize(new Dimension(80, 20));
        }
        colorButtons[myColors.length - 1].setBackground(myColors[myColors.length - 1]);
        colorButtons[myColors.length - 1].setActionCommand(String.valueOf(myColors.length - 1));
        colorButtons[myColors.length - 1].addActionListener(cl);
        colorLabelMax = new JLabel(String.valueOf(myBubbleColor.getZMax()) + " - INF");
        boxOfCrayons.add(colorButtons[myColors.length - 1]);
        add(colorButtons[myColors.length - 1]);
        add(colorLabelMax);
        double interval = (myBubbleColor.getZMax() - myBubbleColor.getZMin()) / ((double) myColors.length - 2.0);
        for (int i = myColors.length - 2; i > 0; i--) {
            colorButtons[i].setBackground(myColors[i]);
            colorButtons[i].setActionCommand(String.valueOf(i));
            colorButtons[i].addActionListener(cl);
            upper = myBubbleColor.getZMin() + i * interval;
            lower = myBubbleColor.getZMin() + (i - 1.) * interval;
            su = numberFormat.format(upper);
            sl = numberFormat.format(lower);
            colorLabel[i - 1].setText(su + " - " + sl);
            boxOfCrayons.add(colorButtons[i]);
            add(colorButtons[i]);
            add(colorLabel[i - 1]);
        }
        colorButtons[0].setBackground(myColors[0]);
        colorButtons[0].setActionCommand(String.valueOf(0));
        colorButtons[0].addActionListener(cl);
        colorLabelMin = new JLabel("-INF - " + String.valueOf(myBubbleColor.getZMin()));
        boxOfCrayons.add(colorButtons[0]);
        add(colorButtons[0]);
        add(colorLabelMin);
        JLabel zMin_label = new JLabel("ZMin:");
        zMinField = new DecimalField();
        zMinField.setColumns(9);
        zMinField.setValue(myBubbleColor.getZMin());
        zMinField.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        zMinField.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                myBubbleColor.setZMin(zMinField.getValue());
                setColorLabels();
            }
        });
        add(zMin_label);
        add(zMinField);
    }

    /** loop through the colors and set scales based on new max/min pair */
    protected void setColorLabels() {
        double upper, lower;
        String su, sl;
        NumberFormat nf = NumberFormat.getNumberInstance();
        nf.setMaximumFractionDigits(3);
        zMaxField.setValue(myBubbleColor.getZMax());
        zMinField.setValue(myBubbleColor.getZMin());
        su = nf.format(myBubbleColor.getZMax());
        sl = nf.format(myBubbleColor.getZMin());
        colorLabelMax.setText(su + " - INF");
        colorLabelMin.setText("-INF - " + sl);
        double interval = (myBubbleColor.getZMax() - myBubbleColor.getZMin()) / ((double) myColors.length - 2.0);
        for (int i = myColors.length - 2; i > 0; i--) {
            upper = myBubbleColor.getZMin() + i * interval;
            lower = myBubbleColor.getZMin() + (i - 1.) * interval;
            su = nf.format(upper);
            sl = nf.format(lower);
            colorLabel[i - 1].setText(su + " - " + sl);
        }
    }

    public String getDisplayName() {
        return "Colors";
    }

    public Icon getSmallDisplayIcon() {
        return null;
    }

    public Icon getLargeDisplayIcon() {
        return null;
    }

    class ColorListener implements ActionListener {

        public void actionPerformed(ActionEvent e) {
            Color newColor = null;
            JButton source = (JButton) e.getSource();
            for (int i = 0; i < myColors.length; i++) {
                if (source.getActionCommand().equals(String.valueOf(i))) colorButtons[i].setBackground(myColors[i]);
                colorButtons[i].setSelected(false);
                newColor = myColors[i];
            }
            getColorSelectionModel().setSelectedColor(newColor);
        }
    }

    /** set the max color scale */
    public void setZMax(double val) {
        myBubbleColor.setZMax(val);
        setColorLabels();
    }

    /** set the min color scale */
    public void setZMin(double val) {
        myBubbleColor.setZMin(val);
        setColorLabels();
    }
}
