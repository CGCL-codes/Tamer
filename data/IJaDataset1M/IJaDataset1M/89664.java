package com.ewaloo.impl.cms.app;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Connection;
import java.util.Map;
import java.util.Iterator;
import org.apache.commons.dbutils.DbUtils;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.atlantal.api.app.acl.ACL;
import org.atlantal.api.app.db.Query;
import org.atlantal.api.app.db.QueryException;
import org.atlantal.api.app.db.QueryResult;
import org.atlantal.api.app.db.QueryWhereGroup;
import org.atlantal.api.cache.CacheObject;
import org.atlantal.api.cms.app.content.ContentContext;
import org.atlantal.api.cms.app.content.ContentManager;
import org.atlantal.api.cms.content.Content;
import org.atlantal.api.cms.content.ContentWrapper;
import org.atlantal.api.cms.content.ObjectContent;
import org.atlantal.api.cms.data.MapContentData;
import org.atlantal.api.cms.definition.AggregationContentDefinition;
import org.atlantal.api.cms.definition.ContentDefinition;
import org.atlantal.api.cms.definition.ContentDefinitionItem;
import org.atlantal.api.cms.definition.Document;
import org.atlantal.api.cms.definition.Filter;
import org.atlantal.api.cms.definition.Form;
import org.atlantal.api.cms.exception.ContentException;
import org.atlantal.api.cms.exception.ContentIOException;
import org.atlantal.api.cms.exception.SourceException;
import org.atlantal.api.cms.field.Field;
import org.atlantal.api.cms.id.ContentId;
import org.atlantal.api.cms.util.ContentAccessMode;
import org.atlantal.api.cms.util.ContentDataAccessMode;
import org.atlantal.api.cms.util.MultiAccessItems;
import org.atlantal.utils.AtlantalMap;
import org.atlantal.utils.wrapper.ObjectWrapper;
import org.atlantal.impl.app.db.QueryResultInstance;
import com.ewaloo.api.cms.content.EwObjectContent;
import com.ewaloo.api.cms.field.EwTemplateField;
import com.ewaloo.api.cms.id.EwContentId;
import com.ewaloo.api.cms.source.EwTemplate;
import com.ewaloo.api.cms.source.EwTemplateChild;
import com.ewaloo.api.cms.source.EwTemplateClass;
import com.ewaloo.api.cms.util.EwContentAccessMode;
import com.ewaloo.impl.cms.app.lib.EwContentManagerContext;
import com.ewaloo.impl.cms.field.EwFieldLib;

/**
 * @author <a href="mailto:masurel@mably.com">Francois MASUREL</a>
 */
public final class MySQLContentReader {

    private static final Logger LOGGER = Logger.getLogger(MySQLContentReader.class);

    static {
        LOGGER.setLevel(Level.DEBUG);
    }

    /**
     *
     */
    private MySQLContentReader() {
    }

    /**
     * @param ctx ctx
     * @param obj obj
     * @param rs rs
     * @param prefix prefix
     * @throws SQLException a generic exception
     * @throws SourceException a generic exception
     */
    public static void readObject(EwContentManagerContext ctx, EwObjectContent obj, ResultSet rs, String prefix) throws SQLException, SourceException {
        ContentId ocid = obj.getId();
        ocid.setId(Integer.valueOf(rs.getInt(prefix + "id")));
        ocid.setVersion(rs.getInt(prefix + "id_version"));
        Integer idtree = Integer.valueOf(rs.getInt(prefix + "id_tree"));
        ObjectWrapper tplchildW = ctx.getContentManager().getContentSourceService().getContentSource(idtree);
        ocid.setSource(tplchildW);
        int idcontext = rs.getInt(prefix + "id_context");
        if (idcontext > 0) {
            obj.setContextId(Integer.valueOf(idcontext));
        }
        int idroot = rs.getInt(prefix + "id_root");
        if (idroot > 0) {
            obj.setRootId(Integer.valueOf(idroot));
        }
        int idparent = rs.getInt(prefix + "id_parent");
        if (idparent > 0) {
            ContentManager cm = ctx.getContentManager();
            EwContentId parentId = (EwContentId) cm.newContentId(ContentDefinition.OBJECT);
            parentId.setId(Integer.valueOf(idparent));
            obj.setParentId(parentId);
        }
        obj.setOwnerId(String.valueOf(rs.getInt(prefix + "id_owner")));
        obj.setState(rs.getInt(prefix + "state"));
        obj.setPublished(rs.getBoolean(prefix + "published"));
        MapContentData values = obj.getAggregationData();
        values.put("object_id", ocid.getId());
        values.put("object_id_root", obj.getRootId());
        values.put("object_id_context", obj.getContextId());
        values.put("object_id_version", Integer.valueOf(ocid.getVersion()));
        if (obj.getParentId() != null) {
            values.put("object_id_parent", obj.getParentId().getId());
        }
    }

    /**
     * @param conn conn
     * @param content content
     * @return boolean
     * @throws SQLException a generic exception
     * @throws SourceException a generic exception
     */
    protected static boolean loadBaseObject(EwContentManagerContext ctx, ContentAccessMode cam, Connection conn, ObjectContent content) throws SQLException, SourceException {
        boolean ok = false;
        EwObjectContent obj = (EwObjectContent) content;
        EwContentId objId = (EwContentId) obj.getId();
        Integer id = (Integer) objId.getId();
        String queryid = "base" + "b" + cam.getBranch();
        Query query = (Query) ctx.getSystemCache().get("objqry" + queryid);
        if (query == null) {
            query = new Query();
            QueryWhereGroup qwg = query.getWhereGroup("object");
            query.setGroupBy(true);
            getObjectSQL(null, query, cam);
            query.addWhere(qwg, "object", "object.id = <%_object_%>");
            ctx.getSystemCache().defineObject("objqry" + queryid, ctx.getServiceAttributes(), query, true);
        }
        Map params = new AtlantalMap();
        params.put("_object_", id.toString());
        String sql = query.toSQL(params).toString();
        Statement stmt = conn.createStatement();
        ResultSet rs = stmt.executeQuery(sql);
        if (rs.next()) {
            readObject(ctx, obj, rs, "object_");
            obj.setTempRights(~0);
            ok = true;
        }
        DbUtils.closeQuietly(rs);
        DbUtils.closeQuietly(stmt);
        return ok;
    }

    /**
     * @param conn conn
     * @param obj obj
     * @return boolean
     * @throws SQLException a generic exception
     * @throws SourceException a generic exception
     */
    protected static boolean loadBaseObjectSecure(EwContentManagerContext ctx, ContentAccessMode cam, Connection conn, EwObjectContent obj) throws SQLException, SourceException {
        boolean ok = false;
        ContentId objId = obj.getId();
        EwTemplateChild source = null;
        EwTemplate template = null;
        ObjectWrapper sourceW = objId.getSource();
        if (sourceW != null) {
            source = (EwTemplateChild) sourceW.getWrappedObject();
        }
        if (source != null) {
            template = source.getChildObject();
        }
        Integer id = (Integer) objId.getId();
        StringBuilder queryidBldr = new StringBuilder();
        if (template == null) {
            queryidBldr.append("sec");
        } else {
            queryidBldr.append("tplsec");
        }
        int branch = cam.getBranch();
        queryidBldr.append("b").append(branch);
        String queryid = queryidBldr.toString();
        Query query = (Query) ctx.getSystemCache().get("objqry" + queryid);
        if (query == null) {
            query = new Query();
            QueryWhereGroup qwg = query.getWhereGroup("object");
            query.setGroupBy(true);
            getObjectSQL(template, query, cam);
            query.addWhere(qwg, "object", "object.id = <%_object_%>");
            if (!cam.isTableCache()) {
                if (cam.isWorkingCopy()) {
                    query.addWhere(qwg, "object", "object.branch" + branch + "_newversion > 0");
                } else {
                    query.addWhere(qwg, "object", "object.branch" + branch + "_version > 0");
                }
            }
            query.addSelect("object_acl", "BIT_OR(object_acl.rights)", "object_acl_rights", false);
            String jtype = "INNER JOIN";
            String jsource = "object_acl";
            String jcond = "object_acl.id_obj_acl = object.id";
            query.addJoin("object_acl", jsource, jcond, jtype, "object");
            jtype = "INNER JOIN";
            jsource = "usergroup_tree AS usergrouptree";
            jcond = "usergrouptree.id_parent = object_acl.id_group";
            query.addJoin("usergrouptree", jsource, jcond, jtype, "object_acl");
            String where = "(usergrouptree.id_child = <%_userid_%>" + " AND (object_acl.rights & " + ACL.READ + ") > 0)";
            query.addWhere(qwg, "usergrouptree", where);
            CacheObject cacheobj = ctx.getSystemCache().defineObject("objqry" + queryid, ctx.getServiceAttributes(), query, true);
            if (sourceW != null) {
                CacheObject sourceCache = (CacheObject) sourceW;
                sourceCache.addDependent(cacheobj);
            }
        }
        Map params = new AtlantalMap();
        params.put("_object_", id.toString());
        params.put("_userid_", cam.getUser().getId());
        String sql = query.toSQL(params).toString();
        Statement stmt = conn.createStatement();
        ResultSet rs = stmt.executeQuery(sql);
        if (rs.next()) {
            readObject(ctx, obj, rs, "object_");
            objId.setSource(obj.getSource());
            obj.setTempRights(rs.getInt("object_acl_rights"));
            ok = true;
        }
        rs.close();
        stmt.close();
        return ok;
    }

    /**
     * @param ctx ctx
     * @param cam cam
     * @param conn conn
     * @param content content
     * @return boolean
     * @throws ContentIOException a generic exception
     */
    public static boolean loadObjectContent(EwContentManagerContext ctx, ContentAccessMode cam, Connection conn, Content content) throws ContentIOException {
        boolean ok = false;
        try {
            EwObjectContent obj = (EwObjectContent) content;
            EwContentId objId = (EwContentId) obj.getId();
            if (objId.isExtended()) {
                ok = loadObjectContentEx(ctx, cam, conn, obj);
            } else {
                if (cam.getSecure() > ACL.NONE) {
                    ok = loadBaseObjectSecure(ctx, cam, conn, obj);
                } else {
                    ok = loadBaseObject(ctx, cam, conn, obj);
                }
            }
        } catch (SQLException e) {
            throw new ContentIOException(e);
        } catch (SourceException e) {
            throw new ContentIOException(e);
        } catch (QueryException e) {
            throw new ContentIOException(e);
        }
        return ok;
    }

    /**
     * @param ctx ctx
     * @param conn conn
     * @param obj id
     * @return object
     * @throws SQLException a generic exception
     * @throws SourceException a generic exception
     * @throws QueryException a generic exception
     */
    protected static boolean loadObjectContentEx(EwContentManagerContext ctx, ContentAccessMode cam, Connection conn, EwObjectContent obj) throws SQLException, SourceException, QueryException {
        EwContentId objId = (EwContentId) obj.getId();
        ContentManager cm = ctx.getContentManager();
        ContentContext cntctx = new ContentContext(cm, cam);
        Integer id = objId.getIntegerId();
        if (objId.getSource() == null) {
            loadBaseObject(ctx, cam, conn, obj);
        }
        EwTemplateChild source = (EwTemplateChild) objId.getSourceObject();
        EwTemplate template = source.getChildObject();
        Query query = getTemplateFieldsQuery(ctx, template, cam);
        Map params = new AtlantalMap();
        params.put("_object_", id.toString());
        params.put("_userid_", cam.getUser().getId());
        String sql = query.toSQL(params).toString();
        LOGGER.debug(sql);
        boolean ok;
        Statement stmt = conn.createStatement();
        ResultSet rs = stmt.executeQuery(sql);
        if (rs.next()) {
            QueryResult result = new QueryResultInstance(rs);
            readObject(ctx, obj, rs, "object_");
            if (cam.getSecure() > ACL.NONE) {
                obj.setTempRights(rs.getInt("object_acl_rights"));
            }
            MapContentData data = obj.getAggregationData();
            readTemplateFieldsValues(cntctx, template, data, result, cam);
            ok = true;
        } else {
            ok = false;
        }
        rs.close();
        stmt.close();
        return ok;
    }

    /**
     * {@inheritDoc}
     */
    public static boolean loadAggregationContent(EwContentManagerContext ctx, ContentAccessMode cam, Connection conn, Content content) throws SQLException, SourceException, QueryException {
        boolean ok = false;
        ContentId coid = content.getId();
        ObjectWrapper contdefW = coid.getDefinition();
        AggregationContentDefinition contdef = (AggregationContentDefinition) coid.getDefinitionObject();
        ObjectWrapper templateW = contdef.getSource();
        EwTemplate template = (EwTemplate) contdef.getSourceObject();
        Integer id = (Integer) coid.getId();
        boolean cache = template.isTableCache(cam);
        StringBuilder cacheid = new StringBuilder();
        cacheid.append("aggsql").append(contdef.getId());
        cacheid.append(cache ? "c" : "");
        cacheid.append(cam.isWorkingCopy() ? "w" : "");
        cacheid.append("b").append(cam.getBranch());
        Query query = (Query) ctx.getSystemCache().get(cacheid.toString());
        if (query == null) {
            query = loadAggregationQuery(cam, contdef, coid);
            CacheObject cacheobj = ctx.getSystemCache().defineObject(cacheid.toString(), ctx.getServiceAttributes(), query, true);
            CacheObject contDefCache = (CacheObject) contdefW;
            contDefCache.addDependent(cacheobj);
            CacheObject sourceCache = (CacheObject) templateW;
            sourceCache.addDependent(cacheobj);
        }
        Map params = new AtlantalMap();
        params.put("_object_", id.toString());
        params.put("_userid_", cam.getUser().getId());
        String sql = query.toSQL(params).toString();
        LOGGER.debug(sql);
        Statement stmt = conn.createStatement();
        ResultSet rs = stmt.executeQuery(sql);
        if (rs.next()) {
            QueryResult result = new QueryResultInstance(rs);
            EwObjectContent obj = (EwObjectContent) content;
            readObject(ctx, obj, rs, "object_");
            obj.setTempRights(rs.getInt("object_acl_rights"));
            MapContentData ctvalues = obj.getAggregationData();
            ctvalues.put("object_acl_rights", Integer.valueOf(obj.getTempRights()));
            EwTemplateChild tplchild = (EwTemplateChild) obj.getSourceObject();
            template = tplchild.getChildObject();
            ObjectWrapper[] tcs = template.getClassesObject();
            for (int i = 0; i < tcs.length; i++) {
                EwTemplateClass tc = (EwTemplateClass) tcs[i].getWrappedObject();
                String code = tc.getCode();
                int codeid = rs.getInt(code + "_id");
                ctvalues.put(code + ".id", Integer.valueOf(codeid));
                ctvalues.put(code + "_id", Integer.valueOf(codeid));
            }
            Map fileActions = new AtlantalMap();
            MultiAccessItems aggregItems = contdef.getItemsObject();
            int length = aggregItems.size();
            Field field;
            ContentDefinitionItem item;
            for (int i = 0; i < length; i++) {
                item = (ContentDefinitionItem) aggregItems.get(i);
                field = item.getFieldObject();
                field.queryValues(result, ctvalues, null);
                if (field.hasPrepareMethods()) {
                    field.prepareLoad(ctvalues, fileActions);
                }
            }
            if (fileActions.size() > 0) {
                MySQLContentFileActions.executeFileActions(ctx.getContentManager(), fileActions);
            }
            ok = true;
        } else {
            ok = false;
        }
        DbUtils.closeQuietly(rs);
        DbUtils.closeQuietly(stmt);
        return ok;
    }

    private static Query loadAggregationQuery(ContentAccessMode cam, AggregationContentDefinition contdef, ContentId coid) {
        EwTemplate template = (EwTemplate) contdef.getSourceObject();
        boolean cache = template.isTableCache(cam);
        Query query = new Query();
        QueryWhereGroup qwg = query.getWhereGroup("object");
        query.setGroupBy(true);
        getObjectSQL(template, query, cam);
        String pfx = cache ? "_object_" : "";
        query.addWhere(qwg, "object", "object." + pfx + "id = <%_object_%>");
        query.addSelect("object_acl", "BIT_OR(object_acl.rights)", "object_acl_rights", false);
        String jtype = "INNER JOIN";
        String jsource = "object_acl";
        String jcond = "object_acl.id_obj_acl = object." + pfx + "id";
        query.addJoin("object_acl", jsource, jcond, jtype, "object");
        jtype = "INNER JOIN";
        jsource = "usergroup_tree AS usergrouptree";
        jcond = "usergrouptree.id_parent = object_acl.id_group";
        query.addJoin("usergrouptree", jsource, jcond, jtype, "object_acl");
        String where = "((usergrouptree.id_child = <%_userid_%>" + " AND (object_acl.rights & " + ACL.READ + ") > 0))";
        query.addWhere(qwg, "usergrouptree", where);
        getTemplateSQL(template, query, cam, true);
        switch(coid.getType()) {
            case ContentDefinition.FORM:
                getFormSQL((Form) contdef, query, cam);
                break;
            case ContentDefinition.DOCUMENT:
                getDocumentSQL((Document) contdef, query, cam);
                break;
            default:
                break;
        }
        return query;
    }

    /**
     * @param template template
     * @param query query
     * @param cam cam
     */
    protected static void getObjectSQL(EwTemplate template, Query query, ContentAccessMode cam) {
        int branch = cam.getBranch();
        if ((template != null) && template.isTableCache(cam)) {
            query.addSelect("object", "object._object_id", "object_id", true);
            query.addSelect("object", "object._object_id_tree", "object_id_tree", true);
            query.addSelect("object", "object._object_id_context", "object_id_context", true);
            query.addSelect("object", "object._object_id_root", "object_id_root", true);
            query.addSelect("object", "object._object_id_parent", "object_id_parent", true);
            query.addSelect("object", "object._object_id_owner", "object_id_owner", true);
            query.addSelect("object", "object._object_id_version", "object_id_version", true);
            query.addSelect("object", "object._object_state", "object_state", true);
            query.addSelect("object", "true", "object_published", true);
            String tplcachename = template.getCacheName();
            query.addFrom("object", tplcachename + " AS object");
        } else {
            query.addSelect("object", "object.id", "object_id", true);
            query.addSelect("object", "object.id_tree", "object_id_tree", true);
            query.addSelect("object", "object.id_context", "object_id_context", true);
            query.addSelect("object", "object.id_root", "object_id_root", true);
            query.addSelect("object", "object.id_parent", "object_id_parent", true);
            query.addSelect("object", "object.id_owner", "object_id_owner", true);
            if (cam.isWorkingCopy()) {
                query.addSelect("object", "object.branch" + branch + "_newversion", "object_id_version", true);
            } else {
                query.addSelect("object", "object.branch" + branch + "_version", "object_id_version", true);
            }
            query.addSelect("object", "(object.branch" + branch + "_newversion = " + "object.branch" + branch + "_version)", "object_published", true);
            query.addSelect("object", "object.branch" + branch + "_state", "object_state", true);
            query.addFrom("object", "objects AS object");
        }
    }

    /**
     * @param form form
     * @param query query
     * @param cam mode
     */
    public static void getFormSQL(Form form, Query query, ContentAccessMode cam) {
        if (form != null) {
            Iterator it = form.getItemsObject().getIdMap().values().iterator();
            while (it.hasNext()) {
                ObjectWrapper eltW = (ObjectWrapper) it.next();
                ContentDefinitionItem elt = (ContentDefinitionItem) eltW.getWrappedObject();
                elt.queryPrepare(query, cam, null);
                elt.querySelect(query, cam);
            }
        }
    }

    /**
     * @param doc document
     * @param query query
     * @param cam mode
     */
    public static void getDocumentSQL(Document doc, Query query, ContentAccessMode cam) {
        if (doc != null) {
            MultiAccessItems items = doc.getItemsObject();
            int length = items.size();
            for (int i = 0; i < length; i++) {
                ContentDefinitionItem item = (ContentDefinitionItem) items.get(i);
                item.queryPrepare(query, cam, null);
                item.querySelect(query, cam);
            }
        }
    }

    /**
     * @param child child
     * @param query query
     * @param qwg qwg
     * @param cam cam
     */
    public static void getChildFilterSQL(EwTemplateChild child, Query query, QueryWhereGroup qwg, ContentAccessMode cam) {
        int id = ((Integer) child.getId()).intValue();
        if (child.getChildObject().isTableCache(cam)) {
            query.addWhere(qwg, "object", "object._object_id_tree = " + id);
        } else {
            query.addWhere(qwg, "object", "object.id_tree = " + id);
        }
    }

    /**
     * @param filter filter
     * @param query query
     * @param qwg qwg
     * @param mode mode
     */
    public static void getFilterSQL(Filter filter, Query query, QueryWhereGroup qwg, ContentAccessMode mode) {
        Map params = new AtlantalMap();
        filter.sqlWhere(query, qwg, params, mode);
    }

    /**
     * @param template template
     * @param query query
     * @param cam branch
     * @param withClassIds TODO
     */
    public static void getTemplateSQL(EwTemplate template, Query query, ContentAccessMode cam, boolean withClassIds) {
        if (template.isTableCache(cam)) {
            MySQLContentSQL.getTemplateCacheSQL(template, query, withClassIds);
        } else {
            MySQLContentSQL.getTemplateNoCacheSQL(template, query, cam, withClassIds);
        }
    }

    /**
     * @param template template
     * @param query query
     * @param cam mode
     */
    public static void getTemplateFieldsSQL(EwTemplate template, Query query, ContentAccessMode cam) {
        if (template != null) {
            ObjectWrapper[] flds = template.getFieldsObject();
            for (int i = 0; i < flds.length; i++) {
                EwTemplateField field = (EwTemplateField) flds[i].getWrappedObject();
                if (cam.isTableCache() && !field.isTableCache()) {
                    continue;
                }
                switch(field.getFieldType()) {
                    case EwTemplateField.FIELD_NORMAL:
                        field.querySelect(query, cam, null);
                        break;
                    default:
                        break;
                }
            }
        }
    }

    /**
     * @param ctx ctx
     * @param template template
     * @param cam cam
     * @return query
     */
    protected static Query getTemplateFieldsQuery(EwContentManagerContext ctx, EwTemplate template, ContentAccessMode cam) {
        int branch = cam.getBranch();
        boolean secure = (cam.getSecure() > ACL.NONE);
        boolean working = cam.isWorkingCopy();
        boolean cache = template.isTableCache(cam);
        StringBuilder key = new StringBuilder();
        key.append(template.getId().toString());
        key.append("b").append(branch);
        key.append((cache ? "c" : ""));
        key.append((secure ? "s" : ""));
        key.append((working ? "w" : ""));
        Query query = (Query) ctx.getSystemCache().get("tplqry" + key);
        if (query == null) {
            query = new Query();
            QueryWhereGroup qwg = query.getWhereGroup("object");
            query.setGroupBy(true);
            getObjectSQL(template, query, cam);
            if (cache) {
                query.addWhere(qwg, "object", "_object_id = <%_object_%>");
            } else {
                query.addWhere(qwg, "object", "object.id = <%_object_%>");
            }
            if (secure) {
                query.addSelect("object_acl", "BIT_OR(object_acl.rights)", "object_acl_rights", false);
                String jtype = "INNER JOIN";
                String jsource = "object_acl";
                String jcond = "object_acl.id_obj_acl = " + (cache ? "object._object_id" : "object.id");
                query.addJoin("object_acl", jsource, jcond, jtype, "object");
                jtype = "INNER JOIN";
                jsource = "usergroup_tree AS usergrouptree";
                jcond = "usergrouptree.id_parent = object_acl.id_group";
                query.addJoin("usergrouptree", jsource, jcond, jtype, "object_acl");
                String where = "((usergrouptree.id_child = <%_userid_%>" + " AND (object_acl.rights & " + ACL.READ + ") > 0))";
                query.addWhere(qwg, "usergrouptree", where);
            }
            getTemplateSQL(template, query, cam, true);
            getTemplateFieldsSQL(template, query, cam);
            CacheObject cacheobj = ctx.getSystemCache().defineObject("tplqry" + key, ctx.getServiceAttributes(), query, true);
            CacheObject tplCache = ctx.getSystemCache().getHandle(template.getId());
            tplCache.addDependent(cacheobj);
        }
        return query;
    }

    /**
     * @param obj obj
     * @param rs rs
     * @param secure secure
     * @param cdam mode
     * @throws SQLException a generic exception
     * @throws QueryException a generic exception
     */
    protected static void readTemplateFieldsValues(ContentContext ctx, EwTemplate template, MapContentData ctvalues, QueryResult result, ContentAccessMode cam) throws SQLException, QueryException {
        ObjectWrapper[] flds = template.getFieldsObject();
        for (int i = 0; i < flds.length; i++) {
            EwTemplateField field = (EwTemplateField) flds[i].getWrappedObject();
            if (cam.isTableCache() && !field.isTableCache()) {
                continue;
            }
            switch(field.getFieldType()) {
                case EwTemplateField.FIELD_NORMAL:
                    field.queryValues(result, ctvalues, null);
                    break;
                default:
                    break;
            }
        }
    }

    /**
     * @param template
     * @return
     */
    private static Query getTemplateFullTextQuery(EwContentManagerContext ctx, EwTemplate template) {
        String cacheId = "qrytplcache" + template.getId() + "FT";
        Query query = (Query) ctx.getSystemCache().get(cacheId);
        if (query == null) {
            query = new Query();
            query.setNewVersion(true);
            query.addSelect("object", "object.id", "object_id", true);
            query.addFrom("object", "objects AS object");
            QueryWhereGroup qwg = query.getWhereGroup("object");
            query.addWhere(qwg, "object", "object.id = <%_object_%>");
            ContentAccessMode cam = new EwContentAccessMode();
            cam.setBranch(EwContentAccessMode.BRANCH_PROD);
            cam.setWorkingCopy(false);
            cam.setTableCache(false);
            ContentDataAccessMode cdam = cam.getDataAccessMode();
            cdam.setFulltext(true);
            ObjectWrapper[] tcs = template.getClassesObject();
            for (int i = 0; i < tcs.length; i++) {
                EwTemplateClass tc = (EwTemplateClass) tcs[i].getWrappedObject();
                String fldSrcAlias = "object_" + tc.getCode();
                EwFieldLib.templateClassJoin(query, tc, fldSrcAlias, "object", cam);
            }
            ObjectWrapper[] flds = template.getFieldsObject();
            for (int i = 0; i < flds.length; i++) {
                EwTemplateField field = (EwTemplateField) flds[i].getWrappedObject();
                if (field.isFullText()) {
                    field.queryPrepare(query, cam, null);
                    field.querySelect(query, cam, null);
                }
            }
            CacheObject cacheobj = ctx.getSystemCache().defineObject(cacheId, ctx.getServiceAttributes(), query, true);
            CacheObject depCache = ctx.getSystemCache().getHandle(template.getId());
            depCache.addDependent(cacheobj);
        }
        return query;
    }

    /**
     * @param obj
     * @param rs
     * @param secure
     * @throws ContentException
     */
    private static void readTemplateFullTextValues(ContentWrapper objW, QueryResult rs) throws QueryException {
        EwObjectContent obj = (EwObjectContent) objW.getContent();
        MapContentData ctvalues = obj.getAggregationData();
        EwTemplateChild child = (EwTemplateChild) obj.getSourceObject();
        EwTemplate template = child.getChildObject();
        ObjectWrapper[] flds = template.getFieldsObject();
        for (int i = 0; i < flds.length; i++) {
            EwTemplateField field = (EwTemplateField) flds[i].getWrappedObject();
            if (field.isFullText()) {
                field.queryValues(rs, ctvalues, null);
            }
        }
    }

    /**
     * @param obj
     * @param conn
     * @throws ContentException
     */
    private static void loadTemplateFullTextObject(EwContentManagerContext ctx, ContentWrapper objW, Connection conn) throws SQLException, QueryException {
        EwObjectContent obj = (EwObjectContent) objW.getContent();
        ContentId objid = obj.getId();
        EwTemplateChild child = (EwTemplateChild) obj.getSourceObject();
        EwTemplate template = child.getChildObject();
        Query query = getTemplateFullTextQuery(ctx, template);
        Map params = new AtlantalMap();
        params.put("_object_", objid.getId().toString());
        String sql = query.toSQL(params).toString();
        LOGGER.debug(sql);
        Statement stmt = conn.createStatement();
        ResultSet rs = stmt.executeQuery(sql);
        if (rs.next()) {
            QueryResult result = new QueryResultInstance(rs);
            readTemplateFullTextValues(objW, result);
        }
        rs.close();
        stmt.close();
    }

    /**
     * @param mgrctx mgrctx
     * @param ctx ctx
     * @param ft ft
     * @param objW obj
     * @param conn conn
     * @throws ContentIOException a generic exception
     */
    public static void getObjectFullText(EwContentManagerContext mgrctx, ContentContext ctx, StringBuilder ft, ContentWrapper objW, Connection conn) throws ContentIOException {
        EwObjectContent obj = (EwObjectContent) objW.getContent();
        try {
            loadTemplateFullTextObject(mgrctx, objW, conn);
        } catch (SQLException e) {
            throw new ContentIOException(e);
        } catch (QueryException e) {
            throw new ContentIOException(e);
        }
        EwTemplateChild child = (EwTemplateChild) obj.getSourceObject();
        ObjectWrapper[] fields = child.getChildObject().getFieldsObject();
        for (int i = 0; i < fields.length; i++) {
            EwTemplateField field = (EwTemplateField) fields[i].getWrappedObject();
            if (field.isFullText()) {
                ft.append(" ");
                field.fullText(ft, obj.getAggregationData());
            }
        }
        ObjectWrapper[] children = child.getChildrenObject();
        for (int i = 0; i < children.length; i++) {
            EwTemplateChild tplchild = (EwTemplateChild) children[i].getWrappedObject();
            if (tplchild.getFullText()) {
                try {
                    getChildrenFullText(ft, obj.getId(), tplchild, conn);
                } catch (Exception e) {
                    e.printStackTrace(System.out);
                }
            }
        }
    }

    /**
     * @param parentid parentid
     * @param child child
     * @param conn conn
     * @return fulltext
     */
    private static void getChildrenFullText(StringBuilder ft, ContentId parentid, EwTemplateChild child, Connection conn) throws ContentException {
        Statement stmt = null;
        try {
            StringBuilder sql = new StringBuilder();
            sql.append("SELECT object_fulltext.full_text");
            sql.append(" FROM object_fulltext INNER JOIN objects");
            sql.append(" ON (objects.id = object_fulltext.id_object AND ");
            sql.append("objects.branch0_version = object_fulltext.id_version)");
            sql.append(" WHERE objects.id_parent = ").append(parentid);
            sql.append(" AND objects.id_tree = ").append(child.getId());
            LOGGER.debug(sql);
            stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql.toString());
            while (rs.next()) {
                ft.append(" ");
                ft.append(rs.getString("full_text"));
            }
            rs.close();
        } catch (Exception e) {
            e.printStackTrace(System.out);
        } finally {
            DbUtils.closeQuietly(stmt);
        }
    }
}
