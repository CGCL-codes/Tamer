package cn.mop4j.core.forum.service.impl;

import java.util.Collection;
import cn.mop4j.base.service.BaseService;
import cn.mop4j.core.forum.dao.impl.HibernateForumDAO;
import cn.mop4j.core.forum.service.IForumService;
import cn.mop4j.core.forum.vo.Topic;

public class ForumServiceImpl extends BaseService implements IForumService {

    private HibernateForumDAO forumDAO;

    public void setForumDAO(HibernateForumDAO forumDAO) {
        this.forumDAO = forumDAO;
    }

    public HibernateForumDAO getForumDAO() {
        return forumDAO;
    }

    @Override
    public Collection getTop20Topics() {
        return forumDAO.getTop20Topics();
    }

    @Override
    public void createTopic(Topic topic) {
        try {
            forumDAO.save(topic);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
