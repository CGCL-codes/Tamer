package org.ministone.mlets.cms.domain;

/**
 * 
 *@author Sun Wenju
 *@since 0.1
 */
public abstract class ArticleData extends org.ministone.mlets.cms.domain.CmsElementDataImpl {

    /**
     * The serial version UID of this class. Needed for serialization.
     */
    private static final long serialVersionUID = 8093136043986223864L;

    private java.lang.String label;

    /**
     * <p>
     * ��ʾ����
     * </p>
     */
    public java.lang.String getLabel() {
        return this.label;
    }

    public void setLabel(java.lang.String label) {
        this.label = label;
    }

    private java.lang.String author;

    /**
     * 
     */
    public java.lang.String getAuthor() {
        return this.author;
    }

    public void setAuthor(java.lang.String author) {
        this.author = author;
    }

    private java.lang.String source;

    /**
     * 
     */
    public java.lang.String getSource() {
        return this.source;
    }

    public void setSource(java.lang.String source) {
        this.source = source;
    }

    private java.lang.String uri;

    /**
     * <p>
     * if it is url��the uri is the common url, if it is content ,then
     * uri starts with content:// ,and followed by stored path.
     * </p>
     */
    public java.lang.String getUri() {
        return this.uri;
    }

    public void setUri(java.lang.String uri) {
        this.uri = uri;
    }

    private java.lang.String keywords;

    /**
     * 
     */
    public java.lang.String getKeywords() {
        return this.keywords;
    }

    public void setKeywords(java.lang.String keywords) {
        this.keywords = keywords;
    }

    private java.lang.String brief;

    /**
     * 
     */
    public java.lang.String getBrief() {
        return this.brief;
    }

    public void setBrief(java.lang.String brief) {
        this.brief = brief;
    }

    private org.ministone.mlets.cms.domain.ArticleStatus articleStatus;

    /**
     * 
     */
    public org.ministone.mlets.cms.domain.ArticleStatus getArticleStatus() {
        return this.articleStatus;
    }

    public void setArticleStatus(org.ministone.mlets.cms.domain.ArticleStatus articleStatus) {
        this.articleStatus = articleStatus;
    }

    private org.ministone.mlets.cms.domain.ArticleDynData articleDynData;

    /**
     * 
     */
    public org.ministone.mlets.cms.domain.ArticleDynData getArticleDynData() {
        return this.articleDynData;
    }

    public void setArticleDynData(org.ministone.mlets.cms.domain.ArticleDynData articleDynData) {
        this.articleDynData = articleDynData;
    }

    /**
     * 
     */
    public abstract void enable();

    /**
     * 
     */
    public abstract void disable();

    /**
     * 
     */
    public abstract void audit();

    /**
     * This entity does not have any identifiers
     * but since it extends the <code>org.ministone.mlets.cms.domain.CmsElementDataImpl</code> class
     * it will simply delegate the call up there.
     *
     * @see org.ministone.mlets.cms.domain.CmsElementData#equals(Object)
     */
    public boolean equals(Object object) {
        return super.equals(object);
    }

    /**
     * This entity does not have any identifiers
     * but since it extends the <code>org.ministone.mlets.cms.domain.CmsElementDataImpl</code> class
     * it will simply delegate the call up there.
     *
     * @see org.ministone.mlets.cms.domain.CmsElementData#hashCode()
     */
    public int hashCode() {
        return super.hashCode();
    }

    /**
     * Constructs new instances of {@link org.ministone.mlets.cms.domain.ArticleData}.
     */
    public static final class Factory {

        /**
         * Constructs a new instance of {@link org.ministone.mlets.cms.domain.ArticleData}.
         */
        public static org.ministone.mlets.cms.domain.ArticleData newInstance() {
            return new org.ministone.mlets.cms.domain.ArticleDataImpl();
        }

        /**
         * Constructs a new instance of {@link org.ministone.mlets.cms.domain.ArticleData}, taking all required and/or
         * read-only properties as arguments.
         */
        public static org.ministone.mlets.cms.domain.ArticleData newInstance(org.ministone.mlets.cms.domain.ArticleStatus articleStatus, org.ministone.mlets.cms.domain.ArticleDynData articleDynData, java.lang.String title, java.util.Date createdTime, java.util.Date updatedTime, java.util.Date publishedTime, java.lang.String contentStatus) {
            final org.ministone.mlets.cms.domain.ArticleData entity = new org.ministone.mlets.cms.domain.ArticleDataImpl();
            entity.setArticleStatus(articleStatus);
            entity.setArticleDynData(articleDynData);
            entity.setTitle(title);
            entity.setCreatedTime(createdTime);
            entity.setUpdatedTime(updatedTime);
            entity.setPublishedTime(publishedTime);
            entity.setContentStatus(contentStatus);
            return entity;
        }

        /**
         * Constructs a new instance of {@link org.ministone.mlets.cms.domain.ArticleData}, taking all possible properties
         * (except the identifier(s))as arguments.
         */
        public static org.ministone.mlets.cms.domain.ArticleData newInstance(java.lang.String label, java.lang.String author, java.lang.String source, java.lang.String uri, java.lang.String keywords, java.lang.String brief, org.ministone.mlets.cms.domain.ArticleStatus articleStatus, java.lang.String title, java.util.Date createdTime, java.util.Date updatedTime, java.util.Date publishedTime, java.lang.String contentStatus, org.ministone.mlets.cms.domain.ArticleDynData articleDynData, java.util.Collection cmsResourceDatas, org.ministone.mlets.cms.domain.CmsCategoryData cmsCategoryData) {
            final org.ministone.mlets.cms.domain.ArticleData entity = new org.ministone.mlets.cms.domain.ArticleDataImpl();
            entity.setLabel(label);
            entity.setAuthor(author);
            entity.setSource(source);
            entity.setUri(uri);
            entity.setKeywords(keywords);
            entity.setBrief(brief);
            entity.setArticleStatus(articleStatus);
            entity.setTitle(title);
            entity.setCreatedTime(createdTime);
            entity.setUpdatedTime(updatedTime);
            entity.setPublishedTime(publishedTime);
            entity.setContentStatus(contentStatus);
            entity.setArticleDynData(articleDynData);
            entity.setCmsResourceDatas(cmsResourceDatas);
            entity.setCmsCategoryData(cmsCategoryData);
            return entity;
        }
    }
}
