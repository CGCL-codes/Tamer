package ejb.bprocess.opac.xcql.searchType;

import ejb.bprocess.opac.xcql.*;

/**
 *
 * @author  administrator
 */
public class UniformTitleSHSearch implements SearchType {

    /** Creates a new instance of UniformTitleSHSearch */
    public UniformTitleSHSearch() {
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
        String tables_sh = tables + " ,uniform_title_af ut ,searchable_uniformsh_ass sutsh ";
        String cond_sh = " and ut.uniform_title_id=sutsh.subuniformtitle_id and ut.Library_Id=sutsh.subuniformtitle_libraryid and sutsh.cataloguerecordid=a.cataloguerecordid and sutsh.owner_library_id=a.owner_library_id";
        String query = "";
        if (relationName.equals("exact")) {
            term = ejb.bprocess.db.CurrentDB.getInstance().getCurrentDB().getWithUnicodeConstant(term);
            query = part1 + tables_sh + where + part2 + " and UPPER(ut.Title) like UPPER(" + term + ")" + cond_sh;
            queries.addElement(query);
        } else if (relationName.equals("=")) {
            term = ejb.bprocess.db.CurrentDB.getInstance().getCurrentDB().getWithUnicodeConstant(term);
            query = part1 + tables_sh + where + part2 + " and UPPER(ut.Title) like UPPER(" + term + ")" + cond_sh;
            queries.addElement(query);
        } else if (relationName.equals("any")) {
            java.util.StringTokenizer stk = new java.util.StringTokenizer(term, " ");
            while (stk.hasMoreTokens()) {
                String partTerm = stk.nextToken();
                partTerm = ejb.bprocess.db.CurrentDB.getInstance().getCurrentDB().getWithUnicodeConstant("%" + partTerm + "%");
                query = part1 + tables_sh + where + part2 + " and UPPER(ut.Title) like UPPER(" + partTerm + ")" + cond_sh;
                queries.addElement(query);
            }
        } else if (relationName.equals("all")) {
            java.util.StringTokenizer stk = new java.util.StringTokenizer(term, " ");
            String stx = "";
            while (stk.hasMoreTokens()) {
                String partTerm = stk.nextToken();
                partTerm = ejb.bprocess.db.CurrentDB.getInstance().getCurrentDB().getWithUnicodeConstant("%" + partTerm + "%");
                stx += " and UPPER(ut.Title) like UPPER(" + partTerm + ")";
            }
            query = part1 + tables_sh + where + part2 + stx + cond_sh;
            queries.addElement(query);
        }
        return queries;
    }

    public java.util.Hashtable executeId(java.lang.String[] relation, java.lang.String term, java.util.Hashtable searchLimits) {
        java.util.Hashtable htReturn = new java.util.Hashtable();
        java.util.Vector queries = new java.util.Vector(1, 1);
        queries = getQueryId(relation, term, searchLimits, queries);
        htReturn = (new ExecuteQueries()).executeQueries(queries);
        return htReturn;
    }

    public java.util.Vector getQueryId(java.lang.String[] relation, java.lang.String term, java.util.Hashtable searchLimits, java.util.Vector queries) {
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
        java.util.StringTokenizer stk = new java.util.StringTokenizer(term, "|");
        String Id = "", libId = "";
        while (stk.hasMoreElements()) {
            Id = stk.nextToken();
            libId = stk.nextToken();
        }
        Integer pid = new Integer(Id.trim());
        Integer plibid = new Integer(libId.trim());
        String tables_sh = tables + " ,searchable_uniformsh_ass spsh ";
        String cond_sh = " and spsh.subuniformtitle_id='" + pid + "' and spsh.subuniformtitle_libraryid='" + plibid + "' and spsh.cataloguerecordid=a.cataloguerecordid and spsh.owner_library_id=a.owner_library_id";
        String query = "";
        query = part1 + tables_sh + where + part2 + cond_sh;
        queries.addElement(query);
        return queries;
    }
}
