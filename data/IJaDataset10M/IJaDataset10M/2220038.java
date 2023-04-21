package orm;

import org.hibernate.Criteria;
import org.orm.PersistentException;
import org.orm.PersistentSession;
import org.orm.criteria.*;

public class Tcc_asistenciaeventoCriteria extends AbstractORMCriteria {

    public final IntegerExpression ae_id;

    public Tcc_asistenciaeventoCriteria(Criteria criteria) {
        super(criteria);
        ae_id = new IntegerExpression("ae_id", this);
    }

    public Tcc_asistenciaeventoCriteria(PersistentSession session) {
        this(session.createCriteria(Tcc_asistenciaevento.class));
    }

    public Tcc_asistenciaeventoCriteria() throws PersistentException {
        this(orm.ConanControlPersistentManager.instance().getSession());
    }

    public Tcc_alumnoCriteria createTcc_alumnoalCriteria() {
        return new Tcc_alumnoCriteria(createCriteria("tcc_alumnoal"));
    }

    public Tcc_eventoCriteria createTcc_eventoevCriteria() {
        return new Tcc_eventoCriteria(createCriteria("tcc_eventoev"));
    }

    public Tcc_asistenciaevento uniqueTcc_asistenciaevento() {
        return (Tcc_asistenciaevento) super.uniqueResult();
    }

    public Tcc_asistenciaevento[] listTcc_asistenciaevento() {
        java.util.List list = super.list();
        return (Tcc_asistenciaevento[]) list.toArray(new Tcc_asistenciaevento[list.size()]);
    }
}
