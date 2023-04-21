package com.kongur.star.venus.dao.courses.impl;

import java.util.List;
import java.util.Map;
import org.springframework.stereotype.Repository;
import com.kongur.star.venus.common.page.Paginable;
import com.kongur.star.venus.dao.BaseDAO;
import com.kongur.star.venus.dao.courses.CommentsDAO;
import com.kongur.star.venus.domain.courses.CommentsDO;

@Repository("commentsDAO")
public class CommentsDAOImpl extends BaseDAO<CommentsDO> implements CommentsDAO {

    @Override
    public Long insertComments(CommentsDO commentsDO) throws Exception {
        return executeInsert("CommentsDAO.insertComments", commentsDO);
    }

    @Override
    public Integer updateComments(CommentsDO commentsDO) throws Exception {
        return executeUpdate("CommentsDAO.updateComments", commentsDO);
    }

    @Override
    public Integer expertCommitAudit(CommentsDO commentsDO) throws Exception {
        return executeUpdate("CommentsDAO.expertCommitAudit", commentsDO);
    }

    @Override
    public Integer rejectByExpertId(CommentsDO commentsDO) throws Exception {
        return executeUpdate("CommentsDAO.rejectByExpertId", commentsDO);
    }

    @Override
    public CommentsDO selectCommentsById(Long id) throws Exception {
        return null;
    }

    @Override
    public CommentsDO expertSelectComments(Map<String, Object> hashParam) throws Exception {
        return (CommentsDO) this.getSqlMapClientTemplate().queryForObject("CommentsDAO.expertSelectComments", hashParam);
    }

    @Override
    public List<CommentsDO> selectComments(CommentsDO commentsDO) throws Exception {
        return this.selectObject("CommentsDAO.selectComments", commentsDO);
    }

    @Override
    public Paginable<CommentsDO> selectCommentsForPagin(CommentsDO commentsDO, String qTotalCount, String qPagination) throws Exception {
        return null;
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<CommentsDO> getReviewComments(Map<String, Object> parms) {
        return this.getSqlMapClientTemplate().queryForList("CommentsDAO.getReviewComments", parms);
    }
}
