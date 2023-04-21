package org.opennms.wicket.svclayer;

import org.hibernate.criterion.Restrictions;
import org.opennms.netmgt.dao.NotificationDao;
import org.opennms.netmgt.model.OnmsCriteria;
import wicket.spring.injection.annot.SpringBean;

public class NotificationServiceImpl {

    @SpringBean
    private NotificationDao m_dao;

    public NotificationServiceImpl(NotificationDao dao) {
        m_dao = dao;
    }

    public NotificationDao getDao() {
        return m_dao;
    }

    public void setDao(NotificationDao dao) {
        this.m_dao = dao;
    }

    public Integer getOutstandingNoticeCountAll(OnmsCriteria criteria) {
        criteria.createCriteria("respondTime").add(Restrictions.isNull("respondTime"));
        return m_dao.findMatching(criteria).size();
    }
}
