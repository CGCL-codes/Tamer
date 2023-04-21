package de.byteholder.geoclipse.tilefactory.esri;

import java.awt.geom.Point2D;
import java.math.BigDecimal;
import java.math.RoundingMode;
import de.byteholder.geoclipse.map.DefaultTileFactory;
import de.byteholder.geoclipse.map.TileFactoryInfo;
import de.byteholder.gpx.GeoPosition;

/**
 *
 * @author rbair
 */
public class ESRITileFactory extends DefaultTileFactory {

    private static final String projectionCode = "8";

    private static final String format = "png";

    private String userId;

    private String datasource;

    /** Creates a new instance of ESRITileFactory */
    public ESRITileFactory() {
        super(new ESRITileProviderInfo());
        ((ESRITileProviderInfo) super.getInfo()).factory = this;
        datasource = "ArcWeb:TA.Streets.NA";
    }

    public void setUserID(String id) {
        this.userId = id;
    }

    private static final class ESRITileProviderInfo extends TileFactoryInfo {

        private ESRITileFactory factory;

        private ESRITileProviderInfo() {
            super(0, 17, 18, 256, false, true, "http://www.arcwebservices.com/services/v2006/restmap?actn=getMap", "", "", "");
        }

        public String getTileUrl(int x, int y, int zoom) {
            int tileY = y;
            int tileX = x;
            int pixelX = tileX * factory.getTileSize() + (factory.getTileSize() / 2);
            int pixelY = tileY * factory.getTileSize() + (factory.getTileSize() / 2);
            GeoPosition latlong = projection.getPosition(new Point2D.Double(pixelX, pixelY), zoom, this);
            double numFeetPerDegreeLong = 24859.82 * 5280 / 360;
            double numPixelsPerDegreeLong = getLongitudeDegreeWidthInPixels(zoom);
            double numPixelsPerFoot = 96 * 12;
            int sf = (int) (numFeetPerDegreeLong / (numPixelsPerDegreeLong / numPixelsPerFoot));
            BigDecimal lat = new BigDecimal(latlong.getLatitude());
            BigDecimal lon = new BigDecimal(latlong.getLongitude());
            lat = lat.setScale(5, RoundingMode.DOWN);
            lon = lon.setScale(5, RoundingMode.DOWN);
            System.out.println("Tile      : [" + tileX + ", " + tileY + "]");
            System.out.println("Pixel     : [" + pixelX + ", " + pixelY + "]");
            System.out.println("Lat/Long  : [" + latlong.getLatitude() + ", " + latlong.getLongitude() + "]");
            System.out.println("Lat2/Long2: [" + lat.doubleValue() + ", " + lon.doubleValue() + "]");
            String url = baseURL + "&usrid=" + factory.userId + "&ds=" + factory.datasource + "&c=" + lon.doubleValue() + "%7C" + lat.doubleValue() + "&sf=" + sf + "&fmt=" + format + "&ocs=" + projectionCode;
            System.out.println("the URL: " + url);
            return url;
        }
    }
}
