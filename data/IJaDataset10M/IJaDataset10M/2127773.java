package uk.ac.ebi.intact.persistence.dao.query;

import java.io.Serializable;
import java.util.Collection;
import java.util.HashSet;

/**
 * A phrase represent a group of terms
 *
 * @author Bruno Aranda (baranda@ebi.ac.uk)
 * @version $Id: QueryPhrase.java 7540 2007-02-19 10:30:57Z skerrien $
 * @since 1.5
 */
public class QueryPhrase implements Serializable {

    private Collection<QueryTerm> terms;

    public QueryPhrase() {
        this.terms = new HashSet<QueryTerm>();
    }

    public QueryPhrase(Collection<QueryTerm> terms) {
        this.terms = terms;
    }

    public Collection<QueryTerm> getTerms() {
        return terms;
    }

    public void setTerms(Collection<QueryTerm> terms) {
        this.terms = terms;
    }

    public boolean isOnlyWildcard() {
        if (terms != null && terms.size() == 1) {
            return terms.iterator().next().isOnlyWildcard();
        }
        return false;
    }
}
