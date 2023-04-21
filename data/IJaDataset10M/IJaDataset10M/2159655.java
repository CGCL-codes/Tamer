package eu.coaxion.gnumedj.postgre.v14;

import java.util.List;
import org.hibernate.criterion.DetachedCriteria;
import org.orm.PersistentSession;
import org.orm.criteria.*;

public class Lnk_identity2commDetachedCriteria extends AbstractORMDetachedCriteria {

    public final IntegerExpression pk;

    public final StringExpression url;

    public final BooleanExpression is_confidential;

    public Lnk_identity2commDetachedCriteria() {
        super(eu.coaxion.gnumedj.postgre.v14.Lnk_identity2comm.class, eu.coaxion.gnumedj.postgre.v14.Lnk_identity2commCriteria.class);
        pk = new IntegerExpression("pk", this.getDetachedCriteria());
        url = new StringExpression("url", this.getDetachedCriteria());
        is_confidential = new BooleanExpression("is_confidential", this.getDetachedCriteria());
    }

    public Lnk_identity2commDetachedCriteria(DetachedCriteria aDetachedCriteria) {
        super(aDetachedCriteria, eu.coaxion.gnumedj.postgre.v14.Lnk_identity2commCriteria.class);
        pk = new IntegerExpression("pk", this.getDetachedCriteria());
        url = new StringExpression("url", this.getDetachedCriteria());
        is_confidential = new BooleanExpression("is_confidential", this.getDetachedCriteria());
    }

    public IdentityDetachedCriteria createFk_identityCriteria() {
        return new IdentityDetachedCriteria(createCriteria("fk_identity"));
    }

    public AddressDetachedCriteria createFk_addressCriteria() {
        return new AddressDetachedCriteria(createCriteria("fk_address"));
    }

    public Enum_comm_typesDetachedCriteria createFk_typeCriteria() {
        return new Enum_comm_typesDetachedCriteria(createCriteria("fk_type"));
    }

    public Lnk_identity2comm uniqueLnk_identity2comm(PersistentSession session) {
        return (Lnk_identity2comm) super.createExecutableCriteria(session).uniqueResult();
    }

    public Lnk_identity2comm[] listLnk_identity2comm(PersistentSession session) {
        List list = super.createExecutableCriteria(session).list();
        return (Lnk_identity2comm[]) list.toArray(new Lnk_identity2comm[list.size()]);
    }
}
