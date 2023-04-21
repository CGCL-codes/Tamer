package com.tysanclan.site.projectewok.entities.dao.hibernate;

import org.hibernate.Criteria;
import org.hibernate.Query;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import com.jeroensteenbeeke.hyperion.data.SearchFilter;
import com.tysanclan.site.projectewok.dataaccess.EwokHibernateDAO;
import com.tysanclan.site.projectewok.entities.ForumPost;
import com.tysanclan.site.projectewok.entities.UnreadForumPost;
import com.tysanclan.site.projectewok.entities.User;

/**
 * @author Ties
 */
@Component
@Scope("request")
public class UnreadForumPostDAOImpl extends EwokHibernateDAO<UnreadForumPost> implements com.tysanclan.site.projectewok.entities.dao.UnreadForumPostDAO {

    @Override
    protected Criteria createCriteria(SearchFilter<UnreadForumPost> filter) {
        return getSession().createCriteria(UnreadForumPost.class);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, readOnly = false)
    public void markAsRead(User user, ForumPost post) {
        Query query = getSession().createQuery("delete from UnreadForumPost where user = :reader and forumPost = :post");
        query.setEntity("reader", user);
        query.setEntity("post", post);
        query.executeUpdate();
    }
}
