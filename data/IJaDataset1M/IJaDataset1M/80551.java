package mecca.tools;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * @author Shamsul Bahrin Abd Mutalib
 * @version 1.01
 */
public class Tree implements Serializable {

    private String id;

    protected String title;

    private String description;

    private List subtrees;

    private Tree parent;

    private boolean gotChild;

    private String url;

    private int level;

    private boolean bRoot;

    private int child_count = 0;

    /**
     *
     */
    public Tree() {
        this("");
    }

    /**
     *
     */
    public Tree(String title) {
        subtrees = new ArrayList();
        this.title = title;
        String id = "0";
    }

    /**
     *
     */
    public Tree(String title, String url) {
        this(title);
        this.url = url;
    }

    /**
     *
     */
    public void add(Tree tree) {
        tree.setParent(this);
        tree.setLevel(this.getLevel() + 1);
        subtrees.add(tree);
        gotChild = true;
        child_count++;
    }

    /**
     *
     */
    public Iterator getChildIterator() {
        return subtrees.iterator();
    }

    /**
     *
     */
    public List getChildList() {
        return subtrees;
    }

    /**
     *
     */
    public Tree getChildAt(int i) {
        return (Tree) subtrees.get(i);
    }

    /**
     * Set an identification string for this tree instance.
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     * Return and identification string for this tree instance.
     */
    public String getId() {
        return id;
    }

    /**
     * Set a title string for this tree instance.
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * Return a title string for this tree instance.
     */
    public String getTitle() {
        return title;
    }

    /**
     * Set a description string for this tree instance.
     */
    public void setDescription(String descr) {
        this.description = descr;
    }

    /**
     * Return a description string for this tree instance.
     */
    public String getDescription() {
        return description;
    }

    /**
     * Set a parent tree for this tree instance.
     */
    public void setParent(Tree parent) {
        this.parent = parent;
    }

    /**
     * Return a parent tree for this tree instance.
     */
    public Tree getParent() {
        return parent;
    }

    /**
     * Return a boolean value indicating whether this tree instance has a child or not.
     * @return true		If this tree instance has childs.
     * @return false	If this tree instance does not has childs.
     */
    public boolean hasChild() {
        return gotChild;
    }

    /**
     * Set a url string for this tree instance.
     */
    public void setUrl(String url) {
        this.url = url;
    }

    /**
     * Return a url string for this tree instance.
     */
    public String getUrl() {
        return url;
    }

    /**
     * Return a level for this tree instance.
     * @return int level
     */
    public int getLevel() {
        return level;
    }

    public void setIsRoot(boolean b) {
        bRoot = true;
    }

    public boolean isRoot() {
        return bRoot;
    }

    public int getChildCount() {
        return child_count;
    }

    private void setLevel(int i) {
        this.level = i;
    }
}
