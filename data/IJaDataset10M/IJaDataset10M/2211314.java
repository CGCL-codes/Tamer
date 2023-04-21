package com.luzan.app.map.service.bean;

import com.luzan.app.map.bean.user.UserMapPoint;
import com.luzan.app.map.bean.user.UserMapTrack;
import com.luzan.app.map.bean.user.UserMapTrackPoint;
import com.luzan.app.map.bean.user.UserMapOriginal;
import com.luzan.app.map.bean.MapPoint;
import com.luzan.app.map.bean.publik.PublicMapResource;
import com.luzan.app.map.manager.MapManager;
import com.luzan.app.map.utils.TagUtil;
import com.luzan.parser.map.bean.ozi.*;
import com.luzan.parser.map.bean.gmap.*;
import com.luzan.parser.util.ParserUtil;
import com.luzan.bean.User;
import com.luzan.db.dao.GenericDAO;
import com.luzan.db.dao.DAOFactory;
import com.luzan.db.dao.DAOException;
import java.util.List;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Set;
import java.lang.reflect.InvocationTargetException;
import org.hibernate.criterion.Expression;

public class BeanFactory {

    public static UserMapPoint createUserPoint(WayPointRow row, User user) throws DAOException {
        return createUserPoint(row, user, null);
    }

    public static UserMapPoint createUserPoint(WayPointRow row, User user, String tags) throws DAOException {
        GenericDAO<UserMapPoint> dao = DAOFactory.createDAO(UserMapPoint.class);
        UserMapPoint point = dao.findUniqueByCriteria(Expression.and(Expression.eq("user", user), Expression.and(Expression.and(Expression.eq("lat", row.getLatitude()), Expression.eq("lon", row.getLongitude())), row.getAltitude() == null ? Expression.isNull("alt") : Expression.eq("alt", row.getAltitude()))));
        if (point == null) {
            point = new UserMapPoint(row.getName(), row.getDescription(), row.getLatitude(), row.getLongitude(), row.getAltitude(), UserMapPoint.State.UPLOAD, UserMapPoint.SubState.COMPLETE, user);
            point.setSubjects(TagUtil.refreshTags(TagUtil.getTagList(tags)));
            dao.save(point);
        }
        return point;
    }

    public static UserMapTrack createUserTrack(PLTTrack track, User user) throws DAOException {
        return createUserTrack(track, user, null);
    }

    public static UserMapTrack createUserTrack(PLTTrack track, User user, String tags) throws DAOException {
        GenericDAO<UserMapTrackPoint> pointDao = DAOFactory.createDAO(UserMapTrackPoint.class);
        GenericDAO<UserMapTrack> trackDao = DAOFactory.createDAO(UserMapTrack.class);
        UserMapTrack mapTrack = new UserMapTrack();
        final PLTTrackHeader trackHeader = track.getHeader();
        if (trackHeader != null) {
            final PLTTrackHeader.DisplayInfo trackDisplayInfo = trackHeader.getDisplayInfo();
            if (trackDisplayInfo != null) {
                mapTrack.setDescription(trackHeader.getDisplayInfo().getDescription());
            }
        }
        mapTrack.setName("");
        mapTrack.setUser(user);
        mapTrack.setState(UserMapTrack.State.UPLOAD);
        mapTrack.setSubstate(UserMapTrack.SubState.INPROC);
        mapTrack.setSubjects(TagUtil.refreshTags(TagUtil.getTagList(tags)));
        trackDao.save(mapTrack);
        Set<PLTTrackRow> rows = track.getTrackData();
        double swLat = 190;
        double swLon = 190;
        double neLat = -190;
        double neLon = -190;
        int order = 0;
        for (PLTTrackRow row : rows) {
            UserMapTrackPoint point = new UserMapTrackPoint(mapTrack, row.getLatitude(), row.getLongitude(), row.getAltitude(), order++);
            swLat = Math.min(swLat, row.getLatitude());
            swLon = Math.min(swLon, row.getLongitude());
            neLat = Math.max(neLat, row.getLatitude());
            neLon = Math.max(neLon, row.getLongitude());
            pointDao.save(point);
        }
        if (swLat < 190) {
            mapTrack.setSWLat(swLat);
            mapTrack.setSWLon(swLon);
            mapTrack.setNELat(neLat);
            mapTrack.setNELon(neLon);
        }
        return mapTrack;
    }

    public static Collection<MapPointResponse> createMapPointResponseList(Collection<? extends MapPoint> points) {
        List<MapPointResponse> response = new ArrayList<MapPointResponse>();
        for (MapPoint point : points) response.add(new MapPointResponse(point));
        return response;
    }

    public static ResourceResponse createResourceResponse(PublicMapResource pr) {
        ResourceResponse r = new ResourceResponse();
        r.setCreated(pr.getCreated());
        r.setGuid(pr.getGuid());
        r.setLogin(pr.getLogin());
        r.setName(pr.getName());
        r.setDescription(pr.getDescription());
        r.setResource(pr.getResource());
        r.setTypeGUID(pr.getType().getGuid());
        r.setTypeName(pr.getType().getName());
        return r;
    }

    public static UserMapPoint createUserPoint(PlacemarkType placemark, User user) throws DAOException, IllegalAccessException, NoSuchMethodException, InvocationTargetException {
        return createUserPoint(placemark, user, null);
    }

    public static UserMapPoint createUserPoint(PlacemarkType placemark, User user, String tags) throws DAOException, IllegalAccessException, NoSuchMethodException, InvocationTargetException {
        if (!PointType.class.isAssignableFrom(placemark.getGeometry().getValue().getClass())) return null;
        List<GeoData> coord = ParserUtil.extractGeoData(((PointType) placemark.getGeometry().getValue()).getCoordinates().get(0));
        GeoData geoPoint = coord.get(0);
        GenericDAO<UserMapPoint> dao = DAOFactory.createDAO(UserMapPoint.class);
        UserMapPoint point = dao.findUniqueByCriteria(Expression.and(Expression.eq("user", user), Expression.and(Expression.and(Expression.eq("lat", geoPoint.getLatitude()), Expression.eq("lon", geoPoint.getLongitude())), geoPoint.getAltitude() == null ? Expression.isNull("alt") : Expression.eq("alt", geoPoint.getAltitude()))));
        if (point == null) {
            point = new UserMapPoint(placemark.getName(), placemark.getDescription(), geoPoint.getLatitude(), geoPoint.getLongitude(), geoPoint.getAltitude(), UserMapPoint.State.UPLOAD, UserMapPoint.SubState.COMPLETE, user);
            point.setSubjects(TagUtil.refreshTags(TagUtil.getTagList(tags)));
            dao.save(point);
        }
        return point;
    }

    public static UserMapTrack createUserTrack(PlacemarkType placemark, User user) throws DAOException, IllegalAccessException, NoSuchMethodException, InvocationTargetException {
        return createUserTrack(placemark, user, null);
    }

    public static UserMapTrack createUserTrack(PlacemarkType placemark, User user, String tags) throws DAOException, IllegalAccessException, NoSuchMethodException, InvocationTargetException {
        if (!LineStringType.class.isAssignableFrom(placemark.getGeometry().getValue().getClass())) return null;
        GenericDAO<UserMapTrackPoint> pointDao = DAOFactory.createDAO(UserMapTrackPoint.class);
        GenericDAO<UserMapTrack> trackDao = DAOFactory.createDAO(UserMapTrack.class);
        UserMapTrack mapTrack = new UserMapTrack();
        mapTrack.setDescription(placemark.getDescription());
        mapTrack.setName(placemark.getName());
        mapTrack.setUser(user);
        mapTrack.setState(UserMapTrack.State.UPLOAD);
        mapTrack.setSubstate(UserMapTrack.SubState.INPROC);
        mapTrack.setSubjects(TagUtil.refreshTags(TagUtil.getTagList(tags)));
        trackDao.save(mapTrack);
        double swLat = 190;
        double swLon = 190;
        double neLat = -190;
        double neLon = -190;
        int order = 0;
        for (String coord : ((LineStringType) placemark.getGeometry().getValue()).getCoordinates()) {
            GeoData row = ParserUtil.extractGeoData(coord).get(0);
            UserMapTrackPoint point = new UserMapTrackPoint(mapTrack, row.getLatitude(), row.getLongitude(), row.getAltitude(), order++);
            swLat = Math.min(swLat, row.getLatitude());
            swLon = Math.min(swLon, row.getLongitude());
            neLat = Math.max(neLat, row.getLatitude());
            neLon = Math.max(neLon, row.getLongitude());
            pointDao.save(point);
        }
        if (swLat < 190) {
            mapTrack.setSWLat(swLat);
            mapTrack.setSWLon(swLon);
            mapTrack.setNELat(neLat);
            mapTrack.setNELon(neLon);
        }
        return mapTrack;
    }

    public static UserMapOriginal createUserMapOriginal(MapProjection proj, User user) throws DAOException {
        return createUserMapOriginal(proj, user, null);
    }

    public static UserMapOriginal createUserMapOriginal(MapProjection proj, User user, String tags) throws DAOException {
        GenericDAO<UserMapOriginal> mapDao = DAOFactory.createDAO(UserMapOriginal.class);
        UserMapOriginal map = new UserMapOriginal();
        map.setName(proj.getTitle());
        map.setUser(user);
        map.setState(UserMapOriginal.State.UPLOAD);
        map.setSubstate(UserMapOriginal.SubState.COMPLETE);
        map.setSubjects(TagUtil.refreshTags(TagUtil.getTagList(tags)));
        MapManager.updateProjection(proj, map);
        mapDao.save(map);
        return map;
    }

    public static UserMapOriginal createUserMapOriginal(GroundOverlayType overlay, User user) throws DAOException {
        return createUserMapOriginal(overlay, user, null);
    }

    public static UserMapOriginal createUserMapOriginal(GroundOverlayType overlay, User user, String tags) throws DAOException {
        GenericDAO<UserMapOriginal> mapDao = DAOFactory.createDAO(UserMapOriginal.class);
        UserMapOriginal map = new UserMapOriginal();
        map.setName(overlay.getName());
        map.setDescription(overlay.getDescription());
        map.setUser(user);
        map.setState(UserMapOriginal.State.UPLOAD);
        map.setSubstate(UserMapOriginal.SubState.COMPLETE);
        map.setSubjects(TagUtil.refreshTags(TagUtil.getTagList(tags)));
        map.setNELat(overlay.getLatLonBox().getNorth());
        map.setNELon(overlay.getLatLonBox().getEast());
        map.setSWLat(overlay.getLatLonBox().getSouth());
        map.setSWLon(overlay.getLatLonBox().getWest());
        mapDao.save(map);
        return map;
    }
}
