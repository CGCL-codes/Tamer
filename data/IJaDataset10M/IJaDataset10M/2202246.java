package sk.bielyvlk.gpsdb;

import java.util.Hashtable;
import javax.microedition.lcdui.*;
import sk.bielyvlk.vlkui.*;

public class WpSymbols {

    private static Image[] source;

    private static final String[] names = { "Marina", "Bell", "Green Diamond", "Red Diamond", "Diver Down Flag 1", "Diver Down Flag 2", "Bank", "Fishing Area", "Gas Station", "Horn", "Residence", "Restaurant", "Light", "Bar", "Skull and Crossbones", "Green Square", "Red Square", "White Buoy", "Waypoint", "Shipwreck", "Navaid Amber", "Navaid Black", "Navaid Blue", "Navaid Green", "Navaid Red", "Navaid White", "White Dot", "Radio Beacon", "Campground", "Drinking Water", "Medical Facility", "Information", "Parking Area", "Park", "Picnic Area", "Scenic Area", "Skiing Area", "Swimming Area", "Car", "Hunting Area", "Shopping Center", "Exit", "Flag", "Capital", "City Large", "City Medium", "City Small", "Village", "Church", "Geocache", "Geocache Found", "Pizza", "Post Office", "VlkGPS", "Train", "Bus", "Metro", "Airport", "Tram", "Office", "Castle", "Tunel", "Lake", "Mountain", "Stadium", "Island", "Volcano", "Boat", "Internet", "Bridge", "Library/school", "Museum", "Tower", "Theatre", "Cinema", "Traditional Cache", "Traditional Cache" + " Found", "Multi-cache", "Multi-cache" + " Found", "Letterbox Cache", "Letterbox Cache" + " Found", "Unknown Cache", "Unknown Cache" + " Found", "Event Cache", "Event Cache" + " Found", "Earth Cache", "Earth Cache" + " Found", "Virtual Cache", "Webcam Cache", "Earth Cache", "Earth Cache" + " Found", "Final Location", "Parking Area", "Question to Answer", "Stages of a Multicache", "Trailhead", "Reference Point" };

    private static int[] garminSymbols = { 0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 22, 23, 24, 25, 29, 32, 35, 37, 151, 154, 156, 157, 158, 159, 160, 161, 162, 163, 170, 170, 172, 177, 178, 8203, 8200, 8199, 8198, 9198, 8236, 8255, 8256, 8213, 8214, 8257, 18, 18, 18, 16384, 18, 18, 0 };

    private static Hashtable lookupTable;

    public WpSymbols() {
        source = new Image[names.length];
    }

    public static Image getImage(int id) {
        try {
            if (source[id] == null) {
                try {
                    source[id] = Image.createImage("/symbols/" + id + ".png");
                } catch (Exception e) {
                    Debug.debug("MY symbol " + id + " ( " + names[id] + " ) not found");
                    try {
                        source[id] = Image.createImage("/res/vlkGPS.png");
                    } catch (Exception ee) {
                        Image err = Image.createImage(16, 16);
                        Graphics g = err.getGraphics();
                        g.drawLine(0, 0, 16, 16);
                        g.drawLine(0, 16, 0, 16);
                        source[id] = err;
                    }
                }
            }
            return source[id];
        } catch (Exception e) {
            return source[53];
        }
    }

    public static String getName(int id) {
        try {
            return names[id];
        } catch (Exception e) {
            return "";
        }
    }

    public static int getId(String name) {
        try {
            if (lookupTable == null) {
                lookupTable = new Hashtable();
                for (int i = 0; i < names.length; i++) {
                    lookupTable.put(names[i], new Integer(i));
                }
            }
            Integer symbol = (Integer) lookupTable.get(name);
            if (symbol != null) return symbol.intValue();
            return 53;
        } catch (Exception e) {
            return 53;
        }
    }

    public static int getGarminCode(int id) {
        try {
            return garminSymbols[id];
        } catch (Exception e) {
            return 0;
        }
    }

    public static int getCount() {
        return names.length;
    }
}
