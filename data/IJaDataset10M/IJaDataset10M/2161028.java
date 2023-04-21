package edu.upmc.opi.caBIG.caTIES.installer.fusion;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Iterator;
import java.util.List;
import org.apache.log4j.Logger;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;
import org.jdom.xpath.XPath;

public class CaTIES_GoogleGeocoderCaller {

    /**
	 * Field logger.
	 */
    private static final Logger logger = Logger.getLogger(CaTIES_GoogleGeocoderCaller.class);

    private String rawLocation = "";

    private String lat = "";

    private String lng = "";

    /**
	 * @param args
	 */
    public static void main(String[] args) {
        new CaTIES_GoogleGeocoderCaller();
    }

    public CaTIES_GoogleGeocoderCaller() {
    }

    private String synthesizeUrl() {
        StringBuffer sb = new StringBuffer();
        sb.append("http://maps.googleapis.com/maps/api/geocode/xml?address=");
        sb.append(this.rawLocation.replaceAll("\\s", "+"));
        sb.append("&sensor=false");
        return sb.toString();
    }

    @SuppressWarnings("rawtypes")
    public void execute() {
        try {
            String url = synthesizeUrl();
            byte[] xmlAsByteArray = pullMapBytes(url);
            String xmlAsString = new String(xmlAsByteArray);
            ByteArrayInputStream bis = new ByteArrayInputStream(xmlAsByteArray);
            logger.debug(xmlAsString);
            SAXBuilder sb = new SAXBuilder();
            Document doc = sb.build(bis);
            String xpathQueryString = "//result/geometry/location/lat";
            XPath xpath = XPath.newInstance(xpathQueryString);
            List results = xpath.selectNodes(doc);
            if (results.size() > 0) {
                for (Iterator iterator = results.iterator(); iterator.hasNext(); ) {
                    Element childElement = (Element) iterator.next();
                    this.lat = childElement.getText();
                    break;
                }
            }
            xpathQueryString = "//result/geometry/location/lng";
            xpath = XPath.newInstance(xpathQueryString);
            results = xpath.selectNodes(doc);
            if (results.size() > 0) {
                for (Iterator iterator = results.iterator(); iterator.hasNext(); ) {
                    Element childElement = (Element) iterator.next();
                    this.lng = childElement.getText();
                    break;
                }
            }
            logger.debug("lat = " + lat + " and lng = " + lng);
        } catch (JDOMException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private byte[] pullMapBytes(String directoryLocation) {
        ByteArrayOutputStream buffer = new ByteArrayOutputStream();
        try {
            URL url = new URL(directoryLocation);
            HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
            InputStream is = httpURLConnection.getInputStream();
            int nRead;
            byte[] data = new byte[1024];
            while ((nRead = is.read(data, 0, data.length)) != -1) {
                buffer.write(data, 0, nRead);
            }
            buffer.flush();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return buffer.toByteArray();
    }

    public String getLat() {
        return lat;
    }

    public void setLat(String lat) {
        this.lat = lat;
    }

    public String getLng() {
        return lng;
    }

    public void setLng(String lng) {
        this.lng = lng;
    }

    public String getRawLocation() {
        return rawLocation;
    }

    public void setRawLocation(String rawLocation) {
        this.rawLocation = rawLocation;
    }
}
