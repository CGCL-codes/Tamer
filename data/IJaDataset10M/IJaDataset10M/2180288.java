package pl.org.waff.repository;

import com.google.appengine.api.datastore.DatastoreFailureException;
import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.EntityNotFoundException;
import com.google.appengine.api.datastore.FetchOptions;
import com.google.appengine.api.datastore.KeyFactory;
import com.google.appengine.api.datastore.PreparedQuery;
import com.google.appengine.api.datastore.Query;
import com.google.appengine.api.datastore.Query.SortDirection;
import java.util.ArrayList;
import java.util.Collections;
import java.util.ConcurrentModificationException;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;
import java.util.logging.Logger;

/**
 *
 * @author greg
 */
public class DefaultContentCache implements ContentCache {

    private static final Logger log = Logger.getLogger(DefaultContentCache.class.getName());

    public void init() {
        DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
        Entity rootFolder;
        Query q = new Query("folder").setKeysOnly();
        q.addFilter("path", Query.FilterOperator.EQUAL, "/");
        List<Entity> folders = datastore.prepare(q).asList(FetchOptions.Builder.withDefaults());
        if (folders.isEmpty()) {
            rootFolder = new Entity("folder");
            rootFolder.setProperty("path", "/");
            rootFolder.setProperty("parent", "/");
            datastore.put(rootFolder);
        }
        ;
        q = new Query("type").setKeysOnly();
        List<Entity> types = datastore.prepare(q).asList(FetchOptions.Builder.withDefaults());
        if (types.isEmpty()) {
            Entity type;
            type = new Entity("type");
            type.setProperty("name", "article");
            datastore.put(type);
            type = new Entity("type");
            type.setProperty("name", "image");
            datastore.put(type);
            type = new Entity("type");
            type.setProperty("name", "map");
            datastore.put(type);
            type = new Entity("type");
            type.setProperty("name", "video");
            datastore.put(type);
            type = new Entity("type");
            type.setProperty("name", "file");
            datastore.put(type);
        }
        ;
    }

    public void addFolder(String folderPath) {
        if (folderPath == null) {
            return;
        }
        if (!folderPath.startsWith("/")) {
            folderPath = "/" + folderPath;
        }
        String parentPath = null;
        if (folderPath.length() > 1) {
            parentPath = folderPath.substring(0, folderPath.lastIndexOf("/"));
        }
        DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
        Query q = new Query("folder").setKeysOnly();
        q.addFilter("path", Query.FilterOperator.EQUAL, folderPath);
        List<Entity> folders = datastore.prepare(q).asList(FetchOptions.Builder.withDefaults());
        if (folders.isEmpty()) {
            Entity folder = new Entity("folder");
            folder.setProperty("path", folderPath);
            folder.setProperty("parent", parentPath);
            datastore.put(folder);
        } else {
        }
    }

    public void addType(String typeName) {
        if (typeName == null) {
            return;
        }
        DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
        Query q = new Query("type").setKeysOnly();
        q.addFilter("name", Query.FilterOperator.EQUAL, typeName);
        List<Entity> types = datastore.prepare(q).asList(FetchOptions.Builder.withDefaults());
        if (types.isEmpty()) {
            Entity type = new Entity("type");
            type.setProperty("name", typeName);
            datastore.put(type);
        } else {
        }
    }

    public void addTag(String tagName) {
        if (tagName == null) {
            return;
        }
        DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
        Query q = new Query("tag").setKeysOnly();
        q.addFilter("name", Query.FilterOperator.EQUAL, tagName);
        List<Entity> tags = datastore.prepare(q).asList(FetchOptions.Builder.withDefaults());
        if (tags.isEmpty()) {
            Entity tag = new Entity("tag");
            tag.setProperty("name", tagName);
            datastore.put(tag);
        } else {
        }
    }

    public ArrayList getAllTags() {
        DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
        Query q = new Query("tag");
        List<Entity> tags = datastore.prepare(q).asList(FetchOptions.Builder.withDefaults());
        ArrayList result = new ArrayList();
        Entity tag;
        for (int i = 0; i < tags.size(); i++) {
            tag = tags.get(i);
            result.add((String) tag.getProperty("name"));
        }
        return result;
    }

    public Document getDocument(String id) {
        Document document;
        DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
        try {
            Entity entity = datastore.get(KeyFactory.stringToKey(id));
            document = new Document(entity);
        } catch (EntityNotFoundException e) {
            document = null;
        }
        return document;
    }

    public Comment getComment(String id) {
        Comment comment;
        DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
        try {
            Entity entity = datastore.get(KeyFactory.stringToKey(id));
            comment = new Comment(entity);
        } catch (EntityNotFoundException e) {
            comment = null;
        }
        return comment;
    }

    public ArrayList findDocuments(String folderPath, String documentType, String status, String target) {
        return findDocuments(folderPath, documentType, status, target, null);
    }

    public ArrayList findDocuments(String folderPath, String documentType, String status, String target, String tag) {
        ArrayList ids = new ArrayList();
        ArrayList docs = new ArrayList();
        DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
        Query q = new Query("document");
        if (folderPath != null) {
            q.addSort("published", SortDirection.DESCENDING);
            q.addFilter("folder", Query.FilterOperator.EQUAL, folderPath);
        }
        if (documentType != null) {
            q.addFilter("type", Query.FilterOperator.EQUAL, documentType);
        }
        if (status != null) {
            q.addFilter("status", Query.FilterOperator.EQUAL, status);
        }
        if (target != null) {
            q.addFilter("target", Query.FilterOperator.EQUAL, target);
        }
        if (tag != null) {
            q.addFilter("taglist", Query.FilterOperator.EQUAL, tag);
        }
        PreparedQuery pq = datastore.prepare(q);
        try {
            for (Entity result : pq.asIterable()) {
                ids.add(KeyFactory.keyToString(result.getKey()));
                docs.add(result);
            }
            if (folderPath == null) {
                Collections.sort(docs, new PublishedComparator());
                ids = new ArrayList();
                for (int i = 0; i < docs.size(); i++) {
                    ids.add(KeyFactory.keyToString(((Entity) docs.get(i)).getKey()));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ids;
    }

    public Properties getCommentsInfo(String documentId) {
        Properties properties = new Properties();
        List list;
        DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
        Query q = new Query("comment");
        if (documentId != null) {
            properties.setProperty("document.id", documentId);
            q.addFilter("parent", Query.FilterOperator.EQUAL, documentId);
        }
        q.addSort("created", SortDirection.DESCENDING);
        PreparedQuery pq = datastore.prepare(q);
        list = pq.asList(FetchOptions.Builder.withDefaults());
        if (list.size() > 0) {
            properties.setProperty("counter", "" + list.size());
            Comment comment = new Comment((Entity) list.get(0));
            properties.setProperty("created", comment.getCreatedAsString("yyyy-MM-dd HH:mm"));
        } else {
            properties.setProperty("counter", "0");
            properties.setProperty("created", "");
        }
        return properties;
    }

    public ArrayList findComments(String documentId, String status) {
        ArrayList ids = new ArrayList();
        DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
        Query q = new Query("comment");
        q.addFilter("parent", Query.FilterOperator.EQUAL, documentId);
        if (status != null) {
            q.addFilter("status", Query.FilterOperator.EQUAL, status);
        }
        PreparedQuery pq = datastore.prepare(q);
        for (Entity result : pq.asIterable()) {
            ids.add(KeyFactory.keyToString(result.getKey()));
        }
        return ids;
    }

    public ArrayList findDocuments(String folderPath, String documentType, String queryString) {
        ArrayList ids = null;
        return ids;
    }

    public List getAllFolders() {
        ArrayList folders = new ArrayList();
        DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
        Query q = new Query("folder");
        q.addSort("path", SortDirection.ASCENDING);
        Iterator<Entity> it = datastore.prepare(q).asIterator(FetchOptions.Builder.withDefaults());
        Entity entity;
        while (it.hasNext()) {
            entity = it.next();
            folders.add((String) entity.getProperty("path"));
        }
        return folders;
    }

    public List getAllTypes() {
        ArrayList types = new ArrayList();
        DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
        Query q = new Query("type");
        q.addSort("name", SortDirection.ASCENDING);
        Iterator<Entity> it = datastore.prepare(q).asIterator(FetchOptions.Builder.withDefaults());
        Entity entity;
        while (it.hasNext()) {
            entity = it.next();
            types.add((String) entity.getProperty("name"));
        }
        return types;
    }

    public ArrayList getSubfolders(String parentPath) {
        ArrayList subfolders = new ArrayList();
        DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
        Query q = new Query("folder");
        q.addFilter("parent", Query.FilterOperator.EQUAL, parentPath);
        q.addSort("path", SortDirection.ASCENDING);
        Iterator<Entity> folders = datastore.prepare(q).asIterator(FetchOptions.Builder.withDefaults());
        Entity entity;
        while (folders.hasNext()) {
            entity = folders.next();
            subfolders.add((String) entity.getProperty("path"));
        }
        return subfolders;
    }

    public int putDocument(String folderPath, Document document) {
        if (document == null) {
            return ERR_DOCUMENT_IS_NULL;
        }
        if (document.getFolder() == null || document.getFolder().isEmpty()) {
            return ERR_PATH_IS_EMPTY;
        }
        if (document.getType() == null) {
            return ERR_TYPE_IS_NULL;
        }
        if (document.getTitle() == null || document.getTitle().isEmpty()) {
            document.setTitle("new " + document.getType());
        }
        DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
        Entity entity = document.toEntity();
        datastore.put(entity);
        document.setId(KeyFactory.keyToString(entity.getKey()));
        List tags = document.getTaglistAsList();
        String tagName;
        for (int i = 0; i < tags.size(); i++) {
            tagName = (String) tags.get(i);
            addTag(tagName);
        }
        return ERR_NO_ERROR;
    }

    public int putComment(Comment comment) {
        if (comment == null) {
            return ERR_DOCUMENT_IS_NULL;
        }
        if (comment.getTitle() == null || comment.getTitle().isEmpty()) {
            comment.setTitle("...");
        }
        DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
        Entity entity = comment.toEntity();
        datastore.put(entity);
        comment.setId(KeyFactory.keyToString(entity.getKey()));
        return ERR_NO_ERROR;
    }

    public int trashDocument(String id) {
        Document document;
        DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
        try {
            Entity entity = datastore.get(KeyFactory.stringToKey(id));
            entity.setProperty("status", Document.STATUS_TRASH);
            datastore.put(entity);
        } catch (EntityNotFoundException e) {
            document = null;
            return ERR_NOT_FOUND;
        }
        return ERR_NO_ERROR;
    }

    public int deleteDocument(String id) {
        DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
        try {
            datastore.delete(KeyFactory.stringToKey(id));
        } catch (IllegalArgumentException e) {
            return ERR_NOT_FOUND;
        } catch (ConcurrentModificationException e) {
            return ERR_NOT_FOUND;
        } catch (DatastoreFailureException e) {
            return ERR_NOT_FOUND;
        }
        return ERR_NO_ERROR;
    }

    public int deleteComment(String id) {
        DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
        try {
            datastore.delete(KeyFactory.stringToKey(id));
        } catch (IllegalArgumentException e) {
            return ERR_NOT_FOUND;
        } catch (ConcurrentModificationException e) {
            return ERR_NOT_FOUND;
        } catch (DatastoreFailureException e) {
            return ERR_NOT_FOUND;
        }
        return ERR_NO_ERROR;
    }

    public int deleteFolder(String path) {
        return ERR_NOT_IMPLEMENTED;
    }
}
