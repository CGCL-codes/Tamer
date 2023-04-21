package mobac.mapsources.mappacks.region_europe_east;

import mobac.mapsources.AbstractHttpMapSource;
import mobac.program.interfaces.MapSourceTextAttribution;
import mobac.program.model.TileImageType;

/**
 * http://osm.trail.pl/ol.xhtml
 * http://sourceforge.net/tracker/?func=detail&aid=3379692&group_id=238075&atid=1105497
 */
public class OSMapaTopoContours extends AbstractHttpMapSource implements MapSourceTextAttribution {

    public OSMapaTopoContours() {
        super("OSMapaTopoContours", 0, 18, TileImageType.PNG, TileUpdate.IfNoneMatch);
    }

    public String getTileUrl(int zoom, int x, int y) {
        return "http://osm.trail.pl/" + zoom + "/" + x + "/" + y + ".png";
    }

    @Override
    public String toString() {
        return "OSMapa-Topo with contours (PL)";
    }

    public String getAttributionText() {
        return "© Data OpenStreetMap, Hosting TRAIL.PL and centuria.pl";
    }

    public String getAttributionLinkURL() {
        return null;
    }
}
