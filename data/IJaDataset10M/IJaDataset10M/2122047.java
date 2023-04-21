package com.goldgewicht.francois.google.wave.extensions.robots.drmaps;

import java.util.logging.Logger;
import com.goldgewicht.francois.google.wave.extensions.robots.drmaps.util.GoogleMapsApiHelper;
import com.goldgewicht.francois.google.wave.extensions.robots.drmaps.util.GoogleMapsApiPlacemark;
import com.google.wave.api.AbstractRobotServlet;
import com.google.wave.api.Blip;
import com.google.wave.api.Event;
import com.google.wave.api.Image;
import com.google.wave.api.RobotMessageBundle;
import com.google.wave.api.TextView;
import com.google.wave.api.Wavelet;

/**
 * The main servlet for Dr. Maps robot.
 * 
 * @author francois.goldgewicht@gmail.com (Francois Goldgewicht)
 */
public class DrMapsServlet extends AbstractRobotServlet {

    private static final Logger log = Logger.getLogger(DrMapsServlet.class.getName());

    private static String USAGE_INSTRUCTIONS = "Hello!\nUsage:\n/map address\nExample:\n/map 1600 amphitheatre parkway mountain view";

    private static String COMMAND_PREFIX = "/map";

    private static String DEFAULT_MAP_ZOOM = "15";

    private static String DEFAULT_MAP_SIZE = "350x350";

    private static String DEFAULT_MAP_TYPE = "roadmap";

    @Override
    public void processEvents(RobotMessageBundle robotMessageBundle) {
        Wavelet wavelet = robotMessageBundle.getWavelet();
        if (robotMessageBundle.wasSelfAdded()) {
            processWaveletSelfAddedEvent(wavelet);
        } else {
            for (Event event : robotMessageBundle.getBlipSubmittedEvents()) {
                processBlipSubmittedEvent(event.getBlip());
            }
        }
    }

    /**
	 * Processes the event: Wavelet self added.
	 * 
	 * @param wavelet
	 *            the wavelet
	 */
    private void processWaveletSelfAddedEvent(Wavelet wavelet) {
        Blip blip = wavelet.appendBlip();
        TextView textView = blip.getDocument();
        textView.append(USAGE_INSTRUCTIONS);
    }

    /**
	 * Processes the event: Blip submitted.
	 * 
	 * @param blip
	 *            the blip
	 */
    private void processBlipSubmittedEvent(Blip blip) {
        String response = null;
        String blipDocumentText = null;
        if (blip.getBlipId().equals(blip.getWavelet().getRootBlipId())) {
            blipDocumentText = blip.getWavelet().getTitle();
        } else {
            blipDocumentText = blip.getDocument().getText();
        }
        log.info("Received blip: '" + blipDocumentText + "'");
        if (blipDocumentText != null && blipDocumentText.startsWith(COMMAND_PREFIX)) {
            String address = blipDocumentText.substring(COMMAND_PREFIX.length()).trim();
            if (address.length() > 0) {
                generateResponseForAddress(blip.getDocument(), address);
            } else {
                response = "Error: please indicate an address.";
            }
        }
        if (response != null) {
            blip.getDocument().append("\n" + response);
        }
    }

    /**
	 * Generates the robot response for a typed address
	 * 
	 * @param document
	 *            the document
	 * @param address
	 *            the address
	 */
    private void generateResponseForAddress(TextView document, String address) {
        GoogleMapsApiPlacemark placemark = GoogleMapsApiHelper.geocodeAddress(address);
        if (placemark != null) {
            if (placemark.getSiblingCount() == 0) {
                document.append("\nFound 1 address: " + placemark.getAddress());
            } else {
                document.append("\nFound " + (placemark.getSiblingCount() + 1) + " adresses, first of which is: " + placemark.getAddress());
            }
            String mapImageUrl = GoogleMapsApiHelper.computeMapImageUrl(placemark.getCoordinates(), DEFAULT_MAP_ZOOM, DEFAULT_MAP_SIZE, DEFAULT_MAP_TYPE);
            String mapSiteUrl = GoogleMapsApiHelper.computeMapSiteUrl(address);
            document.append("\nHere is a little map:\n");
            document.append("\n ");
            Image mapImage = new Image();
            mapImage.setProperty("url", mapImageUrl);
            document.appendElement(mapImage);
            document.append("\nFor the big map, see Google Maps: " + mapSiteUrl);
        } else {
            document.append("\nError: cannot retrieve map for this address");
        }
    }
}
