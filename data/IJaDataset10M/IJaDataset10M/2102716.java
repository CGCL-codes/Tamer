package storage.framework;

import org.nodal.model.Node;
import org.nodal.nav.Path;
import org.nodal.util.NodeFilter;

public final class PathNode extends NodeFilter {

    private Path.Generator pathGen;

    private Path path;

    static PathNode createNode(Node node, Path path) {
        if (node == null) {
            return null;
        } else {
            return new PathNode(node, path);
        }
    }

    private PathNode(Node node, Path path) {
        super(node);
        this.path = path;
        this.pathGen = null;
    }

    static PathNode createNode(Node node, Path.Generator pathGen) {
        if (node == null) {
            return null;
        } else {
            return new PathNode(node, pathGen);
        }
    }

    private PathNode(Node node, Path.Generator pathGen) {
        super(node);
        this.path = null;
        this.pathGen = pathGen;
    }

    public Path path() {
        if (path != null) {
            return path;
        }
        path = pathGen.path();
        pathGen = null;
        return path;
    }

    public String toString() {
        return "Node{" + path().toString() + "}";
    }
}
