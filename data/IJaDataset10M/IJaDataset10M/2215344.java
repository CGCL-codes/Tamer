package mobac.mapsources.mappacks.openstreetmap;

public class Osm4uMaps extends AbstractOsmMapSource {

    private static String SERVER = "http://176.28.41.237/";

    public Osm4uMaps() {
        super("4uMaps");
        minZoom = 2;
        maxZoom = 15;
        tileUpdate = TileUpdate.IfNoneMatch;
    }

    @Override
    public String getTileUrl(int zoom, int tilex, int tiley) {
        return SERVER + super.getTileUrl(zoom, tilex, tiley);
    }

    @Override
    public String toString() {
        return "OpenStreetMap 4umaps.eu (Europe)";
    }
}
