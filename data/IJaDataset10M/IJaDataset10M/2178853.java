package com.moesol.gwt.maps.client;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Widget;
import com.moesol.gwt.maps.client.stats.Sample;

public class TileImageEngine {

    static class TileInfo {

        long m_lastUsedMillis;

        int m_x;

        int m_y;

        int m_level;

        Object m_image;

        boolean m_placed;

        boolean m_animated;

        double m_scale;

        boolean m_loaded;

        @Override
        public String toString() {
            return "l=" + m_level + ",x=" + m_x + ",y=" + m_y + ",placed=" + m_placed + ",loaded=" + m_loaded;
        }
    }

    public static final int MAX_CACHE_SIZE = 64;

    private final Map<String, List<TileInfo>> m_lookup = new HashMap<String, List<TileInfo>>();

    private final ArrayList<TileInfo> m_infoCache = new ArrayList<TileInfo>();

    private final TileImageEngineListener m_listener;

    private final TiledImageLayer m_imgLayer;

    public TileImageEngine(TiledImageLayer imgLayer, TileImageEngineListener l) {
        m_listener = l;
        m_imgLayer = imgLayer;
    }

    private void clearFlags() {
        for (int i = 0; i < m_infoCache.size(); i++) {
            TileInfo tileInfo = m_infoCache.get(i);
            tileInfo.m_placed = false;
            tileInfo.m_animated = false;
            tileInfo.m_scale = Double.NaN;
        }
    }

    public Object findImage(TileCoords tileCoords, double scale, boolean placed, boolean animated) {
        int iSize = m_infoCache.size();
        for (int i = 0; i < iSize; i++) {
            TileInfo tileInfo = (TileInfo) m_infoCache.get(i);
            if (isMatch(tileInfo, tileCoords)) {
                if (tileInfo.m_placed) {
                    continue;
                }
                if (tileInfo.m_scale == scale) {
                    continue;
                }
                m_listener.useImage(tileCoords, tileInfo.m_image);
                tileInfo.m_placed = placed;
                tileInfo.m_animated = animated;
                tileInfo.m_scale = scale;
                tileInfo.m_lastUsedMillis = System.currentTimeMillis();
                return tileInfo.m_image;
            }
        }
        return null;
    }

    public Object findOrCreateImage(TileCoords tileCoords) {
        Sample.TILE_IMG_ENGINE_FIND_OR_CREATE.beginSample();
        try {
            return _findOrCreateImage(tileCoords);
        } finally {
            Sample.TILE_IMG_ENGINE_FIND_OR_CREATE.endSample();
        }
    }

    private Object _findOrCreateImage(TileCoords tileCoords) {
        TileInfo tileInfo = lookupTileInfo(tileCoords);
        if (tileInfo != null) {
            useTileInfo(tileCoords, tileInfo);
            return tileInfo.m_image;
        }
        TileInfo result = makeTileInfo(tileCoords);
        addTileInfo(result);
        return result.m_image;
    }

    private void useTileInfo(TileCoords tileCoords, TileInfo tileInfo) {
        Sample.USE_IMAGE.beginSample();
        m_listener.useImage(tileCoords, tileInfo.m_image);
        Sample.USE_IMAGE.endSample();
        tileInfo.m_placed = true;
        tileInfo.m_lastUsedMillis = System.currentTimeMillis();
    }

    private TileInfo makeTileInfo(TileCoords tileCoords) {
        TileInfo result = new TileInfo();
        Sample.CREATE_IMAGE.beginSample();
        result.m_image = m_listener.createImage(tileCoords);
        Sample.CREATE_IMAGE.endSample();
        result.m_lastUsedMillis = System.currentTimeMillis();
        result.m_level = levelOrZero(tileCoords);
        result.m_loaded = false;
        result.m_placed = true;
        result.m_x = tileCoords.getX();
        result.m_y = tileCoords.getY();
        return result;
    }

    TileInfo lookupTileInfo(TileCoords tileCoords) {
        String key = buildKey(tileCoords);
        List<TileInfo> list = m_lookup.get(key);
        if (list == null) {
            return null;
        }
        for (TileInfo info : list) {
            if (!info.m_placed) {
                return info;
            }
        }
        return null;
    }

    void addTileInfo(TileInfo result) {
        String key = buildKey(result);
        List<TileInfo> list = m_lookup.get(key);
        if (list == null) {
            list = new ArrayList<TileInfo>();
            m_lookup.put(key, list);
        }
        list.add(result);
        m_infoCache.add(result);
    }

    void removeTileInfo(int idx) {
        TileInfo info = m_infoCache.remove(idx);
        String key = buildKey(info);
        List<TileInfo> list = m_lookup.get(key);
        if (list != null) {
            list.remove(info);
            if (list.isEmpty()) {
                m_lookup.remove(key);
            }
        }
    }

    private String buildKey(TileInfo result) {
        return result.m_level + "," + result.m_y + "," + result.m_x;
    }

    private String buildKey(TileCoords tileCoords) {
        return levelOrZero(tileCoords) + "," + tileCoords.getY() + "," + tileCoords.getX();
    }

    public void clear() {
        for (TileInfo info : m_infoCache) {
            m_listener.destroyImage(info.m_image);
        }
    }

    private int levelOrZero(TileCoords tileCoords) {
        int level = tileCoords.getLevel();
        return level < 0 ? 0 : level;
    }

    public void hideUnplacedImages() {
        doHideUnplacedImages();
        removeStaleEntries();
        clearFlags();
    }

    private void doHideUnplacedImages() {
        for (int i = 0; i < m_infoCache.size(); i++) {
            TileInfo tileInfo = m_infoCache.get(i);
            if (!tileInfo.m_placed && !tileInfo.m_animated) {
                m_listener.hideImage(tileInfo.m_image);
            }
        }
    }

    public void doHideAnimatedImages() {
        for (int i = 0; i < m_infoCache.size(); i++) {
            TileInfo tileInfo = m_infoCache.get(i);
            if (tileInfo.m_animated) {
                tileInfo.m_animated = false;
                m_listener.hideImage(tileInfo.m_image);
            }
        }
    }

    private void removeStaleEntries() {
        if (m_infoCache.size() <= MAX_CACHE_SIZE) {
            return;
        }
        Collections.sort(m_infoCache, new Comparator<TileInfo>() {

            @Override
            public int compare(TileInfo arg0, TileInfo arg1) {
                TileInfo i0 = arg0;
                TileInfo i1 = arg1;
                long r = i0.m_lastUsedMillis - i1.m_lastUsedMillis;
                return r == 0 ? 0 : (r < 0 ? -1 : 1);
            }
        });
        int removed = 0;
        int needToRemove = m_infoCache.size() - MAX_CACHE_SIZE;
        for (int i = 0; i < m_infoCache.size(); ) {
            if (removed >= needToRemove) {
                return;
            }
            TileInfo tileInfo = m_infoCache.get(i);
            if (!tileInfo.m_placed) {
                m_listener.destroyImage(tileInfo.m_image);
                removeTileInfo(i);
                removed++;
            } else {
                i++;
            }
        }
    }

    private boolean isMatch(TileInfo tileInfo, TileCoords tileCoords) {
        if (tileInfo.m_level != levelOrZero(tileCoords)) {
            return false;
        }
        if (tileInfo.m_x != tileCoords.getX()) {
            return false;
        }
        if (tileInfo.m_y != tileCoords.getY()) {
            return false;
        }
        return true;
    }

    public void markLoaded(Widget sender) {
        boolean bUnmatched = true;
        for (int i = 0; i < m_infoCache.size(); i++) {
            TileInfo tileInfo = m_infoCache.get(i);
            if (tileInfo.m_image == sender) {
                tileInfo.m_loaded = true;
                bUnmatched = false;
                break;
            }
        }
        if (bUnmatched) {
            System.out.println("unmatched?: " + sender);
        }
        for (int i = 0; i < m_infoCache.size(); i++) {
            TileInfo tileInfo = m_infoCache.get(i);
            if (tileInfo.m_loaded == false) {
                return;
            }
        }
        m_imgLayer.hideAnimatedTiles();
    }

    public Object firstUnloadedImage() {
        for (int i = 0; i < m_infoCache.size(); i++) {
            TileInfo tileInfo = m_infoCache.get(i);
            if (tileInfo.m_loaded == false) {
                return tileInfo.m_image;
            }
        }
        return null;
    }

    public void setAllZIndex(int zindex) {
        for (int i = 0; i < m_infoCache.size(); i++) {
            TileInfo tileInfo = m_infoCache.get(i);
            Image img = (Image) tileInfo.m_image;
            img.getElement().getStyle().setProperty("zIndex", zindex + "");
        }
    }
}
