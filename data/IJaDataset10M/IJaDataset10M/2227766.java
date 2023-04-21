package jegg.type;

import java.util.Iterator;

/**
 * This class is used to hold a tree of related types.  The root of the tree
 * (usually class Object) is the most common type, and its children are the
 * next least derived types, etc.  Classes on different branches are not
 * directly related through inheritance, although they inherit from a 
 * common ancestor.
 */
public class TypeTree {

    private Node root = new Node(Object.class, null);

    public TypeTree() {
        super();
    }

    public Node find(Class c) {
        return root.findNearest(c);
    }

    public Node getRoot() {
        return root;
    }

    public void insert(Class c, Object cookie) {
        if (Object.class.equals(c)) {
            if (null == root.getCookie()) {
                root.setCookie(cookie);
            }
        } else {
            Node nd = new Node(c, cookie);
            if (!root.insert(nd)) {
                if (!nd.insert(root)) {
                    throw new IllegalStateException("unable to insert class " + c.getName());
                }
            }
        }
    }

    public Iterator iterator() {
        return new TypeTreeIterator(this, true);
    }
}
