package eu.coaxion.gnumedj.postgre.v14;

import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.criterion.Criterion;
import org.orm.PersistentException;
import org.orm.PersistentSession;
import org.orm.criteria.*;

public class Lnk_person2relativeCriteria extends AbstractORMCriteria {

    public final IntegerExpression pk_audit;

    public final IntegerExpression row_version;

    public final TimestampExpression modified_when;

    public final StringExpression modified_by;

    public final IntegerExpression id;

    public final DateExpression started;

    public final DateExpression ended;

    public Lnk_person2relativeCriteria(Criteria criteria) {
        super(criteria);
        pk_audit = new IntegerExpression("pk_audit", this);
        row_version = new IntegerExpression("row_version", this);
        modified_when = new TimestampExpression("modified_when", this);
        modified_by = new StringExpression("modified_by", this);
        id = new IntegerExpression("id", this);
        started = new DateExpression("started", this);
        ended = new DateExpression("ended", this);
    }

    public Lnk_person2relativeCriteria(PersistentSession session) {
        this(session.createCriteria(Lnk_person2relative.class));
    }

    public Lnk_person2relativeCriteria() throws PersistentException {
        this(eu.coaxion.gnumedj.postgre.v14.UntitledPersistentManager.instance().getSession());
    }

    public IdentityCriteria createId_identityCriteria() {
        return new IdentityCriteria(createCriteria("id_identity"));
    }

    public IdentityCriteria createId_relativeCriteria() {
        return new IdentityCriteria(createCriteria("id_relative"));
    }

    public Relation_typesCriteria createId_relation_typeCriteria() {
        return new Relation_typesCriteria(createCriteria("id_relation_type"));
    }

    public Lnk_person2relative uniqueLnk_person2relative() {
        return (Lnk_person2relative) super.uniqueResult();
    }

    public Lnk_person2relative[] listLnk_person2relative() {
        java.util.List list = super.list();
        return (Lnk_person2relative[]) list.toArray(new Lnk_person2relative[list.size()]);
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
