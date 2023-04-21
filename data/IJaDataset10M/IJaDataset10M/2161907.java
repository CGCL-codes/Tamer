package eu.coaxion.gnumedj.postgre.v14;

import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.criterion.Criterion;
import org.orm.PersistentException;
import org.orm.PersistentSession;
import org.orm.criteria.*;

public class Soap_cat_ranksCriteria extends AbstractORMCriteria {

    public final IntegerExpression pk;

    public final IntegerExpression rank;

    public Soap_cat_ranksCriteria(Criteria criteria) {
        super(criteria);
        pk = new IntegerExpression("pk", this);
        rank = new IntegerExpression("rank", this);
    }

    public Soap_cat_ranksCriteria(PersistentSession session) {
        this(session.createCriteria(Soap_cat_ranks.class));
    }

    public Soap_cat_ranksCriteria() throws PersistentException {
        this(eu.coaxion.gnumedj.postgre.v14.UntitledPersistentManager.instance().getSession());
    }

    public Soap_cat_ranks uniqueSoap_cat_ranks() {
        return (Soap_cat_ranks) super.uniqueResult();
    }

    public Soap_cat_ranks[] listSoap_cat_ranks() {
        java.util.List list = super.list();
        return (Soap_cat_ranks[]) list.toArray(new Soap_cat_ranks[list.size()]);
    }

    @Override
    public Criteria createAlias(String arg0, String arg1, int arg2, Criterion arg3) throws HibernateException {
        return null;
    }

    @Override
    public Criteria createCriteria(String arg0, String arg1, int arg2, Criterion arg3) throws HibernateException {
        return null;
    }

    @Override
    public boolean isReadOnly() {
        return false;
    }

    @Override
    public boolean isReadOnlyInitialized() {
        return false;
    }

    @Override
    public Criteria setReadOnly(boolean arg0) {
        return null;
    }
}
