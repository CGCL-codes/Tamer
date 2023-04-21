package org.dorfkind.android.eismann;

import java.net.MalformedURLException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.htmlcleaner.CleanerProperties;
import org.htmlcleaner.HtmlCleaner;
import org.htmlcleaner.TagNode;
import org.osmdroid.util.GeoPoint;

/**
 * @author andreas
 * 
 */
public class Eismann {

    private CleanerProperties props = new CleanerProperties();

    private TagNode tagNode;

    private StringBuffer nodevalue;

    private float lat;

    private float lon;

    private float speed;

    private float heading;

    private String time;

    private String url = "http://www.wo-ist-der-eismann.de/exec?action=eismann&function=position";

    public GeoPoint getCoord() {
        return new GeoPoint((int) (lat * 1e6), (int) (lon * 1e6));
    }

    public float getSpeed() {
        return speed;
    }

    public float getHeading() {
        return heading;
    }

    public String getTime() {
        SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy 'um' HH:mm:ss");
        return sdf.format(new Date(Long.valueOf(time)));
    }

    public void updatePosition() {
        try {
            tagNode = new HtmlCleaner(props).clean(new URL(url));
            nodevalue = tagNode.getText();
            String patternStr = "\\(\\ *\\d+,\\ *([0-9.-]+),\\ *([0-9.-]+),\\ *([0-9.-]+),\\ *([0-9.-]+),\\ *([0-9]+)";
            Pattern pattern = Pattern.compile(patternStr);
            Matcher matcher = pattern.matcher(nodevalue);
            matcher.find();
            lat = Float.parseFloat(matcher.group(1));
            lon = Float.parseFloat(matcher.group(2));
            heading = Float.parseFloat(matcher.group(3));
            speed = Float.parseFloat(matcher.group(4));
            time = matcher.group(5);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
