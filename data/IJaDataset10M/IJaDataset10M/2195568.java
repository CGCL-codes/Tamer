package net.sf.contrail.gaevfs;

import java.io.Serializable;
import java.util.Collection;
import org.apache.commons.vfs.FileName;
import org.apache.commons.vfs.FileObject;
import org.apache.commons.vfs.FileSystemException;
import org.apache.commons.vfs.FileSystemOptions;
import org.apache.commons.vfs.provider.AbstractFileSystem;
import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.EntityNotFoundException;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;

/**
 * A Google Datastore backed file system
 * @author Ted Stockwell
 */
@SuppressWarnings("unchecked")
public class GaeFileSystem extends AbstractFileSystem implements Serializable {

    private static final long serialVersionUID = 1L;

    public static final String DEFAULT_ROOT_NAME = "net.sf.contrail.gaevfs.root";

    /**
	 * Cache of GAE File Data
	 */
    DatastoreService _datastore;

    NodeDescriptor _root;

    /**
	 * @param rootName
	 * @param fileSystemOptions
	 * @throws FileSystemException 
	 */
    protected GaeFileSystem(FileName rootName, FileSystemOptions opts) throws FileSystemException {
        super(rootName, null, opts);
        _datastore = DatastoreServiceFactory.getDatastoreService();
        GaeFileSystemConfigBuilder configBuilder = GaeFileSystemConfigBuilder.getInstance();
        Key rootKey = configBuilder.getRootEntity(opts);
        boolean createRoot = configBuilder.getCreateRoot(opts);
        if (rootKey == null) rootKey = KeyFactory.createKey(null, NodeDescriptor.KIND, DEFAULT_ROOT_NAME);
        Entity entity = null;
        try {
            entity = _datastore.get(rootKey);
            _root = new NodeDescriptor(null, entity);
        } catch (EntityNotFoundException x) {
            if (!createRoot) throw new FileSystemException("Missing root entity:" + rootKey);
            _root = new NodeDescriptor(rootKey);
            _datastore.put(_root.toEntity());
        }
        long revision = configBuilder.getRevision(opts);
        boolean openWritable = configBuilder.getOpenWritable(opts);
        if (0 <= revision) {
        } else if (!openWritable) {
        } else {
        }
    }

    @Override
    protected FileObject createFile(FileName name) throws Exception {
        Key key = KeyFactory.createKey(_root.getKey(), NodeDescriptor.KIND, name.getRelativeName(name));
        NodeDescriptor node = null;
        try {
            Entity entity = _datastore.get(key);
            node = new NodeDescriptor(_root, entity);
        } catch (EntityNotFoundException x) {
        }
        if (node == null) node = new NodeDescriptor(key);
        return new GaeFileObject(this, name, node);
    }

    @Override
    protected void addCapabilities(Collection caps) {
        caps.addAll(GaeFileProvider.capabilities);
    }
}
