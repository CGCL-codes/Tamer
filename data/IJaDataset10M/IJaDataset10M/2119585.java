package mobac.program.tilefilter;

import java.awt.Polygon;
import mobac.program.interfaces.MapSource;
import mobac.program.interfaces.TileFilter;
import mobac.program.model.MapPolygon;

public class PolygonTileFilter implements TileFilter {

    private final Polygon polygon;

    private final int tileSize;

    private final int polygonZoom;

    public PolygonTileFilter(MapPolygon map) {
        this(map.getPolygon(), map.getZoom(), map.getMapSource());
    }

    public PolygonTileFilter(Polygon polygon, int polygonZoom, MapSource mapSource) {
        super();
        this.polygon = polygon;
        this.polygonZoom = polygonZoom;
        this.tileSize = mapSource.getMapSpace().getTileSize();
    }

    public boolean testTile(int x, int y, int zoom, MapSource mapSource) {
        if (polygonZoom != zoom) throw new RuntimeException("Wrong zoom level!");
        int tileCoordinateX = x * tileSize;
        int tileCoordinateY = y * tileSize;
        return polygon.intersects(tileCoordinateX, tileCoordinateY, tileSize, tileSize);
    }
}
