package ejb.bprocess.opac.xcql.searchType;

import ejb.bprocess.opac.xcql.*;

/**
 *
 * @author  administrator
 */
public class NameAllSearch implements SearchType {

    /** Creates a new instance of NameAllSearch */
    public NameAllSearch() {
    }

    public java.util.Hashtable execute(java.lang.String[] relation, java.lang.String term, java.util.Hashtable searchLimits) {
        java.util.Hashtable htReturn = new java.util.Hashtable();
        java.util.Vector queries = new java.util.Vector(1, 1);
        queries = getQuery(relation, term, searchLimits, queries);
        htReturn = (new ExecuteQueries()).executeQueries(queries);
        return htReturn;
    }

    public java.util.Vector getQuery(java.lang.String[] relation, java.lang.String term, java.util.Hashtable searchLimits, java.util.Vector queries) {
        queries = (new PersonalNameSearch()).getQuery(relation, term, searchLimits, queries);
        queries = (new PersonalNameSHSearch()).getQuery(relation, term, searchLimits, queries);
        queries = (new CorporateNameSearch()).getQuery(relation, term, searchLimits, queries);
        queries = (new CorporateNameSHSearch()).getQuery(relation, term, searchLimits, queries);
        queries = (new MeetingNameSearch()).getQuery(relation, term, searchLimits, queries);
        queries = (new MeetingNameSHSearch()).getQuery(relation, term, searchLimits, queries);
        return queries;
    }
}
