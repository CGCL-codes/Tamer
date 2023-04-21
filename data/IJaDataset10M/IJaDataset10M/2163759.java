package siac.com.dao;

import java.util.List;
import org.hibernate.Query;
import org.hibernate.Session;
import siac.com.configuracao.ConexaoHibernate;
import siac.com.entity.FacTipoProcedimento;

public class FacTipoProcedimentoDao extends AbstractHibernateDao<FacTipoProcedimento> {

    public FacTipoProcedimentoDao() {
        super(FacTipoProcedimento.class);
    }

    @Override
    protected Session getSession() {
        return ConexaoHibernate.getSessionFactory().getCurrentSession();
    }

    public List<FacTipoProcedimento> findCode(Object code) {
        Query query = this.getSession().getNamedQuery("FacTipoProcedimento.findByCode");
        query.setParameter("code", code);
        return query.list();
    }

    public List<FacTipoProcedimento> findName(Object name) {
        Query query = this.getSession().getNamedQuery("FacTipoProcedimento.findByName");
        query.setParameter("name", name);
        return query.list();
    }

    public List<FacTipoProcedimento> findStatus(Object status) {
        Query query = this.getSession().getNamedQuery("FacTipoProcedimento.findByStatus");
        query.setParameter("status", status);
        return query.list();
    }
}
