package boston.Bus.Map.parser;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.Reader;
import java.io.StringReader;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.concurrent.ConcurrentHashMap;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import org.xml.sax.Attributes;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.AttributesImpl;
import org.xml.sax.helpers.DefaultHandler;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import skylight1.opengl.files.QuickParseUtil;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.util.Xml;
import android.util.Xml.Encoding;
import boston.Bus.Map.data.BusLocation;
import boston.Bus.Map.data.Directions;
import boston.Bus.Map.data.RouteConfig;
import boston.Bus.Map.data.RoutePool;
import boston.Bus.Map.transit.TransitSystem;

public class VehicleLocationsFeedParser extends DefaultHandler {

    private final Drawable bus;

    private final Drawable arrow;

    private final Directions directions;

    private final HashMap<String, String> routeKeysToTitles;

    public VehicleLocationsFeedParser(Drawable bus, Drawable arrow, Directions directions, HashMap<String, String> routeKeysToTitles) {
        this.bus = bus;
        this.arrow = arrow;
        this.directions = directions;
        this.routeKeysToTitles = routeKeysToTitles;
    }

    public void runParse(InputStream data) throws SAXException, ParserConfigurationException, IOException {
        android.util.Xml.parse(data, Encoding.UTF_8, this);
        data.close();
    }

    private long lastUpdateTime;

    private final HashMap<String, BusLocation> busMapping = new HashMap<String, BusLocation>();

    private final HashMap<String, Integer> tagCache = new HashMap<String, Integer>();

    private static final String vehicleKey = "vehicle";

    private static final String latKey = "lat";

    private static final String lonKey = "lon";

    private static final String idKey = "id";

    private static final String routeTagKey = "routeTag";

    private static final String secsSinceReportKey = "secsSinceReport";

    private static final String headingKey = "heading";

    private static final String predictableKey = "predictable";

    private static final String dirTagKey = "dirTag";

    private static final String lastTimeKey = "lastTime";

    private static final String timeKey = "time";

    private static final String tripTagKey = "tripTag";

    private static final String speedKmHrKey = "speedKmHr";

    @Override
    public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
        if (localName.equals(vehicleKey)) {
            clearAttributes(attributes);
            float lat = QuickParseUtil.parseFloat(getAttribute(latKey, attributes));
            float lon = QuickParseUtil.parseFloat(getAttribute(lonKey, attributes));
            String id = getAttribute(idKey, attributes);
            String route = getAttribute(routeTagKey, attributes);
            int seconds = Integer.parseInt(getAttribute(secsSinceReportKey, attributes));
            String heading = getAttribute(headingKey, attributes);
            boolean predictable = Boolean.parseBoolean(getAttribute(predictableKey, attributes));
            String dirTag = getAttribute(dirTagKey, attributes);
            long lastFeedUpdate = TransitSystem.currentTimeMillis() - (seconds * 1000);
            String inferBusRoute = null;
            final int arrowTopDiff = bus.getIntrinsicHeight() / 5;
            BusLocation newBusLocation = new BusLocation(lat, lon, id, lastFeedUpdate, lastUpdateTime, heading, predictable, dirTag, inferBusRoute, bus, arrow, route, directions, routeKeysToTitles.get(route), false, arrowTopDiff);
            if (busMapping.containsKey(id)) {
                newBusLocation.movedFrom(busMapping.get(id));
            }
            busMapping.put(id, newBusLocation);
        } else if (localName.equals(lastTimeKey)) {
            lastUpdateTime = Long.parseLong(attributes.getValue(timeKey));
            lastUpdateTime += TransitSystem.getTimeZone().getOffset(lastUpdateTime);
            for (String key : busMapping.keySet()) {
                busMapping.get(key).setLastUpdateInMillis(lastUpdateTime);
            }
        }
    }

    private String getAttribute(String key, Attributes attributes) {
        return XmlParserHelper.getAttribute(key, attributes, tagCache);
    }

    private void clearAttributes(Attributes attributes) {
        XmlParserHelper.clearAttributes(attributes, tagCache);
    }

    public double getLastUpdateTime() {
        return lastUpdateTime;
    }

    public void fillMapping(ConcurrentHashMap<String, BusLocation> outputBusMapping) {
        outputBusMapping.putAll(busMapping);
    }
}
