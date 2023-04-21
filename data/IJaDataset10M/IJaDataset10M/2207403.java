package br.edu.uncisal.farmacia.dao;

import java.util.List;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import br.com.caelum.vraptor.ioc.Component;
import br.edu.uncisal.farmacia.model.Orgao;
import br.edu.uncisal.farmacia.model.Unidade;

@Component
public class UnidadeDao extends Dao<Unidade> {

    UnidadeDao(Session session) {
        super(session, Unidade.class);
    }

    @SuppressWarnings("unchecked")
    public List<Unidade> listByOrgao(Orgao o) {
        Criteria crit = session.createCriteria(Unidade.class);
        crit.add(Restrictions.eq("orgao.id", o.getId()));
        crit.addOrder(Order.asc("nome"));
        return crit.list();
    }
}
