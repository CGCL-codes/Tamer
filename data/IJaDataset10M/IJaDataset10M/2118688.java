package siac.com.dao;

import java.util.List;
import org.hibernate.Query;
import org.hibernate.Session;
import siac.com.configuracao.ConexaoHibernate;
import siac.com.entity.LocMunicipio;
import siac.com.entity.LocProvincia;

public class LocMunicipioDao extends AbstractHibernateDao<LocMunicipio> {

    public LocMunicipioDao() {
        super(LocMunicipio.class);
    }

    @Override
    protected Session getSession() {
        return ConexaoHibernate.getSessionFactory().getCurrentSession();
    }

    public LocMunicipio findCode(Object code) {
        Query query = (Query) this.getSession().getNamedQuery("LocMunicipio.findByCode");
        query.setParameter("code", code);
        return (LocMunicipio) query.uniqueResult();
    }

    public List<LocMunicipio> findName(Object name) {
        Query query = (Query) this.getSession().getNamedQuery("LocMunicipio.findByCode");
        query.setParameter("name", name);
        return query.list();
    }

    public List<LocMunicipio> findStatus(Object status) {
        Query query = this.getSession().getNamedQuery("LocMunicipio.findByStatus");
        query.setParameter("status", status);
        return query.list();
    }

    public List<LocMunicipio> findByProvincia(Object provincia) {
        Query query = this.getSession().getNamedQuery("LocMunicipio.findByProvincia");
        query.setParameter("provincia", provincia);
        return query.list();
    }
}
