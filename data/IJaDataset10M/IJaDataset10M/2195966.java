package com.clican.pluto.cms.dao.hibernate;

import java.io.Serializable;
import java.sql.SQLException;
import java.util.List;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.springframework.orm.hibernate3.HibernateCallback;
import com.clican.pluto.cms.dao.DataModelDao;
import com.clican.pluto.orm.desc.ModelDescription;
import com.clican.pluto.orm.dynamic.inter.IDataModel;
import com.clican.pluto.orm.dynamic.inter.IDirectory;

public class DataModelDaoHibernateImpl extends BaseDao implements DataModelDao {

    @SuppressWarnings("unchecked")
    public List<Serializable> query(String queryString) {
        return getHibernateTemplate().find(queryString);
    }

    @SuppressWarnings("unchecked")
    public List<IDataModel> getDataModels(IDirectory directory, ModelDescription modelDescription, List<String> orderBy) {
        StringBuffer hql = new StringBuffer();
        hql.append("from ");
        hql.append(modelDescription.getFirstCharUpperName());
        hql.append(" m where m.parent = :directory");
        if (orderBy != null && orderBy.size() > 0) {
            hql.append(" order by ");
            for (int i = 0; i < orderBy.size(); i++) {
                hql.append("m.");
                hql.append(orderBy.get(i));
                if (i != orderBy.size()) {
                    hql.append(",");
                }
            }
        }
        return (List<IDataModel>) getHibernateTemplate().findByNamedParam(hql.toString(), "directory", directory);
    }

    @SuppressWarnings("unchecked")
    public List<IDataModel> getDataModels(ModelDescription modelDescription, String name) {
        StringBuffer hql = new StringBuffer();
        hql.append("from ");
        hql.append(modelDescription.getFirstCharUpperName());
        hql.append(" m where m.name like :name");
        return (List<IDataModel>) getHibernateTemplate().findByNamedParam(hql.toString(), "name", "%" + name + "%");
    }

    public IDataModel loadDataModels(Class<?> clazz, Long id) {
        return (IDataModel) this.getHibernateTemplate().load(clazz, id);
    }

    public void delete(List<IDataModel> dataModels, final ModelDescription modelDescription) {
        final StringBuffer hql = new StringBuffer();
        hql.append("delete from ");
        hql.append(modelDescription.getFirstCharUpperName() + " m where m.id in ");
        hql.append(this.getInString(dataModels));
        getHibernateTemplate().execute(new HibernateCallback() {

            public Object doInHibernate(Session session) throws HibernateException, SQLException {
                int rows = session.createQuery(hql.toString()).executeUpdate();
                if (log.isDebugEnabled()) {
                    log.debug("delete " + rows + " rows " + modelDescription.getName());
                }
                return null;
            }
        });
    }
}
