package de.lamasep.map.parser.impl;

import de.lamasep.map.River;
import de.lamasep.map.addresses.AddressStore;
import de.lamasep.map.geo.Polygon;
import de.lamasep.map.City;
import de.lamasep.map.Natural;
import de.lamasep.map.Node;
import de.lamasep.map.PointOfInterest;
import de.lamasep.map.Street;
import de.lamasep.map.StreetMap;
import de.lamasep.map.StreetType;
import de.lamasep.map.geo.Circle;
import de.lamasep.map.geo.Position;
import de.lamasep.map.geo.Rectangle;
import de.lamasep.map.parser.ParserReceiver;
import de.lamasep.map.tiles.CompositeMapTile;
import de.lamasep.map.tiles.TileProcessor;
import de.lamasep.map.tiles.impl.StreetMapTileProcessor;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.apache.log4j.Logger;

/**
 * Definiert einen Receiver, der aus an ihn übergebenen Kartenobjekten
 * eine StreetMap und einen AddressStore erzeugt.
 * @see ParserReceiver
 */
public class OsmParserReceiver implements ParserReceiver {

    /**
     * Logging.
     */
    private static final Logger log;

    static {
        log = Logger.getLogger(OsmParserReceiver.class);
    }

    /**
     * Maximale Distanz einer Straße zu einem City-Info-Node für Zuordnung.
     */
    private static final int CITY_INFO_SNAP_DEST = 2000;

    /**
     * Straßenkarte, welche die Kartenobjekte enthält.
     */
    private StreetMap map;

    /**
     * Liste der Straßen.
     */
    private List<Street> streets;

    /**
     * Liste der Naturals.
     */
    private List<Natural> naturals;

    /**
     * Liste der Rivers.
     */
    private List<River> rivers;

    /**
     * Liste der POIs.
     */
    private List<PointOfInterest> pois;

    /**
     * Adress-Daten (Adressen in der Karte).
     */
    private AddressStore store;

    /**
     * Mapping von Städten auf die zugehörigen Stadtgrenzen.
     */
    private Map<City, Polygon> cities;

    /**
     * Mapping von Stadt-Name auf eine Menge von Städten mit diesem Namen.
     */
    private Map<String, Set<City>> cityPolygonNames;

    /**
     * Abbildung von Cities auf deren Position.
     */
    private Map<City, Position> cityInfo;

    /**
     * Abbildung von Cities auf deren Stadtgrenzen-Radius.
     */
    private Map<City, Circle> cityRadius;

    /**
     * Straßentypen die bei der Umkreissuche berücksichtigt werden.
     */
    private final Set<StreetType> radiusStreetTypes;

    /**
     * Kleinster Breitengrad der Karte.
     */
    private double minLat = Position.MAX_LATITUDE;

    /**
     * Größer Breitengrad der Karte.
     */
    private double maxLat = Position.MIN_LATITUDE;

    /**
     * Kleinster Längengrad der Karte.
     */
    private double minLon = Position.MAX_LONGITUDE;

    /**
     * Größter Längengrad der Karte.
     */
    private double maxLon = Position.MIN_LONGITUDE;

    /**
     * Konstruktor.
     */
    public OsmParserReceiver() {
        radiusStreetTypes = new HashSet<StreetType>();
        radiusStreetTypes.add(StreetType.RESIDENTIAL);
        radiusStreetTypes.add(StreetType.TRACK);
        radiusStreetTypes.add(StreetType.FOOTWAY);
        radiusStreetTypes.add(StreetType.CYCLEWAY);
        radiusStreetTypes.add(StreetType.ARBITRARY);
        reset();
    }

    /**
     * Fügt einen Knoten hinzu. Wird nur benutzt, um die Gesamtausmaße der Karte
     * zu berechnen.
     * @param node Hinzuzufügender Node, <code>node != null</code>
     * @throws IllegalArgumentException falls <code>node == null</code>
     */
    @Override
    public void receive(Node node) {
        if (node == null) {
            throw new IllegalArgumentException();
        }
        updateBounds(node.getPosition());
    }

    /**
     * Berechnet die geographische Abdeckung (Rechteck) der Karte.
     * Das Rechteck wird ggf. erweitert, sofern <code>position</code> nicht
     * innerhalb des bereits bekannten Rechtecks liegt.
     *
     * POST: alle bekannten Positionen liegen im Rechteck
     *      (minLat, minLon, maxLat, MaxLon)
     *
     * @param position <code>position != null</code>
     */
    private void updateBounds(Position position) {
        double lat = position.getLatitude();
        double lon = position.getLongitude();
        if (lat < minLat) {
            minLat = lat;
        } else if (lat > maxLat) {
            maxLat = lat;
        }
        if (lon < minLon) {
            minLon = lon;
        } else if (lon > maxLon) {
            maxLon = lon;
        }
    }

    /**
     * Fügt eine Straße hinzu
     * @param street Hinzuzufügende Straße, <code>street != null</code>
     * @throws IllegalArgumentException falls <code>street == null</code>
     */
    @Override
    public void receive(Street street) {
        if (street == null) {
            throw new IllegalArgumentException();
        }
        streets.add(street);
    }

    /**
     * Fügt einen PointOfInterest hinzu
     * @param poi Hinzuzufügender POI, <code>poi != null</code>
     * @throws IllegalArgumentException falls <code>poi == null</code>
     */
    @Override
    public void receive(PointOfInterest poi) {
        if (poi == null) {
            throw new IllegalArgumentException();
        }
        pois.add(poi);
    }

    /**
     * Fügt ein Natural-Objekt hinzu.
     * @param natural Hinzuzufügendes Naturalobjekt, <code>natural != null</code>
     * @throws IllegalArgumentException falls <code>natural == null</code>
     */
    @Override
    public void receive(Natural natural) {
        if (natural == null) {
            throw new IllegalArgumentException();
        }
        naturals.add(natural);
    }

    /**
     * Fügt eine Stadt mit Hilfe zugehöriger Ausmaße auf der Karte hinzu.
     *
     * Ist <code>border</code> kein gültiges Polygon (weniger als 3 Knoten),
     * wird die City verworfen.
     *
     * @param city Hinzuzufügendes City-Objekt, <code>city != null</code>
     * @param border Ausdehnung des Stadtgebiets von <code>city</code>,
     *        <code>border != null</code>
     * @throws IllegalArgumentException falls <code>city == null ||
     *         border == null</code>
     */
    @Override
    public void receive(City city, Polygon border) {
        if (city == null) {
            throw new IllegalArgumentException("city == null");
        }
        if (border == null) {
            throw new IllegalArgumentException("border == null");
        }
        if (border.size() >= 3) {
            cities.put(city, border);
            String name = city.getName();
            if (cityPolygonNames.containsKey(name)) {
                cityPolygonNames.get(name).add(city);
            } else {
                Set<City> cities = new HashSet<City>();
                cities.add(city);
                cityPolygonNames.put(name, cities);
            }
        }
    }

    /**
     * Fügt ein River-Objekt hinzu.
     * @param river Einzufügender Fluss, <code>river != null</code>
     * @throws IllegalArgumentException falls <code>river == null</code>
     */
    @Override
    public void receive(River river) {
        if (river == null) {
            throw new IllegalArgumentException();
        }
        rivers.add(river);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void receive(City city, Position pos, Circle radius) {
        if (city == null) {
            throw new IllegalArgumentException("city == null");
        }
        if (pos == null) {
            throw new IllegalArgumentException("pos == null");
        }
        cityInfo.put(city, pos);
        if (radius != null) {
            cityRadius.put(city, radius);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void reset() {
        log.info("Resetting ParserReceiver");
        map = null;
        streets = new LinkedList<Street>();
        rivers = new LinkedList<River>();
        naturals = new LinkedList<Natural>();
        pois = new LinkedList<PointOfInterest>();
        cities = new HashMap<City, Polygon>();
        cityInfo = new HashMap<City, Position>();
        cityPolygonNames = new HashMap<String, Set<City>>();
        cityRadius = new HashMap<City, Circle>();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void build() {
        log.info("Building map");
        TileProcessor tp = new StreetMapTileProcessor(new Rectangle(new Position(minLon, minLat), new Position(maxLon, maxLat)));
        map = new CompositeMapTile(tp);
        store = new AddressStore();
        Thread t1 = new Thread(new Runnable() {

            @Override
            public void run() {
                for (Street street : streets) {
                    map.add(street);
                }
                log.info("Finished adding streets to map");
                for (City city : cities.keySet()) {
                    Polygon borders = cities.get(city);
                    Rectangle bounds = borders.getBounds();
                    for (Street street : map.getStreets(bounds)) {
                        for (Node node : street.getNodes()) {
                            if (borders.contains(node.getPosition())) {
                                city.addStreet(street);
                            }
                        }
                    }
                    store.add(city);
                }
                log.info("Finished assigning streets to cities");
            }
        }, "ParserReceiver-2");
        t1.start();
        Thread t2 = new Thread(new Runnable() {

            @Override
            public void run() {
                for (River river : rivers) {
                    map.add(river);
                }
                log.info("Finished adding rivers");
                for (Natural natural : naturals) {
                    map.add(natural);
                }
                log.info("Finished adding naturals");
                for (PointOfInterest poi : pois) {
                    map.add(poi);
                }
                log.info("Finished adding pois");
            }
        }, "ParserReceiver-1");
        t2.start();
        try {
            t1.join();
            t2.join();
        } catch (InterruptedException ex) {
        }
        for (City city : cityInfo.keySet()) {
            log.debug("InfoNode mapping for: " + city.getName());
            Position pos = cityInfo.get(city);
            String cityName = city.getName();
            if (cityPolygonNames.containsKey(cityName)) {
                boolean found = false;
                for (City polyCity : cityPolygonNames.get(cityName)) {
                    Polygon border = cities.get(polyCity);
                    if (border.contains(pos)) {
                        found = true;
                        break;
                    }
                }
                if (found) {
                    log.debug("InfoNode skip, Polygon has precedence: " + city.getName());
                    continue;
                }
            }
            if (cityRadius.containsKey(city)) {
                radiusSearch(city);
            }
        }
        log.info("Finished extended Street -> City mapping");
    }

    /**
     * Sucht mittels StreetMap.snapTo() nach einer Straße die als Stadtzentrum
     * gewertet werden kann und fügt diese der Stadt hinzu.
     * @param city zu betrachtende Stadt.
     * @param pos Position um die gesucht werden soll.
     */
    private void snapToSearch(City city, Position pos) {
        Node snap = map.snapTo(pos);
        if (snap == null) {
            log.debug(pos + " NOT ON MAP with bounds " + map.getBounds());
            return;
        }
        Position snapPos = snap.getPosition();
        double distance = snapPos.distance(pos);
        if (distance > CITY_INFO_SNAP_DEST) {
            log.debug("InfoNode skip, to far away: " + distance);
            return;
        }
        log.debug(String.format("InfoNode snapped to: %s, %f away", snap.getPosition(), distance));
        Set<Long> relatedWayIds = snap.getRelatedWays();
        for (long wayId : relatedWayIds) {
            Street relatedWay = map.getStreet(wayId, pos);
            if (relatedWay == null) {
                log.debug("InfoNode, relatedWay == null, skipping");
                continue;
            }
            StreetType type = relatedWay.getType();
            if (type == StreetType.RESIDENTIAL) {
                if (relatedWay.getName() == null) {
                    log.debug("InfoNode snapTo, updating street name");
                    relatedWay.setName(cityCenterName(city));
                }
                log.debug("InfoNode snapTo, found street: " + relatedWay.getName());
                city.addStreet(relatedWay);
                store.add(city);
                break;
            }
        }
    }

    /**
     * Führt eine Umkreissuche nach zugehörigen Straßen um eine Stadt durch.
     * @param city Zu betrachtende Stadt.
     */
    private void radiusSearch(City city) {
        Circle radius = cityRadius.get(city);
        log.debug(String.format("InfoNode radius-search for %s (%f m)", city.getName(), radius.getRadius()));
        Rectangle bounds = radius.getBounds();
        if (bounds == null) {
            log.warn(String.format("radius.getBounds == null, city=%s, r=%f", city.getName(), radius.getRadius()));
            return;
        }
        Set<Street> streetsFromRect = map.getStreets(bounds);
        log.debug(String.format("InfoNode radius-search found %d Streets", streetsFromRect.size()));
        boolean foundStreets = false;
        long count = 0;
        long skipCount = 0;
        for (Street sFromRect : streetsFromRect) {
            StreetType type = sFromRect.getType();
            if (radiusStreetTypes.contains(type)) {
                if (sFromRect.getName() == null) {
                    log.debug("InfoNode skip street, name==null");
                    continue;
                }
                if (!radius.contains(sFromRect.getPath())) {
                    log.debug("InfoNode skip street, outside radius");
                    skipCount++;
                    continue;
                }
                log.debug("InfoNode radius-search found street: " + sFromRect.getName());
                city.addStreet(sFromRect);
                count++;
                foundStreets = true;
            }
        }
        log.debug(String.format("InfoNode radius-search added %d Streets," + " skipped %d", count, skipCount));
        if (foundStreets) {
            store.add(city);
        } else {
            snapToSearch(city, radius.getCenter());
        }
    }

    /**
     * Gibt den Namen für das Zentrum einer Stadt zurück.
     * @param city Stadt.
     * @return Name für das Stadtzentrum.
     */
    private String cityCenterName(City city) {
        return city.getName() + " (Zentrum)";
    }

    /**
     * Gibt den aus eingelesenen Kartenobjekten erzeugten AddressStore zurück.
     * @return Erzeugter AddressStore
     */
    @Override
    public AddressStore getAddressStore() {
        return store;
    }

    /**
     * Gibt die aus eingelesenen Kartenobjekten erzeugte Karte zurück.
     * @return Erzeugte Karte.
     */
    @Override
    public StreetMap getMap() {
        return map;
    }
}
