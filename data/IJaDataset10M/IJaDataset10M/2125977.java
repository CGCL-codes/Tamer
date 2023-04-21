package bs;

import bs.earth.EarthBean;
import bs.earth.EarthHandler;
import bs.earth.google.GoogleEarthHandler;
import bs.earth.virtual.VirtualEarthHandler;
import bs.util.FileCopy;
import bs.util.ImageUtil;
import bs.util.MapUtil;
import java.awt.image.BufferedImage;
import java.io.File;
import bs.earth.EarthUtil.DataSet;
import bs.earth.EarthUtil.MapSource;
import bs.earth.EarthUtil.Zoom;
import java.io.FileWriter;
import java.io.FilenameFilter;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Iterator;

/*************************************************************************/
public class Main {

    private static final DecimalFormat df6 = new DecimalFormat("000000");

    public Main() {
    }

    public static void getImages(double minLon, double maxLon, double minLat, double maxLat, int numGroupedImgs, int colorType, DataSet dataSet, Zoom zoom, MapSource mapSource, String mapName) {
        try {
            EarthHandler eh = mapSource == MapSource.GOOGLEEARTH ? new GoogleEarthHandler(dataSet, zoom) : new VirtualEarthHandler(dataSet, zoom);
            double lon = minLon;
            double lat = minLat;
            double group_lon_start = 0;
            double group_lat_start = 0;
            double nextLon = 0;
            double nextLat = 0;
            double nextWest = -Double.MAX_VALUE;
            double nextNorth = +Double.MAX_VALUE;
            int counter = 0;
            ArrayList imagesRange = null;
            EarthBean eb = null;
            while (nextNorth > maxLat) {
                while (nextWest < maxLon) {
                    int currGroupImg = 0;
                    double group_west = +Double.MAX_VALUE;
                    double group_north = -Double.MAX_VALUE;
                    double group_east = -Double.MAX_VALUE;
                    double group_south = +Double.MAX_VALUE;
                    group_lon_start = lon;
                    group_lat_start = lat;
                    imagesRange = new ArrayList();
                    for (int group_y = 0; group_y < numGroupedImgs; group_y++) {
                        for (int group_x = 0; group_x < numGroupedImgs; group_x++) {
                            String imageFileName = "group_x" + group_x + "_y" + group_y + "." + eh.getImageExtension();
                            eb = eh.getTileInfo(lon, lat);
                            nextWest = eb.getTileCoordinates().getMaxX();
                            nextNorth = eb.getTileCoordinates().getMaxY();
                            group_west = group_west > eb.getTileCoordinates().getMinX() ? eb.getTileCoordinates().getMinX() : group_west;
                            group_north = group_north < eb.getTileCoordinates().getMinY() ? eb.getTileCoordinates().getMinY() : group_north;
                            group_east = group_east < eb.getTileCoordinates().getMaxX() ? eb.getTileCoordinates().getMaxX() : group_east;
                            group_south = group_south > eb.getTileCoordinates().getMaxY() ? eb.getTileCoordinates().getMaxY() : group_south;
                            Iterator imagesI = eb.getImages().entrySet().iterator();
                            while (imagesI.hasNext()) {
                                System.out.println("Img urls         : " + imagesI.next());
                            }
                            System.out.println("Position on image: [" + eb.getCoordinatePosition().getX() + "," + eb.getCoordinatePosition().getY() + "]");
                            System.out.println("Tile bounds      : [(" + eb.getTileCoordinates().getX() + "," + eb.getTileCoordinates().getY() + ") (" + eb.getTileCoordinates().getMaxX() + "," + eb.getTileCoordinates().getMaxY() + ")]");
                            System.out.println("Next lon-lat     : [" + eb.getNextCoordinate().getX() + "," + eb.getNextCoordinate().getY() + "]");
                            ImageUtil.download(eb.getImages(), imageFileName);
                            System.out.println("Image written to: " + imageFileName);
                            imagesRange.add(imageFileName);
                            currGroupImg++;
                            nextLon = eb.getNextCoordinate().getX();
                            nextLat = eb.getNextCoordinate().getY();
                            lon = nextLon;
                            System.out.println("-LONGITUDE MOVE- (lon=" + lon + ")");
                            System.out.println("eb.getTileCoordinates().getMaxX()=" + eb.getTileCoordinates().getMaxX());
                            if (eb.getTileCoordinates().getMaxX() > maxLon) {
                                group_x = numGroupedImgs;
                                break;
                            }
                        }
                        System.out.println("eb.getTileCoordinates().getMaxY()=" + eb.getTileCoordinates().getMaxY());
                        if (eb.getTileCoordinates().getMaxY() < maxLat) {
                            group_y = numGroupedImgs;
                            break;
                        }
                        lon = group_lon_start;
                        lat = nextLat;
                        System.out.println("-----LATITUDE MOVE----- (lat=" + lat + ")");
                    }
                    if (imagesRange.size() > 0) {
                        String fileName = mapName + "_" + eh.getZoom() + "_" + df6.format(++counter);
                        ImageUtil.merge((String[]) imagesRange.toArray(new String[0]), new File(fileName + ".png"), colorType);
                        String lastFileName = (String) imagesRange.get(imagesRange.size() - 1);
                        int x = Integer.parseInt(lastFileName.substring(lastFileName.indexOf("_x") + 2, lastFileName.indexOf("_y"))) + 1;
                        int y = Integer.parseInt(lastFileName.substring(lastFileName.indexOf("_y") + 2, lastFileName.indexOf("."))) + 1;
                        int width = x * 256;
                        int height = y * 256;
                        MapUtil.createCalibrationFile(new File(fileName + ".map"), group_east, group_west, group_north, group_south, width, height);
                    } else {
                        System.out.println("No images needed on this scan...");
                    }
                    lon = nextLon;
                    lat = group_lat_start;
                    group_west = +Double.MAX_VALUE;
                    group_north = -Double.MAX_VALUE;
                    group_east = -Double.MAX_VALUE;
                    group_south = +Double.MAX_VALUE;
                }
                nextWest = minLon;
                lon = nextWest;
                lat = nextLat;
                System.out.println("-----LATITUDE MOVE----- (lat=" + lat + ")");
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public static void prepareForTrekBuddy(final String mapName, final String zoom, int tileSize, int colorType) throws Exception {
        String pngFiles[] = new File("./").list(new FilenameFilter() {

            public boolean accept(File dir, String name) {
                if (name.endsWith(".png") && name.startsWith(mapName + "_" + zoom)) return true; else return false;
            }
        });
        String rootMapDir = "maps/" + mapName + "_" + zoom;
        new File(rootMapDir).mkdirs();
        FileWriter fwAtlas = new FileWriter(new File("maps/cr.tba"));
        fwAtlas.write("Atlas 1.0\r\n");
        fwAtlas.close();
        String rootOziDir = "ozi";
        new File(rootOziDir).mkdirs();
        for (int i = 0; i < pngFiles.length; i++) {
            String fileName = pngFiles[i].substring(0, pngFiles[i].length() - 4);
            System.out.println("Processing " + pngFiles[i] + " ...");
            File fSet = new File(rootMapDir + "/" + fileName + "/set");
            File fMap = new File(fileName + ".map");
            File fPng = new File(fileName + ".png");
            fSet.mkdirs();
            FileCopy.copy(fMap, new File(rootMapDir + "/" + fileName, fileName + ".map"));
            ImageUtil.splitImage(fPng, fSet, tileSize, colorType);
            String pngTiles[] = fSet.list(new FilenameFilter() {

                public boolean accept(File dir, String name) {
                    if (name.endsWith(".png") && name.startsWith(mapName + "_" + zoom)) return true; else return false;
                }
            });
            FileWriter fwSet = new FileWriter(new File(rootMapDir + "/" + fileName, fileName + ".set"));
            for (int j = 0; j < pngTiles.length; j++) {
                fwSet.write(pngTiles[j] + "\r\n");
            }
            fwSet.close();
            fMap.renameTo(new File(rootOziDir, fileName + ".map"));
            fPng.renameTo(new File(rootOziDir, fileName + ".png"));
        }
    }

    public static void main(String[] args) throws Exception {
        double minLon = -122.484442;
        double maxLon = -122.472633;
        double minLat = +37.827652;
        double maxLat = +37.823471;
        int numGroupedImgs = 20;
        int colorType = BufferedImage.TYPE_BYTE_INDEXED;
        DataSet dataSet = DataSet.ROAD;
        Zoom zoom = Zoom.ZOOM_17;
        MapSource mapSource = MapSource.GOOGLEEARTH;
        String mapName = "sf_goldengate";
        int tileSize = 512;
        Main m = new Main();
        m.getImages(minLon, maxLon, minLat, maxLat, numGroupedImgs, colorType, dataSet, zoom, mapSource, mapName);
        m.prepareForTrekBuddy(mapName, zoom.toString(), tileSize, colorType);
    }
}
