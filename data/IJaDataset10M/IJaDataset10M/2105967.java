package cn.myapps.core.personalmessage.dao;

import java.util.Collection;
import org.hibernate.Query;
import org.hibernate.Session;
import cn.myapps.base.dao.HibernateBaseDAO;

public class HibernatePersonalMessageDAO extends HibernateBaseDAO implements PersonalMessageDAO {

    public HibernatePersonalMessageDAO(String voClassName) {
        super(voClassName);
    }

    public Collection queryTrash(String userid, int page, int line) throws Exception {
        StringBuffer hql = new StringBuffer();
        hql.append("from ").append(_voClazzName).append(" vo ");
        hql.append("where (vo.senderId='").append(userid).append("' or vo.receiverId='").append(userid).append("') and vo.trash=true order by vo.sendDate desc");
        Session session = currentSession();
        Query query = session.createQuery(hql.toString());
        return query.list();
    }

    public int countNewMessages(String userid) throws Exception {
        StringBuffer hql = new StringBuffer();
        hql.append("from ").append(_voClazzName).append(" vo ");
        hql.append("where vo.receiverId='").append(userid).append("' and vo.trash=false and vo.read=false and vo.inbox=true order by vo.sendDate desc");
        Session session = currentSession();
        Query query = session.createQuery(hql.toString());
        return query.list().size();
    }
}
