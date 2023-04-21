package jgroups.html_parser;

import java.util.Enumeration;
import java.io.*;

/**
 * This class stores an HTML file in tree format. It can be constructed
 * from an HTMLTokenizer or a file name, in which case it will create its
 * own tokenizer. <p>
 * Once the HTML file has been parsed a number of search operations can
 * be performed. The nature of the searches are described below, but some
 * of their uses are highlighted here: <p>
 * <ul>
 * <li>Subtree - Finding all of the FORM elements within a BODY element.
 * <li>Sibling - Finding all the LI elements within the same UL element.
 * <li>All - Finding every occurence of the A element.
 * </ul>
 * There is also a context search, which performs a subtree search on the
 * specified element's parent. This can be thought of as a combination
 * between as sibling search and a subtree search.
 * @see HTMLTokenizer
 * @author <a href="http://www.strath.ac.uk/~ras97108/">David McNicol</a>
 */
public class HTMLTree {

    private HTMLNode root;

    /**
	 * Constructs a new HTMLTree using the tokens from the
	 * specified Enumeration.
	 */
    public HTMLTree(Enumeration e) {
        root = new HTMLNode(null, null, e);
    }

    /**
	* Constructs a new HTMLTree using an input stream
	*
	*/
    public HTMLTree(InputStream is) throws IOException {
        HTMLTokenizer ht = new HTMLTokenizer(is);
        root = new HTMLNode(null, null, ht.getTokens());
    }

    /**
	* Constructs a new HTMLTree using a byte array
	*/
    public HTMLTree(byte[] array) throws IOException {
        InputStream is = new ByteArrayInputStream(array);
        HTMLTokenizer ht = new HTMLTokenizer(is);
        root = new HTMLNode(null, null, ht.getTokens());
        is.close();
    }

    /**
	* Gets the root of the tree
	*/
    public HTMLNode getRoot() {
        return root;
    }

    /**
	 * Constructs a new HTMLTree using the tokens from the
	 * specified HTMLTokenizer.
	 * @param ht the source of the HTML tokens.
	 */
    public HTMLTree(HTMLTokenizer ht) {
        root = new HTMLNode(null, null, ht.getTokens());
    }

    /**
	 * Finds the first element with the specified name in the specified
	 * subtree.
	 * @param name the name of the element to search for.
	 * @param tree the subtree to search.
	 */
    public HTMLNode findInSubtree(String name, HTMLNode tree) {
        return find(name, tree, null, true, false);
    }

    /**
	 * Finds the next element after the specified one in the subtree.
	 * If the previous element is not in the subtree then nothing will
	 * be found.
	 * @param tree the subtree to search.
	 * @param prev a previously found element.
	 */
    public HTMLNode findNextInSubtree(HTMLNode tree, HTMLNode prev) {
        if (prev == null) return null;
        return find(prev.getName(), tree, prev, true, false);
    }

    /**
	 * Finds the first element with the specified name in the entire
	 * tree.
	 * @param name the name of the element to search for.
	 */
    public HTMLNode findInAll(String name) {
        return find(name, root, null, true, false);
    }

    /**
	 * Finds the next element with the same name as the one specified
	 * in the entire tree.
	 * @param prev the previously found element.
	 */
    public HTMLNode findNextInAll(HTMLNode prev) {
        if (prev == null) return null;
        return find(prev.getName(), prev.getParent(), prev, true, true);
    }

    /**
	 * Find the first element with the specified name in the specified
	 * element's context (that is, the elements parent's subtree).
	 * @param name the name of the element to search for.
	 * @param el the element whose context is to be searched.
	 */
    public HTMLNode findInContext(String name, HTMLNode el) {
        if (el == null) return null;
        return find(name, el.getParent(), null, true, false);
    }

    /**
	 * Find the next element with the same name as the specified one
	 * in the first element's context (that is, the first elements
	 * parent's subtree).
	 * If the previous element is not in the subtree then nothing
	 * will be found.
	 * @param el the element whose context is to be searched.
	 * @param the previously found element.
	 */
    public HTMLNode findNextInContext(HTMLNode el, HTMLNode prev) {
        if (el == null) return null;
        return find(el.getName(), el.getParent(), prev, true, false);
    }

    /**
	 * Finds the next element with the same name as the specified
	 * one amongst that elements siblings (that is, the elements
	 * parent's children).
	 * @param el the element whose siblings are to be searched.
	 */
    public HTMLNode findSibling(HTMLNode el) {
        if (el == null) return null;
        return find(el.getName(), el.getParent(), el, false, false);
    }

    /**
	 * Prints a string representation of the HTMLTree.
	 */
    public String toString() {
        return "HTMLTree[" + root + "]";
    }

    /**
	 * Generic find method which searches for a string in the given
	 * tree's children. However, the search will not start until the
	 * start element has been passed. The tree's grandchildren will
	 * be searched recursively if the <code>recursive</code> argument
	 * is true. The whole tree after the element will be searched
	 * if the <code>searchParent</code> argument is true. In this
	 * case the method will recurse back towards the root element.
	 */
    private HTMLNode find(String name, HTMLNode tree, HTMLNode start, boolean recursive, boolean searchParent) {
        Enumeration children;
        Object next;
        boolean searching;
        HTMLNode child;
        HTMLNode found;
        if (name == null || tree == null) return null;
        searching = (start == null);
        children = tree.getChildren();
        if (children == null) return null;
        while (children.hasMoreElements()) {
            next = children.nextElement();
            if (!(next instanceof HTMLNode)) continue;
            child = (HTMLNode) next;
            if (searching) {
                if (name.equalsIgnoreCase(child.getName())) return child;
                if (recursive) {
                    found = find(name, child, null, true, false);
                    if (found != null) return found;
                }
            } else {
                if (child == start) searching = true;
            }
        }
        if (searchParent) {
            HTMLNode parent = tree.getParent();
            if (parent == null) return null;
            return find(name, parent, tree, true, true);
        }
        return null;
    }
}
