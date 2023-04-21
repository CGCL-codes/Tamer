package prajna.data.query;

import java.util.HashSet;
import java.util.Set;
import prajna.data.DataRecord;
import prajna.data.DocData;

/**
 * Clause representing the a tree of search filters. The TreeClause may have
 * any number of terms as sub-filters. If the And flag is set, the TreeClause
 * matches if all filters match. If the And flag is not set, this filter
 * matches if any of its sub-filters match.
 * 
 * @author <a href="http://www.ganae.com/edswing">Edward Swing</a>
 */
public class TreeClause extends DocFilter {

    private static final long serialVersionUID = -5334296895335503326L;

    private boolean and = true;

    private HashSet<DocFilter> terms = new HashSet<DocFilter>();

    /**
     * Return whether the DataRecord is accepted by this filter.
     * 
     * @param rec the data record to match.
     * @return true if the record matches the filter, false otherwise
     */
    @Override
    public boolean accept(DataRecord rec) {
        return and ? acceptAnd(rec) : acceptOr(rec);
    }

    /**
     * Return whether the DataRecord is accepted by this filter.
     * 
     * @param rec the data record to match.
     * @return true if the record matches the filter, false otherwise
     */
    private boolean acceptAnd(DataRecord rec) {
        for (DocFilter term : terms) {
            if (!term.accept(rec)) {
                return false;
            }
        }
        return true;
    }

    /**
     * Return whether the DataRecord is accepted by this filter.
     * 
     * @param rec the data record to match.
     * @return true if the record matches the filter, false otherwise
     */
    private boolean acceptOr(DataRecord rec) {
        for (DocFilter term : terms) {
            if (term.accept(rec)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Add a term to this tree clause. If the term is itself a tree clause of
     * the same type (AND or OR), this method flattens the clause by adding all
     * of its terms instead
     * 
     * @param term the term to add to this clause
     */
    public void addTerm(DocFilter term) {
        if (term instanceof TreeClause && ((TreeClause) term).isAnd() == and) {
            terms.addAll(((TreeClause) term).getTerms());
        } else {
            terms.add(term);
        }
    }

    /**
     * Add a set of terms to this clause
     * 
     * @param queryTerms the terms to add
     */
    public void addTerms(Set<DocFilter> queryTerms) {
        terms.addAll(queryTerms);
    }

    /**
     * Remove all terms from this TreeClause.
     */
    public void clearTerms() {
        terms.clear();
    }

    /**
     * Determine whether another object is equal to this one. This method
     * returns true if the classes of the two objects are the same, and they
     * contain the same terms.
     * 
     * @param obj the object to compare
     * @return true if the two clauses are equal, false otherwise
     */
    @Override
    public boolean equals(Object obj) {
        boolean match = false;
        if (obj instanceof TreeClause) {
            TreeClause cls = (TreeClause) obj;
            match = (cls.getTerms().equals(terms)) && (and == cls.isAnd());
        }
        return match;
    }

    /**
     * Get the set of terms for this clause
     * 
     * @return the set of terms
     */
    public Set<DocFilter> getTerms() {
        return terms;
    }

    /**
     * Return the hash code for this MatchFilter
     * 
     * @return the hash code
     */
    @Override
    public int hashCode() {
        return (getClass().hashCode() * 31 + terms.hashCode()) * (and ? 1 : -1);
    }

    /**
     * Return whether this tree is an AND tree. An AND tree ANDs its leaves
     * together, while an OR tree ORs them together.
     * 
     * @return whether the tree is an AND tree
     */
    public boolean isAnd() {
        return and;
    }

    /**
     * Return whether this Boolean clause is flat. A clause is flat if it has
     * no Boolean clauses as terms.
     * 
     * @return true if the clause is flat, false otherwise
     */
    public boolean isFlat() {
        boolean flat = true;
        for (DocFilter term : terms) {
            flat &= !(term instanceof TreeClause);
        }
        return flat;
    }

    /**
     * Return whether the represented document is accepted by this filter.
     * 
     * @param doc the document record to match.
     * @return true if the document matches the filter, false otherwise
     */
    @Override
    protected boolean matchDoc(DocData doc) {
        for (DocFilter term : getTerms()) {
            if (!term.accept(doc)) {
                return false;
            }
        }
        return true;
    }

    /**
     * Set whether this tree is an AND tree or an OR tree. An AND tree ANDs its
     * leaves together, while an OR tree ORs them together.
     * 
     * @param andFlag true if the tree is an AND tree. True by default.
     */
    public void setAnd(boolean andFlag) {
        and = andFlag;
    }

    /**
     * Convert this query tree to its string representation
     * 
     * @return the query tree string representation
     */
    @Override
    public String toString() {
        StringBuffer buf = new StringBuffer();
        for (DocFilter term : getTerms()) {
            if (buf.length() > 0) {
                buf.append(and ? " AND " : " OR ");
            }
            if (term instanceof TreeClause) {
                buf.append('(');
            }
            buf.append(term);
            if (term instanceof TreeClause) {
                buf.append(')');
            }
        }
        return buf.toString();
    }

    /**
     * Return a string representation, replacing any clauses with their tags
     * 
     * @return a string representation with clauses replaced by their tags
     */
    public String toTagString() {
        StringBuilder buf = new StringBuilder();
        Set<String> tags = new HashSet<String>();
        for (DocFilter term : terms) {
            if (term.getTag() != null) {
                tags.add(term.getTag());
            } else if (term instanceof TreeClause && !((TreeClause) term).isAnd()) {
                tags.add('(' + term.toString() + ')');
            } else {
                tags.add(term.toString());
            }
        }
        for (String tag : tags) {
            buf.append(tag);
            buf.append(and ? " AND " : " OR  ");
        }
        buf.setLength(buf.length() - 5);
        return buf.toString();
    }
}
