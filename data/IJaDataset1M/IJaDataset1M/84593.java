package fi.passiba.groups.ui.pages.googlemap;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Static class to get markers inside a given box
 * 
 * In reality this would call a webservice or query your database, but for simplicity I am
 * just generating random markers with random content
 * 
 */
public class GetLocations {

    public static Map<Integer, LocationData> locationData = new HashMap<Integer, LocationData>();

    public static int ID = 0;

    public static List<LocationData> getLocations(double south, double east, double north, double west) {
        List<LocationData> locations = new ArrayList<LocationData>();
        for (int index = 0; index < 5; index++) {
            double lat = Math.random() * (west - east) + east;
            double lng = Math.random() * (north - south) + south;
            LocationData locationContent = new LocationData();
            locationContent.setId(++ID);
            locationContent.setLat(lat);
            locationContent.setLng(lng);
            locationContent.setTitle("interesting location");
            locationContent.setContent(lat + "," + lng + ": some interesing content" + Math.random());
            locations.add(locationContent);
            locationData.put(ID, locationContent);
        }
        return locations;
    }

    public static LocationData getLocationData(int id) {
        return locationData.get(id);
    }
}
