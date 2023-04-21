package org.logtime.blog.dao;

import java.util.List;
import org.logtime.blog.bean.Article;
import org.logtime.blog.util.PageModule;

/**
 * 
 * ArticleDao make dao for blog article
 * 
 * @version: 1.0
 * @author: sumin
 * @createdate: 2011-11-04
 */
public interface ArticleDao {

    /**
	 * Save or update an article
	 */
    public void save(Article article);

    /**
	 * Delete an article
	 * 
	 * @param id
	 *            article id
	 */
    public void delete(long id);

    /**
	 * Get article by id
	 * 
	 * @param id
	 *            article id
	 */
    public Article getById(long id);

    /**
	 * Get all articles
	 * 
	 * @param level
	 *            article level
	 */
    public PageModule<Article> getAll(String level, int page, int pageSize);

    /**
	 * Get articles by author
	 * 
	 * @param level
	 *            article level
	 * 
	 * @param authorID
	 *            article author ID
	 */
    public PageModule<Article> getByAuthor(String level, long authorID, int page, int pageSize);

    /**
	 * Get articles by search
	 * 
	 * @param level
	 *            article level
	 * 
	 * @param search
	 *            search param
	 */
    public PageModule<Article> getBySearch(String level, String search, int page, int pageSize);

    /**
	 * Get all article archives
	 * 
	 * @param level
	 *            article level
	 * 
	 */
    public List<String> getAllArchive(String level);

    /**
	 * Get articles by archive
	 * 
	 * @param level
	 *            article level
	 * 
	 * @param archive
	 *            article archive
	 * 
	 */
    public PageModule<Article> getByArchive(String level, String archive, int page, int pageSize);
}
