package org.fpdev.apps.admin.gui;

import org.fpdev.util.gui.GUIFactory;
import org.fpdev.core.transit.Station;
import org.fpdev.apps.admin.AdminClient;
import java.awt.event.*;
import java.util.Iterator;
import javax.swing.*;
import javax.swing.event.*;
import javax.swing.border.*;

public class StationSelectionPanel extends JPanel implements ActionListener, ListSelectionListener {

    private AdminClient av_;

    private JList stationList_;

    private DefaultListModel sListModel_;

    private JButton newStaBtn_, delStaBtn_, zoomStaBtn_;

    private JScrollPane scrollPane_;

    public StationSelectionPanel(AdminClient cv) {
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setBorder(new CompoundBorder(new TitledBorder("Transit Stations"), new EmptyBorder(2, 4, 2, 4)));
        av_ = cv;
        sListModel_ = new DefaultListModel();
        stationList_ = new JList(sListModel_);
        stationList_.setFont(GUIFactory.MAIN_FONT);
        stationList_.setVisibleRowCount(5);
        scrollPane_ = new JScrollPane(stationList_);
        stationList_.addListSelectionListener(this);
        newStaBtn_ = GUIFactory.newButton("New", 60, this);
        delStaBtn_ = GUIFactory.newButton("Delete", 60, this);
        zoomStaBtn_ = GUIFactory.newButton("Zoom", 60, this);
        JPanel staBtnRow = new JPanel();
        staBtnRow.setLayout(new BoxLayout(staBtnRow, BoxLayout.X_AXIS));
        staBtnRow.add(Box.createHorizontalGlue());
        staBtnRow.add(newStaBtn_);
        staBtnRow.add(delStaBtn_);
        staBtnRow.add(zoomStaBtn_);
        staBtnRow.add(Box.createHorizontalGlue());
        add(GUIFactory.newLabel("All Stations:"));
        scrollPane_.setAlignmentX(LEFT_ALIGNMENT);
        staBtnRow.setAlignmentX(LEFT_ALIGNMENT);
        add(scrollPane_);
        add(Box.createVerticalStrut(5));
        add(staBtnRow);
        updateStations();
    }

    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == newStaBtn_) {
            av_.getStationOps().newStation();
        }
        if (e.getSource() == delStaBtn_) {
            av_.getStationOps().deleteStation();
        }
        if (e.getSource() == zoomStaBtn_) {
            av_.getStationOps().zoomStation();
        }
    }

    @Override
    public void valueChanged(ListSelectionEvent e) {
        if (!e.getValueIsAdjusting()) {
            stationSelected();
        }
    }

    public void updateStations() {
        Iterator<Station> stations = av_.getStationOps().getStations();
        sListModel_.removeAllElements();
        delStaBtn_.setEnabled(false);
        zoomStaBtn_.setEnabled(false);
        if (av_.getStationOps().getStationCount() == 0) {
            stationList_.setEnabled(false);
        } else {
            stationList_.setEnabled(true);
            while (stations.hasNext()) {
                Station station = stations.next();
                sListModel_.addElement(station.getID() + " - " + station.getName());
            }
        }
    }

    public void setSelectedStationIndex(int i) {
        if (i < 0 || i >= stationList_.getModel().getSize()) {
            return;
        }
        stationList_.setSelectedIndex(i);
        scrollPane_.getVerticalScrollBar().setValue(i * scrollPane_.getVerticalScrollBar().getMaximum() / av_.getStationOps().getStationCount());
        stationSelected();
    }

    public void stationSelected() {
        int i = stationList_.getMinSelectionIndex();
        if (i >= 0) {
            delStaBtn_.setEnabled(true);
            zoomStaBtn_.setEnabled(true);
            av_.getStationOps().stationSelected();
        }
    }

    public int selectedStationIndex() {
        return stationList_.getMinSelectionIndex();
    }
}
