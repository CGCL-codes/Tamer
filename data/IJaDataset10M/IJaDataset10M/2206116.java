package net.sf.mzmine.modules.visualization.spectra;

import java.awt.Color;
import java.awt.Insets;
import java.awt.event.ActionListener;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JToolBar;
import net.sf.mzmine.util.GUIUtils;

/**
 * Spectra visualizer's toolbar class
 */
public class SpectraToolBar extends JToolBar {

    static final Icon centroidIcon = new ImageIcon("icons/centroidicon.png");

    static final Icon continuousIcon = new ImageIcon("icons/continuousicon.png");

    static final Icon dataPointsIcon = new ImageIcon("icons/datapointsicon.png");

    static final Icon annotationsIcon = new ImageIcon("icons/annotationsicon.png");

    static final Icon pickedPeakIcon = new ImageIcon("icons/pickedpeakicon.png");

    static final Icon isotopePeakIcon = new ImageIcon("icons/isotopepeakicon.png");

    static final Icon axesIcon = new ImageIcon("icons/axesicon.png");

    private JButton centroidContinuousButton, dataPointsButton;

    public SpectraToolBar(ActionListener masterFrame) {
        super(JToolBar.VERTICAL);
        setFloatable(false);
        setFocusable(false);
        setMargin(new Insets(5, 5, 5, 5));
        setBackground(Color.white);
        centroidContinuousButton = GUIUtils.addButton(this, null, centroidIcon, masterFrame, "TOGGLE_PLOT_MODE", "Toggle centroid/continuous mode");
        addSeparator();
        dataPointsButton = GUIUtils.addButton(this, null, dataPointsIcon, masterFrame, "SHOW_DATA_POINTS", "Toggle displaying of data points  in continuous mode");
        addSeparator();
        GUIUtils.addButton(this, null, annotationsIcon, masterFrame, "SHOW_ANNOTATIONS", "Toggle displaying of peak values");
        addSeparator();
        GUIUtils.addButton(this, null, pickedPeakIcon, masterFrame, "SHOW_PICKED_PEAKS", "Toggle displaying of picked peaks");
        addSeparator();
        GUIUtils.addButton(this, null, isotopePeakIcon, masterFrame, "SHOW_ISOTOPE_PEAKS", "Toggle displaying of predicted isotope peaks");
        addSeparator();
        GUIUtils.addButton(this, null, axesIcon, masterFrame, "SETUP_AXES", "Setup ranges for axes");
    }

    public void setCentroidButton(boolean centroid) {
        if (centroid) {
            centroidContinuousButton.setIcon(centroidIcon);
            dataPointsButton.setEnabled(true);
        } else {
            centroidContinuousButton.setIcon(continuousIcon);
            dataPointsButton.setEnabled(false);
        }
    }
}
