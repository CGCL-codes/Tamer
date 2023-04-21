package eu.coaxion.gnumedj.postgre.v14;

import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.criterion.Criterion;
import org.orm.PersistentException;
import org.orm.PersistentSession;
import org.orm.criteria.*;

public class Lnk_result2lab_reqCriteria extends AbstractORMCriteria {

    public final IntegerExpression pk;

    public Lnk_result2lab_reqCriteria(Criteria criteria) {
        super(criteria);
        pk = new IntegerExpression("pk", this);
    }

    public Lnk_result2lab_reqCriteria(PersistentSession session) {
        this(session.createCriteria(Lnk_result2lab_req.class));
    }

    public Lnk_result2lab_reqCriteria() throws PersistentException {
        this(eu.coaxion.gnumedj.postgre.v14.UntitledPersistentManager.instance().getSession());
    }

    public Test_resultCriteria createFk_resultCriteria() {
        return new Test_resultCriteria(createCriteria("fk_result"));
    }

    public Lab_requestCriteria createFk_requestCriteria() {
        return new Lab_requestCriteria(createCriteria("fk_request"));
    }

    public Lnk_result2lab_req uniqueLnk_result2lab_req() {
        return (Lnk_result2lab_req) super.uniqueResult();
    }

    public Lnk_result2lab_req[] listLnk_result2lab_req() {
        java.util.List list = super.list();
        return (Lnk_result2lab_req[]) list.toArray(new Lnk_result2lab_req[list.size()]);
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
