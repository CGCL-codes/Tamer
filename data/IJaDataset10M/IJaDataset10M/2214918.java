/**
 * 
 */
package info.burdigala.impl.cms;

import java.sql.Connection;
import java.sql.SQLException;

import javax.sql.DataSource;

import org.apache.commons.dbutils.DbUtils;

import net.sf.ehcache.CacheManager;
import net.sf.ehcache.Ehcache;
import net.sf.ehcache.Element;
import net.sf.ehcache.constructs.blocking.CacheEntryFactory;
import net.sf.ehcache.constructs.blocking.SelfPopulatingCache;

import info.burdigala.api.cms.Content;
import info.burdigala.api.cms.ContentCollection;
import info.burdigala.api.cms.ContentCollectionId;
import info.burdigala.api.cms.ContentId;
import info.burdigala.api.cms.ContentNode;
import info.burdigala.api.cms.ContentNodeId;
import info.burdigala.api.cms.ContentMedia;
import info.burdigala.api.cms.ContentNodeType;
import info.burdigala.api.cms.ContentNodeTypeManager;
import info.burdigala.api.cms.ContentSession;

/**
 * @author François
 *
 */
public class ContentRepositoryImpl extends ContentRepositoryAbstract {

    private DataSource dataSource;
    private Ehcache contentCache;
    private ContentNodeTypeManagerImpl nodeTypeManager;
    
    private ContentReaderImpl reader;
    private ContentWriterImpl writer;
    
    private int sessionIdCounter = Integer.MAX_VALUE;
    
    public ContentRepositoryImpl() {
        super();
    }

    public DataSource getDataSource() {
        return dataSource;
    }

    public void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public ContentNodeTypeManager getNodeTypeManager() {
        return nodeTypeManager;
    }

    public void setNodeTypeManager(ContentNodeTypeManager nodeTypeManager) {
        this.nodeTypeManager = (ContentNodeTypeManagerImpl)nodeTypeManager;
    }

    public Ehcache getContentCache() {
        return contentCache;
    }

    public void setContentCache(Ehcache cache) {
        CacheEntryFactory cacheFactory = new ContentCacheEntryFactory(this);
        SelfPopulatingCache spCache =
            new SelfPopulatingCache(cache, cacheFactory); 
        this.contentCache = spCache;
    }
    
    public CacheManager getCacheManager() {
        return this.contentCache.getCacheManager();
    }
    
    private synchronized int getNewSessionId() {
        return this.sessionIdCounter--;
    }
    
    public void init() {
        this.reader = new ContentReaderImpl(this.nodeTypeManager);        
        this.writer = new ContentWriterImpl(this.nodeTypeManager);
        ContentSession session = new ContentSessionImpl(this, 0);
        ContentCollectionId id =
            newContentCollectionId(session, "type:node_type");
        ContentCollection nodeTypesContent =
            (ContentCollection)this.getContent(session, id);
        this.nodeTypeManager.init(nodeTypesContent);
    }

    /* (non-Javadoc)
     * @see info.burdigala.api.cms.ContentRepository#login()
     */
    public ContentSession login() {
        int sessionId = this.getNewSessionId();
        ContentSession session = new ContentSessionImpl(this, sessionId);
        return session;
    }

    @Override
    public ContentCollection newContentCollection(ContentSession session) {
        ContentCollectionImpl content = new ContentCollectionImpl(session);
        return content;
    }

    @Override
    public ContentId newContentId(
            ContentSession session, ContentIdData idData) {
        ContentId id = null;
        if (idData instanceof ContentNodeIdData) {
            ContentNodeIdImpl nodeId = new ContentNodeIdImpl(session);
            nodeId.setData(idData);
            id = nodeId;
        }
        return id;
    }

    @Override
    public ContentCollectionId newContentCollectionId(
            ContentSession session, Object identifier) {
        ContentCollectionIdImpl id =
            new ContentCollectionIdImpl(session, identifier);
        return id;
    }

    public ContentNodeId newContentNodeId(ContentSession session) {
        ContentNodeId id = new ContentNodeIdImpl(session);
        return id;
    }

    public ContentNodeId newContentNodeId(
            ContentSession session, Object identifier) {
        ContentNodeId id = new ContentNodeIdImpl(session, identifier);
        return id;
    }

    protected ContentNode newContentNode(ContentSession session) {
        ContentNodeImpl content = new ContentNodeImpl(session);
        return content;
    }

    @Override
    public ContentNode newContentNode(ContentSession session, String typeName) {
        ContentNodeTypeImpl type =
            (ContentNodeTypeImpl)this.nodeTypeManager.getNodeType(typeName);
        ContentNodeImpl content = new ContentNodeImpl(session);
        ContentNodeIdImpl id = (ContentNodeIdImpl)newContentNodeId(session);
        content.setId(id);
        content.setPrimaryNodeType(type);
        ContentNodeDataImpl data = new ContentNodeDataImpl();
        data.setIdType(type.getId());
        content.setData(data);
        return content;
    }

    @Override
    public ContentNode getContentNode(ContentSession session, Object identifier) {
        ContentId id = this.newContentNodeId(session, identifier);
        return (ContentNode)getContent(session, id);
    }

    @Override
    public ContentCollection getContentCollection(
            ContentSession session, Object identifier) {
        ContentId id = this.newContentCollectionId(session, identifier);
        return (ContentCollection)getContent(session, id);
    }

    public Content getContent(ContentSession session, ContentId id) {
        ContentSessionData sessionData =
            ((ContentSessionImpl)session).getData();
        ContentIdData idData = ((ContentIdImpl)id).getData();
        ContentCacheKey ccKey = new ContentCacheKey(sessionData, idData);
        Element element = this.contentCache.get(ccKey);
        ContentData data = (ContentData)element.getValue();
        Content content = null;
        if (data != null) {
            content = initContent(session, id, data);
        }
        return content;
    }

    public Content getContent(
            ContentSession session, ContentIdData idData) {
        ContentSessionData sessionData =
            ((ContentSessionImpl)session).getData();
        ContentCacheKey ccKey = new ContentCacheKey(sessionData, idData);
        Element element = this.contentCache.get(ccKey);
        ContentData data = (ContentData)element.getValue();
        Content content = null;
        if (data != null) {
            ContentId id = this.newContentId(session, idData);
            content = initContent(session, id, data);
        }
        return content;
    }

    private Content initContent(
            ContentSession session, ContentId id, ContentData data) {
        Content content = null;
        if (id instanceof ContentNodeId) {
            ContentNodeImpl nodeContent =
                (ContentNodeImpl)this.newContentNode(session);
            int typeId = ((ContentNodeDataImpl)data).getIdType();
            ContentNodeType type = this.nodeTypeManager.getNodeType(typeId);
            nodeContent.setPrimaryNodeType(type);
            nodeContent.setId(id);
            nodeContent.setData(data);
            content = nodeContent;
        }
        if (id instanceof ContentCollectionId) {
            ContentCollectionImpl nodesContent =
                (ContentCollectionImpl)this.newContentCollection(session);
            nodesContent.setId(id);
            nodesContent.setData(data);
            content = nodesContent;
        }
        return content;
    }

    public void renderContent(
            ContentSession session, Content content, ContentMedia media) {
    }

    @Override
    public ContentData getContentData(
            ContentSessionData sessionData, ContentIdData idData) {
        ContentData data = null;
        Connection conn = null;
        try {
            conn = this.dataSource.getConnection();
            data = reader.readContent(sessionData, idData, conn);
        } catch (SQLException e) {
            e.printStackTrace(System.out);
        } finally {
            DbUtils.closeQuietly(conn);
        }
        return data;
    }

    @Override
    public void addContentNode(
            ContentSession session, Object parentIdentifier, ContentNode node) {
        Connection conn = null;
        try {
            conn = this.dataSource.getConnection();
            conn.setAutoCommit(false);
            writer.createContentNode(session, parentIdentifier, node, conn);
            conn.commit();
        } catch (SQLException e) {
            e.printStackTrace(System.out);
            try {
                conn.rollback();
            } catch (SQLException e2) {
                e2.printStackTrace(System.out);
            }            
        } finally {
            try {
                conn.setAutoCommit(true);
            } catch (SQLException e) {
                e.printStackTrace(System.out);
            }            
            DbUtils.closeQuietly(conn);
        }
    }

    @Override
    public void updateContentNode(ContentSession session, ContentNode node) {
        Connection conn = null;
        try {
            conn = this.dataSource.getConnection();
            conn.setAutoCommit(false);
            writer.updateContentNode(session, node, conn);
            conn.commit();
        } catch (SQLException e) {
            e.printStackTrace(System.out);
            try {
                conn.rollback();
            } catch (SQLException e2) {
                e2.printStackTrace(System.out);
            }            
        } finally {
            try {
                conn.setAutoCommit(true);
            } catch (SQLException e) {
                e.printStackTrace(System.out);
            }            
            DbUtils.closeQuietly(conn);
        }
    }

    @Override
    public void removeContentNode(ContentSession session, ContentNode node) {
        Connection conn = null;
        try {
            conn = this.dataSource.getConnection();
            conn.setAutoCommit(false);
            writer.removeContentNode(session, node, conn);
            conn.commit();
        } catch (SQLException e) {
            e.printStackTrace(System.out);
            try {
                conn.rollback();
            } catch (SQLException e2) {
                e2.printStackTrace(System.out);
            }            
        } finally {
            try {
                conn.setAutoCommit(true);
            } catch (SQLException e) {
                e.printStackTrace(System.out);
            }            
            DbUtils.closeQuietly(conn);
        }
}

    @Override
    public ContentNode getRootNode(ContentSession session) {
        ContentNodeImpl rootNode =
            (ContentNodeImpl)newContentNode(session, "root");
        ContentNodeIdImpl id = (ContentNodeIdImpl)rootNode.getId();
        id.setIdentifier("0");
        return rootNode;
    }
}
