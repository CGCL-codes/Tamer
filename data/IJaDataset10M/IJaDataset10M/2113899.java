package com.kescom.matrix.core.pulse;

import org.hibernate.Criteria;
import org.hibernate.NonUniqueResultException;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Restrictions;
import com.kescom.matrix.core.env.MatrixContext;
import com.kescom.matrix.core.series.Series;

public class SeriesByTemplateNameResolver<ISeries, IUser> implements IResolver<ISeries, IUser> {

    @SuppressWarnings("unchecked")
    public ISeries resolve(String name, IUser context) {
        if (name == null) return null;
        Criteria criteria = MatrixContext.getSession().createCriteria(Series.class).createAlias("entity", "entity").createAlias("entity.user", "entity.user").createAlias("template", "template").add(Restrictions.eq("entity.user", context)).add(Restrictions.ilike("template.name", name, MatchMode.START));
        try {
            return (ISeries) criteria.uniqueResult();
        } catch (NonUniqueResultException e) {
            return null;
        }
    }
}
