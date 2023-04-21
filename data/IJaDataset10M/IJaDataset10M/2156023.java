package Query;

import java.util.Vector;
import java.util.regex.Pattern;
import Exception.MalformedQueryException;

/**
 * This is a SELECT query.
 */
public class SelectQuery extends Query {

    private String query;

    protected Vector<String> selection = new Vector<String>();

    protected Vector<String> where = new Vector<String>();

    protected Vector<String> triplets = new Vector<String>();

    public static final String DROITE = "droite";

    public static final String GAUCHE = "gauche";

    public static final String MILIEU = "milieu";

    /**
	 * Parse the SELECT given query.
	 */
    public void parseQuery(String query) throws MalformedQueryException {
        where.removeAllElements();
        selection.removeAllElements();
        triplets.removeAllElements();
        String req = "^SELECT ([\\?[\\w]+ ]+) WHERE ((\\{+((\\?[\\w]+ ?|[\\w]+:[\\w]+ ?){3} ?\\.? ?)+ ?\\}+( UNION )?)+)";
        Pattern pattern = Pattern.compile(req);
        java.util.regex.Matcher m = pattern.matcher(query);
        if (query.matches(req)) {
            while (m.find()) {
                String req_where = m.group(2);
                if (req_where.matches("\\{+((\\?[\\w]+ ?){3} ?\\}+)")) {
                    where.removeAllElements();
                } else {
                    String new_re = req_where.replaceAll("\\{|\\}|UNION|", "");
                    String[] tab_trip = new_re.split(" \\. ");
                    for (int i = 0; i < tab_trip.length; i++) {
                        triplets.add(tab_trip[i]);
                    }
                    String new_req = req_where.replaceAll("\\{|\\}|UNION|\\.", " ");
                    String req_where_final = new_req.replaceAll(" +", " ");
                    String[] tab_where = req_where_final.split(" ");
                    for (int i = 0; i < tab_where.length; i++) {
                        if (tab_where[i].matches("[\\w]+:[\\w]+")) {
                            where.add(tab_where[i]);
                        }
                    }
                }
                String req_select = m.group(1);
                String[] tab_select = req_select.split(" ");
                for (int i = 0; i < tab_select.length; i++) {
                    selection.add(tab_select[i]);
                }
            }
        } else {
            throw new MalformedQueryException(query, "Requete mal formée requete dans Select");
        }
    }

    /**
	 * Rebuild and return query.
	 */
    public String getQuery() {
        return query;
    }

    public void setQuery(String query) {
        this.query = query;
    }

    public Vector<String> getSelection() {
        return selection;
    }

    public Vector<String> getWhere() {
        return where;
    }

    public static SelectQuery createDescribeQuery(Vector<Pair<String, String>> prefix, String elem, String position) {
        SelectQuery query = new SelectQuery();
        String res = "";
        String str_prefix = "";
        for (int i = 0; i < prefix.size(); i++) {
            str_prefix += "PREFIX " + prefix.get(i).getFirst() + ":<" + prefix.get(i).getSecond() + ">\n";
        }
        res += str_prefix + " DESCRIBE ?a ?b WHERE ";
        if (position == DROITE) {
            res += "{?a ?b " + elem + "}";
        } else if (position == MILIEU) {
            res += "{?a " + elem + " ?b}";
        } else if (position == GAUCHE) {
            res += "{" + elem + " ?a ?b}";
        }
        query.setQuery(res);
        return query;
    }

    public static SelectQuery createSimpleSelectQuery(Vector<Pair<String, String>> prefix, String elem, String position) {
        SelectQuery query = new SelectQuery();
        String res = "";
        res += "SELECT ?a ?b WHERE ";
        if (position == DROITE) {
            res += "{?a ?b " + elem + "}";
        } else if (position == MILIEU) {
            res += "{?a " + elem + " ?b}";
        } else if (position == GAUCHE) {
            res += "{" + elem + " ?a ?b}";
        }
        query.setQuery(res);
        return query;
    }

    public static SelectQuery createSimpleSelectQueryWithPrefix(Vector<Pair<String, String>> prefix, String elem, String position) {
        SelectQuery query = new SelectQuery();
        String res = "";
        String str_prefix = "";
        for (int i = 0; i < prefix.size(); i++) {
            str_prefix += "PREFIX " + prefix.get(i).getFirst() + ":<" + prefix.get(i).getSecond() + ">\n";
        }
        res += str_prefix + "SELECT ?a ?b WHERE ";
        if (position == DROITE) {
            res += "{?a ?b " + elem + "}";
        } else if (position == MILIEU) {
            res += "{?a " + elem + " ?b}";
        } else if (position == GAUCHE) {
            res += "{" + elem + " ?a ?b}";
        }
        query.setQuery(res);
        return query;
    }

    public static SelectQuery selectQueryToDescribeQuery(Vector<Pair<String, String>> prefix, SelectQuery query) {
        SelectQuery sq = new SelectQuery();
        String res = "";
        String str_prefix = "";
        for (int i = 0; i < prefix.size(); i++) {
            str_prefix += "PREFIX " + prefix.get(i).getFirst() + ":<" + prefix.get(i).getSecond() + ">\n";
        }
        String fin = query.getQuery().substring(7);
        String str = str_prefix + "DESCRIBE " + fin;
        sq.setQuery(str);
        return sq;
    }

    public static String getQueryWithPrefix(Vector<Pair<String, String>> prefix, SelectQuery q) {
        SelectQuery sq = new SelectQuery();
        String str_prefix = "";
        for (int i = 0; i < prefix.size(); i++) {
            str_prefix += "PREFIX " + prefix.get(i).getFirst() + ":<" + prefix.get(i).getSecond() + ">\n";
        }
        String fin = q.getQuery().substring(7);
        String str = str_prefix + "SELECT " + fin;
        sq.setQuery(str);
        return sq.getQuery();
    }

    public Vector<String> getTriplets() {
        return triplets;
    }
}
