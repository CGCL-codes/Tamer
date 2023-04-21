package org.fpdev.apps.rtemaster;

import java.awt.geom.Rectangle2D;
import java.util.Iterator;
import javax.swing.JOptionPane;
import org.fpdev.core.basenet.BLink;
import org.fpdev.core.basenet.BNode;
import org.fpdev.core.transit.Station;
import org.fpdev.core.transit.Stations;
import org.fpdev.apps.rtemaster.gui.map.MapDrawItems;

/**
 *
 * @author demory
 */
public class StationOps {

    private RouteMaster av_;

    private Stations stations_;

    /** Creates a new instance of StationManager */
    public StationOps(RouteMaster av) {
        av_ = av;
        stations_ = av_.getEngine().getStations();
    }

    public Iterator<Station> getStations() {
        return stations_.iterator();
    }

    public void setStations(Stations stations) {
        stations_ = stations;
    }

    public int getStationCount() {
        return stations_.count();
    }

    public void stationSelected() {
        Station sta = this.getSelectedStation();
        if (!sta.initialized()) {
            av_.getEngine().initNodes(sta.getNodeIDs());
            av_.getEngine().initLinks(sta.getLinkIDs());
            sta.initNetworkElements(av_.getEngine().getBaseNet());
        }
        av_.getGUI().getMapPanel().getDrawItems().addItem(MapDrawItems.STATION, sta, "s", 15);
        av_.getGUI().getMapPanel().recenter(sta.getBoundingBox().getCenterX(), sta.getBoundingBox().getCenterY());
    }

    public void newStation() {
        String id = JOptionPane.showInputDialog("Please Specify Station ID:");
        String name = JOptionPane.showInputDialog("Please Specify Route Name:");
        if (id != null && id.length() > 0 && name != null && name.length() > 0) {
            Station station = new Station(id, name);
            stations_.add(station);
            av_.getGUI().getStationSelectionPanel().updateStations();
        }
    }

    public void deleteStation() {
        Station station = getSelectedStation();
        if (station == null) {
            av_.msg("No station selected");
            return;
        }
        if (station.getNodeCount() > 0 || station.getLinkCount() > 0) {
            av_.msg("Remove link/node associations first");
            return;
        }
        stations_.remove(station.getID());
        av_.getGUI().getStationSelectionPanel().updateStations();
    }

    public Station getSelectedStation() {
        int r = av_.getGUI().getStationSelectionPanel().selectedStationIndex();
        return stations_.getFromIndex(r);
    }

    public void zoomStation() {
        Station station = getSelectedStation();
        if (station != null) {
            Rectangle2D.Double bounds = station.getBoundingBox();
            if (bounds != null) {
                av_.getGUI().getMapPanel().zoomRange(bounds);
                av_.getGUI().getMapPanel().zoomOut();
                av_.getGUI().getMapPanel().repaint();
            } else {
                av_.msg("Station includes no nodes or links");
            }
        }
    }

    public void writeStationsFile() {
        stations_.writeFile(av_.getGUI());
        av_.msg("Wrote stations file");
    }

    public boolean toggleStationNode(BNode node) {
        if (node == null) {
            av_.msg("No node selected");
            return false;
        }
        Station station = getSelectedStation();
        if (station == null) {
            av_.msg("No station selected");
            return false;
        }
        if (node.isStation() && node.getStation() != station) {
            av_.msg("Node already belongs to " + node.getStation().getName());
            return false;
        }
        if (station.includesNode(node)) {
            station.removeNode(node);
        } else {
            station.addNode(node);
        }
        av_.getGUI().getMapPanel().refresh(false, true, true);
        stations_.changesMade();
        return true;
    }

    public boolean toggleStationLink(BLink link) {
        if (link == null) {
            av_.msg("No link selected");
            return false;
        }
        Station station = getSelectedStation();
        if (station == null) {
            av_.msg("No station selected");
            return false;
        }
        if (link.isStation() && link.getStation() != station) {
            av_.msg("Link already belongs to " + link.getStation().getName());
            return false;
        }
        if (station.includesLink(link)) {
            station.removeLink(link);
        } else {
            station.addLink(link);
        }
        av_.getGUI().getMapPanel().refresh(false, true, true);
        stations_.changesMade();
        return true;
    }

    public boolean renameStationNode(BNode node) {
        if (node == null) {
            av_.msg("No node selected");
            return false;
        }
        Station station = getSelectedStation();
        if (node.isStation() && node.getStation() != station) {
            station = node.getStation();
            av_.getGUI().getStationSelectionPanel().setSelectedStationIndex(stations_.indexOf(station));
        }
        if (station == null) {
            av_.msg("Not a station node");
            return false;
        }
        if (station.includesNode(node)) {
            String name = station.getNodeName(node);
            String newName = JOptionPane.showInputDialog("Please specify a name:", name);
            name = (newName != null) ? newName : name;
            station.setNodeName(node, name);
            av_.msg("Node Renamed \"" + name + "\"");
            stations_.changesMade();
            return true;
        } else {
            av_.msg("Selected Station Does Not Include Node");
        }
        return false;
    }
}
