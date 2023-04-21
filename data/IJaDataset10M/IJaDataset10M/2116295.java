package es.alvsanand.webpage.services.admin;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.cache.Cache;
import javax.cache.CacheException;
import javax.cache.CacheFactory;
import javax.cache.CacheManager;
import es.alvsanand.webpage.common.Globals;
import es.alvsanand.webpage.common.Logger;
import es.alvsanand.webpage.db.QueryBean;
import es.alvsanand.webpage.db.dao.DAOException;
import es.alvsanand.webpage.db.dao.cms.ArticleDAO;
import es.alvsanand.webpage.db.dao.cms.ArticleDAOImpl;
import es.alvsanand.webpage.db.dao.cms.TagDAO;
import es.alvsanand.webpage.db.dao.cms.TagDAOImpl;
import es.alvsanand.webpage.model.Article;
import es.alvsanand.webpage.model.ArticleVersion;
import es.alvsanand.webpage.model.Comment;
import es.alvsanand.webpage.model.Rating;
import es.alvsanand.webpage.model.Tag;
import es.alvsanand.webpage.services.ServiceException;

/**
 * This class implements the service Tag
 *
 * @author alvaro.santos
 * @date 18/11/2009
 *
 */
public class CmsAdminServiceImpl implements CmsAdminService {

    private static final Logger logger = new Logger(CmsAdminServiceImpl.class);

    private TagDAO tagDAO = new TagDAOImpl();

    private ArticleDAO articleDAO = new ArticleDAOImpl();

    public void saveOrUpdateTag(Tag tag) throws ServiceException {
        try {
            logger.debug("Saving tag[" + ((tag != null) ? tag : "") + "]");
            tagDAO.saveOrUpdateTagWithSameName(tag);
        } catch (DAOException e) {
            logger.error("Error saving tag", e);
            throw new ServiceException(e);
        }
    }

    public Tag getTag(Tag tag) throws ServiceException {
        try {
            logger.debug("Getting tag[" + ((tag != null) ? tag : "") + "]");
            return tagDAO.getTagWithSameName(tag);
        } catch (DAOException e) {
            logger.error("Error getting tag", e);
            throw new ServiceException(e);
        }
    }

    public int getTagCount(String name) throws ServiceException {
        try {
            logger.debug("Getting count of tag[" + ((name != null) ? name : "") + "]");
            return tagDAO.getTagCountByName(name);
        } catch (DAOException e) {
            logger.error("Error count of tag", e);
            throw new ServiceException(e);
        }
    }

    public int getTagCount() throws ServiceException {
        try {
            logger.debug("Getting count of tag");
            return tagDAO.getTagCountWithSameName();
        } catch (DAOException e) {
            logger.error("Error count of tag", e);
            throw new ServiceException(e);
        }
    }

    public void deleteTag(Tag tag) throws ServiceException {
        try {
            logger.debug("Deleting tag[" + ((tag != null) ? tag : "") + "]");
            tagDAO.deleteTagWithSameName(tag);
            removeActivatedArticlesFromCache();
        } catch (DAOException e) {
            logger.error("Error deleting tag", e);
            throw new ServiceException(e);
        }
    }

    public List<Tag> getTags(QueryBean queryBean) throws ServiceException {
        try {
            logger.debug("Getting tags[" + ((queryBean != null) ? queryBean : "") + "]");
            return tagDAO.getTagsWithSameName(queryBean);
        } catch (DAOException e) {
            logger.error("Error getting tags", e);
            throw new ServiceException(e);
        }
    }

    public void saveOrUpdateArticle(Article article) throws ServiceException {
        try {
            logger.debug("Saving article[" + ((article != null) ? article : "") + "]");
            articleDAO.saveOrUpdateArticle(article);
            removeActivatedArticlesFromCache();
        } catch (DAOException e) {
            logger.error("Error saving article", e);
            throw new ServiceException(e);
        }
    }

    public void saveOrUpdateArticles(List<Article> articleList) throws ServiceException {
        try {
            logger.debug("Saving state articles[" + ((articleList != null) ? articleList : "") + "]");
            articleDAO.saveOrUpdateArticles(articleList);
            removeActivatedArticlesFromCache();
        } catch (DAOException e) {
            logger.error("Error saving state articles", e);
            throw new ServiceException(e);
        }
    }

    public Article getArticle(Article article) throws ServiceException {
        try {
            logger.debug("Getting article[" + ((article != null) ? article : "") + "]");
            return articleDAO.getArticle(article);
        } catch (DAOException e) {
            logger.error("Error getting article", e);
            throw new ServiceException(e);
        }
    }

    public ArticleVersion getArticleVersion(ArticleVersion articleVersion) throws ServiceException {
        try {
            logger.debug("Getting articleVersion[" + ((articleVersion != null) ? articleVersion : "") + "]");
            return articleDAO.getArticleVersion(articleVersion);
        } catch (DAOException e) {
            logger.error("Error getting articleVersion", e);
            throw new ServiceException(e);
        }
    }

    public int getArticleCount(String name) throws ServiceException {
        try {
            logger.debug("Getting count of article[" + ((name != null) ? name : "") + "]");
            return articleDAO.getArticleCountByName(name);
        } catch (DAOException e) {
            logger.error("Error count of article", e);
            throw new ServiceException(e);
        }
    }

    public int getArticleCount() throws ServiceException {
        try {
            logger.debug("Getting count of article");
            return articleDAO.getArticleCount();
        } catch (DAOException e) {
            logger.error("Error count of article", e);
            throw new ServiceException(e);
        }
    }

    public void deleteArticle(Article article) throws ServiceException {
        try {
            logger.debug("Deleting article[" + ((article != null) ? article : "") + "]");
            articleDAO.deleteArticle(article);
            removeActivatedArticlesFromCache();
        } catch (DAOException e) {
            logger.error("Error deleting article", e);
            throw new ServiceException(e);
        }
    }

    public List<Article> getArticles(QueryBean queryBean) throws ServiceException {
        try {
            logger.debug("Getting articles[" + ((queryBean != null) ? queryBean : "") + "]");
            return articleDAO.getArticles(queryBean);
        } catch (DAOException e) {
            logger.error("Error getting articles", e);
            throw new ServiceException(e);
        }
    }

    private void removeActivatedArticleFromCache(Article article) {
        Cache cache;
        try {
            logger.debug("Removing activated article from cache.");
            CacheFactory cacheFactory = CacheManager.getInstance().getCacheFactory();
            cache = cacheFactory.createCache(Collections.emptyMap());
            {
                Map<Integer, List<Article>> articlesMap = (Map<Integer, List<Article>>) cache.get(Globals.ACTIVATED_ARTICLES_CACHE_NAME);
                if (articlesMap != null) {
                    List<Integer> offsetToDelete = new ArrayList<Integer>();
                    for (Integer offset : articlesMap.keySet()) {
                        List<Article> articles = articlesMap.get(offset);
                        if (articles != null) {
                            for (Article _article : articles) {
                                if (article.getIdArticle().equals(_article.getIdArticle())) {
                                    offsetToDelete.add(offset);
                                    break;
                                }
                            }
                        }
                    }
                    for (Integer offset : offsetToDelete) {
                        articlesMap.remove(offset);
                    }
                    cache.put(Globals.ACTIVATED_ARTICLES_CACHE_NAME, articlesMap);
                }
            }
            {
                Map<String, List<Article>> articlesMap = (Map<String, List<Article>>) cache.get(Globals.ACTIVATED_ARTICLES_BY_TAG_CACHE_NAME);
                if (articlesMap != null) {
                    List<String> tagNamesToDelete = new ArrayList<String>();
                    for (String tagName : articlesMap.keySet()) {
                        List<Article> articles = articlesMap.get(tagName);
                        if (articles != null) {
                            for (Article _article : articles) {
                                if (article.getIdArticle().equals(_article.getIdArticle())) {
                                    tagNamesToDelete.add(tagName);
                                    break;
                                }
                            }
                        }
                    }
                    for (String tagName : tagNamesToDelete) {
                        articlesMap.remove(tagName);
                    }
                    cache.put(Globals.ACTIVATED_ARTICLES_BY_TAG_CACHE_NAME, articlesMap);
                }
            }
            {
                Map<String, Article> articlesMap = (Map<String, Article>) cache.get(Globals.ACTIVATED_ARTICLES_BY_ID_CACHE_NAME);
                if (articlesMap != null) {
                    articlesMap = new HashMap<String, Article>();
                    articlesMap.remove(article.getIdArticle());
                    cache.put(Globals.ACTIVATED_ARTICLES_BY_ID_CACHE_NAME, articlesMap);
                }
            }
        } catch (CacheException cacheException) {
            logger.error("Error removing activated articles from cache.", cacheException);
        }
    }

    private void removeActivatedArticlesFromCache() {
        Cache cache;
        try {
            logger.debug("Removing activated articles from cache.");
            CacheFactory cacheFactory = CacheManager.getInstance().getCacheFactory();
            cache = cacheFactory.createCache(Collections.emptyMap());
            for (String cacheName : Globals.ARTICLE_CAHE_NAMES) {
                cache.remove(cacheName);
            }
        } catch (CacheException cacheException) {
            logger.error("Error removing activated articles from cache.", cacheException);
        }
    }

    public void saveOrUpdateComment(Comment comment) throws ServiceException {
        try {
            logger.debug("Saving comment[" + ((comment != null) ? comment : "") + "]");
            articleDAO.saveOrUpdateComment(comment);
            removeActivatedArticleFromCache(comment.getArticle());
        } catch (DAOException e) {
            logger.error("Error saving comment", e);
            throw new ServiceException(e);
        }
    }

    public void deleteComment(Comment comment) throws ServiceException {
        try {
            logger.debug("Deleting comment[" + ((comment != null) ? comment : "") + "]");
            articleDAO.deleteComment(comment);
            removeActivatedArticleFromCache(comment.getArticle());
        } catch (DAOException e) {
            logger.error("Error deleting comment", e);
            throw new ServiceException(e);
        }
    }

    public void saveOrUpdateRating(Rating rating) throws ServiceException {
        try {
            logger.debug("Saving rating[" + ((rating != null) ? rating : "") + "]");
            articleDAO.saveOrUpdateRating(rating);
            removeActivatedArticleFromCache(rating.getArticle());
        } catch (DAOException e) {
            logger.error("Error saving rating", e);
            throw new ServiceException(e);
        }
    }
}
