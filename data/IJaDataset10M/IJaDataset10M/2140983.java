package org.ministone.mlets.user.vo;

/**
 * 
 */
public class ArticleDyn implements java.io.Serializable {

    /**
     * The serial version UID of this class. Needed for serialization.
     */
    private static final long serialVersionUID = 2494977982440096078L;

    public ArticleDyn() {
        this.readCount = 0;
        this.id = null;
    }

    public ArticleDyn(int readCount, java.lang.String id) {
        this.readCount = readCount;
        this.id = id;
    }

    /**
     * Copies constructor from other ArticleDyn
     *
     * @param otherBean, cannot be <code>null</code>
     * @throws java.lang.NullPointerException if the argument is <code>null</code>
     */
    public ArticleDyn(ArticleDyn otherBean) {
        this(otherBean.getReadCount(), otherBean.getId());
    }

    /**
     * Copies all properties from the argument value object into this value object.
     */
    public void copy(ArticleDyn otherBean) {
        if (otherBean != null) {
            this.setReadCount(otherBean.getReadCount());
            this.setId(otherBean.getId());
        }
    }

    private int readCount = 0;

    /**
     * 
     */
    public int getReadCount() {
        return this.readCount;
    }

    public void setReadCount(int readCount) {
        this.readCount = readCount;
    }

    private java.lang.String id;

    /**
     * 
     */
    public java.lang.String getId() {
        return this.id;
    }

    public void setId(java.lang.String id) {
        this.id = id;
    }
}
