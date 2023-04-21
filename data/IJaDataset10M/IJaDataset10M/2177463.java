package ar.fi.uba.apolo.daos;

import java.util.List;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Expression;
import ar.fi.uba.apolo.entities.Pregunta;

public class PreguntaDaoImpl extends AbstractGenericDaoImpl implements PreguntaDaoInterface {

    public List<Pregunta> findAll() {
        DetachedCriteria criteria = DetachedCriteria.forClass(Pregunta.class);
        return getHibernateTemplate().findByCriteria(criteria);
    }

    public Pregunta findById(Long id) {
        DetachedCriteria criteria = DetachedCriteria.forClass(Pregunta.class);
        criteria.add(Expression.eq("id", id));
        List<Pregunta> list = getHibernateTemplate().findByCriteria(criteria);
        if (list.isEmpty()) return null;
        return (Pregunta) list.get(0);
    }
}
