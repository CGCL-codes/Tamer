package com.jaspersoft.jasperserver.api.metadata.common.service.impl.hibernate;

import java.util.Iterator;
import java.util.List;
import org.hibernate.criterion.Conjunction;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Disjunction;
import org.hibernate.criterion.Junction;
import org.hibernate.criterion.Restrictions;
import com.jaspersoft.jasperserver.api.JSException;
import com.jaspersoft.jasperserver.api.metadata.view.domain.Filter;
import com.jaspersoft.jasperserver.api.metadata.view.domain.FilterElement;
import com.jaspersoft.jasperserver.api.metadata.view.domain.ParentFolderFilter;
import com.jaspersoft.jasperserver.api.metadata.view.domain.PropertyFilter;

/**
 * @author Lucian Chirita (lucianc@users.sourceforge.net)
 * @version $Id: HibernateFilter.java 2 2006-04-30 16:19:39Z sgwood $
 */
public class HibernateFilter implements Filter {

    public final Junction junction;

    private Criterion criterion;

    public HibernateFilter(Junction junction) {
        this.junction = junction;
    }

    protected final Criterion last() {
        return criterion;
    }

    protected void add(Criterion newCriterion) {
        this.criterion = newCriterion;
        if (junction != null) {
            junction.add(newCriterion);
        }
    }

    public void applyParentFolderFilter(ParentFolderFilter filter) {
        add(Restrictions.eq("parent.URI", filter.getFolderURI()));
    }

    public void applyPropertyFilter(PropertyFilter filter) {
        Criterion propCriterion;
        switch(filter.getOp()) {
            case PropertyFilter.EQ:
                propCriterion = Restrictions.eq(filter.getProperty(), filter.getValue());
                break;
            case PropertyFilter.LIKE:
                propCriterion = Restrictions.like(filter.getProperty(), filter.getValue());
                break;
            default:
                throw new JSException("Unknown property filter operation " + filter.getOp());
        }
        add(propCriterion);
    }

    public void applyNegatedFilter(FilterElement element) {
        HibernateFilter notFilter = new HibernateFilter(null);
        element.apply(notFilter);
        add(Restrictions.not(notFilter.last()));
    }

    public void applyConjunction(List filterElements) {
        Conjunction conjunction = Restrictions.conjunction();
        applyJunction(filterElements, conjunction);
    }

    public void applyDisjunction(List filterElements) {
        Disjunction disjunction = Restrictions.disjunction();
        applyJunction(filterElements, disjunction);
    }

    protected void applyJunction(List filterElements, Junction subJunction) {
        HibernateFilter junctionFilter = new HibernateFilter(subJunction);
        for (Iterator it = filterElements.iterator(); it.hasNext(); ) {
            FilterElement filterElement = (FilterElement) it.next();
            filterElement.apply(junctionFilter);
        }
        add(subJunction);
    }
}
