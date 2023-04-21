package org.tigr.microarray.mev.cluster.gui.impl.hcl;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Frame;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.KeyListener;
import java.awt.event.WindowEvent;
import java.util.Hashtable;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSeparator;
import javax.swing.JSlider;
import javax.swing.JTextField;
import javax.swing.UIManager;
import javax.swing.border.BevelBorder;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;
import javax.swing.event.ChangeListener;
import org.tigr.microarray.mev.cluster.gui.impl.dialogs.AlgorithmDialog;
import org.tigr.microarray.mev.cluster.gui.impl.dialogs.DialogListener;
import org.tigr.microarray.mev.cluster.gui.impl.dialogs.dialogHelpUtil.HelpWindow;

public class HCLConfigDialog extends AlgorithmDialog {

    private int result;

    private JTextField zeroTextField;

    private JTextField minTextField;

    private JTextField maxTextField;

    private JTextField numTerminalsField;

    private float zThr;

    private int minPixDist;

    private int maxPixDist;

    private float origZThr;

    private int origMinPixDist;

    private int origMaxPixDist;

    private float minDist;

    private float maxDist;

    private float distRange;

    private JButton applyButton;

    private JCheckBox createClusterViewsBox;

    private JSlider slider;

    private HCLTree tree;

    private HCLViewer viewer;

    /**
     * Constructs the dialog.
     */
    public HCLConfigDialog(Frame parent, HCLViewer viewer, float zeroThreshold, int minPixelDistance, int maxPixelDistance, float minDist, float maxDist) {
        super(parent, "Tree Configuration", true);
        this.viewer = viewer;
        zThr = zeroThreshold;
        this.minDist = minDist;
        this.maxDist = maxDist;
        this.origZThr = zeroThreshold;
        this.origMinPixDist = minPixelDistance;
        this.origMaxPixDist = maxPixelDistance;
        this.distRange = maxDist - minDist;
        minPixDist = minPixelDistance;
        maxPixDist = maxPixelDistance;
        Listener listener = new Listener();
        addWindowListener(listener);
        JPanel cutOffPanel = new JPanel(new GridBagLayout());
        cutOffPanel.setBackground(Color.white);
        cutOffPanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(EtchedBorder.LOWERED), "Distance Treshold Adjustment", TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION, new Font("Dialog", Font.BOLD, 12), Color.black));
        cutOffPanel.add(new JLabel("Distance threshold"), new GridBagConstraints(0, 0, 1, 1, 0, 0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 10), 0, 0));
        String z = String.valueOf(zeroThreshold);
        z = z.substring(0, Math.min(5, z.length()));
        zeroTextField = new JTextField(z, 4);
        cutOffPanel.add(zeroTextField, new GridBagConstraints(1, 0, 1, 1, 0, 0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
        slider = new JSlider(JSlider.HORIZONTAL, 0, 1000, 0);
        slider.setBackground(Color.white);
        slider.setValue((int) (1000 * ((zThr - this.minDist) / (this.maxDist - this.minDist))));
        Hashtable labelTable = new Hashtable();
        labelTable.put(new Integer(0), new JLabel("Min"));
        labelTable.put(new Integer(500), new JLabel("(Distance Range)"));
        labelTable.put(new Integer(1000), new JLabel("Max"));
        slider.setLabelTable(labelTable);
        slider.setPaintLabels(true);
        cutOffPanel.add(slider, new GridBagConstraints(0, 1, 2, 1, 0, 0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(10, 0, 10, 0), 0, 0));
        cutOffPanel.add(new JSeparator(JSeparator.VERTICAL), new GridBagConstraints(2, 0, 1, 2, 0, 0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(5, 10, 5, 10), 0, 0));
        cutOffPanel.add(new JLabel("# of Terminal Nodes:"), new GridBagConstraints(3, 0, 1, 1, 0, 0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 10), 0, 0));
        numTerminalsField = new JTextField(6);
        numTerminalsField.setEditable(false);
        numTerminalsField.setSize(90, 25);
        numTerminalsField.setPreferredSize(new Dimension(90, 25));
        cutOffPanel.add(numTerminalsField, new GridBagConstraints(4, 0, 1, 1, 0, 0, GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
        createClusterViewsBox = new JCheckBox("Create Cluster Viewers", false);
        createClusterViewsBox.setFocusPainted(false);
        createClusterViewsBox.setBackground(Color.white);
        createClusterViewsBox.setForeground(UIManager.getColor("Label.foreground"));
        cutOffPanel.add(createClusterViewsBox, new GridBagConstraints(3, 1, 2, 1, 0, 0, GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
        JPanel treeDimPanel = new JPanel(new GridBagLayout());
        treeDimPanel.setBackground(Color.white);
        treeDimPanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(EtchedBorder.LOWERED), "Tree Dimension Parameters", TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION, new Font("Dialog", Font.BOLD, 12), Color.black));
        treeDimPanel.add(new JLabel("Minimum pixel height"), new GridBagConstraints(0, 0, 1, 1, 0.3, 0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 20, 0, 10), 0, 0));
        treeDimPanel.add(new JLabel("Maximum pixel height"), new GridBagConstraints(0, 1, 1, 1, 0.3, 0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 20, 0, 10), 0, 0));
        minTextField = new JTextField(String.valueOf(minPixelDistance), 4);
        treeDimPanel.add(minTextField, new GridBagConstraints(1, 0, 1, 1, 0.3, 0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
        maxTextField = new JTextField(String.valueOf(maxPixelDistance), 4);
        treeDimPanel.add(maxTextField, new GridBagConstraints(1, 1, 1, 1, 0.3, 0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
        applyButton = new JButton("Apply Dimensions");
        applyButton.setActionCommand("apply-tree-dimensions-command");
        applyButton.setFocusPainted(false);
        applyButton.setMargin(new Insets(5, 15, 5, 15));
        applyButton.setSize(110, 30);
        applyButton.setPreferredSize(new Dimension(120, 30));
        applyButton.setBorder(BorderFactory.createBevelBorder(BevelBorder.RAISED, Color.lightGray, Color.gray));
        treeDimPanel.add(applyButton, new GridBagConstraints(2, 0, 1, 2, 0.8, 0, GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
        JPanel panel3 = new JPanel(new BorderLayout());
        panel3.setForeground(Color.white);
        panel3.setBorder(BorderFactory.createLineBorder(Color.gray));
        panel3.setBackground(Color.white);
        panel3.add(cutOffPanel, BorderLayout.NORTH);
        panel3.add(treeDimPanel, BorderLayout.CENTER);
        JPanel panel1 = new JPanel(new BorderLayout());
        panel1.add(panel3, BorderLayout.CENTER);
        addContent(panel1);
        slider.addChangeListener(listener);
        zeroTextField.addKeyListener(listener);
        applyButton.addActionListener(listener);
        setActionListeners(listener);
        this.pack();
    }

    /**
     * Show the dialog in screen's center.
     */
    public int showModal() {
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        setLocation((screenSize.width - getSize().width) / 2, (screenSize.height - getSize().height) / 2);
        show();
        return result;
    }

    /**
     * Associate an HCLTree object with the Configuration Dialog
     */
    public void setTree(HCLTree tree) {
        this.tree = tree;
        this.numTerminalsField.setText(String.valueOf(this.tree.getNumberOfTerminalNodes()));
    }

    /**
     * Returns choosed zero threshold value.
     */
    public float getZeroThreshold() {
        float value = Float.parseFloat(zeroTextField.getText());
        if (value <= minDist) return minDist;
        if (value >= maxDist) return maxDist;
        return value;
    }

    /**
     * Returns choosed min distance value.
     */
    public int getMinDistance() {
        return Integer.parseInt(minTextField.getText());
    }

    /**
     * Returns choosed max distance value.
     */
    public int getMaxDistance() {
        return Integer.parseInt(maxTextField.getText());
    }

    /**
     * Returns true if cluster views are to be created
     */
    public boolean isCreateClusterViews() {
        return this.createClusterViewsBox.isSelected();
    }

    /**
     * The class to listen to the dialog events.
     */
    private class Listener extends DialogListener implements ChangeListener, KeyListener {

        public void actionPerformed(ActionEvent e) {
            int min, max;
            String command = e.getActionCommand();
            if (command.equals("ok-command")) {
                try {
                    zThr = Float.parseFloat(zeroTextField.getText());
                    minPixDist = Integer.parseInt(minTextField.getText());
                    maxPixDist = Integer.parseInt(maxTextField.getText());
                    tree.setProperties(zThr, minPixDist, maxPixDist);
                    viewer.revalidateViewer();
                    viewer.repaint();
                    result = JOptionPane.OK_OPTION;
                    dispose();
                } catch (Exception exc) {
                    result = JOptionPane.CANCEL_OPTION;
                    dispose();
                }
            } else if (command.equals("apply-tree-dimensions-command")) {
                try {
                    minPixDist = Integer.parseInt(minTextField.getText());
                    maxPixDist = Integer.parseInt(maxTextField.getText());
                    tree.setPixelHeightLimits(minPixDist, maxPixDist);
                    viewer.revalidateViewer();
                    viewer.repaint();
                } catch (NumberFormatException e1) {
                    result = JOptionPane.CANCEL_OPTION;
                    dispose();
                }
            } else if (command.equals("cancel-command")) {
                tree.setProperties(origZThr, origMinPixDist, origMaxPixDist);
                viewer.revalidateViewer();
                viewer.repaint();
                result = JOptionPane.CANCEL_OPTION;
                dispose();
            } else if (command.equals("reset-command")) {
                zeroTextField.setText(String.valueOf(origZThr));
                minTextField.setText(String.valueOf(origMinPixDist));
                maxTextField.setText(String.valueOf(origMaxPixDist));
                tree.setProperties(origZThr, origMinPixDist, origMaxPixDist);
                slider.setValue(0);
                createClusterViewsBox.setSelected(false);
                viewer.revalidateViewer();
                viewer.repaint();
            } else if (command.equals("info-command")) {
                HelpWindow helpWindow = new HelpWindow(HCLConfigDialog.this, "HCL Tree Properties");
                if (helpWindow.getWindowContent()) {
                    helpWindow.setSize(450, 500);
                    helpWindow.setLocation();
                    helpWindow.show();
                } else {
                    helpWindow.dispose();
                }
            }
        }

        public void windowClosing(WindowEvent e) {
            result = JOptionPane.CLOSED_OPTION;
            dispose();
        }

        public void stateChanged(javax.swing.event.ChangeEvent changeEvent) {
            if (changeEvent.getSource() == slider) {
                float value = distRange * (float) (slider.getValue() / 1000f) + minDist;
                if (tree != null) {
                    zThr = value;
                    tree.setZeroThreshold(value);
                    zeroTextField.setText(String.valueOf(value));
                    numTerminalsField.setText(String.valueOf(tree.getNumberOfTerminalNodes()));
                    tree.repaint();
                }
            }
        }

        public void keyPressed(java.awt.event.KeyEvent keyEvent) {
        }

        public void keyReleased(java.awt.event.KeyEvent keyEvent) {
            if (keyEvent.getSource() == zeroTextField) {
                try {
                    float value = Float.parseFloat(zeroTextField.getText());
                    zThr = value;
                    tree.setZeroThreshold(value);
                    numTerminalsField.setText(String.valueOf(tree.getNumberOfTerminalNodes()));
                    tree.repaint();
                } catch (NumberFormatException e) {
                }
            }
        }

        public void keyTyped(java.awt.event.KeyEvent keyEvent) {
        }
    }

    public static void main(String[] args) {
    }
}
