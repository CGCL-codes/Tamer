package net.sourceforge.javautil.web.library.richfaces;

import gracelets.util.ClosureUtil;
import groovy.lang.Closure;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;
import org.richfaces.model.TreeNode;
import org.richfaces.model.TreeNodeImpl;

/**
 * A tree model that will be used by a tree model closure factory.
 *
 * @author elponderador
 * @author $Author$
 * @version $Id$
 */
public class RichFacesTreeModelMap<T> implements TreeNode<T> {

    protected final Map<Object, Object> children;

    protected Map<Object, TreeNode<T>> nodes = new LinkedHashMap<Object, TreeNode<T>>();

    protected T data;

    protected TreeNode parent;

    public RichFacesTreeModelMap(Map<Object, Object> children) {
        this(null, children);
    }

    public RichFacesTreeModelMap(TreeNode parent, Map<Object, Object> children) {
        this.children = children;
        this.setParent(parent);
    }

    public void addChild(Object key, TreeNode<T> node) {
        children.put(key, node.getData());
        nodes.put(key, node);
    }

    public TreeNode<T> getChild(Object key) {
        if (!nodes.containsKey(key) && children.containsKey(key)) {
            TreeNode node = this.createNode(key);
            this.nodes.put(key, node);
        }
        if (this.nodes.get(key) == null) {
            System.out.println("Null value for : " + key);
        }
        return nodes.get(key);
    }

    public Iterator<Entry<Object, TreeNode<T>>> getChildren() {
        if (nodes.size() < children.size()) {
            for (Object key : children.keySet()) {
                nodes.put(key, this.createNode(key));
            }
        }
        return nodes.entrySet().iterator();
    }

    public T getData() {
        return this.data;
    }

    public TreeNode<T> getParent() {
        return this.parent;
    }

    public boolean isLeaf() {
        return this.children.size() == 0;
    }

    public void removeChild(Object key) {
        this.children.remove(key);
        this.nodes.remove(key);
    }

    public void setData(T data) {
        this.data = data;
    }

    public void setParent(TreeNode<T> node) {
        this.parent = node;
    }

    /**
	 * @param key The node to be created from the original model data
	 * @return A new tree node
	 */
    protected TreeNode createNode(Object key) {
        Object value = children.get(key);
        if (value instanceof Closure) value = ClosureUtil.call((Closure) value, key, this);
        if (value instanceof Map) {
            RichFacesTreeModelMap node = new RichFacesTreeModelMap(this, (Map) value);
            node.setData(key);
            return node;
        }
        if (value == null) {
            System.out.println("value is null for: " + key);
            return null;
        }
        TreeNode node = new TreeNodeImpl();
        node.setData(value);
        node.setParent(this);
        return node;
    }
}
