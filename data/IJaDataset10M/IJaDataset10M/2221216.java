package org.hyperimage.connector;

/**
 * <p>This class organises information regarding the repository structure.</p>
 * 
 * <p>There are three possible cases:
 * <ol>
 * <li>Element in hierarchy is a level, i.e. a branch in the tree
 * <ul><li>m_isLevel==true, m_hasChildren==true, m_hasPreview==false</li></ul></li>
 * <li>Element in hierarchy is a level, but is a leaf in the tree
 * <ul><li>m_isLevel==true, m_hasChildren==false, m_hasPreview==false</li></ul></li>
 * <li>Element in hierarchy is a leaf and contains binary data (e.g. an image)
 * <ul><li>m_isLevel==false, m_hasChildren==false, m_hasPreview=={true,false}</li></ul></li>
 * </ol>
 * </p>
 * 
 * <p>m_hasPreview is only relevant when dealing with binary data, e.g. an image, and when
 * a preview image exists for the image in question.</p>
 * 
 * @author Heinz-Günter Kuper
 */
public class HIHierarchyLevel {

    private String m_strURN;

    private String m_strDisplayName;

    private boolean m_isLevel;

    private boolean m_hasChildren;

    private boolean m_hasPreview;

    public HIHierarchyLevel() {
        m_strURN = "";
        m_strDisplayName = "";
        m_isLevel = false;
        m_hasChildren = false;
        m_hasPreview = false;
    }

    public String getURN() {
        return m_strURN;
    }

    public void setURN(String strURN) {
        m_strURN = strURN;
    }

    public String getDisplayName() {
        return m_strDisplayName;
    }

    public void setDisplayName(String strDisplayName) {
        m_strDisplayName = strDisplayName;
    }

    public boolean isLevel() {
        return m_isLevel;
    }

    public void setLevel(boolean isLevel) {
        this.m_isLevel = isLevel;
    }

    public boolean hasChildren() {
        return m_hasChildren;
    }

    public void setChildren(boolean hasChildren) {
        this.m_hasChildren = hasChildren;
    }

    public boolean hasPreview() {
        return m_hasPreview;
    }

    public void setPreview(boolean hasPreview) {
        this.m_hasPreview = hasPreview;
    }
}
