package br.com.caelum.jambo.dao;

import java.util.List;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Order;
import br.com.caelum.jambo.model.Term;

/**
 * Dao for <code>Term</code>
 * @author Lucas
 *
 */
public class TermDao extends Dao<Term> {

    public TermDao(Session session) {
        super(Term.class, session);
    }

    /**
	 * @return an ordered list of Terms
	 */
    @SuppressWarnings("unchecked")
    public List<Term> orderedList() {
        Criteria criteria = super.getSession().createCriteria(Term.class);
        criteria.addOrder(Order.desc("begin"));
        return criteria.list();
    }
}
