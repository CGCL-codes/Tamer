package com.ewaloo.impl.cms.app;

import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.LinkedHashMap;
import java.util.Map;
import javax.sql.DataSource;
import org.apache.commons.dbutils.DbUtils;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.atlantal.api.app.acl.ACL;
import org.atlantal.api.app.db.DatabasePool;
import org.atlantal.api.app.db.QueryException;
import org.atlantal.api.app.exception.ServiceException;
import org.atlantal.api.app.rundata.AtlantalRequest;
import org.atlantal.api.app.user.User;
import org.atlantal.api.cache.Attributes;
import org.atlantal.api.cache.Cache;
import org.atlantal.api.cache.CacheLoader;
import org.atlantal.api.cache.CacheLoaderParams;
import org.atlantal.api.cache.CacheObject;
import org.atlantal.api.cms.app.content.ContentManagerContext;
import org.atlantal.api.cms.content.CachedContentWrapper;
import org.atlantal.api.cms.content.ChildrenContent;
import org.atlantal.api.cms.content.Content;
import org.atlantal.api.cms.content.ContentWrapper;
import org.atlantal.api.cms.content.ForeignKeyContent;
import org.atlantal.api.cms.content.ListContent;
import org.atlantal.api.cms.content.ObjectContent;
import org.atlantal.api.cms.definition.ContentDefinition;
import org.atlantal.api.cms.definition.Filter;
import org.atlantal.api.cms.exception.ContentException;
import org.atlantal.api.cms.exception.ContentIOException;
import org.atlantal.api.cms.exception.ContentWriteException;
import org.atlantal.api.cms.exception.SourceException;
import org.atlantal.api.cms.id.CachedContentId;
import org.atlantal.api.cms.id.ChildrenContentId;
import org.atlantal.api.cms.id.ContentId;
import org.atlantal.api.cms.id.ForeignKeyContentId;
import org.atlantal.api.cms.source.ContentSource;
import org.atlantal.api.cms.util.ContentAccessMode;
import org.atlantal.api.cms.util.Currency;
import org.atlantal.api.cms.util.FileInfo;
import org.atlantal.api.workflow.Workflow;
import org.atlantal.api.workflow.WorkflowException;
import org.atlantal.utils.ActionResults;
import org.atlantal.utils.ActionResultsItem;
import org.atlantal.utils.AtlantalException;
import org.atlantal.utils.session.Session;
import org.atlantal.utils.session.SessionException;
import org.atlantal.utils.session.SessionInstance;
import org.atlantal.utils.sql.SQL;
import org.atlantal.impl.app.app.ApplicationInstance;
import org.atlantal.impl.cache.AttributesInstance;
import org.atlantal.impl.cache.base.BaseCacheLoaderParamsInstance;
import com.ewaloo.api.cms.content.EwObjectContent;
import com.ewaloo.api.cms.id.EwListContentId;
import com.ewaloo.api.cms.source.EwTemplate;
import com.ewaloo.api.cms.source.EwTemplateChild;
import com.ewaloo.api.cms.util.ObjectTreeConfig;
import com.ewaloo.impl.cms.app.content.AbstractEwContentManager;
import com.ewaloo.impl.cms.app.lib.EwContentManagerContext;
import com.ewaloo.impl.cms.loader.content.MySQLContentLoader;

/**
 * <p>Titre : Ewaloo</p>
 * <p>Description : Moteur de recherche structur�</p>
 * <p>Copyright : Copyright (c) 2002</p>
 * <p>Soci�t� : Mably Multim�dia</p>
 * @author Fran�ois MASUREL
 * @version 1.0
 */
public class EwSQLContentManager extends AbstractEwContentManager {

    private static final Logger LOGGER = Logger.getLogger(EwSQLContentManager.class);

    static {
        LOGGER.setLevel(Level.INFO);
    }

    private static final String ERR_NOEDITMODE = "Vous n'�tes pas en mode 'Edition' !";

    private static final String SQL_OBJSRC_RIGHTS = "SELECT usergrouptree.id_child, template_tree_acl.rights" + " FROM template_tree_acl" + " INNER JOIN usergroup_tree AS usergrouptree" + " ON template_tree_acl.id_group = usergrouptree.id_parent" + " INNER JOIN object_ctx_tree" + " ON object_ctx_tree.id_parent = template_tree_acl.id_ctx_acl";

    private static final String APPBASEPATH;

    static {
        APPBASEPATH = ApplicationInstance.getParameter("application.basepath");
    }

    /** Comment for <code>currencies</code> */
    private Map currencies;

    private DatabasePool dbpool = null;

    private final EwContentManagerContext context;

    /**
     * Constructor
     */
    public EwSQLContentManager() {
        context = new EwContentManagerContext();
    }

    /**
     * {@inheritDoc}
     */
    public void init(Map params) throws ServiceException {
        super.init(params);
        String dbservice = (String) params.get("databasepool");
        dbpool = (DatabasePool) this.getService(dbservice);
        Cache systemCache = getCacheService().getCache("system");
        Cache fkCache = getCacheService().getCache("foreignkeys");
        Cache contentCache = getContentCacheService().getCache("content");
        CacheLoader loader;
        String jclass;
        CacheLoaderParams clparams = new BaseCacheLoaderParamsInstance(this, dbpool);
        AttributesInstance serviceAttributes = new AttributesInstance();
        serviceAttributes.setPermanent(true);
        jclass = MySQLContentLoader.class.getName();
        loader = getCacheService().getLoader(jclass);
        AttributesInstance contentAttributes = new AttributesInstance();
        contentAttributes.setLoader(loader);
        contentAttributes.setLoaderParams(clparams);
        Attributes fkAttributes = new AttributesInstance();
        ObjectTreeConfig contextTreeConfig = new ObjectTreeConfig();
        contextTreeConfig.setTable("object_ctx_tree");
        contextTreeConfig.setParent("id_parent");
        contextTreeConfig.setChild("id_child");
        contextTreeConfig.setDistance("distance");
        contextTreeConfig.init();
        context.setContentManager(this);
        context.setSystemCache(systemCache);
        context.setContentCache(contentCache);
        context.setServiceAttributes(serviceAttributes);
        context.setContentAttributes(contentAttributes);
        context.setContextTreeConfig(contextTreeConfig);
        context.setFKCache(fkCache);
        context.setFKAttributes(fkAttributes);
        context.setDatabasePool(this.dbpool);
        reload();
    }

    /**
     * {@inheritDoc}
     */
    public void reload() {
    }

    /**
     * @return Content manager context
     */
    public ContentManagerContext getContext() {
        return context;
    }

    /**
     * {@inheritDoc}
     */
    public ContentWrapper getContentWrapper(ContentAccessMode cam, ContentId id) throws ContentIOException {
        return getContentWrapper(cam, id, true);
    }

    /**
     * {@inheritDoc}
     */
    public ContentWrapper getContentWrapper(ContentAccessMode cam, ContentId id, boolean load) throws ContentIOException {
        ContentWrapper wrapper;
        if (cam.isMemCache() && (id instanceof CachedContentId) && ((CachedContentId) id).isCacheable()) {
            wrapper = getCachedContentWrapper(cam, id);
        } else {
            wrapper = newContentInstanceWrapper(cam, id);
            if (load) {
                boolean loaded = loadContent(cam, wrapper.getContent());
                wrapper.setLoaded(loaded);
            }
        }
        return wrapper;
    }

    /**
     * {@inheritDoc}
     */
    public boolean loadContent(ContentAccessMode cam, Content content) throws ContentIOException {
        boolean ok = false;
        if (content != null) {
            ContentId pid = content.getId();
            try {
                switch(pid.getType()) {
                    case ContentDefinition.OBJECT:
                        ok = loadObjectContent(cam, content);
                        break;
                    case ContentDefinition.DOCUMENT:
                    case ContentDefinition.FORM:
                        ok = loadAggregationContent(cam, content);
                        break;
                    case ContentDefinition.LIST:
                        ok = loadListContent(cam, content);
                        break;
                    case ContentDefinition.FOREIGN:
                        ok = loadForeignKeyContent(cam, content);
                        break;
                    case ContentDefinition.CHILDREN:
                        ok = loadChildrenContent(cam, content);
                        break;
                    case ContentDefinition.UNDEFINED:
                        ok = true;
                        break;
                    default:
                        ok = false;
                        break;
                }
            } catch (SQLException e) {
                throw new ContentIOException(e);
            } catch (SourceException e) {
                throw new ContentIOException(e);
            } catch (QueryException e) {
                throw new ContentIOException(e);
            }
        }
        return ok;
    }

    /**
     * {@inheritDoc}
     */
    public ContentWrapper getCachedContentWrapper(ContentAccessMode cam, ContentId id) throws ContentIOException {
        ContentWrapper ccw = null;
        CachedContentWrapper wrapper = (CachedContentWrapper) newCachedContentInstanceWrapper(cam, id);
        CacheObject handle = context.getContentCache().getHandle(wrapper.getCacheId(), wrapper, context.getContentAttributes());
        if (handle != null) {
            wrapper.setCacheHandle(handle);
            ccw = wrapper;
        }
        return ccw;
    }

    /**
     * {@inheritDoc}
     */
    public ACL[] getContentACLs(ContentId cid) throws ContentException {
        try {
            return MySQLContentACL.getContentACLs(cid, context.getPermanentConnection());
        } catch (SQLException e) {
            throw new ContentException(e);
        }
    }

    /**
     * @param cam cam
     * @param content content
     * @return boolean
     * @throws ContentException a generic exception
     */
    private boolean loadObjectContent(ContentAccessMode cam, Content content) throws ContentIOException {
        try {
            Connection conn = context.getPermanentConnection();
            boolean ok = MySQLContentReader.loadObjectContent(context, cam, conn, content);
            return ok;
        } catch (SQLException e) {
            throw new ContentIOException(e);
        }
    }

    /**
     * {@inheritDoc}
     */
    private boolean loadAggregationContent(ContentAccessMode cam, Content content) throws SQLException, SourceException, QueryException {
        boolean ok = false;
        ok = MySQLContentReader.loadAggregationContent(context, cam, context.getPermanentConnection(), content);
        return ok;
    }

    /**
     * @param cam cam
     * @param content content
     * @return boolean
     * @throws ContentIOException a generic exception
     */
    public boolean loadListContent(ContentAccessMode cam, Content content) throws ContentIOException {
        boolean ok = false;
        ListContent listcontent = (ListContent) content;
        EwListContentId lcid = (EwListContentId) listcontent.getId();
        ok = MySQLContentReadList.loadListContentResultsArray(context, cam, lcid, listcontent);
        return ok;
    }

    /**
     * @param cam cam
     * @param content content
     * @return boolean
     * @throws ContentIOException a generic exception
     */
    private boolean loadForeignKeyContent(ContentAccessMode cam, Content content) throws ContentIOException {
        boolean ok = false;
        ForeignKeyContent fkct = (ForeignKeyContent) content;
        ForeignKeyContentId fkctId = (ForeignKeyContentId) fkct.getId();
        if (fkctId.getKeyValue() == null) {
            if (fkctId.getLabelValue() == null) {
                ok = (MySQLContentForeignKeys.getForeignKeyValues(context, cam, fkct) != null);
            } else {
                ok = MySQLContentForeignKeys.getForeignKeyValue(context, cam, fkct);
            }
        } else {
            ok = MySQLContentForeignKeys.getForeignKeyLabel(context, cam, fkct);
        }
        return ok;
    }

    /**
     * @param cam cam
     * @param content content
     * @return boolean
     * @throws Exception a generic exception
     */
    private boolean loadChildrenContent(ContentAccessMode cam, Content content) throws ContentIOException {
        boolean ok = false;
        ChildrenContent childrencontent = (ChildrenContent) content;
        ChildrenContentId childrencntid = (ChildrenContentId) content.getId();
        EwListContentId lcid = (EwListContentId) childrencntid.getListContentId();
        ok = MySQLContentReadList.loadListContentResultsArray(context, cam, lcid, childrencontent);
        return ok;
    }

    /**
     * {@inheritDoc}
     */
    public void updateContent(AtlantalRequest request, ContentWrapper contentW, ActionResults results) throws ContentException {
        ContentAccessMode cam = this.getContentAccessMode(request);
        if (!cam.isWorkingCopy()) {
            results.setState(ActionResults.CANCELED);
            results.addMessage(ERR_NOEDITMODE);
            return;
        }
        Session session = null;
        EwObjectContent obj = (EwObjectContent) contentW.getContent();
        ContentSource source = obj.getSourceObject();
        Workflow workflow = this.getContentSourceWorkflow(source, ContentSource.UPDATE);
        try {
            DataSource ds = dbpool.getDataSource();
            String storeDir = this.getContentDirPath(false);
            String tempDir = this.getTempDir("_content_").getAbsolutePath();
            session = new SessionInstance(ds, storeDir, tempDir);
            session.start();
            MySQLContentUpdate.updateObject(context, request, session, contentW, results);
            if (results.getState() != ActionResults.CANCELED) {
                if (workflow != null) {
                    results.setState(ActionResults.PENDING);
                }
                session.commit();
            } else {
                session.rollback();
            }
        } catch (AtlantalException e) {
            if (session != null) {
                session.rollback();
            }
            e.printStackTrace(System.out);
            results.setState(ActionResults.ERROR);
            results.addMessage("Erreur lors de la modification du contenu" + " [" + obj.getId() + "]");
        } finally {
            if (session != null) {
                session.stop();
            }
        }
        switch(results.getState()) {
            case ActionResults.PENDING:
                try {
                    MySQLContentWriter.startContentWorkflow(context, request, workflow, obj, results);
                    results.addMessage("Proc�dure en cours...");
                } catch (WorkflowException e) {
                    throw new ContentException(e);
                }
                break;
            case ActionResults.OK:
                ActionResultsItem ari = new ActionResultsItem();
                ari.setMessage("Le contenu [" + obj.getId() + "] a �t� modifi�");
                results.addMessage(ari);
                EwTemplateChild tplchild = (EwTemplateChild) obj.getSourceObject();
                if (tplchild.isAutoPublish()) {
                    this.publishContent(request, contentW, results);
                }
                break;
            case ActionResults.CANCELED:
                results.addMessage("Aucune modification n'a �t� apport�e" + " au contenu [" + obj.getId() + "]");
                break;
            case ActionResults.ERROR:
                break;
            default:
        }
    }

    /**
     * {@inheritDoc}
     */
    public void insertContent(AtlantalRequest request, ContentWrapper contentW, ActionResults results) throws ContentException {
        ContentAccessMode cam = this.getContentAccessMode(request);
        if (!cam.isWorkingCopy()) {
            results.setState(ActionResults.CANCELED);
            results.addMessage(ERR_NOEDITMODE);
            return;
        }
        Exception error = null;
        EwObjectContent obj = (EwObjectContent) contentW.getContent();
        Session session = null;
        try {
            DataSource ds = dbpool.getDataSource();
            String storeDir = this.getContentDirPath(false);
            String tempDir = this.getTempDir("_content_").getAbsolutePath();
            session = new SessionInstance(ds, storeDir, tempDir);
            session.start();
            MySQLContentInsert.insertObject(context, request, session, contentW, results);
            if (results.getState() != ActionResults.CANCELED) {
                session.commit();
                this.afterContentInsertOk(request, obj);
            } else {
                session.rollback();
            }
        } catch (ContentIOException e) {
            if (session != null) {
                session.rollback();
            }
            error = e;
        } catch (SessionException e) {
            if (session != null) {
                session.rollback();
            }
            error = e;
        } finally {
            if (session != null) {
                session.stop();
            }
        }
        if (error != null) {
            error.printStackTrace(System.out);
            results.setState(ActionResults.ERROR);
        }
        switch(results.getState()) {
            case ActionResults.PENDING:
                results.addMessage("Proc�dure en cours...");
                break;
            case ActionResults.OK:
                results.addMessage("Le contenu [" + obj.getId() + "] a �t� cr��");
                EwTemplateChild tplchild = (EwTemplateChild) obj.getSourceObject();
                if (tplchild.isAutoPublish()) {
                    this.publishContent(request, contentW, results);
                }
                break;
            case ActionResults.CANCELED:
                break;
            case ActionResults.ERROR:
                StringBuilder msg = new StringBuilder();
                msg.append("Cr�ation du contenu impossible");
                if (error != null) {
                    msg.append(" - ").append(error.getMessage());
                }
                results.addMessage(msg.toString());
                break;
            default:
        }
    }

    /**
     * -- Utilis� dans les scripts BeanShell --
     * {@inheritDoc}
     */
    public void insertContent(AtlantalRequest request, Session session, ContentWrapper contentW, ActionResults results) throws ContentIOException {
        try {
            int branch = contentW.getContentAccessMode().getBranch();
            MySQLContentInsert.insertObject(context, request, session, contentW, results);
            MySQLContentPublisher.publishObjectTo(context, session.getConnection(), contentW, branch);
        } catch (SQLException e) {
            results.addMessage("Ajout impossible - " + e.getMessage());
            throw new ContentIOException(e);
        } catch (ContentException e) {
            results.addMessage("Ajout impossible - " + e.getMessage());
            throw new ContentIOException(e);
        }
    }

    /**
     * {@inheritDoc}
     */
    public void cloneContent(AtlantalRequest request, ContentWrapper contentW, ActionResults results) throws ContentException {
        ContentAccessMode cam = this.getContentAccessMode(request);
        if (!cam.isWorkingCopy()) {
            results.setState(ActionResults.CANCELED);
            results.addMessage(ERR_NOEDITMODE);
            return;
        }
        EwObjectContent obj = (EwObjectContent) contentW.getContent();
        Integer oldParentId = (Integer) obj.getId().getId();
        Session session = null;
        try {
            DataSource ds = dbpool.getDataSource();
            String storeDir = this.getContentDirPath(false);
            String tempDir = this.getTempDir("_content_").getAbsolutePath();
            session = new SessionInstance(ds, storeDir, tempDir);
            session.start();
            MySQLContentInsert.insertObject(context, request, session, contentW, results);
            MySQLContentWriter.duplicateChildren(context, request, session, oldParentId, contentW, results);
            session.commit();
        } catch (SessionException e) {
            if (session != null) {
                session.rollback();
            }
            e.printStackTrace(System.out);
            results.setState(ActionResults.ERROR);
            results.addMessage("Duplication du contenu [" + oldParentId + "]" + " impossible - " + e.getMessage());
        } catch (ContentException e) {
            if (session != null) {
                session.rollback();
            }
            e.printStackTrace(System.out);
            results.setState(ActionResults.ERROR);
            results.addMessage("Duplication du contenu [" + oldParentId + "]" + " impossible - " + e.getMessage());
        } finally {
            if (session != null) {
                session.stop();
            }
        }
        switch(results.getState()) {
            case ActionResults.PENDING:
                results.addMessage("Proc�dure en cours...");
                break;
            case ActionResults.OK:
                results.addMessage("Contenu [" + oldParentId + "] dupliqu�" + " vers le contenu [" + obj.getId() + "]");
                EwTemplateChild tplchild = (EwTemplateChild) obj.getSourceObject();
                if (tplchild.isAutoPublish()) {
                    this.publishContent(request, contentW, results);
                }
                break;
            case ActionResults.CANCELED:
                break;
            case ActionResults.ERROR:
                break;
            default:
        }
    }

    /**
     * {@inheritDoc}
     */
    public void deleteContent(AtlantalRequest request, ContentWrapper contentW, ActionResults result) throws ContentException {
        deleteContent(request, null, contentW, result);
    }

    /**
     * {@inheritDoc}
     */
    private void deleteContent(AtlantalRequest request, Session session, ContentWrapper object, ActionResults results) throws ContentException {
        EwObjectContent obj = (EwObjectContent) object.getContent();
        ContentSource source = obj.getSourceObject();
        Workflow workflow = this.getContentSourceWorkflow(source, ContentSource.DELETE);
        try {
            if (workflow == null) {
                if (session == null) {
                    deleteContentReal(request, object, results);
                } else {
                    deleteContentReal(request, session, object, results);
                }
            } else {
                results.setState(ActionResults.PENDING);
            }
        } catch (Exception e) {
            e.printStackTrace(System.out);
            results.setState(ActionResults.ERROR);
            results.addMessage("Impossible de supprimer le contenu" + " [" + obj.getId() + "] - " + e.getMessage());
        }
        switch(results.getState()) {
            case ActionResults.PENDING:
                try {
                    MySQLContentWriter.startContentWorkflow(context, request, workflow, obj, results);
                    results.addMessage("Suppression du contenu" + " [" + obj.getId() + "] en cours...");
                } catch (WorkflowException e) {
                    throw new ContentException(e);
                }
                break;
            case ActionResults.OK:
                results.addMessage("Le contenu [" + obj.getId() + "]" + " a �t� supprim�");
                break;
            case ActionResults.CANCELED:
                break;
            case ActionResults.ERROR:
                break;
            default:
        }
    }

    /**
     * {@inheritDoc}
     */
    public void publishContentTo(AtlantalRequest request, ContentWrapper contentW, int tgtBranch, ActionResults results) throws ContentException {
        Exception error = null;
        Connection conn = null;
        try {
            conn = dbpool.getConnection();
            MySQLContentPublisher.mergeContentTo(context, conn, contentW, tgtBranch, results);
        } catch (Exception e) {
            error = e;
        } finally {
            DbUtils.closeQuietly(conn);
        }
        if (error != null) {
            throw new ContentException(error);
        }
    }

    /**
     * {@inheritDoc}
     */
    public void unbranchContent(AtlantalRequest request, ContentWrapper contentW, int branch, ActionResults results) throws ContentException {
        Exception error = null;
        Connection conn = null;
        try {
            conn = dbpool.getConnection();
            MySQLContentPublisher.unbranchContent(this.context, conn, contentW, branch, results);
        } catch (Exception e) {
            error = e;
        } finally {
            DbUtils.closeQuietly(conn);
        }
        if (error != null) {
            throw new ContentException(error);
        }
    }

    /**
     * {@inheritDoc}
     */
    public void deleteContentReal(AtlantalRequest request, ContentWrapper contentW, ActionResults results) throws ContentException {
        Session session = null;
        try {
            DataSource ds = dbpool.getDataSource();
            String storeDir = this.getContentDirPath(false);
            String tempDir = this.getTempDir("_content_").getAbsolutePath();
            session = new SessionInstance(ds, storeDir, tempDir);
            session.start();
            MySQLContentDelete.deleteAllVersions(context, session, request, contentW, results);
            session.commit();
        } catch (SessionException e) {
            if (session != null) {
                session.rollback();
            }
            throw new ContentException(e);
        } catch (ContentException e) {
            if (session != null) {
                session.rollback();
            }
            throw new ContentException(e);
        } catch (Exception e) {
            if (session != null) {
                session.rollback();
            }
            e.printStackTrace(System.out);
            throw new ContentException(e);
        } finally {
            if (session != null) {
                session.stop();
            }
        }
    }

    /**
     * {@inheritDoc}
     */
    public void deleteContentReal(AtlantalRequest request, Session session, ContentWrapper object, ActionResults result) throws ContentException {
        MySQLContentDelete.deleteAllVersions(context, session, request, object, result);
    }

    /**
     * {@inheritDoc}
     */
    public void deleteChildren(ObjectContent object, ActionResults result) throws ContentException {
        Session session = null;
        try {
            DataSource ds = dbpool.getDataSource();
            String storeDir = this.getContentDirPath(false);
            String tempDir = this.getTempDir("_content_").getAbsolutePath();
            session = new SessionInstance(ds, storeDir, tempDir);
            session.start();
            deleteChildren(session, object, result);
            session.commit();
        } catch (SessionException e) {
            if (session != null) {
                session.rollback();
            }
            e.printStackTrace(System.out);
        } catch (ContentException e) {
            if (session != null) {
                session.rollback();
            }
            e.printStackTrace(System.out);
        } finally {
            if (session != null) {
                session.stop();
            }
        }
    }

    /**
     * {@inheritDoc}
     */
    private void deleteChildren(Session session, ObjectContent object, ActionResults result) throws ContentException {
        MySQLContentDelete.deleteChildren(context, null, session, object.getId(), result);
    }

    /**
     * {@inheritDoc}
     */
    public void deleteChildren(AtlantalRequest request, Session conn, ContentAccessMode cam, ObjectContent obj, Filter filter, ActionResults result) throws ContentException {
        if (filter == null) {
            MySQLContentDelete.deleteChildren(context, request, conn, obj.getId(), result);
        } else {
            MySQLContentDelete.deleteChildren(context, request, conn, obj.getId(), filter, result);
        }
    }

    /**
     * {@inheritDoc}
     */
    public boolean createTemplateCache(EwTemplate template) throws ContentException {
        return MySQLContentCache.createTemplateCache(dbpool, template);
    }

    /**
     * {@inheritDoc}
     */
    public boolean refreshTemplateCache(EwTemplate template) throws ContentException {
        return MySQLContentCache.refreshTemplateCache(context, dbpool, template);
    }

    /**
     * {@inheritDoc}
     */
    public void setObjectOwner(AtlantalRequest request, EwObjectContent object, User owner) throws ContentException {
        if (object.getOwnerId().equals(request.getUserId())) {
            Connection conn = null;
            Statement stmt = null;
            boolean prevAutoCommit = true;
            try {
                conn = dbpool.getConnection();
                prevAutoCommit = conn.getAutoCommit();
                conn.setAutoCommit(false);
                stmt = conn.createStatement();
                ContentId objid = object.getId();
                StringBuilder sql = new StringBuilder();
                sql.append("UPDATE objects SET id_owner = ");
                sql.append(owner.getId());
                sql.append(" WHERE id = ").append(objid.getId());
                stmt.executeUpdate(sql.toString());
                sql = new StringBuilder();
                sql.append("UPDATE object_acl SET id_group = ");
                sql.append(owner.getGroupId());
                sql.append(" WHERE id_obj_acl = ").append(objid.getId());
                sql.append(" AND id_group = ");
                sql.append(request.getUser().getGroupId());
                stmt.executeUpdate(sql.toString());
                DbUtils.closeQuietly(stmt);
                SQL.setAutoCommitQuietly(conn, prevAutoCommit);
                DbUtils.commitAndCloseQuietly(conn);
            } catch (SQLException e) {
                e.printStackTrace(System.out);
                DbUtils.closeQuietly(stmt);
                SQL.setAutoCommitQuietly(conn, prevAutoCommit);
                DbUtils.rollbackAndCloseQuietly(conn);
                throw new ContentWriteException(e);
            }
        }
    }

    /**
     * {@inheritDoc}
     */
    public void publishContent(AtlantalRequest request, ContentWrapper contentW, ActionResults results) throws ContentException {
        ContentAccessMode cam = this.getContentAccessMode(request);
        if (cam.isWorkingCopy()) {
            EwObjectContent content = (EwObjectContent) contentW.getContent();
            if (content.isPublished()) {
                results.setState(ActionResults.CANCELED);
                results.addMessage("Le contenu a d�j� �t� publi�");
            } else {
                publishContentExec(request, contentW, results);
            }
        } else {
            results.setState(ActionResults.CANCELED);
            results.addMessage(ERR_NOEDITMODE);
        }
    }

    /**
     * {@inheritDoc}
     */
    private void publishContentExec(AtlantalRequest request, ContentWrapper contentW, ActionResults results) throws ContentException {
        EwObjectContent content = (EwObjectContent) contentW.getContent();
        Workflow workflow = this.getContentSourceWorkflow(content.getSourceObject(), ContentSource.PUBLISH);
        Exception error = null;
        Connection conn = null;
        boolean prevAutoCommit = true;
        try {
            conn = dbpool.getConnection();
            prevAutoCommit = conn.getAutoCommit();
            conn.setAutoCommit(false);
            boolean doPublication = false;
            if (workflow == null) {
                doPublication = true;
            } else {
                if (MySQLContentPublisher.checkTagId(content, conn)) {
                    doPublication = true;
                } else {
                    int tagId = MySQLContentPublisher.tagObject(contentW, conn);
                    content.setTagId(tagId);
                    doPublication = false;
                }
            }
            if (doPublication) {
                MySQLContentPublisher.publishContent(context, contentW, conn);
                results.setState(ActionResults.OK);
            } else {
                results.setState(ActionResults.PENDING);
            }
            SQL.setAutoCommitQuietly(conn, prevAutoCommit);
            DbUtils.commitAndCloseQuietly(conn);
        } catch (SQLException e) {
            SQL.setAutoCommitQuietly(conn, prevAutoCommit);
            DbUtils.rollbackAndCloseQuietly(conn);
            e.printStackTrace(System.out);
            error = e;
            results.setState(ActionResults.ERROR);
        }
        MySQLContentPublisher.publishContentFinish(context, request, content, results, workflow, error);
    }

    /**
     * {@inheritDoc}
     */
    public void unpublishContent(AtlantalRequest request, ContentWrapper contentW, ActionResults results) throws ContentException {
        ContentAccessMode cam = this.getContentAccessMode(request);
        if (!cam.isWorkingCopy()) {
            results.setState(ActionResults.CANCELED);
            results.addMessage(ERR_NOEDITMODE);
            return;
        }
        Exception error = null;
        Connection conn = null;
        try {
            conn = dbpool.getConnection();
            MySQLContentPublisher.unpublishObject(context, request.getUser(), conn, contentW);
        } catch (Exception e) {
            error = e;
        } finally {
            DbUtils.closeQuietly(conn);
        }
        if (error != null) {
            throw new ContentException(error);
        }
    }

    /**
     * {@inheritDoc}
     */
    public File getFile(String filename, boolean secure) {
        String dirPath = getContentDirPath(secure);
        return new File(dirPath + File.separator + filename);
    }

    /**
     * {@inheritDoc}
     */
    public String getFileVirtualPath(String filename, boolean secure) {
        return getContentDirVirtualPath(secure) + "/" + filename;
    }

    /**
     * @param secure secure
     * @param virtual virtual
     * @return path
     * @throws Exception a generic exception
     */
    private String getContentDirPath(boolean secure) {
        String sep = File.separator;
        String dirPath = sep + "upl" + sep + "content";
        return this.getRealPath(dirPath);
    }

    /**
     * @param secure secure
     * @param virtual virtual
     * @return path
     * @throws Exception a generic exception
     */
    private String getContentDirVirtualPath(boolean secure) {
        return APPBASEPATH + "/upl/content";
    }

    /**
     * {@inheritDoc}
     */
    public void addFile(FileInfo info, Session session) throws IOException {
        if (MySQLContentFile.addFile(session, info)) {
            session.createFile(info.getFile(), info.getFileName());
        }
    }

    /**
     * {@inheritDoc}
     */
    public void removeFile(FileInfo info, Session session) throws IOException {
        if (MySQLContentFile.removeFile(session, info)) {
            session.deleteFile(info.getFileName());
        }
    }

    /**
     * {@inheritDoc}
     */
    public void useFile(FileInfo info, Session session) throws IOException {
        MySQLContentFile.useFile(session, info);
    }

    /**
     * {@inheritDoc}
     */
    public boolean hasContentSourceRights(ContentSource objsource, ContentWrapper parentW, User user, int rights) throws ContentException {
        boolean ok = false;
        EwTemplateChild child = (EwTemplateChild) objsource;
        EwObjectContent parent = (EwObjectContent) parentW.getContent();
        if (parentW.isLoaded()) {
            if (user.getId().equals(parent.getOwnerId())) {
                ok = ((child.getInheritedRights() & rights) > 0);
            }
            if (!ok) {
                ok = hasContentSourceRights2(child, parent, user, rights);
            }
        }
        return ok;
    }

    /**
     * {@inheritDoc}
     */
    private boolean hasContentSourceRights2(EwTemplateChild child, EwObjectContent parent, User user, int rights) throws ContentException {
        boolean ok = false;
        Integer rootId = parent.getRootId();
        Integer contextId = parent.getContextId();
        StringBuilder sql = new StringBuilder(SQL_OBJSRC_RIGHTS);
        sql.append(" WHERE usergrouptree.id_child = ").append(user.getId());
        sql.append(" AND id_template_tree = ").append(child.getId());
        sql.append(" AND object_ctx_tree.id_child ");
        if (contextId.equals(rootId)) {
            sql.append("= ").append(contextId);
        } else {
            sql.append("IN (").append(contextId);
            sql.append(", ").append(rootId).append(")");
        }
        sql.append(" AND (rights & ").append(rights).append(") > 0");
        LOGGER.debug("TREEACL > " + sql);
        Connection conn = null;
        Statement stmt = null;
        ResultSet rs = null;
        try {
            conn = dbpool.getConnection();
            stmt = conn.createStatement();
            rs = stmt.executeQuery(sql.toString());
            ok = rs.next();
        } catch (SQLException e) {
            e.printStackTrace(System.out);
        } finally {
            DbUtils.closeQuietly(rs);
            DbUtils.closeQuietly(stmt);
            DbUtils.closeQuietly(conn);
        }
        return ok;
    }

    /**
     * {@inheritDoc}
     */
    public boolean checkUserContentSourceQuotas(ContentSource ctsrc, User user) throws ContentException {
        boolean ok = false;
        try {
            Connection conn = context.getPermanentConnection();
            ok = MySQLContentQuota.checkUserContentSourceQuotas(conn, ctsrc, user);
        } catch (SQLException e) {
            e.printStackTrace(System.out);
            throw new ContentException(e);
        }
        return ok;
    }

    /**
     * @param objsource objsource
     * @param action action
     * @return boolean
     * @throws ContentException a generic exception
     */
    public Workflow getContentSourceWorkflow(ContentSource objsource, int action) throws ContentException {
        EwTemplateChild child = (EwTemplateChild) objsource;
        return child.getWorkflow(action);
    }

    /**
     * {@inheritDoc}
     */
    public void beforeContentInsert(AtlantalRequest request, Session conn, Content object) throws ContentIOException {
        EwTemplateChild child = (EwTemplateChild) object.getSourceObject();
        child.beforeInsert(request, conn, this, object);
    }

    /**
     * {@inheritDoc}
     */
    public void beforeContentUpdate(AtlantalRequest request, Session conn, Content object) throws ContentIOException {
        try {
            EwTemplateChild child = (EwTemplateChild) object.getSourceObject();
            child.beforeUpdate(request, conn, this, object);
        } catch (Exception e) {
            e.printStackTrace(System.out);
            throw new ContentIOException(e);
        }
    }

    /**
     * {@inheritDoc}
     */
    public void beforeContentDelete(AtlantalRequest request, Content object) throws ContentException {
        EwTemplateChild child = (EwTemplateChild) object.getSourceObject();
        child.beforeDelete(request, object);
    }

    /**
     * {@inheritDoc}
     */
    public void afterContentInsert(AtlantalRequest request, Session conn, ContentWrapper contentW, ActionResults results) throws ContentIOException {
        Content content = contentW.getContent();
        EwTemplateChild child = (EwTemplateChild) content.getSourceObject();
        child.afterInsert(request, conn, this, contentW, results);
    }

    /**
     * {@inheritDoc}
     */
    public void afterContentInsertOk(AtlantalRequest request, Content content) throws ContentException {
        EwTemplateChild child = (EwTemplateChild) content.getSourceObject();
        child.afterInsertOk(request, this, content);
    }

    /**
     * {@inheritDoc}
     */
    public void afterContentUpdate(AtlantalRequest request, Session conn, Content object, ActionResults results) throws ContentIOException {
        EwTemplateChild child = (EwTemplateChild) object.getSourceObject();
        child.afterUpdate(request, conn, object, results);
    }

    /**
     * {@inheritDoc}
     */
    public void afterContentDelete(AtlantalRequest request, Session conn, Content object) throws ContentIOException {
        EwTemplateChild child = (EwTemplateChild) object.getSourceObject();
        child.afterDelete(request, conn, object);
    }

    /**
     * {@inheritDoc}
     */
    public Map getCurrencies() {
        if (currencies == null) {
            currencies = new LinkedHashMap();
            Connection conn = null;
            Statement stmt = null;
            ResultSet rs = null;
            try {
                String sql = "SELECT * FROM currency ORDER BY sort";
                conn = dbpool.getConnection();
                stmt = conn.createStatement();
                rs = stmt.executeQuery(sql);
                while (rs.next()) {
                    Currency currency = new Currency();
                    currency.setId(rs.getInt("id"));
                    currency.setCode(rs.getString("code"));
                    currency.setLabel(rs.getString("label"));
                    currency.setSymbol(rs.getString("symbol"));
                    currency.setTaux(rs.getFloat("taux"));
                    currencies.put(currency.getId(), currency);
                }
            } catch (SQLException e) {
                e.printStackTrace(System.out);
            } finally {
                DbUtils.closeQuietly(rs);
                DbUtils.closeQuietly(stmt);
                DbUtils.closeQuietly(conn);
            }
        }
        return currencies;
    }

    /**
     * TODO : Pour tester
     * @param request rundata
     * @param objectW object
     * @param results results
     * @throws ContentException a generic exception
     */
    public void deleteUnusedVersions(AtlantalRequest request, ContentWrapper objectW, ActionResults results) throws ContentException {
        Session session = null;
        try {
            DataSource ds = dbpool.getDataSource();
            String storeDir = this.getContentDirPath(false);
            String tempDir = this.getTempDir("_content_").getAbsolutePath();
            session = new SessionInstance(ds, storeDir, tempDir);
            session.start();
            MySQLContentDelete.deleteUnusedVersions(this, session, request, objectW, results);
            session.commit();
        } catch (SessionException e) {
            if (session != null) {
                session.rollback();
            }
            throw new ContentException(e);
        } catch (ContentException e) {
            if (session != null) {
                session.rollback();
            }
            throw new ContentException(e);
        } catch (Exception e) {
            if (session != null) {
                session.rollback();
            }
            e.printStackTrace(System.out);
            throw new ContentException(e);
        } finally {
            if (session != null) {
                session.stop();
            }
        }
    }
}
