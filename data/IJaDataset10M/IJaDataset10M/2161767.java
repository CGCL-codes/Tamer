package storage.framework;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Iterator;
import org.nodal.Repository;
import org.nodal.filesystem.Directory;
import org.nodal.filesystem.Document;
import org.nodal.filesystem.DocumentFormat;
import org.nodal.model.MapNode;
import org.nodal.model.RecordNode;
import org.nodal.model.Node;
import org.nodal.model.NodeContent;
import org.nodal.model.TxnHandlerFilter;
import org.nodal.nav.Path;
import org.nodal.nav.Paths;
import org.nodal.nav.Path.Failure;
import org.nodal.type.NodeType;
import org.nodal.type.RecordType;
import org.nodal.util.ConstraintFailure;
import org.nodal.util.Name;
import org.nodal.util.Names;
import org.nodal.util.PropertyConstraintFailure;

/**
 * Implemention of the Directory document type given a Node of type Directory.
 * 
 * @author leei
 */
public class DirFromNode extends DocFromNode implements Directory {

    private Node mapNode;

    protected Directory.Editor editor;

    static DocumentFormat DIRLIST_FORMAT = DocumentFormat.Registry.get("text/x-nodal-dirlist");

    DirFromNode(Path path, NodeContent node) {
        super(path, node, DIRLIST_FORMAT);
    }

    private MapNode map() {
        if (content != null) {
            try {
                mapNode = (Node) content.getField(ROOT_N);
                if (mapNode == null) {
                    RecordType dirType = node.nodeType().asRecordType();
                    RecordNode.Editor editDoc = content.editRecord();
                    mapNode = editDoc.createNode((NodeType) dirType.fieldType(ROOT_N));
                    editDoc.setField(ROOT_N).setNode(mapNode);
                }
            } catch (ConstraintFailure e) {
                throw new RuntimeException(e);
            }
            content.addTxnHandler(new Handler());
            return mapNode.asMapNode();
        }
        return null;
    }

    public Document get(String name) {
        MapNode map = map();
        if (map != null) {
            try {
                Node node = map.value(name).getNode();
                Path newPath = Paths.createDirElementOp(name).applyTo(path());
                return reuseOrCreateDoc(newPath, node.content());
            } catch (Path.Failure e) {
                throw new RuntimeException("Non-Directory path for Directory.");
            }
        }
        return null;
    }

    public Iterator entries() {
        MapNode map = map();
        return (map == null ? null : map.properties());
    }

    public Directory asDirectory() {
        return this;
    }

    public Directory.Editor edit() {
        if (editor == null) {
            MapNode map = map();
            if (map != null) {
                MapNode.Editor mapEditor = map.editMap();
                if (mapEditor != null) {
                    editor = createEditor(mapEditor);
                }
            }
        }
        return editor;
    }

    protected Directory.Editor createEditor(MapNode.Editor mapEdit) {
        return new MyEditor(mapEdit);
    }

    protected class MyEditor implements Directory.Editor {

        MapNode.Editor editor;

        MyEditor(MapNode.Editor editor) {
            this.editor = editor;
        }

        public Directory createSubdirectory(String name) {
            try {
                NodeContent.Editor newNode = editor.createNode((NodeType) DIR_T);
                Path newPath = Paths.createDirElementOp(name).applyTo(path());
                editor.setValue(name).set(newNode);
                return createFromDocNodeAndFormat(newPath, newNode, format()).asDirectory();
            } catch (ConstraintFailure e) {
                throw new RuntimeException(e);
            } catch (Failure e) {
                throw new RuntimeException(e);
            }
        }

        public Document createDocument(String name, DocumentFormat format) {
            NodeContent.Editor newNode = editor.createNode((NodeType) DOC_T);
            try {
                Path newPath = Paths.createDirElementOp(name).applyTo(path());
                editor.setValue(name).set(newNode);
                return createFromDocNodeAndFormat(newPath, newNode, format);
            } catch (ConstraintFailure e) {
                throw new RuntimeException(e);
            } catch (Failure e) {
                throw new RuntimeException(e);
            }
        }

        public Document createDocument(String name, String mimeType) {
            DocumentFormat format = repository().documentFormat(mimeType);
            if (format != null) {
                return createDocument(name, format);
            }
            return null;
        }

        public Document link(String name, Document doc) {
            try {
                Path newPath = Paths.createDirElementOp(name).applyTo(path());
                return createFromDocNodeAndFormat(newPath, doc.docNode().content(), doc.format());
            } catch (Failure e) {
                throw new RuntimeException(e);
            }
        }

        public boolean unlink(String name) {
            try {
                Name nm = Names.getName(name);
                if (editor.getValue(nm) == null) {
                    return false;
                }
                editor.removeKey(nm);
                return true;
            } catch (PropertyConstraintFailure e) {
                throw new RuntimeException(e);
            }
        }

        public Document get(String name) {
            return DirFromNode.this.get(name);
        }

        public Iterator entries() {
            return DirFromNode.this.entries();
        }

        public Directory.Editor edit() {
            return this;
        }

        public Repository repository() {
            return DirFromNode.this.repository();
        }

        public Path path() {
            return DirFromNode.this.path();
        }

        public DocumentFormat format() {
            return DirFromNode.this.format();
        }

        public Node docNode() {
            return DirFromNode.this.docNode();
        }

        public Node nodeNamed(Name name) {
            return DirFromNode.this.nodeNamed(name);
        }

        public void write(OutputStream s) throws IOException {
            DirFromNode.this.write(s);
        }

        public Node root() {
            return DirFromNode.this.root();
        }

        public Node nodeNamed(String name) {
            return DirFromNode.this.nodeNamed(name);
        }

        public Directory asDirectory() {
            return DirFromNode.this;
        }
    }

    /**
   * Create an instance of DirFromNode.
   * 
   * @param path
   *          the Path of this new Directpry
   * @param node
   *          the "Document" Node for this Directory
   * @return a Directory wrapped around the Node
   */
    static DirFromNode createDir(Path path, NodeContent node) {
        return new DirFromNode(path, node);
    }

    class Handler extends TxnHandlerFilter {

        public void notifySetValue(NodeContent c, Object property, Object value) {
        }

        public void notifyRemove(NodeContent c, Object key) {
        }
    }

    public String toString() {
        return "Directory<" + path().toURLString() + ">";
    }
}
