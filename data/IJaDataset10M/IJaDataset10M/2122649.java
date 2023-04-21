package com.mymaps.geocode;

import java.io.IOException;
import java.io.InputStream;
import java.util.Iterator;
import java.util.List;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;
import com.vividsolutions.jts.geom.Coordinate;

public class ResponseParser {

    private static final Log logger = LogFactory.getLog(ResponseParser.class);

    public static final String NODE_POINT = "Point";

    public static final String NODE_LAT = "lat";

    public static final String NODE_LONG = "long";

    public static final String NODE_DESCRIPTION = "desc";

    public ResponseParser() {
        super();
    }

    /**
	 * ParseCoordinate extracts a Coordinate out of
	 * a REST-RDF.  This format is returned by
	 * geocoder.us and brainoff.com.
	 * 
	 * @param response InputStream
	 * @return Coordinate or null if parse fails
	 */
    public static Coordinate parseCoordinate(InputStream response) {
        Coordinate retVal = null;
        SAXBuilder sax = new SAXBuilder();
        try {
            Document doc = sax.build(response);
            if (doc != null) {
                Element root = doc.getRootElement();
                if (root != null) {
                    List rootList = root.getChildren();
                    Iterator iter = rootList.iterator();
                    while (iter.hasNext()) {
                        Element point = (Element) iter.next();
                        if (point.getName().equals(NODE_POINT)) {
                            List pointList = point.getChildren();
                            Iterator pIter = pointList.iterator();
                            Element lat = null;
                            Element lng = null;
                            while (pIter.hasNext()) {
                                Element l = (Element) pIter.next();
                                if (l.getName().equals(NODE_LAT)) {
                                    lat = l;
                                } else if (l.getName().equals(NODE_LONG)) {
                                    lng = l;
                                }
                            }
                            if (lat != null && lng != null) {
                                String sLat = lat.getText();
                                String sLng = lng.getText();
                                double dLat = new Double(sLat).doubleValue();
                                double dLng = new Double(sLng).doubleValue();
                                retVal = new Coordinate(dLng, dLat);
                                break;
                            }
                        }
                    }
                }
            }
        } catch (JDOMException e) {
            logger.error(e.getMessage(), new Throwable(e));
        } catch (IOException e) {
            logger.error(e.getMessage(), new Throwable(e));
        } catch (NumberFormatException e) {
            logger.error(e.getMessage(), new Throwable(e));
        }
        return retVal;
    }

    /**
	 * ParseCoordinate extracts a description out of
	 * a REST-RDF.  This format is returned by
	 * geocoder.us.
	 * 
	 * @param response InputStream
	 * @return Description or null if description is 
	 * not available or parse fails
	 */
    public static String parseDescription(String response) {
        String retVal = null;
        SAXBuilder sax = new SAXBuilder();
        try {
            Document doc = sax.build(response);
            if (doc != null) {
                Element root = doc.getRootElement();
                if (root != null) {
                    List rootList = root.getChildren();
                    Iterator iter = rootList.iterator();
                    while (iter.hasNext()) {
                        Element point = (Element) iter.next();
                        if (point.getName().equals(NODE_POINT)) {
                            List pointList = point.getChildren();
                            Iterator pIter = pointList.iterator();
                            while (pIter.hasNext()) {
                                Element l = (Element) pIter.next();
                                if (l.getName().equals(NODE_DESCRIPTION)) {
                                    retVal = l.getText();
                                    break;
                                }
                            }
                        }
                    }
                }
            }
        } catch (JDOMException e) {
            logger.error(e.getMessage(), new Throwable(e));
        } catch (IOException e) {
            logger.error(e.getMessage(), new Throwable(e));
        } catch (NumberFormatException e) {
            logger.error(e.getMessage(), new Throwable(e));
        }
        return retVal;
    }

    /**
	 * CleanValue ensures a value is valid to be 
	 * used in a URL.  This includes trimming 
	 * leading and trailing whitespace, and replacing
	 * embedded spaces.
	 * 
	 * @param value String value to be cleaned
	 * @return String cleaned value
	 */
    public static final String cleanValue(String value) {
        String retVal = value;
        if (value != null) {
            retVal = value.trim();
            retVal = retVal.replaceAll(" ", "+");
        }
        return retVal;
    }
}
