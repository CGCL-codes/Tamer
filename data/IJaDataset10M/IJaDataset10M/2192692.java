package org.s3b.search.query;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.foafrealm.manage.Person;
import org.s3b.search.query.impl.QueryParameterImpl;

/**
 * @author  Lukasz Porwol
 *
 */
public abstract class QueryObject {

    protected List<QueryParameter> queryParameters;

    protected Map<QueryParameterType, List<QueryParameterEntry>> mapParameters;

    protected boolean conjunction;

    protected Person postedBy;

    public QueryObject() {
        queryParameters = new ArrayList<QueryParameter>();
        mapParameters = new HashMap<QueryParameterType, List<QueryParameterEntry>>();
    }

    /**
     * @return String array that can be posted directly to lucene - based on all parameter types in QueryObject. 
     * The arrays is 1 or 2 elements big. First element is the query to match positively, second negativaly (-)s
     */
    public abstract String[] getLuceneQuery(boolean ifuri);

    public abstract String[] getLuceneQuery2(boolean ifuri, List<QueryParameterEntry> queryList);

    public abstract void modifyQueryAppend(List<QueryParameterEntry> queryList, List<QueryParameterEntry> expansion, QueryParameterEntry entry, boolean wordnet);

    public abstract void modifyQueryOverwrite(List<QueryParameterEntry> queryList, List<QueryParameterEntry> expansion, QueryParameterEntry entry, boolean wordnet);

    public Person getPostedBy() {
        return postedBy;
    }

    public void setPostedBy(Person _postedBy) {
        postedBy = _postedBy;
    }

    /**
	 * Removes all entries in the parameters list.
	 * 
	 */
    public void clearParameters() {
        queryParameters.clear();
    }

    /**
     * Adds new parameter to the QueryObject.<br/>
     * <b>NOTE:</b> if <tt>instantAdd</tt> is true then <tt>qp</tt> is added directly to the parameter list<br/>
     * else it is stored in temporary collection. <br/>Use <tt>QueryObject reload()</tt> to put it in the parameter list
     * for use<br/>
     * Provided for ConcurentModificationException handling
     * @param qp <tt> QueryParameter</tt> Query parameter to add
     * @param instantAdd <tt>boolean</tt> indicates whether to add to actual parameter list or just to temp one 
     */
    public void addParameter(QueryParameter qp, boolean instantAdd) {
        List<QueryParameterEntry> values = new ArrayList<QueryParameterEntry>();
        values.addAll(qp.getValues());
        if (!instantAdd) addParameter(qp.getType(), values); else {
            mapParameters.put(qp.getType(), qp.getValues());
            queryParameters.add(qp);
        }
    }

    public void reloadParameters() {
        queryParameters.clear();
        for (QueryParameterType type : mapParameters.keySet()) {
            queryParameters.add(new QueryParameterImpl(type, mapParameters.get(type)));
        }
    }

    /**
	 * Adds new parameter to the query object. <b>NOTE:</b> If the parameter of the given type already exists - its list of entries will extended with <code>_lstEntries</code>
	 * 
	 * @param _type
	 *            Type of the property to be added
	 * @param _lstEntries
	 *            List of possible values for the property
	 */
    public void addParameter(QueryParameterType _type, List<QueryParameterEntry> _lstEntries) {
        List<QueryParameterEntry> oldLst = mapParameters.get(_type);
        if (oldLst == null) {
            mapParameters.put(_type, _lstEntries);
        } else {
            oldLst.addAll(_lstEntries);
        }
    }

    public List<QueryParameter> getParameters() {
        return queryParameters;
    }

    /**
     * Gets <tt>true</tt> if all the conditions stated by the actor posting the query are to be fullfiled
     * @return Returns <tt>true</tt> if all the conditions stated by the actor posting the query are to be fullfiled
     */
    public boolean isConjunction() {
        return conjunction;
    }

    /**
     * Sets <tt>true</tt> if all the conditions stated by the actor posting the query are to be fullfiled
     * @param _isConjunction <tt>true</tt> if all the conditions stated by the actor posting the query are to be fullfiled
     */
    public void setConjunction(boolean _isConjunction) {
        conjunction = _isConjunction;
    }
}
