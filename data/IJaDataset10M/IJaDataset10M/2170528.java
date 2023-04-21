package org.light.portlets.feedback.dao.hibernate;

import java.util.List;
import org.light.portal.core.dao.hibernate.BaseDaoImpl;
import org.light.portlets.feedback.dao.FeedbackDao;
import org.light.portlets.feedback.model.Feedback;

/**
 * 
 * @author Jianmin Liu
 **/
public class FeedbackDaoImpl extends BaseDaoImpl implements FeedbackDao {

    public List<Feedback> getFeedback(long orgId) {
        List<Feedback> list = this.getHibernateTemplate().find("select feedback from Feedback feedback where feedback.orgId=? order by createDate desc", orgId);
        return list;
    }

    public Feedback getFeedbackById(long id) {
        return (Feedback) this.getHibernateTemplate().get(Feedback.class, id);
    }
}
