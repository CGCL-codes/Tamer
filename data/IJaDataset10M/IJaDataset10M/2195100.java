package eu.coaxion.gnumedj.postgre.v14;

import java.util.List;
import org.hibernate.criterion.DetachedCriteria;
import org.orm.PersistentSession;
import org.orm.criteria.*;

public class Enum_ext_id_typesDetachedCriteria extends AbstractORMDetachedCriteria {

    public final IntegerExpression pk;

    public final StringExpression name;

    public final StringExpression issuer;

    public Enum_ext_id_typesDetachedCriteria() {
        super(eu.coaxion.gnumedj.postgre.v14.Enum_ext_id_types.class, eu.coaxion.gnumedj.postgre.v14.Enum_ext_id_typesCriteria.class);
        pk = new IntegerExpression("pk", this.getDetachedCriteria());
        name = new StringExpression("name", this.getDetachedCriteria());
        issuer = new StringExpression("issuer", this.getDetachedCriteria());
    }

    public Enum_ext_id_typesDetachedCriteria(DetachedCriteria aDetachedCriteria) {
        super(aDetachedCriteria, eu.coaxion.gnumedj.postgre.v14.Enum_ext_id_typesCriteria.class);
        pk = new IntegerExpression("pk", this.getDetachedCriteria());
        name = new StringExpression("name", this.getDetachedCriteria());
        issuer = new StringExpression("issuer", this.getDetachedCriteria());
    }

    public Lnk_identity2ext_idDetachedCriteria createLnk_identity2ext_idCriteria() {
        return new Lnk_identity2ext_idDetachedCriteria(createCriteria("lnk_identity2ext_id"));
    }

    public Lnk_org2ext_idDetachedCriteria createLnk_org2ext_idCriteria() {
        return new Lnk_org2ext_idDetachedCriteria(createCriteria("lnk_org2ext_id"));
    }

    public Enum_ext_id_types uniqueEnum_ext_id_types(PersistentSession session) {
        return (Enum_ext_id_types) super.createExecutableCriteria(session).uniqueResult();
    }

    public Enum_ext_id_types[] listEnum_ext_id_types(PersistentSession session) {
        List list = super.createExecutableCriteria(session).list();
        return (Enum_ext_id_types[]) list.toArray(new Enum_ext_id_types[list.size()]);
    }
}
