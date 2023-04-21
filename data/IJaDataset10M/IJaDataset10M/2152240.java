package org.dinopolis.gpstool.plugin.kismetimport;

import java.util.HashMap;
import org.dinopolis.gpstool.gui.layer.location.*;
import org.dinopolis.gpstool.util.geoscreen.GeoScreenList;
import org.xml.sax.Attributes;
import org.xml.sax.helpers.DefaultHandler;

/** 
 * SAX Handler for Kismet XML scan files.
 * The handler will generate a KismetWirelessNetwork Object
 * and save the data using a LocationMarkerSource.
 *
 *
 * @author sven@boeckelmann.org
 */
public class KismetLocationHandler extends DefaultHandler {

    /** needed to store the GPS info
     */
    LocationMarkerSource locationMarkerSource = null;

    /** network to be stored
     */
    KismetWirelessNetwork network = null;

    /** helps to anzalyze gps info
     */
    HashMap location = null;

    /** the current element withing gps-info
     */
    String key = null;

    /** data within an element
     */
    String CDATA = null;

    /** gpstool LocationMarkerCategory for 
     *  unencrypted AccessPoints
     */
    LocationMarkerCategory openAp = null;

    /** gpstool LocationMarkerCategory for 
     *  encrypted AccessPoints
     */
    LocationMarkerCategory encryptedAp = null;

    /** northern boundary 
     */
    float north = 0;

    /** southern boundary 
     */
    float south = 0;

    /** western boundary 
     */
    float west = 0;

    /** eastern boundary 
     */
    float east = 0;

    /** offset for boundaries
     */
    float offset = Float.parseFloat("0.001");

    /** create new instance of KismetLocatiionHandler
     * @param locationMarkerSource store networks using this source
     */
    public KismetLocationHandler(LocationMarkerSource locationMarkerSource) {
        this.locationMarkerSource = locationMarkerSource;
        openAp = LocationMarkerCategory.getCategory("open_ap");
        encryptedAp = LocationMarkerCategory.getCategory("encrypted_ap");
    }

    /** handle SAX startElement event
     * @param uri URI
     * @param localName local name
     * @param qName element name
     * @param attributes the Attributes
     */
    public void startElement(String uri, String localName, String qName, Attributes attributes) {
        try {
            if (qName.equals("wireless-network")) {
                String type = attributes.getValue("type");
                if (type != null && type.equals("infrastructure")) {
                    network = new KismetWirelessNetwork();
                    network.setWep(attributes.getValue("wep").equals("true"));
                } else network = null;
            }
            if (qName.equals("gps-info")) {
                location = new HashMap();
            }
        } catch (Exception e) {
            System.out.println("exception at startElement " + qName + ":" + e.getMessage());
        }
    }

    /** handle SAX endElement event
     * @param uri URI
     * @param localName local name
     * @param qName element name
     */
    public void endElement(String uri, String localName, String qName) {
        try {
            if (network != null && qName.equals("wireless-network")) {
                System.out.println("\nfound network \n" + network.getName() + "\nlat=" + network.getLatitude() + ", lon=" + network.getLongtitude());
                LocationMarkerFilter filter = new LocationMarkerFilter(LocationMarkerFilter.KEY_NAME, new String[] { "%" + network.getBssid() }, LocationMarkerFilter.LIKE_OPERATION, false);
                north = network.getMaxLatitude();
                south = network.getMinLatitude();
                west = network.getMinLongtitude();
                east = network.getMaxLongtitude();
                west = west - offset;
                east = east + offset;
                north = north + offset;
                south = south - offset;
                GeoScreenList geoScreenList = locationMarkerSource.getLocationMarkers(north, south, west, east, filter);
                if (geoScreenList.size() == 0) {
                    LocationMarkerCategory currentCategory = null;
                    if (network.getWep()) currentCategory = encryptedAp; else currentCategory = openAp;
                    LocationMarker locationMarker = new LocationMarker(network.getName(), network.getLatitude(), network.getLongtitude(), currentCategory);
                    locationMarkerSource.putLocationMarker(locationMarker);
                } else System.out.println("network is already known");
                network = null;
            }
            if (qName.equals("max-lat") || qName.equals("min-lat") || qName.equals("min-lon") || qName.equals("max-lon")) {
                location.put(qName, CDATA);
            }
            if (network != null && location != null && qName.equals("gps-info")) {
                network.setMaxLatitude(location.get("max-lat").toString());
                network.setMinLatitude(location.get("min-lat").toString());
                network.setMaxLongtitude(location.get("max-lon").toString());
                network.setMinLongtitude(location.get("min-lon").toString());
                location = null;
            }
            if (network != null && qName.equals("SSID")) {
                network.setSsid(CDATA);
            }
            if (network != null && qName.equals("BSSID")) {
                network.setBssid(CDATA);
            }
        } catch (Exception e) {
            System.out.println("exception at endElement " + qName + ":" + e.getMessage());
        }
    }

    /** process CDATA
     *  @param ch characters
     *  @param start start index
     *  @param length length to read
     */
    public void characters(char[] ch, int start, int length) {
        CDATA = new String(ch).substring(start, start + length);
    }
}
