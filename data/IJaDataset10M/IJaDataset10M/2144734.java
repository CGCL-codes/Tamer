package ar.fi.uba.apolo.daos;

import java.util.List;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Expression;
import ar.fi.uba.apolo.entities.Evaluado;

public class EvaluadoDaoImpl extends UsuarioDaoImpl implements EvaluadoDaoInterface {

    public List<Evaluado> findAll() {
        DetachedCriteria criteria = DetachedCriteria.forClass(Evaluado.class);
        return getHibernateTemplate().findByCriteria(criteria);
    }

    public Evaluado findById(Long id) {
        return (Evaluado) getHibernateTemplate().get(Evaluado.class, id);
    }

    public Evaluado findByNroDocumento(String nroDocumento) {
        DetachedCriteria criteria = DetachedCriteria.forClass(Evaluado.class);
        criteria.add(Expression.eq("nroDocumento", nroDocumento));
        List<Evaluado> list = getHibernateTemplate().findByCriteria(criteria);
        if (list.isEmpty()) return null;
        return (Evaluado) list.get(0);
    }
}
