package com.turtle3d.gui.visualoptions;

import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JColorChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.WindowConstants;

/**
* This code was edited or generated using CloudGarden's Jigloo
* SWT/Swing GUI Builder, which is free for non-commercial
* use. If Jigloo is being used commercially (ie, by a corporation,
* company or business for any purpose whatever) then you
* should purchase a license for each developer using Jigloo.
* Please visit www.cloudgarden.com for details.
* Use of Jigloo implies acceptance of these licensing terms.
* A COMMERCIAL LICENSE HAS NOT BEEN PURCHASED FOR
* THIS MACHINE, SO JIGLOO OR THIS CODE CANNOT BE USED
* LEGALLY FOR ANY CORPORATE OR COMMERCIAL PURPOSE.
*/
public class VisualOptionsPanel extends javax.swing.JPanel {

    private JLabel jLabel_VisualOptions;

    private JButton jButton_Crop;

    private JButton jButton_BackgroundColor;

    private JCheckBox jCheckBox_AA;

    public static String AA = "AA", BG_COLOR = "BgColor", CROP = "Crop";

    private PropertyChangeListener propertyChangeListener;

    /**
	* Auto-generated main method to display this 
	* JPanel inside a new JFrame.
	*/
    public static void main(String[] args) {
        JFrame frame = new JFrame();
        frame.getContentPane().add(new VisualOptionsPanel());
        frame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
    }

    public VisualOptionsPanel() {
        super();
        initGUI();
    }

    private void initGUI() {
        try {
            GridBagLayout thisLayout = new GridBagLayout();
            thisLayout.rowWeights = new double[] { 0.0, 0.1, 0.1, 0.1, 1.0 };
            thisLayout.rowHeights = new int[] { 7, 7, 7, 7, 7 };
            thisLayout.columnWeights = new double[] { 0.1 };
            thisLayout.columnWidths = new int[] { 7 };
            this.setLayout(thisLayout);
            this.setPreferredSize(new java.awt.Dimension(236, 465));
            {
                jLabel_VisualOptions = new JLabel();
                this.add(jLabel_VisualOptions, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
                jLabel_VisualOptions.setText("Visual Options");
            }
            {
                jCheckBox_AA = new JCheckBox();
                this.add(jCheckBox_AA, new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 25, 0, 0), 0, 0));
                jCheckBox_AA.setText("Antialiasing");
                jCheckBox_AA.addActionListener(new ActionListener() {

                    public void actionPerformed(ActionEvent evt) {
                        propertyChangeListener.propertyChange(new PropertyChangeEvent(this, AA, null, jCheckBox_AA.isSelected()));
                    }
                });
            }
            {
                jButton_BackgroundColor = new JButton();
                this.add(jButton_BackgroundColor, new GridBagConstraints(0, 2, 1, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 25, 0, 0), 0, 0));
                jButton_BackgroundColor.setText("Background Color");
                jButton_BackgroundColor.addActionListener(new ActionListener() {

                    public void actionPerformed(ActionEvent evt) {
                        Color newColor = JColorChooser.showDialog(jButton_BackgroundColor, "Choose background color", new Color(0, 0, 1));
                        propertyChangeListener.propertyChange(new PropertyChangeEvent(this, BG_COLOR, null, newColor));
                    }
                });
            }
            {
                jButton_Crop = new JButton();
                this.add(jButton_Crop, new GridBagConstraints(0, 3, 1, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 25, 0, 0), 0, 0));
                jButton_Crop.setText("Crop");
                jButton_Crop.addActionListener(new ActionListener() {

                    public void actionPerformed(ActionEvent evt) {
                        propertyChangeListener.propertyChange(new PropertyChangeEvent(this, CROP, null, null));
                    }
                });
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public PropertyChangeListener getPropertyChangeListener() {
        return propertyChangeListener;
    }

    public void setPropertyChangeListener(PropertyChangeListener propertyChangeListener) {
        this.propertyChangeListener = propertyChangeListener;
    }
}
