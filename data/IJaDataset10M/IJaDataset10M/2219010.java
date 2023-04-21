package prajna.semantic.endeca;

import prajna.data.query.*;

/**
 * Transforms a query string into an Endeca-formatted query
 * 
 * @author <a href="http://www.ganae.com/edswing">Edward Swing</a>
 */
public class EndecaQueryTransform implements QueryTransform {

    private String searchField = null;

    /**
     * Get the field specified for the text search
     * 
     * @return the search field
     */
    public String getSearchField() {
        return searchField;
    }

    /**
     * Set the field for the text search. If unset, a search query will search
     * against dimension values
     * 
     * @param field the search field
     */
    public void setSearchField(String field) {
        searchField = field;
    }

    /**
     * Transform the query into an Endeca-formatted query string that can be
     * sent to the Endeca engine. The string will still need to be uuencoded.
     * 
     * @param query the query
     * @return an Endeca-formatted query string
     */
    public String transformQuery(DocFilter query) {
        String xform = transformTerm(query);
        if (searchField == null) {
            throw new IllegalStateException("Must set the search field");
        }
        return "Ntx=mode+matchboolean&Ntk=" + searchField + "&Ntt=" + xform;
    }

    /**
     * Transform a particular term into an Endeca-formatted string. This step
     * transforms the individual terms appropriately
     * 
     * @param term the query term
     * @return the Endeca translation of the term
     */
    private String transformTerm(DocFilter term) {
        String xform = null;
        if (term instanceof SearchTerm) {
            xform = ((SearchTerm) term).getTerm();
        } else if (term instanceof MatchFilter) {
            String field = ((MatchFilter) term).getField();
            String value = ((MatchFilter) term).getValue();
            xform = field + ": " + value;
        } else if (term instanceof NotClause) {
            NotClause cls = (NotClause) term;
            xform = " not " + transformTerm(cls.getNegatedFilter());
        } else if (term instanceof TreeClause) {
            TreeClause cls = (TreeClause) term;
            StringBuffer buf = new StringBuffer("(");
            for (DocFilter subTerm : cls.getTerms()) {
                buf.append(transformTerm(subTerm));
                if (cls.isAnd()) buf.append(cls.isAnd() ? " and " : " or  ");
            }
            buf.setLength(buf.length() - 5);
            buf.append(")");
            xform = buf.toString();
        } else if (term instanceof ProximitySearch) {
            ProximitySearch prox = (ProximitySearch) term;
            xform = prox.getTerm1() + (prox.isOrdered() ? " ONEAR/" : " NEAR/") + prox.getDistance() + " " + prox.getTerm2();
        }
        return xform;
    }
}
