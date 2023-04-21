package de.uniba.kinf.cityguide.dataimport;

import java.awt.Point;
import java.awt.Polygon;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class DataImport {

    private Map<Integer, Polygon> tesselations;

    private List<POI> poiList;

    private String kmlFile;

    /**
	 * Needs a kml file with a set of tesselations
	 * @param kmlFile
	 */
    public DataImport(String kmlFile) {
        this.kmlFile = kmlFile;
        this.tesselations = new HashMap<Integer, Polygon>();
        this.poiList = new ArrayList<POI>();
        try {
            parseKML();
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        } catch (SAXException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
	 * Retrieve the tesselations from the kml file and store them as a polygon
	 * @throws ParserConfigurationException
	 * @throws SAXException
	 * @throws IOException
	 */
    private void parseKML() throws ParserConfigurationException, SAXException, IOException {
        DocumentBuilderFactory docBuilderFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder docBuilder;
        docBuilder = docBuilderFactory.newDocumentBuilder();
        Document doc = docBuilder.parse(new File(kmlFile));
        doc.getDocumentElement().normalize();
        NodeList lop = doc.getElementsByTagName("coordinates");
        int tp = lop.getLength();
        for (int i = 0; i < tp; i++) {
            Node coordinates = lop.item(i);
            this.tesselations.put(i, makePolygon(coordinates.getTextContent()));
        }
    }

    /**
	 * Transforms a polygon based on GeoPoints into a polygon in a plain coordinate system with x and y values.
	 * This allows the application of the "point in polygon" procedure later on
	 * @param coords
	 * @return
	 */
    private Polygon makePolygon(String coords) {
        String[] coordsArray = coords.split(",0");
        Polygon p = new Polygon();
        CoordinateConversion cc = new CoordinateConversion();
        for (int i = 0; i < coordsArray.length - 1; i += 3) {
            String[] latlong = coordsArray[i].split(",");
            double lat = Double.parseDouble(latlong[0]);
            double lng = Double.parseDouble(latlong[1]);
            LatLng utm = cc.latLon2UTM(lat, lng);
            int x = ((int) utm.getLat());
            int y = ((int) utm.getLng());
            p.addPoint(x, y);
        }
        return p;
    }

    /**
	 * Assign a set of POIs to a set of tesselated areas
	 * @param pois
	 */
    public void insertPOIs(List<POI> pois) {
        for (POI point : pois) {
            for (int tessNo : this.tesselations.keySet()) {
                Polygon p = this.tesselations.get(tessNo);
                if (contains(p, point)) {
                    point.tesselation = tessNo;
                    this.poiList.add(point);
                }
            }
        }
    }

    /**
	 * Assign a poi to a tesselation
	 * @param poi
	 * @return
	 */
    public POI insertPOI(POI poi) {
        for (int tessNo : this.tesselations.keySet()) {
            Polygon p = this.tesselations.get(tessNo);
            if (contains(p, poi)) {
                poi.setTesselation(tessNo);
            }
        }
        return poi;
    }

    /**
	 * print the internal poi list
	 */
    public void printPOIList() {
        for (POI poi : this.poiList) {
            System.out.println(poi.lat + "," + poi.lng + ":" + poi.tesselation);
        }
    }

    /**
	 * Find out if a poi is inside a polygon
	 * @param poly
	 * @param p
	 * @return
	 */
    private boolean contains(Polygon poly, POI p) {
        CoordinateConversion cc = new CoordinateConversion();
        LatLng utm = cc.latLon2UTM(p.lng, p.lat);
        int x = ((int) utm.getLat());
        int y = ((int) utm.getLng());
        boolean retVal = poly.contains(new Point(x, y));
        return retVal;
    }
}
