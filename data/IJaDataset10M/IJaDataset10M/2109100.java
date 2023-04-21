package main.dict.manager;

import java.util.List;
import org.hibernate.Query;
import org.lc.dao.hibernate.BaseHibernateDao;
import domain.dict.Dict;

public class DictManager extends BaseHibernateDao<Dict> {

    @SuppressWarnings("unchecked")
    public List getDictList(Integer type) {
        String hql = "select obj from domain.dict.Dict as obj where obj.dictType = ? order by obj.dictOrder";
        Query query = getSession().createQuery(hql);
        query.setInteger(0, type);
        return query.list();
    }

    @SuppressWarnings("unchecked")
    public Dict getDict(Integer dictType, String dictValue) {
        String hql = "select obj from domain.dict.Dict as obj where obj.dictType=? and obj.dictValue=?";
        Query query = getSession().createQuery(hql);
        query.setInteger(0, dictType);
        query.setString(1, dictValue);
        List list = query.list();
        if (list.size() > 0) {
            return (Dict) list.get(0);
        } else {
            return null;
        }
    }
}
