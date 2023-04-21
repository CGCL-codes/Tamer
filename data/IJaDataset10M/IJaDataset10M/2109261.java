package org.ministone.arena.service;

/**
 * <p>
 * BlogArticle is only used in needed cases
 * </p>
 *@author Sun Wenju
 *@since 0.1 
 */
public interface BlogService {

    /**
     * <p>
     * the result isa collection of BlogArticle
     * </p>
     */
    public org.ministone.util.PagedObject queryArticles(org.ministone.arena.criteria.BlogArticleCriteria criteria);

    /**
     * 
     */
    public java.util.Collection getAllCmsCategories(java.lang.String blogId);

    /**
     * <p>
     * return the read article,the info piece of the article should
     * have been updated,such as the read counter and so on
     * </p>
     */
    public org.ministone.arena.vo.BlogArticle readArticle(java.lang.String id);

    /**
     * 
     */
    public org.ministone.arena.vo.BlogArticle getArticleById(java.lang.String id);

    /**
     * 
     */
    public void removeArticles(java.lang.String idlist);

    /**
     * 
     */
    public void createBlogArticle(org.ministone.arena.vo.BlogArticle blogArticle);

    /**
     * 
     */
    public void updateBlogArticle(org.ministone.arena.vo.BlogArticle blogArticle);

    /**
     * 
     */
    public void createStageCmsCategory(org.ministone.arena.vo.StageCmsCategory stageCmsCategory);

    /**
     * 
     */
    public void updateStageCmsCategory(org.ministone.arena.vo.StageCmsCategory stageCmsCategory);

    /**
     * 
     */
    public void commentArticle(java.lang.String articleId, org.ministone.mlets.cms.vo.Comment comment);

    /**
     * 
     */
    public void removeCmsCategories(java.lang.String idlist);
}
