package edu.washington.assist.gui.stateview;

import java.applet.Applet;
import java.applet.AudioClip;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;
import javax.swing.ButtonGroup;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JRadioButtonMenuItem;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;
import edu.washington.assist.animation.AnimationManager;
import edu.washington.assist.gui.AssistGUI;
import edu.washington.assist.report.Report;
import edu.washington.assist.report.ReportManager;
import edu.washington.assist.report.event.ReportSelectionModel;

@SuppressWarnings("serial")
public class ClusterViewer extends JPanel implements MouseListener, ActionListener {

    public enum DisplayMode {

        CLUSTER, NEW_CLUSTER
    }

    ;

    private long currentMission;

    private ArrayList<ClusterFeatureSetListener> clusterListeners = new ArrayList<ClusterFeatureSetListener>();

    protected AudioClip clusterChangeClip = null;

    protected boolean playChangeClip = false;

    private JPopupMenu clusterMenu;

    private JMenuItem goToTimeItem;

    private JMenuItem changeKItem;

    private JMenu changeClusteringSubMenu;

    private ButtonGroup changeClusteringButtonGrp;

    private JMenuItem emptyItem;

    private JCheckBoxMenuItem playClickItem;

    private ClusterChangeKDialog changeKDialog;

    public ClusterViewer(AssistGUI gui, AnimationManager animator, ReportSelectionModel reportSelector, DisplayMode mode) {
        super(new BorderLayout());
        TimeLinePanel timeLine = new TimeLinePanel(animator.getTimeZoom());
        TimeSeriesPanel viewer;
        if (mode == DisplayMode.CLUSTER) viewer = new ClusterPanel(animator, this); else viewer = new NewIndividualClusterPanel(animator, this);
        addClusterFeatureSetListener((ClusterFeatureSetListener) viewer);
        JPanel activityDisplay = new JPanel();
        activityDisplay.setLayout(new BorderLayout());
        activityDisplay.add(viewer, BorderLayout.CENTER);
        activityDisplay.add(timeLine, BorderLayout.SOUTH);
        JScrollPane scrollPane = new JScrollPane(activityDisplay);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_NEVER);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        viewer.setViewport(scrollPane.getViewport());
        JScrollBar scollBar = scrollPane.getVerticalScrollBar();
        if (scollBar != null) {
            scollBar.setValue(scollBar.getMaximum());
        }
        this.add(scrollPane, BorderLayout.CENTER);
        URL clipURL = null;
        try {
            clipURL = new File("sounds/activity_change.wav").toURI().toURL();
        } catch (Exception e) {
            System.err.println("Unable to initialize sound for clusters.");
        }
        if (clipURL != null) {
            this.clusterChangeClip = Applet.newAudioClip(clipURL);
        }
        this.changeKDialog = new ClusterChangeKDialog(gui, animator);
        this.goToTimeItem = new JMenuItem("Go to Time", KeyEvent.VK_T);
        this.goToTimeItem.setFont(goToTimeItem.getFont().deriveFont(Font.BOLD));
        this.goToTimeItem.addActionListener(this);
        this.changeKItem = new JMenuItem("Change Value of K", KeyEvent.VK_C);
        this.changeKItem.addActionListener(this);
        this.changeClusteringSubMenu = new JMenu("Change Grouping Type");
        this.changeClusteringSubMenu.setMnemonic(KeyEvent.VK_C);
        this.emptyItem = new JMenuItem("Empty.");
        this.emptyItem.setEnabled(false);
        this.changeClusteringSubMenu.add(emptyItem);
        this.playClickItem = new JCheckBoxMenuItem("Play click sound on transitions.", false);
        this.playClickItem.addActionListener(this);
        this.clusterMenu = new JPopupMenu();
        this.clusterMenu.add(goToTimeItem);
        this.clusterMenu.add(changeKItem);
        this.clusterMenu.addSeparator();
        this.clusterMenu.add(changeClusteringSubMenu);
        this.clusterMenu.addSeparator();
        this.clusterMenu.add(playClickItem);
        viewer.addMouseListener(this);
        timeLine.addMouseListener(this);
    }

    public void addClusterFeatureSetListener(ClusterFeatureSetListener l) {
        this.clusterListeners.add(l);
    }

    public void removeClusterFeatureSetListener(ClusterFeatureSetListener l) {
        this.clusterListeners.remove(l);
    }

    /**
	 * Update the clustering list to the given report, if the report is not the same mission as the
	 * previous report.  If report is null, empties the list.
	 */
    public void changeClusterReport(Report report) {
        Collection<Report> selectedReports = ReportManager.getMasterReportManager().getEnabledReports();
        assert (selectedReports != null);
        if (selectedReports.isEmpty()) {
            changeClusteringSubMenu.removeAll();
            changeClusteringSubMenu.add(emptyItem);
        } else {
            if (report.getMissionID() == currentMission) {
                return;
            }
            this.currentMission = report.getMissionID();
            Map<String, Integer> clusteringNames = report.getFeatureSet();
            ClusterNameRadioItem item = null;
            changeClusteringSubMenu.removeAll();
            changeClusteringButtonGrp = new ButtonGroup();
            for (String name : clusteringNames.keySet()) {
                item = new ClusterNameRadioItem(name, (clusteringNames.get(name).intValue() == 1), clusteringNames.get(name).intValue());
                changeClusteringSubMenu.add(item);
                changeClusteringButtonGrp.add(item);
                item.addActionListener(this);
            }
        }
    }

    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == this.changeKItem) {
            this.changeKDialog.setVisible(true);
        } else if (e.getSource() == this.playClickItem) {
            this.playChangeClip = this.playClickItem.isSelected();
        } else {
            for (ClusterFeatureSetListener l : this.clusterListeners) {
                l.featureSetChanged(((ClusterNameRadioItem) e.getSource()).getFeatureSet());
            }
        }
    }

    public void mouseClicked(MouseEvent e) {
    }

    public void mouseEntered(MouseEvent e) {
    }

    public void mouseExited(MouseEvent e) {
    }

    public void mousePressed(MouseEvent e) {
        if (e.isPopupTrigger()) {
            this.clusterMenu.show((Component) e.getSource(), e.getX(), e.getY());
        }
    }

    public void mouseReleased(MouseEvent e) {
        if (e.isPopupTrigger()) {
            this.clusterMenu.show((Component) e.getSource(), e.getX(), e.getY());
        }
    }

    private class ClusterNameRadioItem extends JRadioButtonMenuItem {

        private int featureSet;

        public ClusterNameRadioItem(String name, boolean selected, int featureSet) {
            super(name, selected);
            this.featureSet = featureSet;
        }

        public int getFeatureSet() {
            return this.featureSet;
        }
    }
}
