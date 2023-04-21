package mobac.mapsources.mappacks.region_america_north;

import java.io.IOException;
import java.net.HttpURLConnection;
import mobac.mapsources.AbstractHttpMapSource;
import mobac.program.interfaces.HttpMapSource;
import mobac.program.model.TileImageType;

/**
 * 
 * http://www.mytopo.com/maps/
 * 
 * Funny: The URL indicates PNG images but the server provides JPEG files...
 * 
 */
public class MyTopo extends AbstractHttpMapSource {

    private static String BASE_URL = "http://maps.mytopo.com/mytopob04g82H/tilecache.py/1.0.0/topoG/";

    public MyTopo() {
        super("MyTopo", 6, 16, TileImageType.JPG, HttpMapSource.TileUpdate.None);
    }

    @Override
    public String getTileUrl(int zoom, int tilex, int tiley) {
        return BASE_URL + zoom + "/" + tilex + "/" + tiley + ".jpg";
    }

    @Override
    public HttpURLConnection getTileUrlConnection(int zoom, int tilex, int tiley) throws IOException {
        HttpURLConnection conn = super.getTileUrlConnection(zoom, tilex, tiley);
        conn.addRequestProperty("Referer", "http://www.mytopo.com/maps/");
        return conn;
    }

    @Override
    public String toString() {
        return "MyTopo (USA only)";
    }
}
