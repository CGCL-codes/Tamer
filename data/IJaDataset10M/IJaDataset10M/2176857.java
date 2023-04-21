package eu.coaxion.gnumedj.postgre.v14;

import java.util.List;
import org.hibernate.criterion.DetachedCriteria;
import org.orm.PersistentSession;
import org.orm.criteria.*;

public class Vacc_routeDetachedCriteria extends AbstractORMDetachedCriteria {

    public final IntegerExpression pk_audit;

    public final IntegerExpression row_version;

    public final TimestampExpression modified_when;

    public final StringExpression modified_by;

    public final IntegerExpression id;

    public final StringExpression abbreviation;

    public final StringExpression description;

    public Vacc_routeDetachedCriteria() {
        super(eu.coaxion.gnumedj.postgre.v14.Vacc_route.class, eu.coaxion.gnumedj.postgre.v14.Vacc_routeCriteria.class);
        pk_audit = new IntegerExpression("pk_audit", this.getDetachedCriteria());
        row_version = new IntegerExpression("row_version", this.getDetachedCriteria());
        modified_when = new TimestampExpression("modified_when", this.getDetachedCriteria());
        modified_by = new StringExpression("modified_by", this.getDetachedCriteria());
        id = new IntegerExpression("id", this.getDetachedCriteria());
        abbreviation = new StringExpression("abbreviation", this.getDetachedCriteria());
        description = new StringExpression("description", this.getDetachedCriteria());
    }

    public Vacc_routeDetachedCriteria(DetachedCriteria aDetachedCriteria) {
        super(aDetachedCriteria, eu.coaxion.gnumedj.postgre.v14.Vacc_routeCriteria.class);
        pk_audit = new IntegerExpression("pk_audit", this.getDetachedCriteria());
        row_version = new IntegerExpression("row_version", this.getDetachedCriteria());
        modified_when = new TimestampExpression("modified_when", this.getDetachedCriteria());
        modified_by = new StringExpression("modified_by", this.getDetachedCriteria());
        id = new IntegerExpression("id", this.getDetachedCriteria());
        abbreviation = new StringExpression("abbreviation", this.getDetachedCriteria());
        description = new StringExpression("description", this.getDetachedCriteria());
    }

    public VaccineDetachedCriteria createVaccineCriteria() {
        return new VaccineDetachedCriteria(createCriteria("vaccine"));
    }

    public Vacc_route uniqueVacc_route(PersistentSession session) {
        return (Vacc_route) super.createExecutableCriteria(session).uniqueResult();
    }

    public Vacc_route[] listVacc_route(PersistentSession session) {
        List list = super.createExecutableCriteria(session).list();
        return (Vacc_route[]) list.toArray(new Vacc_route[list.size()]);
    }
}
