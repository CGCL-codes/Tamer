package com.luzan.app.map.bean;

import org.apache.log4j.Logger;
import com.luzan.app.map.bean.ExtMapTile;
import com.luzan.common.geomap.Tile;

/**
 * NASAMapTile
 *
 * @author Alexander Bondar
 */
public class NASAMapTile extends ExtMapTile {

    private static final Logger logger = Logger.getLogger(NASAMapTile.class);

    public NASAMapTile(int x, int y, int z) {
        super("http://onearth.jpl.nasa.gov/wms.cgi?request=GetMap&layers=BMNG&srs=EPSG:4326&format=image/jpeg&styles=Jul&width=260&height=260&bbox=" + Tile.getBBOX(x, y, z), x, y, z);
        setWidth(260);
        setHeight(260);
    }

    public String toKeyString() {
        return x + "_" + y + "_" + z;
    }
}
