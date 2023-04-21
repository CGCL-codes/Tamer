package au.edu.monash.merc.capture.dao.impl;

import java.util.List;
import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Repository;
import au.edu.monash.merc.capture.dao.HibernateGenericDAO;
import au.edu.monash.merc.capture.domain.Dataset;
import au.edu.monash.merc.capture.dto.OrderBy;
import au.edu.monash.merc.capture.dto.page.Pagination;
import au.edu.monash.merc.capture.repository.IDatasetRepository;

@Scope("prototype")
@Repository
public class DatasetDAO extends HibernateGenericDAO<Dataset> implements IDatasetRepository {

    @Override
    public Dataset getDatasetByHandlId(String handleId) {
        return (Dataset) this.session().createCriteria(this.persistClass).add(Restrictions.eq("handleId", handleId)).uniqueResult();
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<Dataset> getDatasetByCollectionId(long cid) {
        Criteria criteria = this.session().createCriteria(this.persistClass);
        Criteria colcrit = criteria.createCriteria("collection");
        colcrit.add(Restrictions.eq("id", cid));
        return criteria.list();
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<Dataset> getDatasetByCollectionIdUsrId(long cid, long uid) {
        Criteria criteria = this.session().createCriteria(this.persistClass);
        Criteria colcrit = criteria.createCriteria("collection");
        colcrit.add(Restrictions.eq("id", cid));
        Criteria usrcrit = colcrit.createCriteria("owner");
        usrcrit.add(Restrictions.eq("id", uid));
        criteria.addOrder(Order.desc("importDateTime"));
        return criteria.list();
    }

    @Override
    public boolean checkDatasetNameExisted(String dsName, long cid) {
        Criteria criteria = this.session().createCriteria(this.persistClass);
        Criteria colcrit = criteria.createCriteria("collection");
        colcrit.add(Restrictions.eq("id", cid));
        int num = (Integer) criteria.setProjection(Projections.rowCount()).add(Restrictions.eq("name", dsName)).uniqueResult();
        if (num == 1) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    public void deleteDatasetByCollectionId(long cid) {
        String del_hql = "DELETE FROM " + this.persistClass.getSimpleName() + " AS ds WHERE ds.collection.id = :id";
        Query query = this.session().createQuery(del_hql);
        query.setLong("id", cid);
        query.executeUpdate();
    }

    @Override
    public void deleteDatasetById(long id) {
        String del_hql = "DELETE FROM " + this.persistClass.getSimpleName() + " AS ds WHERE ds.id = :id";
        Query query = this.session().createQuery(del_hql);
        query.setLong("id", id);
        query.executeUpdate();
    }

    @SuppressWarnings("unchecked")
    @Override
    public Pagination<Dataset> getDatasetByCollectionId(long cid, int startPageNo, int recordsPerPage, OrderBy[] orderBys) {
        Criteria criteria = this.session().createCriteria(this.persistClass);
        Criteria cocriteria = criteria.createCriteria("collection");
        cocriteria.add(Restrictions.eq("id", cid));
        criteria.setProjection(Projections.rowCount());
        int total = ((Integer) criteria.uniqueResult()).intValue();
        Pagination<Dataset> dsPage = new Pagination<Dataset>(startPageNo, recordsPerPage, total);
        Criteria qcriteria = this.session().createCriteria(this.persistClass);
        Criteria qcoCrit = qcriteria.createCriteria("collection");
        qcoCrit.add(Restrictions.eq("id", cid));
        if (orderBys != null && orderBys.length > 0) {
            for (int i = 0; i < orderBys.length; i++) {
                Order order = orderBys[i].getOrder();
                if (order != null) {
                    qcriteria.addOrder(order);
                }
            }
        } else {
            qcriteria.addOrder(Order.desc("importDateTime"));
        }
        qcriteria.setFirstResult(dsPage.getFirstResult());
        qcriteria.setMaxResults(dsPage.getSizePerPage());
        List<Dataset> dsList = qcriteria.list();
        dsPage.setPageResults(dsList);
        return dsPage;
    }
}
