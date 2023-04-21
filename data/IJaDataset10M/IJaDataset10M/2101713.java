package orm;

import org.hibernate.Criteria;
import org.orm.PersistentException;
import org.orm.PersistentSession;
import org.orm.criteria.*;

public class Tda_alumnoCriteria extends AbstractORMCriteria {

    public final StringExpression alum_rut;

    public final IntegerExpression alum_id;

    public final StringExpression alum_nombre;

    public final StringExpression alum_descripcion;

    public Tda_alumnoCriteria(Criteria criteria) {
        super(criteria);
        alum_rut = new StringExpression("alum_rut", this);
        alum_id = new IntegerExpression("alum_id", this);
        alum_nombre = new StringExpression("alum_nombre", this);
        alum_descripcion = new StringExpression("alum_descripcion", this);
    }

    public Tda_alumnoCriteria(PersistentSession session) {
        this(session.createCriteria(Tda_alumno.class));
    }

    public Tda_alumnoCriteria() throws PersistentException {
        this(orm.AnotacionesPersistentManager.instance().getSession());
    }

    public Tda_cursoCriteria createTda_cursocursoCriteria() {
        return new Tda_cursoCriteria(createCriteria("tda_cursocurso"));
    }

    public Tda_anotacionCriteria createTda_anotacionCriteria() {
        return new Tda_anotacionCriteria(createCriteria("ORM_Tda_anotacion"));
    }

    public Tda_alumno uniqueTda_alumno() {
        return (Tda_alumno) super.uniqueResult();
    }

    public Tda_alumno[] listTda_alumno() {
        java.util.List list = super.list();
        return (Tda_alumno[]) list.toArray(new Tda_alumno[list.size()]);
    }
}
