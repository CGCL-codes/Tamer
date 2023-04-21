package com.faceye.components.blog.service.controller;

import java.io.Serializable;
import java.util.List;
import org.apache.commons.collections.CollectionUtils;
import com.faceye.components.blog.dao.iface.IArticleDao;
import com.faceye.components.blog.dao.model.Article;
import com.faceye.components.blog.dao.model.ArticleClickCount;
import com.faceye.components.blog.dao.model.Discus;
import com.faceye.components.blog.service.iface.IArticleService;
import com.faceye.core.componentsupport.service.controller.DomainService;
import com.faceye.core.util.helper.PaginationSupport;

public class ArticleService extends DomainService implements IArticleService {

    private IArticleDao articleDao = null;

    public IArticleDao getArticleDao() {
        return articleDao;
    }

    public void setArticleDao(IArticleDao articleDao) {
        this.articleDao = articleDao;
    }

    public PaginationSupport getArticlesByUserId(Serializable userId, int startIndex, int pageSize) {
        return this.getArticleDao().getArticlesByUserId(userId, startIndex, pageSize);
    }

    public PaginationSupport getArticlesByUserIdAndArticleCategoryId(Serializable userId, Serializable articleCategoryId, int startIndex, int pageSize) {
        return this.getArticleDao().getArticlesByUserIdAndArticleCategoryId(userId, articleCategoryId, startIndex, pageSize);
    }

    public void remove(Serializable id) {
        String hql = "from " + Discus.class.getName() + " d where d.article.id=?";
        String hql2 = "from " + ArticleClickCount.class.getName() + " a where a.article.id=?";
        List items1 = this.getArticleDao().getAllByHQL(hql, id);
        List items2 = this.getArticleDao().getAllByHQL(hql2, id);
        if (CollectionUtils.isNotEmpty(items1)) {
            this.getArticleDao().removeAllObjects(items1);
        }
        if (CollectionUtils.isNotEmpty(items2)) {
            this.getArticleDao().removeAllObjects(items2);
        }
        Article article = (Article) this.getArticleDao().getObject(Article.class, id);
        if (null != article) {
            this.getArticleDao().removeObject(article);
        }
    }

    public PaginationSupport getNewerArticleOrderList(int startIndex, int pageSize) {
        return this.getArticleDao().getNewerArticleOrderList(startIndex, pageSize);
    }
}
