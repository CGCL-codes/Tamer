package org.deri.iris.new_stuff.facts;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.deri.iris.api.basics.IPredicate;
import org.deri.iris.api.basics.ITuple;
import org.deri.iris.api.storage_old.IDataSource;
import org.deri.iris.api.terms.ITerm;
import org.deri.iris.factory.Factory;
import org.deri.iris.new_stuff.storage.IRelation;

public class FactsWithExternalData implements IFacts {

    /**
	 * Constructor.
	 */
    public FactsWithExternalData(IFacts facts, List<IDataSource> externalDataSources) {
        mFacts = facts;
        mExternalDataSources = new ArrayList<IDataSource>(externalDataSources);
    }

    public IRelation get(IPredicate predicate) {
        IRelation result = mFacts.get(predicate);
        if (mExternalPredicatesAlreadyFetched.add(predicate)) {
            ITuple from = Factory.BASIC.createTuple(new ITerm[predicate.getArity()]);
            ITuple to = Factory.BASIC.createTuple(new ITerm[predicate.getArity()]);
            for (IDataSource dataSource : mExternalDataSources) ;
        }
        return result;
    }

    public Set<IPredicate> getPredicates() {
        return mFacts.getPredicates();
    }

    private final IFacts mFacts;

    private final List<IDataSource> mExternalDataSources;

    private Set<IPredicate> mExternalPredicatesAlreadyFetched = new HashSet<IPredicate>();
}
