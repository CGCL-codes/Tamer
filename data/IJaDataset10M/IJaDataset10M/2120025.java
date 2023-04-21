package orm;

import org.hibernate.Criteria;
import org.orm.PersistentException;
import org.orm.PersistentSession;
import org.orm.criteria.*;

public class SA_CargoCriteria extends AbstractORMCriteria {

    public final StringExpression id_cargo;

    public final StringExpression descripcion;

    public SA_CargoCriteria(Criteria criteria) {
        super(criteria);
        id_cargo = new StringExpression("id_cargo", this);
        descripcion = new StringExpression("descripcion", this);
    }

    public SA_CargoCriteria(PersistentSession session) {
        this(session.createCriteria(SA_Cargo.class));
    }

    public SA_CargoCriteria() throws PersistentException {
        this(orm.SolicitudEmpleadoPersistentManager.instance().getSession());
    }

    public SA_EmpleadoCriteria createSA_EmpleadoCriteria() {
        return new SA_EmpleadoCriteria(createCriteria("ORM_SA_Empleado"));
    }

    public SA_Cargo uniqueSA_Cargo() {
        return (SA_Cargo) super.uniqueResult();
    }

    public SA_Cargo[] listSA_Cargo() {
        java.util.List list = super.list();
        return (SA_Cargo[]) list.toArray(new SA_Cargo[list.size()]);
    }
}
