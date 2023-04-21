package com.ht.board;

import java.util.ArrayList;
import java.util.Collection;
import com.ht.board.map.IMapViewFactory;
import com.ht.board.map.ITileViewFactory;
import com.ht.board.tile.TileView;
import com.ht.world.IMap;
import com.ht.world.ITile;

/**
 * TODO_DOCUMENT_ME
 * 
 * @since 1.0
 */
public class MapViewFactory implements IMapViewFactory {

    private ITileViewFactory _tileFactory;

    public MapView getMap(IMap map) {
        MapView mapView = new MapView(map);
        Collection<TileView> tiles = getTiles(map);
        return mapView;
    }

    private Collection<TileView> getTiles(IMap map) {
        Collection<TileView> tileViews = new ArrayList<TileView>(map.getSize());
        for (ITile tile : map.getTiles()) {
            tileViews.add(_tileFactory.getTile(tile));
        }
        return tileViews;
    }

    /**
    * @return the tileFactory
    */
    public ITileViewFactory getTileFactory() {
        return _tileFactory;
    }

    /**
    * @param tileFactory the tileFactory to set
    */
    public void setTileFactory(ITileViewFactory tileFactory) {
        _tileFactory = tileFactory;
    }
}
