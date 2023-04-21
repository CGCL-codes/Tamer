package org.ministone.mlets.cms.domain;

/**
 * @see org.ministone.mlets.cms.domain.ArticleDynData
 */
public class ArticleDynDataImpl extends org.ministone.mlets.cms.domain.ArticleDynData {

    /**
     * The serial version UID of this class. Needed for serialization.
     */
    private static final long serialVersionUID = -5883661911963763109L;

    /**
     * @see org.ministone.mlets.cms.domain.ArticleDynData#read()
     */
    @Override
    public void read() {
        int currentCount = super.getReadCount();
        super.setReadCount(++currentCount);
    }

    public void addComment() {
        int currentCommentCount = super.getCommentCount();
        super.setCommentCount(++currentCommentCount);
    }
}
