package mobac.mapsources.mappacks.openstreetmap;

import java.io.IOException;
import java.net.HttpURLConnection;
import mobac.exceptions.DownloadFailedException;
import mobac.exceptions.StopAllDownloadsException;
import mobac.exceptions.TileException;
import mobac.mapsources.AbstractMultiLayerMapSource;
import mobac.program.interfaces.HttpMapSource;
import mobac.program.interfaces.MapSource;
import mobac.program.interfaces.MapSourceTextAttribution;
import mobac.program.model.TileImageType;

public class OpenPisteMap extends AbstractMultiLayerMapSource implements MapSourceTextAttribution {

    private static final String BASE = "http://tiles.openpistemap.org/nocontours";

    private static final String LAMDSHED = "http://tiles2.openpistemap.org/landshaded";

    public OpenPisteMap() {
        super("OpenPisteMapBCL", TileImageType.PNG);
        mapSources = new MapSource[] { new Mapnik(), new OpenPisteMapBase(), new OpenPisteMapLandshed() };
        initializeValues();
    }

    @Override
    public String toString() {
        return "Open Piste Map";
    }

    public String getAttributionText() {
        return "© OpenStreetMap contributors, CC-BY-SA";
    }

    public String getAttributionLinkURL() {
        return "http://openstreetmap.org";
    }

    public abstract static class AbstractOpenPisteMap extends AbstractOsmMapSource {

        public AbstractOpenPisteMap(String name) {
            super(name);
        }

        @Override
        public byte[] getTileData(int zoom, int x, int y, LoadMethod loadMethod) throws IOException, TileException, InterruptedException {
            try {
                return super.getTileData(zoom, x, y, loadMethod);
            } catch (DownloadFailedException e) {
                if (e.getHttpResponseCode() == HttpURLConnection.HTTP_FORBIDDEN) {
                    throw new StopAllDownloadsException("Server blocks mass download - aborting map dowload", e);
                } else throw e;
            }
        }
    }

    public static class OpenPisteMapBase extends AbstractOpenPisteMap {

        public OpenPisteMapBase() {
            super("OpenPisteMap");
            maxZoom = 17;
            tileUpdate = HttpMapSource.TileUpdate.LastModified;
        }

        @Override
        public String toString() {
            return "Open Piste Contours Layer";
        }

        public String getTileUrl(int zoom, int tilex, int tiley) {
            return BASE + super.getTileUrl(zoom, tilex, tiley);
        }
    }

    public static class OpenPisteMapLandshed extends AbstractOpenPisteMap {

        public OpenPisteMapLandshed() {
            super("OpenPisteMapLandshed");
            maxZoom = 17;
            tileUpdate = HttpMapSource.TileUpdate.LastModified;
        }

        @Override
        public String toString() {
            return "Open Piste Landshed Layer";
        }

        public String getTileUrl(int zoom, int tilex, int tiley) {
            return LAMDSHED + super.getTileUrl(zoom, tilex, tiley);
        }
    }
}
