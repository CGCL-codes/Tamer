package CB_Core.GL_UI.Views;

import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicInteger;
import CB_Core.Config;
import CB_Core.GlobalCore;
import CB_Core.DB.Database;
import CB_Core.Enums.CacheTypes;
import CB_Core.GL_UI.SpriteCache;
import CB_Core.Log.Logger;
import CB_Core.Map.Descriptor;
import CB_Core.Types.Cache;
import CB_Core.Types.MysterySolution;
import CB_Core.Types.Waypoint;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;

public class MapViewCacheList {

    private int maxZoomLevel;

    private queueProcessor queueProcessor = null;

    private AtomicInteger state = new AtomicInteger(0);

    private Vector2 point1;

    private Vector2 point2;

    private int zoom = 15;

    public ArrayList<WaypointRenderInfo> list = new ArrayList<MapViewCacheList.WaypointRenderInfo>();

    public ArrayList<WaypointRenderInfo> tmplist;

    public int anz = 0;

    private boolean hideMyFinds = false;

    /**
	 * true, falls bei Mysterys mit L�sung (Final Waypoint) der Cache ausgeblendet werden soll, wenn der Cache nicht selected ist.
	 */
    boolean hideCacheWithFinal = true;

    public MapViewCacheList(int maxZoomLevel) {
        super();
        this.maxZoomLevel = maxZoomLevel;
        if (queueProcessor == null) {
            try {
                queueProcessor = new queueProcessor();
                queueProcessor.setPriority(Thread.MIN_PRIORITY);
            } catch (Exception ex) {
                String s = ex.getMessage();
            }
            queueProcessor.start();
        }
        hideMyFinds = Config.settings.MapHideMyFinds.getValue();
    }

    private class queueProcessor extends Thread {

        @Override
        public void run() {
            boolean queueEmpty = false;
            try {
                do {
                    if (state.compareAndSet(1, 2)) {
                        int iconSize = 0;
                        if ((zoom >= 13) && (zoom <= 14)) iconSize = 1; else if (zoom > 14) iconSize = 2;
                        tmplist = new ArrayList<MapViewCacheList.WaypointRenderInfo>();
                        if (GlobalCore.SelectedCache() != null) {
                            if (!(hideMyFinds && GlobalCore.SelectedCache().Found)) {
                                ArrayList<Waypoint> wps = GlobalCore.SelectedCache().waypoints;
                                for (Waypoint wp : wps) {
                                    WaypointRenderInfo wpi = new WaypointRenderInfo();
                                    double MapX = 256.0 * Descriptor.LongitudeToTileX(maxZoomLevel, wp.Pos.Longitude);
                                    double MapY = -256.0 * Descriptor.LatitudeToTileY(maxZoomLevel, wp.Pos.Latitude);
                                    wpi.MapX = (float) MapX;
                                    wpi.MapY = (float) MapY;
                                    wpi.Icon = SpriteCache.MapIcons.get((int) wp.Type.ordinal());
                                    wpi.Cache = GlobalCore.SelectedCache();
                                    wpi.Waypoint = wp;
                                    wpi.Selected = (GlobalCore.SelectedWaypoint() == wp);
                                    wpi.UnderlayIcon = getUnderlayIcon(wpi.Cache, wpi.Waypoint);
                                    tmplist.add(wpi);
                                }
                            }
                        }
                        for (Cache cache : Database.Data.Query) {
                            if (hideMyFinds && cache.Found) continue;
                            double MapX = 256.0 * Descriptor.LongitudeToTileX(maxZoomLevel, cache.Longitude());
                            double MapY = -256.0 * Descriptor.LatitudeToTileY(maxZoomLevel, cache.Latitude());
                            boolean show = false;
                            if ((MapX >= point1.x) && (MapX < point2.x) && (Math.abs(MapY) > Math.abs(point1.y)) && (Math.abs(MapY) < Math.abs(point2.y))) show = true;
                            if (cache == GlobalCore.SelectedCache()) show = true;
                            if ((hideCacheWithFinal) && (cache.Type == CacheTypes.Mystery) && cache.MysterySolved() && cache.HasFinalWaypoint()) {
                                if (cache != GlobalCore.SelectedCache()) show = false;
                            }
                            if (show) {
                                {
                                    WaypointRenderInfo wpi = new WaypointRenderInfo();
                                    wpi.MapX = (float) MapX;
                                    wpi.MapY = (float) MapY;
                                    wpi.Icon = SpriteCache.MapIcons.get(cache.GetMapIconId());
                                    wpi.UnderlayIcon = getUnderlayIcon(cache, wpi.Waypoint);
                                    if (cache.Archived || !cache.Available) wpi.OverlayIcon = SpriteCache.MapOverlay.get(2);
                                    if ((iconSize < 1) && (cache != GlobalCore.SelectedCache())) {
                                        int iconId = 0;
                                        switch(cache.Type) {
                                            case Traditional:
                                                iconId = 0;
                                                break;
                                            case Letterbox:
                                                iconId = 0;
                                                break;
                                            case Multi:
                                                iconId = 1;
                                                break;
                                            case Event:
                                                iconId = 2;
                                                break;
                                            case MegaEvent:
                                                iconId = 2;
                                                break;
                                            case Virtual:
                                                iconId = 3;
                                                break;
                                            case Camera:
                                                iconId = 3;
                                                break;
                                            case Earth:
                                                iconId = 3;
                                                break;
                                            case Mystery:
                                                {
                                                    if (cache.HasFinalWaypoint()) iconId = 5; else iconId = 4;
                                                    break;
                                                }
                                            case Wherigo:
                                                iconId = 4;
                                                break;
                                        }
                                        if (cache.Found) iconId = 6;
                                        if (cache.ImTheOwner()) iconId = 7;
                                        if (cache.Archived || !cache.Available) iconId += 8;
                                        wpi.Icon = SpriteCache.MapIconsSmall.get(iconId);
                                        wpi.UnderlayIcon = null;
                                    }
                                    wpi.Cache = cache;
                                    wpi.Waypoint = null;
                                    wpi.Selected = (GlobalCore.SelectedCache() == cache);
                                    {
                                        tmplist.add(wpi);
                                    }
                                }
                            }
                        }
                        for (MysterySolution solution : Database.Data.Query.MysterySolutions) {
                            if ((zoom < 14) && (solution.Cache.Type != CacheTypes.Mystery)) continue;
                            if (GlobalCore.SelectedCache() == solution.Cache) continue;
                            if (hideMyFinds && solution.Cache.Found) continue;
                            double mapX = 256.0 * Descriptor.LongitudeToTileX(maxZoomLevel, solution.Longitude);
                            double mapY = -256.0 * Descriptor.LatitudeToTileY(maxZoomLevel, solution.Latitude);
                            boolean show = false;
                            if ((mapX >= point1.x) && (mapX < point2.x) && (Math.abs(mapY) > Math.abs(point1.y)) && (Math.abs(mapY) < Math.abs(point2.y))) show = true;
                            if (solution.Cache != GlobalCore.SelectedCache()) show = true;
                            if (!show) continue;
                            WaypointRenderInfo wpiF = new WaypointRenderInfo();
                            wpiF.MapX = (float) mapX;
                            wpiF.MapY = (float) mapY;
                            if (iconSize == 2) {
                                wpiF.Icon = (solution.Cache.Type == CacheTypes.Mystery) ? SpriteCache.MapIcons.get(21) : SpriteCache.MapIcons.get(18);
                                wpiF.UnderlayIcon = getUnderlayIcon(solution.Cache, solution.Waypoint);
                                if ((hideCacheWithFinal) && (solution.Cache.Type == CacheTypes.Mystery) && solution.Cache.MysterySolved() && solution.Cache.HasFinalWaypoint()) {
                                    if (GlobalCore.SelectedCache() != solution.Cache) {
                                        if (solution.Cache.Found) wpiF.Icon = SpriteCache.MapIcons.get(19);
                                        if (solution.Cache.ImTheOwner()) wpiF.Icon = SpriteCache.MapIcons.get(20);
                                    } else {
                                        wpiF.Icon = SpriteCache.MapIcons.get((int) solution.Waypoint.Type.ordinal());
                                    }
                                }
                            } else {
                                int iconId = 0;
                                switch(solution.Cache.Type) {
                                    case Traditional:
                                        iconId = 0;
                                        break;
                                    case Letterbox:
                                        iconId = 0;
                                        break;
                                    case Multi:
                                        iconId = 1;
                                        break;
                                    case Event:
                                        iconId = 2;
                                        break;
                                    case MegaEvent:
                                        iconId = 2;
                                        break;
                                    case Virtual:
                                        iconId = 3;
                                        break;
                                    case Camera:
                                        iconId = 3;
                                        break;
                                    case Earth:
                                        iconId = 3;
                                        break;
                                    case Mystery:
                                        {
                                            if (solution.Cache.HasFinalWaypoint()) iconId = 5; else iconId = 4;
                                            break;
                                        }
                                    case Wherigo:
                                        iconId = 4;
                                        break;
                                }
                                if (solution.Cache.Found) iconId = 6;
                                if (solution.Cache.ImTheOwner()) iconId = 7;
                                if (solution.Cache.Archived || !solution.Cache.Available) iconId += 8;
                                wpiF.Icon = SpriteCache.MapIconsSmall.get(iconId);
                                wpiF.OverlayIcon = null;
                            }
                            wpiF.Cache = solution.Cache;
                            wpiF.Waypoint = solution.Waypoint;
                            wpiF.Selected = (GlobalCore.SelectedWaypoint() == solution.Waypoint);
                            tmplist.add(wpiF);
                        }
                        synchronized (list) {
                            list.clear();
                            list = tmplist;
                            tmplist = null;
                        }
                        Thread.sleep(400);
                        state.set(0);
                        anz++;
                    } else {
                        Thread.sleep(400);
                    }
                } while (true);
            } catch (Exception ex3) {
                Logger.Error("MapCacheList.queueProcessor.doInBackground()", "3", ex3);
            } finally {
            }
            return;
        }
    }

    private Sprite getUnderlayIcon(Cache cache, Waypoint waypoint) {
        if (waypoint == null) {
            if ((cache == null) || (cache == GlobalCore.SelectedCache())) {
                return SpriteCache.MapOverlay.get(1);
            } else {
                return SpriteCache.MapOverlay.get(0);
            }
        } else {
            if (waypoint == GlobalCore.SelectedWaypoint()) {
                return SpriteCache.MapOverlay.get(1);
            } else {
                return SpriteCache.MapOverlay.get(0);
            }
        }
    }

    private Vector2 lastPoint1;

    private Vector2 lastPoint2;

    private int lastzoom;

    public void update(Vector2 point1, Vector2 point2, int zoom, boolean doNotCheck) {
        if (state.get() != 0) return;
        if ((zoom == lastzoom) && (!doNotCheck)) {
            if ((point1.x >= lastPoint1.x) && (point2.x <= lastPoint2.x) && (point1.y >= lastPoint1.y) && (point2.y <= lastPoint2.y)) return;
        }
        Vector2 size = new Vector2(point2.x - point1.x, point2.y - point1.y);
        point1.x -= size.x;
        point2.x += size.x;
        point1.y -= size.y;
        point2.y += size.y;
        this.lastzoom = zoom;
        lastPoint1 = point1;
        lastPoint2 = point2;
        this.zoom = zoom;
        this.point1 = point1;
        this.point2 = point2;
        state.set(1);
    }

    public boolean hasNewResult() {
        return state.get() == 3;
    }

    public static class WaypointRenderInfo {

        public float MapX;

        public float MapY;

        public Cache Cache;

        public Waypoint Waypoint;

        public boolean Selected;

        public Sprite Icon;

        public Sprite UnderlayIcon;

        public Sprite OverlayIcon;
    }

    ;
}
