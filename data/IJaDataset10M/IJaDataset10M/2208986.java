package eu.coaxion.gnumedj.postgre.v14;

import java.util.List;
import org.hibernate.criterion.DetachedCriteria;
import org.orm.PersistentSession;
import org.orm.criteria.*;

public class Coded_phraseDetachedCriteria extends AbstractORMDetachedCriteria {

    public final IntegerExpression pk_audit;

    public final IntegerExpression row_version;

    public final TimestampExpression modified_when;

    public final StringExpression modified_by;

    public final IntegerExpression pk;

    public final StringExpression term;

    public final StringExpression code;

    public final StringExpression xfk_coding_system;

    public Coded_phraseDetachedCriteria() {
        super(eu.coaxion.gnumedj.postgre.v14.Coded_phrase.class, eu.coaxion.gnumedj.postgre.v14.Coded_phraseCriteria.class);
        pk_audit = new IntegerExpression("pk_audit", this.getDetachedCriteria());
        row_version = new IntegerExpression("row_version", this.getDetachedCriteria());
        modified_when = new TimestampExpression("modified_when", this.getDetachedCriteria());
        modified_by = new StringExpression("modified_by", this.getDetachedCriteria());
        pk = new IntegerExpression("pk", this.getDetachedCriteria());
        term = new StringExpression("term", this.getDetachedCriteria());
        code = new StringExpression("code", this.getDetachedCriteria());
        xfk_coding_system = new StringExpression("xfk_coding_system", this.getDetachedCriteria());
    }

    public Coded_phraseDetachedCriteria(DetachedCriteria aDetachedCriteria) {
        super(aDetachedCriteria, eu.coaxion.gnumedj.postgre.v14.Coded_phraseCriteria.class);
        pk_audit = new IntegerExpression("pk_audit", this.getDetachedCriteria());
        row_version = new IntegerExpression("row_version", this.getDetachedCriteria());
        modified_when = new TimestampExpression("modified_when", this.getDetachedCriteria());
        modified_by = new StringExpression("modified_by", this.getDetachedCriteria());
        pk = new IntegerExpression("pk", this.getDetachedCriteria());
        term = new StringExpression("term", this.getDetachedCriteria());
        code = new StringExpression("code", this.getDetachedCriteria());
        xfk_coding_system = new StringExpression("xfk_coding_system", this.getDetachedCriteria());
    }

    public Coded_phrase uniqueCoded_phrase(PersistentSession session) {
        return (Coded_phrase) super.createExecutableCriteria(session).uniqueResult();
    }

    public Coded_phrase[] listCoded_phrase(PersistentSession session) {
        List list = super.createExecutableCriteria(session).list();
        return (Coded_phrase[]) list.toArray(new Coded_phrase[list.size()]);
    }
}
