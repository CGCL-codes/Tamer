package org.processmining.analysis.activityclustering.ui;

import java.awt.BorderLayout;
import java.awt.Color;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JViewport;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import org.deckfour.slickerbox.components.RoundedPanel;
import org.processmining.analysis.activityclustering.ClusterTypeSet;
import org.processmining.analysis.activityclustering.model.ClusterType;
import org.processmining.framework.log.LogReader;

/**
 * @author christian
 * 
 */
public class ActivityClusteringResultUI extends JPanel {

    protected LogReader log;

    protected ClusterTypeSet clusters;

    protected JList clusterList;

    protected JList footprintList;

    protected Color colorListBg = new Color(140, 140, 140);

    protected Color colorListFg = new Color(40, 40, 40);

    protected Color colorListSelectedBg = new Color(20, 20, 60);

    protected Color colorListSelectedFg = new Color(220, 40, 40);

    public ActivityClusteringResultUI(LogReader log, ClusterTypeSet clusters) {
        this.clusters = clusters;
        this.log = log;
        setupGui();
    }

    protected void setupGui() {
        this.setBorder(BorderFactory.createEmptyBorder());
        this.setLayout(new BorderLayout());
        this.setBackground(new Color(90, 90, 90));
        JPanel browserPanel = new JPanel();
        browserPanel.setOpaque(true);
        browserPanel.setLayout(new BorderLayout());
        browserPanel.setBackground(new Color(40, 40, 40));
        browserPanel.setBorder(BorderFactory.createEmptyBorder());
        RoundedPanel browserInnerPanel = new RoundedPanel(10, 5, 5);
        browserInnerPanel.setBackground(new Color(100, 100, 100));
        browserInnerPanel.setLayout(new BoxLayout(browserInnerPanel, BoxLayout.X_AXIS));
        clusterList = new JList();
        clusterList.setBackground(colorListBg);
        clusterList.setForeground(colorListFg);
        clusterList.setSelectionForeground(colorListBg);
        clusterList.setSelectionBackground(colorListSelectedBg);
        clusterList.setSelectionForeground(colorListSelectedFg);
        footprintList = new JList();
        footprintList.setBackground(colorListBg);
        footprintList.setForeground(colorListFg);
        footprintList.setSelectionForeground(colorListBg);
        footprintList.setSelectionBackground(colorListSelectedBg);
        footprintList.setSelectionForeground(colorListSelectedFg);
        clusterList.setModel(new ClusterTypeSetListModel(clusters));
        clusterList.addListSelectionListener(new ListSelectionListener() {

            public void valueChanged(ListSelectionEvent arg0) {
                footprintList.setModel(new FootprintListModel(((ClusterType) clusterList.getSelectedValue()).footprint()));
            }
        });
        JScrollPane clusterListScrollPane = new JScrollPane(clusterList);
        clusterListScrollPane.setBorder(BorderFactory.createLineBorder(new Color(80, 80, 80), 2));
        JScrollPane footprintListScrollPane = new JScrollPane(footprintList);
        footprintListScrollPane.setBorder(BorderFactory.createLineBorder(new Color(80, 80, 80), 2));
        browserInnerPanel.add(clusterListScrollPane);
        browserInnerPanel.add(Box.createHorizontalStrut(5));
        browserInnerPanel.add(footprintListScrollPane);
        browserPanel.add(browserInnerPanel, BorderLayout.CENTER);
        this.add(browserPanel, BorderLayout.SOUTH);
        ActivityClusterPanel clusterPanel = new ActivityClusterPanel(log, clusters);
        JScrollPane scrollPane = new JScrollPane(clusterPanel);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        scrollPane.getViewport().setScrollMode(JViewport.SIMPLE_SCROLL_MODE);
        this.add(scrollPane, BorderLayout.CENTER);
    }
}
