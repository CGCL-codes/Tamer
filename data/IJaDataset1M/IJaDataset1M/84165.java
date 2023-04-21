package net.sf.brightside.belex.service.usage.hibernate;

import java.util.List;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;
import net.sf.brightside.belex.core.exception.BelexException;
import net.sf.brightside.belex.metamodel.Ownership;
import net.sf.brightside.belex.metamodel.Stockholder;
import net.sf.brightside.belex.service.crud.api.GetResultsCommand;
import net.sf.brightside.belex.service.usage.api.OwnedStocksCommand;
import net.sf.brightside.belex.service.usage.exceptions.NoOwnershipsException;

public class OwnedStocksCommandImpl implements OwnedStocksCommand<Ownership> {

    private Stockholder stockholder;

    private GetResultsCommand<Ownership> getter;

    @Override
    public Stockholder getStockholder() {
        return stockholder;
    }

    @Override
    public void setStockholder(Stockholder stockholder) {
        this.stockholder = stockholder;
    }

    @Override
    public List<Ownership> execute() throws NoOwnershipsException, BelexException {
        DetachedCriteria criteria = DetachedCriteria.forClass(Ownership.class);
        criteria.add(Restrictions.eq("stockholder", stockholder));
        getter.setDetachedCriteria(criteria);
        List<Ownership> ownerships = getter.execute();
        if (ownerships.isEmpty()) {
            throw new NoOwnershipsException();
        } else return ownerships;
    }

    public GetResultsCommand<Ownership> getGetter() {
        return getter;
    }

    public void setGetter(GetResultsCommand<Ownership> getter) {
        this.getter = getter;
    }
}
