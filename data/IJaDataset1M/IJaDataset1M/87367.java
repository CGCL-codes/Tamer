package com.luzan.app.map.processor;

import com.luzan.app.map.service.bean.PublishRequest;
import com.luzan.app.map.service.bean.MapTrackResponse;
import com.luzan.app.map.service.bean.MapRequest;
import com.luzan.app.map.bean.MapTile;
import com.luzan.app.map.bean.MapFilter;
import com.luzan.app.map.bean.publik.*;
import com.luzan.app.map.tool.MapTileAssembler;
import com.luzan.app.map.utils.Configuration;
import com.luzan.common.processor.ProcessorTask;
import com.luzan.common.processor.ProcessorContext;
import com.luzan.common.processor.ProcessorException;
import com.luzan.common.geomap.LatLngRectangle;
import com.luzan.common.geomap.Tile;
import com.luzan.common.geomap.LatLngPoint;
import com.luzan.db.dao.*;
import com.luzan.db.hibernate.dao.PublicMapTrackHibernateDAO;
import com.luzan.db.hibernate.dao.PublicMapOriginalHibernateDAO;
import com.luzan.db.hibernate.dao.PublicMapPointHibernateDAO;
import com.luzan.db.TransactionManager;
import com.sun.xfile.XFile;
import com.sun.xfile.XFileOutputStream;
import org.apache.log4j.Logger;
import org.apache.commons.lang.StringUtils;
import org.hibernate.criterion.Expression;
import java.util.List;
import java.util.ArrayList;
import java.util.Collection;
import java.io.File;

public class MapPostThumbnailTask implements ProcessorTask<PublishRequest> {

    private static final Logger logger = Logger.getLogger(MapPostThumbnailTask.class);

    public boolean preProcess(ProcessorContext<PublishRequest> context) throws InterruptedException, ProcessorException {
        logger.info("MapPostThumbnailTask:preProcess");
        return true;
    }

    public byte process(ProcessorContext<PublishRequest> context) throws InterruptedException, ProcessorException {
        logger.info("MapPostThumbnailTask:process");
        final PublishRequest req = context.getItem().getEntity();
        final PublicMapOriginalDAO mapDao = new PublicMapOriginalHibernateDAO(PublicMapOriginal.class);
        final GenericDAO<PublicMapPost> postDao = DAOFactory.createDAO(PublicMapPost.class);
        final PublicMapTrackDAO trackDAO = new PublicMapTrackHibernateDAO(PublicMapTrack.class);
        final PublicMapPointDAO pointDAO = new PublicMapPointHibernateDAO(PublicMapPoint.class);
        PublicMapPost post;
        int z;
        try {
            TransactionManager.beginTransaction();
        } catch (Throwable t) {
            logger.error(t);
            throw new ProcessorException(t);
        }
        try {
            post = postDao.findUniqueByCriteria(Expression.eq("guid", req.getPostGuid()));
            final LatLngRectangle bounds = new LatLngRectangle(new LatLngPoint(post.getSWLat(), post.getSWLon()), new LatLngPoint(post.getNELat(), post.getNELon()));
            z = Tile.getOptimalZoom(bounds, 768);
            if (!StringUtils.isEmpty(req.getFrontMap())) {
                PublicMapOriginal mapOriginalFront = mapDao.findUniqueByCriteria(Expression.eq("guid", req.getFrontMap()));
                z = Math.max(z, mapOriginalFront.getZoomMin().intValue());
            }
            final Tile tileStart = new Tile(bounds.getSouthWest().getLat(), bounds.getSouthWest().getLng(), z);
            final Tile tileEnd = new Tile(bounds.getNorthEast().getLat(), bounds.getNorthEast().getLng(), z);
            List<MapTile> backTiles = new ArrayList<MapTile>();
            final XFile dirPub = new XFile(Configuration.getInstance().getPublicMapStorage().toString());
            if (!StringUtils.isEmpty(req.getFrontMap())) {
                for (double y = tileEnd.getTileCoord().getY(); y <= tileStart.getTileCoord().getY(); y++) for (double x = tileStart.getTileCoord().getX(); x <= tileEnd.getTileCoord().getX(); x++) {
                    final MapTile tile = MapTile.create(req.getFrontMap(), (int) x, (int) y, z);
                    final XFile file = new XFile(dirPub, tile.getPath());
                    if (file.exists() && file.isFile() && file.canRead()) backTiles.add(tile);
                }
            }
            if (backTiles.size() > 0) {
                MapRequest mapReq = new MapRequest();
                MapFilter filter = new MapFilter();
                mapReq.setSWLat(post.getSWLat());
                mapReq.setSWLon(post.getSWLon());
                mapReq.setNELat(post.getNELat());
                mapReq.setNELon(post.getNELon());
                mapReq.setFilter(filter);
                filter.setZoom(z);
                filter.setPostGuid(post.getGuid());
                Collection<PublicMapPoint> points = pointDAO.findPoints(mapReq);
                List<PublicMapTrack> tracks = post.getTracks();
                List<MapTrackResponse<PublicMapTrackPoint>> tracksRes = new ArrayList<MapTrackResponse<PublicMapTrackPoint>>();
                for (PublicMapTrack track : tracks) {
                    final List<PublicMapTrackPoint> trackPoints = trackDAO.getTrackPoints(post, z, track.getId());
                    tracksRes.add(new MapTrackResponse<PublicMapTrackPoint>(track, trackPoints));
                }
                final File wayPointIcon = new File("icon_print.png");
                final XFile storage = new XFile(new XFile(Configuration.getInstance().getPublicMapStorage().toString()), post.getGuid());
                storage.mkdir();
                final XFile tmpFileS = new XFile(storage, "thumbnail-256");
                MapTileAssembler mapAssembler = new MapTileAssembler(dirPub, backTiles, 256);
                final XFileOutputStream outS = new XFileOutputStream(tmpFileS);
                mapAssembler.assemble(points, tracksRes, wayPointIcon, 256, outS);
                outS.flush();
                outS.close();
                mapAssembler.close();
                final XFile tmpFileM = new XFile(storage, "thumbnail-512");
                mapAssembler = new MapTileAssembler(dirPub, backTiles, 256);
                final XFileOutputStream outM = new XFileOutputStream(tmpFileM);
                mapAssembler.assemble(points, tracksRes, wayPointIcon, 512, outM);
                outM.flush();
                outM.close();
                mapAssembler.close();
                final XFile tmpFileL = new XFile(storage, "thumbnail-768");
                mapAssembler = new MapTileAssembler(dirPub, backTiles, 256);
                final XFileOutputStream outL = new XFileOutputStream(tmpFileL);
                mapAssembler.assemble(points, tracksRes, wayPointIcon, 768, outL);
                outL.flush();
                outL.close();
                mapAssembler.close();
                post.setThmbSmall(tmpFileS.getPath());
                post.setThmbMedium(tmpFileM.getPath());
                post.setThmbLarge(tmpFileL.getPath());
                postDao.update(post);
            }
            context.put("post", post);
            TransactionManager.commitTransaction();
        } catch (Throwable e) {
            TransactionManager.rollbackTransaction();
            logger.error("Error", e);
            throw new ProcessorException(e);
        }
        return TaskState.STATE_MO_POST_THUMBNAIL;
    }

    public void postProcess(ProcessorContext<PublishRequest> context) throws InterruptedException, ProcessorException {
        logger.info("MapPostThumbnailTask:postProcess");
    }
}
