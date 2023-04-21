package com.goldgewicht.francois.google.wave.extensions.robots.drmaps.util;

import java.util.logging.Logger;
import org.json.JSONArray;
import org.json.JSONObject;

/**
 * Simple Google Maps API helper.
 * 
 * @author francois.goldgewicht@gmail.com (Francois Goldgewicht)
 */
public class GoogleMapsApiHelper {

    private static final Logger log = Logger.getLogger(GoogleMapsApiHelper.class.getName());

    private static final String GOOGLE_MAPS_API_KEY = "ABQIAAAAYbZIktHU4Hcj2J2sSibCWxRIgZvwA4vX-W_nUaJX0Uphe2nykBSqfLjaOPXqw05uI9Jep_hjNa_69Q";

    private static final String GOOGLE_MAPS_API_GEOCODING_SERVICE_URL = "http://maps.google.com/maps/geo";

    private static final String GOOGLE_STATIC_MAPS_API_URL = "http://maps.google.com/staticmap";

    private static final String GOOGLE_MAPS_SITE_URL = "http://maps.google.com/maps";

    /**
	 * Invokes the Google Maps API Geocoding service to convert an address to
	 * geographic coordinates
	 * 
	 * @param address
	 *            the address to geocode
	 * @return the placemark or null if any problem
	 */
    public static GoogleMapsApiPlacemark geocodeAddress(String address) {
        GoogleMapsApiPlacemark placemark = null;
        try {
            log.info("Geocoding for: '" + address + "'...");
            String geocodingServiceCompleteUrl = GOOGLE_MAPS_API_GEOCODING_SERVICE_URL + "?q=" + HttpHelper.urlEncode(address) + "&key=" + GOOGLE_MAPS_API_KEY + "&sensor=false" + "&output=json";
            String jsonResponseAsString = HttpHelper.sendSynchronousHttpGetRequest(geocodingServiceCompleteUrl);
            JSONObject jsonResponse = new JSONObject(jsonResponseAsString);
            int responseCode = jsonResponse.getJSONObject("Status").getInt("code");
            switch(responseCode) {
                case 200:
                    log.info("Geocoding successful: 200 - G_GEO_SERVER_SUCCESS");
                    break;
                case 500:
                    log.info("Geocoding error: 500 - G_GEO_SERVER_ERROR");
                    break;
                case 601:
                    log.info("Geocoding error: 601 - G_GEO_MISSING_QUERY");
                    break;
                case 602:
                    log.info("Geocoding error: 602 - G_GEO_UNKNOWN_ADDRESS");
                    break;
                case 603:
                    log.info("Geocoding error: 603 - G_GEO_UNAVAILABLE_ADDRESS");
                    break;
                case 610:
                    log.info("Geocoding error: 610 - G_GEO_BAD_KEY");
                    break;
                case 620:
                    log.info("Geocoding error: 620 - G_GEO_TOO_MANY_QUERIES");
                    break;
                default:
                    log.info("Geocoding error: " + responseCode + " - Unknown !");
                    break;
            }
            if (responseCode == 200) {
                JSONArray jsonPlacemarks = jsonResponse.getJSONArray("Placemark");
                int placemarkSiblingCount = (jsonPlacemarks.length() - 1);
                JSONObject jsonPlacemark = jsonPlacemarks.getJSONObject(0);
                JSONArray jsonPlacemarkCoordinates = jsonPlacemark.getJSONObject("Point").getJSONArray("coordinates");
                double[] placemarkCoordinates = { jsonPlacemarkCoordinates.getDouble(0), jsonPlacemarkCoordinates.getDouble(1) };
                String placemarkAddress = jsonPlacemark.getString("address");
                placemark = new GoogleMapsApiPlacemark();
                placemark.setCoordinates(placemarkCoordinates);
                placemark.setAddress(placemarkAddress);
                placemark.setSiblingCount(placemarkSiblingCount);
                log.info("Returned: '" + placemarkAddress + "', '" + placemarkCoordinates[0] + ", " + placemarkCoordinates[1] + "', '" + placemarkSiblingCount + "'");
            }
        } catch (Exception e) {
            log.warning("Unknown error: " + e.getMessage());
        }
        return placemark;
    }

    /**
	 * Computes the Google Static Maps image URL for a point
	 * 
	 * @param geographicCoordinates
	 *            the coordinates of the point
	 * @param mapZoom
	 *            the zoom
	 * @param mapSize
	 *            the size
	 * @param mapType
	 *            the type
	 * @return the URL of the map image
	 */
    public static String computeMapImageUrl(double[] geographicCoordinates, String mapZoom, String mapSize, String mapType) {
        return GOOGLE_STATIC_MAPS_API_URL + "?zoom=" + mapZoom + "&size=" + mapSize + "&format=png" + "&maptype=" + mapType + "&markers=" + geographicCoordinates[1] + "," + geographicCoordinates[0] + ",blue" + "&key=" + GOOGLE_MAPS_API_KEY + "&sensor=false";
    }

    /**
	 * Computes the Google Maps site URL for an address
	 * 
	 * @param address
	 *            the address
	 * @return the URL of the site
	 */
    public static String computeMapSiteUrl(String address) {
        String url = null;
        try {
            url = GOOGLE_MAPS_SITE_URL + "?q=" + HttpHelper.urlEncode(address);
        } catch (Exception e) {
            log.warning("Unknown error: " + e.getMessage());
        }
        return url;
    }
}
