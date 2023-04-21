package orm;

import java.util.List;
import org.hibernate.criterion.DetachedCriteria;
import org.orm.PersistentSession;
import org.orm.criteria.*;

public class Tdg_anotadorDetachedCriteria extends AbstractORMDetachedCriteria {

    public final StringExpression an_rut;

    public final StringExpression an_nombre;

    public final StringExpression an_apellido;

    public Tdg_anotadorDetachedCriteria() {
        super(orm.Tdg_anotador.class, orm.Tdg_anotadorCriteria.class);
        an_rut = new StringExpression("an_rut", this.getDetachedCriteria());
        an_nombre = new StringExpression("an_nombre", this.getDetachedCriteria());
        an_apellido = new StringExpression("an_apellido", this.getDetachedCriteria());
    }

    public Tdg_anotadorDetachedCriteria(DetachedCriteria aDetachedCriteria) {
        super(aDetachedCriteria, orm.Tdg_anotadorCriteria.class);
        an_rut = new StringExpression("an_rut", this.getDetachedCriteria());
        an_nombre = new StringExpression("an_nombre", this.getDetachedCriteria());
        an_apellido = new StringExpression("an_apellido", this.getDetachedCriteria());
    }

    public Tdg_subsectorDetachedCriteria createAn_id_subsectorCriteria() {
        return new Tdg_subsectorDetachedCriteria(createCriteria("an_id_subsector"));
    }

    public Tdg_anotacionDetachedCriteria createTdg_anotacionCriteria() {
        return new Tdg_anotacionDetachedCriteria(createCriteria("ORM_Tdg_anotacion"));
    }

    public Tdg_anotador uniqueTdg_anotador(PersistentSession session) {
        return (Tdg_anotador) super.createExecutableCriteria(session).uniqueResult();
    }

    public Tdg_anotador[] listTdg_anotador(PersistentSession session) {
        List list = super.createExecutableCriteria(session).list();
        return (Tdg_anotador[]) list.toArray(new Tdg_anotador[list.size()]);
    }
}
