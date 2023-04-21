package ch.ethz.sg.cuttlefish.gui;

import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JPanel;

/**
 * Class for the JPanel that appears always in the bottom of the window with tools like saving and 
 * stopping the layout or selecting the mouse mode
 * @author david
 */
public class LayoutPanel extends JPanel {

    private JComboBox layoutComboBox = null;

    private JButton stopLayoutButton = null;

    private JButton restartLayoutButton = null;

    private CuttlefishPanel parentPanel = null;

    private static final long serialVersionUID = 1L;

    /**
 * Constructor depending on the parent CuttlefishPanel
 * @param parentPanel where the LayoutPanel will be added
 */
    public LayoutPanel(CuttlefishPanel parentPanel) {
        this.parentPanel = parentPanel;
        setLayout(new GridBagLayout());
        add(getLayoutComboBox(), new GridBagConstraints());
        setBackground(Color.gray);
        add(getStopLayoutButton(), new GridBagConstraints());
        add(getRestartLayoutButton(), new GridBagConstraints());
    }

    /**
 * This method initializes layoutComboBox	
 * @return javax.swing.JComboBox	
 */
    private JComboBox getLayoutComboBox() {
        String[] layoutNames = { "ARFLayout", "KCore", "Fixed", "WeightedARFLayout", "SpringLayout", "Kamada-Kawai", "Fruchterman-Reingold", "ISOMLayout", "CircleLayout", "TreeLayout", "BaloonLayout", "RadialTreeLayout" };
        if (layoutComboBox == null) {
            layoutComboBox = new JComboBox(layoutNames);
            layoutComboBox.setName("Layout");
            layoutComboBox.setBackground(Color.gray);
            layoutComboBox.setForeground(Color.orange);
            layoutComboBox.addActionListener(new ActionListener() {

                public void actionPerformed(ActionEvent e) {
                    parentPanel.setLayout((String) layoutComboBox.getSelectedItem());
                }
            });
        }
        return layoutComboBox;
    }

    /**
 * This method initializes stopLayoutButton	
 * 	
 * @return javax.swing.JButton	
 */
    private JButton getStopLayoutButton() {
        if (stopLayoutButton == null) {
            stopLayoutButton = new JButton();
            stopLayoutButton.setText("Stop Layout");
            stopLayoutButton.addActionListener(new java.awt.event.ActionListener() {

                public void actionPerformed(java.awt.event.ActionEvent e) {
                    parentPanel.stopLayout();
                    stopLayoutButton.setEnabled(false);
                    getRestartLayoutButton().setEnabled(true);
                }
            });
        }
        return stopLayoutButton;
    }

    /**
 * This method initializes restartLayoutButton	
 * 	
 * @return javax.swing.JButton	
 */
    private JButton getRestartLayoutButton() {
        if (restartLayoutButton == null) {
            restartLayoutButton = new JButton();
            restartLayoutButton.setText("Restart Layout");
            restartLayoutButton.setEnabled(false);
            restartLayoutButton.addActionListener(new java.awt.event.ActionListener() {

                public void actionPerformed(java.awt.event.ActionEvent e) {
                    parentPanel.resumeLayout();
                    restartLayoutButton.setEnabled(false);
                    stopLayoutButton.setEnabled(true);
                }
            });
        }
        return restartLayoutButton;
    }
}
