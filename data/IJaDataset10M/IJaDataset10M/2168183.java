package com.redhat.gs.mrlogistics.scheduler;

import java.util.List;
import java.util.Set;
import org.hibernate.criterion.Criterion;

/**
 * @author arjun
 *
 */
public class RevenueReport extends Report {

    @Override
    public String Format(IFormatter formatter) {
        return formatter.Format(this);
    }

    @Override
    public void Populate() {
    }

    public void Populate(Set<Criterion> filters) {
    }

    @Override
    public Class classType() {
        return null;
    }
}
