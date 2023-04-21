package storage.framework;

import java.io.IOException;
import java.io.InputStream;
import java.net.FileNameMap;
import java.net.URLConnection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import org.nodal.Repository;
import org.nodal.filesystem.Document;
import org.nodal.filesystem.DocumentFormat;
import org.nodal.model.Node;
import org.nodal.model.NodeContent;
import org.nodal.model.NodeFactory;
import org.nodal.model.RecordNode;
import org.nodal.nav.Path;
import org.nodal.type.NodeType;
import org.nodal.util.ConstraintFailure;
import org.nodal.util.GraphIterator;
import org.nodal.util.Name;

/**
 * A Node that represents the root Node of a Document that is managed decoding
 * the contents of an InputStream. This is implemented as a proxy to a node that
 * is only created when necessary, given access requests to the Node's contents
 * or properties.
 * 
 * @author leei
 */
public class StreamBasedNode extends ProxyNode implements Node {

    private NodeContent node;

    private Loader loader;

    private DocumentFormat format;

    /**
   * StreamBasedDocument backend, to be provided by implementing class.
   * 
   * Created on Dec 3, 2003
   * 
   * @author leei
   */
    public interface Loader {

        /**
     * Notify this Loader that we are reloading a Document with known Node
     * identities.
     * 
     * @param reloading
     *          a boolean establishing whether or not we are reloading a
     *          Document with these Loader
     */
        void setReloading(boolean reloading);

        /**
     * The RepoBackend
     * 
     * @return the RepoBackend associated with this Document loading context
     */
        AbstractRepository.Backend repoBackend();

        /**
     * The path associated with this stream-based document.
     * 
     * @return
     */
        Path path();

        /**
     * An input stream for this particular path.
     * 
     * @return
     */
        InputStream inputStream();

        /**
     * The MIME type of this stream.
     * 
     * @return
     */
        String mimeType();

        /**
     * A factory for building Node instances in the context of this stream
     * loader.
     * 
     * @return
     */
        NodeFactory nodeFactory();

        /**
     * A method to rename Nodes produced by the sequence.
     * 
     * @author leei
     */
        boolean renameNode(Node node, Name nid);

        /**
     * A Map of Path->NID for all Nodes in this Document.
     * 
     * @return a Map of Path->NID for all Nodes in this Document
     */
        Map getNIDMap();

        /**
     * Save the Map of Path->NID for all of the Nodes in this Document.
     * 
     * @param nidMap
     */
        void saveNIDMap(Map nidMap);
    }

    /**
   * Load Document from a given StreamBasedNode.Loader.
   * 
   * @param loader
   *          a StreamBasedNode.Loader that can define a Document creation
   *          environment.
   */
    public static Document loadDocument(Loader loader) throws IOException {
        NodeContent.Editor node = StreamBasedNode.rootFromLoader(loader);
        DocumentFormat format = DocumentFormat.Registry.get(loader.mimeType());
        return DocFromNode.createFromDocNodeAndFormat(loader.path(), node, format);
    }

    /**
   * Create a Document.root Node that will be associated with a given Loader.
   * 
   * @param loader
   *          a StreamBasedNode.Loader that defines a Document loading
   *          environment
   * @return the root Node of the Document specified
   */
    public static RecordNode.Editor rootFromLoader(Loader loader) {
        try {
            RecordNode.Editor docNode = AbstractDocument.createDocumentNode(loader.nodeFactory(), loader.mimeType());
            Node root = new StreamBasedNode(loader);
            docNode.setField("root").setNode(root);
            return docNode;
        } catch (ConstraintFailure e) {
            return null;
        }
    }

    private StreamBasedNode(Loader loader) {
        this.loader = loader;
    }

    public Repository repository() {
        return loader.repoBackend().repository();
    }

    /**
   * An augmented FileNameMap is installed into the URLConnection class simply
   * by instantiating from this class.
   */
    private static FileNameMap myFileMap;

    private static class MyFileMap implements FileNameMap {

        Map extMap;

        FileNameMap systemMap;

        MyFileMap() {
            systemMap = URLConnection.getFileNameMap();
            extMap = new HashMap();
            extMap.put("nls", "text/x-nodal-schema");
            URLConnection.setFileNameMap(this);
        }

        public String getContentTypeFor(String arg0) {
            String contentType = null;
            int extIndex = arg0.lastIndexOf('.');
            if (extIndex >= 0) {
                String ext = arg0.substring(extIndex + 1);
                contentType = (String) extMap.get(ext);
            }
            if (contentType == null) {
                contentType = systemMap.getContentTypeFor(arg0);
            }
            return contentType;
        }
    }

    /**
   * Same as {@link java.net.URLConnection#guessContentTypeFromName(String)},
   * but after installing NODAL-specific mappings.
   * 
   * @param fname
   *          the name of the file
   * @return a suggested MIME type
   */
    public static String guessContentTypeFromName(String fname) {
        if (myFileMap == null) {
            myFileMap = new MyFileMap();
        }
        return URLConnection.guessContentTypeFromName(fname);
    }

    private DocumentFormat format() {
        if (format == null) {
            format = DocumentFormat.Registry.get(loader.mimeType());
        }
        return format;
    }

    /**
   * Load a document root Node from a stream. Checks to determine whether this
   * is a reload (in which case we must make an effort to restore previously
   * allocated Node ids) or a first time load (in which case Node ids are
   * created).
   * 
   * @return The contents of the root Node
   * @throws IOException
   *           if reading the stream produces one
   */
    protected NodeContent loadStream() throws IOException {
        DocumentFormat.Decoder dec = format().decoder();
        dec.setURI(loader.path().toURLString());
        InputStream in = loader.inputStream();
        try {
            Map nidMap = loader.getNIDMap();
            boolean restoring = (nidMap != null);
            loader.setReloading(restoring);
            NodeFactory factory = loader.nodeFactory();
            Node root = dec.decode(in, factory);
            if (root != null) {
                if (restoring) {
                    restoreNIDs(nidMap, root);
                } else {
                    saveNIDs(root);
                }
                return root.content();
            }
        } finally {
            in.close();
        }
        return null;
    }

    /**
   * Save a record of the Path->Node.id mapping. This is used to restore Node
   * ids when the Document is reloaded at a later date.
   * 
   * @param root
   *          the root Node of the Document
   * @throws IOException
   */
    private void saveNIDs(Node root) throws IOException {
        Map nidMap = new HashMap();
        GraphIterator iter = new GraphIterator(root);
        while (iter.hasNext()) {
            Path path = iter.nextPath();
            Node node = iter.nextNode();
            if (node.id().isGlobal()) {
                throw new IOException("Attempt to save reference to temporary Node " + node);
            }
            nidMap.put(path, node.id());
        }
        loader.saveNIDMap(nidMap);
    }

    /**
   * Restore the Node ids of a set of Nodes given the Path->Node.id mapping as
   * saved by {@link saveNIDs(Node)}.
   * 
   * @param nidMap
   *          the Map from Path->Node.id
   * @param root
   *          the root Node of the Document
   * @throws IOException
   */
    private void restoreNIDs(Map nidMap, Node root) throws IOException {
        GraphIterator iter = new GraphIterator(root);
        Map defer = null;
        while (iter.hasNext()) {
            Path path = iter.nextPath();
            Node node = iter.nextNode();
            Name nid = (Name) nidMap.get(path);
            if (nid == null) {
                if (defer == null) {
                    defer = new HashMap();
                }
                System.out.println("Defer rename " + path + " => " + node);
                defer.put(path, node);
            } else {
                loader.renameNode(node, nid);
                nidMap.remove(path);
            }
        }
        while (defer != null && !defer.isEmpty()) {
            Iterator i = defer.keySet().iterator();
            boolean changed = false;
            while (i.hasNext()) {
                Path path = (Path) i.next();
                Node node = (Node) defer.get(path);
                Name nid = (Name) nidMap.get(path);
                if (nid != null) {
                    loader.renameNode(node, nid);
                    nidMap.remove(path);
                    changed = true;
                    i.remove();
                }
            }
            if (!defer.isEmpty() && !changed) {
                throw new IOException("Unresolved Path->Node references in NIDMap.");
            }
        }
        if (!nidMap.isEmpty()) {
            throw new IOException("Unused nodes in NID map");
        }
    }

    /**
   * The target Node of this proxy. This StreamBasedNode will initially leave
   * the target Node unspecified and then create and initialize it only when
   * this method is called.
   */
    protected Node target() {
        if (node == null) {
            try {
                node = loadStream();
            } catch (IOException e) {
                ;
            }
        }
        return node;
    }

    public NodeType nodeType() {
        if (node != null) {
            return node.nodeType();
        } else {
            return format().rootType();
        }
    }
}
