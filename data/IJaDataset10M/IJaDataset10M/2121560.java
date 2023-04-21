package com.luzan.db.sql;

import org.apache.commons.lang.StringUtils;
import java.util.LinkedList;

/**
 * WhereClause
 *
 * @author Alexander Bondar
 */
public class WhereClause {

    protected LinkedList<WhereClauseToken> whereClause = new LinkedList<WhereClauseToken>();

    public WhereClause add(WhereClauseToken token) {
        whereClause.add(token);
        return this;
    }

    public String toString() {
        StringBuffer buf = new StringBuffer();
        buf.append("(");
        for (WhereClauseToken token : whereClause) {
            buf.append(token);
            buf.append(" ");
        }
        buf.append(")");
        return buf.toString();
    }

    public static void main(String[] args) {
        WhereClause whereClauseLatLon = new WhereClause();
        WhereClause whereClauseOver = new WhereClause();
        WhereClause whereClauseUser = new WhereClause();
        WhereClause whereClauseRoot = new WhereClause();
        whereClauseLatLon.add(new WhereClauseToken(null, ":sw_lon<lon")).add(new WhereClauseToken("and", ":sw_lat<lat")).add(new WhereClauseToken("and", ":ne_lon>lon")).add(new WhereClauseToken("and", ":ne_lat>lat"));
        whereClauseOver.add(new WhereClauseToken(null, "id not in (select point_b_id from map_point_override where zoom=:zoom)"));
        whereClauseUser.add(new WhereClauseToken(null, "user_id=:user_id"));
        whereClauseRoot.add(new WhereClauseToken(null, whereClauseLatLon)).add(new WhereClauseToken("and", whereClauseOver)).add(new WhereClauseToken("and", whereClauseUser));
        String z = whereClauseRoot.toString();
        System.out.print(z);
    }
}
