package mpr.openGPX.lib;

import java.util.Calendar;
import java.util.Date;

/**
 * 
 * @author Martin Preishuber
 *
 */
public class Waypoint {

    public double latitude;

    public double longitude;

    public Date time;

    public String name = "";

    public String description = "";

    public String symbol = "";

    public String source = "";

    public int elevation = Integer.MIN_VALUE;

    public String comment = "";

    private WaypointType mWaypointType = WaypointType.Unknown;

    /**
	 * 
	 */
    public Waypoint() {
        Calendar cal1970 = Calendar.getInstance();
        cal1970.set(1970, 1, 1);
        this.time = cal1970.getTime();
    }

    /**
	 * 
	 * @param strText
	 */
    public void parseTypeString(String strText) {
        String[] arrSplitted = strText.split("\\|");
        if (arrSplitted[0].toLowerCase().equals("geocache")) {
            this.mWaypointType = WaypointType.Cache;
        } else {
            String strCacheType;
            if (arrSplitted.length > 1) strCacheType = arrSplitted[1]; else strCacheType = arrSplitted[0];
            String[] strFirstWord = strCacheType.split(" ");
            this.mWaypointType = WaypointType.valueOf(strFirstWord[0]);
        }
    }

    /**
	 * 
	 * @return
	 */
    public WaypointType getType() {
        return this.mWaypointType;
    }

    /**
	 * 
	 * @param waypointType
	 */
    public void setType(WaypointType waypointType) {
        this.mWaypointType = waypointType;
    }

    /**
	 * Create a human readable representation for maps et al.
	 * @return
	 */
    public String getSnippet() {
        if (mWaypointType == WaypointType.Cache) {
            final String symbolLC = symbol.toLowerCase();
            if (symbolLC.equals("unknown cache") || symbolLC.equals("event cache")) return String.format("Header Coordinates [%s]", symbol); else if (symbolLC.equals("traditional cache")) return String.format("Cache [%s]", symbol); else if (symbolLC.equals("cache")) return "Cache"; else {
                final String descriptionLC = description.toLowerCase();
                if (descriptionLC.contains("traditional cache")) return String.format("Cache [%s]", symbol); else {
                    return String.format("Header Coordinates [%s]", symbol);
                }
            }
        } else {
            return String.format("%s [%s]", description, symbol);
        }
    }

    /**
	 * Returns a readable interpretation of the waypoint
	 */
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(String.format("Name: %s\n", this.name));
        sb.append(String.format("Description: %s\n", this.description));
        sb.append(String.format("Type: %s\n", this.mWaypointType));
        sb.append(String.format("Lat / Long: %.10f / %.10f\n", this.latitude, this.longitude));
        return sb.toString();
    }

    /**
	 * @param args
	 */
    public static void main(String[] args) {
        Waypoint wp = new Waypoint();
        wp.parseTypeString("Parking Area");
        System.out.println(wp.getType().toString());
        wp.parseTypeString("Stages of a Multicache");
        System.out.println(wp.getType().toString());
    }
}
