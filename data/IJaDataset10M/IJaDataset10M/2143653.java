package org.linkedgeodata.dao;

import java.awt.geom.Point2D;
import java.awt.geom.RectangularShape;
import java.io.OutputStream;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import org.apache.commons.collections15.MultiMap;
import org.apache.commons.collections15.multimap.MultiHashMap;
import org.apache.log4j.Logger;
import org.linkedgeodata.core.LGDVocab;
import org.linkedgeodata.core.OSMEntityType;
import org.linkedgeodata.util.ITransformer;
import org.linkedgeodata.util.SQLUtil;
import org.linkedgeodata.util.StringUtil;
import org.linkedgeodata.util.stats.SimpleStatsTracker;
import org.openstreetmap.osmosis.core.domain.v0_6.Node;
import org.openstreetmap.osmosis.core.domain.v0_6.Tag;
import org.openstreetmap.osmosis.core.domain.v0_6.Way;
import com.hp.hpl.jena.rdf.model.AnonId;
import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import com.hp.hpl.jena.rdf.model.Resource;
import com.hp.hpl.jena.vocabulary.RDF;

public class LGDDAO {

    private static final Logger logger = Logger.getLogger(LGDDAO.class);

    private Connection conn;

    private NodeDAO nodeDAO = new NodeDAO();

    private WayDAO wayDAO = new WayDAO();

    public LGDDAO() {
    }

    public LGDDAO(Connection conn) throws SQLException {
        setConnection(conn);
    }

    public void setConnection(Connection conn) throws SQLException {
        this.conn = conn;
        nodeDAO.setConnection(conn);
        wayDAO.setConnection(conn);
    }

    public Connection getConnection() {
        return conn;
    }

    public NodeDAO getNodeDAO() {
        return nodeDAO;
    }

    public WayDAO getWayDAO() {
        return wayDAO;
    }

    public static MultiMap<Long, Tag> getTags(Connection conn, String type, Collection<Long> ids, String tagFilterStr) throws SQLException {
        MultiMap<Long, Tag> result = new MultiHashMap<Long, Tag>();
        if (ids == null || ids.isEmpty()) return result;
        String sql = "SELECT " + type + "_id AS id, k, v FROM " + type + "_tags WHERE " + type + "_id IN (" + StringUtil.implode(",", ids) + ") ";
        if (tagFilterStr != null) {
            sql += "AND " + tagFilterStr + " ";
        }
        logger.trace(sql);
        ResultSet rs = conn.createStatement().executeQuery(sql);
        int counter = 0;
        while (rs.next()) {
            ++counter;
            long wayId = rs.getLong("id");
            String k = rs.getString("k");
            String v = rs.getString("v");
            Tag tag = new Tag(k, v);
            result.put(wayId, tag);
        }
        return result;
    }

    /**
	 * @param type
	 * @param rect
	 * @param limit
	 * @param entityFilter
	 * @return
	 */
    public Collection<Long> getNodesWithinRect(RectangularShape rect, String tagFilter, Long offset, Integer limit) throws SQLException {
        String sql = buildGetNodesWithinRectQuery(rect, tagFilter, limit, offset);
        Collection<Long> result = SQLUtil.executeList(conn, sql, Long.class);
        return result;
    }

    public Collection<Long> getWaysWithinRect(RectangularShape rect, String tagFilter, Long offset, Integer limit) throws SQLException {
        String sql = buildGetWaysWithinRectQuery(rect, tagFilter, limit, offset);
        Collection<Long> result = SQLUtil.executeList(conn, sql, Long.class);
        return result;
    }

    public Collection<Long> getNodesWithinRadius(Point2D point, double radius, String tagFilter, Integer limit, Long offset) throws SQLException {
        String sql = buildGetNodesWithinRadiusQuery(point, radius, tagFilter, limit, offset);
        Collection<Long> result = SQLUtil.executeList(conn, sql, Long.class);
        return result;
    }

    public Collection<Long> getWaysWithinRadius(Point2D point, double radius, String tagFilter, Integer limit, Long offset) throws SQLException {
        String sql = buildGetWaysWithinRadiusQuery(point, radius, tagFilter, limit, offset);
        Collection<Long> result = SQLUtil.executeList(conn, sql, Long.class);
        return result;
    }

    private static String buildGetNodesWithinRadiusQuery(Point2D point, double radius, String tagFilter, Integer limit, Long offset) {
        String spatialCondition = "ST_DWithin(x.geom, " + LGDQueries.buildPoint(point.getY(), point.getX()) + "::geography, " + radius + ", true) ";
        String result = buildGetEntitiesWithinX("node", spatialCondition, tagFilter, limit, offset);
        return result;
    }

    private static String buildGetWaysWithinRadiusQuery(Point2D point, double radius, String tagFilter, Integer limit, Long offset) {
        String spatialCondition = "ST_DWithin(x.linestring, " + LGDQueries.buildPoint(point.getY(), point.getX()) + "::geography, " + radius + ", true) ";
        String result = buildGetEntitiesWithinX("way", spatialCondition, tagFilter, limit, offset);
        return result;
    }

    private static String buildGetNodesWithinRectQuery(RectangularShape rect, String tagFilter, Integer limit, Long offset) {
        String spatialCondition = "geom && " + LGDQueries.BBox(rect);
        String result = buildGetEntitiesWithinX("node", spatialCondition, tagFilter, limit, offset);
        return result;
    }

    private static String buildGetWaysWithinRectQuery(RectangularShape rect, String tagFilter, Integer limit, Long offset) {
        String spatialCondition = "linestring && " + LGDQueries.BBox(rect);
        String result = buildGetEntitiesWithinX("way", spatialCondition, tagFilter, limit, offset);
        return result;
    }

    public static String buildGetEntitiesWithinX(String tableName, String spatialCondition, String tagFilter, Integer limit, Long offset) {
        String result = "SELECT DISTINCT id FROM  " + tableName + "s x ";
        if (tagFilter != null) result += "JOIN " + tableName + "_tags ON (" + tableName + "_id = id) ";
        result += "WHERE " + spatialCondition + " ";
        if (tagFilter != null) result += "AND " + tagFilter + " ";
        if (limit != null) result += "LIMIT " + limit;
        if (offset != null) result += "OFFSET " + offset;
        return result;
    }
}
