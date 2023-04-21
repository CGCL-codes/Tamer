package com.jandan.persistence.sqlmapdao;

import java.sql.SQLException;
import java.util.List;
import org.springframework.orm.ibatis.support.SqlMapClientDaoSupport;
import com.jandan.persistence.iface.ArticleDao;
import com.jandan.ui.model.Article;

/**
 * ArticleDao接口实现类，使用Ibatis框架进行持久化
 * @author Gong Yong
 * @version 1.0
 * @see com.jandan.persistence.iface.ArticleDao
 */
public class ArticleSqlMapDao extends SqlMapClientDaoSupport implements ArticleDao {

    public List<Article> getLatestJandanArticleList(int n) {
        return this.getSqlMapClientTemplate().queryForList("Article.getAllArticleList", 0, n);
    }

    public Article getArticleByArticleID(long articleID) {
        return (Article) this.getSqlMapClientTemplate().queryForObject("Article.getArticleByArticleID", Long.valueOf(articleID));
    }

    public List<Article> getAllArticleList() {
        return this.getSqlMapClientTemplate().queryForList("Article.getAllArticleList");
    }

    public List<Article> getAllArticleList(int start, int limit) {
        return this.getSqlMapClientTemplate().queryForList("Article.getAllArticleList", start, limit);
    }

    public long insertArticle(Article article) {
        long articleID = 0;
        try {
            this.getSqlMapClient().startTransaction();
            this.getSqlMapClientTemplate().insert("Article.insertArticle", article);
            articleID = (Long) getSqlMapClientTemplate().queryForObject("Article.lastInsertArticleID");
            this.getSqlMapClient().commitTransaction();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                this.getSqlMapClient().endTransaction();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return articleID;
    }

    public void updateArticle(Article article) {
        this.getSqlMapClientTemplate().update("Article.updateArticle", article);
    }

    public void deleteArticle(long articleID) {
        this.getSqlMapClientTemplate().delete("Article.deleteArticle", Long.valueOf(articleID));
    }

    public int getTotalArticleCount() {
        return (Integer) getSqlMapClientTemplate().queryForObject("Article.getTotalArticleCount");
    }
}
