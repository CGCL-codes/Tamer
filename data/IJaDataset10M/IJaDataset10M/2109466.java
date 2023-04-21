package eu.coaxion.gnumedj.postgre.v14;

import java.util.List;
import org.hibernate.criterion.DetachedCriteria;
import org.orm.PersistentSession;
import org.orm.criteria.*;

public class CountryDetachedCriteria extends AbstractORMDetachedCriteria {

    public final IntegerExpression id;

    public final StringExpression code;

    public final StringExpression name;

    public final DateExpression deprecated;

    public CountryDetachedCriteria() {
        super(eu.coaxion.gnumedj.postgre.v14.Country.class, eu.coaxion.gnumedj.postgre.v14.CountryCriteria.class);
        id = new IntegerExpression("id", this.getDetachedCriteria());
        code = new StringExpression("code", this.getDetachedCriteria());
        name = new StringExpression("name", this.getDetachedCriteria());
        deprecated = new DateExpression("deprecated", this.getDetachedCriteria());
    }

    public CountryDetachedCriteria(DetachedCriteria aDetachedCriteria) {
        super(aDetachedCriteria, eu.coaxion.gnumedj.postgre.v14.CountryCriteria.class);
        id = new IntegerExpression("id", this.getDetachedCriteria());
        code = new StringExpression("code", this.getDetachedCriteria());
        name = new StringExpression("name", this.getDetachedCriteria());
        deprecated = new DateExpression("deprecated", this.getDetachedCriteria());
    }

    public StateDetachedCriteria createStateCriteria() {
        return new StateDetachedCriteria(createCriteria("state"));
    }

    public Country uniqueCountry(PersistentSession session) {
        return (Country) super.createExecutableCriteria(session).uniqueResult();
    }

    public Country[] listCountry(PersistentSession session) {
        List list = super.createExecutableCriteria(session).list();
        return (Country[]) list.toArray(new Country[list.size()]);
    }
}
