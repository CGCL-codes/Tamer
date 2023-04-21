package cn.myapps.core.workflow.storage.runtime.dao;

import java.util.Collection;
import org.hibernate.Query;
import org.hibernate.Session;
import cn.myapps.base.dao.HibernateBaseDAO;
import cn.myapps.core.workflow.storage.runtime.ejb.RelationHIS;

public class HibernateRelationHISDAO extends HibernateBaseDAO implements RelationHISDAO {

    public HibernateRelationHISDAO(String voClassName) {
        super(voClassName);
    }

    public RelationHIS findRelHISByCondition(String docid, String startnodeid, String endnodeid, boolean ispassed) throws Exception {
        String passed = null;
        if (ispassed) {
            passed = "1";
        } else {
            passed = "0";
        }
        String hql = "FROM " + _voClazzName + " vo WHERE " + "vo.docid='" + docid + "' AND vo.startnodeid='" + startnodeid + "' AND vo.endnodeid='" + endnodeid + "' AND vo.ispassed=" + passed;
        Collection datas = this.getDatas(hql, null);
        RelationHIS relationHIS = null;
        Object[] obj = datas.toArray();
        if (obj.length > 0) {
            relationHIS = (RelationHIS) (datas.toArray())[0];
        }
        return relationHIS;
    }

    public Collection queryRelationHIS(String docid, String flowid, String endnodeid) throws Exception {
        String hql = "FROM " + _voClazzName + " vo WHERE " + "vo.docid='" + docid + "' AND vo.flowid='" + flowid + "' AND vo.endnodeid='" + endnodeid + "'";
        return this.getDatas(hql, null);
    }

    public Collection query(String docid, String flowid) throws Exception {
        String hql = "FROM " + _voClazzName + " vo WHERE " + "vo.docid='" + docid + "' AND vo.flowid='" + flowid + "'" + "ORDER BY vo.actiontime";
        return this.getDatas(hql, null);
    }

    public RelationHIS find(String docid, String flowid) throws Exception {
        String hql = "FROM " + _voClazzName + " vo WHERE " + "vo.docid='" + docid + "' AND vo.flowid='" + flowid + "'" + "ORDER BY vo.id desc";
        Session s = this.currentSession();
        Query query = s.createQuery(hql);
        query.setMaxResults(1);
        return (RelationHIS) query.uniqueResult();
    }
}
