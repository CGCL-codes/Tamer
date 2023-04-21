package org.s3b.service.nlp;

import org.openrdf.sesame.constants.QueryLanguage;

/**
 * This class allows to have both RDF query and the type of the query
 *
 * 
 * @author Sebastian Ryszard Kruk &lt;sebastian.kruk@deri.org&gt;
 */
public class TypedRdfQuery {

    /**
	 * Query language of this query template
	 */
    QueryLanguage qlanguage;

    /**
	 * Query string
	 */
    String query;

    /**
	 * 
	 */
    public TypedRdfQuery(QueryLanguage ql, String _query) {
        this.qlanguage = ql;
        this.query = _query;
    }

    /**
	 * @return Returns the qlanguage.
	 */
    public QueryLanguage getQlanguage() {
        return qlanguage;
    }

    /**
	 * @param qlanguage The qlanguage to set.
	 */
    public void setQlanguage(QueryLanguage _qlanguage) {
        this.qlanguage = _qlanguage;
    }

    /**
	 * @return Returns the query.
	 */
    public String getQuery() {
        return query;
    }

    /**
	 * @param query The query to set.
	 */
    public void setQuery(String _query) {
        this.query = _query;
    }
}
