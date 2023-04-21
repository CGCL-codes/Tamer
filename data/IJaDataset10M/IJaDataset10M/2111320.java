package query.implementation.db4o;

import org.joda.time.ReadableInterval;
import persistence.db4o.Db4oModelPersistence;
import query.framework.criteria.Criteria;
import query.framework.query.SearchQuery;
import query.framework.results.Db4oObjectSetSearchResults;
import query.framework.results.LazySearchResultsSpecification;
import query.framework.results.SearchResults;
import com.db4o.query.Query;

public abstract class StandardSodaSearchQuery<GenericCriteria extends Criteria> implements SearchQuery {

    private GenericCriteria criteria;

    public void setCriteria(Criteria criteria) {
        this.criteria = (GenericCriteria) criteria;
    }

    public SearchResults results() {
        return new Db4oObjectSetSearchResults(resultsSpecification(), query().execute());
    }

    protected GenericCriteria criteria() {
        return criteria;
    }

    protected abstract Query query();

    protected abstract LazySearchResultsSpecification resultsSpecification();

    protected static final Query queryFor(Class clazz) {
        Query query = Db4oModelPersistence.instance().container().query();
        query.constrain(clazz);
        return query;
    }

    /**
	 * Adds an interval constraint to the query. Orders descending.  
	 * 
	 * Note: Assumes that the object constrained has a "date" field, instance of a subclass of BaseDateTime.
	 *  
	 * @param query
	 * @param interval
	 * @return The query with the constraint applied.
	 */
    protected static final Query constrainInterval(Query query, ReadableInterval interval) {
        Query iMillisField = query.descend("date").descend("iMillis");
        iMillisField.constrain(interval.getStartMillis()).greater().or(iMillisField.constrain(interval.getStartMillis()).equal());
        iMillisField.constrain(interval.getEndMillis()).smaller();
        iMillisField.orderDescending();
        return query;
    }

    protected static final Query constrainEquals(Query query, String fieldName, Object anObject) {
        Query aField = query.descend(fieldName);
        aField.constrain(anObject).equal();
        return query;
    }
}
