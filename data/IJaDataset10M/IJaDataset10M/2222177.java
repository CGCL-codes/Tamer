package com.integrationpath.mengine.service.impl;

import java.util.List;
import com.integrationpath.mengine.dao.AjaxUtilityDao;
import com.integrationpath.mengine.dao.UtilityDao;
import com.integrationpath.mengine.service.AjaxUtilityManager;
import com.integrationpath.mengine.service.ArticleManager;
import com.integrationpath.mengine.service.UtilityManager;

public class AjaxUtilityManagerImpl implements AjaxUtilityManager {

    private AjaxUtilityDao ajaxUtilityDao;

    public AjaxUtilityDao getAjaxUtilityDao() {
        return ajaxUtilityDao;
    }

    public void setAjaxUtilityDao(AjaxUtilityDao ajaxUtilityDao) {
        this.ajaxUtilityDao = ajaxUtilityDao;
    }

    public List getAllCategories() {
        return ajaxUtilityDao.getAllCategories();
    }

    public List getAllTags() {
        return ajaxUtilityDao.getAllTags();
    }

    public String changeArticleStatus(Long articleId) {
        return ajaxUtilityDao.changeArticleStatus(articleId).toString();
    }

    public String changeArticleFeatured(Long articleId) {
        return ajaxUtilityDao.changeArticleFeatured(articleId).toString();
    }

    public String changeCommentStatus(Long commentId) {
        return ajaxUtilityDao.changeCommentStatus(commentId).toString();
    }

    public String changeUserStatus(Long userId) {
        return ajaxUtilityDao.changeUserStatus(userId).toString();
    }
}
