package de.tudresden.inf.rn.mobilis.server.xhunt;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import javax.xml.namespace.QName;
import org.apache.axiom.om.OMAttribute;
import org.apache.axiom.om.OMElement;
import org.jdesktop.swingx.mapviewer.GeoPosition;
import de.tudresden.inf.rn.mobilis.server.xhunt.beans.Route;
import de.tudresden.inf.rn.mobilis.server.xhunt.beans.Station;

/**
 * RouteManagement manages all the stations and routes of the map.
 * This includes stops of tram, railway and bus.
 * It also holds the possible start positions.
 * @author fanny
 *
 */
public class RouteManagement {

    private ArrayList<Route> routes;

    private ArrayList<Station> stations;

    private ArrayList<Station> startPositions;

    private GeoPosition mapCenter;

    /**
	 * Constructor for RouteManagement.
	 */
    public RouteManagement() {
        routes = new ArrayList<Route>();
        stations = new ArrayList<Station>();
        startPositions = new ArrayList<Station>();
    }

    /**
	 * Returns all stations that exist in the game.
	 * @return ArrayList<Station> - stations
	 */
    public ArrayList<Station> getStations() {
        return stations;
    }

    /**
	 * Sets all stations that exist in the game. And overwrites the existing.
	 * @param ArrayList<Station> stations
	 */
    public void setStations(ArrayList<Station> stations) {
        this.stations = stations;
    }

    /**
	 * Adds a station to the stations.
	 * @param Station station
	 */
    public void addStation(Station station) {
        stations.add(station);
    }

    public ArrayList<Route> getRoutes() {
        return routes;
    }

    public void setRoutes(ArrayList<Route> routes) {
        this.routes = routes;
    }

    public void addRoute(Route route) {
        routes.add(route);
    }

    public ArrayList<Station> getStartPositions() {
        return this.startPositions;
    }

    public void setStartPositions(ArrayList<Station> startStations) {
        this.startPositions = startStations;
    }

    /**
	 * Returns a random Station out of the startPositions. startPositions are those stations out of
	 * the XML file in the tag < InitialStops >. If this tag did not exist all existing stations are copied
	 * into the startPositions.
	 * If one Station was chosen it is removed out of the list. Therefore every player has a different
	 * position.
	 * @return Station startStation
	 */
    public Station getRandomStartPosition() {
        int size = this.startPositions.size();
        double decNumber = Math.random();
        int randomNumber = -1;
        Station start = null;
        if (size <= 0) this.startPositions = (ArrayList<Station>) this.stations.clone();
        size = this.startPositions.size();
        if (size > 0) {
            randomNumber = (int) Math.round(decNumber * (size - 1));
            start = startPositions.remove(randomNumber);
            writeSomething("RandomStart: " + start.getName());
            return start;
        } else return null;
    }

    /**
	 * Calculates a random station out of the current position. The resulting station
	 * is in a maximum distance of maxDistance meters. Method returns null if there is 
	 * no available station in this distance.
	 * @param currentPosition
	 * @param maxDistance
	 * @return Station - a random station with a maximum distance of maxDistance
	 */
    public Station getANearStartPosition(GeoPosition currentPosition, double maxDistance) {
        ArrayList<Station> distMap = new ArrayList<Station>();
        for (Station s : stations) {
            double distance = calculateDistanceInMeters(currentPosition, s.getGeoPosition());
            if (distance <= maxDistance) {
                distMap.add(s);
                writeSomething(s.getName());
            }
        }
        int size = distMap.size();
        double decNumber = Math.random();
        Station start;
        if (size > 0) {
            int randomNumber = (int) Math.round(decNumber * size);
            start = distMap.remove(randomNumber - 1);
            writeSomething("RandomStart: " + start.getName());
            return start;
        } else return null;
    }

    /**
	 * If we use a flat earth approximation, then the distance calculation uses the Pythagorean theorem:
	 * distance = sqrt(dlat*dlat + dlon*dlon)
	 * Where dlat is the difference in latitudes, and dlon is the difference in longitudes.
	 * @param from
	 * @param to
	 * @return double - distance in degrees
	 */
    public double calculateDistance(GeoPosition from, GeoPosition to) {
        double difLat = from.getLatitude() - to.getLatitude();
        double difLon = from.getLongitude() - to.getLongitude();
        return Math.sqrt((difLat * difLat) + (difLon * difLon));
    }

    /**
	 * Calculates the distance between the two given GeoPositions and returns
	 * a double value in meters.
	 * @param from
	 * @param to
	 * @return double - distance in meters
	 */
    public double calculateDistanceInMeters(GeoPosition from, GeoPosition to) {
        double fromLat = from.getLatitude() / 180 * Math.PI;
        double fromLon = from.getLongitude() / 180 * Math.PI;
        double toLat = to.getLatitude() / 180 * Math.PI;
        double toLon = to.getLongitude() / 180 * Math.PI;
        double zeta = Math.acos(Math.sin(fromLat) * Math.sin(toLat) + Math.cos(fromLat) * Math.cos(toLat) * Math.cos(toLon - fromLon));
        return zeta / (2 * Math.PI) * 40000 * 1000;
    }

    public GeoPosition getMapCenter() {
        if (mapCenter != null) return mapCenter;
        double maxLat = -91, maxLon = -181, minLat = 91, minLon = 181;
        double lat, lon;
        for (Station s : stations) {
            lat = s.getGeoPosition().getLatitude();
            lon = s.getGeoPosition().getLongitude();
            if (lat > maxLat) maxLat = lat;
            if (lat < minLat) minLat = lat;
            if (lon > maxLon) maxLon = lon;
            if (lon < minLon) minLon = lon;
        }
        mapCenter = new GeoPosition((maxLat + minLat) / 2, (maxLon + minLon) / 2);
        return mapCenter;
    }

    /**
	 * This method calculates the routes for the station.
	 * @param station
	 * @return ArrayList<Route> routes passing this station
	 */
    public ArrayList<Route> getRoutesForStation(Station station) {
        ArrayList<Route> result = new ArrayList<Route>();
        int pos = -1;
        for (Route r : routes) {
            pos = r.getPositionOfStation(station);
            if (pos != -1) {
                result.add(r);
            }
        }
        return result;
    }

    /**
	 * Returns the Station for the given id string.
	 * @param String id
	 * @return Station
	 */
    public Station getStation(String id) {
        for (Station stop : stations) {
            if (stop.getId().equals(id)) return stop;
        }
        return null;
    }

    /**
	 * This method is the third part of the initialization of the Stations and adds the routes
	 * to the stations.
	 */
    public void updateStationsWithRoutes() {
        for (Station updateStation : getStations()) {
            ArrayList<Route> availableRoutes = getRoutesForStation(updateStation);
            updateStation.setRoutes(availableRoutes);
        }
    }

    /**
	 * This method searches the xml document recursively. For every node it calls the
	 * transformDocument() method.
	 * @param tree
	 */
    public void searchDocument(OMElement tree) {
        OMElement treeElement = null;
        Iterator<OMElement> treeIterator = tree.getChildElements();
        while (treeIterator.hasNext()) {
            treeElement = treeIterator.next();
            transformDocument(treeElement);
            searchDocument(treeElement);
        }
    }

    /**
	 * This method calls methods depending of the current xml node.
	 * If treeElement is like < Placemark >...< /Placemark > it calls convertPlacemark.
	 * If treeElement is like < Route >...< /Route > it calls convertRoute.
	 * If treeElement is like < InitialStops >...< /InitialStops > it calls convertInitialStops.
	 * @param treeElement
	 */
    public void transformDocument(OMElement treeElement) {
        String elementName = treeElement.getLocalName();
        if (elementName.equals("Placemark")) convertPlacemark(treeElement); else if (elementName.equals("Route")) convertRoute(treeElement); else if (elementName.equals("InitialStops")) convertInitialStops(treeElement);
    }

    /**
	 * This method is called, if the current treeElement is a node of type 
	 * < InitialStops >...< /InitialStops >.
	 * It therefore sets the possible start positions of the game, which are read from the xml.
	 * It sets the attribute startPositions.
	 * @param OMElement treeElement - current xml/kml node
	 */
    private void convertInitialStops(OMElement treeElement) {
        Iterator<OMElement> children = treeElement.getChildElements();
        while (children.hasNext()) {
            OMElement child = children.next();
            if (child.getLocalName() == "stop") {
                String stationId = child.getText();
                Station currentStation = this.getStation(stationId);
                if (currentStation != null) {
                    this.startPositions.add(currentStation);
                    writeSomething("Added station " + stationId + " to startPositions.");
                } else writeSomething("Not added station " + stationId + " to startPositions.");
            }
        }
    }

    /**
	 * This method is called if treeElement is an xml node of type 
	 * < Route id="3" type="tram" start="stationId" end="stationId" >
	 *   < stop nr="1" >stationId< /stop >
	 *   < stop nr="2" >stationId< /stop >
	 *   ...
	 * < /Route >".
	 * It first searches the attributes to get the meta information of the route.
	 * It then searches the elements and sets the stations with the positions in
	 * the route.
	 */
    private void convertRoute(OMElement treeElement) {
        Iterator<OMAttribute> attrIter = treeElement.getAllAttributes();
        String id = "";
        String type = "";
        String start = "";
        String end = "";
        ArrayList<Station> stops = new ArrayList<Station>();
        while (attrIter.hasNext()) {
            OMAttribute attribute = attrIter.next();
            String attrName = attribute.getLocalName();
            if (attrName.equals("id")) id = attribute.getAttributeValue(); else if (attrName.equals("type")) type = attribute.getAttributeValue(); else if (attrName.equals("start")) start = attribute.getAttributeValue(); else if (attrName.equals("end")) end = attribute.getAttributeValue(); else writeSomething("An unknown attribute in XML-Node Route is found!");
        }
        Route currentRoute = new Route(id, type, start, end);
        QName posAttribute = new QName(null, "nr");
        Iterator<OMElement> children = treeElement.getChildElements();
        while (children.hasNext()) {
            OMElement child = children.next();
            if (child.getLocalName() == "stop") {
                String stationId = child.getText();
                OMAttribute position = child.getAttribute(posAttribute);
                Integer pos = Integer.parseInt(position.getAttributeValue());
                Station currentStation = this.getStation(stationId);
                if (currentStation != null) currentRoute.addStation(pos, currentStation);
                writeSomething("Added station " + stationId + " to route " + id + ".");
            }
        }
        routes.add(currentRoute);
    }

    /**
	 * This method is called if treeElement is a kml node of type < Placemark >...< /Placemark >.
	 * It reads the stations with the GPS location.
	 * @param treeElement
	 */
    private void convertPlacemark(OMElement treeElement) {
        Iterator<OMElement> children = treeElement.getChildElements();
        String name = "";
        String id = "";
        float longitude = 0.0f;
        float latitude = 0.0f;
        ArrayList<Route> availableRoutes = new ArrayList<Route>();
        while (children.hasNext()) {
            OMElement child = children.next();
            if (child.getLocalName().equals("name")) name = child.getText(); else if (child.getLocalName().equals("description")) id = child.getText(); else if (child.getLocalName().equals("Point")) {
                Iterator<OMElement> grandChildren = child.getChildElements();
                while (grandChildren.hasNext()) {
                    OMElement grandChild = grandChildren.next();
                    if (grandChild.getLocalName().equals("coordinates")) {
                        String[] split = grandChild.getText().split(",");
                        longitude = Float.parseFloat(split[0]);
                        latitude = Float.parseFloat(split[1]);
                    }
                }
            }
        }
        Station currentStation = new Station(id, name, longitude, latitude);
        if (!(id == "" && longitude == 0.0f && latitude == 0.0f)) {
            stations.add(currentStation);
            writeSomething("Added station with id " + id + " and name " + name + " on location(" + longitude + ", " + latitude + ").");
        }
    }

    private void writeSomething(String status) {
    }
}
