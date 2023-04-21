package ejb.bprocess.opac.xcql.searchType;

import ejb.bprocess.opac.xcql.*;

/**
 *
 * @author  Administrator
 */
public class CustomIndexSearch implements SearchType {

    private int customIndexId = 0;

    /** Creates a new instance of CustomIndexSearch */
    public CustomIndexSearch() {
    }

    public java.util.Hashtable execute(java.lang.String[] relation, java.lang.String term, java.util.Hashtable searchLimits) {
        java.util.Hashtable htReturn = new java.util.Hashtable();
        java.util.Vector queries = new java.util.Vector(1, 1);
        queries = getQuery(relation, term, searchLimits, queries);
        htReturn = (new ExecuteQueries()).executeQueries(queries);
        return htReturn;
    }

    public java.util.Vector getQuery(java.lang.String[] relation, java.lang.String term, java.util.Hashtable searchLimits, java.util.Vector queries) {
        StandardPartsOfSearchSQL ssp = new StandardPartsOfSearchSQL();
        String relationName = relation[0];
        String relationModifier = relation[1];
        String libraryId = "";
        String bibliographicLevelId = "";
        String materialTypeId = "";
        String language = "";
        java.util.Hashtable htStand = ssp.getStandardPartsOfSearchResult(searchLimits);
        String part1 = htStand.get("part1").toString();
        String tables = htStand.get("tables").toString();
        String where = htStand.get("where").toString();
        String part2 = htStand.get("part2").toString();
        String tables_main = tables + " ,cat_custom_index_data ut ";
        String cond_main = " and ut.cataloguerecordid=b.cataloguerecordid and ut.ownerlibraryid=b.owner_library_id and ut.custom_index_id=" + getCustomIndexId();
        String query = "";
        if (relationName.equals("exact")) {
            term = ejb.bprocess.db.CurrentDB.getInstance().getCurrentDB().getWithUnicodeConstant(term);
            query = part1 + tables_main + where + part2 + " and UPPER(ut.data) like UPPER(" + term + ")" + cond_main;
            queries.addElement(query);
        } else if (relationName.equals("=")) {
            term = ejb.bprocess.db.CurrentDB.getInstance().getCurrentDB().getWithUnicodeConstant(term);
            query = part1 + tables_main + where + part2 + " and UPPER(ut.data) like UPPER(" + term + ")" + cond_main;
            queries.addElement(query);
        } else if (relationName.equals("any")) {
            java.util.StringTokenizer stk = new java.util.StringTokenizer(term, " ");
            while (stk.hasMoreTokens()) {
                String partTerm = stk.nextToken();
                partTerm = ejb.bprocess.db.CurrentDB.getInstance().getCurrentDB().getWithUnicodeConstant("%" + partTerm + "%");
                query = part1 + tables_main + where + part2 + " and UPPER(ut.data) like UPPER(" + partTerm + ")" + cond_main;
                queries.addElement(query);
            }
        } else if (relationName.equals("all")) {
            java.util.StringTokenizer stk = new java.util.StringTokenizer(term, " ");
            String stx = "";
            while (stk.hasMoreTokens()) {
                String partTerm = stk.nextToken();
                partTerm = ejb.bprocess.db.CurrentDB.getInstance().getCurrentDB().getWithUnicodeConstant("%" + partTerm + "%");
                stx += " and UPPER(ut.data) like UPPER(" + partTerm + ")";
            }
            query = part1 + tables_main + where + part2 + stx + cond_main;
            queries.addElement(query);
        }
        return queries;
    }

    /** Getter for property customIndexId.
     * @return Value of property customIndexId.
     *
     */
    public int getCustomIndexId() {
        return customIndexId;
    }

    /** Setter for property customIndexId.
     * @param customIndexId New value of property customIndexId.
     *
     */
    public void setCustomIndexId(int customIndexId) {
        this.customIndexId = customIndexId;
    }
}
