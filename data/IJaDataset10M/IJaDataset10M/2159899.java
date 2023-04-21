package com.topotracker.route;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.TimeZone;
import com.topotracker.geotiff.Projection;

public class WayPoint {

    private double[] location;

    private Projection proj;

    public WayPoint(double x, double y, Projection proj) {
        location = new double[] { x, y, 0.0 };
        this.proj = proj;
    }

    public void setLocation(double x, double y) {
        location[0] = x;
        location[1] = y;
    }

    public double[] getLocation() {
        return location;
    }

    public String toString() {
        double[] pos = new double[2];
        proj.imageToLonlat(new int[] { (int) location[0], (int) location[1] }, pos);
        DecimalFormat formatter = new DecimalFormat("#.000000");
        return ("(" + formatter.format(pos[0]) + "," + formatter.format(pos[1]) + ")");
    }

    static final double NA = Double.NEGATIVE_INFINITY;

    private double lat = NA;

    private double lon = NA;

    private double ele = NA;

    Date time = null;

    private String name = "";

    /**
	 * GPX files that are read in from outside sources may contain
	 * a bunch of other information that TopoTracker does not care
	 * about.  They are stored in hashmap and regurgitated if the
	 * gpx file is rewritten.
	 */
    protected LinkedHashMap<String, String> fields = new LinkedHashMap<String, String>();

    /**
	 * time strings read from and written to gpx files are in 
	 * the GMT time zone.  dateFormatGmt will be used to 
	 * parse/format those times
	 */
    private static SimpleDateFormat dateFormatGmt;

    /**
	 * time strings displayed to the user should be in local
	 * time zone.  dateFormatLocal should be used for this 
	 * purpose.
	 */
    public static SimpleDateFormat dateFormatLocal = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    public WayPoint() {
        if (dateFormatGmt == null) {
            dateFormatGmt = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            dateFormatGmt.setTimeZone(TimeZone.getTimeZone("GMT"));
        }
    }

    public WayPoint(double lat, double lon) {
        this();
        this.setLat(lat);
        this.setLon(lon);
    }

    public void toGpxString(StringBuffer gpx, boolean routePoint) {
        gpx.append(String.format("  <%1s lat=\"%1.12f\" lon=\"%1.12f\">%n", (routePoint ? "rtept" : "wpt"), lat, lon));
        for (String key : fields.keySet()) gpx.append(String.format("    <%1s>%1s</%1s>%n", key, fields.get(key), key));
        gpx.append(String.format("  </%1s>%n", (routePoint ? "rtept" : "wpt")));
    }

    /**
	 * @param lat the lat to set
	 */
    public void setLat(double lat) {
        this.lat = lat;
    }

    /**
	 * @return the lat
	 */
    public double getLat() {
        return lat;
    }

    /**
	 * @param lon the lon to set
	 */
    public void setLon(double lon) {
        this.lon = lon;
    }

    /**
	 * @return the lon
	 */
    public double getLon() {
        return lon;
    }

    /**
	 * @param ele the ele to set
	 */
    void setEle(double ele) {
        this.ele = ele;
        fields.put("ele", String.format("%1.3f", ele));
    }

    /**
	 * @return the ele
	 */
    double getEle() {
        return ele;
    }

    /**
	 * @param name the name to set
	 */
    void setName(String name) {
        this.name = name;
        fields.put("name", name);
    }

    /**
	 * @return the name
	 */
    String getName() {
        return name;
    }

    public String getTimeGmt() {
        return dateFormatGmt.format(time);
    }

    public String getTimeLocal() {
        return dateFormatLocal.format(time);
    }

    public void setTime(String time) {
        try {
            this.time = dateFormatGmt.parse(time.replace('T', ' '));
            fields.put("time", dateFormatGmt.format(this.time));
        } catch (ParseException e) {
            e.printStackTrace();
            System.exit(1);
        }
    }

    public void addField(String key, String value) {
        if (key.equals("ele")) setEle(Double.parseDouble(value)); else if (key.equals("name")) setName(value); else if (key.equals("time")) setTime(value); else fields.put(key, value);
    }

    public String getField(String key) {
        return fields.get(key);
    }
}
