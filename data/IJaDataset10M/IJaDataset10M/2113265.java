package org.tigr.microarray.mev.script.scriptGUI;

import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.JOptionPane;
import javax.swing.JPopupMenu;
import org.tigr.microarray.mev.cluster.gui.Experiment;
import org.tigr.microarray.mev.cluster.gui.helpers.ExperimentClusterCentroidViewer;
import org.tigr.microarray.mev.cluster.gui.helpers.ExperimentUtil;

public class ScriptExperimentCentroidViewer extends ExperimentClusterCentroidViewer {

    private JPopupMenu popup;

    /**
     * Construct a <code>KMCCentroidViewer</code> with specified experiment
     * and clusters.
     */
    public ScriptExperimentCentroidViewer(Experiment experiment, int[][] clusters) {
        super(experiment, clusters);
        Listener listener = new Listener();
        this.popup = createJPopupMenu(listener);
        getContentComponent().addMouseListener(listener);
    }

    private void writeObject(java.io.ObjectOutputStream oos) throws java.io.IOException {
    }

    private void readObject(java.io.ObjectInputStream ois) throws java.io.IOException, ClassNotFoundException {
        Listener listener = new Listener();
        this.popup = createJPopupMenu(listener);
        getContentComponent().addMouseListener(listener);
    }

    /**
     * Creates a popup menu.
     */
    private JPopupMenu createJPopupMenu(Listener listener) {
        JPopupMenu popup = new JPopupMenu();
        addMenuItems(popup, listener);
        return popup;
    }

    /**
     * Saves all clusters.
     */
    private void onSaveClusters() {
        Frame frame = JOptionPane.getFrameForComponent(getContentComponent());
        try {
            ExperimentUtil.saveAllExperimentClusters(frame, getExperiment(), getData(), getClusters());
        } catch (Exception e) {
            JOptionPane.showMessageDialog(frame, "Can not save clusters!", e.toString(), JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }

    /**
     * Save the viewer cluster.
     */
    private void onSaveCluster() {
        Frame frame = JOptionPane.getFrameForComponent(getContentComponent());
        try {
            ExperimentUtil.saveExperimentCluster(frame, getExperiment(), getData(), getCluster());
        } catch (Exception e) {
            JOptionPane.showMessageDialog(frame, "Can not save cluster!", e.toString(), JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }

    /**
     * Removes a public color.
     */
    private void onSetDefaultColor() {
        setClusterColor(null);
    }

    /**
     * The class to listen to mouse and action events.
     */
    private class Listener extends MouseAdapter implements ActionListener {

        public void actionPerformed(ActionEvent e) {
            String command = e.getActionCommand();
            if (command.equals(SAVE_CLUSTER_CMD)) {
                onSaveCluster();
            } else if (command.equals(SAVE_ALL_CLUSTERS_CMD)) {
                onSaveClusters();
            } else if (command.equals(STORE_CLUSTER_CMD)) {
                storeCluster();
            } else if (command.equals(SET_DEF_COLOR_CMD)) {
                onSetDefaultColor();
            } else if (command.equals(SET_Y_TO_EXPERIMENT_MAX_CMD)) {
                yRangeOption = ExperimentClusterCentroidViewer.USE_EXPERIMENT_MAX;
                setClusterMaxMenuItem.setEnabled(true);
                setOverallMaxMenuItem.setEnabled(false);
                repaint();
            } else if (command.equals(SET_Y_TO_CLUSTER_MAX_CMD)) {
                yRangeOption = ExperimentClusterCentroidViewer.USE_CLUSTER_MAX;
                setClusterMaxMenuItem.setEnabled(false);
                setOverallMaxMenuItem.setEnabled(true);
                repaint();
            } else if (command.equals(LAUNCH_NEW_SESSION_CMD)) {
                launchNewSession();
            }
        }

        public void mouseReleased(MouseEvent event) {
            maybeShowPopup(event);
        }

        public void mousePressed(MouseEvent event) {
            maybeShowPopup(event);
        }

        private void maybeShowPopup(MouseEvent e) {
            if (!e.isPopupTrigger() || getCluster() == null || getCluster().length == 0) {
                return;
            }
            popup.show(e.getComponent(), e.getX(), e.getY());
        }
    }
}
