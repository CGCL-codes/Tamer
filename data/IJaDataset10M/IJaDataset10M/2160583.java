package org.kaintoch.gis.maps;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.MediaTracker;
import java.awt.Point;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;

/**
 * This class provides an image which shows a map.
 * The map is loaded from OpenStreetMap synchronously
 * and shows an area which is defined by the user of this class.
 * @see http://wiki.openstreetmap.org/index.php/Slippy_map_tilenames
 * @author stefan
 */
public class OsmMapImageSync implements IMapImage {

    /** this's class logger */
    private static Logger logger = Logger.getLogger("org.kaintoch.gis.maps.OsmMapImageSync");

    public static final String TILE_OSM_BASE_URL = "http://tile.openstreetmap.org/";

    public static final String MAPLINT_OSM_BASE_URL = "http://tah.openstreetmap.org/Tiles/maplint.php/";

    public static final String OSMARENDER_TAH_OSM_BASE_URL = "http://tah.openstreetmap.org/Tiles/tile.php/";

    public static final String FIREFISHS_PROXY_OSM_BASE_URL = "http://osm-tah-cache.firefishy.com/~ojw/Tiles/tile.php/";

    public static final String DEFAULT_OSM_BASE_URL = TILE_OSM_BASE_URL;

    public static final String CACHE_PREFIX = "C:\\temp\\jgpxcache\\";

    public static final String CACHE_EXTENSION = ".png";

    public static final String CACHE_FORMAT = "PNG";

    private BufferedImage image = null;

    private String osmBaseUrl = DEFAULT_OSM_BASE_URL;

    private String cachePathBaseUrl = "";

    private ImageIcon failedIcon = null;

    private int osmZoom = 0;

    private int osmX = 0;

    private int osmY = 0;

    /** width in tiles */
    private int osmWidth = 1;

    /** height in tiles */
    private int osmHeight = 1;

    private int reqOsmZoom = 0;

    private int reqOsmX = 0;

    private int reqOsmY = 0;

    private int reqOsmWidth = 1;

    private int reqOsmHeight = 1;

    private double parmLongMin = 0;

    private double parmLongMax = 0;

    private double parmLatMin = 0;

    private double parmLatMax = 0;

    /** Width and height of a OpenStreetMap tile */
    public static final int OSM_TILE_SIZE = 256;

    /**
	 * Constructor. Required for derived classes.
	 */
    protected OsmMapImageSync() {
    }

    public OsmMapImageSync(double longMin, double longMax, double latMin, double latMax, int zoom, String baseUrl) {
        init(longMin, longMax, latMin, latMax, zoom, baseUrl);
    }

    public OsmMapImageSync(double longMin, double longMax, double latMin, double latMax, int zoom) {
        init(longMin, longMax, latMin, latMax, zoom, DEFAULT_OSM_BASE_URL);
    }

    private static boolean isBaseUrl(String baseUrl) {
        return (baseUrl != null && baseUrl.trim().length() != 0);
    }

    protected void init_part1(double longMin, double longMax, double latMin, double latMax, int zoom, String baseUrl) {
        parmLongMin = longMin;
        parmLongMax = longMax;
        parmLatMin = latMin;
        parmLatMax = latMax;
        osmBaseUrl = ((OsmMapImageSync.isBaseUrl(baseUrl)) ? baseUrl : DEFAULT_OSM_BASE_URL);
        cachePathBaseUrl = url2FilePart(osmBaseUrl);
        calcReqTiles(zoom);
        req2Osm();
        image = new BufferedImage(osmWidth * OSM_TILE_SIZE, osmHeight * OSM_TILE_SIZE, BufferedImage.TYPE_INT_RGB);
    }

    protected void init(double longMin, double longMax, double latMin, double latMax, int zoom, String baseUrl) {
        init_part1(longMin, longMax, latMin, latMax, zoom, baseUrl);
        osmLoadTiles();
    }

    private void req2Osm() {
        osmZoom = reqOsmZoom;
        osmX = reqOsmX;
        osmY = reqOsmY;
        osmWidth = reqOsmWidth;
        osmHeight = reqOsmHeight;
    }

    /**
	 * 
	 */
    private boolean calcReqTiles(int zoom) {
        boolean fit = false;
        int xMin = 0;
        int yMin = 0;
        int wid = 1;
        int hei = 1;
        if (zoom <= 17) {
            xMin = xTile(parmLongMin, zoom);
            int xMax = xTile(parmLongMax, zoom);
            yMin = yTile(parmLatMax, zoom);
            int yMax = yTile(parmLatMin, zoom);
            wid = Math.abs(xMax - xMin) + 1;
            hei = Math.abs(yMax - yMin) + 1;
            fit = true;
        }
        if (fit) {
            reqOsmZoom = zoom;
            reqOsmX = xMin;
            reqOsmY = yMin;
            reqOsmWidth = wid;
            reqOsmHeight = hei;
        }
        return fit;
    }

    /**
	 * @see org.kaintoch.gis.maps.IMapImage#getImage()
	 */
    public Image getImage() {
        return image;
    }

    /**
	 * @see org.kaintoch.gis.maps.IMapImage#getXY(double, double)
	 */
    public Point getXY(double longitude, double latitude) {
        int x = -128;
        int y = -128;
        int xTile = OsmMapImageSync.xTile(longitude, osmZoom);
        int yTile = OsmMapImageSync.yTile(latitude, osmZoom);
        double longLeft = OsmMapImageSync.longLeft(osmZoom, xTile);
        double longRight = OsmMapImageSync.longLeft(osmZoom, xTile + 1);
        double latTop = OsmMapImageSync.latTop(osmZoom, yTile);
        double latBottom = OsmMapImageSync.latTop(osmZoom, yTile + 1);
        int xPixTile = (int) (OSM_TILE_SIZE * (longitude - longLeft) / (longRight - longLeft));
        int yPixTile = (int) (OSM_TILE_SIZE * (latitude - latBottom) / (latTop - latBottom));
        x = (xTile - osmX) * OSM_TILE_SIZE + xPixTile;
        y = (yTile - osmY) * OSM_TILE_SIZE + yPixTile;
        return new Point(x, OSM_TILE_SIZE - y);
    }

    /**
	 * Reinit and reload this map if required.
	 * @param longMin new minimum longitude
	 * @param longMax new maximum longitude
	 * @param latMin new minimum latitude
	 * @param latMax new maximum latitude
	 * @param width map's new pixel width
	 * @param height map's new pixel height
	 * @return true if reinit was required, false otherwise.
	 * @see org.kaintoch.gis.maps.IMapImage#reinit(double, double, double, double, int, int)
	 */
    public boolean reinit(double longMin, double longMax, double latMin, double latMax, int width, int height) {
        return false;
    }

    /**
	 * @see org.kaintoch.gis.maps.IMapImage#isLoading()
	 */
    public boolean isLoading() {
        return false;
    }

    public static double xScaled(double longitude, int zoom) {
        return (longitude + 180.0) / 360.0 * (1 << zoom);
    }

    public static double yScaled(double latitude, int zoom) {
        return (1.0 - Math.log(Math.tan(latitude * Math.PI / 180.0) + 1.0 / Math.cos(latitude * Math.PI / 180.0)) / Math.PI) / 2.0 * (1 << zoom);
    }

    public static int xTile(double longitude, int zoom) {
        return (int) Math.floor(xScaled(longitude, zoom));
    }

    public static int yTile(double latitude, int zoom) {
        return (int) Math.floor(yScaled(latitude, zoom));
    }

    public static int x(double longitude, int zoom) {
        return (int) Math.floor(xScaled(longitude, zoom) * OSM_TILE_SIZE);
    }

    public static int y(double latitude, int zoom) {
        return (int) Math.floor(yScaled(latitude, zoom) * OSM_TILE_SIZE);
    }

    public static double longLeft(int zoom, int xTile) {
        return ((double) xTile) / (1 << zoom) * 360.0 - 180.0;
    }

    public static double latTop(int zoom, int yTile) {
        double n = Math.PI - 2.0 * Math.PI * yTile / (1 << zoom);
        return (180.0 / Math.PI * Math.atan(0.5 * (Math.exp(n) - Math.exp(-n))));
    }

    public static String osmTileUrlLonLat(double lon, double lat, int zoom, String baseUrl) {
        int xtile = xTile(lon, zoom);
        int ytile = yTile(lat, zoom);
        String url = osmTileUrl(xtile, ytile, zoom, baseUrl);
        return url;
    }

    public static String osmTileUrl(int x, int y, int zoom, String baseUrl) {
        String url = ((OsmMapImageSync.isBaseUrl(baseUrl)) ? baseUrl : DEFAULT_OSM_BASE_URL) + zoom + "/" + x + "/" + y + ".png";
        return url;
    }

    private ImageIcon osmLoadTile(int zoom, int x, int y) {
        logger.finest("OsmMapImage#osmLoadTile(int zoom, int x, int y)");
        ImageIcon icon = null;
        boolean loadingFailed = true;
        try {
            icon = getImageFromCache(osmBaseUrl, zoom, x, y);
            if (icon == null) {
                String url = OsmMapImageSync.osmTileUrl(x, y, zoom, osmBaseUrl);
                logger.info("Loading: " + url);
                icon = new ImageIcon(new URL(url));
                putImageToCache(icon, osmBaseUrl, zoom, x, y);
            }
            if (icon != null && icon.getImage() != null && icon.getImageLoadStatus() != MediaTracker.ERRORED) {
                loadingFailed = false;
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            if (loadingFailed) {
                icon = getFailedIcon();
            }
        }
        logger.finest("OsmMapImage#osmLoadTile(int zoom, int x, int y) done");
        return icon;
    }

    private String url2FilePart(String baseUrl) {
        return baseUrl.replaceAll("[^a-zA-Z0-9_.+-]", "_");
    }

    private ImageIcon getImageFromCache(String baseUrl, int zoom, int x, int y) {
        String fName = CACHE_PREFIX + cachePathBaseUrl + "_" + zoom + "_" + x + "_" + y + CACHE_EXTENSION;
        logger.info("reading cached tile " + fName);
        ImageIcon icon = null;
        try {
            icon = new ImageIcon(fName);
            if (icon == null || icon.getImage() == null || icon.getImageLoadStatus() == MediaTracker.ERRORED) {
                icon = null;
                logger.info("reading cached tile failed " + fName);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return icon;
    }

    private void putImageToCache(ImageIcon icon, String baseUrl, int zoom, int x, int y) {
        if (icon != null && icon.getImage() != null && icon.getImageLoadStatus() != MediaTracker.ERRORED) {
            String fName = CACHE_PREFIX + cachePathBaseUrl + "_" + zoom + "_" + x + "_" + y + CACHE_EXTENSION;
            logger.info("writing cached tile " + fName);
            BufferedImage bufImg = createBufferedImage(icon.getImage());
            try {
                ImageIO.write(bufImg, CACHE_FORMAT, new File(fName));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
	 * Get a BufferedImage from a Image.
	 * @param img An Image object.
	 * @return the BufferedImage
	 */
    public BufferedImage createBufferedImage(Image img) {
        Canvas c = new Canvas();
        try {
            MediaTracker tracker = new MediaTracker(c);
            tracker.addImage(img, 0);
            tracker.waitForID(0);
        } catch (Exception e) {
        }
        int width = img.getWidth(c);
        int height = img.getHeight(c);
        BufferedImage bi = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        Graphics2D biContext = bi.createGraphics();
        biContext.drawImage(img, 0, 0, null);
        return bi;
    }

    /**
	 * @return
	 */
    private ImageIcon getFailedIcon() {
        if (failedIcon == null) {
            BufferedImage image = new BufferedImage(OSM_TILE_SIZE, OSM_TILE_SIZE, BufferedImage.TYPE_INT_RGB);
            Graphics2D g = image.createGraphics();
            g.setFont(new Font("SansSerif", Font.PLAIN, 12));
            g.setPaintMode();
            g.setColor(Color.GRAY);
            g.setBackground(Color.WHITE);
            g.clearRect(0, 0, OSM_TILE_SIZE, OSM_TILE_SIZE);
            g.drawString("Loading failed", 1, OSM_TILE_SIZE / 2);
            failedIcon = new ImageIcon(image);
        }
        return failedIcon;
    }

    protected void osmLoadTiles() {
        int xx;
        int yy;
        Graphics g = image.getGraphics();
        g.setColor(Color.WHITE);
        g.fillRect(0, 0, osmWidth * OSM_TILE_SIZE, osmHeight * OSM_TILE_SIZE);
        g.setColor(Color.RED);
        for (xx = 0; xx < osmWidth; ++xx) {
            for (yy = 0; yy < osmHeight; ++yy) {
                int xTile = xx * OSM_TILE_SIZE + 1;
                int yTile = yy * OSM_TILE_SIZE + 1;
                logger.finest("writing \"LOADING...\" " + xx + "," + yy + ": " + xTile + "," + yTile);
                g.drawString("LOADING...", xTile, yTile);
            }
        }
        for (xx = 0; xx < osmWidth; ++xx) {
            for (yy = 0; yy < osmHeight; ++yy) {
                int xTile = xx * OSM_TILE_SIZE;
                int yTile = yy * OSM_TILE_SIZE;
                logger.finest("loading tile " + xx + "," + yy + ": " + xTile + "," + yTile);
                ImageIcon icon = osmLoadTile(osmZoom, osmX + xx, osmY + yy);
                logger.finest("loaded tile " + xx + "," + yy + ": " + xTile + "," + yTile);
                g.drawImage(icon.getImage(), xTile, yTile, null);
            }
        }
    }
}
